package com.example.beupdated.authentication.presentation

import com.example.beupdated.common.route.AppRoute

sealed interface AuthAction {
    data class OnUsernameChange(val username: String): AuthAction
    data class OnPasswordChange(val password: String): AuthAction
    data class OnResetPassword(val emailToReset: String): AuthAction
    data object OnLoginAction: AuthAction
    data object OnResetError: AuthAction
    data object OnResetSuccess: AuthAction
    data object OnResetState : AuthAction
}