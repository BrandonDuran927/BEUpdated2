package com.example.beupdated.savedproduct.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.savedproduct.domain.SavedProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedProductViewModel @Inject constructor(
    private val repository: SavedProductRepository
) : ViewModel() {
    var state by mutableStateOf(SavedProductState())
        private set

    fun onAction(action: SavedProductAction) {
        when (action) {
            is SavedProductAction.OnFetchSavedProducts -> {
                fetchSavedProducts(action.userId)
            }

            SavedProductAction.OnResetError -> state = state.copy(message = "")

            is SavedProductAction.OnIncrementQuantity -> {
                incrementSavedProduct(action.userId, action.savedAt)
            }

            is SavedProductAction.OnDecrementQuantity -> {
                decrementSavedProduct(action.userId, action.productId)
            }

            is SavedProductAction.OnShowWarningQuantity ->
                state =
                    state.copy(
                        message = "Are you sure you want to remove this item from your saved products?",
                        currentProductToRemove = action.productId
                    )

            is SavedProductAction.OnCheckProduct -> {
                onCheckProduct(
                    userId = action.userId,
                    isCheck = action.onCheck,
                    productName = action.productName,
                    productSize = action.productSize,
                    productColor = action.productColor
                )
            }

            is SavedProductAction.OnResetState -> {
                state = SavedProductState()
                viewModelScope.launch {
                    repository.unCheckAllProducts(userId = action.userId).first()
                }
            }
        }
    }

    private fun fetchSavedProducts(userId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            delay(1500L)
            repository.getSavedProducts(
                userId = userId
            ).collect { result ->
                state = when (result) {
                    is CustomResult.Success -> {
                        println("Fetched saved products: ${result.data}")
                        state.copy(savedProducts = result.data, isLoading = false)
                    }

                    is CustomResult.Failure -> state.copy(message = result.exception.message.toString(), isLoading = false)
                }
            }

        }
    }

    private fun decrementSavedProduct(userId: String, productId: String) {
        viewModelScope.launch {
            val savedProduct = state.savedProducts.find { it.productId == productId }

            println("Product to remove is ${savedProduct?.productId}")

            if (savedProduct != null) {
                val result = repository.decrementSavedProduct(
                    userId = userId,
                    product = savedProduct
                ).first()

                when (result) {
                    is CustomResult.Success -> {
                        state = state.copy(message = "", currentProductToRemove = "")
                    }

                    is CustomResult.Failure -> {
                        state = state.copy(message = result.exception.message.toString())
                    }
                }
            }
        }
    }

    private fun incrementSavedProduct(userId: String, savedAt: String) {
        viewModelScope.launch {
            println("Product to add is $savedAt")


            val result = repository.incrementSavedProduct(
                userId = userId,
                savedAt = savedAt
            ).first()

            when (result) {
                is CustomResult.Success -> {
                    println("Increment quantity success")
                    state = state.copy(currentProductToRemove = "")
                }

                is CustomResult.Failure -> {
                    state = state.copy(message = result.exception.message.toString())
                }
            }
        }
    }

    private fun onCheckProduct(
        userId: String,
        productName: String,
        productSize: String?,
        productColor: String,
        isCheck: Boolean
    ) {
        viewModelScope.launch {
            val result = repository.onCheckProduct(
                userId = userId,
                isCheck = isCheck,
                productName = productName,
                productSize = productSize,
                productColor = productColor
            ).first()

            when (result) {
                is CustomResult.Success -> {
                    val checkedProducts = state.savedProducts.filter { it.onCheckProduct }
                    state = state.copy(checkProducts = checkedProducts)
                    println("onCheckProduct success")
                }

                is CustomResult.Failure -> state = state.copy(message = "Error in onCheckProduct: ${result.exception.message.toString()}")
            }
        }
    }
}