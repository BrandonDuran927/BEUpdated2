package com.example.beupdated.orderdetails.data

import com.example.beupdated.checkout.presentation.composables.convertMillisToDate
import com.example.beupdated.checkout.presentation.composables.convertTimestampToTime
import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.orderdetails.domain.model.Order
import com.example.beupdated.orderdetails.domain.model.OrderDetails
import com.example.beupdated.orderdetails.domain.OrderDetailsRepository
import com.example.beupdated.orderdetails.domain.model.PickUpDetails
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
) : OrderDetailsRepository {
    override suspend fun fetchOrder(
        orderId: String,
        userId: String
    ): Flow<CustomResult<OrderDetails>> {
        return callbackFlow {
            val listenerRegistration = firebaseFirestore
                .collection("users")
                .document(userId)
                .collection("orders")
                .document(orderId)
                .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(CustomResult.Failure(error))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val productsList = snapshot.get("products") as? List<Map<String, Any>> ?: emptyList()

                    val products = productsList.map { productMap ->
                        Order(
                            productQuantity = (productMap["quantity"] as? Long)?.toInt() ?: 0,
                            productId = productMap["productId"] as? String ?: "",
                            productName = productMap["productName"] as? String ?: "",
                            productSize = productMap["productSize"] as? String ?: "",
                            productColor = productMap["productColor"] as? String ?: "",
                            productPrice = productMap["productPrice"] as? String ?: ""
                        )
                    }

                    val pickUpMap = snapshot.get("pickupDetails") as? Map<String, Any> ?: emptyMap()
                    val pickUpDetails = PickUpDetails(
                        date = pickUpMap["date"] as? String ?: "",
                        time = pickUpMap["time"] as? String ?: "",
                        dateForPickUp = pickUpMap["dateForPickup"] as? String ?: ""
                    )

                    val orderDetails = OrderDetails(
                        approval = snapshot.getBoolean("approval") ?: false,
                        products = products,
                        pickUpDetails = pickUpDetails,
                        paymentMethod = snapshot.getString("paymentMethod") ?: "",
                        status = snapshot.getString("status") ?: "",
                        timeStamp = snapshot.get("timestamp").toString(),
                        userId = snapshot.getString("userId") ?: "",
                        productId = orderId,
                    )

                    println(snapshot.getBoolean("approval"))

                    trySend(CustomResult.Success(orderDetails)) // Emit success with data
                } else {
                    trySend(CustomResult.Failure(Exception("Order not found")))
                }
            }
            awaitClose { listenerRegistration.remove() }
        }.catch { e ->
            emit(CustomResult.Failure(e))
        }
    }

    override suspend fun cancelOrder(orderId: String, userId: String): CustomResult<Unit> {
        return try {
            val orderRef = firebaseFirestore
                .collection("users")
                .document(userId)
                .collection("orders")
                .document(orderId)
                .get()
                .await()

            if (orderRef.exists()) {
                val updates = mapOf(
                    "status" to "cancelled",
                    "pickupDetails.date" to convertMillisToDate(System.currentTimeMillis()), // Clear or update as needed
                    "pickupDetails.time" to convertTimestampToTime(System.currentTimeMillis())
                )

                orderRef.reference.update(updates).await()
                CustomResult.Success(Unit)
            } else {
                CustomResult.Failure(Throwable("Order does not exists"))
            }
        } catch (e: Exception) {
            CustomResult.Failure(e)
        }
    }


}