package com.example.beupdated.product.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.product.presentation.ProductAction
import com.example.beupdated.savedproduct.presentation.SavedProductAction

@Composable
fun AlertDialogErrorMessage(
    action: (ProductAction) -> Unit,
    errorMessage: String
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {
            action(ProductAction.OnResetError)
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