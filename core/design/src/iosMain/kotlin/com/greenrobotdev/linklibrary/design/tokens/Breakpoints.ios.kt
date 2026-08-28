package com.greenrobotdev.linklibrary.design.tokens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * iOS implementation of rememberWindowSizeClass
 * Note: This is a simplified implementation. For iOS apps,
 * you would typically access the actual screen dimensions through UIKit bindings.
 */
@Composable
actual fun rememberWindowSizeClass(): WindowSizeClass {
    // For iOS, we default to Compact since most iOS devices are phones
    // In a real iOS app, you would access the actual screen size
    return remember {
        WindowSizeClass.Compact
    }
}
