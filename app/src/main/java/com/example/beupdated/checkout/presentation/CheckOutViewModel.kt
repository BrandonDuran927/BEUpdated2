package com.example.beupdated.checkout.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beupdated.checkout.domain.CheckOutRepository
import com.example.beupdated.checkout.presentation.composables.convertMillisToDate
import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.registration.domain.usecases.ValidatePhoneNumberUseCase
import com.example.beupdated.savedproduct.domain.DecrementProductUseCase
import com.example.beupdated.savedproduct.domain.IncrementProductUseCase
import com.example.beupdated.savedproduct.domain.SavedProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckOutViewModel @Inject constructor(
    private val repository: CheckOutRepository,
    private val decrementProductUseCase: DecrementProductUseCase,
    private val incrementProductUseCase: IncrementProductUseCase,
    private val phoneNumberUseCase: ValidatePhoneNumberUseCase
) : ViewModel() {
    var state by mutableStateOf(CheckOutState())
        private set

    fun onAction(action: CheckOutAction) {
        when (action) {
            is CheckOutAction.OnRetrieveCheckProducts -> retrieveCheckProducts(action.userId)
            is CheckOutAction.OnDecrementQuantity -> decrementSavedProduct(
                userId = action.userId,
                productId = action.productId
            )

            is CheckOutAction.OnIncrementQuantity -> incrementSavedProduct(
                userId = action.userId,
                savedAt = action.savedAt
            )

            CheckOutAction.OnResetError -> state = state.copy(message = "")
            CheckOutAction.OnResetState -> state = CheckOutState()
            is CheckOutAction.OnChangeDateLong -> {
                state = state.copy(
                    pickUpDateLong = action.date ?: 0,
                    pickUpDate = convertMillisToDate(state.pickUpDateLong)
                )
            }

            CheckOutAction.OnDismissModal -> state = state.copy(showModal = false)
            CheckOutAction.OnShowModal -> state = state.copy(showModal = true)
            is CheckOutAction.OnShowMessage -> state = state.copy(message = action.message)

            is CheckOutAction.OnPaymentMethod -> state =
                state.copy(onPayment = action.selectedOption)

            CheckOutAction.OnDismissPayment -> state = state.copy(onPayment = "")

            is CheckOutAction.OnChangeAmount -> state = state.copy(gcashAmount = action.amount)
            is CheckOutAction.OnChangeGcashNumber -> state =
                state.copy(gcashNumber = action.gcashNumber)

            is CheckOutAction.VerifyPayment -> {
                if (action.paymentMethod == "Gcash") {
                    when {
                        state.gcashAmount.toDouble() != state.totalPrice -> state =
                            state.copy(message = "Please enter the correct amount")

                        !phoneNumberUseCase(state.gcashNumber) -> state =
                            state.copy(message = "Please enter a valid Gcash number")

                        else -> {
                            paymentSuccessful(
                                userId = action.userId,
                                products = state.checkProducts
                            )
                        }
                    }
                } else if (action.paymentMethod == "Credit Card") {
                    when {
                        state.creditCardAmount.toDouble() != state.totalPrice -> state =
                            state.copy(message = "Please enter the correct amount")

                        state.cardNumber.length != 16 -> state =
                            state.copy(message = "Please enter a valid card number")

                        state.expiration.length != 5 -> state =
                            state.copy(message = "Please enter a valid expiration date")

                        state.cvv.length != 3 -> state =
                            state.copy(message = "Please enter a valid CVV")

                        state.nameInCard.isEmpty() -> state =
                            state.copy(message = "Please enter a valid name on the card")

                        else -> {
                            paymentSuccessful(
                                userId = action.userId,
                                products = state.checkProducts
                            )
                        }
                    }
                }
            }

            is CheckOutAction.OnChangeCardNumber -> state =
                state.copy(cardNumber = action.cardNumber)

            is CheckOutAction.OnChangeCreditCardAmount -> state =
                state.copy(creditCardAmount = action.amount)

            is CheckOutAction.OnChangeCvv -> state = state.copy(cvv = action.cvv)
            is CheckOutAction.OnChangeExpiration -> state =
                state.copy(expiration = action.expiration)

            is CheckOutAction.OnChangeNameInCard -> state = state.copy(nameInCard = action.cardName)
        }
    }

    private fun retrieveCheckProducts(userId: String) {
        viewModelScope.launch {
            delay(1500L)
            repository.retrieveCheckProducts(
                userId = userId
            ).collect { result ->
                state = when (result) {
                    is CustomResult.Success -> {
                        state.copy(
                            checkProducts = result.data,
                            totalPrice = result.data.sumOf { it.productPrice.toDouble() * it.quantity }
                        )
                    }

                    is CustomResult.Failure -> state.copy(
                        message = result.exception.message.toString()
                    )
                }
            }
        }
    }

    private fun decrementSavedProduct(userId: String, productId: String) {
        viewModelScope.launch {
            val savedProduct = state.checkProducts.find { it.productId == productId }
            println("Product to remove is ${savedProduct?.productId}")

            if (savedProduct != null) {
                state = when (val result = decrementProductUseCase.invoke(
                    userId = userId,
                    savedProduct = savedProduct
                ).first()) {
                    is CustomResult.Success -> {
                        state.copy(message = "", currentProductToRemove = "")
                    }

                    is CustomResult.Failure -> {
                        state.copy(message = result.exception.message.toString())
                    }
                }
            }
        }
    }

    private fun incrementSavedProduct(userId: String, savedAt: String) {
        viewModelScope.launch {
            val result = incrementProductUseCase.invoke(
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

    private fun paymentSuccessful(userId: String, products: List<SavedProduct>) {
        viewModelScope.launch {
            repository.removeCheckOutProducts(userId, products).collectLatest { result ->
                when (result) {
                    is CustomResult.Success -> processOrder(userId, products)
                    is CustomResult.Failure -> state =
                        state.copy(message = result.exception.message.toString())
                }
            }
        }
    }

    private suspend fun processOrder(userId: String, products: List<SavedProduct>) {
        state = state.copy(isLoading = true)
        repository.addCheckOutProducts(
            userId = userId,
            checkedOutProducts = products,
            paymentMethod = state.onPayment,
            dateForPickup = state.pickUpDateLong.toString()
        ).collectLatest { result ->
            state = when (result) {
                is CustomResult.Success -> CheckOutState().copy(
                    onPaymentSuccess = true,
                    orderId = result.data,
                    pickUpDate = convertMillisToDate(state.pickUpDateLong),
                    isLoading = false
                )
                is CustomResult.Failure -> state.copy(message = result.exception.message.toString(), isLoading = false)
            }
        }
    }
}