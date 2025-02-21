package com.example.beupdated.authentication.domain

import com.example.beupdated.common.utilities.CustomResult
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signInWithEmailPassword(email: String, password: String) : Flow<CustomResult<AuthResult>>
    suspend fun signOutUser() : CustomResult<Unit>
    suspend fun resetAccount(email: String) : Flow<CustomResult<Void>>
    fun retrieveUser() : Flow<FirebaseUser?>
}