package com.example.beupdated.notification.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.beupdated.common.route.NotificationScreenRoute
import com.example.beupdated.common.route.ProductScreenRoute

fun NavGraphBuilder.notificationNavGraph(
    navController: NavController
) {
    composable<NotificationScreenRoute> {
        NotificationScreen(
            onBackPressed = {
                navController.popBackStack()
            },
            onProductScreen = {
                /* no-op */
            }
        )
    }
}