package com.example.beupdated.productdisplay.presentation

sealed interface ProductDisplayAction {
    data object OnLogoutUser : ProductDisplayAction
    data class OnFetchUserData(val userUID: String) : ProductDisplayAction
    data object OnFetchProducts : ProductDisplayAction
    data object OnResetError : ProductDisplayAction
    data object OnRefresh : ProductDisplayAction
}