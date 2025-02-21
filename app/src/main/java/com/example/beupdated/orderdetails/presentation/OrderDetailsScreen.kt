package com.example.beupdated.orderdetails.presentation

import android.widget.Toast
import androidx.collection.emptyLongSet
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R
import com.example.beupdated.checkout.presentation.composables.convertMillisToDate
import com.example.beupdated.checkout.presentation.composables.convertTimestampToTime
import com.example.beupdated.common.composables.ShimmerEffect
import com.example.beupdated.common.composables.imageFinder
import com.example.beupdated.orderdetails.domain.model.Order
import com.example.beupdated.orderdetails.domain.model.OrderDetails
import com.example.beupdated.orderdetails.domain.model.PickUpDetails
import com.example.beupdated.orderdetails.presentation.composables.AlertDialogErrorMessage
import com.example.beupdated.orderdetails.presentation.composables.OrderDetailsItem
import com.example.beupdated.paymentsuccess.domain.Receipt
import com.example.beupdated.paymentsuccess.presentation.PaymentAction
import com.example.beupdated.productdisplay.presentation.toDisplayDouble
import com.example.beupdated.ui.theme.BeUpdatedTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onPaymentSuccessfulScreen: () -> Unit,
    state: OrderDetailsState,
    action: (OrderDetailsAction) -> Unit,
    onProductScreen: (String) -> Unit
) {
    val context = LocalContext.current

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
                containerColor = colorResource(R.color.blue)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.width(320.dp),
                        onClick = {
                            onPaymentSuccessfulScreen()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.gold),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(
                            text = "Buy Again",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.blue)
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
            horizontalAlignment = Alignment.Start
        ) {
            if (state.orderDetail == null) {
                item {
                    OrderDetailsShimmer()
                }
            } else {
                val itemStatus = when (state.orderDetail.status) {
                    "pending" -> "for pick up"
                    "completed" -> "already received"
                    "cancelled" -> "cancelled"
                    else -> ""
                }
                item {
                    Spacer(Modifier.height(20.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        text = "Your Order is ${if (state.orderDetail.status == "pending") "for pick up" else state.orderDetail.status}",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(R.drawable.truckicon),
                            contentDescription = null
                        )
                        Text(
                            text = "Your item is $itemStatus",
                            fontSize = 20.sp
                        )
                    }
                    Spacer(Modifier.height(15.dp))
                    HorizontalDivider(Modifier.fillMaxWidth())
                }

                items(state.orderDetail.products, key = { item -> item.productId }) { item ->
                    OrderDetailsItem(
                        image = imageFinder(item.productName),
                        productName = item.productName,
                        productSize = item.productSize,
                        qty = item.productQuantity.toString(),
                        price = item.productPrice.toDouble().toDisplayDouble(),
                        totalPrice = (item.productPrice.toDouble() * item.productQuantity).toDisplayDouble(),
                        productColor = item.productColor,
                        onProductScreen = {
                            onProductScreen(item.productName)
                        }
                    )
                }

                item {
                    HorizontalDivider(Modifier.fillMaxWidth())
                    Spacer(Modifier.height(15.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        text = "Details",
                        fontSize = 20.sp
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        text = "Order Placed",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(R.drawable.dateicon),
                            contentDescription = null
                        )

                        Text(
                            text = convertMillisToDate(state.orderDetail.timeStamp.toLong()),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light
                        )

                        Spacer(Modifier.width(15.dp))

                        Image(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(R.drawable.timeicon),
                            contentDescription = null
                        )

                        Text(
                            text = convertTimestampToTime(state.orderDetail.timeStamp.toLong()),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light,
                        )
                    }
                }


                if (itemStatus == "already received") {
                    item {
                        Spacer(Modifier.height(15.dp))
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            text = "Order Picked-Up",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(R.drawable.dateicon),
                                contentDescription = null
                            )

                            Text(
                                text = state.orderDetail.pickUpDetails.date,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light,
                            )

                            Spacer(Modifier.width(15.dp))

                            Image(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(R.drawable.timeicon),
                                contentDescription = null
                            )

                            Text(
                                text = state.orderDetail.pickUpDetails.time,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light,
                            )
                        }
                    }
                }

                if (itemStatus == "cancelled") {
                    item {
                        Spacer(Modifier.height(15.dp))
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            text = "Cancelled",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(R.drawable.dateicon),
                                contentDescription = null
                            )

                            Text(
                                text = state.orderDetail.pickUpDetails.date,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light,
                            )

                            Spacer(Modifier.width(15.dp))

                            Image(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(R.drawable.timeicon),
                                contentDescription = null
                            )

                            Text(
                                text = state.orderDetail.pickUpDetails.time,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light,
                            )
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(15.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        text = "Order ID",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        text = state.orderDetail.productId,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light,
                    )
                }

                item {
                    Spacer(Modifier.height(15.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        text = "Total",
                        fontSize = 20.sp
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        text = state.totalPrice.toDisplayDouble().formatted,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                item {
                    Spacer(Modifier.height(15.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),

                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "E-Invoice",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        OutlinedButton(
                            onClick = {
                                val receipt = Receipt(
                                    orderId = state.orderDetail.productId,
                                    pickUpDate = state.orderDetail.pickUpDetails.dateForPickUp,
                                    status = state.orderDetail.status
                                )
                                action(OrderDetailsAction.OnGenerateReceipt(receipt))
                            }
                        ) {
                            Text(
                                text = "Download",
                                color = Color.Black
                            )
                        }
                    }
                }

                if (itemStatus == "for pick up" && !state.approval) {
                    item {
                        Spacer(Modifier.height(15.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    action(OrderDetailsAction.OnShowConfirmation)
                                }
                            ) {
                                Text(
                                    text = "Cancel Order",
                                    color = Color.Red.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(20.dp))
                }
            }

        }
        if (state.message == "Are you sure you want to cancel your order?" && state.orderDetail != null) {
            AlertDialogErrorMessage(
                action = action,
                errorMessage = state.message,
                userId = state.orderDetail.userId,
                orderId = state.orderDetail.productId,
                showCancelOrderWarning = true
            )
        }

        if (state.message.isNotEmpty() && state.message != "Are you sure you want to cancel your order?") {
            AlertDialogErrorMessage(
                action = action,
                errorMessage = state.message,
                userId = state.orderDetail?.userId,
                orderId = state.orderDetail?.productId,
                showCancelOrderWarning = false
            )
        }

        if (state.receiptPath.isNotEmpty()) {
            println("Receipt path: ${state.receiptPath}")
            Toast.makeText(
                context,
                "Receipt downloaded successfully",
                Toast.LENGTH_SHORT
            ).show()
            action(OrderDetailsAction.OnResetReceiptPath)
        }
    }
}

@Composable
fun OrderDetailsShimmer(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
        ShimmerEffect(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .height(30.dp)
                .fillMaxWidth()
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
        )
        Row {
            ShimmerEffect(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .size(25.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
            )
            ShimmerEffect(
                modifier = Modifier
                    .height(25.dp)
                    .width(200.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
            )
        }

        HorizontalDivider(Modifier.padding(top = 10.dp))

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
                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ShimmerEffect(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .height(15.dp)
                                .width(75.dp)
                                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                        )

                        ShimmerEffect(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .height(15.dp)
                                .width(75.dp)
                                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                        )
                    }
                }
            }
        }
        HorizontalDivider(Modifier.padding(vertical = 10.dp))
        ShimmerEffect(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
                .height(15.dp)
                .width(100.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
        )
        ShimmerEffect(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(25.dp)
                .width(150.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
        )
        Row(modifier = Modifier.padding(vertical = 5.dp)) {
            repeat(2) {
                ShimmerEffect(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .size(25.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                )
                ShimmerEffect(
                    modifier = Modifier
                        .height(25.dp)
                        .width(100.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                )
            }
        }
    }

}

@Preview
@Composable
private fun Preview() {
    BeUpdatedTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            OrderDetailsShimmer()
        }
    }
//    OrderDetailsScreen(
//        onPaymentSuccessfulScreen = {},
//        onBackPressed = {},
//        state = OrderDetailsState(
//            orderDetail = OrderDetails(
//                status = "pending",
//                products = listOf(
//                    Order(
//                        productColor = "White",
//                        productId = "LKASJDLKSAJD",
//                        productName = "Tote Bag",
//                        productPrice = "100.00",
//                        productQuantity = 1,
//                        productSize = "M"
//                    ), Order(
//                        productColor = "White",
//                        productId = "LKASJDLKSAJD",
//                        productName = "Tote Bag",
//                        productPrice = "100.00",
//                        productQuantity = 1,
//                        productSize = "M"
//                    )
//                ),
//                paymentMethod = "Gcash",
//                pickUpDetails = PickUpDetails(
//                    date = "",
//                    dateForPickUp = "October 24, 2025",
//                    time = "11:32:10"
//                ),
//                timeStamp = "1739155455076",
//                userId = "",
//                productId = "",
//                approval = false
//            ),
//            isLoading = false,
//            message = ""
//        ),
//        action = {},
//        onProductScreen = {}
//    )
}
