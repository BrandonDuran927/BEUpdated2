package com.example.beupdated.checkout.presentation

import com.example.beupdated.savedproduct.presentation.SavedProductAction

sealed class CheckOutAction {
    data object OnResetError : CheckOutAction()
    data object OnResetState : CheckOutAction()
    data object OnShowModal : CheckOutAction()
    data object OnDismissModal : CheckOutAction()
    data object OnDismissPayment : CheckOutAction()

    data class OnChangeExpiration(val expiration: String) : CheckOutAction()
    data class OnChangeCardNumber(val cardNumber: String) : CheckOutAction()

    data class VerifyPayment(
        val paymentMethod: String,
        val userId: String
    ) : CheckOutAction()

    data class OnChangeGcashNumber(val gcashNumber: String) : CheckOutAction()
    data class OnChangeAmount(val amount: String) : CheckOutAction()
    data class OnShowMessage(val message: String) : CheckOutAction()
    data class OnRetrieveCheckProducts(val userId: String) : CheckOutAction()
    data class OnIncrementQuantity(val userId: String, val savedAt: String) : CheckOutAction()
    data class OnDecrementQuantity(val userId: String, val productId: String) : CheckOutAction()
    data class OnChangeDateLong(val date: Long?) : CheckOutAction()
    data class OnPaymentMethod(val selectedOption: String) : CheckOutAction()
    data class OnChangeCvv(val cvv: String) : CheckOutAction()
    data class OnChangeNameInCard(val cardName: String) : CheckOutAction()
    data class OnChangeCreditCardAmount(val amount: String) : CheckOutAction()
}