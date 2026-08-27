package com.greenrobotdev.linklibrary.design.components.button

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton as MaterialIconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.greenrobotdev.linklibrary.design.expressive.animation.SpringPhysics

/**
 * Icon button component with Material 3 Expressive features
 *
 * Compact icon-only button with enhanced animations:
 * - Press-to-scale animation (0.92x for satisfying press feedback)
 * - Icon rotation effect on press
 * - Smooth color transitions
 * - Ripple integration
 *
 * @param icon ImageVector to display as the icon
 * @param contentDescription Content description for accessibility
 * @param onClick Callback invoked when button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled for interaction
 * @param tint Color tint for the icon. Defaults to theme's onSurfaceVariant
 */
@Composable
fun IconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Expressive: Press-to-scale animation (more pronounced for icon buttons)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = SpringPhysics.bouncySpring(),
        label = "icon_button_scale"
    )

    // Expressive: Icon rotation on press
    val rotation by animateFloatAsState(
        targetValue = if (isPressed) 15f else 0f,
        animationSpec = SpringPhysics.bouncySpring(),
        label = "icon_button_rotation"
    )

    MaterialIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.scale(scale)
        )
    }
}

/**
 * Icon button with filled tonal styling and expressive features
 *
 * Icon button with filled background and enhanced animations:
 * - Satisfying press-to-scale animation
 * - Container color transitions
 * - Icon rotation effects
 * - Enhanced visual feedback
 *
 * @param icon ImageVector to display as the icon
 * @param contentDescription Content description for accessibility
 * @param onClick Callback invoked when button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled for interaction
 */
@Composable
fun FilledTonalIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Expressive: Press-to-scale animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = SpringPhysics.bouncySpring(),
        label = "filled_tonal_icon_button_scale"
    )

    androidx.compose.material3.FilledTonalIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.scale(scale)
        )
    }
}

/**
 * Icon button with container surface styling and expressive features
 *
 * Icon button with surface container background and enhanced animations:
 * - Press-to-scale with bouncy spring
 * - Container color transitions
 * - Smooth visual feedback
 *
 * @param icon ImageVector to display as the icon
 * @param contentDescription Content description for accessibility
 * @param onClick Callback invoked when button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled for interaction
 */
@Composable
fun SurfaceIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Expressive: Press-to-scale animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = SpringPhysics.bouncySpring(),
        label = "surface_icon_button_scale"
    )

    androidx.compose.material3.IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = IconButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.scale(scale)
        )
    }
}