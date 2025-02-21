package com.example.beupdated.registration.presentation

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beupdated.R
import com.example.beupdated.authentication.presentation.hideKeyboard
import com.example.beupdated.common.composables.AlertDialogCircularProgressIndicator
import com.example.beupdated.common.route.AuthScreenRoute
import com.example.beupdated.common.route.SignUpScreenRouteD
import com.example.beupdated.common.utilities.PhoneNumberFilter
import com.example.beupdated.core.network.NetworkStatus
import com.example.beupdated.registration.presentation.composables.AlertDialogEmailOTP
import com.example.beupdated.registration.presentation.composables.AlertDialogErrorMessage
import com.example.beupdated.registration.presentation.composables.AlertDialogSMSOTP
import com.example.beupdated.registration.presentation.composables.PasswordSpecification

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenA(
    onReturnAuthScreen: () -> Unit,
    state: SignUpState,
    action: (SignUpAction) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 25.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.gold).copy(alpha = 0f)
                ),
                title = {
                    Text(
                        text = "Create Account",
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onReturnAuthScreen()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Arrow Back",
                            modifier = Modifier.size(30.dp),
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(R.drawable.bgimagesignup),
                contentDescription = "Image Background SignUp",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier.padding(top = 130.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "What's your name?",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Enter the name you use in real life.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Black
                )

                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier.width(280.dp),
                    value = state.firstName,
                    onValueChange = {
                        action(SignUpAction.OnFirstNameChange(it))
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        focusedBorderColor = colorResource(R.color.blue),
                        cursorColor = colorResource(R.color.blue),
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedPlaceholderColor = Color.Black
                    ),
                    placeholder = {
                        Text("First Name")
                    },
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier.width(280.dp),
                    value = state.lastName,
                    onValueChange = {
                        action(SignUpAction.OnLastNameChange(it))
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        focusedBorderColor = colorResource(R.color.blue),
                        cursorColor = colorResource(R.color.blue),
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedPlaceholderColor = Color.Black,
                    ),
                    placeholder = {
                        Text("Last Name")
                    },
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier.width(280.dp),
                    value = state.middleName,
                    onValueChange = {
                        action(SignUpAction.OnMiddleNameChange(it))
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        focusedBorderColor = colorResource(R.color.blue),
                        cursorColor = colorResource(R.color.blue),
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedPlaceholderColor = Color.Black,
                    ),
                    placeholder = {
                        Text("Middle Name")
                    },
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(Modifier.height(10.dp))

                Button(
                    modifier = Modifier.width(280.dp),
                    onClick = {
                        action(SignUpAction.OnScreenB)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.blue)
                    )
                ) {
                    Text(
                        text = "Next",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                TextButton(
                    onClick = {
                        onReturnAuthScreen()
                    }
                ) {
                    Text(
                        text = "Already have an account?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Black
                    )
                }
            }
        }
    }

    if (state.errorMessage.isNotEmpty()) {
        AlertDialogErrorMessage(
            action = action,
            errorMessage = state.errorMessage
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenB(
    onReturnAuthScreen: () -> Unit,
    onBack: () -> Unit,
    state: SignUpState,
    action: (SignUpAction) -> Unit,
    networkStatus: NetworkStatus,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 25.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.gold).copy(alpha = 0f)
                ),
                title = {
                    Text(
                        text = "Create Account",
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Arrow Back",
                            modifier = Modifier.size(30.dp),
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { _ ->
        var showSnackbarMessage by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(showSnackbarMessage) {
            showSnackbarMessage?.let { message ->
                snackbarHostState.showSnackbar(message)
                showSnackbarMessage = null
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            contentAlignment = Alignment.TopCenter
        ) {


            Image(
                painter = painterResource(R.drawable.bgimagesignup),
                contentDescription = "Image Background SignUp",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier.padding(top = 130.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.width(330.dp),
                    text = "Enter your email and school ID",
                    fontSize = 32.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 65.dp),
                    text = "Enter your school email address where you can be reached and your present student ID.",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier.width(280.dp),
                    value = state.emailAddress,
                    onValueChange = {
                        action(SignUpAction.OnEmailAddressChange(it))
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        focusedBorderColor = colorResource(R.color.blue),
                        cursorColor = colorResource(R.color.blue),
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedPlaceholderColor = Color.Black,
                    ),
                    placeholder = {
                        Text("Email Address")
                    },
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier.width(280.dp),
                    value = state.studentID,
                    onValueChange = {
                        action(SignUpAction.OnStudentIdChange(it))
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        focusedBorderColor = colorResource(R.color.blue),
                        cursorColor = colorResource(R.color.blue),
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedPlaceholderColor = Color.Black,
                    ),
                    placeholder = {
                        Text("Student ID")
                    },
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(Modifier.height(10.dp))

                Button(
                    modifier = Modifier.width(280.dp),
                    onClick = {
                        println("On fetch user: ${state.onFetchUsers}")
                        if (networkStatus == NetworkStatus.Unavailable || !state.onFetchUsers) {
                            context.hideKeyboard()
                            showSnackbarMessage = "No internet connection"
                            action(SignUpAction.OnCancelFetchUsers)
                            action(SignUpAction.OnStartFetchUserOneTime)
                        } else {
                            action(SignUpAction.OnScreenC)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.blue)
                    )
                ) {
                    Text(
                        text = "Next",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                TextButton(
                    onClick = {
                        onReturnAuthScreen()
                    }
                ) {
                    Text(
                        text = "Already have an account?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Black
                    )
                }
            }
        }
    }

    if (state.isLoading) {
        AlertDialogCircularProgressIndicator(
            modifier = Modifier.alpha(1f),
            containerColor = Color.Transparent
        )
    }

    if (state.errorMessage.isNotEmpty()) {
        when (state.errorMessage) {
            "No internet connection, please try again." -> {
                Toast.makeText(
                    context,
                    "No internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                AlertDialogErrorMessage(
                    action = action,
                    errorMessage = state.errorMessage
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenC(
    onReturnAuthScreen: () -> Unit,
    onBack: () -> Unit,
    state: SignUpState,
    action: (SignUpAction) -> Unit,
    networkStatus: NetworkStatus,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 25.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.gold).copy(alpha = 0f)
                ),
                title = {
                    Text(
                        text = "Create Account",
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Arrow Back",
                            modifier = Modifier.size(30.dp),
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { _ ->
        LaunchedEffect(Unit) {
            println("SignUpScreenC LaunchedEffect Triggered")
        }
        val context = LocalContext.current
        var showSnackbarMessage by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(showSnackbarMessage) {
            showSnackbarMessage?.let { message ->
                snackbarHostState.showSnackbar(message)
                showSnackbarMessage = null
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(R.drawable.bgimagesignup),
                contentDescription = "Image Background SignUp",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier.padding(top = 130.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 50.dp),
                    text = "Enter your phone number",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp
                )
                Spacer(Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 65.dp),
                    text = "Enter your phone number where you can be reached.",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier.width(280.dp),
                    value = state.phoneNumber,
                    onValueChange = { newValue ->
                        val cleaned = newValue.filter { it.isDigit() }.take(10)
                        action(SignUpAction.OnPhoneNumberChange(cleaned))
                    },
                    visualTransformation = PhoneNumberFilter(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        focusedBorderColor = colorResource(R.color.blue),
                        cursorColor = colorResource(R.color.blue),
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedPlaceholderColor = Color.Black,
                    ),
                    placeholder = {
                        Text(
                            text = "Phone Number",
                            color = Color.Black
                        )
                    },
                    shape = RoundedCornerShape(15.dp),
                    leadingIcon = {
                        Text(
                            text = "+63",
                            color = Color.Black
                        )
                    }
                )

                Spacer(Modifier.height(10.dp))

                Button(
                    modifier = Modifier.width(280.dp),
                    onClick = {
                        if (networkStatus == NetworkStatus.Unavailable || !state.onFetchUsers) {
                            context.hideKeyboard()
                            showSnackbarMessage = "No internet connection"
                            action(SignUpAction.OnCancelFetchUsers)
                            action(SignUpAction.OnStartFetchUserOneTime)
                        } else {
                            action(SignUpAction.OnScreenD)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.blue)
                    )
                ) {
                    Text(
                        text = "Next",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                TextButton(
                    onClick = {
                        onReturnAuthScreen()
                    }
                ) {
                    Text(
                        text = "Already have an account?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Black
                    )
                }
            }
        }
    }
    if (state.isLoading) {
        AlertDialogCircularProgressIndicator(
            modifier = Modifier.alpha(1f),
            containerColor = Color.Transparent
        )
    }

    if (state.errorMessage.isNotEmpty()) {
        AlertDialogErrorMessage(
            action = action,
            errorMessage = state.errorMessage
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenD(
    onReturnAuthScreen: () -> Unit,
    onBack: () -> Unit,
    state: SignUpState,
    action: (SignUpAction) -> Unit,
    activity: Activity,
    networkStatus: NetworkStatus,
    snackbarHostState: SnackbarHostState,
    smsTimer: Int,
    emailTimer: Int,
    isResendSmsOtpEnabled: Boolean,
    isResendEmailOtpEnabled: Boolean
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 25.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.gold).copy(alpha = 0f)
                ),
                title = {
                    Text(
                        text = "Create Account",
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Arrow Back",
                            modifier = Modifier.size(30.dp),
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { _ ->
        val showSnackbarMessage = remember { mutableStateOf<String?>(null) }

        LaunchedEffect(showSnackbarMessage) {
            showSnackbarMessage.value?.let { message ->
                snackbarHostState.showSnackbar(message)
                showSnackbarMessage.value = null
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(R.drawable.bgimagesignup),
                contentDescription = "Image Background SignUp",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier.padding(top = 130.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Enter your password",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(10.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 65.dp),
                    text = "Create a password with at least 8 characters. It should be something others couldn’t guess.",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier.width(280.dp),
                    value = state.password,
                    onValueChange = {
                        action(SignUpAction.OnPasswordChange(it))
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        focusedBorderColor = colorResource(R.color.blue),
                        cursorColor = colorResource(R.color.blue),
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedPlaceholderColor = Color.Black,
                    ),
                    placeholder = {
                        Text(
                            text = "Password",
                            color = Color.Black
                        )
                    },
                    shape = RoundedCornerShape(15.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    trailingIcon = {
                        if (isPasswordVisible) {
                            IconButton(
                                onClick = {
                                    isPasswordVisible = !isPasswordVisible
                                }
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(25.dp)
                                        .alpha(0.5f),
                                    painter = painterResource(R.drawable.openeye),
                                    contentDescription = "password visible",
                                    tint = Color.Black
                                )
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    isPasswordVisible = !isPasswordVisible
                                }
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(25.dp)
                                        .alpha(0.8f),
                                    painter = painterResource(R.drawable.eyecannot),
                                    contentDescription = "password hidden",
                                    tint = Color.Black
                                )
                            }
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
                )

                Spacer(Modifier.height(10.dp))

                if (state.password.isNotEmpty()) {
                    PasswordSpecification(password = state.password)
                }

                Spacer(Modifier.height(10.dp))

                Button(
                    modifier = Modifier.width(280.dp),
                    onClick = {
                        if (networkStatus == NetworkStatus.Unavailable || !state.onFetchUsers) {
                            println("Triggered") // TODO: Why this does not work? TEST IT AGAIN
                            showSnackbarMessage.value = "No internet connection"
                            action(SignUpAction.OnCancelFetchUsers)
                            action(SignUpAction.OnStartFetchUserOneTime)
                        } else {
                            println("SignUp Button Triggered")
                            action(SignUpAction.OnScreenVerification(activity))
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.blue)
                    )
                ) {
                    Text(
                        text = "SignUp",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                TextButton(
                    onClick = {
                        onReturnAuthScreen()
                    }
                ) {
                    Text(
                        text = "Already have an account?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Black
                    )
                }
            }
        }
    }

    if (state.isLoading) {
        AlertDialogCircularProgressIndicator(
            modifier = Modifier.alpha(1f),
            containerColor = Color.Transparent
        )
    }

    if (state.onSmsOtp) {
        AlertDialogSMSOTP(
            title = "SMS OTP",
            description = "We sent a 6-digit SMS OTP to your mobile number. Please enter the code below within 5 minutes.",
            action = action,
            state = state,
            smsTimer = smsTimer,
            isResendSmsOtpEnabled = isResendSmsOtpEnabled
        )
    }

    if (state.onEmailOtp) {
        AlertDialogEmailOTP(
            title = "Email OTP",
            description = "We sent a 6-digit email OTP to your email address. Please enter the code below within 5 minutes.",
            action = action,
            state = state,
            emailTimer = emailTimer,
            isResendEmailOtpEnabled = isResendEmailOtpEnabled
        )
    }

    if (state.errorMessage.isNotEmpty()) {
        when (state.errorMessage) {
            "No internet connection, please try again." -> {
                action(SignUpAction.OnResetError)
                Toast.makeText(
                    context,
                    "No internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                AlertDialogErrorMessage(
                    action = action,
                    errorMessage = state.errorMessage
                )
            }
        }
    }
}