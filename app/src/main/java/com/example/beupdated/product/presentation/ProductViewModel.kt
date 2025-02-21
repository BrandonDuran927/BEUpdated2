package com.example.beupdated.product.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.product.domain.ProductRepository
import com.example.beupdated.productdisplay.domain.usecases.GetProductByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductByNameUseCase: GetProductByNameUseCase,
    private val repository: ProductRepository
) : ViewModel() {
    var state by mutableStateOf(ProductState())
        private set

    fun onAction(action: ProductAction) {
        when (action) {
            is ProductAction.OnLoadProduct -> {
                state = state.copy(productName = action.productName, userId = action.userId)
                loadProduct()
            }

            is ProductAction.OnChangedColor -> {
                state = state.copy(selectedColor = action.color)
            }

            is ProductAction.OnChangedSize -> {
                state = state.copy(selectedSize = action.size)
            }

            ProductAction.OnSaveProduct -> {
                viewModelScope.launch {
                    state = state.copy(isLoading = true)
                    try {
                        val result = withTimeout(5000){
                            repository.savedProduct(
                                userId = state.userId,
                                productName = state.productName,
                                productSize = state.selectedSize,
                                productColor = state.selectedColor,
                                productPrice = state.product?.price.toString()
                            ).first()
                        }
                        state = when (result) {
                            is CustomResult.Success -> {
                                state.copy(
                                    productSavedSuccessfully = true,
                                    isLoading = false,
                                    selectedColor = "Select color",
                                    selectedSize = "Select size"
                                )
                            }

                            is CustomResult.Failure -> state.copy(message = result.exception.message.toString(), isLoading = false)
                        }
                    } catch (e: TimeoutCancellationException) {
                        state = state.copy(message = "Internet unstable, please try again later.", isLoading = false)
                    } catch (e: Exception) {
                        state = state.copy(message = e.message.toString(), isLoading = false)
                    }
                }
            }

            ProductAction.OnResetProductSaved -> state =
                state.copy(productSavedSuccessfully = false)

            ProductAction.OnResetError -> state = state.copy(message = "")

            ProductAction.OnResetState -> state = ProductState()

            ProductAction.OnReserveProduct -> reserveProduct(
                userId = state.userId,
                productName = state.productName,
                productSize = state.selectedSize,
                productColor = state.selectedColor,
                productPrice = state.product?.price.toString()
            )
        }
    }

    private fun loadProduct() {
        viewModelScope.launch {
            delay(1000L)
            when (val result = getProductByNameUseCase.invoke(state.productName).first()) {
                is CustomResult.Success -> {
                    state = state.copy(
                        product = result.data,
                        selectedColor = "Select color",
                        selectedSize = "Select size"
                    )
                    println("Product: ${result.data}")
                }

                is CustomResult.Failure -> state =
                    state.copy(message = result.exception.message.toString())
            }
        }
    }

    private fun reserveProduct(
        userId: String,
        productName: String,
        productSize: String,
        productColor: String,
        productPrice: String
    ) {
        viewModelScope.launch {
            val result = repository.onReserveProduct(
                userId = userId,
                productName = productName,
                productSize = productSize,
                productColor = productColor,
                productPrice = productPrice
            ).first()

            when (result) {
                is CustomResult.Success -> state = state.copy(reserveSuccess = true)
                is CustomResult.Failure -> state = state.copy(message = result.exception.message.toString())
            }
        }
    }
}