package com.example.beupdated.authentication.presentation

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.beupdated.R
import com.example.beupdated.authentication.presentation.composables.AlertDialogErrorMessage
import com.example.beupdated.authentication.presentation.composables.AlertDialogSuccessMessage
import com.example.beupdated.common.composables.AlertDialogCircularProgressIndicator
import com.example.beupdated.core.network.NetworkStatus

@Composable
fun AuthScreen(
    state: AuthState,
    action: (AuthAction) -> Unit,
    onLoggedIn: (String) -> Unit,
    onRegister: () -> Unit,
    onResetPassword: () -> Unit,
    networkStatus: NetworkStatus,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showSnackbarMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(showSnackbarMessage) {
        showSnackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            showSnackbarMessage = null // Reset after showing
        }
    }

    LaunchedEffect(key1 = state.user) {
        if (state.user != null) {
            onLoggedIn(state.user.uid)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.bgimagelogin),
            contentDescription = "Image Background Login",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.width(280.dp),
                value = state.username,
                onValueChange = {
                    action(AuthAction.OnUsernameChange(it))
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = colorResource(R.color.blue),
                    cursorColor = colorResource(R.color.blue),
                    unfocusedContainerColor = Color.White,
                    focusedPlaceholderColor = Color.Black,
                ),
                leadingIcon = {
                    Icon(
                        modifier = Modifier.alpha(0.5f),
                        imageVector = Icons.Rounded.Email,
                        contentDescription = "Email address",
                        tint = Color.Black
                    )
                },
                placeholder = {
                    Text("Username..")
                },
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.width(280.dp),
                value = state.password,
                onValueChange = {
                    action(AuthAction.OnPasswordChange(it))
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = colorResource(R.color.blue),
                    cursorColor = colorResource(R.color.blue),
                    unfocusedContainerColor = Color.White,
                    focusedPlaceholderColor = Color.Black,
                ),
                placeholder = {
                    Text("Password..")
                },
                shape = RoundedCornerShape(15.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                leadingIcon = {
                    Icon(
                        modifier = Modifier.alpha(0.5f),
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "Email address",
                        tint = Color.Black
                    )
                },
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

            Button(
                onClick = {
                    context.hideKeyboard()
                    when {
                        state.username.isNotEmpty() && state.password.isNotEmpty() -> {
                            action(AuthAction.OnLoginAction)
                        }
                        networkStatus == NetworkStatus.Unavailable -> {
                            showSnackbarMessage = "No internet connection"
                        }
                        else -> {
                            Toast.makeText(
                                context,
                                "Username or password cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.gold),
                    contentColor = colorResource(R.color.blue)
                )
            ) {
                Text("Sign-in")
            }

            Spacer(Modifier.height(15.dp))

            TextButton(
                onClick = {
                    onRegister()
                }
            ) {
                Text(text = "No account yet?", textAlign = TextAlign.Center, color = Color.White)
            }

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.width(70.dp), color = Color.White)
                Text(
                    text = "Or",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                HorizontalDivider(modifier = Modifier.width(70.dp), color = Color.White)
            }

            Spacer(Modifier.height(10.dp))

            TextButton(
                onClick = {
                    onResetPassword()
                }
            ) {
                Text(text = "Forgot password", textAlign = TextAlign.Center, color = Color.White)
            }
        }


        if (state.isLoading) {
            AlertDialogCircularProgressIndicator(
                containerColor = Color.Transparent,
                modifier = Modifier.alpha(1f)
            )
        }

        if (state.error != null) {
            AlertDialogErrorMessage(action = action, errorMessage = state.error)
        }

        if (state.successMessage != null) {
            AlertDialogSuccessMessage(action = action, successfulMessage = state.successMessage)
        }
    }
}


fun Context.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocus = (this as? Activity)?.currentFocus
    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}
