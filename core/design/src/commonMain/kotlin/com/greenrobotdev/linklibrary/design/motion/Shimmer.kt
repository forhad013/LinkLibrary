package com.greenrobotdev.linklibrary.design.motion

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

/** One shimmer sweep, left to right. A steady loop, so a fixed linear tween, not a spring. */
private const val SHIMMER_MS = 1200

/**
 * Loading placeholder: a soft highlight sweeping across. Drawn in the draw phase (no recomposition
 * per frame); a flat placeholder when the user turned animations off.
 */
@Composable
fun Modifier.shimmer(): Modifier {
    val base = MaterialTheme.colorScheme.surfaceContainerHigh
    if (rememberReducedMotion()) return background(base)
    val highlight = MaterialTheme.colorScheme.surfaceContainerLowest
    val progress by rememberInfiniteTransition(label = "shimmer").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(SHIMMER_MS, easing = LinearEasing)),
        label = "shimmer sweep",
    )
    return drawBehind {
        // The highlight band travels from fully off the left edge to fully off the right
        val band = size.width
        val x = -band + progress * (size.width + band * 2)
        drawRect(base)
        drawRect(Brush.linearGradient(listOf(base, highlight, base), start = Offset(x, 0f), end = Offset(x + band, size.height)))
    }
}
