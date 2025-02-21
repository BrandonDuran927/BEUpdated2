package com.example.beupdated.orderdetails.domain.model

data class OrderDetails(
    val products: List<Order>,
    val productId: String,
    val pickUpDetails: PickUpDetails,
    val paymentMethod: String,
    val status: String,
    val timeStamp: String,
    val userId: String,
    val approval: Boolean
)


