package com.example.beupdated.profile.presentation

sealed class ProfileAction {
    data class OnFetchUserInfo(val userId: String) : ProfileAction()
    data class OnResetPassword(val email: String) : ProfileAction()
    data class OnDeleteAccount(val userId: String) : ProfileAction()
    data object OnDeleteAccountConfirmation : ProfileAction()
    data object OnResetState : ProfileAction()
    data object OnResetError : ProfileAction()
    data object OnResetStatePassword : ProfileAction()
}