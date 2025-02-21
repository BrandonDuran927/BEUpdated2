package com.example.beupdated.profile.presentation

import com.example.beupdated.profile.domain.Profile

data class ProfileState(
    val message: String = "",
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val resetSuccess: Boolean = false,
    val deleteAccountConfirmation: Boolean = false,
    val deleteSuccess: Boolean = false
)
