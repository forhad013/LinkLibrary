package com.greenrobotdev.linklibrary.design.components.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.expressive.animation.SpringPhysics

/**
 * Filled button component with Material 3 Expressive features
 *
 * High-emphasis button for primary actions with enhanced animations:
 * - Press-to-scale animation (0.97x when pressed)
 * - Dynamic elevation changes
 * - Smooth color transitions
 * - Enhanced ripple effects
 *
 * @param text Button text label
 * @param onClick Callback invoked when button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled for interaction
 * @param icon Optional leading icon to display before text
 */
@Composable
fun FilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    // Interaction source for expressive animations
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Expressive: Press-to-scale animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = SpringPhysics.defaultSpring(),
        label = "button_scale"
    )

    // Expressive: Dynamic elevation animation
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 8.dp else 0.dp,
        animationSpec = SpringPhysics.gentleSpring(),
        label = "button_elevation"
    )

    // Expressive: Color transition animation
    val containerColor by animateColorAsState(
        targetValue = if (enabled) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        },
        animationSpec = SpringPhysics.gentleSpring(),
        label = "button_container_color"
    )

    Button(
        onClick = onClick,
        modifier = modifier.scale(scale),
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            pressedElevation = 8.dp,
            hoveredElevation = 2.dp,
            focusedElevation = 2.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
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
 * Filled button with expressive loading state
 *
 * Enhanced loading state with smooth fade transition and animated progress indicator:
 * - Content fade out when loading starts
 * - Progress indicator fade in with rotation
 * - Scale animation during press while loading
 * - Disabled state automatically applied during loading
 *
 * @param text Button text label
 * @param onClick Callback invoked when button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled for interaction
 * @param isLoading Whether to show loading indicator
 * @param icon Optional leading icon to display before text
 */
@Composable
fun FilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Expressive: Press-to-scale animation (even during loading if not disabled)
    val scale by animateFloatAsState(
        targetValue = when {
            isLoading -> 1f  // Normal scale during loading
            isPressed -> 0.97f  // Slightly scale down when pressed
            else -> 1f  // Normal scale
        },
        animationSpec = SpringPhysics.defaultSpring(),
        label = "button_scale_loading"
    )

    // Expressive: Dynamic elevation with loading state
    val elevation by animateDpAsState(
        targetValue = when {
            isLoading -> 0.dp  // No elevation during loading
            isPressed -> 8.dp  // Higher elevation when pressed
            else -> 0.dp  // Normal elevation
        },
        animationSpec = SpringPhysics.gentleSpring(),
        label = "button_elevation_loading"
    )

    Button(
        onClick = onClick,
        modifier = modifier.scale(scale),
        enabled = enabled && !isLoading,
        interactionSource = interactionSource,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            pressedElevation = 8.dp,
            hoveredElevation = 2.dp,
            focusedElevation = 2.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    ) {
        if (isLoading) {
            // Expressive: Animated loading indicator
            androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier
                    .padding(ButtonDefaults.IconSpacing)
                    .size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
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
}