package com.example.beupdated.orderdetails.domain.model

data class Order(
    val productColor: String,
    val productId: String,
    val productName: String,
    val productPrice: String,
    val productSize: String,
    val productQuantity: Int
)
