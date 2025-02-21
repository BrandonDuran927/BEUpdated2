package com.example.beupdated.paymentsuccess.presentation


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.paymentsuccess.domain.PaymentRepository
import com.example.beupdated.paymentsuccess.domain.Receipt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: PaymentRepository
) : ViewModel() {
    var state by mutableStateOf(PaymentState())
        private set

    fun onAction(action: PaymentAction) {
        when (action) {
            is PaymentAction.OnDownloadReceipt -> downloadReceipt(action.receipt)
            PaymentAction.OnResetReceiptPath -> state = state.copy(receiptPath = "")
            PaymentAction.OnResetState -> state = PaymentState()
            PaymentAction.OnResetError -> state = state.copy(message = "")
        }
    }

    private fun downloadReceipt(receipt: Receipt) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            repository.generateReceipt(receipt).collectLatest { result ->
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
