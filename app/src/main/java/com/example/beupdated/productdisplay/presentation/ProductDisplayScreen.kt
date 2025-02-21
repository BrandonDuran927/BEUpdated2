package com.example.beupdated.productdisplay.presentation

import android.icu.text.NumberFormat
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R
import com.example.beupdated.authentication.presentation.composables.AlertDialogErrorMessage
import com.example.beupdated.common.composables.AlertDialogCircularProgressIndicator
import com.example.beupdated.common.composables.ShimmerEffect
import com.example.beupdated.core.network.NetworkStatus
import com.example.beupdated.productdisplay.domain.Product
import com.example.beupdated.productdisplay.presentation.composables.ProductItem
import com.example.beupdated.productdisplay.presentation.composables.SearchBar
import com.example.beupdated.ui.theme.BeUpdatedTheme
import com.google.firebase.auth.FirebaseUser
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDisplayScreen(
    state: ProductDisplayUIState,
    onProductScreen: (productName: String) -> Unit,
    onNotificationScreen: () -> Unit,
    onSavedProductScreen: () -> Unit,
    onSearchScreen: () -> Unit,
    onProfileScreen: () -> Unit,
    onOrderScreen: () -> Unit,
    action: (ProductDisplayAction) -> Unit
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
                        onClick = {/* no-op */}
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
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(),
                    painter = painterResource(R.drawable.homeimage),
                    contentDescription = "Home Image",
                    contentScale = ContentScale.Crop
                )
            }

            if (state.products.isEmpty()) {
                item {
                    repeat(2) {
                        Spacer(Modifier.height(15.dp))
                        Column {
                            ShimmerEffect(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(20.dp)
                                    .background(Color.LightGray, shape = RoundedCornerShape(5.dp))
                            )
                            Row {
                                ProductDisplayShimmer()
                                ProductDisplayShimmer()
                            }
                        }
                    }
                }
            } else {
                state.groupByCategory.forEach { (category, product) ->
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 10.dp,
                                    top = 10.dp
                                ),
                            text = category.uppercase(),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        )
                    }

                    item {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(product) { item ->
                                ProductItem(
                                    productName = item.name,
                                    productPrice = item.price.toDouble().toDisplayDouble(),
                                    productStock = item.stockQuantity.toString(),
                                    productColors = item.color,
                                    lastUpdated = item.lastUpdated,
                                    produceSize = item.size,
                                    onProductScreen = {
                                        onProductScreen(item.name)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        if (state.message.isNotEmpty()) {
            AlertDialogErrorMessage(
                action = { action(ProductDisplayAction.OnResetError) },
                errorMessage = state.message
            )
        }
    }
}

@Composable
fun ProductDisplayShimmer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ShimmerEffect(
            modifier = modifier
                .size(150.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
        )
        repeat(6) {
            ShimmerEffect(
                modifier = modifier
                    .width(100.dp)
                    .height(15.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(5.dp))
            )
        }
    }
}

@Preview
@Composable
private fun ProductDisplayShimmerPrev() {
    BeUpdatedTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            ProductDisplayShimmer()
        }
    }
}

//@Preview
//@Composable
//private fun ProductDisplayPrev() {
//    ProductDisplayScreen(
//        user = null,
//        action = {},
//        onLoggedOut = {},
//        onProductScreen = {},
//        onNotificationScreen = {},
//        onSavedProductScreen = {},
//        onSearchScreen = {},
//        onProfileScreen = {},
//        onOrderScreen = {},
//        state = ProductDisplayUIState()
//    )
//}

// FIXME: Temporary only due to date

//internal val uniformItems = listOf(
//    Items("Tourism Suit", 400.00.toDisplayDouble(), R.drawable.tourismsuit),
//    Items("Traditional Male Polo SHS", 300.00.toDisplayDouble(), R.drawable.traditionalmaleshs),
//    Items("Traditional F/M Uniform", 250.00.toDisplayDouble(), R.drawable.traditionalunif),
//)
//
//internal val tShirtsItems = listOf(
//    Items("White TS", 120.00.toDisplayDouble(), R.drawable.whitets),
//    Items("Black TS", 120.00.toDisplayDouble(), R.drawable.blackts),
//    Items("Go for the B/G TS", 180.00.toDisplayDouble(), R.drawable.bgts),
//)
//
//internal val accessories = listOf(
//    Items("Plastic Water Bottle", 260.00.toDisplayDouble(), R.drawable.plasticwaterbottle),
//    Items("Tumbler", 299.00.toDisplayDouble(), R.drawable.tumbler),
//    Items("Hydro Coffee", 90.00.toDisplayDouble(), R.drawable.hydrocoffee),
//)
//
//internal val bags = listOf(
//    Items("Sports Bag", 399.00.toDisplayDouble(), R.drawable.sportsbag),
//    Items("Tote Bag", 99.00.toDisplayDouble(), R.drawable.totebag),
//)
//
//data class Items(
//    val productName: String,
//    val productPrice: DisplayableNumber,
//    @DrawableRes val image: Int
//)
//

data class DisplayableNumber(
    val value: Double,
    val formatted: String
)

fun Double.toDisplayDouble(): DisplayableNumber {
    val formatter = NumberFormat.getInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    return DisplayableNumber(
        value = this,
        formatted = formatter.format(this)
    )
}