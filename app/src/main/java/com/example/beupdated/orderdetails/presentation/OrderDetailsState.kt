package com.example.beupdated.orderdetails.presentation

import com.example.beupdated.orderdetails.domain.model.OrderDetails

data class OrderDetailsState(
    val isLoading: Boolean = false,
    val message: String = "",

    val orderDetail: OrderDetails? = null,
    val totalPrice: Double = 0.0,
    val approval: Boolean = false,

    val receiptPath: String = ""
)
