package com.example.beupdated.product.presentation

sealed class ProductAction {
    data class OnLoadProduct(val productName: String, val userId: String) : ProductAction()
    data class OnChangedSize(val size: String) : ProductAction()
    data class OnChangedColor(val color: String) : ProductAction()
    data object OnSaveProduct : ProductAction()
    data object OnResetProductSaved : ProductAction()
    data object OnResetError : ProductAction()
    data object OnResetState : ProductAction()
    data object OnReserveProduct : ProductAction()
}