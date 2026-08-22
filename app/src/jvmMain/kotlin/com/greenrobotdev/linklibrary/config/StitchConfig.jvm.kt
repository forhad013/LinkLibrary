package com.greenrobotdev.linklibrary.config
import java.io.File
import java.io.FileInputStream
import java.util.Properties

/**
 * JVM implementation of StitchConfig
 * Reads API key from environment variables or .env file
 */
actual object StitchConfig {

    actual fun loadConfig(): ApiConfig? {
        // Try environment variables first
        val envApiKey = System.getenv("GOOGLE_STITCH_API_KEY")
        val envEndpoint = System.getenv("GOOGLE_STITCH_ENDPOINT")

        if (!envApiKey.isNullOrBlank()) {
            return ApiConfig(
                apiKey = envApiKey,
                endpoint = envEndpoint ?: "https://stitch.googleapis.com"
            )
        }

        // Try to read from .env file in project directory
        val envFile = File(".env")
        if (envFile.exists()) {
            try {
                val properties = Properties()
                FileInputStream(envFile).use { properties.load(it) }

                val fileApiKey = properties.getProperty("GOOGLE_STITCH_API_KEY")
                val fileEndpoint = properties.getProperty("GOOGLE_STITCH_ENDPOINT")

                if (!fileApiKey.isNullOrBlank()) {
                    return ApiConfig(
                        apiKey = fileApiKey,
                        endpoint = fileEndpoint ?: "https://stitch.googleapis.com"
                    )
                }
            } catch (e: Exception) {
                println("Warning: Failed to read .env file: ${e.message}")
            }
        }

        // Try user home directory .linklibrary/.env
        val userEnvFile = File(System.getProperty("user.home"), ".linklibrary/.env")
        if (userEnvFile.exists()) {
            try {
                val properties = Properties()
                FileInputStream(userEnvFile).use { properties.load(it) }

                val fileApiKey = properties.getProperty("GOOGLE_STITCH_API_KEY")
                val fileEndpoint = properties.getProperty("GOOGLE_STITCH_ENDPOINT")

                if (!fileApiKey.isNullOrBlank()) {
                    return ApiConfig(
                        apiKey = fileApiKey,
                        endpoint = fileEndpoint ?: "https://stitch.googleapis.com"
                    )
                }
            } catch (e: Exception) {
                println("Warning: Failed to read user .env file: ${e.message}")
            }
        }

        return null
    }

    actual fun getConfig(): ApiConfig {
        return loadConfig() ?: throw IllegalStateException(
            "Google Stitch API key not configured. " +
            "Set GOOGLE_STITCH_API_KEY environment variable or create a .env file " +
            "with GOOGLE_STITCH_API_KEY=your_api_key"
        )
    }
}
