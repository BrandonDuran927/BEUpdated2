package com.example.beupdated.common.utilities

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneNumberFilter : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Add space every 3 characters and then between the next 4
        val trimmed = text.text.take(10) // Limit to 10 digits
        var out = ""

        trimmed.forEachIndexed { index, char ->
            out += char
            // Add space after every 3rd character, except before the last 4 digits
            if ((index + 1) % 3 == 0 && index < 6) out += " "
        }

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Calculate offset with spaces
                return when {
                    offset <= 0 -> 0
                    offset <= 3 -> offset
                    offset <= 6 -> offset + (offset / 3) // One space for every 3 characters
                    else -> offset + 2 // Add two spaces for the remaining digits
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Calculate offset without spaces
                return when {
                    offset <= 3 -> offset
                    offset <= 7 -> offset - (offset / 4) // Remove one space for every 4 characters
                    else -> offset - 2 // Remove two spaces for the last segment
                }
            }
        }

        return TransformedText(
            AnnotatedString(out),
            offsetTranslator
        )
    }
}