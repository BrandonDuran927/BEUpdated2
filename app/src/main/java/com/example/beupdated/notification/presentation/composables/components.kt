package com.example.beupdated.notification.presentation.composables

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
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int,
    itemName: String,
    description: String,
    time: String,
    onProductScreen: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onProductScreen()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
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
            modifier = Modifier.weight(1f).padding(horizontal = 10.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(itemName)
                    }

                    append(description)
                }, //transformation specific words,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(15.dp))

            Text(
                text = time,
                fontSize = 12.sp
            )
        }

        Text(
            modifier = Modifier.padding(end = 10.dp),
            text = "...",
            fontSize = 25.sp
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun Prev() {
    NotificationItem(
        image = R.drawable.totebag,
        itemName = "Bulldog Tote Bag",
        description = ", available in white and blue, currently has 45 units in stock as of now.",
        time = "1h ago",
        onProductScreen = {}
    )
}




data class NotificationModel(
    @DrawableRes val image: Int,
    val itemName: String,
    val description: String,
    val time: String
)