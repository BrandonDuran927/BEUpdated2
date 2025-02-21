package com.example.beupdated.order.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.order.domain.Order
import com.example.beupdated.order.domain.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {
    var state by mutableStateOf(OrderState())
        private set

    fun onAction(action: OrderAction) {
        when (action) {
            OrderAction.OnResetState -> state = OrderState()
            OrderAction.OnResetError -> state = state.copy(message = "")
            is OrderAction.OnFetchOrders -> fetchOrders(action.userId)
        }
    }

    private fun fetchOrders(userId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            delay(500L)
            repository.fetchOrders(userId).collectLatest { result ->
                when (result) {
                    is CustomResult.Success -> {
                        val allProducts: List<Order> = result.data.flatMap { orderDetails ->
                            orderDetails.products
                        }
                        println("Orders: ${result.data},\nProducts: $allProducts")
                        state = state.copy(orders = result.data, isLoading = false, allProducts = allProducts)
                    }

                    is CustomResult.Failure -> state =
                        state.copy(message = result.exception.message.toString(), isLoading = false)
                }
            }
        }
    }
}
