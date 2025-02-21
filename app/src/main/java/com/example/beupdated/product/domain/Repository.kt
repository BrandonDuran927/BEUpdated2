package com.example.beupdated.product.domain

import com.example.beupdated.common.utilities.CustomResult
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun savedProduct(
        userId: String,
        productName: String,
        productSize: String,
        productColor: String,
        productPrice: String,
    ): Flow<CustomResult<Unit>>

    suspend fun onReserveProduct(
        userId: String,
        productName: String,
        productSize: String,
        productColor: String,
        productPrice: String,
    ): Flow<CustomResult<Unit>>
}