package com.example.beupdated.order.presentation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.beupdated.common.route.NotificationScreenRoute
import com.example.beupdated.common.route.OrderDetailsScreenRoute
import com.example.beupdated.common.route.OrderScreenRoute
import com.example.beupdated.common.route.ProductDisplayRoute
import com.example.beupdated.common.route.ProfileScreenRoute
import com.example.beupdated.common.route.SavedProductScreenRoute
import com.example.beupdated.common.route.SearchScreenRoute

fun NavGraphBuilder.orderNavGraph(
    navController: NavController,
    viewModel: OrderViewModel
) {
    composable<OrderScreenRoute> {
        val userId = it.toRoute<OrderScreenRoute>().userId

        LaunchedEffect(Unit) {
            viewModel.onAction(OrderAction.OnFetchOrders(userId))
        }

        OrderScreen(
            onSearchScreen = {
                navController.navigate(SearchScreenRoute(userId)) {
                    popUpTo(OrderScreenRoute(userId))
                }
                viewModel.onAction(OrderAction.OnResetState)
            },
            onSavedProductScreen = {
                navController.navigate(SavedProductScreenRoute(userId)) {
                    popUpTo(OrderScreenRoute(userId))
                }
                viewModel.onAction(OrderAction.OnResetState)
            },
            onNotificationScreen = {
                navController.navigate(NotificationScreenRoute) {
                    popUpTo(OrderScreenRoute(userId))
                }
                viewModel.onAction(OrderAction.OnResetState)
            },
            onProfileScreen = {
                navController.navigate(ProfileScreenRoute(userId)) {
                    popUpTo(OrderScreenRoute(userId))
                }
                viewModel.onAction(OrderAction.OnResetState)
            },
            onProductDisplayScreen = {
                navController.navigate(ProductDisplayRoute(userId)) {
                    popUpTo(OrderScreenRoute(userId)) {
                        inclusive = true
                    }
                }
                viewModel.onAction(OrderAction.OnResetState)
            },
            onOrderDetailsScreen = { orderId, productId ->
                navController.navigate(OrderDetailsScreenRoute(userId = userId, orderId = orderId, productId = productId)) {
                    popUpTo(OrderScreenRoute(userId))
                }
                viewModel.onAction(OrderAction.OnResetState)
            },
            action = viewModel::onAction,
            state = viewModel.state
        )
    }
}