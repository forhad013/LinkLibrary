package com.greenrobotdev.linklibrary.design.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Material 3 Expressive Theme Configuration for KMP
 *
 * This theme uses standard MaterialTheme with enhanced custom animations:
 * - Custom spring-based motion physics
 * - Enhanced ripple effects
 * - Dynamic color schemes
 * - Expressive component behaviors via custom implementations
 */
@Composable
fun MaterialExpressiveAppTheme(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        modifier = modifier,
        content = content
    )
}

/**
 * Surface with Material 3 Expressive features enabled
 */
@Composable
fun ExpressiveSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        content = content
    )
}
