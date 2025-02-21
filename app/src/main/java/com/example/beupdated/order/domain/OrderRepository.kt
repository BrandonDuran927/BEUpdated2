package com.example.beupdated.order.domain

import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.order.domain.OrderDetails
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun fetchOrders(userId: String) : Flow<CustomResult<List<OrderDetails>>>
}