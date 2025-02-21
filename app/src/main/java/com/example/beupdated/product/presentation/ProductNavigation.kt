package com.example.beupdated.product.presentation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.beupdated.common.route.CheckoutScreenRoute
import com.example.beupdated.common.route.ProductScreenRoute

fun NavGraphBuilder.productNavGraph(
    navController: NavController,
    productViewModel: ProductViewModel
) {
    composable<ProductScreenRoute> {
        val productName = it.toRoute<ProductScreenRoute>().productName
        val userId = it.toRoute<ProductScreenRoute>().userId

        LaunchedEffect(true, productName, userId) {
            if (productName.isNotEmpty() && userId.isNotEmpty()) {
                productViewModel.onAction(ProductAction.OnLoadProduct(productName, userId))
            }
        }

        ProductScreen(
            onBackPressed = {
                productViewModel.onAction(ProductAction.OnResetState)
                navController.popBackStack()
            },
            onCheckoutScreen = {
                productViewModel.onAction(ProductAction.OnResetState)
                navController.navigate(CheckoutScreenRoute(userId)) {
                    popUpTo(ProductScreenRoute(productName, userId))
                }
            },
            onSaveProduct = {
                productViewModel.onAction(ProductAction.OnSaveProduct)
            },
            state = productViewModel.state,
            action = productViewModel::onAction
        )
    }
}