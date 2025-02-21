package com.example.beupdated.product.data

import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.product.domain.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : ProductRepository {
    override suspend fun savedProduct(
        userId: String, productName: String, productSize: String, productColor: String, productPrice: String
    ): Flow<CustomResult<Unit>> {
        return flow<CustomResult<Unit>> {
            println("The ID of the user is: $userId")
            val savedProduct = hashMapOf(
                "userId" to userId,
                "productName" to productName,
                "productSize" to productSize,
                "productColor" to productColor,
                "savedAt" to System.currentTimeMillis().toString(),
                "productPrice" to productPrice,
                "onCheckProduct" to false
            )

            firebaseFirestore
                .collection("users")
                .document(userId)
                .collection("savedProducts")
                .add(savedProduct)
                .await()

            emit(CustomResult.Success(Unit))
        }.catch { e ->
            emit(CustomResult.Failure(e))
        }
    }

    override suspend fun onReserveProduct(
        userId: String,
        productName: String,
        productSize: String,
        productColor: String,
        productPrice: String
    ): Flow<CustomResult<Unit>> {
        return flow<CustomResult<Unit>> {
            val savedProduct = hashMapOf(
                "userId" to userId,
                "productName" to productName,
                "productSize" to productSize,
                "productColor" to productColor,
                "savedAt" to System.currentTimeMillis().toString(),
                "productPrice" to productPrice,
                "onCheckProduct" to true
            )

            firebaseFirestore
                .collection("users")
                .document(userId)
                .collection("savedProducts")
                .add(savedProduct)
                .await()

            emit(CustomResult.Success(Unit))
        }.catch { e ->
            emit(CustomResult.Failure(e))
        }
    }
}