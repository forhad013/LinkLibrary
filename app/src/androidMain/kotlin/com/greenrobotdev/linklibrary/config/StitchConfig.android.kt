package com.greenrobotdev.linklibrary.config

/**
 * Android implementation of StitchConfig
 * Reads API key from BuildConfig or system environment variable
 */
actual object StitchConfig {

    actual fun loadConfig(): ApiConfig? {
        // Try BuildConfig first (set via gradle)
        val apiKey = getConfigValue("GOOGLE_STITCH_API_KEY")
        val endpoint = getConfigValue("GOOGLE_STITCH_ENDPOINT") ?: "https://stitch.googleapis.com"

        return if (!apiKey.isNullOrBlank()) {
            ApiConfig(
                apiKey = apiKey,
                endpoint = endpoint
            )
        } else {
            null
        }
    }

    actual fun getConfig(): ApiConfig {
        return loadConfig() ?: throw IllegalStateException(
            "Google Stitch API key not configured. " +
            "Set GOOGLE_STITCH_API_KEY environment variable or BuildConfig field."
        )
    }

    private fun getConfigValue(key: String): String? {
        // Try system environment first
        val envValue = System.getenv(key)
        if (!envValue.isNullOrBlank()) {
            return envValue
        }

        // For Android: you would typically set this in build.gradle via BuildConfig
        // This is a fallback mechanism for development
        val properties = javaClass.getResourceAsStream("/config.properties")?.use { stream ->
            java.util.Properties().apply { load(stream) }
        }
        return properties?.getProperty(key)
    }
}
