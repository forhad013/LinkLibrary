package com.greenrobotdev.linklibrary.design.tokens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * WASM implementation of rememberWindowSizeClass
 * Note: This is a simplified implementation. For WASM apps,
 * you would typically access window.innerWidth/innerHeight through JavaScript interop.
 */
@Composable
actual fun rememberWindowSizeClass(): WindowSizeClass {
    // For WASM, we default to Compact but could be enhanced with JS interop
    // to get actual browser window dimensions
    return remember {
        WindowSizeClass.Compact
    }
}
