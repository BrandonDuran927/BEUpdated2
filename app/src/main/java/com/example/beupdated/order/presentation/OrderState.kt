package com.example.beupdated.order.presentation

import com.example.beupdated.order.domain.Order
import com.example.beupdated.order.domain.OrderDetails

data class OrderState(
    val orders: List<OrderDetails> = emptyList(),
    val allProducts: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val message: String = ""
)
