package com.greenrobotdev.linklibrary.data.stitch

import com.greenrobotdev.linklibrary.config.StitchClientConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * HTTP client for Google Stitch API
 */
class StitchApiClient(
    private val config: StitchClientConfig
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
    }

    private val client = HttpClient {
        expectSuccess = false // Handle errors manually

        defaultRequest {
            url(config.endpoint)
            header("X-Goog-Api-Key", config.apiKey)
            header("Content-Type", "application/json")
            header("Accept", "application/json")
//            timeout {
//                requestTimeoutMillis = config.timeoutMs
//            }
        }

        install(ContentNegotiation) {
            json(json)
        }

        // Retry logic for transient failures
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            exponentialDelay()
        }

        // Logging (optional, enable for debugging)
        // install(Logging) {
        //     level = LogLevel.INFO
        // }
    }

    /**
     * Generate content using Stitch AI
     */
    fun generateContent(request: StitchContentRequest): Flow<Result<StitchContentResponse>> = flow {
        try {
            val response = client.post {
                url("/v1/stitch/models/${request.model}:generateContent")
                setBody(Json.encodeToString(request))
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val contentResponse = response.body<StitchContentResponse>()
                    emit(Result.success(contentResponse))
                }
                else -> {
                    val errorResponse = response.body<StitchErrorResponse>()
                    emit(Result.failure(
                        StitchApiException(
                            code = errorResponse.error.code,
                            message = errorResponse.error.message,
                            status = errorResponse.error.status
                        )
                    ))
                }
            }
        } catch (e: Exception) {
            emit(Result.failure(handleApiException(e)))
        }
    }

    /**
     * Generate content with streaming (simplified version)
     * Note: Full streaming implementation would need SSE support
     */
    suspend fun generateContentBlocking(request: StitchContentRequest): StitchContentResponse {
        val response = client.post {
            url("/v1/stitch/models/${request.model}:generateContent")
            setBody(Json.encodeToString(request))
        }

        when (response.status) {
            HttpStatusCode.OK -> {
                return response.body()
            }
            else -> {
                val errorResponse = response.body<StitchErrorResponse>()
                throw StitchApiException(
                    code = errorResponse.error.code,
                    message = errorResponse.error.message,
                    status = errorResponse.error.status
                )
            }
        }
    }

    private fun handleApiException(e: Exception): Exception {
        return when (e) {
            is ClientRequestException -> StitchApiException(
                code = e.response.status.value,
                message = "HTTP ${e.response.status.value}: ${e.message}",
                status = "HTTP_ERROR"
            )
            is ServerResponseException -> StitchApiException(
                code = e.response.status.value,
                message = "Server error: ${e.message}",
                status = "SERVER_ERROR"
            )
            else -> e
        }
    }

    /**
     * Close the HTTP client
     */
    fun close() {
        client.close()
    }
}

/**
 * Custom exception for Stitch API errors
 */
class StitchApiException(
    val code: Int,
    message: String,
    val status: String
) : Exception("Stitch API Error [$code - $status]: $message")
