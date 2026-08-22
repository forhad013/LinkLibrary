package com.greenrobotdev.linklibrary.config

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Configuration for Google Stitch API
 */
data class ApiConfig(
    val apiKey: String,
    val endpoint: String = "https://stitch.googleapis.com"
)

/**
 * Platform-specific configuration loader
 */
expect object StitchConfig {
    /**
     * Loads the Stitch API configuration from platform-specific sources
     * @return ApiConfig with API key and endpoint, or null if not configured
     */
    fun loadConfig(): ApiConfig?

    /**
     * Gets the current configuration or throws if not set
     */
    fun getConfig(): ApiConfig
}

/**
 * Stitch API client configuration
 */
@OptIn(ExperimentalUuidApi::class)
data class StitchClientConfig(
    val apiKey: String,
    val endpoint: String = "https://stitch.googleapis.com",
    val clientId: String = Uuid.random().toString(),
    val timeoutMs: Long = 30000L
)
