package com.example.beupdated.authentication.presentation.composables

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R
import com.example.beupdated.authentication.presentation.AuthAction
import com.example.beupdated.authentication.presentation.AuthState
import com.example.beupdated.authentication.presentation.AuthViewModel
import com.example.beupdated.ui.theme.BeUpdatedTheme

@Composable
fun AlertDialogSuccessMessage(
    action: (AuthAction) -> Unit,
    successfulMessage: String
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {
            action(AuthAction.OnResetSuccess)
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    modifier = Modifier.size(50.dp),
                    contentDescription = null,
                    tint = colorResource(R.color.gold)
                )
                Text(
                    text = successfulMessage,
                    color = colorResource(R.color.blue),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

@Composable
fun AlertDialogErrorMessage(
    action: (AuthAction) -> Unit,
    errorMessage: String
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {
            action(AuthAction.OnResetError)
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
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
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

@Preview
@Composable
private fun Test() {
    BeUpdatedTheme {
        AlertDialogErrorMessage(
            action = {},
            errorMessage = "Error message"
        )
    }
}

@Composable
fun ResetPassword(
    state: AuthState,
    action: (AuthAction) -> Unit,
    isReset: MutableState<Boolean>
) {
    var emailAddress by remember { mutableStateOf("") }
    val context = LocalContext.current

    if (isReset.value) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = {
                isReset.value = false
            },
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Reset Password")
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        modifier = Modifier.widthIn(280.dp),
                        value = emailAddress,
                        onValueChange = {
                            emailAddress = it
                        },
                        keyboardActions = KeyboardActions(onDone = {

                            if (emailAddress.isNotEmpty()) {
                                action(AuthAction.OnResetPassword(emailAddress))
                                if (state.error == null && !state.isLoading) {
                                    isReset.value = false
                                    Toast.makeText(
                                        context,
                                        "Link is sent to your email!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Email address must not be empty!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = colorResource(R.color.blue),
                            cursorColor = colorResource(R.color.blue),
                            unfocusedContainerColor = Color.White,
                            focusedPlaceholderColor = Color.Black
                        ),
                        placeholder = {
                            Text("Your email address..")
                        },
                        shape = RoundedCornerShape(15.dp)
                    )
                }
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            if (emailAddress.isNotEmpty()) {
                                action(AuthAction.OnResetPassword(emailAddress))
                            } else {
                                Toast.makeText(
                                    context,
                                    "Email address must not be empty!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.gold),
                            contentColor = colorResource(R.color.blue)
                        )
                    ) {
                        Text("Submit")
                    }
                }
            }
        )
    }
}
