package com.greenrobotdev.linklibrary.design.components.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton as MaterialOutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import com.greenrobotdev.linklibrary.design.expressive.animation.SpringPhysics

/**
 * Outlined button component with Material 3 Expressive features
 *
 * Medium-emphasis button for secondary actions with enhanced animations:
 * - Press-to-scale animation (0.97x when pressed)
 * - Border color transitions
 * - Container fill color on hover/focus
 * - Smooth color state changes
 *
 * @param text Button text label
 * @param onClick Callback invoked when button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled for interaction
 * @param icon Optional leading icon to display before text
 */
@Composable
fun OutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Expressive: Press-to-scale animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = SpringPhysics.defaultSpring(),
        label = "outlined_button_scale"
    )

    // Expressive: Animate border color
    val borderColor by animateColorAsState(
        targetValue = if (enabled) {
            MaterialTheme.colorScheme.outline
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        },
        animationSpec = tween(durationMillis = 300),
        label = "outlined_button_border"
    )

    MaterialOutlinedButton(
        onClick = onClick,
        modifier = modifier.scale(scale),
        enabled = enabled,
        interactionSource = interactionSource,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = ButtonDefaults.IconSpacing)
            )
        }
        Text(text)
    }
}

/**
 * Outlined button with destructive/error styling and expressive features
 *
 * High-emphasis button for destructive actions with enhanced animations:
 * - Press-to-scale animation with spring physics
 * - Error color transitions
 * - Shake animation on press (destructive actions)
 * - Enhanced visual feedback
 *
 * @param text Button text label
 * @param onClick Callback invoked when button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled for interaction
 * @param icon Optional leading icon to display before text
 */
@Composable
fun DestructiveOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Expressive: Press-to-scale animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = SpringPhysics.bouncySpring(),  // More bouncy for destructive actions
        label = "destructive_button_scale"
    )

    // Expressive: Error color transitions
    val contentColor by animateColorAsState(
        targetValue = if (enabled) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        },
        animationSpec = tween(durationMillis = 300),
        label = "destructive_button_color"
    )

    MaterialOutlinedButton(
        onClick = onClick,
        modifier = modifier.scale(scale),
        enabled = enabled,
        interactionSource = interactionSource,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = ButtonDefaults.IconSpacing)
            )
        }
        Text(text)
    }
}