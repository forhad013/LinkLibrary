package com.greenrobotdev.linklibrary.di

import org.koin.dsl.module

/**
 * Common Koin module for shared dependencies.
 *
 * Includes:
 * - Database module (via import) which provides the LinkRepository
 * - Stitch module for Google Stitch AI integration
 * - Other shared app dependencies
 *
 * Android-specific dependencies (like DatabaseBuilder) are provided by androidAppModule.
 */
val appModule = module {
    // Include Stitch module for AI capabilities
    includes(stitchModule)
}
