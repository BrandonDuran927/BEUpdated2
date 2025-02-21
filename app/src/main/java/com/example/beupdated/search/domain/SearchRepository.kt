package com.example.beupdated.search.domain

import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.productdisplay.domain.Product
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchProducts(query: String): Flow<CustomResult<List<Product>>>
}