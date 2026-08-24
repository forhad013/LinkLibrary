package com.greenrobotdev.linklibrary.di

import com.greenrobotdev.linklibrary.data.MetadataFetchService
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
    // HTTP Client for metadata fetching
    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(Logging) {
                level = LogLevel.INFO
                logger = object : Logger {
                    override fun log(message: String) {
                        println("KTOR: $message")
                    }
                }
            }
        }
    }

    // Metadata fetch service for auto-fetch functionality
    single { MetadataFetchService(get()) }

    // Stitch module temporarily excluded for WASM prototype
}
