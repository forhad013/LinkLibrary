package com.greenrobotdev.linklibrary.design.components.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.greenrobotdev.linklibrary.design.theme.ComponentSize
import com.greenrobotdev.linklibrary.design.theme.ComponentStyle

/**
 * Outlined button - Convenience wrapper for StandardButton with outlined style
 *
 * This is a simplified API for outlined buttons that internally uses StandardButton
 * with ComponentStyle.Outlined. For full customization options, use StandardButton directly.
 *
 * ## Usage Example
 * ```kotlin
 * // Basic outlined button
 * OutlinedButton(
 *     text = "Cancel",
 *     onClick = { /* handle click */ }
 * )
 *
 * // With icon
 * OutlinedButton(
 *     text = "Learn More",
 *     icon = Icons.Default.Info,
 *     onClick = { /* handle click */ }
 * )
 *
 * // For more options, use StandardButton directly
 * StandardButton(
 *     text = "Custom",
 *     onClick = { /* handle click */ },
 *     style = ComponentStyle.Outlined,
 *     size = ComponentSize.Large,
 *     colors = ComponentColors(container = /* custom */)
 * )
 * ```
 *
 * ## Material 3 Compliance
 * - ✅ Expressive press-to-scale animation (0.97x when pressed)
 * - ✅ Border color transitions and smooth animations
 * - ✅ Enhanced ripple effects with Material 3 radius
 * - ✅ Proper accessibility semantics and content descriptions
 * - ✅ Theme-consistent color tokens with override capability
 *
 * @param text Required: Button text label
 * @param onClick Required: Callback invoked when button is clicked
 * @param modifier Optional: Modifier to be applied to the button
 * @param enabled Optional: Whether the button is enabled for interaction (default: true)
 * @param icon Optional: Leading icon to display before text
 * @param size Optional: Button size variant (default: ComponentSize.Medium)
 * @param colors Optional: Custom color overrides
 */
@Composable
fun OutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    size: ComponentSize = ComponentSize.Medium,
    colors: ComponentColors? = null
) {
    StandardButton(
        onClick = onClick,
        modifier = modifier,
        text = text,
        enabled = enabled,
        style = ComponentStyle.Outlined,
        size = size,
        icon = icon,
        colors = colors
    )
}

/**
 * Destructive outlined button - Convenience wrapper for StandardButton with error colors
 *
 * This is a simplified API for destructive outlined buttons that internally uses StandardButton
 * with ComponentStyle.Outlined and error color overrides. For full customization options,
 * use StandardButton directly with custom ComponentColors.
 *
 * ## Usage Example
 * ```kotlin
 * // Basic destructive button
 * DestructiveOutlinedButton(
 *     text = "Delete",
 *     onClick = { /* handle destructive action */ }
 * )
 *
 * // With icon
 * DestructiveOutlinedButton(
 *     text = "Remove",
 *     icon = Icons.Default.Delete,
 *     onClick = { /* handle destructive action */ }
 * )
 *
 * // For more control, use StandardButton directly
 * StandardButton(
 *     text = "Custom Destructive",
 *     onClick = { /* handle click */ },
 *     style = ComponentStyle.Outlined,
 *     colors = ComponentColors(
 *         content = MaterialTheme.colorScheme.error
 *     )
 * )
 * ```
 *
 * ## Material 3 Compliance
 * - ✅ Expressive press-to-scale animation with bouncy spring physics
 * - ✅ Error color transitions and smooth animations
 * - ✅ Enhanced visual feedback for destructive actions
 * - ✅ Proper accessibility semantics for destructive actions
 * - ✅ Theme-consistent error color tokens
 *
 * @param text Required: Button text label
 * @param onClick Required: Callback invoked when button is clicked
 * @param modifier Optional: Modifier to be applied to the button
 * @param enabled Optional: Whether the button is enabled for interaction (default: true)
 * @param icon Optional: Leading icon to display before text
 * @param size Optional: Button size variant (default: ComponentSize.Medium)
 */
@Composable
fun DestructiveOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    size: ComponentSize = ComponentSize.Medium
) {
    StandardButton(
        onClick = onClick,
        modifier = modifier,
        text = text,
        enabled = enabled,
        style = ComponentStyle.Outlined,
        size = size,
        icon = icon,
        colors = ComponentColors(
            content = androidx.compose.material3.MaterialTheme.colorScheme.error,
            border = androidx.compose.material3.MaterialTheme.colorScheme.error
        )
    )
}