package com.example.beupdated.product.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R
import com.example.beupdated.authentication.presentation.composables.AlertDialogErrorMessage
import com.example.beupdated.common.composables.AlertDialogCircularProgressIndicator
import com.example.beupdated.common.composables.ShimmerEffect
import com.example.beupdated.common.composables.imageFinder
import com.example.beupdated.productdisplay.presentation.toDisplayDouble

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    state: ProductState,
    onBackPressed: () -> Unit,
    onCheckoutScreen: () -> Unit,
    onSaveProduct: () -> Unit,
    action: (ProductAction) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 5.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.gold).copy(alpha = 0f)
                ),
                title = {
                    Text(
                        text = "Product",
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackPressed()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Arrow Back",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(colorResource(R.color.blue)),
                        onClick = {
                            if (state.selectedColor == "Select color") {
                                Toast.makeText(
                                    context,
                                    "Select a color variation first",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (state.selectedSize == "Select size" && state.product?.size.toString() != "[]") {
                                Toast.makeText(
                                    context,
                                    "Select a size variation first",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                println("Else is triggered (onSaveProduct)")
                                onSaveProduct()
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Wishlist Button",
                            tint = Color.White
                        )

                        Text(
                            text = "Saved to wishlist",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                    TextButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(colorResource(R.color.gold)),
                        onClick = {
                            if (state.selectedColor == "Select color") {
                                Toast.makeText(
                                    context,
                                    "Select a color variation first",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (state.selectedSize == "Select size" && state.product?.size.toString() != "[]") {
                                Toast.makeText(
                                    context,
                                    "Select a size variation first",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                println("Else is triggered (onCheckoutScreen)")
                                action(ProductAction.OnReserveProduct)
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = "Home Button",
                            tint = Color.Black
                        )

                        Text(
                            text = "Make a reservation",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        var expandedSize by remember { mutableStateOf(false) }
        var expandedColor by remember { mutableStateOf(false) }

        if (state.productSavedSuccessfully) {
            Toast.makeText(context, "Product saved successfully", Toast.LENGTH_SHORT).show()
            action(ProductAction.OnResetProductSaved)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {
            if (state.product == null) {
                item {
                    ProductShimmer()
                }
            } else {
                item {
                    Card(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(start = 10.dp, end = 10.dp, bottom = 20.dp),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(imageFinder(state.productName)),
                            contentDescription = "Image of product",
                            contentScale = ContentScale.Crop
                        )
                    }
                }


                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = state.productName,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 34.sp
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = "P ${state.product.price.toDouble().toDisplayDouble().formatted}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Light
                    )
                }

                item {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }

                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = "Stock Quantity: ${state.product.stockQuantity}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(10.dp))
                    Box {
                        Row(
                            modifier = Modifier
                                .clickable { expandedColor = !expandedColor },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                text = if (state.selectedColor == "Select color") "Color: Select" else "Color: ${state.selectedColor}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                Icons.Default.ArrowDropDown,
                                "Arrow drop down"
                            )
                        }
                        DropdownMenu(
                            modifier = Modifier.background(Color.White),
                            expanded = expandedColor,
                            onDismissRequest = {
                                expandedColor = false
                            }
                        ) {
                            state.product.color.forEach { color ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            fontSize = 24.sp,
                                            text = color,
                                            color = Color.Black
                                        )
                                    },
                                    onClick = {
                                        action(ProductAction.OnChangedColor(color))
                                        expandedColor = false
                                    }
                                )
                            }
                        }
                    }
                    if (state.product.size.toString() != "[]") {
                        Spacer(Modifier.height(10.dp))
                        Box {
                            Row(
                                modifier = Modifier
                                    .clickable { expandedSize = !expandedSize },
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    text = if (state.selectedSize == "Select size") "Size: Select" else "Size: ${state.selectedSize}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    "Arrow drop down"
                                )
                            }
                            DropdownMenu(
                                modifier = Modifier.background(Color.White),
                                expanded = expandedSize,
                                onDismissRequest = { expandedSize = false }
                            ) {
                                state.product.size.forEach { size ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                fontSize = 24.sp,
                                                text = size,
                                                color = Color.Black
                                            )
                                        },
                                        onClick = {
                                            action(ProductAction.OnChangedSize(size))
                                            expandedSize = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = state.product.description,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }

        }
        if (state.isLoading) {
            AlertDialogCircularProgressIndicator(
                modifier = Modifier.alpha(1f),
                containerColor = Color.Transparent
            )
        }
        if (state.message.isNotEmpty()) {
            AlertDialogErrorMessage(
                action = {
                    action(ProductAction.OnResetError)
                },
                errorMessage = state.message
            )
        }
        if (state.reserveSuccess) {
            onCheckoutScreen()
        }
    }
}

@Composable
fun ProductShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ShimmerEffect(
            modifier = modifier
                .aspectRatio(1f)
                .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(20.dp))
        )
        ShimmerEffect(
            modifier = modifier
                .padding(horizontal = 10.dp)
                .height(25.dp)
                .fillMaxWidth()
                .background(Color.LightGray, shape = RoundedCornerShape(5.dp))
        )
        HorizontalDivider(Modifier.padding(10.dp))
        repeat(3) {
            ShimmerEffect(
                modifier = modifier
                    .padding(horizontal = 10.dp)
                    .height(15.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray, shape = RoundedCornerShape(5.dp))
            )
        }
        ShimmerEffect(
            modifier = modifier
                .padding(horizontal = 10.dp)
                .height(50.dp)
                .fillMaxWidth()
                .background(Color.LightGray, shape = RoundedCornerShape(5.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductScreenPrev() {
    ProductScreen(
        onBackPressed = {},
        onCheckoutScreen = {},
        state = ProductState(),
        action = {},
        onSaveProduct = {}
    )
}