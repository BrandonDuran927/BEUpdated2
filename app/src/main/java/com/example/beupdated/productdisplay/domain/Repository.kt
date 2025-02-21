package com.example.beupdated.productdisplay.domain

import com.example.beupdated.common.utilities.CustomResult
import kotlinx.coroutines.flow.Flow

interface ProductDisplayRepository {
    suspend fun signOutUser() : CustomResult<Unit>
    suspend fun fetchUserData(userUID: String) : Flow<CustomResult<User>>
    suspend fun getProducts() : Flow<CustomResult<List<Product>>>
    suspend fun getProductByName(productName: String) : Flow<CustomResult<Product>>
}