package com.greenrobotdev.lordofthelinks

import androidx.compose.ui.window.CanvasBasedWindow
import com.greenrobotdev.lordofthelinks.screens.root.RootScreen
import com.greenrobotdev.lordofthelinks.design.theme.LinkLibraryTheme
import org.koin.core.context.startKoin
import com.greenrobotdev.lordofthelinks.di.appModule

/**
 * Main entry point for the WASM (WebAssembly) target.
 *
 * This function initializes the app for web browsers using Compose for Web.
 * It sets up:
 * 1. Koin dependency injection
 * 2. Canvas-based rendering context
 * 3. Material 3 theme
 * 4. Main navigation/RootScreen
 */
fun main() {
    // Initialize Koin for WASM (simplified for prototype)
    startKoin {
        modules(appModule)
    }

    CanvasBasedWindow("Link Library") {
        LinkLibraryTheme {
            RootScreen(
                sharedContent = null, // Will receive from Chrome extension in the future
                onSharedContentHandled = {}
            )
        }
    }
}