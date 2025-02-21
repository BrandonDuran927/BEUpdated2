package com.example.beupdated.checkout.presentation.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R
import com.example.beupdated.checkout.presentation.CheckOutAction
import com.example.beupdated.checkout.presentation.CheckOutState
import com.example.beupdated.common.utilities.PhoneNumberFilter
import com.example.beupdated.productdisplay.presentation.DisplayableNumber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun CheckoutItem(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int,
    productName: String,
    productSize: String,
    productColor: String,
    totalPrice: DisplayableNumber,
    quantity: Int,
    onProductDisplay: (String) -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                // TODO: Proceed to product screen; download pdf
                onProductDisplay(productName)
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
                    onIncrement()
                },
                onDecrement = {
                    onDecrement()
                }
            )
        }
    }
}

@Composable
fun AlertDialogErrorMessage(
    action: (CheckOutAction) -> Unit,
    errorMessage: String,
    showQuantityWarning: Boolean = false,
    userId: String,
    productId: String,
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {
            action(CheckOutAction.OnResetError)
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
                                println("ID of the product under AlertDialogErrorMessage: $productId")
                                action(
                                    CheckOutAction.OnDecrementQuantity(
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
                                action(CheckOutAction.OnResetError)
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

@Composable
fun AlertDialogGcash(
    modifier: Modifier = Modifier,
    action: (CheckOutAction) -> Unit,
    state: CheckOutState,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {},
        title = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(R.drawable.gcashlogo),
                    contentDescription = "Gcash option"
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = title,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    text = "Enter your account number",
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.gcashNumber,
                    onValueChange = { newValue ->
                        val cleaned = newValue.filter { it.isDigit() }.take(10)
                        action(CheckOutAction.OnChangeGcashNumber(cleaned))
                    },
                    visualTransformation = PhoneNumberFilter(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        focusedBorderColor = colorResource(R.color.blue),
                        cursorColor = colorResource(R.color.blue),
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedPlaceholderColor = Color.Black,
                    ),
                    leadingIcon = {
                        Text(text = "+63")
                    },
                    placeholder = {
                        Text("e.g. 912 345 6789")
                    },
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    text = "Amount",
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.gcashAmount,
                    onValueChange = {
                        action(CheckOutAction.OnChangeAmount(it))
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        focusedBorderColor = colorResource(R.color.blue),
                        cursorColor = colorResource(R.color.blue),
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedPlaceholderColor = Color.Black,
                    ),
                    placeholder = {
                        Text("₱ 0.00")
                    },
                    shape = RoundedCornerShape(15.dp)
                )
            }
        },
        confirmButton = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = colorResource(R.color.blue)
                        )
                    ) {
                        Text("Cancel")
                    }

                    Spacer(Modifier.width(12.dp))

                    Button(
                        onClick = {
                            onConfirm()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.gold),
                            contentColor = colorResource(R.color.blue)
                        )
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    )
}

