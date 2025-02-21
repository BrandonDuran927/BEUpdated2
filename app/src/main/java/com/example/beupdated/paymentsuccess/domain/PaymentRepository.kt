package com.example.beupdated.paymentsuccess.domain

import android.content.Context
import com.example.beupdated.common.utilities.CustomResult
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    suspend fun generateReceipt(receipt: Receipt) : Flow<CustomResult<String>>
}