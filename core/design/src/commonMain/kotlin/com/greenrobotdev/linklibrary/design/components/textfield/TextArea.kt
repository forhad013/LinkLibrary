package com.greenrobotdev.linklibrary.design.components.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField as MaterialOutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Text area component with Material 3 styling
 *
 * Multi-line text input field with outlined border style. Ideal for
 * longer text content like descriptions, notes, or comments.
 *
 * @param value Current input value
 * @param onValueChange Callback invoked when value changes
 * @param modifier Modifier to be applied to the text area
 * @param label Optional label to display above the field
 * @param placeholder Optional placeholder text when field is empty
 * @param minLines Minimum number of lines to display
 * @param maxLines Maximum number of lines (default 5)
 * @param isError Whether the field is in error state
 * @param errorMessage Optional error message to display
 * @param enabled Whether the field is enabled for input
 * @param readOnly Whether the field is read-only
 */
@Composable
fun TextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    minLines: Int = 3,
    maxLines: Int = 5,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    val colors = TextFieldDefaults.colors(
        focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
        unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
        disabledContainerColor = androidx.compose.ui.graphics.Color.Transparent,
        errorContainerColor = androidx.compose.ui.graphics.Color.Transparent,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorTextColor = MaterialTheme.colorScheme.onSurface,
        cursorColor = MaterialTheme.colorScheme.primary,
        errorCursorColor = MaterialTheme.colorScheme.error,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
        disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        errorIndicatorColor = MaterialTheme.colorScheme.error
    )

    MaterialOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.heightIn(min = minLines * 24.dp, max = maxLines * 24.dp),
        enabled = enabled,
        readOnly = readOnly,
        label = if (label != null) {
            { androidx.compose.material3.Text(label) }
        } else null,
        placeholder = if (placeholder != null) {
            { androidx.compose.material3.Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        } else null,
        supportingText = if (isError && errorMessage != null) {
            {
                androidx.compose.material3.Text(
                    errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else null,
        singleLine = false,
        minLines = minLines,
        maxLines = maxLines,
        isError = isError,
        colors = colors,
        shape = MaterialTheme.shapes.small
    )
}