package com.example.beupdated.authentication.presentation

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.beupdated.authentication.presentation.composables.ResetPassword
import com.example.beupdated.common.route.AppRoute
import com.example.beupdated.common.route.AuthScreenRoute
import com.example.beupdated.common.route.ProductDisplayRoute
import com.example.beupdated.common.route.SignUpScreenRouteA
import com.example.beupdated.core.network.NetworkStatus
import com.example.beupdated.core.network.NetworkViewModel
import com.example.beupdated.registration.presentation.SignUpAction
import com.example.beupdated.registration.presentation.SignUpViewModel

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    viewModel: AuthViewModel,
    signUpViewModel: SignUpViewModel,
    networkStatus: NetworkStatus,
    snackbarHostState: SnackbarHostState
) {
    composable<AuthScreenRoute> {
        Scaffold{ _ ->
            LaunchedEffect(Unit) {
                signUpViewModel.onAction(SignUpAction.OnScreenToFalse)
            }
            val isReset = remember { mutableStateOf(false) }

            AuthScreen(
                state = viewModel.state,
                action = viewModel::onAction,
                onLoggedIn = { userUID ->
                    navController.navigate(ProductDisplayRoute(userUID)) {
                        popUpTo(AuthScreenRoute) {
                            inclusive = true
                        }
                    }
                },
                onRegister = {
                    println("On register trigger")
                    navController.navigate(SignUpScreenRouteA)
                },
                onResetPassword = {
                    isReset.value = true
                },
                networkStatus = networkStatus,
                snackbarHostState = snackbarHostState
            )

            if (isReset.value) {
                ResetPassword(
                    state = viewModel.state,
                    isReset = isReset,
                    action = viewModel::onAction
                )
            }
        }
    }
}