package com.example.beupdated.checkout.domain

import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.savedproduct.domain.SavedProduct
import kotlinx.coroutines.flow.Flow

interface CheckOutRepository {
    suspend fun retrieveCheckProducts(userId: String) : Flow<CustomResult<List<SavedProduct>>>
    suspend fun removeCheckOutProducts(
        userId: String,
        checkedOutProducts: List<SavedProduct>
    ) : Flow<CustomResult<Unit>>
    suspend fun addCheckOutProducts(
        userId: String,
        checkedOutProducts: List<SavedProduct>,
        dateForPickup: String,
        paymentMethod: String,
    ) : Flow<CustomResult<String>>
}