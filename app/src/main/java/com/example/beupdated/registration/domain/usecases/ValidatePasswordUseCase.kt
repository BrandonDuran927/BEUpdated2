package com.example.beupdated.registration.domain.usecases

import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
    operator fun invoke(password: String) : Boolean {
        return password.length >= 8
                && hasLowerCase(password)
                && hasUpperCase(password)
                && hasNumbers(password)
                && hasSpecialCharacters(password)
    }

    private fun hasLowerCase(password: String): Boolean {
        password.forEach { if (it.isLowerCase()) return true }
        return false
    }
    private fun hasUpperCase(password: String): Boolean {
        password.forEach { if (it.isUpperCase()) return true }
        return false
    }
    private fun hasNumbers(password: String): Boolean {
        password.forEach { if (it.isDigit()) return true }
        return false
    }
    private fun hasSpecialCharacters(password: String): Boolean {
        val specialCharacters = "!@#$%^&*()_+~`|}{[]:;?><,./-="
        password.forEach { if (specialCharacters.contains(it)) return true }
        return false
    }
}