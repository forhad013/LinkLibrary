package com.greenrobotdev.linklibrary.design.expressive.effects

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme

/**
 * Material Design 3 Expressive Shimmer Effect
 *
 * Provides shimmer/skeleton loading animations following Material 3 guidelines.
 * Used for loading states to provide visual feedback while content is being fetched.
 */

/**
 * Shimmer animation configuration
 */
object ShimmerDefaults {
    /** Default shimmer animation duration in milliseconds */
    const val AnimationDuration = 1200

    /** Default shimmer easing */
    val AnimationEasing = LinearEasing

    /** Default shimmer brush for light theme */
    fun defaultShimmerBrush(
        shimmerWidth: Float = 500f,
        isDark: Boolean = false
    ): Brush {
        return Brush.linearGradient(
            colors = if (isDark) {
                listOf(
                    Color.White.copy(alpha = 0.14f),
                    Color.White.copy(alpha = 0.27f),
                    Color.White.copy(alpha = 0.14f)
                )
            } else {
                listOf(
                    Color.Black.copy(alpha = 0.1f),
                    Color.Black.copy(alpha = 0.2f),
                    Color.Black.copy(alpha = 0.1f)
                )
            },
            start = Offset.Zero,
            end = Offset(x = shimmerWidth, y = 0f)
        )
    }
}

/**
 * Creates a shimmer brush with animated gradient
 *
 * @param shimmerWidth Width of the shimmer effect
 * @param isDark Whether the theme is dark mode
 * @return Animated shimmer brush
 */
@Composable
fun rememberShimmerBrush(
    shimmerWidth: Float = 500f,
    isDark: Boolean = false
): Brush {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")

    // Animate the shimmer position
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = shimmerWidth * 2,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = ShimmerDefaults.AnimationDuration,
                easing = ShimmerDefaults.AnimationEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )

    // Calculate start and end offsets for the gradient
    val startOffset = Offset(x = offsetX - shimmerWidth, y = 0f)
    val endOffset = Offset(x = offsetX + shimmerWidth, y = 0f)

    return Brush.linearGradient(
        colors = if (isDark) {
            listOf(
                Color.White.copy(alpha = 0.14f),
                Color.White.copy(alpha = 0.27f),
                Color.White.copy(alpha = 0.14f)
            )
        } else {
            listOf(
                Color.Black.copy(alpha = 0.1f),
                Color.Black.copy(alpha = 0.2f),
                Color.Black.copy(alpha = 0.1f)
            )
        },
        start = startOffset,
        end = endOffset
    )
}

/**
 * Applies shimmer effect to a composable
 *
 * @param isDark Whether the theme is dark mode
 * @param shimmerWidth Width of the shimmer effect
 */
@Composable
fun Modifier.shimmer(
    isDark: Boolean = false,
    shimmerWidth: Float = 500f
): Modifier {
    val shimmerBrush = rememberShimmerBrush(shimmerWidth, isDark)

    return this.then(
        background(shimmerBrush)
    )
}

/**
 * Applies shimmer effect with custom color scheme
 *
 * @param baseColor Base color of the content
 * @param highlightColor Highlight color for the shimmer
 * @param shimmerWidth Width of the shimmer effect
 */
@Composable
fun Modifier.customShimmer(
    baseColor: Color,
    highlightColor: Color,
    shimmerWidth: Float = 500f
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")

    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = shimmerWidth * 2,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = ShimmerDefaults.AnimationDuration,
                easing = ShimmerDefaults.AnimationEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            baseColor,
            highlightColor,
            baseColor
        ),
        start = Offset(x = offsetX - shimmerWidth, y = 0f),
        end = Offset(x = offsetX + shimmerWidth, y = 0f)
    )

    return this.then(
        background(shimmerBrush)
    )
}