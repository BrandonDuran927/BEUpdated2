package com.example.beupdated.paymentsuccess.domain.usecase

import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.paymentsuccess.domain.PaymentRepository
import com.example.beupdated.paymentsuccess.domain.Receipt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GenerateReceiptUseCase @Inject constructor(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke(receipt: Receipt) : Flow<CustomResult<String>> {
        return repository.generateReceipt(receipt = receipt)
    }
}