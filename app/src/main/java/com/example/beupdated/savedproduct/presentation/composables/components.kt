package com.example.beupdated.savedproduct.presentation.composables

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.example.beupdated.checkout.presentation.composables.QuantityModifier
import com.example.beupdated.productdisplay.presentation.DisplayableNumber
import com.example.beupdated.productdisplay.presentation.toDisplayDouble
import com.example.beupdated.savedproduct.presentation.SavedProductAction
import com.example.beupdated.ui.theme.BeUpdatedTheme

@Composable
fun SavedItem(
    @DrawableRes image: Int,
    productId: String,
    productName: String,
    productSize: String,
    productColor: String,
    totalPrice: DisplayableNumber,
    quantity: Int,
    userId: String,
    onCheck: Boolean,
    savedAt: String,
    onProductScreen: () -> Unit,
    action: (SavedProductAction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .clickable {
                onProductScreen()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(
            selected = onCheck,
            onClick = {
                action(
                    SavedProductAction.OnCheckProduct(
                        onCheck = !onCheck,
                        userId = userId,
                        productName = productName,
                        productSize = productSize,
                        productColor = productColor
                    )
                )
            },
            modifier = Modifier,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Black,
                unselectedColor = Color.Black
            )
        )

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.width(170.dp),
                    text = productName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                IconButton(
                    modifier = Modifier.size(30.dp),
                    onClick = {
                        action(SavedProductAction.OnShowWarningQuantity(productName, productId))
                    }
                ) {
                    Image(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(R.drawable.savedicon),
                        contentDescription = null
                    )
                }
            }

            if (productSize.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = productSize,
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = productColor,
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = "P ${totalPrice.formatted}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            QuantityModifier(
                quantity = quantity,
                onIncrement = {
                    action(
                        SavedProductAction.OnIncrementQuantity(
                            userId = userId,
                            savedAt = savedAt
                        )
                    )
                },
                onDecrement = {
                    if (quantity == 1) {
                        action(SavedProductAction.OnShowWarningQuantity(productName, productId))
                    } else {
                        action(
                            SavedProductAction.OnDecrementQuantity(
                                userId = userId,
                                productId = productId
                            )
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun AlertDialogErrorMessage(
    action: (SavedProductAction) -> Unit,
    errorMessage: String,
    showQuantityWarning: Boolean = false,
    userId: String,
    productId: String,
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {
            action(SavedProductAction.OnResetError)
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
                if (showQuantityWarning) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                // FIXME: Use the ID instead
                                println("ID of the product under AlertDialogErrorMessage: $productId")
                                action(
                                    SavedProductAction.OnDecrementQuantity(
                                        userId = userId,
                                        productId = productId
                                    )
                                )
                            }
                        ) {
                            Text(text = "Yes", color = Color.Black)
                        }
                        Spacer(Modifier.width(20.dp))
                        Button(
                            onClick = {
                                action(SavedProductAction.OnResetError)
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
    BeUpdatedTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            SavedItem(
                image = R.drawable.blankproduct,
                productId = "",
                productName = "Tourism Suit",
                productColor = "Black",
                productSize = "Medium",
                onCheck = false,
                quantity = 1,
                totalPrice = 120.00.toDisplayDouble(),
                savedAt = "",
                userId = "",
                action = {},
                onProductScreen = {}
            )
        }
    }
}