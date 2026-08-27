package com.greenrobotdev.linklibrary.design.components.textfield

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Filled text field component with Material 3 styling
 *
 * Text input field with filled background style. Integrates with theme
 * colors and provides proper error states and leading icon support.
 *
 * @param value Current input value
 * @param onValueChange Callback invoked when value changes
 * @param modifier Modifier to be applied to the text field
 * @param label Optional label to display above the field
 * @param placeholder Optional placeholder text when field is empty
 * @param leadingIcon Optional icon to display at the start of the field
 * @param trailingIcon Optional icon to display at the end of the field
 * @param isError Whether the field is in error state
 * @param errorMessage Optional error message to display
 * @param enabled Whether the field is enabled for input
 * @param singleLine Whether to restrict to single line
 * @param maxLines Maximum number of lines (if multi-line)
 * @param minLines Minimum number of lines (if multi-line)
 */
@Composable
fun FilledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1
) {
    val colors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
        errorContainerColor = MaterialTheme.colorScheme.errorContainer,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorTextColor = MaterialTheme.colorScheme.onErrorContainer,
        cursorColor = MaterialTheme.colorScheme.primary,
        errorCursorColor = MaterialTheme.colorScheme.error,
        focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
        unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
        disabledIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
        errorIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
    )

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = false,
        label = if (label != null) {
            { androidx.compose.material3.Text(label) }
        } else null,
        placeholder = if (placeholder != null) {
            { androidx.compose.material3.Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        } else null,
        leadingIcon = if (leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else null,
        trailingIcon = if (trailingIcon != null) {
            {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        isError = isError,
        colors = colors,
        shape = MaterialTheme.shapes.small
    )
}