package com.greenrobotdev.linklibrary.design.components.icon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * IconBackground - Standardized icon background component for consistent appearance across all screens
 *
 * Provides a unified icon background implementation that replaces all the inconsistent
 * Surface-based icon containers throughout the application. This ensures that icons
 * appear with consistent sizing, spacing, and styling across Link Details, Organization,
 * and all other screens.
 *
 * Design Philosophy:
 * - Consistent Material 3 Expressive styling
 * - Standardized sizing and spacing
 * - Proper corner radius for all icon backgrounds
 * - Support for both regular and compact variants
 * - Conditional styling support for state-based appearances
 *
 * Features:
 * - Regular size (32dp container, 18dp icon) for standard headers
 * - Compact size (24dp container, 14dp icon) for compact layouts
 * - Consistent corner radius (8dp for regular, 6dp for compact)
 * - Customizable background color and alpha
 * - Optional tonal elevation for depth
 * - Consistent icon tint color support
 *
 * @param icon The icon vector to display
 * @param modifier Optional modifier for layout
 * @param variant Size variant (Regular or Compact)
 * @param backgroundColor Background color for the icon container
 * @param backgroundAlpha Alpha value for the background color (default: 0.4f)
 * @param iconTint Optional tint color for the icon (default: backgroundColor)
 * @param tonalElevation Optional tonal elevation for depth
 */
@Composable
fun AppIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    variant: IconBackgroundVariant = IconBackgroundVariant.Regular,
    backgroundColor: androidx.compose.ui.graphics.Color? = MaterialTheme.colorScheme.primary,
    backgroundAlpha: Float = 0.4f,
    iconTint: androidx.compose.ui.graphics.Color? = null,
    tonalElevation: androidx.compose.ui.unit.Dp = 0.dp
) {
    val (containerSize, cornerRadius, iconSize) = when (variant) {
        IconBackgroundVariant.Regular -> Triple(32.dp, 8.dp, 18.dp)
        IconBackgroundVariant.Compact -> Triple(24.dp, 6.dp, 14.dp)
    }

    Surface(
        modifier = modifier.size(containerSize),
        shape = RoundedCornerShape(cornerRadius),
        color = backgroundColor?.copy(alpha = backgroundAlpha) ?: MaterialTheme.colorScheme.primary,
        tonalElevation = tonalElevation
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint ?: backgroundColor ?: MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

/**
 * IconBackgroundVariant - Size variants for icon backgrounds
 *
 * Defines the available size options for icon backgrounds to ensure
 * consistent sizing across the application.
 */
enum class IconBackgroundVariant {
    /**
     * Regular size (32dp container, 18dp icon)
     * Use for standard headers, form cards, and general icon displays
     */
    Regular,

    /**
     * Compact size (24dp container, 14dp icon)
     * Use for compact layouts, smaller cards, and space-constrained UI
     */
    Compact
}

/**
 * IconBackgroundRegular - Convenience function for regular-sized icon backgrounds
 *
 * Simplified call for the most common use case (regular size with default styling).
 *
 * @param icon The icon vector to display
 * @param modifier Optional modifier for layout
 * @param backgroundColor Background color for the icon container
 * @param iconTint Optional tint color for the icon
 */
@Composable
fun IconBackgroundRegular(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    backgroundColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    iconTint: androidx.compose.ui.graphics.Color? = null
) {
    AppIcon(
        icon = icon,
        modifier = modifier,
        variant = IconBackgroundVariant.Regular,
        backgroundColor = backgroundColor,
        iconTint = iconTint
    )
}

/**
 * IconBackgroundCompact - Convenience function for compact icon backgrounds
 *
 * Simplified call for compact-sized icon backgrounds with default styling.
 *
 * @param icon The icon vector to display
 * @param modifier Optional modifier for layout
 * @param backgroundColor Background color for the icon container
 * @param iconTint Optional tint color for the icon
 */
@Composable
fun IconBackgroundCompact(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    backgroundColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    iconTint: androidx.compose.ui.graphics.Color? = null
) {
    AppIcon(
        icon = icon,
        modifier = modifier,
        variant = IconBackgroundVariant.Compact,
        backgroundColor = backgroundColor,
        iconTint = iconTint
    )
}