package com.example.beupdated.orderdetails.domain

import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.orderdetails.domain.model.OrderDetails
import kotlinx.coroutines.flow.Flow

interface OrderDetailsRepository {
    suspend fun fetchOrder(orderId: String, userId: String): Flow<CustomResult<OrderDetails>>
    suspend fun cancelOrder(orderId: String, userId: String): CustomResult<Unit>
}