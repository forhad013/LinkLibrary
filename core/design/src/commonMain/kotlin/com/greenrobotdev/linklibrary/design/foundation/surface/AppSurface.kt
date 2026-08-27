package com.greenrobotdev.linklibrary.design.foundation.surface

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.tokens.ElevationTokens

/**
 * Foundation surface component with automatic theme integration
 *
 * This wrapper provides a simplified surface component that automatically integrates
 * with the Material 3 theme while offering sensible defaults for common use cases.
 *
 * @param modifier Modifier to be applied to the surface
 * @param shape Shape to be applied to the surface. Defaults to theme's medium shape
 * @param color Background color. Defaults to theme's surface color
 * @param contentColor Content color. Defaults to theme's onSurface color
 * @param tonalElevation Tonal elevation level. Defaults to Level1 (1dp)
 * @param shadowElevation Shadow elevation level. Defaults to Level1 (1dp)
 * @param border Border to be applied to the surface
 * @param content Content to be displayed on the surface
 */
@Composable
fun AppSurface(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    tonalElevation: Dp = ElevationTokens.Level1,
    shadowElevation: Dp = ElevationTokens.Level1,
    border: androidx.compose.foundation.BorderStroke? = null,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        border = border,
        content = content
    )
}

/**
 * Convenience composable for card-style surfaces with elevated appearance
 */
@Composable
fun AppCardSurface(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (onClick != null) {
        androidx.compose.material3.Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape
        ) {
            content()
        }
    } else {
        androidx.compose.material3.Card(
            modifier = modifier,
            shape = shape
        ) {
            content()
        }
    }
}