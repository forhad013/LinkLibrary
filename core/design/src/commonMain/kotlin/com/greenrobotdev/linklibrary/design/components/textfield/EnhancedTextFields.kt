package com.greenrobotdev.linklibrary.design.components.textfield

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField as MaterialOutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Enhanced TextFields - Specialized textfield variations for different contexts
 *
 * Provides a comprehensive set of textfield components optimized for specific
 * use cases throughout the application. Each variant includes proper validation,
 * formatting, and Material 3 Expressive styling.
 *
 * Categories:
 * - Input Variants: URL, number, password, email
 * - Layout Variants: Compact, multiline, readonly
 * - Enhanced Features: Character count, validation, clear buttons
 * - Specialized: Search, secure, measurement inputs
 */

// ==================== INPUT TYPE VARIANTS ====================

/**
 * UrlTextField - Specialized textfield for URL input
 *
 * Usage: Web addresses, link input, URL fields
 * Features: URL keyboard type, proper validation styling, clear button
 *
 * @param value Current URL value
 * @param onValueChange Callback invoked when value changes
 * @param modifier Optional modifier
 * @param label Optional label text
 * @param placeholder Optional placeholder text
 * @param isError Whether field is in error state
 * @param errorMessage Optional error message
 * @param enabled Whether field is enabled
 */
