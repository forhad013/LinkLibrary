package com.greenrobotdev.linklibrary.design.tokens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

/**
 * Android implementation of rememberWindowSizeClass
 */
@Composable
actual fun rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    return remember(configuration.screenWidthDp) {
        when {
            configuration.screenWidthDp < BreakpointsTokens.Medium.value -> WindowSizeClass.Compact
            configuration.screenWidthDp < BreakpointsTokens.Expanded.value -> WindowSizeClass.Medium
            else -> WindowSizeClass.Expanded
        }
    }
}
