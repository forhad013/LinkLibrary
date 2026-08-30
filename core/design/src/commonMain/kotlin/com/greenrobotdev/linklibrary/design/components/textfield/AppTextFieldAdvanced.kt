package com.greenrobotdev.linklibrary.design.components.textfield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * AppTextFieldWithCharacterLimit - Enhanced text field with character counting and limit warnings
 *
 * Advanced text field component that provides character counting functionality
 * commonly needed in forms with length constraints.
 *
 * Design Philosophy:
 * - Consistent with standard AppTextField styling
 * - Adds character counting and limit functionality
 * - Dynamic visual feedback based on character limit proximity
 * - Proper Material 3 Expressive theming
 *
 * Features:
 * - Character counter showing current/limit
 * - Dynamic border colors based on character usage
 * - Warning indicators when approaching limit
 * - Consistent Material 3 styling
 * - Configurable character limits
 *
 * @param value Current text value
 * @param onValueChange Callback when text value changes (enforces character limit)
 * @param maxLength Maximum character limit
 * @param modifier Optional modifier
 * @param label Optional label text
 * @param placeholder Optional placeholder text
 * @param singleLine Whether field is single-line (default: false for notes)
 * @param minLines Minimum lines for multi-line
 * @param maxLines Maximum lines for multi-line
 * @param enabled Whether field is enabled
 */
@Composable
fun AppTextFieldWithCharacterLimit(
    value: String,
    onValueChange: (String) -> Unit,
    maxLength: Int,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    singleLine: Boolean = false,
    minLines: Int = 3,
    maxLines: Int = 5,
    enabled: Boolean = true
) {
    // Progress color based on how close to limit
    val progressColor = when {
        value.length >= maxLength * 0.9 -> MaterialTheme.colorScheme.error
        value.length >= maxLength * 0.7 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }

    Column(
        modifier = modifier,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        // Label if provided
        label?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // Character counter row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "${value.length}/$maxLength",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = progressColor
            )

        }

        // Enhanced text field with dynamic styling
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            border = BorderStroke(
                width = 1.dp,
                color = when {
                    value.length >= maxLength * 0.8 -> progressColor.copy(alpha = 0.3f)
                    else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                }
            ),
            tonalElevation = 2.dp
        ) {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                OutlinedTextField(
                    value = value,
                    onValueChange = {
                        if (it.length <= maxLength) {
                            onValueChange(it)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = placeholder?.let { { Text(it) } },
                    singleLine = singleLine,
                    minLines = minLines,
                    maxLines = maxLines,
                    enabled = enabled,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        // Warning indicators
        if (value.length >= maxLength * 0.8) {
            val warningText = when {
                value.length >= maxLength -> "Character limit reached"
                value.length >= maxLength * 0.9 -> "Almost at character limit"
                else -> "Getting close to character limit"
            }

            Row(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(24.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = progressColor.copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = progressColor,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Text(
                    text = warningText,
                    style = MaterialTheme.typography.bodySmall,
                    color = progressColor
                )
            }
        }
    }
}

/**
 * AppTextFieldEnhanced - Enhanced text field with custom styling and icon support
 *
 * Advanced text field with enhanced Material 3 styling that supports
 * leading icons and custom container styling for complex form inputs.
 *
 * Design Philosophy:
 * - Consistent Material 3 Expressive styling
 * - Enhanced visual feedback and theming
 * - Icon support for visual context
 * - Smooth, modern appearance
 *
 * Features:
 * - Leading icon support for visual context
 * - Enhanced Material 3 styling with transparent borders
 * - Surface container with rounded corners
 * - Consistent color theming
 * - Proper dark theme support
 *
 * @param value Current text value
 * @param onValueChange Callback when text value changes
 * @param modifier Optional modifier
 * @param label Optional label text
 * @param placeholder Optional placeholder text
 * @param leadingIcon Optional leading icon
 * @param singleLine Whether field is single-line (default: true)
 * @param enabled Whether field is enabled
 * @param isError Whether field is in error state
 */
@Composable
fun AppTextFieldEnhanced(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    isError: Boolean = false
) {
    Column(
        modifier = modifier,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        // Label if provided
        label?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // Enhanced text field with custom styling
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            border = BorderStroke(
                width = 1.dp,
                color = if (isError) {
                    MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                }
            ),
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Leading icon
                if (leadingIcon != null) {
                    Icon(
                        leadingIcon,
                        contentDescription = null,
                        tint = if (isError) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Text field
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = placeholder?.let { { Text(it) } },
                        singleLine = singleLine,
                        enabled = enabled,
                        isError = isError,
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.secondary,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            errorBorderColor = Color.Transparent,
                            errorCursorColor = MaterialTheme.colorScheme.error,
                            errorTextColor = MaterialTheme.colorScheme.error
                        )
                    )
                }
            }
        }
    }
}