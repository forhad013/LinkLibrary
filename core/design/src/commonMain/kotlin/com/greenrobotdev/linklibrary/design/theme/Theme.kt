package com.greenrobotdev.linklibrary.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

/**
 * Vibrant Citadel Dark - Material Design 3 Expressive Theme
 *
 * Main theme composable providing complete Material Design 3 styling
 * with Vibrant Citadel Dark colors, expressive typography, and shapes.
 * Optimized for low-light environments with atmospheric vitality and
 * tactile depth through tonal stacking.
 */
@Composable
fun LinkLibraryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        },
        typography = Typography(),
        content = content
    )
}
