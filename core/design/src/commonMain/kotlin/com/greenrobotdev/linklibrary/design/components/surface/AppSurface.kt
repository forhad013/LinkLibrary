package com.greenrobotdev.linklibrary.design.components.surface

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
 * AppSurface - Comprehensive surface component system with contextual usage styles
 *
 * Provides a complete set of surface components optimized for different contexts
 * throughout the application. Each component is pre-configured with appropriate
 * Material 3 elevation, colors, and shapes.
 *
 * Design Philosophy:
 * - Consistent Material 3 Expressive styling
 * - Context-appropriate elevation and tonal values
 * - Enhanced dark theme support with proper contrast
 * - Flexible content slots and layouts
 * - Reusable across all application screens
 *
 * Categories:
 * - Container: General purpose containers with different elevations
 * - Interactive: Clickable surfaces for buttons and interactions
 * - Icon Containers: Specialized surfaces for icon display
 * - Status: Status indicator surfaces
 * - Special: Empty states, badges, chips
 */

// ==================== CONTAINER SURFACES ====================

/**
 * BaseSurface - Standard container surface
 *
 * Usage: General purpose content container, grouping elements
 * Style: surfaceContainer color with subtle elevation
 *
 * @param modifier Optional modifier
 * @param content Content to display inside the surface
 */
@Composable
fun BaseSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 1.dp,
        content = content
    )
}

/**
 * ElevatedSurface - Surface with higher elevation
 *
 * Usage: Important content, floating cards, elevated sections
 * Style: surfaceContainerHighest with increased elevation
 *
 * @param modifier Optional modifier
 * @param content Content to display inside the surface
 */
@Composable
fun ElevatedSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        tonalElevation = 3.dp,
        shadowElevation = 4.dp,
        content = content
    )
}

/**
 * SubtleSurface - Low elevation surface for subtle grouping
 *
 * Usage: Secondary content, subtle grouping, background areas
 * Style: surfaceContainerLow with minimal elevation
 *
 * @param modifier Optional modifier
 * @param content Content to display inside the surface
 */
@Composable
fun SubtleSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 0.dp,
        content = content
    )
}

/**
 * BorderedSurface - Surface with visible border
 *
 * Usage: Form sections, outlined content areas, defined boundaries
 * Style: surfaceContainer with outlineVariant border
 *
 * @param modifier Optional modifier
 * @param borderColor Optional border color (defaults to outlineVariant)
 * @param content Content to display inside the surface
 */
@Composable
fun BorderedSurface(
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, borderColor),
        tonalElevation = 1.dp,
        content = content
    )
}

// ==================== INTERACTIVE SURFACES ====================

/**
 * InteractiveSurface - Clickable surface with proper feedback
 *
 * Usage: Clickable containers, interactive cards, button alternatives
 * Style: surfaceContainer with hover/focus states
 *
 * @param onClick Callback when surface is clicked
 * @param modifier Optional modifier
 * @param enabled Whether surface is interactive
 * @param content Content to display inside the surface
 */
@Composable
fun InteractiveSurface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 2.dp,
        content = content
    )
}

/**
 * TonalInteractiveSurface - Interactive surface with tonal emphasis
 *
 * Usage: Primary interactive areas, important clickable sections
 * Style: primaryContainer tonal surface for emphasis
 *
 * @param onClick Callback when surface is clicked
 * @param modifier Optional modifier
 * @param enabled Whether surface is interactive
 * @param content Content to display inside the surface
 */
@Composable
fun TonalInteractiveSurface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
        tonalElevation = 2.dp,
        content = content
    )
}

/**
 * StatusInteractiveSurface - Interactive surface for status/actions
 *
 * Usage: Status cards, action items, interactive status displays
 * Style: tertiaryContainer with custom color support
 *
 * @param onClick Callback when surface is clicked
 * @param modifier Optional modifier
 * @param statusColor Color for status indication
 * @param enabled Whether surface is interactive
 * @param content Content to display inside the surface
 */
@Composable
fun StatusInteractiveSurface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    statusColor: Color = MaterialTheme.colorScheme.tertiary,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        color = statusColor.copy(alpha = 0.2f),
        tonalElevation = 2.dp,
        content = content
    )
}

// ==================== ICON CONTAINER SURFACES ====================

/**
 * IconSurface - Small surface for icon display
 *
 * Usage: Icon backgrounds, small icon containers, icon buttons
 * Style: Circular or rounded icon container
 *
 * @param icon Icon to display
 * @param modifier Optional modifier
 * @param iconTint Tint color for icon
 * @param containerColor Background color for icon container
 * @param iconSize Size of the icon
 * @param contentDescription Content description for accessibility
 */
@Composable
fun IconSurface(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    iconSize: androidx.compose.ui.unit.DpSize = androidx.compose.ui.unit.DpSize(24.dp, 24.dp),
    contentDescription: String? = null
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        shape = CircleShape,
        tonalElevation = 1.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconTint,
                modifier = Modifier.size(iconSize.width, iconSize.height)
            )
        }
    }
}

