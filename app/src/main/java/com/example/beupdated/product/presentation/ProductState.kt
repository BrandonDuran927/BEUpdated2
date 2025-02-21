package com.example.beupdated.product.presentation

import com.example.beupdated.productdisplay.domain.Product

data class ProductState(
    val userId: String = "",
    val productName: String = "",
    val product: Product? = null,
    val isLoading: Boolean = false,
    val message: String = "",
    val selectedColor: String = "",
    val selectedSize: String = "",
    val reserveSuccess: Boolean = false,

    val productSavedSuccessfully: Boolean = false
)
