package com.greenrobotdev.linklibrary.di

import com.greenrobotdev.linklibrary.bookmarks.service.MetadataFetchService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

/**
 * Common Koin module for shared dependencies.
 *
 * Note: Stitch module integration removed for WASM prototype
 * Will be re-integrated for production with proper WASM support
 */
val appModule = module {
    // HTTP Client for metadata fetching (platform-specific implementation)
    single<HttpClient> { createHttpClient() }

    // Metadata fetch service for auto-fetch functionality
    single { MetadataFetchService(get()) }

    // Stitch module temporarily excluded for WASM prototype
}

/**
 * Platform-specific HTTP client creation
 * Each platform provides its own implementation with appropriate engine
 */
expect fun createHttpClient(): HttpClient
