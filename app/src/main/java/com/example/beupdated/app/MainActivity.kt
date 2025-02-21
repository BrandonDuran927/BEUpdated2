package com.example.beupdated.app

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.beupdated.authentication.presentation.AuthViewModel
import com.example.beupdated.authentication.presentation.authNavGraph
import com.example.beupdated.checkout.presentation.CheckOutViewModel
import com.example.beupdated.checkout.presentation.checkOutNavGraph
import com.example.beupdated.common.route.AppRoute
import com.example.beupdated.common.route.ProductDisplayRoute
import com.example.beupdated.core.network.ConnectivityObserver
import com.example.beupdated.core.network.NetworkStatus
import com.example.beupdated.core.network.NetworkViewModel
import com.example.beupdated.notification.presentation.notificationNavGraph
import com.example.beupdated.order.presentation.OrderViewModel
import com.example.beupdated.order.presentation.orderNavGraph
import com.example.beupdated.orderdetails.presentation.OrderDetailsViewModel
import com.example.beupdated.orderdetails.presentation.orderDetailsNavGraph
import com.example.beupdated.paymentsuccess.presentation.PaymentViewModel
import com.example.beupdated.paymentsuccess.presentation.paymentNavGraph
import com.example.beupdated.product.presentation.ProductViewModel
import com.example.beupdated.product.presentation.productNavGraph
import com.example.beupdated.productdisplay.presentation.ProductDisplayViewModel
import com.example.beupdated.productdisplay.presentation.productDisplayNavGraph
import com.example.beupdated.profile.presentation.ProfileViewModel
import com.example.beupdated.profile.presentation.profileNavGraph
import com.example.beupdated.registration.presentation.SignUpViewModel
import com.example.beupdated.registration.presentation.signUpNavGraph
import com.example.beupdated.savedproduct.presentation.SavedProductViewModel
import com.example.beupdated.savedproduct.presentation.savedProductNavGraph
import com.example.beupdated.search.presentation.SearchViewModel
import com.example.beupdated.search.presentation.searchNavGraph
import com.example.beupdated.ui.theme.BeUpdatedTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.sign

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        enableEdgeToEdge()

        installSplashScreen()

        setContent {
            BeUpdatedTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { _ ->
                    val navController = rememberNavController()

                    NavGraph(
                        navController = navController
                    )
                }
            }
        }

        this.actionBar?.hide()

    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    productDisplayViewModel: ProductDisplayViewModel = hiltViewModel(),
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    networkViewModel: NetworkViewModel = hiltViewModel(),
    productViewModel: ProductViewModel = hiltViewModel(),
    savedProductViewModel: SavedProductViewModel = hiltViewModel(),
    checkViewModel: CheckOutViewModel = hiltViewModel(),
    paymentViewModel: PaymentViewModel = hiltViewModel(),
    orderDetailsViewModel: OrderDetailsViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    authViewModel.retrieveUser()
    val user = authViewModel.state.user
    val snackbarHostState = remember { SnackbarHostState() }
    val networkStatus by networkViewModel.networkStatus.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { _ ->

        LaunchedEffect(key1 = networkStatus) {
            println("Network status: $networkStatus")
            if (networkStatus == NetworkStatus.Unavailable) {
                snackbarHostState.showSnackbar("No internet connection")
            }
        }

        LaunchedEffect(Unit) {
            if (user != null) {
                println("Login successfully: ${user.email}")
                navController.navigate(ProductDisplayRoute(user.email.toString())) {
                    popUpTo(AppRoute.Auth.route) { inclusive = true }
                }
            }
        }
        NavHost(navController = navController, startDestination = AppRoute.Auth.route) {
            authNavGraph(
                navController = navController,
                viewModel = authViewModel,
                signUpViewModel = signUpViewModel,
                networkStatus = networkStatus,
                snackbarHostState = snackbarHostState
            )

            signUpNavGraph(
                navController = navController,
                viewModel = signUpViewModel,
                authViewModel = authViewModel,
                networkStatus = networkStatus,
                snackbarHostState = snackbarHostState
            )

            productDisplayNavGraph(
                navController = navController,
                viewModel = productDisplayViewModel,
                networkStatus = networkStatus,
                savedProductViewModel = savedProductViewModel
            )

            productNavGraph(
                navController = navController,
                productViewModel = productViewModel
            )

            checkOutNavGraph(
                navController = navController,
                viewModel = checkViewModel
            )

            notificationNavGraph(
                navController = navController
            )

            paymentNavGraph(
                navController = navController,
                checkOutViewModel = checkViewModel,
                paymentViewModel = paymentViewModel
            )

            savedProductNavGraph(
                navController = navController,
                viewModel = savedProductViewModel
            )

            searchNavGraph(
                navController = navController,
                viewModel = searchViewModel
            )

            profileNavGraph(
                navController = navController,
                authViewModel = authViewModel,
                productDisplayViewModel = productDisplayViewModel,
                profileViewModel = profileViewModel
            )

            orderNavGraph(
                navController = navController,
                viewModel = orderViewModel
            )

            orderDetailsNavGraph(
                navController = navController,
                viewModel = orderDetailsViewModel
            )
        }
    }

}

