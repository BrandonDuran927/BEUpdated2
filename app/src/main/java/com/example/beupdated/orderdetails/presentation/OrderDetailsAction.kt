package com.example.beupdated.orderdetails.presentation

import com.example.beupdated.paymentsuccess.domain.Receipt

sealed class OrderDetailsAction {
    data object OnResetState : OrderDetailsAction()
    data object OnResetError : OrderDetailsAction()
    data object OnShowConfirmation : OrderDetailsAction()
    data object OnResetReceiptPath : OrderDetailsAction()

    data class OnFetchedOrder(val orderId: String, val userId: String, val productId: String) : OrderDetailsAction()
    data class OnCancelOrder(val userId: String, val orderId: String) : OrderDetailsAction()
    data class OnGenerateReceipt(val receipt: Receipt) : OrderDetailsAction()
}