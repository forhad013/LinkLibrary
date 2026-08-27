package com.greenrobotdev.linklibrary.design.expressive.effects

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.greenrobotdev.linklibrary.design.expressive.animation.SpringPhysics

/**
 * Material Design 3 Expressive Elevation Effects
 *
 * Provides dynamic elevation animations following Material 3 tonal elevation system.
 * Elevation in MD3 is communicated through tonal surface color, not just shadows.
 */

/**
 * Elevation levels following Material 3 guidelines
 */
object ElevationLevels {
    /** Level 0 - Flat surfaces, most components at rest */
    val Level0 = 0.dp

    /** Level 1 - Elevated cards, modal sheets (1dp) */
    val Level1 = 1.dp

    /** Level 2 - Menus, nav bar, scrolled app bar (3dp) */
    val Level2 = 3.dp

    /** Level 3 - FAB, dialogs, search, date/time pickers (6dp) */
    val Level3 = 6.dp

    /** Level 4 - Hover/focus increase only (8dp) */
    val Level4 = 8.dp

    /** Level 5 - Hover/focus increase only (12dp) */
    val Level5 = 12.dp
}

/**
 * Calculate target elevation based on interaction state
 * Used for dynamic elevation animations
 *
 * @param isPressed Whether component is pressed
 * @param isHovered Whether component is hovered (desktop)
 * @param isFocused Whether component has keyboard focus
 * @param baseElevation Base elevation level
 */
fun calculateTargetElevation(
    isPressed: Boolean = false,
    isHovered: Boolean = false,
    isFocused: Boolean = false,
    baseElevation: Dp = ElevationLevels.Level0
): Dp {
    return when {
        isPressed -> baseElevation + 4.dp  // Elevate further when pressed
        isHovered || isFocused -> baseElevation + 2.dp // Slight elevation on hover/focus
        else -> baseElevation
    }
}

/**
 * Animate elevation with spring physics
 *
 * @param targetElevation Target elevation value
 * @param animationSpec Optional custom animation spec
 */
@Composable
fun animateElevation(
    targetElevation: Dp,
    animationSpec: AnimationSpec<Dp> = SpringPhysics.defaultSpring()
): Dp {
    val elevation by animateDpAsState(
        targetValue = targetElevation,
        animationSpec = animationSpec,
        label = "elevation"
    )
    return elevation
}

/**
 * Animate elevation with custom tween
 *
 * @param targetElevation Target elevation value
 * @param durationMillis Duration of animation in milliseconds
 */
@Composable
fun animateElevationTween(
    targetElevation: Dp,
    durationMillis: Int = 300
): Dp {
    val elevation by animateDpAsState(
        targetValue = targetElevation,
        animationSpec = tween(durationMillis = durationMillis),
        label = "elevation"
    )
    return elevation
}

/**
 * Animate tonal elevation color based on elevation level
 * Material 3 uses tonal surface colors instead of shadows
 *
 * @param surfaceColor Base surface color
 * @param elevation Current elevation level
 * @param animationSpec Optional custom animation spec
 */
@Composable
fun animateElevationColor(
    surfaceColor: Color,
    elevation: Dp,
    animationSpec: AnimationSpec<Color> = SpringPhysics.defaultSpring()
): Color {
    // Calculate tonal offset based on elevation
    val tonalOffset = when (elevation) {
        ElevationLevels.Level0 -> 0f
        ElevationLevels.Level1 -> 0.05f
        ElevationLevels.Level2 -> 0.08f
        ElevationLevels.Level3 -> 0.11f
        ElevationLevels.Level4 -> 0.12f
        ElevationLevels.Level5 -> 0.14f
        else -> 0f
    }

    // Apply tonal offset to create elevated color
    val targetColor = if (tonalOffset > 0f) {
        surfaceColor.copy(
            red = (surfaceColor.red + tonalOffset).coerceAtMost(1f),
            green = (surfaceColor.green + tonalOffset).coerceAtMost(1f),
            blue = (surfaceColor.blue + tonalOffset).coerceAtMost(1f)
        )
    } else {
        surfaceColor
    }

    val color by animateColorAsState(
        targetValue = targetColor,
        animationSpec = animationSpec,
        label = "elevation_color"
    )

    return color
}

/**
 * Modifier to apply animated elevation effect
 *
 * @param isPressed Whether component is pressed
 * @param isHovered Whether component is hovered
 * @param isFocused Whether component has keyboard focus
 * @param baseElevation Base elevation level
 */
fun Modifier.animateElevation(
    isPressed: Boolean = false,
    isHovered: Boolean = false,
    isFocused: Boolean = false,
    baseElevation: Dp = ElevationLevels.Level0
): Modifier = composed {
    val targetElevation = calculateTargetElevation(
        isPressed = isPressed,
        isHovered = isHovered,
        isFocused = isFocused,
        baseElevation = baseElevation
    )

    val elevation = animateElevation(targetElevation)

    this.then(Modifier)
}