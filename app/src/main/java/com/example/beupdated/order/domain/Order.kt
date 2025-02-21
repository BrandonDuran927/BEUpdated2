package com.example.beupdated.order.domain

data class Order(
    val productColor: String,
    val productId: String,
    val orderId: String,
    val productName: String,
    val productPrice: String,
    val productSize: String,
    val productQuantity: Int,
    val status: String
)
