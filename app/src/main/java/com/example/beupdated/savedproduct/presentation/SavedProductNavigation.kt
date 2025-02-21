package com.example.beupdated.savedproduct.presentation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.beupdated.common.route.AppRoute
import com.example.beupdated.common.route.CheckoutScreenRoute
import com.example.beupdated.common.route.NotificationScreenRoute
import com.example.beupdated.common.route.OrderScreenRoute
import com.example.beupdated.common.route.ProductDisplayRoute
import com.example.beupdated.common.route.ProductScreenRoute
import com.example.beupdated.common.route.ProfileScreenRoute
import com.example.beupdated.common.route.SavedProductScreenRoute
import com.example.beupdated.common.route.SearchScreenRoute

fun NavGraphBuilder.savedProductNavGraph(
    viewModel: SavedProductViewModel,
    navController: NavController
) {
    composable<SavedProductScreenRoute> {
        val userId = it.toRoute<SavedProductScreenRoute>().userId

        LaunchedEffect(Unit) {
            println("This is the userId: $userId")
            viewModel.onAction(SavedProductAction.OnFetchSavedProducts(userId))
        }

        SavedProductScreen(
            onCheckoutScreen = {
                navController.navigate(CheckoutScreenRoute(userId)) {
                    popUpTo(SavedProductScreenRoute(userId))
                }
            },
            onProductDisplayScreen = {
                navController.navigate(ProductDisplayRoute(userId)) {
                    popUpTo(SavedProductScreenRoute(userId))
                }
            },
            onProductScreen = { productName ->
                navController.navigate(ProductScreenRoute(productName = productName, userId = userId)) {
                    popUpTo(SavedProductScreenRoute(userId))
                }
                viewModel.onAction(SavedProductAction.OnResetState(userId))
            },
            onNotificationScreen = {
                navController.navigate(NotificationScreenRoute) {
                    popUpTo(SavedProductScreenRoute(userId))
                }
                viewModel.onAction(SavedProductAction.OnResetState(userId))
            },
            onSearchScreen = {
                navController.navigate(SearchScreenRoute(userId)) {
                    popUpToRoute
                }
                viewModel.onAction(SavedProductAction.OnResetState(userId))
            },
            onProfileScreen = {
                navController.navigate(ProfileScreenRoute(userId)) {
                    popUpTo(SavedProductScreenRoute(userId)) {
                        inclusive = true
                    }
                }
                viewModel.onAction(SavedProductAction.OnResetState(userId))
            },
            onOrderScreen = {
                navController.navigate(OrderScreenRoute(userId)) {
                    popUpTo(SavedProductScreenRoute(userId)) {
                        inclusive = true
                    }
                }
                viewModel.onAction(SavedProductAction.OnResetState(userId))
            },
            state = viewModel.state,
            action = viewModel::onAction,
            userId = userId
        )
    }
}