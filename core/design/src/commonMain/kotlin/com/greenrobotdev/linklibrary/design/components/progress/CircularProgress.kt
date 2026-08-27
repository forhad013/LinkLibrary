package com.greenrobotdev.linklibrary.design.components.progress

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Circular progress indicator with Material 3 styling
 *
 * Shows progress in a circular form. Use determinate form when progress can be
 * calculated, or indeterminate form when progress is unknown.
 *
 * @param progress Current progress value (0.0 to 1.0). Null for indeterminate.
 * @param modifier Modifier to be applied to the progress indicator
 * @param color Color of the progress indicator. Defaults to theme's primary color
 * @param strokeWidth Width of the progress stroke. Defaults to 4dp
 * @param size Size of the circular indicator. Defaults to 40dp
 */
@Composable
fun CircularProgress(
    progress: Float? = null,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 4.dp,
    size: Dp = 40.dp
) {
    if (progress != null) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = modifier.size(size),
            color = color,
            strokeWidth = strokeWidth
        )
    } else {
        CircularProgressIndicator(
            modifier = modifier.size(size),
            color = color,
            strokeWidth = strokeWidth
        )
    }
}

/**
 * Circular progress indicator with determinate value
 *
 * @param progress Current progress value (0.0 to 1.0)
 * @param modifier Modifier to be applied to the progress indicator
 * @param color Color of the progress indicator. Defaults to theme's primary color
 * @param strokeWidth Width of the progress stroke. Defaults to 4dp
 * @param size Size of the circular indicator. Defaults to 40dp
 */
@Composable
fun DeterminateCircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 4.dp,
    size: Dp = 40.dp
) {
    val clampedProgress = progress.coerceIn(0f, 1f)

    CircularProgressIndicator(
        progress = { clampedProgress },
        modifier = modifier.size(size),
        color = color,
        strokeWidth = strokeWidth
    )
}

/**
 * Small circular progress indicator for compact spaces
 *
 * @param modifier Modifier to be applied to the progress indicator
 * @param color Color of the progress indicator. Defaults to theme's primary color
 */
@Composable
fun SmallCircularProgress(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    CircularProgressIndicator(
        modifier = modifier.size(16.dp),
        color = color,
        strokeWidth = 2.dp
    )
}

/**
 * Large circular progress indicator for emphasis
 *
 * @param modifier Modifier to be applied to the progress indicator
 * @param color Color of the progress indicator. Defaults to theme's primary color
 */
@Composable
fun LargeCircularProgress(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    CircularProgressIndicator(
        modifier = modifier.size(56.dp),
        color = color,
        strokeWidth = 5.dp
    )
}