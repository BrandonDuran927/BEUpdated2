package com.example.beupdated.authentication.domain.usecases

import com.example.beupdated.authentication.domain.AuthRepository
import com.example.beupdated.common.utilities.CustomResult
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() : CustomResult<Unit>{
        return repository.signOutUser()
    }
}