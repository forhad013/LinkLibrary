package com.greenrobotdev.linklibrary.di

import org.koin.dsl.module

/**
 * Common Koin module for shared dependencies.
 *
 * Note: Stitch module integration removed for WASM prototype
 * Will be re-integrated for production with proper WASM support
 */
val appModule = module {
    // Core dependencies will be added here
    // Stitch module temporarily excluded for WASM prototype
}
