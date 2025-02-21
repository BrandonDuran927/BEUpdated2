package com.example.beupdated.registration.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.registration.data.local.EmailDAO
import com.example.beupdated.registration.data.local.EmailEntity
import com.example.beupdated.registration.data.local.PhoneNumberDAO
import com.example.beupdated.registration.data.local.PhoneNumberEntity
import com.example.beupdated.registration.domain.Repository
import com.example.beupdated.registration.domain.usecases.ValidateEmailAddressUseCase
import com.example.beupdated.registration.domain.usecases.ValidateNameUseCase
import com.example.beupdated.registration.domain.usecases.ValidatePasswordUseCase
import com.example.beupdated.registration.domain.usecases.ValidatePhoneNumberUseCase
import com.example.beupdated.registration.domain.usecases.ValidateStudentIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: Repository,
    private val validateNameUseCase: ValidateNameUseCase,
    private val validateEmailAddressUseCase: ValidateEmailAddressUseCase,
    private val validateStudentIdUseCase: ValidateStudentIdUseCase,
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val phoneNumberDAO: PhoneNumberDAO,
    private val emailDAO: EmailDAO
) : ViewModel() {
    var state by mutableStateOf(SignUpState())
        private set

    private val smsTimeOut = MutableStateFlow(300)
    private val _emailTimeOut = MutableStateFlow(300)
    private val startFromSms = MutableStateFlow(false)
    private val _isResendSmsOtpEnabled = MutableStateFlow(false)
    val isResendSmsOtpEnabled: StateFlow<Boolean> get() = _isResendSmsOtpEnabled
    private val _isResendEmailOtpEnabled = MutableStateFlow(false)
    val isResendEmailOtpEnabled: StateFlow<Boolean> get() = _isResendEmailOtpEnabled

    private val _smsTimer = MutableStateFlow(60)
    val smsTimer: StateFlow<Int> get() = _smsTimer
    private val _emailTimer = MutableStateFlow(60)
    val emailTimer: StateFlow<Int> get() = _emailTimer

    private var usersListenerJob: Job? = null
    private var usersListenerJobOneTime: Job? = null
    private var smsCountDown: Job? = null
    private var emailCountDown: Job? = null

    fun onAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.OnFirstNameChange -> state = state.copy(firstName = action.fName)
            is SignUpAction.OnLastNameChange -> state = state.copy(lastName = action.lName)
            is SignUpAction.OnMiddleNameChange -> state = state.copy(middleName = action.mName)
            is SignUpAction.OnEmailAddressChange -> state =
                state.copy(emailAddress = action.emailAddress)

            is SignUpAction.OnStudentIdChange -> state = state.copy(studentID = action.studentId)
            is SignUpAction.OnPhoneNumberChange -> state =
                state.copy(phoneNumber = action.phoneNumber)

            is SignUpAction.OnPasswordChange -> state = state.copy(password = action.password)
            is SignUpAction.OnEmailOtpChange -> state = state.copy(emailOtp = action.emailOtp)
            is SignUpAction.OnSmsOtpChange -> state = state.copy(smsOtp = action.smsOtp)

            SignUpAction.OnResetError -> state = state.copy(errorMessage = "")
            SignUpAction.OnScreenB -> {
                if (validateNameUseCase.invoke(state.firstName, state.lastName)) {
                    state = state.copy(onScreenB = true)
                } else {
                    state = state.copy(errorMessage = "First name and last name must not be empty!")
                }
            }

            SignUpAction.OnReturnScreenA -> state = state.copy(onScreenB = false)
            SignUpAction.OnScreenC -> {
                val validEmailAndStudentId = validateEmailAddressUseCase.invoke(state.emailAddress)
                        && validateStudentIdUseCase.invoke(state.studentID)
                        && !state.emailAddresses.contains(state.emailAddress)
                        && !state.studentIds.contains(state.studentID)

                val alreadyExisting =
                    state.emailAddresses.contains(state.emailAddress) || state.studentIds.contains(
                        state.studentID
                    )

                state = when {
                    validEmailAndStudentId -> {
                        println("Triggered 1")
                        state.copy(onScreenC = true)
                    }

                    alreadyExisting -> {
                        println("Triggered 2")
                        state.copy(errorMessage = "The email address or student ID you entered is already registered. Would you like to log in instead?")
                    }

                    else -> {
                        println("Triggered 3")
                        state.copy(errorMessage = "Please enter a valid email address and student ID to continue.")
                    }
                }
            }

            SignUpAction.OnReturnScreenB -> state = state.copy(onScreenC = false)
            SignUpAction.OnScreenD -> {
                state =
                    if (validatePhoneNumberUseCase.invoke(state.phoneNumber)) {
                        state.copy(onScreenD = true)
                    } else {
                        state.copy(errorMessage = "Please enter a valid phone number to continue.")
                    }
            }

            SignUpAction.OnReturnScreenC -> state = state.copy(onScreenD = false)
            is SignUpAction.OnScreenVerification -> {
                when {
                    startFromSms.value -> {
                        println("Triggered startFromSms.value")
                        state = state.copy(smsOtp = "")
                        sendSmsOtp()
                    }

                    smsTimeOut.value != 300 || _emailTimeOut.value != 300 -> {
                        state =
                            state.copy(errorMessage = "Time out, please try again later.")
                    }

                    state.sentSmsOtp.isNotEmpty() && state.smsDone -> {
                        state = state.copy(onEmailOtp = true)
                    }

                    state.sentSmsOtp.isNotEmpty() -> {
                        state = state.copy(onSmsOtp = true)
                    }

                    validatePasswordUseCase.invoke(state.password) -> {
                        println("Triggered validatePasswordUseCase")
                        if (smsTimeOut.value == 300 && _emailTimeOut.value == 300 && !state.isPhoneNumberSpam && !state.isEmailSpam) {
                            sendSmsOtp()
                        } else {
                            state =
                                state.copy(errorMessage = "Time out, please try again later.")
                        }
                    }

                    else -> {
                        state =
                            state.copy(errorMessage = "Your input does not meet the required criteria.")
                    }
                }
            }

            SignUpAction.OnShowEmailOtp -> {
                when {
                    _emailTimeOut.value != 300 -> {
                        state =
                            state.copy(
                                errorMessage = "Time out, please try again later.",
                                onEmailOtp = false,
                                onSmsOtp = true
                            )
                    }

                    !state.onFetchUsers -> state = state.copy(
                        onSmsOtp = true,
                        errorMessage = "No internet connection, please try again."
                    )

                    state.smsOtp == state.sentSmsOtp
                            && state.onFetchUsers
                            && _emailTimeOut.value == 300
                            && !state.isEmailSpam -> {
                        viewModelScope.launch {
                            state = state.copy(isLoading = true, smsDone = true)
                            val previousEmail = emailDAO.getEmail(state.emailAddress)
                            val currentTimeAndDate = getCurrentDateTime()

                            if (previousEmail?.frequency == 2) {
                                state = state.copy(
                                    isEmailSpam = true, // Set this to false if 5 minutes elapsed
                                    errorMessage = "Email OTP has timeout due to frequent registering the same email address, try again later.",
                                    isLoading = false,
                                )
                                fiveMinutesCountDownEmail()
                                emailDAO.deleteEmail(previousEmail)
                            } else {
                                val otp = generateOtp()
                                val result = repository.sendEmailOtp(state.emailAddress, otp)
                                when (result) {
                                    is CustomResult.Success -> {
                                        println("sendEmailVerificationCode successful: ${state.emailAddress}")
                                        state = state.copy(
                                            sentEmailOtp = otp,
                                            onEmailOtp = true,
                                            isLoading = false
                                        )
                                        smsCountDown?.cancel()
                                        _isResendEmailOtpEnabled.value = false
                                        emailCountDown = viewModelScope.launch {
                                            for (time in 59 downTo 0) {
                                                _emailTimer.value = time
                                                println("$time 1: ${_isResendEmailOtpEnabled.value}")
                                                delay(1000L)
                                            }
                                            _emailTimer.value = 60
                                            _isResendEmailOtpEnabled.value = true
                                        }

                                        if (previousEmail != null) {
                                            if (previousEmail.email == state.emailAddress && timeDifferenceCalculator(
                                                    phoneNumberEntity = null,
                                                    emailEntity = previousEmail,
                                                    currentTimeAndDate
                                                )
                                            ) {
                                                emailDAO.updateEmail(previousEmail.copy(frequency = previousEmail.frequency + 1))
                                                println("Triggered")
                                            }
                                        } else {
                                            println("Triggered 2")
                                            emailDAO.insertEmail(
                                                email = EmailEntity(
                                                    email = state.emailAddress,
                                                    emailInputTimeDate = getCurrentDateTime()
                                                )
                                            )
                                        }
                                    }

                                    is CustomResult.Failure -> println("sendEmailVerificationCode failed")
                                }
                            }
                        }
                    }

                    else -> {
                        state = state.copy(
                            onSmsOtp = true,
                            errorMessage = "The OTP you entered is not correct. Please check the SMS and try again."
                        )
                    }
                }
            }

            SignUpAction.OnResetSmsOtp -> state =
                state.copy(onEmailOtp = false, onSmsOtp = false, isLoading = false)

            SignUpAction.OnResetEmailOtp -> state =
                state.copy(onEmailOtp = false, isLoading = false)

            SignUpAction.SignUpSuccessful -> {
                if (state.emailOtp == state.sentEmailOtp) {
                    state = state.copy(isLoading = true)
                    viewModelScope.launch {
                        val result =
                            repository.createAccount(state.emailAddress, state.password).first()
                        when (result) {
                            is CustomResult.Success -> {
                                println("(SignUpViewModel) accountCreation successful")
                                val innerResult = repository.addUserToFirebase(
                                    result.data.user?.uid,
                                    state.firstName,
                                    state.middleName,
                                    state.lastName,
                                    state.studentID,
                                    state.emailAddress,
                                    state.phoneNumber
                                )
                                when (innerResult) {
                                    is CustomResult.Success -> {
                                        println("addingUser successful id: ${state.studentID}")
                                        state = SignUpState().copy(
                                            onAuthScreen = true,
                                            onRegisterComplete = true,
                                            isLoading = false
                                        )
                                    }

                                    is CustomResult.Failure -> {
                                        println("addingUser failed: ${innerResult.exception.message}")
                                    }
                                }
                            }

                            is CustomResult.Failure -> state = state.copy(
                                errorMessage = "${result.exception.message}",
                                isLoading = false
                            )
                        }
                    }
                } else {
                    state = state.copy(
                        onEmailOtp = true,
                        errorMessage = "The OTP you entered is not correct. Please check your email and try again."
                    )
                }
            }

            SignUpAction.OnResetState -> {
                if (state.onAuthScreen && state.onRegisterComplete) {
                    state = SignUpState()
                }
            }

            is SignUpAction.OnScreenToFalse -> {
                usersListenerJob?.cancel()
                smsCountDown?.cancel()
                emailCountDown?.cancel()
                state = SignUpState().copy(
                    firstName = state.firstName,
                    lastName = state.lastName,
                    middleName = state.middleName,
                    emailAddress = state.emailAddress,
                    studentID = state.studentID,
                    phoneNumber = state.phoneNumber,
                )
            }

            SignUpAction.OnCancelFetchUsers -> {
                state = state.copy(onFetchUsers = false)
                println("OnCancelFetchUsers triggered: ${state.onFetchUsers}")
                usersListenerJob?.cancel()
                usersListenerJobOneTime?.cancel()
            }

            SignUpAction.OnStartFetchUsers -> {
                fetchUsers()
            }

            SignUpAction.OnStartFetchUserOneTime -> {
                fetchUsersOneTime()
            }

            SignUpAction.OnResendOtp -> {
                viewModelScope.launch {
                    if (_isResendSmsOtpEnabled.value && !_isResendEmailOtpEnabled.value) {
                        state = state.copy(isLoading = true)
                        delay(2000L)
                        val otp = generateOtp()

                        val result = repository.sendSmsOtp(state.phoneNumber, otp)
                        when (result) {
                            is CustomResult.Success -> {
                                println("sendSmsVerificationCode successful")
                                state = state.copy(
                                    sentSmsOtp = otp,
                                    isLoading = false,
                                    smsOtp = ""
                                )
                                emailCountDown?.cancel()
                                _isResendSmsOtpEnabled.value = false
                                println("OTP: $otp")
                                smsCountDown = viewModelScope.launch {
                                    for (time in 59 downTo 0) {
                                        _smsTimer.value = time
                                        println("$time: $_isResendSmsOtpEnabled")
                                        delay(1000L)
                                    }
                                    _smsTimer.value = 60
                                    _isResendSmsOtpEnabled.value = true
                                }
                            }
                            is CustomResult.Failure -> println("sendSmsVerificationCode failed")
                        }
                    } else {
                        state = state.copy(isLoading = true)
                        val otp = generateOtp()
                        val result = repository.sendEmailOtp(state.emailAddress, otp)
                        when (result) {
                            is CustomResult.Success -> {
                                println("sendEmailVerificationCode successful: ${state.emailAddress}")
                                state = state.copy(
                                    sentEmailOtp = otp,
                                    emailOtp = "",
                                    isLoading = false
                                )
                                smsCountDown?.cancel()
                                emailCountDown = viewModelScope.launch {
                                    _isResendEmailOtpEnabled.value = false
                                    for (time in 59 downTo 0) {
                                        _emailTimer.value = time
                                        println("$time: ${_isResendEmailOtpEnabled.value}")
                                        delay(1000L)
                                    }
                                    _emailTimer.value = 60
                                    _isResendEmailOtpEnabled.value = true
                                }
                            }

                            is CustomResult.Failure -> println("sendEmailVerificationCode failed")
                        }
                    }
                }
            }

            SignUpAction.OnTemporaryTruncate -> {
                viewModelScope.launch {
                    phoneNumberDAO.truncateTable()
                }
            }
        }
    }

    private fun fetchUsers() {
        println("fetchUsers triggered")
        usersListenerJob = viewModelScope.launch {
            println("fetchUsers triggered 1")
            repository.retrieveUsersRef().collect { result ->
                println("fetchUsers triggered 2")
                when (result) {
                    is CustomResult.Success -> {
                        val studentIds = getStudentId(result.data)
                        val emailAddresses = getEmailAddress(result.data)
                        val phoneNumbers = getPhoneNumber(result.data)

                        state = state.copy(
                            studentIds = studentIds,
                            emailAddresses = emailAddresses,
                            phoneNumbers = phoneNumbers,
                            onFetchUsers = true
                        )
                        println("email address: $emailAddresses, onFetchUser: ${state.onFetchUsers}")
                    }

                    is CustomResult.Failure -> state =
                        state.copy(errorMessage = "Error indicates: ${result.exception.message.toString()}")
                }
            }
        }
    }

    private fun fetchUsersOneTime() {
        usersListenerJobOneTime?.cancel()
        usersListenerJobOneTime = viewModelScope.launch {
            repository.retrieveUsersRefOneTime().collect{ result ->
                when (result) {
                    is CustomResult.Success -> {
                        val studentIds = getStudentId(result.data)
                        val emailAddresses = getEmailAddress(result.data)
                        val phoneNumbers = getPhoneNumber(result.data)

                        state = state.copy(
                            studentIds = studentIds,
                            emailAddresses = emailAddresses,
                            phoneNumbers = phoneNumbers,
                            onFetchUsers = true
                        )
                        println("email address: $emailAddresses, onFetchUser: ${state.onFetchUsers}")
                    }

                    is CustomResult.Failure ->
                        state = state.copy(errorMessage = result.exception.message.toString())
                }
            }
        }
    }


    private fun sendSmsOtp() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            delay(2000L)
            val otp = generateOtp()
            val result = repository.sendSmsOtp(state.phoneNumber, otp)
            when (result) {
                is CustomResult.Success -> {
                    println("sendSmsVerificationCode successful")
                    state = state.copy(
                        sentSmsOtp = otp,
                        onSmsOtp = true,
                        isLoading = false,
                    )
                    emailCountDown?.cancel()
                    _isResendSmsOtpEnabled.value = false
                    startFromSms.value = false

                    println("OTP: $otp")

                    smsCountDown = viewModelScope.launch {
                        for (time in 59 downTo 0) {
                            _smsTimer.value = time
                            println("$time: ${_isResendSmsOtpEnabled.value}")
                            delay(1000L)
                        }
                        _smsTimer.value = 60
                        _isResendSmsOtpEnabled.value = true
                    }

                }

                is CustomResult.Failure -> println("sendSmsVerificationCode failed")
            }
        }
    }

    private fun generateOtp(): String {
        return (100000..999999).random().toString()
    }

    private fun fiveMinutesCountDownEmail() {
        viewModelScope.launch {
            for (time in 300 downTo 0) {
                _emailTimeOut.value = time
                println(time)
                delay(1000L)
            }
            state = state.copy(isEmailSpam = false)
            _emailTimeOut.value = 300
            startFromSms.value = true
        }
    }

    // 12/12/2025-13:53
    private fun timeDifferenceCalculator(
        phoneNumberEntity: PhoneNumberEntity?,
        emailEntity: EmailEntity?,
        currentDateAndTime: String
    ): Boolean {
        if (phoneNumberEntity != null) {
            val phoneNumberEntDate = phoneNumberEntity.phoneNumberInputTimeDate.substring(0, 9)
            val phoneNumberEntTime =
                convertToTime(phoneNumberEntity.phoneNumberInputTimeDate.substring(11, 15))
            val currentDate = currentDateAndTime.substring(0, 9)
            val currentTime = convertToTime(currentDateAndTime.substring(11, 15))

            println(
                "${
                    phoneNumberEntity.phoneNumberInputTimeDate.substring(
                        0,
                        10
                    )
                } and ${phoneNumberEntity.phoneNumberInputTimeDate.substring(11, 16)}"
            )

            if (phoneNumberEntDate == currentDate) {
                val timeDifference = currentTime.time - phoneNumberEntTime.time
                val differenceInMinutes =
                    timeDifference / (1000 * 60) // converts milliseconds to minutes
                return differenceInMinutes in 0..5
            }
            return false
        } else {
            val emailEntDate = emailEntity!!.emailInputTimeDate.substring(0, 9)
            val emailEntTime = convertToTime(emailEntity.emailInputTimeDate.substring(11, 15))
            val currentDate = currentDateAndTime.substring(0, 9)
            val currentTime = convertToTime(currentDateAndTime.substring(11, 15))

            if (emailEntDate == currentDate) {
                val timeDifference = currentTime.time - emailEntTime.time
                val differenceInMinutes =
                    timeDifference / (1000 * 60) // converts milliseconds to minutes
                return differenceInMinutes in 0..5
            }
            return false
        }
    }

    private fun convertToTime(currentTime: String): Date {
        // Split the time string into hours and minutes
        val timeParts = currentTime.split(":")
        val hours = timeParts[0].toInt()
        val minutes = timeParts[1].toInt()

        // Create a Calendar instance
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, minutes)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return calendar.time // Return the Date object
    }

    private fun listInfo(users: Map<String, Map<String, Any>>): List<Map<String, String>> {
        return users.mapNotNull { map ->
            map.value.mapValues { (_, value) -> value.toString() }
        }
    }

    private fun getStudentId(users: Map<String, Map<String, Any>>): List<String> {
        val infoList = listInfo(users)
        val listOfStudentIds = mutableListOf<String>()

        infoList.forEach {
            it.mapKeys { (keys, values) ->
                if (keys == "id") {
                    listOfStudentIds.add(values)
                }
            }
        }

        return listOfStudentIds
    }

    private fun getEmailAddress(users: Map<String, Map<String, Any>>): List<String> {
        val infoList = listInfo(users)
        val listOfEmailAddresses = mutableListOf<String>()

        infoList.forEach {
            it.mapKeys { (keys, values) ->
                if (keys == "email") {
                    listOfEmailAddresses.add(values)
                }
            }
        }

        return listOfEmailAddresses
    }

    private fun getPhoneNumber(users: Map<String, Map<String, Any>>): List<String> {
        val infoList = listInfo(users)
        val listOfPhoneNumbers = mutableListOf<String>()

        infoList.forEach {
            it.mapKeys { (keys, values) ->
                if (keys == "phoneNumber") {
                    listOfPhoneNumbers.add(values)
                }
            }
        }

        return listOfPhoneNumbers
    }

    private fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()

        val formatter = SimpleDateFormat("dd/MM/yyyy-HH:mm", Locale.getDefault())

        return formatter.format(calendar.time)
    }
}