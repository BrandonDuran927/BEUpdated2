package com.example.beupdated.paymentsuccess.presentation

import com.example.beupdated.paymentsuccess.domain.Receipt

sealed class PaymentAction {
    data object OnResetReceiptPath : PaymentAction()
    data object OnResetState : PaymentAction()
    data object OnResetError : PaymentAction()
    data class OnDownloadReceipt(
        val receipt: Receipt
    ) : PaymentAction()
}