package com.example.beupdated.savedproduct.domain

import com.example.beupdated.common.utilities.CustomResult
import kotlinx.coroutines.flow.Flow

interface SavedProductRepository {
    suspend fun getSavedProducts(userId: String): Flow<CustomResult<List<SavedProduct>>>
    suspend fun decrementSavedProduct(userId: String, product: SavedProduct): Flow<CustomResult<Unit>>
    suspend fun incrementSavedProduct(userId: String, savedAt: String): Flow<CustomResult<Unit>>
    suspend fun onCheckProduct(
        userId: String,
        productName: String,
        productSize: String?,
        productColor: String,
        isCheck: Boolean
    ): Flow<CustomResult<Unit>>
    suspend fun unCheckAllProducts(userId: String): Flow<CustomResult<Unit>>
}