package com.example.beupdated.authentication.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beupdated.authentication.domain.AuthRepository
import com.example.beupdated.common.utilities.CustomResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(AuthState())
        private set

    fun onAction(action: AuthAction) {
        when (action) {
            AuthAction.OnLoginAction -> {
                signInWithEmailAndPass()
            }
            is AuthAction.OnUsernameChange -> {
                state = state.copy(username = action.username)
            }
            is AuthAction.OnPasswordChange -> {
                state = state.copy(password = action.password)

            }

            is AuthAction.OnResetPassword -> {
                resetPasswordEmail(action.emailToReset)
            }

            AuthAction.OnResetError -> {
                state = state.copy(error = null)
            }

            AuthAction.OnResetSuccess -> {
                state = state.copy(successMessage = null)
            }

            AuthAction.OnResetState -> {
                state = AuthState()
            }
        }
    }

    fun retrieveUser() {
        viewModelScope.launch {
            val result = repository.retrieveUser().first()

            if (result != null) {
                state = state.copy(user = result)
            }
        }
    }

    fun resetUser() {
        state = AuthState()
    }

    private fun signInWithEmailAndPass() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val result = repository.signInWithEmailPassword(
                state.username, state.password
            ).first()

            when (result) {
                is CustomResult.Success -> {
                    println("Sign-in successfully: ${result.data.user?.uid}")
                    state = state.copy(isLoading = false, error = null, user = result.data.user)
                }
                is CustomResult.Failure -> {
                    state = state.copy(isLoading = false, error = "${result.exception.message}")
                }
            }

        }
    }

    private fun resetPasswordEmail(email: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            repository.resetAccount(email).collect { result ->
                state = when(result) {
                    is CustomResult.Success -> {
                        state.copy(isLoading = false, error = null, successMessage = "Link is sent to your email already!")
                    }

                    is CustomResult.Failure -> {
                        state.copy(isLoading = false, error = "${result.exception.message}")
                    }
                }
            }
        }
    }
}