package com.greenrobotdev.linklibrary.design.components.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Enhanced Circular Progress Indicators - Specialized progress components for different contexts
 *
 * Provides a comprehensive set of circular progress indicator components optimized for
 * specific use cases throughout the application. Each variant includes proper styling,
 * states, and Material 3 Expressive design integration.
 *
 * Design Philosophy:
 * - Consistent Material 3 Expressive styling
 * - Context-appropriate sizes and stroke widths
 * - Enhanced state feedback (loading, success, error)
 * - Flexible layout and positioning
 * - Accessible and informative progress display
 *
 * Categories:
 * - Size Variants: Extra small, small, medium, large, extra large
 * - State Variants: Loading, success, error, warning
 * - Content Variants: With text, with icon, with percentage
 * - Special: Full screen, inline, overlay, determinate/indeterminate
 */

// ==================== SIZE VARIANT PROGRESS INDICATORS ====================

/**
 * ExtraSmallCircularProgress - Minimal circular progress
 *
 * Usage: Buttons, small icons, inline loading states
 * Features: 16dp size, thin stroke for minimal appearance
 *
 * @param modifier Optional modifier
 * @param color Progress color
 */
@Composable
fun ExtraSmallCircularProgress(
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
 * CompactCircularProgress - Compact circular progress
 *
 * Usage: List items, card actions, compact loading states
 * Features: 24dp size, balanced stroke width
 *
 * @param modifier Optional modifier
 * @param color Progress color
 */
@Composable
fun CompactCircularProgress(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    CircularProgressIndicator(
        modifier = modifier.size(24.dp),
        color = color,
        strokeWidth = 2.5.dp
    )
}

/**
 * MediumCircularProgress - Standard circular progress
 *
 * Usage: Dialogs, forms, standard loading states
 * Features: 40dp size, standard stroke width
 *
 * @param modifier Optional modifier
 * @param color Progress color
 */
@Composable
fun MediumCircularProgress(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    CircularProgressIndicator(
        modifier = modifier.size(40.dp),
        color = color,
        strokeWidth = 4.dp
    )
}

/**
 * ProminentCircularProgress - Prominent circular progress
 *
 * Usage: Full screen loading, emphasis states, main content loading
 * Features: 56dp size, thick stroke for visibility
 *
 * @param modifier Optional modifier
 * @param color Progress color
 */
@Composable
fun ProminentCircularProgress(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    CircularProgressIndicator(
        modifier = modifier.size(56.dp),
        color = color,
        strokeWidth = 5.dp
    )
}

/**
 * ExtraLargeCircularProgress - Maximum emphasis circular progress
 *
 * Usage: Hero loading states, full screen emphasis, major operations
 * Features: 72dp size, extra thick stroke
 *
 * @param modifier Optional modifier
 * @param color Progress color
 */
@Composable
fun ExtraLargeCircularProgress(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    CircularProgressIndicator(
        modifier = modifier.size(72.dp),
        color = color,
        strokeWidth = 6.dp
    )
}

// ==================== STATE VARIANT PROGRESS INDICATORS ====================

/**
 * LoadingCircularProgress - Standard loading state indicator
 *
 * Usage: Standard loading states, data fetching, processing
 * Features: Indeterminate progress with primary color
 *
 * @param modifier Optional modifier
 * @param size Size of indicator (default: medium)
 */
@Composable
fun LoadingCircularProgress(
    modifier: Modifier = Modifier,
    size: ProgressSize = ProgressSize.Medium
) {
    val (indicatorSize, strokeWidth) = when (size) {
        ProgressSize.ExtraSmall -> 16.dp to 2.dp
        ProgressSize.Small -> 24.dp to 2.5.dp
        ProgressSize.Medium -> 40.dp to 4.dp
        ProgressSize.Large -> 56.dp to 5.dp
        ProgressSize.ExtraLarge -> 72.dp to 6.dp
    }

    CircularProgressIndicator(
        modifier = modifier.size(indicatorSize),
        color = MaterialTheme.colorScheme.primary,
        strokeWidth = strokeWidth
    )
}

/**
 * SuccessCircularProgress - Completed state with success indicator
 *
 * Usage: Completion states, successful operations, finished downloads
 * Features: Checkmark icon with success color
 *
 * @param modifier Optional modifier
 * @param size Size of indicator (default: medium)
 */
@Composable
fun SuccessCircularProgress(
    modifier: Modifier = Modifier,
    size: ProgressSize = ProgressSize.Medium
) {
    val indicatorSize = when (size) {
        ProgressSize.ExtraSmall -> 16.dp
        ProgressSize.Small -> 24.dp
        ProgressSize.Medium -> 40.dp
        ProgressSize.Large -> 56.dp
        ProgressSize.ExtraLarge -> 72.dp
    }

    Surface(
        modifier = modifier.size(indicatorSize),
        shape = androidx.compose.foundation.shape.CircleShape,
        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Success",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(indicatorSize * 0.5f)
            )
        }
    }
}

/**
 * ErrorCircularProgress - Error state with error indicator
 *
 * Usage: Failed operations, error states, unsuccessful attempts
 * Features: Error icon with error color
 *
 * @param modifier Optional modifier
 * @param size Size of indicator (default: medium)
 */
@Composable
fun ErrorCircularProgress(
    modifier: Modifier = Modifier,
    size: ProgressSize = ProgressSize.Medium
) {
    val indicatorSize = when (size) {
        ProgressSize.ExtraSmall -> 16.dp
        ProgressSize.Small -> 24.dp
        ProgressSize.Medium -> 40.dp
        ProgressSize.Large -> 56.dp
        ProgressSize.ExtraLarge -> 72.dp
    }

    Surface(
        modifier = modifier.size(indicatorSize),
        shape = androidx.compose.foundation.shape.CircleShape,
        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(indicatorSize * 0.5f)
            )
        }
    }
}

