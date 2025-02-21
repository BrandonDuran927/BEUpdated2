package com.example.beupdated.notification.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R
import com.example.beupdated.notification.presentation.composables.NotificationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onProductScreen: () -> Unit
) {
    val local = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 5.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                title = {
                    Text(
                        text = "Notifications",
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
                containerColor = colorResource(R.color.gold)
            ) {
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {

                    }
                ) {
                    Icon(
                        Icons.Default.Email,
                        null,
                        tint = Color.Black,
                        modifier = Modifier.size(25.dp)
                    )
                    Text(
                        text = "Mark all as read",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(items) { item ->
                NotificationItem(
                    image = item.image,
                    itemName = item.itemName,
                    description = item.description,
                    time = item.time,
                    onProductScreen = {
                        onProductScreen()
                    }
                )

                HorizontalDivider(Modifier.fillMaxWidth())
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Prev() {
    NotificationScreen(
        onBackPressed = {

        },
        onProductScreen = {

        }
    )
}

internal val items = listOf(
    NotificationModel(
        image = R.drawable.plasticwaterbottle,
        itemName = "Plastic water bottle",
        description = ", available in white and blue, currently has 45 units in stock as of now",
        "1h ago"
    ),
    NotificationModel(
        image = R.drawable.tumbler,
        itemName = "Tumbler",
        description = ", available in gray, black, blue, and white, has 80 units in stock as of now",
        "2h ago"
    ),
    NotificationModel(
        image = R.drawable.sportsbag,
        itemName = "Sports bag",
        description = ", available in black, has 15 units in stock as of now",
        "2h ago"
    ),
    NotificationModel(
        image = R.drawable.bgts,
        itemName = "Go for the B/G TS",
        description = ", available in sizes S, M, L, and XL, has 20 units in stock as of now",
        "3h ago"
    ),
    NotificationModel(
        image = R.drawable.blackts,
        itemName = "Black TS",
        description = ", available in sizes XL and M, has 50 units in stock as of October 18, 2024",
        "4h ago"
    ),
    NotificationModel(
        image = R.drawable.plasticwaterbottle,
        itemName = "Plastic water bottle",
        description = ", available in white and blue, currently has 45 units in stock as of now",
        "1h ago"
    ),
    NotificationModel(
        image = R.drawable.tumbler,
        itemName = "Tumbler",
        description = ", available in gray, black, blue, and white, has 80 units in stock as of now",
        "2h ago"
    ),
    NotificationModel(
        image = R.drawable.sportsbag,
        itemName = "Sports bag",
        description = ", available in black, has 15 units in stock as of now",
        "2h ago"
    ),
    NotificationModel(
        image = R.drawable.bgts,
        itemName = "Go for the B/G TS",
        description = ", available in sizes S, M, L, and XL, has 20 units in stock as of now",
        "3h ago"
    ),
    NotificationModel(
        image = R.drawable.blackts,
        itemName = "Black TS",
        description = ", available in sizes XL and M, has 50 units in stock as of October 18, 2024",
        "4h ago"
    ),
)

data class NotificationModel(
    @DrawableRes val image: Int,
    val itemName: String,
    val description: String,
    val time: String
)