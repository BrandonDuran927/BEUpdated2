package com.example.beupdated.registration.domain.usecases

import android.util.Log
import javax.inject.Inject

class ValidateNameUseCase @Inject constructor() {
    operator fun invoke(firstName: String, lastName: String): Boolean {
        return firstName.isNotEmpty() && lastName.isNotEmpty()
    }
}