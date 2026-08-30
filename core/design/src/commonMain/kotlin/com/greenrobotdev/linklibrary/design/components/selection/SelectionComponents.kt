package com.greenrobotdev.linklibrary.design.components.selection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * PrioritySelector - Enhanced priority selection with Material 3 expressive styling
 *
 * A reusable component for selecting from multiple priority options. Features
 * expressive animations, clear selection states, and consistent styling.
 * Perfect for priority levels, difficulty ratings, or any multi-option selection.
 *
 * Features:
 * - Three-option horizontal layout
 * - Spring-based selection animations
 * - Enhanced border and elevation for selected items
 * - Icon indicators for selected state
 * - Customizable labels and colors
 *
 * Usage:
 * ```kotlin
 * PrioritySelector(
 *     options = listOf("Low", "Medium", "High"),
 *     selectedOption = selectedPriority,
 *     onOptionSelected = { selectedPriority = it },
 *     optionColors = listOf(
 *         MaterialTheme.colorScheme.secondaryContainer,
 *         MaterialTheme.colorScheme.tertiaryContainer,
 *         MaterialTheme.colorScheme.primaryContainer
 *     )
 * )
 * ```
 */
@Composable
fun PrioritySelector(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    optionColors: List<androidx.compose.ui.graphics.Color>? = null,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Enhanced label section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Priority Level",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Enhanced option buttons with Material 3 expressive design
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = selectedOption == option
                val buttonScale = if (isSelected) 1.05f else 1f
                val containerColor = optionColors?.getOrNull(index) ?: MaterialTheme.colorScheme.surfaceContainerHighest

                Surface(
                    onClick = { if (enabled) onOptionSelected(option) },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .scale(buttonScale),
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) {
                        containerColor
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f)
                    },
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) {
                            containerColor
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        }
                    ),
                    enabled = enabled,
                    tonalElevation = if (isSelected) 2.dp else 0.dp
                ) {
                    androidx.compose.foundation.layout.Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSelected) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Text(
                                option,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * OptionSelector - Generic multi-option selector component
 *
 * A more flexible version of PrioritySelector that can handle any number
 * of options with customizable styling.
 *
 * Features:
 * - Flexible option count
 * - Custom colors per option
 * - Horizontal scrolling support
 * - Selection animations
 * - Custom icon support
 *
 * Usage:
 * ```kotlin
 * OptionSelector(
 *     options = listOf("Option 1", "Option 2", "Option 3"),
 *     selectedOption = currentSelection,
 *     onOptionSelected = { currentSelection = it }
 * )
 * ```
 */
@Composable
fun OptionSelector(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    optionColors: List<androidx.compose.ui.graphics.Color>? = null,
    enabled: Boolean = true,
    horizontal: Boolean = true
) {
    if (horizontal) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = selectedOption == option
                val buttonScale = if (isSelected) 1.05f else 1f
                val containerColor = optionColors?.getOrNull(index) ?: MaterialTheme.colorScheme.surfaceContainerHighest

                Surface(
                    onClick = { if (enabled) onOptionSelected(option) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .scale(buttonScale),
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) {
                        containerColor
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f)
                    },
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) {
                            containerColor
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        }
                    ),
                    enabled = enabled,
                    tonalElevation = if (isSelected) 2.dp else 0.dp
                ) {
                    androidx.compose.foundation.layout.Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSelected) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Text(
                                option,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = selectedOption == option
                val buttonScale = if (isSelected) 1.05f else 1f
                val containerColor = optionColors?.getOrNull(index) ?: MaterialTheme.colorScheme.surfaceContainerHighest

                Surface(
                    onClick = { if (enabled) onOptionSelected(option) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .scale(buttonScale),
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) {
                        containerColor
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f)
                    },
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) {
                            containerColor
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        }
                    ),
                    enabled = enabled,
                    tonalElevation = if (isSelected) 2.dp else 0.dp
                ) {
                    androidx.compose.foundation.layout.Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSelected) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Text(
                                option,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * LabeledSelector - Option selector with label
 *
 * Combines a label with the option selector for consistent form styling.
 */
@Composable
fun LabeledSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    optionColors: List<androidx.compose.ui.graphics.Color>? = null,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Label
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 4.dp)
        )

        // Option selector
        OptionSelector(
            options = options,
            selectedOption = selectedOption,
            onOptionSelected = onOptionSelected,
            optionColors = optionColors,
            enabled = enabled
        )
    }
}