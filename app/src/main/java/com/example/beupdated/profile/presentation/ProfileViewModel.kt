package com.example.beupdated.profile.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beupdated.authentication.domain.usecases.ResetPasswordUseCase
import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.profile.domain.Profile
import com.example.beupdated.profile.domain.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.contracts.contract

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {
    var state by mutableStateOf(ProfileState())
        private set

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnFetchUserInfo -> fetchUserInfo(action.userId)
            is ProfileAction.OnResetPassword -> resetPassword(action.email)
            ProfileAction.OnDeleteAccountConfirmation -> state = state.copy(deleteAccountConfirmation = true)
            is ProfileAction.OnDeleteAccount -> deleteAccount(action.userId)
            ProfileAction.OnResetState -> state = ProfileState()
            ProfileAction.OnResetError -> state = state.copy(message = "",  deleteAccountConfirmation = false)
            ProfileAction.OnResetStatePassword -> state = state.copy(resetSuccess = false)
        }
    }

    private fun fetchUserInfo(userId: String) {
        viewModelScope.launch {
            when (val result = repository.fetchUserInfo(userId)) {
                is CustomResult.Success -> {
                    println("fetch user info: ${result.data}")
                    state = state.copy(profile = result.data)
                }

                is CustomResult.Failure -> state =
                    state.copy(message = result.exception.message.toString())
            }
        }
    }

    private fun resetPassword(email: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            resetPasswordUseCase(email).collectLatest { result ->
                when (result) {
                    is CustomResult.Success -> state =
                        state.copy(resetSuccess = true, isLoading = false)

                    is CustomResult.Failure -> state =
                        state.copy(message = result.exception.message.toString(), isLoading = false)
                }
            }
        }
    }

    private fun deleteAccount(userId: String) {
        viewModelScope.launch {
            repository.deleteAccount(userId).collectLatest { result ->
                when (result) {
                    is CustomResult.Success -> state = state.copy(deleteSuccess = true, profile = null)
                    is CustomResult.Failure -> state = state.copy(message = result.exception.message.toString())
                }
            }
        }
    }
}