package com.example.beupdated.orderdetails.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.orderdetails.domain.OrderDetailsRepository
import com.example.beupdated.paymentsuccess.domain.Receipt
import com.example.beupdated.paymentsuccess.domain.usecase.GenerateReceiptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val repository: OrderDetailsRepository,
    private val generateReceiptUseCase: GenerateReceiptUseCase
): ViewModel() {
    var state by mutableStateOf(OrderDetailsState())
        private set

    fun onAction(action: OrderDetailsAction) {
        when (action) {
            OrderDetailsAction.OnResetState -> state = OrderDetailsState()
            is OrderDetailsAction.OnFetchedOrder -> fetchOrder(userId = action.userId, orderId = action.orderId, productId = action.productId)
            OrderDetailsAction.OnResetError -> state = state.copy(message = "")
            is OrderDetailsAction.OnCancelOrder -> cancelOrder(orderId = action.orderId, userId = action.userId)
            OrderDetailsAction.OnShowConfirmation -> state = state.copy(message = "Are you sure you want to cancel your order?")
            OrderDetailsAction.OnResetReceiptPath -> state = state.copy(receiptPath = "")
            is OrderDetailsAction.OnGenerateReceipt -> generateReceipt(receipt = action.receipt)
        }
    }

    private fun fetchOrder(orderId: String, userId: String, productId: String) {
        viewModelScope.launch {
            delay(500L)
            repository.fetchOrder(orderId, userId).collect{ result ->
                state = when (result) {
                    is CustomResult.Success -> {
                        println(result.data)
                        if (productId.isNotEmpty()) {
                            val orderDetail = result.data.products.filter { it.productId == productId }
                            val total = orderDetail
                                .sumOf { it.productPrice.toDouble() * it.productQuantity }
                            state.copy(orderDetail = result.data.copy(products = orderDetail), totalPrice = total, approval = result.data.approval)
                        } else {
                            val total = result.data.products.sumOf { it.productPrice.toDouble() * it.productQuantity }
                            state.copy(orderDetail = result.data, totalPrice = total, approval = result.data.approval)
                        }
                    }

                    is CustomResult.Failure -> state.copy(message = result.exception.message.toString())
                }
            }
        }
    }

    private fun cancelOrder(orderId: String, userId: String) {
        viewModelScope.launch {
            when (val result = repository.cancelOrder(orderId = orderId, userId = userId)) {
                is CustomResult.Success -> {
                    state = state.copy(message = "Order cancelled successfully")
                }
                is CustomResult.Failure -> state = state.copy(message = result.exception.message.toString())
            }
        }
    }

    private fun generateReceipt(receipt: Receipt) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            generateReceiptUseCase(receipt).collectLatest { result ->
                state = when (result) {
                    is CustomResult.Success -> {
                        state.copy(
                            receiptPath = result.data,
                            isLoading = false
                        )
                    }

                    is CustomResult.Failure -> state.copy(
                        message = result.exception.message.toString(),
                        isLoading = false
                    )
                }
            }
        }
    }
}