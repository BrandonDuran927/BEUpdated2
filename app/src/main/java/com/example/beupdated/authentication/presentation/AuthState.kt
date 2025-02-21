package com.example.beupdated.authentication.presentation

import com.google.firebase.auth.FirebaseUser

data class AuthState(
    val username: String = "",
    val password: String = "",

    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,

    val user: FirebaseUser? = null
)