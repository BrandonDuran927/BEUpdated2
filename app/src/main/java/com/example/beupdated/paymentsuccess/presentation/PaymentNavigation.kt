package com.example.beupdated.paymentsuccess.presentation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.beupdated.checkout.presentation.CheckOutAction
import com.example.beupdated.checkout.presentation.CheckOutViewModel
import com.example.beupdated.common.route.AppRoute
import com.example.beupdated.common.route.OrderDetailsScreenRoute
import com.example.beupdated.common.route.PaymentSuccessfulScreenRoute
import com.example.beupdated.common.route.ProductDisplayRoute
import com.example.beupdated.common.route.ProductScreenRoute

fun NavGraphBuilder.paymentNavGraph(
    paymentViewModel: PaymentViewModel,
    checkOutViewModel: CheckOutViewModel,
    navController: NavController
) {
    composable<PaymentSuccessfulScreenRoute> {
        val userId = it.toRoute<PaymentSuccessfulScreenRoute>().userId
        val orderId = it.toRoute<PaymentSuccessfulScreenRoute>().orderId
        val dateForPickUp = it.toRoute<PaymentSuccessfulScreenRoute>().dateForPickUp

        LaunchedEffect(Unit) {
            checkOutViewModel.onAction(CheckOutAction.OnResetState)
        }

        PaymentSuccessScreen(
            dateForPickUp = dateForPickUp,
            orderId = orderId,
            onProductDisplayScreen = {
                navController.navigate(ProductDisplayRoute(userId)) {
                    popUpTo(PaymentSuccessfulScreenRoute(
                        userId, orderId, dateForPickUp
                    )) {
                        inclusive = true
                    }
                }
                paymentViewModel.onAction(PaymentAction.OnResetState)
            },
            onOrderDetailsScreen = {
                navController.navigate(OrderDetailsScreenRoute(orderId = orderId, userId = userId)) {
                    popUpTo(PaymentSuccessfulScreenRoute(
                        userId, orderId, dateForPickUp
                    )) {
                        inclusive = true
                    }
                }
                paymentViewModel.onAction(PaymentAction.OnResetState)
            },
            action = paymentViewModel::onAction,
            state = paymentViewModel.state
        )
    }
}