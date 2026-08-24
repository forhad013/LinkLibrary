package com.greenrobotdev.linklibrary.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

/**
 * Android-specific HTTP client implementation using OkHttp engine
 * Provides robust networking with proper timeouts and SSL configuration
 */
actual fun createHttpClient(): HttpClient {
    return HttpClient(OkHttp) {
        // Configure timeouts to prevent indefinite hangs
        engine {
            config {
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
                // Retry failed requests
                retryOnConnectionFailure(true)
            }
        }

        // Content negotiation for JSON parsing
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }

        // Logging for debugging
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