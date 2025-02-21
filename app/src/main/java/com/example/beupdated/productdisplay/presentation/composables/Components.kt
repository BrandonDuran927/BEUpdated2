package com.example.beupdated.productdisplay.presentation.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.beupdated.common.composables.imageFinder
import com.example.beupdated.productdisplay.presentation.DisplayableNumber
import com.example.beupdated.productdisplay.presentation.ProductDisplayAction
import com.example.beupdated.productdisplay.presentation.toDisplayDouble
import com.example.beupdated.registration.presentation.SignUpAction

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onNotificationScreen: () -> Unit,
    onSearchScreen: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "BEUpdated",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )

            Text(
                text = "Find the product you need!",
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
                color = Color.White
            )
        }

        IconButton(
            onClick = {
                onSearchScreen()
            }
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search Button",
                tint = Color.White
            )
        }

        IconButton(
            onClick = {
                onNotificationScreen()
            }
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Rounded.Notifications,
                contentDescription = "Notification Button",
                tint = Color.White
            )
        }
    }
}

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int = R.drawable.blankproduct,
    productName: String,
    productStock: String,
    productColors: List<String>,
    lastUpdated: String,
    produceSize: List<String>,
    productPrice: DisplayableNumber,
    onProductScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                onProductScreen()
            },
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 15.dp)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(imageFinder(productName)),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Text(
            modifier = Modifier.width(150.dp),
            text = productName,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            softWrap = false
        )
        Spacer(Modifier.height(6.dp))
        if (produceSize.toString() != "[]") {
            Text(
                modifier = Modifier.width(150.dp),
                text = "Size: ${produceSize.joinToString(", ")}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = false
            )
            Spacer(Modifier.height(6.dp))
        }
        Text(
            modifier = Modifier.width(150.dp),
            text = "Stock: $productStock"
        )
        Spacer(Modifier.height(6.dp))
        Text(
            modifier = Modifier.width(150.dp),
            text = "Colors: ${productColors.joinToString(", ")}"
        )
        Spacer(Modifier.height(6.dp))
        Text(
            modifier = Modifier.width(150.dp),
            text = lastUpdated
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "P ${productPrice.formatted}",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun AlertDialogErrorMessage(
    action: (ProductDisplayAction) -> Unit,
    errorMessage: String
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {
            action(ProductDisplayAction.OnResetError)
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
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
private fun SearchBarPrev() {
    val tmpPrice: Double = 400.00

    ProductItem(
        image = R.drawable.beupdatedlogo,
        productName = "Tourism Suit asldfkjasldkfjalskdj",
        productPrice = tmpPrice.toDisplayDouble(),
        onProductScreen = {},
        productStock = "45",
        productColors = listOf("White", "Blue"),
        lastUpdated = "12/12/2023",
        produceSize = listOf("s", "m", "l")
    )
}

