package com.greenrobotdev.linklibrary.design.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.components.icon.AppIcon
import com.greenrobotdev.linklibrary.design.components.icon.IconBackgroundVariant

/**
 * FormCard - Base form card with consistent Material 3 Expressive styling
 *
 * A reusable card component that provides the consistent styling used throughout
 * the application's forms. Features enhanced contrast, atmospheric layering,
 * and proper elevation for both light and dark themes.
 *
 * Features:
 * - Consistent 24dp "spirited padding"
 * - Rounded corners (24dp) for expressive design
 * - Surface container highest for proper elevation
 * - Subtle borders with outline variant colors
 * - Proper shadow elevation
 *
 * Usage:
 * ```kotlin
 * FormCard(
 *     headerIcon = Icons.Default.Link,
 *     headerTitle = "Link Details",
 *     headerColor = MaterialTheme.colorScheme.primary
 * ) {
 *     // Card content
 * }
 * ```
 */
@Composable
fun FormCard(
    modifier: Modifier = Modifier,
    headerIcon: ImageVector? = null,
    headerIconBackgroundColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.inverseSurface,
    headerTitle: String? = null,
    headerTextColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Optional header section
            if (headerIcon != null && headerTitle != null) {
                FormCardHeader(
                    icon = headerIcon,
                    title = headerTitle,
                    iconBackgroundColor = headerIconBackgroundColor,
                    textColor = headerTextColor
                )
            }

            // Card content
            content()
        }
    }
}

/**
 * FormCardHeader - Internal header component for form cards
 */
@Composable
private fun FormCardHeader(
    icon: ImageVector,
    title: String,
    iconBackgroundColor: androidx.compose.ui.graphics.Color =  MaterialTheme.colorScheme.inverseSurface,
    textColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppIcon(
            icon = icon,
            backgroundColor = iconBackgroundColor,
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

/**
 * ToggleCard - Form card with toggle functionality
 *
 * A specialized form card that includes a toggle switch for enable/disable
 * functionality. Perfect for settings, optional features, and any content
 * that can be enabled/disabled.
 *
 * Features:
 * - Toggle switch with proper Material 3 styling
 * - Animated appearance when enabled
 * - Enhanced border and elevation when active
 * - Content reveal when enabled
 * - Icon changes based on state
 *
 * Usage:
 * ```kotlin
 * ToggleCard(
 *     isEnabled = taskEnabled,
 *     onEnabledChange = { taskEnabled = it },
 *     title = "Add to Tasks",
 *     description = "Set priority and due date",
 *     enabledIcon = Icons.Default.AutoAwesome,
 *     disabledIcon = Icons.Default.Schedule
 * ) {
 *     // Content shown when enabled
 * }
 * ```
 */
@Composable
fun ToggleCard(
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    enabledIcon: ImageVector? = null,
    disabledIcon: ImageVector? = null,
    activeColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    content: @Composable () -> Unit = {}
) {
    // Enhanced styling when enabled
    val cardScale = if (isEnabled) 1f else 0.98f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(cardScale),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isEnabled) {
                activeColor.copy(alpha = 0.4f)
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            }
        ),
        border = BorderStroke(
            width = if (isEnabled) 2.dp else 1.dp,
            color = if (isEnabled) {
                activeColor.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isEnabled) 6.dp else 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Toggle header with switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Icon that changes based on enabled state
                    (if (isEnabled) enabledIcon else disabledIcon)?.let {
                        AppIcon(
                            icon = it,
                            backgroundColor = if (isEnabled) {
                                activeColor
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerHigh
                            },
                            backgroundAlpha = if (isEnabled) 0.6f else 0.5f,
                            iconTint = if (isEnabled) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            tonalElevation = if (isEnabled) 2.dp else 1.dp
                        )
                    }

                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (description != null && isEnabled) {
                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Toggle switch
                Switch(
                    checked = isEnabled,
                    onCheckedChange = onEnabledChange,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = activeColor,
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.width(52.dp)
                )
            }

            // Content reveal when enabled
            if (isEnabled) {
                content()
            }
        }
    }
}

/**
 * CompactFormCard - More compact version of form card
 *
 * Same styling as FormCard but with reduced padding and spacing for
 * more compact layouts.
 */
@Composable
fun CompactFormCard(
    modifier: Modifier = Modifier,
    headerIcon: ImageVector? = null,
    headerTitle: String? = null,
    headerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Optional header section
            if (headerIcon != null && headerTitle != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppIcon(
                        icon = headerIcon,
                        backgroundColor = headerColor,
                        variant = IconBackgroundVariant.Compact
                    )

                    Text(
                        text = headerTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Card content
            content()
        }
    }
}

/**
 * InputCard - Form card specifically designed for input fields
 *
 * A specialized form card optimized for containing input fields with
 * consistent spacing and styling.
 */
@Composable
fun InputCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }

            content()
        }
    }
}

/**
 * ContentCard - Versatile content card with header and body sections
 *
 * A more flexible card template that provides consistent structure for
 * various content types. Perfect for detail views, input groups, and
 * any content that needs both header and body sections.
 *
 * Features:
 * - Optional header with icon and title
 * - Consistent Material 3 Expressive styling
 * - Flexible content slot
 * - Proper spacing and padding
 * - Enhanced elevation and borders
 *
 * Usage:
 * ```kotlin
 * ContentCard(
 *     headerIcon = Icons.Default.Link,
 *     headerTitle = "Link Details",
 *     headerColor = MaterialTheme.colorScheme.primary
 * ) {
 *     // Your content here
 * }
 * ```
 */
@Composable
fun ContentCard(
    modifier: Modifier = Modifier,
    headerIcon: ImageVector? = null,
    headerIconBackgroundColor : androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surfaceContainerLow,
    headerTitle: String? = null,
    headerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Optional header section
            if (headerIcon != null && headerTitle != null) {
                FormCardHeader(
                    icon = headerIcon,
                    title = headerTitle,
                    iconBackgroundColor = headerIconBackgroundColor
                )
            }

            // Card content
            content()
        }
    }
}

/**
 * SectionContentCard - Card with title and content sections
 *
 * A simpler card template that provides a title above the main content
 * without requiring an icon. Useful for content sections that don't need
 * the visual weight of an icon header.
 *
 * Features:
 * - Text-based header (no icon required)
 * - Material 3 Expressive styling
 * - Consistent spacing and layout
 * - Flexible content slot
 *
 * Usage:
 * ```kotlin
 * SectionContentCard(
 *     title = "Personal Notes"
 * ) {
 *     // Your content here
 * }
 * ```
 */
@Composable
fun SectionContentCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Optional title section
            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = titleColor
                )
            }

            // Card content
            content()
        }
    }
}