// ==================== CONTENT VARIANT PROGRESS INDICATORS ====================

/**
 * CircularProgressWithText - Progress indicator with accompanying text
 *
 * Usage: Loading operations with status text, file uploads with progress
 * Features: Circular progress with label text below
 *
 * @param text Text to display below progress indicator
 * @param modifier Optional modifier
 * @param progress Optional progress value (0.0 to 1.0)
 * @param color Progress color
 */
@Composable
fun CircularProgressWithText(
    text: String,
    modifier: Modifier = Modifier,
    progress: Float? = null,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (progress != null) {
            CircularProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.size(40.dp),
                color = color,
                strokeWidth = 4.dp
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = color,
                strokeWidth = 4.dp
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * CircularProgressWithPercentage - Progress indicator with percentage display
 *
 * Usage: Downloads with percentage, uploads, progress operations
 * Features: Circular progress with centered percentage text
 *
 * @param progress Progress value (0.0 to 1.0)
 * @param modifier Optional modifier
 * @param color Progress color
 * @param size Size of indicator (default: medium)
 */
@Composable
fun CircularProgressWithPercentage(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    size: ProgressSize = ProgressSize.Medium
) {
    val indicatorSize = when (size) {
        ProgressSize.ExtraSmall -> 16.dp
        ProgressSize.Small -> 24.dp
        ProgressSize.Medium -> 40.dp
        ProgressSize.Large -> 56.dp
        ProgressSize.ExtraLarge -> 72.dp
    }

    val strokeWidth = when (size) {
        ProgressSize.ExtraSmall -> 2.dp
        ProgressSize.Small -> 2.5.dp
        ProgressSize.Medium -> 4.dp
        ProgressSize.Large -> 5.dp
        ProgressSize.ExtraLarge -> 6.dp
    }

    Box(
        modifier = modifier.size(indicatorSize),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.size(indicatorSize),
            color = color,
            strokeWidth = strokeWidth
        )

        Text(
            text = "${(progress * 100).toInt()}%",
            style = when (size) {
                ProgressSize.ExtraSmall, ProgressSize.Small -> MaterialTheme.typography.labelSmall
                ProgressSize.Medium -> MaterialTheme.typography.bodyMedium
                ProgressSize.Large, ProgressSize.ExtraLarge -> MaterialTheme.typography.bodyLarge
            },
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * CircularProgressWithIcon - Progress indicator with icon overlay
 *
 * Usage: Loading states with icon, branded loading, contextual loading
 * Features: Circular progress with centered icon
 *
 * @param icon Icon to display in center
 * @param modifier Optional modifier
 * @param progress Optional progress value (0.0 to 1.0)
 * @param color Progress color
 * @param iconTint Tint color for icon
 */
@Composable
fun CircularProgressWithIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    progress: Float? = null,
    color: Color = MaterialTheme.colorScheme.primary,
    iconTint: Color = MaterialTheme.colorScheme.onPrimary
) {
    Box(
        modifier = modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        if (progress != null) {
            CircularProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.size(48.dp),
                color = color,
                strokeWidth = 4.dp
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = color,
                strokeWidth = 4.dp
            )
        }

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ==================== SPECIAL PROGRESS INDICATORS ====================

/**
 * FullScreenCircularProgress - Full screen loading overlay
 *
 * Usage: Full screen loading states, app initialization, major operations
 * Features: Full screen overlay with centered progress
 *
 * @param modifier Optional modifier
 * @param text Optional loading message
 * @param color Progress color
 */
@Composable
fun FullScreenCircularProgress(
    modifier: Modifier = Modifier,
    text: String? = null,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(56.dp),
                        color = color,
                        strokeWidth = 5.dp
                    )

                    if (text != null) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

/**
 * InlineCircularProgress - Inline progress indicator for row layouts
 *
 * Usage: List items with loading, inline operations, compact loading states
 * Features: Horizontal layout with progress and text
 *
 * @param text Text to display next to progress
 * @param modifier Optional modifier
 * @param progress Optional progress value (0.0 to 1.0)
 * @param color Progress color
 */
@Composable
fun InlineCircularProgress(
    text: String,
    modifier: Modifier = Modifier,
    progress: Float? = null,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (progress != null) {
            CircularProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.size(24.dp),
                color = color,
                strokeWidth = 2.5.dp
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = color,
                strokeWidth = 2.5.dp
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * OverlayCircularProgress - Progress indicator with overlay background
 *
 * Usage: Overlay on content, loading overlays, partial screen loading
 * Features: Semi-transparent background with centered progress
 *
 * @param modifier Optional modifier
 * @param text Optional loading message
 * @param color Progress color
 */
@Composable
fun OverlayCircularProgress(
    modifier: Modifier = Modifier,
    text: String? = null,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = color,
                        strokeWidth = 4.dp
                    )

                    if (text != null) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

/**
 * DeterminateLinearProgressWithCircular - Linear progress with circular percentage indicator
 *
 * Usage: Downloads with progress, uploads, multi-stage operations
 * Features: Linear progress bar with circular percentage indicator
 *
 * @param progress Progress value (0.0 to 1.0)
 * @param modifier Optional modifier
 * @param color Progress color
 */
@Composable
fun DeterminateLinearProgressWithCircular(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.weight(1f),
            color = color,
        )

        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.size(32.dp),
                color = color,
                strokeWidth = 3.dp
            )

            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ==================== ENUM FOR SIZE MANAGEMENT ====================

/**
 * Size options for progress indicators
 */
enum class ProgressSize {
    ExtraSmall,
    Small,
    Medium,
    Large,
    ExtraLarge
}