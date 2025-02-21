package com.example.beupdated.checkout.data

import com.example.beupdated.checkout.domain.CheckOutRepository
import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.savedproduct.domain.SavedProduct
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : CheckOutRepository {
    override suspend fun retrieveCheckProducts(userId: String): Flow<CustomResult<List<SavedProduct>>> {
        return callbackFlow {
            val listenerRegistration = firebaseFirestore.collection("users")
                .document(userId)
                .collection("savedProducts")
                .whereEqualTo("onCheckProduct", true) // Filter for checked products
                .addSnapshotListener { querySnapshot, e ->
                    if (e != null) {
                        trySend(CustomResult.Failure(e))
                        return@addSnapshotListener
                    }

                    if (querySnapshot == null || querySnapshot.isEmpty) {
                        trySend(CustomResult.Success(emptyList()))
                        return@addSnapshotListener
                    }

                    val productMap = mutableMapOf<String, SavedProduct>()

                    for (document in querySnapshot.documents) {
                        val productName = document.getString("productName") ?: ""
                        val productSize = document.getString("productSize") ?: ""
                        val productColor = document.getString("productColor") ?: ""
                        val productPrice = document.getString("productPrice") ?: ""
                        val savedAt = document.getString("savedAt") ?: ""
                        val onCheckProduct = document.getBoolean("onCheckProduct") ?: false
                        val productId = document.id // Retrieve the document ID

                        val key = "$productName|$productSize|$productColor"

                        if (productMap.containsKey(key)) {
                            productMap[key]!!.quantity += 1
                        } else {
                            productMap[key] = SavedProduct(
                                productId = productId,
                                productName = productName,
                                productSize = productSize,
                                productColor = productColor,
                                productPrice = productPrice,
                                savedAt = savedAt,
                                onCheckProduct = onCheckProduct,
                                quantity = 1
                            )
                        }
                    }

                    println("Result in getSavedProduct: ${productMap.values.toList()}")

                    trySend(CustomResult.Success(productMap.values.toList().sortedByDescending { it.savedAt }))
                }
            awaitClose { listenerRegistration.remove() }
        }.catch { e ->
            emit(CustomResult.Failure(e))
        }
    }

    override suspend fun removeCheckOutProducts(
        userId: String,
        checkedOutProducts: List<SavedProduct>
    ): Flow<CustomResult<Unit>> {
        return flow<CustomResult<Unit>> {
            val savedProductsRef = firebaseFirestore
                .collection("users")
                .document(userId)
                .collection("savedProducts")

            val batch = firebaseFirestore.batch()

            checkedOutProducts.forEach { product ->
                val querySnapshot = if (product.productSize.isEmpty()) {
                    savedProductsRef
                        .whereEqualTo("productName", product.productName)
                        .whereEqualTo("productColor", product.productColor)
                        .whereEqualTo("productSize", "")
                        .get().await()
                } else {
                    savedProductsRef
                        .whereEqualTo("productName", product.productName)
                        .whereEqualTo("productColor", product.productColor)
                        .whereEqualTo("productSize", product.productSize)
                        .get().await()
                }

                querySnapshot.documents.forEach { doc ->
                    batch.delete(doc.reference)
                }
            }

            batch.commit().await()
            emit(CustomResult.Success(Unit))
        }.catch { e ->
            emit(CustomResult.Failure(e))
        }
    }
    // TODO: Decrement the stocks of a checkout product in firestore

    override suspend fun addCheckOutProducts(
        userId: String,
        checkedOutProducts: List<SavedProduct>,
        dateForPickup: String,
        paymentMethod: String,
    ): Flow<CustomResult<String>> {
        return flow {
            try {
                val orderRef = firebaseFirestore
                    .collection("users")
                    .document(userId)

                println("Debugging checkout:")
                println("User ID: $userId")
                println("Products to checkout: ${checkedOutProducts.map { it.productId }}")

                val orderId = orderRef.collection("orders").document().id

                checkedOutProducts.forEach { product ->
                    println("Processing product: ${product.productId} - ${product.productName}")

                    val productRef = firebaseFirestore.collection("products").document(productIdHelper(product.productName))

                    val productExists = productRef.get().await().exists()
                    if (!productExists) {
                        println("Product not found in database: ${product.productId}")
                        throw Exception("Product ${product.productId} (${product.productName}) not found in database")
                    }

                    firebaseFirestore.runTransaction { transaction ->
                        val productSnapshot = transaction.get(productRef)

                        val currentStock = productSnapshot.getLong("stockQuantity")?.toInt()
                            ?: throw Exception("Stock quantity not found for product ${product.productName}")

                        println("Current stock for ${product.productName}: $currentStock")
                        println("Requested quantity: ${product.quantity}")

                        if (currentStock < product.quantity) {
                            throw Exception("Insufficient stock for ${product.productName} (Available: $currentStock, Requested: ${product.quantity})")
                        }

                        val newStock = currentStock - product.quantity
                        transaction.update(productRef, "stockQuantity", newStock)
                        println("Updated stock to: $newStock")
                    }.await()
                }

                val orderData = hashMapOf(
                    "products" to checkedOutProducts.map { product ->
                        mapOf(
                            "productId" to product.productId,
                            "productName" to product.productName,
                            "productSize" to product.productSize,
                            "productColor" to product.productColor,
                            "productPrice" to product.productPrice,
                            "quantity" to product.quantity,
                            "savedAt" to product.savedAt
                        )
                    },
                    "timestamp" to System.currentTimeMillis(),
                    "userId" to userId,
                    "status" to "pending",
                    "paymentMethod" to paymentMethod,
                    "pickupDetails" to mapOf(
                        "dateForPickup" to dateForPickup,
                        "date" to "",
                        "time" to ""
                    ),
                    "approval" to false
                )

                orderRef.collection("orders").document(orderId).set(orderData).await()

                emit(CustomResult.Success(orderId))
            } catch (e: Exception) {
                println("Error during checkout: ${e.message}")
                println("Stack trace: ${e.stackTrace.joinToString("\n")}")
                emit(CustomResult.Failure(e))
            }
        }.catch { e ->
            println("Flow error: ${e.message}")
            emit(CustomResult.Failure(e))
        }
    }

    private fun productIdHelper(productName: String) : String{
         return when (productName) {
             "Black t-shirt" -> "black-ts"
             "Go for the B/G t-shirt" -> "blue-gold-ts"
             "Hydro coffee" -> "hydro-coffee"
             "Plastic water bottle" -> "plastic-water-bottle"
             "Sports bag" -> "sports-bag"
             "Tote bag" -> "tote-bag"
             "Tourism suit" -> "tourism-suit"
             "Traditional polo for female" -> "traditional-female"
             "Traditional female polo for SHS" -> "traditional-female-shs"
             "Traditional polo for male" -> "traditional-male"
             "Traditional male polo for SHS" -> "traditional-male-shs"
             "Tumbler" -> "tumbler"
             else -> "white-ts"
         }
    }

}