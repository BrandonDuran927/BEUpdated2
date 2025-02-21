package com.example.beupdated.productdisplay.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.beupdated.authentication.presentation.AuthViewModel
import com.example.beupdated.common.route.NotificationScreenRoute
import com.example.beupdated.common.route.OrderScreenRoute
import com.example.beupdated.common.route.ProductDisplayRoute
import com.example.beupdated.common.route.ProductScreenRoute
import com.example.beupdated.common.route.ProfileScreenRoute
import com.example.beupdated.common.route.SavedProductScreenRoute
import com.example.beupdated.common.route.SearchScreenRoute
import com.example.beupdated.core.network.NetworkStatus
import com.example.beupdated.savedproduct.presentation.SavedProductAction
import com.example.beupdated.savedproduct.presentation.SavedProductViewModel
import com.google.firebase.auth.FirebaseUser

fun NavGraphBuilder.productDisplayNavGraph(
    navController: NavController,
    viewModel: ProductDisplayViewModel,
    networkStatus: NetworkStatus,
    savedProductViewModel: SavedProductViewModel
) {
    composable<ProductDisplayRoute> {
        val userUID = it.toRoute<ProductDisplayRoute>().userId

        LaunchedEffect(Unit) {
            savedProductViewModel.onAction(SavedProductAction.OnResetState(userUID))
        }

        // Fetch the user info and data here
        LaunchedEffect(networkStatus) {
            println("User id in productDisplay: $userUID")
            viewModel.onAction(ProductDisplayAction.OnFetchUserData(userUID))
            viewModel.onAction(ProductDisplayAction.OnFetchProducts)
            if (networkStatus == NetworkStatus.Available && viewModel.state.products.isEmpty()) {
                println("Network available")
            }
        }

        ProductDisplayScreen(
            action = viewModel::onAction,
            onProductScreen = { product ->
                navController.navigate(ProductScreenRoute(product, userUID)) {
                    popUpTo(ProductDisplayRoute(userUID))
                }
            },
            onNotificationScreen = {
                navController.navigate(NotificationScreenRoute) {
                    popUpTo(ProductDisplayRoute(userUID))
                }
            },
            onSavedProductScreen = {
                navController.navigate(SavedProductScreenRoute(userUID)) {
                    popUpTo(ProductDisplayRoute(userUID))
                }
            },
            onSearchScreen = {
                navController.navigate(SearchScreenRoute(userId = userUID)) {
                    popUpToRoute
                }
            },
            onProfileScreen = {
                navController.navigate(ProfileScreenRoute(userUID)) {
                    popUpTo(ProductDisplayRoute(userUID))
                }
            },
            onOrderScreen = {
                navController.navigate(OrderScreenRoute(userUID)) {
                    popUpTo(ProductDisplayRoute(userUID))
                }
            },
            state = viewModel.state
        )
    }
}