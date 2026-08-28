package com.greenrobotdev.linklibrary.design.tokens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window

/**
 * JVM/Desktop implementation of rememberWindowSizeClass
 * Note: This is a simplified implementation. For desktop apps,
 * you would typically access the actual window dimensions.
 */
@Composable
actual fun rememberWindowSizeClass(): WindowSizeClass {
    // For desktop, we default to Expanded since most desktop screens are large
    // In a real desktop app, you would access the actual window size
    return remember {
        WindowSizeClass.Expanded
    }
}
