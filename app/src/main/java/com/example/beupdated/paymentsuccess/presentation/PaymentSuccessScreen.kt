package com.example.beupdated.paymentsuccess.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R
import com.example.beupdated.common.composables.AlertDialogCircularProgressIndicator
import com.example.beupdated.paymentsuccess.domain.Receipt
import com.example.beupdated.paymentsuccess.presentation.composables.AlertDialogErrorMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentSuccessScreen(
    modifier: Modifier = Modifier,
    onProductDisplayScreen: () -> Unit,
    onOrderDetailsScreen: () -> Unit,
    dateForPickUp: String,
    orderId: String,
    action: (PaymentAction) -> Unit,
    state: PaymentState
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 25.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.gold).copy(alpha = 0f)
                ),
                title = {
                    Text(
                        text = "Payment",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onProductDisplayScreen()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Arrow Back",
                            modifier = Modifier.size(30.dp),
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { _ ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(R.drawable.paymentbg),
                contentDescription = "Background of payment"
            )

            Column(
                modifier = Modifier
                    .padding(top = 150.dp)
                    .width(350.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .size(50.dp),
                    painter = painterResource(R.drawable.checklogo),
                    contentDescription = null,
                )
                Spacer(Modifier.height(15.dp))
                Text(
                    text = "Payment Successful",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(15.dp))
                Text(
                    text = "Your payment was done successfully",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(15.dp))

                Text(
                    text = "Status: Pending",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    color = Color.Black.copy(alpha = 0.8f)
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    modifier = Modifier.width(150.dp),
                    text = "Date for pick-up: $dateForPickUp",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    color = Color.Black.copy(alpha = 0.8f)
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Order ID:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    color = Color.Black.copy(alpha = 0.8f)
                )
                Text(
                    text = orderId,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    color = Color.Black.copy(alpha = 0.8f)
                )
                Spacer(Modifier.height(20.dp))


                OutlinedButton(
                    modifier = Modifier.width(300.dp),
                    onClick = {
                        val receipt = Receipt(
                            orderId = orderId,
                            pickUpDate = dateForPickUp,
                            status = "Pending"
                        )
                        action(PaymentAction.OnDownloadReceipt(receipt))
                    }
                ) {
                    Text(
                        text = "Download Receipt",
                        fontSize = 20.sp,
                        color = colorResource(R.color.blue)
                    )
                }
                Button(
                    modifier = Modifier.width(300.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.gold)
                    ),
                    onClick = {
                        onOrderDetailsScreen()
                    }
                ) {
                    Text(
                        text = "View Order",
                        fontSize = 20.sp,
                        color = colorResource(R.color.blue)
                    )
                }
                Spacer(Modifier.height(20.dp))
            }

            if (state.isLoading) {
                AlertDialogCircularProgressIndicator(
                    modifier = Modifier.alpha(1f),
                    containerColor = Color.Transparent
                )
            }

            if (state.receiptPath.isNotEmpty()) {
                println("Receipt path: ${state.receiptPath}")
                Toast.makeText(
                    context,
                    "Receipt downloaded successfully",
                    Toast.LENGTH_SHORT
                ).show()
                action(PaymentAction.OnResetReceiptPath)
            }

            if (state.message.isNotEmpty()) {
                AlertDialogErrorMessage(
                    action = action,
                    errorMessage = state.message
                )
            }
        }
    }
}

@Preview
@Composable
private fun Prev() {
    PaymentSuccessScreen(
        onProductDisplayScreen = {},
        dateForPickUp = "October 30, 2024",
        orderId = "12021309919",
        action = {},
        state = PaymentState(),
        onOrderDetailsScreen = {}
    )
}