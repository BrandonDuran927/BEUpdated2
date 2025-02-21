package com.example.beupdated.paymentsuccess.domain

data class Receipt(
    val orderId: String,
    val pickUpDate: String,
    val status: String = "pending" // Default status is "pending"
)
