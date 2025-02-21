package com.example.beupdated.authentication.domain.usecases

import com.example.beupdated.authentication.domain.AuthRepository
import com.example.beupdated.common.utilities.CustomResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RetrieveUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke() : Flow<FirebaseUser?> {
        return repository.retrieveUser()
    }
}