package com.example.beupdated.order.presentation.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R
import com.example.beupdated.authentication.presentation.AuthAction
import com.example.beupdated.checkout.presentation.composables.QuantityModifier
import com.example.beupdated.order.presentation.OrderAction
import com.example.beupdated.productdisplay.presentation.DisplayableNumber
import com.example.beupdated.productdisplay.presentation.toDisplayDouble

@Composable
fun OrderItem(
    modifier: Modifier = Modifier,
    status: String,
    @DrawableRes image: Int,
    productName: String,
    productSize: String,
    productColor: String,
    qty: String,
    totalPrice: DisplayableNumber,
    onOrderDetailScreen: () -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onOrderDetailScreen()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            modifier = Modifier
                .size(120.dp)
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
                fontSize = 24.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = if (productSize == "Select size") productColor else "$productSize, $productColor",
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = "P ${totalPrice.formatted}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )

                Text(
                    text = "x${qty}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = status,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun AlertDialogErrorMessage(
    action: (OrderAction) -> Unit,
    errorMessage: String
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {
            action(OrderAction.OnResetError)
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
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun Prev() {
    OrderItem(
        image = R.drawable.traditionalmaleshs,
        productSize = "Large",
        productName = "Traditional F/M Uniform SHS",
        status = "Order completed",
        totalPrice = 240.00.toDisplayDouble(),
        qty = "1",
        onOrderDetailScreen = {},
        productColor = "Pink"
    )
}