package com.example.beupdated.order.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.beupdated.R
import com.example.beupdated.common.composables.ShimmerEffect
import com.example.beupdated.common.composables.imageFinder
import com.example.beupdated.order.presentation.composables.AlertDialogErrorMessage
import com.example.beupdated.order.presentation.composables.OrderItem
import com.example.beupdated.productdisplay.presentation.DisplayableNumber
import com.example.beupdated.productdisplay.presentation.composables.SearchBar
import com.example.beupdated.productdisplay.presentation.toDisplayDouble
import com.example.beupdated.ui.theme.BeUpdatedTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    modifier: Modifier = Modifier,
    onNotificationScreen: () -> Unit,
    onSearchScreen: () -> Unit,
    onSavedProductScreen: () -> Unit,
    onProductDisplayScreen: () -> Unit,
    onProfileScreen: () -> Unit,
    onOrderDetailsScreen: (String, String) -> Unit,
    action: (OrderAction) -> Unit,
    state: OrderState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.blue)
                ),
                title = {
                    SearchBar(
                        onNotificationScreen = {
                            onNotificationScreen()
                        },
                        onSearchScreen = {
                            onSearchScreen()
                        }
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = colorResource(R.color.blue)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    IconButton(
                        onClick = {
                            onSavedProductScreen()
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Saved Button",
                            tint = Color.White
                        )
                    }

                    Spacer(Modifier.width(50.dp))

                    IconButton(
                        onClick = {
                            onProductDisplayScreen()
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.Outlined.Home,
                            contentDescription = "Home Button",
                            tint = Color.White
                        )
                    }

                    Spacer(Modifier.width(50.dp))

                    IconButton(
                        onClick = {/* no-op */  }
                    ) {
                        Image(
                            modifier = Modifier.size(40.dp),
                            painter = painterResource(R.drawable.orderlogo),
                            contentDescription = "Order Button"
                        )

                    }

                    Spacer(Modifier.width(50.dp))

                    IconButton(
                        onClick = {
                            onProfileScreen()
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Profile Button",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.allProducts.isEmpty()) {
                item {
                    OrderShimmer()
                }
            } else {
                items(state.allProducts, key = { it.productId }) { item ->
                    OrderItem(
                        status = item.status,
                        productName = item.productName,
                        productSize = item.productSize,
                        qty = item.productQuantity.toString(),
                        image = imageFinder(item.productName),
                        totalPrice = (item.productPrice.toDouble() * item.productQuantity).toDisplayDouble(),
                        onOrderDetailScreen = {
                            onOrderDetailsScreen(item.orderId, item.productId)
                        },
                        productColor = item.productColor
                    )

                    HorizontalDivider(Modifier.fillMaxWidth())
                }
            }
        }
        if (state.message.isNotEmpty()) {
            AlertDialogErrorMessage(
                action = action,
                errorMessage = state.message
            )
        }
    }
}

@Composable
fun OrderShimmer(modifier: Modifier = Modifier) {
    repeat(5) {
        Row(
            modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .size(100.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(5.dp))
            )

            Column {
                ShimmerEffect(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .height(20.dp)
                        .width(150.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                )
                Spacer(Modifier.height(10.dp))
                ShimmerEffect(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .height(10.dp)
                        .width(100.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                )

                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ShimmerEffect(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .height(10.dp)
                            .width(75.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                    )

                    ShimmerEffect(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .height(10.dp)
                            .width(75.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                    )
                }
                Spacer(Modifier.height(10.dp))
                ShimmerEffect(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 20.dp)
                        .height(10.dp)
                        .width(100.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                )
            }
        }
        HorizontalDivider()
    }
}

@Preview
@Composable
private fun Prev() {
    BeUpdatedTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            OrderShimmer()
        }
    }
//    OrderScreen(
//        onSearchScreen = {},
//        onNotificationScreen = {},
//        onSavedProductScreen = {},
//        onProductDisplayScreen = {},
//        onProfileScreen = {},
//        onOrderDetailsScreen = { df, sdf ->
//
//        },
//        action = {},
//        state = OrderState()
//    )
}

internal val items = listOf(
    OrderModel(
        image = R.drawable.traditionalunif,
        size = "Large",
        name = "Traditional F/M Uniform SHS",
        status = "Pending",
        totalPrice = 249.00.toDisplayDouble(),
        qty = "1"
    ),
    OrderModel(
        image = R.drawable.traditionalunif,
        size = "Medium",
        name = "Traditional F/M Uniform SHS",
        status = "Canceled Successfully",
        totalPrice = 249.00.toDisplayDouble(),
        qty = "1"
    ),
    OrderModel(
        image = R.drawable.sportsbag,
        size = "Large",
        name = "National University Sports Bag",
        status = "Order Completed",
        totalPrice = 399.00.toDisplayDouble(),
        qty = "1"
    ),
)

internal data class OrderModel(
    @DrawableRes val image: Int,
    val size: String,
    val name: String,
    val status: String,
    val totalPrice: DisplayableNumber,
    val qty: String
)