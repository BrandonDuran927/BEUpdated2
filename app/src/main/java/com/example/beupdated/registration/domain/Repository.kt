package com.example.beupdated.registration.domain

import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.registration.presentation.SignUpAction
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun sendSmsOtp(phoneNumber: String, otp: String): CustomResult<Unit>
    suspend fun sendEmailOtp(recipientEmail: String, otp: String): CustomResult<Unit>
    suspend fun createAccount(email: String, password: String): Flow<CustomResult<AuthResult>>
    suspend fun addUserToFirebase(
        uid: String?,
        firstName: String,
        middleName: String,
        lastName: String,
        studentId: String,
        email: String,
        phoneNumber: String
    ): CustomResult<Unit>
    fun retrieveUsersRef() : Flow<CustomResult<Map<String, Map<String, Any>>>>
    fun retrieveUsersRefOneTime() : Flow<CustomResult<Map<String, Map<String, Any>>>>
}

// TODO: add shimmer effect when loading