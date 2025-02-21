package com.example.beupdated.checkout.presentation.composables

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CreditCardFilter : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Add space every 4 characters
        val trimmed = text.text.take(16)
        var out = ""
        trimmed.forEachIndexed { index, char ->
            out += char
            if ((index + 1) % 4 == 0 && index != 15) out += " "
        }

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Calculate offset with spaces
                return when {
                    offset <= 0 -> 0
                    offset <= 4 -> offset
                    offset <= 8 -> offset + 1
                    offset <= 12 -> offset + 2
                    else -> offset + 3
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Calculate offset without spaces
                return when {
                    offset <= 4 -> offset
                    offset <= 9 -> offset - 1
                    offset <= 14 -> offset - 2
                    else -> offset - 3
                }
            }
        }

        return TransformedText(
            AnnotatedString(out),
            offsetTranslator
        )
    }
}