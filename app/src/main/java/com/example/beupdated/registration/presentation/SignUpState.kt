package com.example.beupdated.registration.presentation

data class SignUpState(
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String = "",
    val emailAddress: String = "",
    val studentID: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val smsOtp: String = "",
    val emailOtp: String = "",
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val onRegisterComplete: Boolean = false,
    val isResendSmsOtpEnabled: Boolean = false,
    val isResendEmailOtpEnabled: Boolean = false,
    val smsDone: Boolean = false,
    val onFetchUsers: Boolean = false,
    val smsTimer: Int = 60,
    val emailTimer: Int = 60,

    val onScreenB: Boolean = false,
    val onScreenC: Boolean = false,
    val onScreenD: Boolean = false,
    val onSmsOtp: Boolean = false,
    val onEmailOtp: Boolean = false,
    val onAuthScreen: Boolean = false,

    val isPhoneNumberSpam: Boolean = false,
    val isEmailSpam: Boolean = false,

    val studentIds: List<String> = emptyList(),
    val emailAddresses: List<String> = emptyList(),
    val phoneNumbers: List<String> = emptyList(),

    val sentSmsOtp: String = "",
    val sentEmailOtp: String = ""
)