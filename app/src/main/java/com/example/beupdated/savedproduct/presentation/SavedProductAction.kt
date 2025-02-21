package com.example.beupdated.savedproduct.presentation

sealed class SavedProductAction {
    data class OnFetchSavedProducts(val userId: String) : SavedProductAction()
    data object OnResetError : SavedProductAction()
    data class OnResetState(val userId: String) : SavedProductAction()
    data class OnIncrementQuantity(val userId: String, val savedAt: String) : SavedProductAction()
    data class OnDecrementQuantity(val userId: String, val productId: String) : SavedProductAction()
    data class OnShowWarningQuantity(val productName: String, val productId: String) : SavedProductAction()
    data class OnCheckProduct(
        val onCheck: Boolean,
        val userId: String,
        val productName: String,
        val productSize: String?,
        val productColor: String
    ): SavedProductAction()
}