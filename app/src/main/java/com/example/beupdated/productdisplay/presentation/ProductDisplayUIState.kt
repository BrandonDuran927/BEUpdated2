package com.example.beupdated.productdisplay.presentation

import com.example.beupdated.productdisplay.domain.Product
import com.example.beupdated.productdisplay.domain.User

data class ProductDisplayUIState(
    val userEmail: String = "",
    val isLoading: Boolean = false,
    val message: String = "",

    val user: User? = null,
    val products: List<Product> = emptyList(),
    val groupByCategory: Map<String, List<Product>> = emptyMap(),

    val isRefreshing: Boolean = false
)
