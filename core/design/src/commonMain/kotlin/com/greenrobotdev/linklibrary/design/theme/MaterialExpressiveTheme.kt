package com.greenrobotdev.linklibrary.design.theme

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Material 3 Expressive Theme Configuration for KMP
 *
 * This theme enables Material 3 Expressive features using the official KMP Expressive APIs:
 * - Spring-based motion physics via MaterialExpressiveTheme
 * - Enhanced ripple effects
 * - Dynamic color schemes
 * - Expressive component behaviors
 *
 * Based on Compose Multiplatform 1.9.0-alpha04 with Expressive support
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MaterialExpressiveAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        content = content
    )
}

/**
 * Standard MaterialTheme wrapper for compatibility
 */
@Composable
fun MaterialAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

/**
 * Surface with Material 3 standard features
 */
@Composable
fun ExpressiveSurface(
    content: @Composable () -> Unit
) {
    Surface(
        content = content
    )
}

/**
 * Material 3 Expressive Theme Configuration for KMP
 *
 * This theme enables Material 3 Expressive features using the official KMP Expressive APIs:
 * - Spring-based motion physics via motionScheme
 * - Enhanced ripple effects
 * - Dynamic color schemes
 * - Expressive component behaviors
 *
 * Based on Compose Multiplatform 1.9.0-alpha04 with Expressive support
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MaterialExpressiveAppTheme(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        motionScheme = MaterialTheme.motionScheme, // Use default expressive motion
        content = content
    )
}

/**
 * Standard MaterialTheme wrapper for compatibility
 */
@Composable
fun MaterialAppTheme(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

/**
 * Surface with Material 3 standard features
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
