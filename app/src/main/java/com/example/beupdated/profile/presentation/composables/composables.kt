package com.example.beupdated.profile.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R
import com.example.beupdated.authentication.presentation.AuthAction
import com.example.beupdated.profile.presentation.ProfileAction
import com.example.beupdated.savedproduct.presentation.SavedProductAction

@Composable
fun AlertDialogErrorMessage(
    action: (ProfileAction) -> Unit,
    errorMessage: String,
    showQuantityWarning: Boolean = false,
    userId: String,
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {
            action(ProfileAction.OnResetError)
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
                                action(ProfileAction.OnDeleteAccount(userId = userId))
                            }
                        ) {
                            Text(text = "Yes", color = Color.Black)
                        }
                        Spacer(Modifier.width(20.dp))
                        Button(
                            onClick = {
                                action(ProfileAction.OnResetError)
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
