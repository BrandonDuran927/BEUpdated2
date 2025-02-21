package com.example.beupdated.checkout.presentation

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R
import com.example.beupdated.checkout.presentation.composables.AlertDialogCreditCard
import com.example.beupdated.checkout.presentation.composables.AlertDialogErrorMessage
import com.example.beupdated.checkout.presentation.composables.AlertDialogGcash
import com.example.beupdated.checkout.presentation.composables.CheckoutItem
import com.example.beupdated.checkout.presentation.composables.DatePickerFieldToModal
import com.example.beupdated.common.composables.AlertDialogCircularProgressIndicator
import com.example.beupdated.common.composables.ShimmerEffect
import com.example.beupdated.common.composables.imageFinder
import com.example.beupdated.productdisplay.presentation.DisplayableNumber
import com.example.beupdated.productdisplay.presentation.toDisplayDouble
import com.example.beupdated.savedproduct.presentation.SavedProductShimmer
import com.example.beupdated.ui.theme.BeUpdatedTheme
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    state: CheckOutState,
    action: (CheckOutAction) -> Unit,
    userId: String,
    onBackPressed: () -> Unit,
    onProductDisplay: (String) -> Unit
) {
    var selectedOption by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 5.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                title = {
                    Text(
                        text = "Order Details",
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
                modifier = Modifier.padding(vertical = 20.dp),
                containerColor = colorResource(R.color.gold)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 30.dp,
                                vertical = 5.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )

                        Text(
                            text = "P ${state.totalPrice.toDisplayDouble().formatted}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        modifier = Modifier.width(320.dp),
                        onClick = {
                            if (state.pickUpDateLong != 0L) {
                                val currentDate = System.currentTimeMillis()
                                val twoDaysFromNow = currentDate + (2 * 24 * 60 * 60 * 1000L)
                                val oneMonthFromNow = currentDate + (30 * 24 * 60 * 60 * 1000L)
                                val calendar = Calendar.getInstance().apply {
                                    timeInMillis = state.pickUpDateLong
                                }
                                val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                                when {
                                    dayOfWeek == Calendar.SUNDAY -> {
                                        action(CheckOutAction.OnShowMessage("School is closed on Sundays. Please select another day."))
                                    }
                                    state.pickUpDateLong < currentDate -> {
                                        action(CheckOutAction.OnShowMessage("Please select a future date"))
                                    }
                                    state.pickUpDateLong < twoDaysFromNow -> {
                                        action(CheckOutAction.OnShowMessage("Please select a date at least 2 days from today"))
                                    }
                                    state.pickUpDateLong > oneMonthFromNow -> {
                                        action(CheckOutAction.OnShowMessage("Please select a date within the next month"))
                                    }
                                    state.totalPrice != 0.00 -> {
                                        action(CheckOutAction.OnPaymentMethod(selectedOption))
                                    }
                                }
                            } else {
                                action(CheckOutAction.OnShowMessage("Please select a pickup date"))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.blue),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
            horizontalAlignment = Alignment.Start
        ) {
            if (state.checkProducts.isEmpty()) {
                item {
                    CheckOutShimmer()
                }
            } else {
                items(state.checkProducts, key = { it.productId }) { item ->
                    CheckoutItem(
                        image = imageFinder(item.productName),
                        productName = item.productName,
                        productSize = item.productSize,
                        totalPrice = (item.productPrice.toDouble() * item.quantity).toDisplayDouble(),
                        quantity = item.quantity,
                        productColor = item.productColor,
                        onIncrement = {
                            action(
                                CheckOutAction.OnIncrementQuantity(
                                    userId = userId,
                                    savedAt = item.savedAt
                                )
                            )
                        },
                        onDecrement = {
                            action(
                                CheckOutAction.OnDecrementQuantity(
                                    userId = userId,
                                    productId = item.productId
                                )
                            )
                        },
                        onProductDisplay = { productName ->
                            onProductDisplay(productName)
                        }
                    )

                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                }


                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                        text = "Payment Method",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Gcash Option
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.padding(start = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    modifier = Modifier.size(30.dp),
                                    painter = painterResource(R.drawable.gcashlogo),
                                    contentDescription = "Gcash option"
                                )

                                Text(
                                    text = "Gcash",
                                    fontSize = 22.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                            // Gcash RadioButton
                            RadioButton(
                                selected = selectedOption == "Gcash",
                                onClick = {
                                    selectedOption = "Gcash"
                                },
                                modifier = Modifier.padding(end = 10.dp),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color.Black,
                                    unselectedColor = Color.Black
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        // Credit Card Option
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.padding(start = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    modifier = Modifier.size(30.dp),
                                    painter = painterResource(R.drawable.mastercardlogo),
                                    contentDescription = "Credit Card"
                                )

                                Text(
                                    text = "Credit Card",
                                    fontSize = 22.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                            // Credit Card RadioButton
                            RadioButton(
                                selected = selectedOption == "Credit Card",
                                onClick = {
                                    selectedOption = "Credit Card"
                                },
                                modifier = Modifier.padding(end = 10.dp),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color.Black,
                                    unselectedColor = Color.Black
                                )
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                }

                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                        text = "Date of retrieval",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        DatePickerFieldToModal(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            action = action,
                            state = state
                        )
                    }
                }
            }
        }
        if (state.message.isNotEmpty()) {
            AlertDialogErrorMessage(
                action = action,
                errorMessage = state.message,
                userId = userId,
                productId = "",
                showQuantityWarning = false
            )
        }

        if (state.isLoading) {
            AlertDialogCircularProgressIndicator(
                modifier = Modifier.alpha(1f),
                containerColor = Color.Transparent
            )
        }

        if (state.onPayment.isNotEmpty() && state.onPayment == "Gcash") {
            AlertDialogGcash(
                title = "Gcash",
                action = action,
                state = state,
                onDismiss = {
                    action(CheckOutAction.OnDismissPayment)
                },
                onConfirm = {
                    action(CheckOutAction.VerifyPayment(
                        paymentMethod = "Gcash",
                        userId = userId
                    ))
                }
            )
        } else if (state.onPayment.isNotEmpty() && state.onPayment == "Credit Card") {
            AlertDialogCreditCard(
                title = "Credit Card",
                action = action,
                state = state,
                onDismiss = {
                    action(CheckOutAction.OnDismissPayment)
                },
                onConfirm = {
                    action(CheckOutAction.VerifyPayment(
                        paymentMethod = "Credit Card",
                        userId = userId
                    ))
                }
            )
        }
    }
}

@Composable
fun CheckOutShimmer(modifier: Modifier = Modifier) {
    repeat(3) {
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
    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalDivider(Modifier.height(10.dp))
        Spacer(Modifier.height(10.dp))
        ShimmerEffect(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(25.dp)
                .width(150.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.height(20.dp))
        repeat(times = 2) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Row {
                    ShimmerEffect(
                        modifier = Modifier
                            .size(25.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(5.dp))
                    )
                    Spacer(Modifier.width(15.dp))
                    ShimmerEffect(
                        modifier = Modifier
                            .width(100.dp)
                            .height(25.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(5.dp))
                    )
                }
                ShimmerEffect(
                    modifier = Modifier
                        .size(25.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(5.dp))
                )
            }
            Spacer(Modifier.height(20.dp))
        }
        HorizontalDivider(Modifier.height(20.dp))

        ShimmerEffect(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(25.dp)
                .width(150.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.height(20.dp))
        ShimmerEffect(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
        )
    }
}

@Preview
@Composable
private fun CheckOutScreenPrev() {
    BeUpdatedTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            CheckOutShimmer()
        }
    }
//    CheckoutScreen(
//        state = CheckOutState(),
//        action = {},
//        onBackPressed = {
//
//        },
//        onPaymentSuccessfulScreen = {},
//        userId = ""
//    )
}



