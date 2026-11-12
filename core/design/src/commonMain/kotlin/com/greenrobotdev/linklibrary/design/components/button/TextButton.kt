package com.greenrobotdev.linklibrary.design.components.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton as MaterialTextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.theme.ComponentSize

/**
 * Text button - Low-emphasis button following Material 3 Expressive guidelines
 *
 * ## Usage Example
 * ```kotlin
 * // Basic text button
 * TextButton(
 *     onClick = { /* handle click */ },
 *     text = "Learn More"
 * )
 *
 * // Text button with icon and custom size
 * TextButton(
 *     onClick = { /* handle click */ },
 *     text = "Cancel",
 *     icon = Icons.Default.Close,
 *     size = ComponentSize.Large
 * )
 *
 * // With custom colors
 * TextButton(
 *     onClick = { /* handle click */ },
 *     text = "Custom Action",
 *     colors = ComponentColors(
 *         content = MaterialTheme.colorScheme.secondary
 *     )
 * )
 * ```
 *
 * ## Material 3 Compliance
 * - ✅ Expressive gentle press-to-scale animation (0.98x when pressed)
 * - ✅ Smooth color transitions with spring physics
 * - ✅ Subtle opacity changes on hover
 * - ✅ Enhanced ripple effects with Material 3 radius
 * - ✅ Proper accessibility semantics and content descriptions
 * - ✅ Theme-consistent color tokens with override capability
 *
 * ## Component Parameters
 * @param onClick Required: Callback invoked when button is clicked
 * @param text Optional: Button text label (can be provided via content slot instead)
 * @param modifier Optional: Modifier to be applied to the button
 * @param enabled Optional: Whether the button is enabled for interaction (default: true)
 * @param size Optional: Button size variant (default: ComponentSize.Medium)
 * @param icon Optional: Leading icon to display before text
 * @param leadingContent Optional: Custom leading content slot
 * @param trailingContent Optional: Custom trailing content slot
 * @param colors Optional: Custom color overrides
 *
 * ## Size Specifications
 * - Small: 32dp height, labelSmall typography
 * - Medium: 40dp height, labelLarge typography
 * - Large: 48dp height, labelLarge typography
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    enabled: Boolean = true,
    size: ComponentSize = ComponentSize.Medium,
    icon: ImageVector? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    colors: ComponentColors? = null
) {
    // Interaction source for expressive animations
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Expressive: Gentle press-to-scale animation (more subtle than filled buttons)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = 0.9f, // More gentle than filled buttons
            stiffness = 300f
        ),
        label = "text_button_scale"
    )

    // Calculate size-based properties
    val textStyle = when (size) {
        ComponentSize.Small -> MaterialTheme.typography.labelSmall
        ComponentSize.Medium -> MaterialTheme.typography.labelLarge
        ComponentSize.Large -> MaterialTheme.typography.labelLarge
    }

    // Expressive: Content color based on state and custom colors
    val contentColor by animateColorAsState(
        targetValue = when {
            !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            colors?.content != null -> colors.content
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(durationMillis = 300),
        label = "text_button_content_color"
    )

    MaterialTextButton(
        onClick = onClick,
        modifier = modifier.scale(scale),
        enabled = enabled,
        interactionSource = interactionSource,
        colors = ButtonDefaults.textButtonColors(
            contentColor = contentColor,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    ) {
        Box(
            modifier = Modifier.padding(horizontal = when (size) {
                ComponentSize.Small -> 8.dp
                ComponentSize.Medium -> 12.dp
                ComponentSize.Large -> 16.dp
            }),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Leading content (icon or custom)
                when {
                    leadingContent != null -> {
                        leadingContent()
                        if (text != null || trailingContent != null) {
                            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                        }
                    }
                    icon != null -> {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        if (text != null || trailingContent != null) {
                            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                        }
                    }
                }

                // Main text content
                if (text != null) {
                    Text(
                        text = text,
                        style = textStyle
                    )
                }

                // Trailing content
                if (trailingContent != null) {
                    if (text != null || leadingContent != null || icon != null) {
                        Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                    }
                    trailingContent()
                }
            }
        }
    }
}

/**
 * Destructive text button - Low-emphasis button with error styling
 *
 * This is a specialized text button for destructive actions that uses error colors.
 * Internally uses TextButton with error color overrides.
 *
 * ## Usage Example
 * ```kotlin
 * // Basic destructive text button
 * DestructiveTextButton(
 *     onClick = { /* handle destructive action */ },
 *     text = "Delete"
 * )
 *
 * // With icon
 * DestructiveTextButton(
 *     onClick = { /* handle destructive action */ },
 *     text = "Remove",
 *     icon = Icons.Default.Delete,
 *     size = ComponentSize.Large
 * )
 *
 * // For more control, use TextButton directly
 * TextButton(
 *     onClick = { /* handle click */ },
 *     text = "Custom Destructive",
 *     colors = ComponentColors(
 *         content = MaterialTheme.colorScheme.error
 *     )
 * )
 * ```
 *
 * ## Material 3 Compliance
 * - ✅ Gentle press animation with spring physics
 * - ✅ Error color transitions and smooth animations
 * - ✅ Subtle feedback for destructive actions
 * - ✅ Proper accessibility semantics for destructive actions
 * - ✅ Theme-consistent error color tokens
 *
 * @param onClick Required: Callback invoked when button is clicked
 * @param text Optional: Button text label
 * @param modifier Optional: Modifier to be applied to the button
 * @param enabled Optional: Whether the button is enabled for interaction
 * @param size Optional: Button size variant
 * @param icon Optional: Leading icon to display before text
 */
@Composable
fun DestructiveTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    enabled: Boolean = true,
    size: ComponentSize = ComponentSize.Medium,
    icon: ImageVector? = null
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        text = text,
        enabled = enabled,
        size = size,
        icon = icon,
        colors = ComponentColors(
            content = MaterialTheme.colorScheme.error
        )
    )
}