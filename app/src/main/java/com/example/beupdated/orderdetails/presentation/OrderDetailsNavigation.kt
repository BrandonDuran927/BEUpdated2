package com.example.beupdated.orderdetails.presentation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.beupdated.common.route.OrderDetailsScreenRoute
import com.example.beupdated.common.route.ProductDisplayRoute
import com.example.beupdated.common.route.ProductScreenRoute

fun NavGraphBuilder.orderDetailsNavGraph(
    navController: NavController,
    viewModel: OrderDetailsViewModel
) {
    composable<OrderDetailsScreenRoute> {
        val userId = it.toRoute<OrderDetailsScreenRoute>().userId
        val orderId = it.toRoute<OrderDetailsScreenRoute>().orderId
        val productId = it.toRoute<OrderDetailsScreenRoute>().productId

        LaunchedEffect(Unit) {
            println("Triggered")
            viewModel.onAction(OrderDetailsAction.OnFetchedOrder(userId = userId, orderId = orderId, productId = productId))
        }

        LaunchedEffect(viewModel.state) {
            println("State value: ${viewModel.state}")
        }

        OrderDetailsScreen(
            onBackPressed = {
                navController.popBackStack()
                viewModel.onAction(OrderDetailsAction.OnResetState)
            },
            onPaymentSuccessfulScreen = {
                navController.navigate(ProductDisplayRoute(userId)) {
                    popUpTo(OrderDetailsScreenRoute(userId, orderId))
                }
                viewModel.onAction(OrderDetailsAction.OnResetState)
            },
            state = viewModel.state,
            action = viewModel::onAction,
            onProductScreen = { productName ->
                navController.navigate(ProductScreenRoute(productName = productName, userId = userId)) {
                    popUpTo(OrderDetailsScreenRoute(orderId = orderId, userId = userId))
                }
                viewModel.onAction(OrderDetailsAction.OnResetState)
            }
        )
    }
}