package com.example.beupdated.authentication.presentation

sealed interface AuthenticationResult {
    data class Success(val username: String, val password: String): AuthenticationResult
    data object Failed: AuthenticationResult
}