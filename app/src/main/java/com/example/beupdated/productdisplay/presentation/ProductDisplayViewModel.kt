package com.example.beupdated.productdisplay.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beupdated.authentication.domain.usecases.RetrieveUserUseCase
import com.example.beupdated.authentication.domain.usecases.SignOutUseCase
import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.productdisplay.domain.Product
import com.example.beupdated.productdisplay.domain.ProductDisplayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class ProductDisplayViewModel @Inject constructor(
    private val repository: ProductDisplayRepository,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {
    var state by mutableStateOf(ProductDisplayUIState())

    fun onAction(action: ProductDisplayAction) {
        when (action) {
            ProductDisplayAction.OnLogoutUser -> {
                viewModelScope.launch {
                    when (val result = signOutUseCase.invoke()) {
                        is CustomResult.Success -> {
                            println("Logout successfully!")
                            state = state.copy(userEmail = "")
                        }

                        is CustomResult.Failure -> {
                            state = state.copy(message = result.exception.message.toString())
                        }
                    }
                }
            }

            is ProductDisplayAction.OnFetchUserData -> {
                viewModelScope.launch {
                    when (val result = repository.fetchUserData(action.userUID).first()) {
                        is CustomResult.Success -> {
                            state = state.copy(user = result.data)
                        }

                        is CustomResult.Failure ->
                            println(result.exception.message)
                    }
                }
            }

            ProductDisplayAction.OnFetchProducts -> {
                fetchProducts()
            }

            ProductDisplayAction.OnResetError -> state = state.copy(message = "")

            ProductDisplayAction.OnRefresh -> fetchProducts()
        }
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            delay(1000L)
            try {
                val result = withTimeout(5000) {
                    repository.getProducts().first()
                }
                state = when (result) {
                    is CustomResult.Success -> {
                        val productsByCategory = result.data.groupBy { it.category }
                        println(result.data)
                        state.copy(
                            products = result.data,
                            groupByCategory = productsByCategory,
                            isLoading = false
                        )
                    }

                    is CustomResult.Failure ->
                        state.copy(
                            isLoading = false
                        )
                }
            } catch (e: TimeoutCancellationException) {
                state = state.copy(
                    message = "Internet unstable, please try again later.",
                    isLoading = false
                )
            } catch (e: Exception) {
                state = state.copy(message = e.message.toString(), isLoading = false)
            }
        }
    }
}