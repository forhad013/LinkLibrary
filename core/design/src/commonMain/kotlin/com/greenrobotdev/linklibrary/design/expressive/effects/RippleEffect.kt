package com.greenrobotdev.linklibrary.design.expressive.effects

import androidx.compose.foundation.Indication
import androidx.compose.foundation.indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ripple

/**
 * Material Design 3 Expressive Ripple Effects
 *
 * Provides ripple indication effects with enhanced animations and customization.
 * Ripples provide visual feedback for touch/click interactions following Material 3 guidelines.
 */

/**
 * Enhanced ripple effect with custom radius and color
 *
 * @param interactionSource Source of interactions for the ripple
 * @param radius Maximum radius of the ripple animation
 * @param color Optional custom color for the ripple
 */
@Composable
fun Modifier.expressiveRipple(
    interactionSource: MutableInteractionSource,
    radius: androidx.compose.ui.unit.Dp = androidx.compose.ui.unit.Dp.Unspecified,
    color: androidx.compose.ui.graphics.Color = androidx.compose.material3.MaterialTheme.colorScheme.primary
): Modifier {
    return indication(
        interactionSource = interactionSource,
        indication = ripple(
            radius = radius,
            color = color
        )
    )
}

/**
 * Expressive clickable modifier with integrated ripple effect
 *
 * @param onClick Callback invoked when component is clicked
 * @param interactionSource Source of interactions for the ripple
 * @param enabled Whether the clickable is enabled
 * @param onClickLabel Optional semantic label for the click action
 * @param role Optional semantic role for accessibility
 */
@Composable
fun Modifier.expressiveClickable(
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: androidx.compose.ui.semantics.Role? = null
): Modifier {
    return clickable(
        onClickLabel = onClickLabel,
        role = role,
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        indication = ripple()
    )
}

/**
 * Bouncy ripple effect with enhanced spring physics
 * Creates a more playful, expressive ripple animation
 *
 * @param interactionSource Source of interactions for the ripple
 * @param radius Maximum radius of the ripple animation
 */
@Composable
fun Modifier.bouncyRipple(
    interactionSource: MutableInteractionSource,
    radius: androidx.compose.ui.unit.Dp = androidx.compose.ui.unit.dp(40)
): Modifier {
    return indication(
        interactionSource = interactionSource,
        indication = ripple(
            radius = radius,
            color = androidx.compose.material3.MaterialTheme.colorScheme.primary
        )
    )
}

/**
 * Subtle ripple effect for less prominent interactions
 * Uses reduced opacity and smaller radius
 *
 * @param interactionSource Source of interactions for the ripple
 * @param radius Maximum radius of the ripple animation (defaults to smaller size)
 */
@Composable
fun Modifier.subtleRipple(
    interactionSource: MutableInteractionSource,
    radius: androidx.compose.ui.unit.Dp = androidx.compose.ui.unit.dp(24)
): Modifier {
    return indication(
        interactionSource = interactionSource,
        indication = ripple(
            radius = radius,
            color = androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        )
    )
}