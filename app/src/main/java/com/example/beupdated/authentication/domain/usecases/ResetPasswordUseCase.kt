package com.example.beupdated.authentication.domain.usecases

import com.example.beupdated.authentication.domain.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String) = repository.resetAccount(email)
}