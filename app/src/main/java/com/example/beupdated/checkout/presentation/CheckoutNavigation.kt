package com.example.beupdated.checkout.presentation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.beupdated.common.route.CheckoutScreenRoute
import com.example.beupdated.common.route.PaymentSuccessfulScreenRoute
import com.example.beupdated.common.route.ProductDisplayRoute
import com.example.beupdated.common.route.ProductScreenRoute

fun NavGraphBuilder.checkOutNavGraph(
    navController: NavController,
    viewModel: CheckOutViewModel
) {
    composable<CheckoutScreenRoute> {
        val userId = it.toRoute<CheckoutScreenRoute>().userId

        LaunchedEffect(Unit) {
            viewModel.onAction(CheckOutAction.OnRetrieveCheckProducts(userId))
        }

        LaunchedEffect(viewModel.state.onPaymentSuccess) {
            if (viewModel.state.onPaymentSuccess) {
                println("Date for pick-up: ${viewModel.state.pickUpDate}\n Date for pick-up long: ${viewModel.state.pickUpDateLong}")
                navController.navigate(PaymentSuccessfulScreenRoute(
                    userId = userId,
                    dateForPickUp = viewModel.state.pickUpDate,
                    orderId = viewModel.state.orderId
                )) {
                    popUpTo(CheckoutScreenRoute(userId)) {
                        inclusive = true
                    }
                }
            }
        }

        CheckoutScreen(
            state = viewModel.state,
            action = viewModel::onAction,
            userId = userId,
            onProductDisplay = { productName ->
                navController.navigate(ProductScreenRoute(userId = userId, productName = productName)) {
                    popUpTo(CheckoutScreenRoute(userId))
                }
            },
            onBackPressed = {
                navController.popBackStack()
            },
        )
    }
}