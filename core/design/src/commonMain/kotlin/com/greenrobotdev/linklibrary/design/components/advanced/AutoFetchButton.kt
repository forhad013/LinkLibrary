package com.greenrobotdev.linklibrary.design.components.advanced

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * AutoFetchButton - Smart metadata fetching with animated loading states
 *
 * A specialized button component for fetching link metadata with comprehensive
 * state management, Material 3 Expressive animations, and visual feedback.
 *
 * Features:
 * - Idle/Loading/Success/Error states
 * - Animated state transitions
 * - Progress indicator during fetch
 * - Icon animations
 * - Color state transitions
 * - Accessibility support
 */
@Composable
fun AutoFetchButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fetchState: FetchState = FetchState.Idle,
    text: String = "Auto-Fetch Metadata"
) {
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = when (fetchState) {
            FetchState.Idle -> MaterialTheme.colorScheme.primary
            FetchState.Loading -> MaterialTheme.colorScheme.secondary
            FetchState.Success -> MaterialTheme.colorScheme.tertiary
            FetchState.Error -> MaterialTheme.colorScheme.error
        },
        contentColor = when (fetchState) {
            FetchState.Error -> MaterialTheme.colorScheme.onError
            else -> MaterialTheme.colorScheme.onPrimary
        }
    )

    // Animation specs
    val scale by animateFloatAsState(
        targetValue = when (fetchState) {
            FetchState.Idle -> 1f
            else -> 0.98f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_scale"
    )

    val elevation by animateDpAsState(
        targetValue = when (fetchState) {
            FetchState.Idle -> 0.dp
            FetchState.Loading -> 2.dp
            FetchState.Success -> 4.dp
            FetchState.Error -> 2.dp
        },
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "button_elevation"
    )

    Button(
        onClick = {
            when (fetchState) {
                FetchState.Idle, FetchState.Error -> onClick()
                else -> {}
            }
        },
        modifier = modifier.height(48.dp),
        enabled = enabled && fetchState != FetchState.Loading,
        colors = buttonColors,
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            pressedElevation = elevation + 2.dp
        )
    ) {
        ButtonContent(
            fetchState = fetchState,
            text = text,
            scale = scale
        )
    }
}

@Composable
private fun ButtonContent(
    fetchState: FetchState,
    text: String,
    scale: Float
) {
    val iconRotation by animateFloatAsState(
        targetValue = when (fetchState) {
            FetchState.Loading -> 360f
            else -> 0f
        },
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "icon_rotation"
    )

    val iconAlpha by animateFloatAsState(
        targetValue = when (fetchState) {
            FetchState.Loading -> 1f
            FetchState.Success -> 1f
            FetchState.Error -> 1f
            FetchState.Idle -> 1f
        },
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "icon_alpha"
    )

    Row(
        modifier = Modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (fetchState) {
            FetchState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Fetching...",
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            FetchState.Success -> {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Fetched!",
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            FetchState.Error -> {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onError
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Failed - Retry",
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            FetchState.Idle -> {
                Icon(
                    imageVector = Icons.Default.CloudDownload,
                    contentDescription = "Auto-fetch",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * State for the AutoFetchButton
 */
sealed class FetchState {
    /** Initial state before any action */
    data object Idle : FetchState()

    /** Currently fetching metadata */
    data object Loading : FetchState()

    /** Successfully fetched metadata */
    data object Success : FetchState()

    /** Failed to fetch metadata */
    data object Error : FetchState()
}

/**
 * Outlined variant of AutoFetchButton for secondary actions
 */
@Composable
fun AutoFetchButtonOutlined(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fetchState: FetchState = FetchState.Idle,
    text: String = "Auto-Fetch Metadata"
) {
    val buttonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = when (fetchState) {
            FetchState.Idle -> MaterialTheme.colorScheme.primary
            FetchState.Loading -> MaterialTheme.colorScheme.secondary
            FetchState.Success -> Color(0xFF4CAF50)
            FetchState.Error -> Color(0xFFF44336)
        }
    )

    OutlinedButton(
        onClick = {
            when (fetchState) {
                FetchState.Idle, FetchState.Error -> onClick()
                else -> {}
            }
        },
        modifier = modifier.height(48.dp),
        enabled = enabled && fetchState != FetchState.Loading,
        colors = buttonColors,
        shape = RoundedCornerShape(12.dp)
    ) {
        ButtonContent(
            fetchState = fetchState,
            text = text,
            scale = 1f
        )
    }
}

/**
 * Text variant of AutoFetchButton for minimal UI
 */
@Composable
fun AutoFetchButtonText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fetchState: FetchState = FetchState.Idle,
    text: String = "Auto-Fetch"
) {
    TextButton(
        onClick = {
            when (fetchState) {
                FetchState.Idle, FetchState.Error -> onClick()
                else -> {}
            }
        },
        modifier = modifier,
        enabled = enabled && fetchState != FetchState.Loading,
        shape = RoundedCornerShape(8.dp)
    ) {
        when (fetchState) {
            FetchState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text)
            }
            FetchState.Success -> {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Fetched!")
            }
            FetchState.Error -> {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text)
            }
            FetchState.Idle -> {
                Icon(
                    imageVector = Icons.Default.CloudDownload,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text)
            }
        }
    }
}