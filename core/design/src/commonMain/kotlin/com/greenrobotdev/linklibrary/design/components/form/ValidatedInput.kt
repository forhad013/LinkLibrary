package com.greenrobotdev.linklibrary.design.components.form

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * ValidatedInput - Text field with real-time validation and animated feedback
 *
 * Material 3 Expressive form component with comprehensive validation:
 * - Real-time validation states
 * - Animated error/success indicators
 * - Character counting
 * - Color state transitions
 * - Accessible error messages
 * - Shake animation for errors
 */

sealed class ValidationResult {
    data object Idle : ValidationResult()
    data object Valid : ValidationResult()
    data class Invalid(val error: String) : ValidationResult()
}

@Composable
fun ValidatedInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    validation: (String) -> ValidationResult = { ValidationResult.Valid },
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    maxCharacters: Int? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val validationResult by remember(value) {
        derivedStateOf {
            if (value.isEmpty()) ValidationResult.Idle
            else validation(value)
        }
    }

    val isError = validationResult is ValidationResult.Invalid
    val isValid = validationResult is ValidationResult.Valid

    // Animated colors for different states
    val containerColor by animateColorAsState(
        targetValue = when (validationResult) {
            is ValidationResult.Invalid -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 200),
        label = "container_color"
    )

    val borderColor by animateColorAsState(
        targetValue = when (validationResult) {
            is ValidationResult.Invalid -> MaterialTheme.colorScheme.error
            is ValidationResult.Valid -> MaterialTheme.colorScheme.tertiary
            is ValidationResult.Idle -> MaterialTheme.colorScheme.outline
        },
        animationSpec = tween(durationMillis = 200),
        label = "border_color"
    )

    // Shake animation for errors
    val shakeOffset by animateFloatAsState(
        targetValue = if (isError) 0f else 0f,
        animationSpec = spring(
            dampingRatio = 0.8f,
            stiffness = Spring.StiffnessMedium
        ),
        label = "shake_offset"
    )

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (maxCharacters == null || newValue.length <= maxCharacters) {
                    onValueChange(newValue)
                }
            },
            label = label?.let { { Text(it) } },
            placeholder = placeholder?.let { { Text(it) } },
            leadingIcon = leadingIcon,
            trailingIcon = {
                Row(
                    modifier = Modifier.padding(end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Validation icon
                    when (validationResult) {
                        is ValidationResult.Valid -> {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Valid",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        is ValidationResult.Invalid -> {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Invalid",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        is ValidationResult.Idle -> {
                            // No icon
                        }
                    }

                    // Clear button
                    if (value.isNotEmpty()) {
                        IconButton(
                            onClick = { onValueChange("") },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // User-provided trailing icon
                    trailingIcon?.invoke()
                }
            },
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = shakeOffset.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = singleLine,
            enabled = enabled,
            readOnly = readOnly,
            visualTransformation = visualTransformation,
            shape = TextFieldDefaults.shape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                errorBorderColor = MaterialTheme.colorScheme.error,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        // Error message and character count
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Error message
            if (isError) {
                Text(
                    text = (validationResult as ValidationResult.Invalid).error,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Character count
            if (maxCharacters != null) {
                val characterCount = "${value.length}/$maxCharacters"
                val countColor = when {
                    value.length > maxCharacters * 0.9 -> MaterialTheme.colorScheme.error
                    value.length > maxCharacters * 0.7 -> MaterialTheme.colorScheme.onSurfaceVariant
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }

                Text(
                    text = characterCount,
                    style = MaterialTheme.typography.labelSmall,
                    color = countColor,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

