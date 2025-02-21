package com.example.beupdated.common.route

import kotlinx.serialization.Serializable

@Serializable
data class ProductDisplayRoute(val userId: String) {
    companion object {
        const val ROUTE = "com.example.beupdated.common.route.ProductDisplayRoute/{email}"
    }
}

@Serializable
data object AuthScreenRoute {
    const val ROUTE = "com.example.beupdated.common.route.AuthScreenRoute"
}

@Serializable
data object SignUpScreenRouteA {
    const val ROUTE = "com.example.beupdated.common.route.SignUpScreenRouteA"
}

@Serializable
data object SignUpScreenRouteB {
    const val ROUTE = "com.example.beupdated.common.route.SignUpScreenRouteB"
}

@Serializable
data object SignUpScreenRouteC {
    const val ROUTE = "com.example.beupdated.common.route.SignUpScreenRouteC"
}

@Serializable
data object SignUpScreenRouteD {
    const val ROUTE = "com.example.beupdated.common.route.SignUpScreenRouteD"
}

@Serializable
data class ProductScreenRoute(val productName: String, val userId: String)

@Serializable
data class CheckoutScreenRoute(val userId: String)

@Serializable
data object NotificationScreenRoute

@Serializable
data class PaymentSuccessfulScreenRoute(val userId: String, val dateForPickUp: String, val orderId: String)

@Serializable
data class SavedProductScreenRoute(val userId: String)

@Serializable
data class SearchScreenRoute(val userId: String)

@Serializable
data class ProfileScreenRoute(val userId: String)

@Serializable
data class OrderScreenRoute(val userId: String)

@Serializable
data class OrderDetailsScreenRoute(val orderId: String, val userId: String, val productId: String = "")



sealed class AppRoute(val route: String) {
    data object Auth: AppRoute(AuthScreenRoute.ROUTE)
    data object ProductDisplay: AppRoute(ProductDisplayRoute.ROUTE)
    data object SignUpA: AppRoute(SignUpScreenRouteA.ROUTE)
    data object SignUpB: AppRoute(SignUpScreenRouteB.ROUTE)
    data object SignUpC: AppRoute(SignUpScreenRouteC.ROUTE)
    data object SignUpD: AppRoute(SignUpScreenRouteD.ROUTE)
}


