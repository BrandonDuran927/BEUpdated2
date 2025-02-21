package com.example.beupdated.paymentsuccess.presentation

data class PaymentState(
    val isLoading: Boolean = false,
    val message: String = "",

    val receiptPath: String = ""
)