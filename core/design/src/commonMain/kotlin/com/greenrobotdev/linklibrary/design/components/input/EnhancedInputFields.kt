package com.greenrobotdev.linklibrary.design.components.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * EnhancedTextField - Text input with icon, action button, and enhanced styling
 *
 * A reusable text field component that provides Material 3 Expressive styling
 * with leading icons, trailing action buttons, and enhanced visual feedback.
 * Perfect for inputs like URLs, search fields, and any text input that needs
 * additional functionality.
 *
 * Features:
 * - Leading icon with optional styling
 * - Trailing action button with loading/error states
 * - Enhanced border and container styling
 * - Material 3 color scheme support
 * - Proper dark theme contrast
 *
 * Usage:
 * ```kotlin
 * EnhancedTextField(
 *     value = url,
 *     onValueChange = { url = it },
 *     leadingIcon = Icons.Default.Link,
 *     placeholder = "https://example.com",
 *     trailingAction = {
 *         ActionButton(
 *             onClick = { /* action */ },
 *             icon = Icons.Default.AutoAwesome
 *         )
 *     }
 * )
 * ```
 */
@Composable
fun EnhancedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingAction: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLength: Int = Int.MAX_VALUE,
    borderColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surfaceContainerHigh
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        ),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Leading icon
            if (leadingIcon != null) {
                Icon(
                    leadingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Text input
            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = value,
                    onValueChange = {
                        if (it.length <= maxLength) {
                            onValueChange(it)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(placeholder) },
                    enabled = enabled,
                    singleLine = singleLine,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                        focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            // Trailing action
            trailingAction?.invoke()
        }
    }
}

/**
 * TextAreaWithCounter - Multi-line text input with character counter
 *
 * A text area component that shows character count and provides visual feedback
 * when approaching the character limit. Perfect for notes, descriptions, and
 * any multi-line input with length constraints.
 *
 * Features:
 * - Multi-line text input with auto-expand
 * - Character counter with progress colors
 * - Visual warnings when approaching limit
 * - Enhanced Material 3 styling
 * - Label and description support
 *
 * Usage:
 * ```kotlin
 * TextAreaWithCounter(
 *     value = notes,
 *     onValueChange = { notes = it },
 *     label = "Personal Notes",
 *     placeholder = "Add your notes here...",
 *     maxLength = 500
 * )
 * ```
 */
@Composable
fun TextAreaWithCounter(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "",
    maxLength: Int = 500,
    enabled: Boolean = true,
    minHeight: androidx.compose.ui.unit.Dp = 100.dp
) {
    // Progress color based on character count
    val progressColor = when {
        value.length >= maxLength * 0.9 -> MaterialTheme.colorScheme.error
        value.length >= maxLength * 0.7 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }

    // Border color based on character count
    val borderColor = when {
        value.length >= maxLength * 0.8 -> progressColor.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Label (optional)
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // Main text area with counter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Text area container
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                border = BorderStroke(
                    width = 1.dp,
                    color = borderColor
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(minHeight),
                        placeholder = { Text(placeholder) },
                        enabled = enabled,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                            focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.secondary,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }

            // Character counter (shown when has content)
            if (value.isNotBlank()) {
                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.End
                ) {
                    Text(
                        text = "${value.length}/$maxLength",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = progressColor
                    )
                }
            }
        }

        // Character limit warning
        if (value.length >= maxLength * 0.8) {
            val warningText = when {
                value.length >= maxLength -> "Character limit reached"
                value.length >= maxLength * 0.9 -> "Almost at character limit"
                else -> "Getting close to character limit"
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Surface(
                    modifier = Modifier.size(24.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = progressColor.copy(alpha = 0.2f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (value.length >= maxLength) Icons.Default.Error else Icons.Default.Info,
                            contentDescription = null,
                            tint = progressColor,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Text(
                    warningText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = progressColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * LabeledInputField - Input field with consistent label styling
 *
 * Simple wrapper that provides consistent label styling for input fields
 * throughout the application.
 */
@Composable
fun LabeledInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Label
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 4.dp)
        )

        // Input field
        EnhancedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            enabled = enabled,
            singleLine = singleLine
        )
    }
}