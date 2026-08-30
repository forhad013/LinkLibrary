package com.greenrobotdev.linklibrary.design.components.textfield

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

/**
 * AppTextField - Standardized text field component for consistent appearance across all form screens
 *
 * Provides a unified text field implementation that can be used consistently across:
 * - Add Link screen
 * - Add Tag screen
 * - Add Collection screen
 * - Any other form-based screens
 *
 * Design Philosophy:
 * - Simple, consistent appearance across all screens
 * - Material 3 Expressive styling with proper theming
 * - Standard form field functionality
 * - No complex variants - one unified text field for all use cases
 *
 * Features:
 * - Consistent Material 3 OutlinedTextField styling
 * - Proper dark theme support with correct contrast
 * - Standard label and placeholder support
 * - Error state indication
 * - Single-line and multi-line support
 * - Enabled/disabled states
 * - Visual transformation support (for passwords, etc.)
 *
 * @param value Current text value in the field
 * @param onValueChange Callback when text value changes
 * @param modifier Optional modifier for layout
 * @param label Label text displayed above the field
 * @param placeholder Placeholder text shown when field is empty
 * @param singleLine Whether field is single-line (default: true)
 * @param minLines Minimum lines for multi-line fields
 * @param maxLines Maximum lines for multi-line fields
 * @param isError Whether field is in error state
 * @param enabled Whether field is enabled for interaction
 * @param visualTransformation Optional visual transformation (e.g., password masking)
 */
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    isError: Boolean = false,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label?.let { { Text(it) } },
        placeholder = placeholder?.let { { Text(it) } },
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        isError = isError,
        enabled = enabled,
        visualTransformation = visualTransformation
    )
}