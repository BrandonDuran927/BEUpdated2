package com.example.beupdated.search.presentation

import com.example.beupdated.productdisplay.domain.Product

data class SearchState(
    val message: String = "",
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val products: List<Product> = emptyList()
)