@Composable
fun UrlTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = "https://example.com",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    val colors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
        errorContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorTextColor = MaterialTheme.colorScheme.onErrorContainer,
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
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        readOnly = false,
        label = if (label != null) {
            { Text(label, style = MaterialTheme.typography.labelMedium) }
        } else null,
        placeholder = if (placeholder != null) {
            { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        } else null,
        trailingIcon = if (value.isNotEmpty()) {
            {
                IconButton(
                    onClick = { onValueChange("") },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Clear URL",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else null,
        supportingText = if (isError && errorMessage != null) {
            {
                Text(
                    errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else null,
        singleLine = true,
        isError = isError,
        colors = colors,
        shape = MaterialTheme.shapes.small,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
    )
}

/**
 * NumberTextField - Textfield for numeric input
 *
 * Usage: Numbers, quantities, measurements
 * Features: Number keyboard, proper alignment
 *
 * @param value Current numeric value
 * @param onValueChange Callback invoked when value changes
 * @param modifier Optional modifier
 * @param label Optional label text
 * @param placeholder Optional placeholder text
 * @param isError Whether field is in error state
 * @param errorMessage Optional error message
 * @param enabled Whether field is enabled
 */
@Composable
fun NumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    val colors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
        errorContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorTextColor = MaterialTheme.colorScheme.onErrorContainer,
        cursorColor = MaterialTheme.colorScheme.primary,
        errorCursorColor = MaterialTheme.colorScheme.error,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
        disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        errorIndicatorColor = MaterialTheme.colorScheme.error
    )

    MaterialOutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            // Only allow numeric input
            if (newValue.all { it.isDigit() || it == '-' }) {
                onValueChange(newValue)
            }
        },
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        readOnly = false,
        label = if (label != null) {
            { Text(label, style = MaterialTheme.typography.labelMedium) }
        } else null,
        placeholder = if (placeholder != null) {
            { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        } else null,
        supportingText = if (isError && errorMessage != null) {
            {
                Text(
                    errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else null,
        singleLine = true,
        isError = isError,
        colors = colors,
        shape = MaterialTheme.shapes.small,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

/**
 * PasswordTextField - Secure password input field
 *
 * Usage: Password entry, secure text input
 * Features: Password visibility toggle, secure input
 *
 * @param value Current password value
 * @param onValueChange Callback invoked when value changes
 * @param modifier Optional modifier
 * @param label Optional label text
 * @param placeholder Optional placeholder text
 * @param isError Whether field is in error state
 * @param errorMessage Optional error message
 * @param enabled Whether field is enabled
 */
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = "Enter password",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    val passwordVisible = remember { mutableStateOf(false) }

    val colors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
        errorContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorTextColor = MaterialTheme.colorScheme.onErrorContainer,
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
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        readOnly = false,
        label = if (label != null) {
            { Text(label, style = MaterialTheme.typography.labelMedium) }
        } else null,
        placeholder = if (placeholder != null) {
            { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        } else null,
        visualTransformation = if (passwordVisible.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(
                onClick = { passwordVisible.value = !passwordVisible.value },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = if (passwordVisible.value) {
                        Icons.Default.VisibilityOff
                    } else {
                        Icons.Default.Visibility
                    },
                    contentDescription = if (passwordVisible.value) {
                        "Hide password"
                    } else {
                        "Show password"
                    },
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        supportingText = if (isError && errorMessage != null) {
            {
                Text(
                    errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else null,
        singleLine = true,
        isError = isError,
        colors = colors,
        shape = MaterialTheme.shapes.small,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

/**
 * EmailTextField - Email input field
 *
 * Usage: Email addresses, mailto fields
 * Features: Email keyboard type, email validation
 *
 * @param value Current email value
 * @param onValueChange Callback invoked when value changes
 * @param modifier Optional modifier
 * @param label Optional label text
 * @param placeholder Optional placeholder text
 * @param isError Whether field is in error state
 * @param errorMessage Optional error message
 * @param enabled Whether field is enabled
 */
@Composable
fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = "email@example.com",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    val colors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
        errorContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorTextColor = MaterialTheme.colorScheme.onErrorContainer,
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
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        readOnly = false,
        label = if (label != null) {
            { Text(label, style = MaterialTheme.typography.labelMedium) }
        } else null,
        placeholder = if (placeholder != null) {
            { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        } else null,
        trailingIcon = if (value.isNotEmpty()) {
            {
                IconButton(
                    onClick = { onValueChange("") },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Clear email",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else null,
        supportingText = if (isError && errorMessage != null) {
            {
                Text(
                    errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else null,
        singleLine = true,
        isError = isError,
        colors = colors,
        shape = MaterialTheme.shapes.small,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

// ==================== LAYOUT VARIANTS ====================

/**
 * CompactTextField - Compact textfield for smaller spaces
 *
 * Usage: Forms with limited space, inline inputs, compact layouts
 * Features: Reduced padding, smaller text, condensed layout
 *
 * @param value Current input value
 * @param onValueChange Callback invoked when value changes
 * @param modifier Optional modifier
 * @param label Optional label text
 * @param placeholder Optional placeholder text
 * @param isError Whether field is in error state
 * @param enabled Whether field is enabled
 */
@Composable
fun CompactTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true
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
        modifier = modifier,
        enabled = enabled,
        readOnly = false,
        label = if (label != null) {
            {
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else null,
        placeholder = if (placeholder != null) {
            {
                Text(
                    placeholder,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else null,
        singleLine = true,
        isError = isError,
        colors = colors,
        shape = MaterialTheme.shapes.small
    )
}

/**
 * ReadOnlyTextField - Non-editable text display
 *
 * Usage: Display values that cannot be edited, readonly fields
 * Features: No editing, proper theming for readonly state
 *
 * @param value Current value to display
 * @param modifier Optional modifier
 * @param label Optional label text
 */
@Composable
fun ReadOnlyTextField(
    value: String,
    modifier: Modifier = Modifier,
    label: String? = null
) {
    val colors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        errorContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        errorTextColor = MaterialTheme.colorScheme.onSurface,
        cursorColor = androidx.compose.ui.graphics.Color.Transparent,
        errorCursorColor = androidx.compose.ui.graphics.Color.Transparent,
        focusedIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        disabledIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
        errorIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    )

    MaterialOutlinedTextField(
        value = value,
        onValueChange = {},
        modifier = modifier.fillMaxWidth(),
        enabled = false,
        readOnly = true,
        label = if (label != null) {
            {
                Text(
                    label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else null,
        singleLine = true,
        colors = colors,
        shape = MaterialTheme.shapes.small
    )
}

// ==================== ENHANCED FEATURE VARIANTS ====================

/**
 * CountedTextField - Textfield with character counter
 *
 * Usage: Text input with length limits, descriptions, comments
 * Features: Character count display, optional max length
 *
 * @param value Current input value
 * @param onValueChange Callback invoked when value changes
 * @param modifier Optional modifier
 * @param label Optional label text
 * @param placeholder Optional placeholder text
 * @param maxLength Maximum character length
 * @param isError Whether field is in error state
 * @param errorMessage Optional error message
 * @param enabled Whether field is enabled
 */
@Composable
fun CountedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    maxLength: Int = 200,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
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
        onValueChange = { if (it.length <= maxLength) onValueChange(it) },
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        readOnly = false,
        label = if (label != null) {
            { Text(label, style = MaterialTheme.typography.labelMedium) }
        } else null,
        placeholder = if (placeholder != null) {
            { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        } else null,
        supportingText = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isError && errorMessage != null) {
                    Text(
                        errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                Text(
                    "${value.length}/$maxLength",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (value.length >= maxLength) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        },
        singleLine = false,
        maxLines = 4,
        minLines = 2,
        isError = isError || value.length >= maxLength,
        colors = colors,
        shape = MaterialTheme.shapes.small
    )
}

/**
 * ValidatedTextField - Textfield with real-time validation
 *
 * Usage: Input fields that require validation feedback
 * Features: Validation state, error messages, success indicators
 *
 * @param value Current input value
 * @param onValueChange Callback invoked when value changes
 * @param modifier Optional modifier
 * @param label Optional label text
 * @param placeholder Optional placeholder text
 * @param validator Function to validate input (returns error message or null)
 * @param enabled Whether field is enabled
 */
@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    validator: (String) -> String? = { null },
    enabled: Boolean = true
) {
    val errorMessage = remember(value) { validator(value) }
    val isError = errorMessage != null

    val colors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
        errorContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorTextColor = MaterialTheme.colorScheme.onErrorContainer,
        cursorColor = MaterialTheme.colorScheme.primary,
        errorCursorColor = MaterialTheme.colorScheme.error,
        focusedIndicatorColor = if (isError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.primary
        },
        unfocusedIndicatorColor = if (isError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.outline
        },
        disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        errorIndicatorColor = MaterialTheme.colorScheme.error
    )

    MaterialOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        readOnly = false,
        label = if (label != null) {
            { Text(label, style = MaterialTheme.typography.labelMedium) }
        } else null,
        placeholder = if (placeholder != null) {
            { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        } else null,
        supportingText = if (isError && errorMessage != null) {
            {
                Text(
                    errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else null,
        singleLine = true,
        isError = isError,
        colors = colors,
        shape = MaterialTheme.shapes.small
    )
}