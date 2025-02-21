package com.example.beupdated.profile.presentation

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R
import com.example.beupdated.common.composables.ShimmerEffect
import com.example.beupdated.productdisplay.presentation.composables.SearchBar
import com.example.beupdated.profile.presentation.composables.AlertDialogErrorMessage
import com.example.beupdated.ui.theme.BeUpdatedTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: ProfileState,
    action: (ProfileAction) -> Unit,
    userId: String,
    onNotificationScreen: () -> Unit,
    onSearchScreen: () -> Unit,
    onSavedProductScreen: () -> Unit,
    onProductDisplayScreen: () -> Unit,
    onOrderScreen: () -> Unit,
    onLogout: () -> Unit
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
                        onClick = {
                            onProductDisplayScreen()
                        }
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
                            /* no-op */
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
        val context = LocalContext.current
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.profile != null) {
                val initialName =
                    if (state.profile.middleName.isNotEmpty()) state.profile.middleName[0].uppercase() else ""
                val phoneNumber = "+63${state.profile.phoneNumber.substring(0, 4)}******"

                item {
                    Spacer(Modifier.height(50.dp))
                }

                item {
                    Image(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(
                                RoundedCornerShape(50)
                            ),
                        painter = painterResource(R.drawable.beupdatedlogo),
                        contentDescription = null
                    )
                }

                item {
                    Text(
                        text = "${state.profile.lastName.replaceFirstChar { it.uppercase() }}, ${state.profile.firstName.replaceFirstChar { it.uppercase() }} ${initialName}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Student",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                item {
                    Spacer(Modifier.height(30.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, bottom = 10.dp),

                        text = "Email",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, bottom = 10.dp),
                        text = state.profile.email,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }

                item {
                    HorizontalDivider(Modifier.fillMaxWidth())

                    Spacer(Modifier.height(10.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, bottom = 10.dp),
                        text = "Password",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "***********",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        IconButton(
                            modifier = Modifier.size(20.dp),
                            onClick = {
                                action(ProfileAction.OnResetPassword(state.profile.email))
                            },
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                null
                            )
                        }
                    }
                }

                item {
                    HorizontalDivider(Modifier.fillMaxWidth())

                    Spacer(Modifier.height(10.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, bottom = 10.dp),
                        text = "Phone number",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = phoneNumber,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

                item {
                    HorizontalDivider(Modifier.fillMaxWidth())
                    Spacer(Modifier.height(10.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, bottom = 10.dp),
                        text = "Account deletion",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Delete account",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Red
                        )
                        IconButton(
                            modifier = Modifier.size(20.dp),
                            onClick = {
                                action(ProfileAction.OnDeleteAccountConfirmation)
                            },
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                null,
                                tint = Color.Red
                            )
                        }
                    }
                }

                item {
                    HorizontalDivider(Modifier.fillMaxWidth())

                    Button(
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.gold)
                        ),
                        onClick = {
                            onLogout()
                        }
                    ) {
                        Text(
                            text = "Logout",
                            color = Color.Black
                        )
                    }
                }
            }
        }

        if (state.message.isNotEmpty()) {
            AlertDialogErrorMessage(
                action = action,
                errorMessage = state.message,
                showQuantityWarning = false,
                userId = userId
            )
        }

        if (state.deleteAccountConfirmation) {
            AlertDialogErrorMessage(
                action = action,
                errorMessage = "Are you sure you want to delete your account?",
                showQuantityWarning = true,
                userId = userId
            )
        }

        if (state.resetSuccess) {
            Toast.makeText(
                context,
                "Link is sent to your email!",
                Toast.LENGTH_SHORT
            ).show()
            action(ProfileAction.OnResetStatePassword)
        }
    }
}

@Composable
fun ProfileShimmer(
    modifier: Modifier = Modifier
) {
    ShimmerEffect(
        modifier = Modifier
            .padding(20.dp)
            .size(20.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(50))
    )
}

@Preview(showBackground = true)
@Composable
private fun Prev() {
    BeUpdatedTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            ProfileShimmer()
        }
    }
//    ProfileScreen(
//        onSearchScreen = {},
//        onNotificationScreen = {},
//        onSavedProductScreen = {},
//        onProductDisplayScreen = {},
//        onOrderScreen = {},
//        onLogout = {},
//        state = ProfileState(),
//        action = {}
//    )
}