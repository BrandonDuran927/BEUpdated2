package com.example.beupdated.savedproduct.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R
import com.example.beupdated.common.composables.ShimmerEffect
import com.example.beupdated.common.composables.imageFinder
import com.example.beupdated.productdisplay.presentation.composables.SearchBar
import com.example.beupdated.productdisplay.presentation.toDisplayDouble
import com.example.beupdated.savedproduct.presentation.composables.AlertDialogErrorMessage
import com.example.beupdated.savedproduct.presentation.composables.SavedItem
import com.example.beupdated.ui.theme.BeUpdatedTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedProductScreen(
    modifier: Modifier = Modifier,
    onCheckoutScreen: () -> Unit,
    onNotificationScreen: () -> Unit,
    onProductDisplayScreen: () -> Unit,
    onProductScreen: (String) -> Unit,
    onSearchScreen: () -> Unit,
    onProfileScreen: () -> Unit,
    onOrderScreen: () -> Unit,
    action: (SavedProductAction) -> Unit,
    userId: String,
    state: SavedProductState
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
                        onClick = {/* no-op */}
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Save Button",
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
                        onClick = {
                            onOrderScreen()
                        }
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
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.width(150.dp),
                containerColor = colorResource(R.color.gold),
                onClick = {
                    if (state.checkProducts.isNotEmpty()) {
                        onCheckoutScreen()
                    }
                }
            ) {
                Text(
                    text = "Check out",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                state.isLoading -> {
                    repeat(6) {
                        item {
                            SavedProductShimmer()
                            HorizontalDivider(modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
                state.savedProducts.isEmpty() -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .fillParentMaxHeight(),
                            contentAlignment = Alignment.Center // Center content
                        ) {
                            Text(
                                modifier = Modifier.alpha(0.7f),
                                text = "Saved products are empty. Go save one!",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                else -> {
                    items(state.savedProducts, key = { it.productId }) { item ->
                        SavedItem(
                            image = imageFinder(item.productName),
                            productId = item.productId,
                            productName = item.productName,
                            productSize = item.productSize,
                            productColor = item.productColor,
                            totalPrice = item.productPrice.toDouble().toDisplayDouble(),
                            quantity = item.quantity,
                            action = action,
                            userId = userId,
                            onCheck = item.onCheckProduct,
                            savedAt = item.savedAt,
                            onProductScreen = {
                                onProductScreen(item.productName)
                            }
                        )
                        HorizontalDivider(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
        if (state.message.isNotEmpty()) {
            AlertDialogErrorMessage(
                action = action,
                errorMessage = state.message,
                showQuantityWarning = true,
                userId = userId,
                productId = state.currentProductToRemove
            )
        }
    }
}

@Composable
fun SavedProductShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ShimmerEffect(
            modifier = Modifier
                .padding(20.dp)
                .size(20.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(50))
        )
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
            ShimmerEffect(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .height(10.dp)
                    .width(100.dp)
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
            Row {
                ShimmerEffect(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .size(20.dp)
                        .background(Color.LightGray)
                )
                ShimmerEffect(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .height(20.dp)
                        .width(50.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                )
                ShimmerEffect(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color.LightGray)
                )
            }
        }
    }
}

@Preview
@Composable
private fun SavedProductScreenPrev() {
    BeUpdatedTheme {
        SavedProductScreen(
            onCheckoutScreen = {},
            onNotificationScreen = {},
            onProductDisplayScreen = {},
            onSearchScreen = {},
            onProfileScreen = {},
            onOrderScreen = {},
            action = {},
            userId = "",
            state = SavedProductState().copy(isLoading = true),
            onProductScreen = {}
        )
    }
}