@Composable
fun AlertDialogCreditCard(
    title: String,
    action: (CheckOutAction) -> Unit,
    state: CheckOutState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {},
        title = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(R.drawable.mastercardlogo),
                    contentDescription = "Gcash option"
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = title,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    text = "Card Details",
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.cardNumber,
                    onValueChange = { newValue ->
                        // Filter and format the input
                        val cleaned = newValue.filter { it.isDigit() }.take(16)
                        action(CheckOutAction.OnChangeCardNumber(cleaned))
                    },
                    visualTransformation = CreditCardFilter(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        focusedBorderColor = colorResource(R.color.blue),
                        cursorColor = colorResource(R.color.blue),
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedPlaceholderColor = Color.Black,
                    ),
                    placeholder = {
                        Text("Card Number")
                    },
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = state.expiration,
                        onValueChange = {
                            action(CheckOutAction.OnChangeExpiration(it))
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = colorResource(R.color.blue),
                            cursorColor = colorResource(R.color.blue),
                            unfocusedTextColor = Color.Black,
                            unfocusedContainerColor = Color.White,
                            focusedPlaceholderColor = Color.Black,
                        ),
                        placeholder = {
                            Text("Exp. (MM/YY)")
                        },
                        shape = RoundedCornerShape(15.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    OutlinedTextField(
                        modifier = Modifier.weight(0.5f),
                        value = state.cvv,
                        onValueChange = {
                            action(CheckOutAction.OnChangeCvv(it))
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = colorResource(R.color.blue),
                            cursorColor = colorResource(R.color.blue),
                            unfocusedTextColor = Color.Black,
                            unfocusedContainerColor = Color.White,
                            focusedPlaceholderColor = Color.Black,
                        ),
                        placeholder = {
                            Text("CVV")
                        },
                        shape = RoundedCornerShape(15.dp)
                    )
                }

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.nameInCard,
                    onValueChange = {
                        action(CheckOutAction.OnChangeNameInCard(it))
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        focusedBorderColor = colorResource(R.color.blue),
                        cursorColor = colorResource(R.color.blue),
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedPlaceholderColor = Color.Black,
                    ),
                    placeholder = {
                        Text("Name on Card")
                    },
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.creditCardAmount,
                    onValueChange = {
                        action(CheckOutAction.OnChangeCreditCardAmount(it))
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        focusedBorderColor = colorResource(R.color.blue),
                        cursorColor = colorResource(R.color.blue),
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedPlaceholderColor = Color.Black,
                    ),
                    placeholder = {
                        Text("Total amount")
                    },
                    shape = RoundedCornerShape(15.dp)
                )
            }
        },
        confirmButton = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = colorResource(R.color.blue)
                        )
                    ) {
                        Text("Cancel")
                    }

                    Spacer(Modifier.width(12.dp))

                    Button(
                        onClick = {
                            onConfirm()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.gold),
                            contentColor = colorResource(R.color.blue)
                        )
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun Prev() {
//    AlertDialogCreditCard(
//        title = "Credit Card",
//        onDismiss = {},
//        onConfirm = {}
//    )
}


@Composable
fun QuantityModifier(
    modifier: Modifier = Modifier,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    quantity: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .border(1.dp, Color.Black.copy(0.6f))
                .size(20.dp),
            onClick = {
                onDecrement()
            }
        ) {
            Text("-")
        }
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = quantity.toString(),
            fontSize = 15.sp
        )
        IconButton(
            modifier = Modifier
                .border(1.dp, Color.Black.copy(0.6f))
                .size(20.dp),
            onClick = {
                onIncrement()
            }
        ) {
            Text("+")
        }
    }
}

@Composable
fun DatePickerFieldToModal(
    modifier: Modifier = Modifier,
    state: CheckOutState,
    action: (CheckOutAction) -> Unit
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = convertMillisToDate(state.pickUpDateLong),
        onValueChange = { /* no-op */ },
        label = { Text("Pick a date") },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            focusedContainerColor = Color.White,
            focusedBorderColor = colorResource(R.color.blue),
            cursorColor = colorResource(R.color.blue),
            unfocusedContainerColor = Color.White,
            focusedPlaceholderColor = Color.Black,
        ),
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .width(280.dp)
            .pointerInput(state.pickUpDateLong) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        action(CheckOutAction.OnShowModal)
                        focusManager.clearFocus()
                    }
                }
            }
    )

    if (state.showModal) {
        DatePickerModal(
            onDateSelected = {
                action(CheckOutAction.OnChangeDateLong(it))
                focusManager.clearFocus()
            },
            onDismiss = {
                action(CheckOutAction.OnDismissModal)
                focusManager.clearFocus()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        colors = DatePickerDefaults.colors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            headlineContentColor = Color.Black,
            weekdayContentColor = Color.Black,
            subheadContentColor = Color.Black,
            yearContentColor = Color.Black,
            currentYearContentColor = Color.Black,
            selectedDayContainerColor = colorResource(R.color.blue),
            selectedDayContentColor = Color.White,
            todayContentColor = Color.Black,
            dayInSelectionRangeContentColor = Color.White,
            dayInSelectionRangeContainerColor = colorResource(R.color.blue),
            selectedYearContainerColor = colorResource(R.color.blue),
            selectedYearContentColor = Color.White
        ),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun convertTimestampToTime(millis: Long): String {
    val date = Date(millis)
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return formatter.format(date)
}
