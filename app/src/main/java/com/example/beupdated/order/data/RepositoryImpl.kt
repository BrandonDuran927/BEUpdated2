package com.example.beupdated.order.data

import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.order.domain.Order
import com.example.beupdated.order.domain.OrderRepository
import com.example.beupdated.order.domain.OrderDetails
import com.example.beupdated.orderdetails.domain.model.PickUpDetails
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : OrderRepository {
    override suspend fun fetchOrders(userId: String): Flow<CustomResult<List<OrderDetails>>> {
        return callbackFlow<CustomResult<List<OrderDetails>>> {
            val listenerRegistration = firebaseFirestore
                .collection("users")
                .document(userId)
                .collection("orders")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(CustomResult.Failure(error))
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val ordersList = snapshot.documents.mapNotNull { doc ->
                            try {
                                val productsList = doc.get("products") as? List<Map<String, Any>> ?: emptyList()

                                val products = productsList.map { productMap ->
                                    Order(
                                        productQuantity = (productMap["quantity"] as? Long)?.toInt() ?: 0,
                                        productId = productMap["productId"] as? String ?: "",
                                        productName = productMap["productName"] as? String ?: "",
                                        productSize = productMap["productSize"] as? String ?: "",
                                        productColor = productMap["productColor"] as? String ?: "",
                                        productPrice = productMap["productPrice"] as? String ?: "",
                                        orderId = doc.id,
                                        status = doc.getString("status") ?: ""
                                    )
                                }

                                val pickUpMap = doc.get("pickupDetails") as? Map<String, Any> ?: emptyMap()
                                val pickUpDetails = PickUpDetails(
                                    date = pickUpMap["date"] as? String ?: "",
                                    time = pickUpMap["time"] as? String ?: "",
                                    dateForPickUp = pickUpMap["dateForPickup"] as? String ?: ""
                                )

                                OrderDetails(
                                    products = products,
                                    pickUpDetails = pickUpDetails,
                                    paymentMethod = doc.getString("paymentMethod") ?: "",
                                    status = doc.getString("status") ?: "",
                                    timeStamp = doc.get("timestamp").toString(),
                                    userId = doc.getString("userId") ?: "",
                                    productId = doc.id,
                                    approval = doc.getBoolean("approval") ?: false
                                )
                            } catch (e: Exception) {
                                null
                            }
                        }
                        trySend(CustomResult.Success(ordersList.sortedByDescending { it.timeStamp }))
                    } else {
                        println("Empty orders")
                        trySend(CustomResult.Success(emptyList()))
                    }
                }

            awaitClose { listenerRegistration.remove() }
        }.catch { e ->
            emit(CustomResult.Failure(e))
        }
    }

}