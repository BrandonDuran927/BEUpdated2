package com.example.beupdated.search.data

import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.productdisplay.domain.Product
import com.example.beupdated.productdisplay.domain.ProductRaw
import com.example.beupdated.productdisplay.domain.toProduct
import com.example.beupdated.search.domain.SearchRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore
): SearchRepository {
    private val productsCollection = firestore.collection("products")

    override suspend fun searchProducts(query: String): Flow<CustomResult<List<Product>>> {
        return flow {
            if (query.isBlank()) {
                emit(CustomResult.Success(emptyList()))
                return@flow
            }

            val searchQuery = query.trim().lowercase()

            println("Searching for query: $searchQuery")

            try {
                val snapshot = productsCollection.get().await()
                println("Documents found: ${snapshot.size()}")

                val products = snapshot.documents.mapNotNull { document ->
                    try {
                        val productRaw = document.toObject(ProductRaw::class.java)
                        val product = productRaw?.toProduct()
                        product?.takeIf { it.name.lowercase().contains(searchQuery) } // Filter manually
                    } catch (e: Exception) {
                        println("Conversion error for doc ${document.id}: ${e.message}") // Debug log
                        null
                    }
                }

                println("Filtered products: $products") // Debug log
                emit(CustomResult.Success(products))
            } catch (e: Exception) {
                println("Query error: ${e.message}") // Debug log
                emit(CustomResult.Failure(e))
            }
        }.catch { e ->
            println("Flow error: ${e.message}") // Debug log
            emit(CustomResult.Failure(e))
        }.flowOn(Dispatchers.IO)
    }



}