package com.example.beupdated.savedproduct.domain

data class SavedProduct(
    val productId: String,
    val productName: String,
    val productSize: String,
    val productColor: String,
    val productPrice: String,
    val savedAt: String,
    var quantity: Int,
    val onCheckProduct: Boolean,
)