/**
 * RoundedIconSurface - Rounded rectangle icon container
 *
 * Usage: Icon badges, squared icon containers, app icons
 * Style: Rounded rectangle shape
 *
 * @param icon Icon to display
 * @param modifier Optional modifier
 * @param iconTint Tint color for icon
 * @param containerColor Background color for icon container
 * @param cornerRadius Corner radius for rounding
 * @param contentDescription Content description for accessibility
 */
@Composable
fun RoundedIconSurface(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    cornerRadius: androidx.compose.ui.unit.Dp = 12.dp,
    contentDescription: String? = null
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        shape = RoundedCornerShape(cornerRadius),
        tonalElevation = 1.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * LargeIconSurface - Larger icon container with emphasis
 *
 * Usage: Hero icons, featured icons, large status indicators
 * Style: Larger surface with enhanced elevation
 *
 * @param icon Icon to display
 * @param modifier Optional modifier
 * @param iconTint Tint color for icon
 * @param containerColor Background color for icon container
 * @param contentDescription Content description for accessibility
 */
@Composable
fun LargeIconSurface(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentDescription: String? = null
) {
    Surface(
        modifier = modifier.size(56.dp),
        color = containerColor,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

// ==================== STATUS SURFACES ====================

/**
 * StatusSurface - Status indicator surface
 *
 * Usage: Status badges, state indicators, condition displays
 * Style: Compact surface with status color
 *
 * @param status Status text to display
 * @param modifier Optional modifier
 * @param statusColor Color for status indication
 * @param icon Optional status icon
 */
@Composable
fun StatusSurface(
    status: String,
    modifier: Modifier = Modifier,
    statusColor: Color = MaterialTheme.colorScheme.primary,
    icon: ImageVector? = null
) {
    Surface(
        modifier = modifier,
        color = statusColor.copy(alpha = 0.2f),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(14.dp)
                )
            }

            Text(
                text = status,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = statusColor
            )
        }
    }
}

/**
 * ErrorSurface - Error state surface
 *
 * Usage: Error indicators, error badges, error states
 * Style: Error color scheme with warning appearance
 *
 * @param error Error message to display
 * @param modifier Optional modifier
 * @param icon Optional error icon
 */
@Composable
fun ErrorSurface(
    error: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    StatusSurface(
        status = error,
        modifier = modifier,
        statusColor = MaterialTheme.colorScheme.error,
        icon = icon
    )
}

/**
 * SuccessSurface - Success state surface
 *
 * Usage: Success indicators, completion badges, success states
 * Style: Tertiary color scheme for positive indication
 *
 * @param success Success message to display
 * @param modifier Optional modifier
 * @param icon Optional success icon
 */
@Composable
fun SuccessSurface(
    success: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    StatusSurface(
        status = success,
        modifier = modifier,
        statusColor = MaterialTheme.colorScheme.tertiary,
        icon = icon
    )
}

// ==================== SPECIAL SURFACES ====================

/**
 * EmptyStateSurface - Surface for empty state content
 *
 * Usage: Empty states, placeholder content, no-data states
 * Style: Large surface with subtle styling for empty content
 *
 * @param modifier Optional modifier
 * @param icon Optional icon for empty state
 * @param title Title text for empty state
 * @param description Optional description text
 * @param content Optional additional content
 */
@Composable
fun EmptyStateSurface(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String? = null,
    description: String? = null,
    content: @Composable () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(48.dp)
                )
            }

            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }

            content()
        }
    }
}

/**
 * BadgeSurface - Small badge surface
 *
 * Usage: Count badges, notification badges, small indicators
 * Style: Compact circular or rounded surface
 *
 * @param count Count or badge text to display
 * @param modifier Optional modifier
 * @param badgeColor Color for badge
 * @param maxCount Maximum count before showing "+X"
 */
@Composable
fun BadgeSurface(
    count: Int,
    modifier: Modifier = Modifier,
    badgeColor: Color = MaterialTheme.colorScheme.error,
    maxCount: Int = 99
) {
    val displayText = if (count > maxCount) "$maxCount+" else count.toString()

    Surface(
        modifier = modifier,
        color = badgeColor,
        shape = CircleShape,
        tonalElevation = 2.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = displayText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * ChipSurface - Chip-style surface for tags and filters
 *
 * Usage: Tags, filters, selectable chips, categories
 * Style: Rounded pill-shaped surface
 *
 * @param text Text to display on chip
 * @param modifier Optional modifier
 * @param selected Whether chip is selected
 * @param onClick Optional callback when chip is clicked
 * @param leadingIcon Optional leading icon
 */
@Composable
fun ChipSurface(
    text: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    leadingIcon: ImageVector? = null
) {
    val surfaceColor = if (selected) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainer
    }

    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier,
            color = surfaceColor,
            shape = RoundedCornerShape(16.dp),
            tonalElevation = if (selected) 2.dp else 1.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                )
            }
        }
    } else {
        Surface(
            modifier = modifier,
            color = surfaceColor,
            shape = RoundedCornerShape(16.dp),
            tonalElevation = if (selected) 2.dp else 1.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                )
            }
        }
    }
}