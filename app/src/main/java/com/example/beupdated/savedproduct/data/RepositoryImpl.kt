package com.example.beupdated.savedproduct.data

import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.savedproduct.domain.SavedProduct
import com.example.beupdated.savedproduct.domain.SavedProductRepository
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
) : SavedProductRepository {
    override suspend fun getSavedProducts(userId: String): Flow<CustomResult<List<SavedProduct>>> {
        return callbackFlow {
            val listenerRegistration = firebaseFirestore.collection("users")
                .document(userId)
                .collection("savedProducts")
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

    override suspend fun decrementSavedProduct(
        userId: String,
        product: SavedProduct
    ): Flow<CustomResult<Unit>> {
        return flow<CustomResult<Unit>> {
            val documentReference = firebaseFirestore.collection("users")
                .document(userId)
                .collection("savedProducts")
                .document(product.productId)

            val documentSnapshot = documentReference.get().await()

            if (documentSnapshot.exists()) {
                println("Raw document of product to delete: ${documentSnapshot.data}")
                documentReference.delete().await()
            }

            emit(CustomResult.Success(Unit))
        }.catch { e ->
            emit(CustomResult.Failure(e))
        }
    }

    override suspend fun incrementSavedProduct(
        userId: String,
        savedAt: String
    ): Flow<CustomResult<Unit>> {
        return flow<CustomResult<Unit>> {
            val querySnapshot = firebaseFirestore.collection("users")
                .document(userId)
                .collection("savedProducts")
                .whereEqualTo("savedAt", savedAt)
                .limit(1)
                .get()
                .await()

            val document = querySnapshot.documents.first()
            val productData = document.data ?: throw Exception("Product data is null.")

            firebaseFirestore
                .collection("users")
                .document(userId)
                .collection("savedProducts")
                .add(productData)
                .await()

            emit(CustomResult.Success(Unit))
        }.catch { e ->
            emit(CustomResult.Failure(e))
        }
    }

    override suspend fun onCheckProduct(
        userId: String,
        productName: String,
        productSize: String?,
        productColor: String,
        isCheck: Boolean
    ): Flow<CustomResult<Unit>> {
        return flow<CustomResult<Unit>> {
            val query = firebaseFirestore.collection("users")
                .document(userId)
                .collection("savedProducts")

            val querySnapshot = if (productSize != null) {
                query.whereEqualTo("productName", productName)
                    .whereEqualTo("productSize", productSize)
                    .whereEqualTo("productColor", productColor)
                    .get()
                    .await()
            } else {
                query.whereEqualTo("productName", productName)
                    .whereEqualTo("productColor", productColor)
                    .get()
                    .await()
            }

            for (document in querySnapshot.documents) {
                document.reference.update("onCheckProduct", isCheck).await()
            }

            emit(CustomResult.Success(Unit))
        }.catch { e ->
            emit(CustomResult.Failure(e))
        }
    }

    override suspend fun unCheckAllProducts(userId: String): Flow<CustomResult<Unit>> {
        return flow<CustomResult<Unit>> {
            val querySnapshot = firebaseFirestore.collection("users")
                .document(userId)
                .collection("savedProducts")
                .whereEqualTo("onCheckProduct", true)
                .get()
                .await()

            for (document in querySnapshot) {
                document.reference.update("onCheckProduct", false).await()
            }

            emit(CustomResult.Success(Unit))
        }.catch { e ->
            emit(CustomResult.Failure(e))
        }
    }

}