package com.example.beupdated.registration.presentation.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R
import com.example.beupdated.authentication.presentation.hideKeyboard
import com.example.beupdated.core.network.NetworkStatus
import com.example.beupdated.registration.presentation.SignUpAction
import com.example.beupdated.registration.presentation.SignUpState
import com.example.beupdated.ui.theme.BeUpdatedTheme

@Composable
fun AlertDialogErrorMessage(
    action: (SignUpAction) -> Unit,
    errorMessage: String
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {
            action(SignUpAction.OnResetError)
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = null
                )
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

@Composable
fun AlertDialogSMSOTP(
    modifier: Modifier = Modifier,
    state: SignUpState,
    title: String,
    description: String,
    action: (SignUpAction) -> Unit,
    smsTimer: Int,
    isResendSmsOtpEnabled: Boolean,
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {},
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = description,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                OtpTextField(
                    otpText = state.smsOtp,
                    onOtpTextChange = { value, _ ->
                        action(SignUpAction.OnSmsOtpChange(value))
                    }
                )
            }
        },
        confirmButton = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            action(SignUpAction.OnResetSmsOtp)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = colorResource(R.color.blue)
                        )
                    ) {
                        Text("Cancel")
                    }

                    Spacer(Modifier.width(12.dp))

                    Button(
                        onClick = {
                            action(SignUpAction.OnResetSmsOtp)
                            action(SignUpAction.OnShowEmailOtp)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.gold),
                            contentColor = colorResource(R.color.blue)
                        )
                    ) {
                        Text(text = "Submit")
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Didn't receive the code?",
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                    Spacer(Modifier.width(10.dp))
                    OutlinedButton(
                        modifier = Modifier
                            .height(40.dp)
                            .padding(4.dp),
                        onClick = {
                            if (isResendSmsOtpEnabled) {
                                action(SignUpAction.OnResendOtp)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = if (smsTimer == 60) colorResource(R.color.blue) else Color.Gray
                        ),
                        border = BorderStroke(0.dp, Color.Transparent),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Resend${if (smsTimer == 60) "" else " $smsTimer"}",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun AlertDialogEmailOTP(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    action: (SignUpAction) -> Unit,
    state: SignUpState,
    emailTimer: Int,
    isResendEmailOtpEnabled: Boolean
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {},
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = description,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                OtpTextField(
                    otpText = state.emailOtp,
                    onOtpTextChange = { value, _ ->
                        action(SignUpAction.OnEmailOtpChange(value))
                    }
                )
            }
        },
        confirmButton = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            action(SignUpAction.OnResetEmailOtp)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = colorResource(R.color.blue)
                        )
                    ) {
                        Text("Cancel")
                    }

                    Spacer(Modifier.width(12.dp))

                    Button(
                        onClick = {
                            action(SignUpAction.SignUpSuccessful)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.gold),
                            contentColor = colorResource(R.color.blue)
                        )
                    ) {
                        Text(text = "Submit")
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Didn't receive the code?",
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                    Spacer(Modifier.width(10.dp))
                    OutlinedButton(
                        modifier = Modifier
                            .height(40.dp)
                            .padding(4.dp),
                        onClick = {
                            if (isResendEmailOtpEnabled) {
                                action(SignUpAction.OnResendOtp)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = if (emailTimer == 60) colorResource(R.color.blue) else Color.Gray
                        ),
                        border = BorderStroke(0.dp, Color.Transparent),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Resend${if (emailTimer == 60) "" else " $emailTimer"}",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun ADPrev() {
    BeUpdatedTheme {
        AlertDialogEmailOTP(
            state = SignUpState(),
            title = "SMS OTP",
            description = "bla bla bal bla bla balbla bla balbla bla balbla bla bal",
            action = {},
            emailTimer = 1,
            isResendEmailOtpEnabled = false
        )
    }
}

@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int = 6,
    onOtpTextChange: (String, Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        if (otpText.length > otpCount) {
            throw IllegalArgumentException("Otp text value must not have more than otpCount: $otpCount characters")
        }
    }

    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpCount) {
                onOtpTextChange.invoke(it.text, it.text.length == otpCount)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = otpText
                    )
                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String
) {
    val isFocused = text.length == index
    val char = when {
        index == text.length -> "0"
        index > text.length -> ""
        else -> text[index].toString()
    }

    Text(
        modifier = Modifier
            .width(40.dp)
            .border(
                1.dp, when {
                    isFocused -> Color.DarkGray
                    else -> Color.LightGray
                }, RoundedCornerShape(8.dp)
            )
            .padding(2.dp),
        text = char,
        style = MaterialTheme.typography.headlineMedium,
        color = if (isFocused) {
            Color.LightGray
        } else {
            Color.DarkGray
        },
        textAlign = TextAlign.Center
    )
}

@Composable
fun PasswordSpecification(
    password: String,
    fontSize: TextUnit = 12.sp
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            if (totalLength(password) < 8) {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = Color.Red
                )
                Text(
                    text = "At least 8 characters",
                    color = Color.Black,
                    fontSize = fontSize
                )
            } else {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.Green
                )
                Text(
                    text = "At least 8 characters",
                    color = Color.Black,
                    fontSize = fontSize
                )
            }
        }

        Row {
            if (!hasLowerCase(password)) {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = Color.Red
                )
                Text(
                    text = "Lower case letters (a-z)",
                    color = Color.Black,
                    fontSize = fontSize
                )
            } else {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.Green
                )
                Text(
                    text = "Lower case letters (a-z)",
                    color = Color.Black,
                    fontSize = fontSize
                )
            }
        }

        Row {
            if (!hasUpperCase(password)) {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = Color.Red
                )
                Text(
                    text = "Upper case letters (A-Z)",
                    color = Color.Black,
                    fontSize = fontSize
                )
            } else {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.Green
                )
                Text(
                    text = "Upper case letters (A-Z)",
                    color = Color.Black,
                    fontSize = fontSize
                )
            }
        }

        Row {
            if (!hasNumbers(password)) {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = Color.Red
                )
                Text(
                    text = "Numbers (0-9)",
                    color = Color.Black,
                    fontSize = fontSize
                )
            } else {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.Green
                )
                Text(
                    text = "Numbers (0-9)",
                    color = Color.Black,
                    fontSize = fontSize
                )
            }
        }

        Row {
            if (!hasSpecialCharacters(password)) {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = Color.Red
                )
                Text(
                    text = "Special characters (e.g. !@#\$%^&*)",
                    color = Color.Black,
                    fontSize = fontSize
                )
            } else {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.Green
                )
                Text(
                    text = "Special characters (e.g. !@#\$%^&*)",
                    color = Color.Black,
                    fontSize = fontSize
                )
            }
        }
    }
}

private fun totalLength(password: String): Int = password.length
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



