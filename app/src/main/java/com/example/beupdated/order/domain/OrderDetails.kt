package com.example.beupdated.order.domain

import com.example.beupdated.orderdetails.domain.model.PickUpDetails

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
