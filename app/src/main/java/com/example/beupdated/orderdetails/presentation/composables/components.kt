package com.example.beupdated.orderdetails.presentation.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R
import com.example.beupdated.orderdetails.presentation.OrderDetailsAction
import com.example.beupdated.productdisplay.presentation.DisplayableNumber
import com.example.beupdated.productdisplay.presentation.toDisplayDouble

@Composable
fun OrderDetailsItem(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int,
    productName: String,
    productSize: String,
    productColor: String,
    qty: String,
    price: DisplayableNumber,
    totalPrice: DisplayableNumber,
    onProductScreen: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onProductScreen(productName)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            modifier = Modifier
                .size(100.dp)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(image),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp),
        ) {
            Text(
                text = productName,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            if (productSize.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = productSize,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontWeight = FontWeight.Light,
                )
            }

            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = productColor,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontWeight = FontWeight.Light,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = "P ${price.formatted}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    maxLines = 1
                )

                Text(
                    text = "x${qty}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                )
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = "Total:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )

                Text(
                    text = "P ${totalPrice.formatted}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun AlertDialogErrorMessage(
    action: (OrderDetailsAction) -> Unit,
    errorMessage: String,
    showCancelOrderWarning: Boolean = false,
    userId: String?,
    orderId: String?,
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {
            action(OrderDetailsAction.OnResetError)
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = null
                )
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                if (showCancelOrderWarning) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                if (userId != null && orderId != null) {
                                    action(OrderDetailsAction.OnCancelOrder(userId = userId, orderId = orderId))
                                }
                            }
                        ) {
                            Text(text = "Yes", color = Color.Black)
                        }
                        Spacer(Modifier.width(20.dp))
                        Button(
                            onClick = {
                                action(OrderDetailsAction.OnResetError)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.gold),
                                contentColor = colorResource(R.color.blue)
                            )
                        ) {
                            Text(text = "No", color = Color.Black)
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun Prev() {
    OrderDetailsItem(
        image = R.drawable.sportsbag,
        productName = "Sports Bag",
        productSize = "M",
        qty = "2",
        price = 150.00.toDisplayDouble(),
        totalPrice = 300.00.toDisplayDouble(),
        productColor = "Blue",
        onProductScreen = {}
    )
}