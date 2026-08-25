package com.greenrobotdev.linklibrary

import androidx.compose.ui.window.CanvasBasedWindow
import com.greenrobotdev.linklibrary.screens.root.RootScreen
import com.greenrobotdev.linklibrary.design.theme.LinkLibraryTheme
import org.koin.core.context.startKoin
import com.greenrobotdev.linklibrary.di.appModule

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
        LinkLibraryTheme(darkTheme = true) {
            RootScreen(
                sharedContent = null, // Will receive from Chrome extension in the future
                onSharedContentHandled = {}
            )
        }
    }
}