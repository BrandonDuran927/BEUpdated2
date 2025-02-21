package com.example.beupdated.productdisplay.data

import com.example.beupdated.authentication.domain.AuthRepository
import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.productdisplay.domain.Product
import com.example.beupdated.productdisplay.domain.ProductDisplayRepository
import com.example.beupdated.productdisplay.domain.ProductRaw
import com.example.beupdated.productdisplay.domain.User
import com.example.beupdated.productdisplay.domain.toProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseDatabase: FirebaseDatabase,
    firestore: FirebaseFirestore,
) : ProductDisplayRepository {

    private val productsCollection = firestore.collection("products")

    override suspend fun signOutUser(): CustomResult<Unit> {
        return try {
            val result = authRepository.signOutUser()
            result
        } catch (e: Exception) {
            CustomResult.Failure(e)
        }
    }

    override suspend fun fetchUserData(userUID: String): Flow<CustomResult<User>> {
        return flow {
            val roomRef = firebaseDatabase.getReference("users").child(userUID)
            val dataSnapshot = roomRef.get().await()
            if (dataSnapshot.exists()) {
                val user = dataSnapshot.getValue(User::class.java)
                if (user != null) {
                    emit(CustomResult.Success(user))
                } else {
                    emit(CustomResult.Failure(Exception("User data is null")))
                }
            } else {
                emit(CustomResult.Failure(Exception("User not found")))
            }
        }.catch { e ->
            e.printStackTrace()
            emit(CustomResult.Failure(e))
        }
    }

    override suspend fun getProducts(): Flow<CustomResult<List<Product>>> {
        return flow<CustomResult<List<Product>>> {
                val snapshot = productsCollection.get().await()
                val products = snapshot.documents.mapNotNull { document ->
                    val rawProduct = document.toObject(ProductRaw::class.java)
                    rawProduct?.toProduct()
                }
                emit(CustomResult.Success(products))
        }.catch { e ->
            emit(CustomResult.Failure(e))
        }
    }

    override suspend fun getProductByName(productName: String): Flow<CustomResult<Product>> {
        return flow {
                val snapshot = productsCollection
                    .whereEqualTo("name", productName)
                    .limit(1)
                    .get()
                    .await()

                if (snapshot.isEmpty) {
                    emit(CustomResult.Failure(Exception("Product not found")))
                    return@flow
                }

                val document = snapshot.documents.first()
                val rawProduct = document.toObject(ProductRaw::class.java)
                val product = rawProduct?.toProduct()

                if (product != null) {
                    emit(CustomResult.Success(product))
                } else {
                    emit(CustomResult.Failure(Exception("Failed to parse product")))
                }
        }.catch { e ->
            emit(CustomResult.Failure(e))
        }
    }

}