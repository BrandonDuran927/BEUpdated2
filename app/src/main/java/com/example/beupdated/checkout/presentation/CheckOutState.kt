package com.example.beupdated.checkout.presentation

import com.example.beupdated.savedproduct.domain.SavedProduct

data class CheckOutState(
    val message: String = "",
    val isLoading: Boolean = false,
    val currentProductToRemove: String = "",
    val totalPrice: Double = 0.0,
    val checkProducts: List<SavedProduct> = emptyList(),
    val pickUpDate: String = "",
    val pickUpDateLong: Long = 0,
    val showModal: Boolean = false,
    val onPayment: String = "",

    val orderId: String = "",

    val gcashNumber: String = "",
    val gcashAmount: String = "",

    val onPaymentSuccess: Boolean = false,

    val cardNumber: String = "",
    val expiration: String = "",
    val cvv: String = "",
    val nameInCard: String = "",
    val creditCardAmount: String = ""
)
