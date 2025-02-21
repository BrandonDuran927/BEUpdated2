package com.example.beupdated.savedproduct.presentation

import com.example.beupdated.savedproduct.domain.SavedProduct

data class SavedProductState(
    val savedProducts: List<SavedProduct> = emptyList(),
    val isLoading: Boolean = false,
    val message: String = "",
    val currentProductToRemove: String = "",
    val checkProducts: List<SavedProduct> = emptyList()
)
