package com.greenrobotdev.linklibrary.di

import org.koin.dsl.module

/**
 * Common Koin module for shared dependencies.
 *
 * Imports database module which provides the LinkRepository.
 * Android-specific dependencies (like DatabaseBuilder) are provided by androidAppModule.
 */
val appModule = module {
}
