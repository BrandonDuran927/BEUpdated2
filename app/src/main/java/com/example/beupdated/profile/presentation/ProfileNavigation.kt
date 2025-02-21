package com.example.beupdated.profile.presentation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.beupdated.authentication.presentation.AuthViewModel
import com.example.beupdated.common.route.AuthScreenRoute
import com.example.beupdated.common.route.NotificationScreenRoute
import com.example.beupdated.common.route.OrderScreenRoute
import com.example.beupdated.common.route.ProductDisplayRoute
import com.example.beupdated.common.route.ProfileScreenRoute
import com.example.beupdated.common.route.SavedProductScreenRoute
import com.example.beupdated.common.route.SearchScreenRoute
import com.example.beupdated.productdisplay.presentation.ProductDisplayAction
import com.example.beupdated.productdisplay.presentation.ProductDisplayViewModel

fun NavGraphBuilder.profileNavGraph(
    navController: NavController,
    authViewModel: AuthViewModel,
    productDisplayViewModel: ProductDisplayViewModel,
    profileViewModel: ProfileViewModel
) {
    composable<ProfileScreenRoute> {
        val userId = it.toRoute<ProfileScreenRoute>().userId

        LaunchedEffect(Unit) {
            profileViewModel.onAction(ProfileAction.OnFetchUserInfo(userId))
        }

        LaunchedEffect(profileViewModel.state.deleteSuccess) {
            if (profileViewModel.state.deleteSuccess) {
                authViewModel.resetUser()
                productDisplayViewModel.onAction(ProductDisplayAction.OnLogoutUser)
                profileViewModel.onAction(ProfileAction.OnResetState)
                navController.navigate(AuthScreenRoute) {
                    popUpTo(ProductDisplayRoute(userId)) {
                        inclusive = true
                    }
                }
            }
        }



        ProfileScreen(
            state = profileViewModel.state,
            action = profileViewModel::onAction,
            userId = userId,
            onNotificationScreen = {
                navController.navigate(NotificationScreenRoute) {
                    popUpTo(ProfileScreenRoute(userId))
                }
            },
            onSavedProductScreen = {
                navController.navigate(SavedProductScreenRoute(userId)) {
                    popUpTo(ProfileScreenRoute(userId))
                }
            },
            onSearchScreen = {
                navController.navigate(SearchScreenRoute(userId)) {
                    popUpTo(ProfileScreenRoute(userId))
                }
            },
            onProductDisplayScreen = {
                navController.navigate(ProductDisplayRoute(userId)) {
                    popUpTo(ProfileScreenRoute(userId)) {
                        inclusive = true
                    }
                }
            },
            onOrderScreen = {
                navController.navigate(OrderScreenRoute(userId)) {
                    popUpTo(ProfileScreenRoute(userId)) {
                        inclusive = true
                    }
                }
            },
            onLogout = {
                authViewModel.resetUser()
                productDisplayViewModel.onAction(ProductDisplayAction.OnLogoutUser)
                navController.navigate(AuthScreenRoute) {
                    popUpTo(ProductDisplayRoute("")) {
                        inclusive = true
                    }
                }
            }
        )
    }
}