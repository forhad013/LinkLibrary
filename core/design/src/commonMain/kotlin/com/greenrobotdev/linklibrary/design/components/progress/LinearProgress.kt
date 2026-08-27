package com.greenrobotdev.linklibrary.design.components.progress

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Linear progress indicator with Material 3 styling
 *
 * Shows progress along a line. Use determinate form when progress can be
 * calculated, or indeterminate form when progress is unknown.
 *
 * @param progress Current progress value (0.0 to 1.0). Null for indeterminate.
 * @param modifier Modifier to be applied to the progress indicator
 * @param color Color of the progress indicator. Defaults to theme's primary color
 * @param trackColor Color of the track/background. Defaults to theme's surface container highest color
 * @param height Height of the progress indicator. Defaults to 4dp
 */
@Composable
fun LinearProgress(
    progress: Float? = null,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    height: Dp = 4.dp
) {
    if (progress != null) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = modifier.height(height),
            color = color,
            trackColor = trackColor
        )
    } else {
        LinearProgressIndicator(
            modifier = modifier.height(height),
            color = color,
            trackColor = trackColor
        )
    }
}

/**
 * Linear progress indicator with determinate value
 *
 * @param progress Current progress value (0.0 to 1.0)
 * @param modifier Modifier to be applied to the progress indicator
 * @param color Color of the progress indicator. Defaults to theme's primary color
 * @param trackColor Color of the track/background. Defaults to theme's surface container highest color
 * @param height Height of the progress indicator. Defaults to 4dp
 */
@Composable
fun DeterminateLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    height: Dp = 4.dp
) {
    val clampedProgress = progress.coerceIn(0f, 1f)

    LinearProgressIndicator(
        progress = { clampedProgress },
        modifier = modifier.height(height),
        color = color,
        trackColor = trackColor
    )
}

/**
 * Linear progress indicator with percentage label
 *
 * @param progress Current progress value (0.0 to 1.0)
 * @param modifier Modifier to be applied to the progress indicator
 * @param showPercentage Whether to show percentage text above progress
 * @param color Color of the progress indicator. Defaults to theme's primary color
 * @param trackColor Color of the track/background. Defaults to theme's surface container highest color
 * @param height Height of the progress indicator. Defaults to 4dp
 */
@Composable
fun LinearProgressWithPercentage(
    progress: Float,
    modifier: Modifier = Modifier,
    showPercentage: Boolean = true,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    height: Dp = 4.dp
) {
    val clampedProgress = progress.coerceIn(0f, 1f)
    val percentage = (clampedProgress * 100).toInt()

    androidx.compose.foundation.layout.Column(modifier = modifier) {
        if (showPercentage) {
            androidx.compose.material3.Text(
                text = "$percentage%",
                style = MaterialTheme.typography.bodySmall,
                color = color,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        LinearProgressIndicator(
            progress = { clampedProgress },
            color = color,
            trackColor = trackColor
        )
    }
}