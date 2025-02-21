package com.example.beupdated.registration.domain.usecases

import javax.inject.Inject

class ValidateStudentIdUseCase @Inject constructor() {
    operator fun invoke(studentId: String): Boolean {
        val regex = Regex("^\\d{4}-\\d{6}$")
        return regex.matches(studentId)
    }
}