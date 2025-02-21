package com.example.beupdated.order.presentation

sealed class OrderAction {
    data object OnResetState : OrderAction()
    data object OnResetError : OrderAction()
    data class OnFetchOrders(val userId: String) : OrderAction()
}