package com.greenrobotdev.linklibrary.design.theme

import androidx.compose.material3.Experimentalexpressive.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Material 3 Expressive Theme Configuration
 *
 * This theme enables Material 3 Expressive features including:
 * - Spring-based motion physics
 * - Enhanced ripple effects
 * - Shape morphing animations
 * - Dynamic color schemes
 * - Expressive component behaviors
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MaterialExpressiveAppTheme(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    MaterialExpressiveTheme(
        modifier = modifier,
        content = content
    )
}

/**
 * Surface with Material 3 Expressive features enabled
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
