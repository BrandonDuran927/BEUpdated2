package com.example.beupdated.registration.domain.usecases

import javax.inject.Inject

class ValidatePhoneNumberUseCase @Inject constructor() {
    operator fun invoke(phoneNumber: String): Boolean {
        val regex = Regex("^\\d{10}")
        return regex.matches(phoneNumber)
    }
}