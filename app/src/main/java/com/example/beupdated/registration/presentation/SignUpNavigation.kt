package com.example.beupdated.registration.presentation

import android.app.Activity
import android.net.Network
import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.beupdated.authentication.presentation.AuthAction
import com.example.beupdated.authentication.presentation.AuthViewModel
import com.example.beupdated.common.route.AuthScreenRoute
import com.example.beupdated.common.route.SignUpScreenRouteA
import com.example.beupdated.common.route.SignUpScreenRouteB
import com.example.beupdated.common.route.SignUpScreenRouteC
import com.example.beupdated.common.route.SignUpScreenRouteD
import com.example.beupdated.core.network.NetworkStatus
import com.example.beupdated.registration.presentation.composables.AlertDialogEmailOTP
import com.example.beupdated.registration.presentation.composables.AlertDialogSMSOTP

fun NavGraphBuilder.signUpNavGraph(
    navController: NavController,
    viewModel: SignUpViewModel,
    authViewModel: AuthViewModel,
    networkStatus: NetworkStatus,
    snackbarHostState: SnackbarHostState
) {
    composable<SignUpScreenRouteA> {
        LaunchedEffect(viewModel.state) {
            authViewModel.onAction(AuthAction.OnResetState)
            if (viewModel.state.onScreenB) {
                navController.navigate(SignUpScreenRouteB)
            }
        }

        LaunchedEffect(networkStatus) {
            if (networkStatus == NetworkStatus.Available) {
                println("SignUpScreenRouteA Network available")
                println("TRIGGERED 47")
                viewModel.onAction(SignUpAction.OnStartFetchUsers)
            }
        }

        SignUpScreenA(
            onReturnAuthScreen = {
                viewModel.onAction(SignUpAction.OnCancelFetchUsers)
                navController.navigate(AuthScreenRoute) {
                    popUpTo(SignUpScreenRouteA) {
                        inclusive = true
                    }
                }
            },
            state = viewModel.state,
            action = viewModel::onAction
        )
    }

    composable<SignUpScreenRouteB> {
        LaunchedEffect(viewModel.state) {
            println("onFetchUser: ${viewModel.state.onFetchUsers}")
            if (viewModel.state.onScreenC) {
                navController.navigate(SignUpScreenRouteC)
            }
        }

        LaunchedEffect(networkStatus) {
            println("Network status changed into: $networkStatus")
            if (networkStatus == NetworkStatus.Available) {
                println("TRIGGERED 75")
                viewModel.onAction(SignUpAction.OnStartFetchUsers)
            } else {
                viewModel.onAction(SignUpAction.OnCancelFetchUsers)
            }
        }

        SignUpScreenB(
            onBack = {
                viewModel.onAction(SignUpAction.OnReturnScreenA)
                navController.navigate(SignUpScreenRouteA) {
                    popUpTo(SignUpScreenRouteB) {
                        inclusive = true
                    }
                }
            },
            onReturnAuthScreen = {
                viewModel.onAction(SignUpAction.OnReturnScreenA)
                navController.navigate(AuthScreenRoute) {
                    popUpTo(SignUpScreenRouteB) {
                        inclusive = true
                    }
                }
            },
            state = viewModel.state,
            action = viewModel::onAction,
            networkStatus = networkStatus,
            snackbarHostState = snackbarHostState
        )
    }

    composable<SignUpScreenRouteC> {
        LaunchedEffect(viewModel.state) {
            println("onFetchUser: ${viewModel.state.onFetchUsers}")
            if (viewModel.state.onScreenD) {
                navController.navigate(SignUpScreenRouteD)
            }
        }
        LaunchedEffect(networkStatus) {
            if (networkStatus == NetworkStatus.Available) {
                println("TRIGGERED 113")
                viewModel.onAction(SignUpAction.OnStartFetchUsers)
            } else {
                viewModel.onAction(SignUpAction.OnCancelFetchUsers)
            }
        }

        SignUpScreenC(
            onBack = {
                viewModel.onAction(SignUpAction.OnReturnScreenB)
                navController.navigate(SignUpScreenRouteB) {
                    popUpTo(SignUpScreenRouteC) {
                        inclusive = true
                    }
                }
            },
            onReturnAuthScreen = {
                viewModel.onAction(SignUpAction.OnReturnScreenA)
                navController.navigate(AuthScreenRoute) {
                    popUpTo(SignUpScreenRouteC) {
                        inclusive = true
                    }
                }
            },
            state = viewModel.state,
            action = viewModel::onAction,
            networkStatus = networkStatus,
            snackbarHostState = snackbarHostState
        )
    }

    composable<SignUpScreenRouteD> {
        val context = LocalContext.current
        val smsTimer by viewModel.smsTimer.collectAsState()
        val emailTimer by viewModel.emailTimer.collectAsState()
        val isResendSmsOtpEnabled by viewModel.isResendSmsOtpEnabled.collectAsState()
        val isResendEmailOtpEnabled by viewModel.isResendEmailOtpEnabled.collectAsState()

        LaunchedEffect(viewModel.state) {
            if (viewModel.state.onAuthScreen && viewModel.state.onRegisterComplete) {
                viewModel.onAction(SignUpAction.OnResetState)
                Toast.makeText(
                    context,
                    "Sign-up is successful!",
                    Toast.LENGTH_SHORT
                ).show()
                navController.navigate(AuthScreenRoute) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
                println(viewModel.state)
            }
        }

        SignUpScreenD(
            onBack = {
                viewModel.onAction(SignUpAction.OnReturnScreenC)
                navController.navigate(SignUpScreenRouteC) {
                    popUpTo(SignUpScreenRouteD) {
                        inclusive = true
                    }
                }
            },
            onReturnAuthScreen = {
                viewModel.onAction(SignUpAction.OnReturnScreenA)
                navController.navigate(AuthScreenRoute) {
                    popUpTo(SignUpScreenRouteD) {
                        inclusive = true
                    }
                }
            },
            state = viewModel.state,
            action = viewModel::onAction,
            activity = context as Activity,
            networkStatus = networkStatus,
            snackbarHostState = snackbarHostState,
            smsTimer = smsTimer,
            emailTimer = emailTimer,
            isResendSmsOtpEnabled = isResendSmsOtpEnabled,
            isResendEmailOtpEnabled = isResendEmailOtpEnabled
        )
    }
}
