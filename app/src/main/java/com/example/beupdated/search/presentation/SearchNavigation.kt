package com.example.beupdated.search.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.beupdated.common.route.ProductDisplayRoute
import com.example.beupdated.common.route.ProductScreenRoute
import com.example.beupdated.common.route.SearchScreenRoute

fun NavGraphBuilder.searchNavGraph(
    navController: NavController,
    viewModel: SearchViewModel
) {
    composable<SearchScreenRoute> {
        val userId = it.toRoute<SearchScreenRoute>().userId

        SearchScreen(
            state = viewModel.state,
            action = viewModel::onAction,
            onProductScreen = { productName ->
                navController.navigate(ProductScreenRoute(productName = productName, userId = userId)) {
                    popUpTo(SearchScreenRoute(userId)) {
                        inclusive = true
                    }
                }
            }
        )
    }
}