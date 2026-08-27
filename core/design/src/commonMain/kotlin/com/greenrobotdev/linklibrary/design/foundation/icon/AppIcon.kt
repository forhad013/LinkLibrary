package com.greenrobotdev.linklibrary.design.foundation.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.greenrobotdev.linklibrary.design.tokens.SpacingTokens

/**
 * Icon size tokens following Material 3 guidelines
 */
object IconSizes {
    val Small = 18.dp
    val Medium = 24.dp
    val Large = 32.dp
    val ExtraLarge = 40.dp
}

/**
 * Foundation icon component with automatic theme integration and sizing
 *
 * This wrapper provides a simplified icon component that automatically integrates
 * with the Material 3 theme while offering sensible defaults for common use cases.
 *
 * @param imageVector Image vector to display
 * @param contentDescription Content description for accessibility
 * @param modifier Modifier to be applied to the icon
 * @param tint Color tint to apply. Defaults to theme's onSurfaceVariant color
 * @param size Icon size. Defaults to Medium (24dp)
 */
@Composable
fun AppIcon(
    imageVector: ImageVector,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    size: IconSize = IconSize.Medium
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.size(size.dp),
        tint = tint
    )
}

/**
 * Sealed class for icon size variants
 */
sealed class IconSize(val dp: androidx.compose.ui.unit.Dp) {
    object Small : IconSize(IconSizes.Small)
    object Medium : IconSize(IconSizes.Medium)
    object Large : IconSize(IconSizes.Large)
    object ExtraLarge : IconSize(IconSizes.ExtraLarge)
}

/**
 * Convenience overloads for commonly used icons
 */

/** Error icon for validation states */
@Composable
fun ErrorIcon(
    contentDescription: String? = "Error",
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.error,
    size: IconSize = IconSize.Medium
) {
    AppIcon(
        imageVector = Icons.Filled.Error,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint,
        size = size
    )
}