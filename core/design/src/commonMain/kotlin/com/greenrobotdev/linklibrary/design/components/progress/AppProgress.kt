package com.greenrobotdev.linklibrary.design.components.progress

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * AppProgress - Standardized progress indicator component for consistent appearance across all form screens
 *
 * Provides a unified progress indicator implementation that can be used consistently across:
 * - Add Link screen
 * - Add Tag screen
 * - Add Collection screen
 * - Any other form-based screens
 *
 * Design Philosophy:
 * - Simple, consistent appearance across all screens
 * - Material 3 Expressive styling with proper theming
 * - Standard circular progress indicator functionality
 * - Consistent sizing and stroke width
 *
 * Features:
 * - Consistent Material 3 CircularProgressIndicator styling
 * - Proper dark theme support with correct contrast
 * - Standard sizes for different use cases
 * - Primary color scheme for consistency
 */

/**
 * AppCircularProgress - Standard circular progress indicator
 *
 * Usage: Standard loading states, form submission loading, data fetching
 * Style: Primary color with medium size (20dp) and standard stroke width (2dp)
 *
 * @param modifier Optional modifier for layout
 * @param size Size of the progress indicator (default: 20dp)
 * @param strokeWidth Width of the progress stroke (default: 2dp)
 */
@Composable
fun AppCircularProgress(
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    strokeWidth: Dp = 2.dp
) {
    CircularProgressIndicator(
        modifier = modifier.size(size),
        strokeWidth = strokeWidth,
        color = MaterialTheme.colorScheme.primary
    )
}

/**
 * AppCircularProgressLarge - Large circular progress indicator
 *
 * Usage: Full screen loading, major operations, prominent loading states
 * Style: Primary color with large size (56dp) and thicker stroke width (5dp)
 *
 * @param modifier Optional modifier for layout
 */
@Composable
fun AppCircularProgressLarge(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier.size(56.dp),
        strokeWidth = 5.dp,
        color = MaterialTheme.colorScheme.primary
    )
}

/**
 * AppCircularProgressSmall - Small circular progress indicator
 *
 * Usage: Inline loading, compact loading states, button loading
 * Style: Primary color with small size (16dp) and thin stroke width (2dp)
 *
 * @param modifier Optional modifier for layout
 */
@Composable
fun AppCircularProgressSmall(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier.size(16.dp),
        strokeWidth = 2.dp,
        color = MaterialTheme.colorScheme.primary
    )
}