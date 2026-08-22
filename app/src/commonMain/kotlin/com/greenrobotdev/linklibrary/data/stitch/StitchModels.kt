package com.greenrobotdev.linklibrary.data.stitch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Stitch API request models
 */

/**
 * Content generation request for Stitch
 */
@Serializable
data class StitchContentRequest(
    @SerialName("model")
    val model: String = "models/stitch-1.0-001",

    @SerialName("contents")
    val contents: List<Content>,

    @SerialName("tools")
    val tools: List<Tool>? = null,

    @SerialName("tool_config")
    val toolConfig: ToolConfig? = null,

    @SerialName("generation_config")
    val generationConfig: GenerationConfig? = null,

    @SerialName("system_instruction")
    val systemInstruction: Content? = null
)

/**
 * Content part for messages
 */
@Serializable
data class Content(
    @SerialName("parts")
    val parts: List<Part>,

    @SerialName("role")
    val role: String? = null
)

/**
 * Part of content (text, image, or function call)
 */
@Serializable
sealed class Part {
    @Serializable
    @SerialName("text")
    data class Text(val text: String) : Part()

    @Serializable
    @SerialName("function_call")
    data class FunctionCall(
        @SerialName("name")
        val name: String,

        @SerialName("args")
        val args: Map<String, String>
    ) : Part()

    @Serializable
    @SerialName("function_response")
    data class FunctionResponse(
        @SerialName("name")
        val name: String,

        @SerialName("response")
        val response: Map<String, String>
    ) : Part()
}

/**
 * Tool definition for function calling
 */
@Serializable
data class Tool(
    @SerialName("function_declarations")
    val functionDeclarations: List<FunctionDeclaration>
)

@Serializable
data class FunctionDeclaration(
    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String,

    @SerialName("parameters")
    val parameters: Schema? = null,

    @SerialName("required")
    val required: List<String>? = null
)

@Serializable
data class Schema(
    @SerialName("type")
    val type: String,

    @SerialName("properties")
    val properties: Map<String, Schema>? = null,

    @SerialName("required")
    val required: List<String>? = null,

    @SerialName("items")
    val items: Schema? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("enum")
    val enum: List<String>? = null
)

/**
 * Tool configuration
 */
@Serializable
data class ToolConfig(
    @SerialName("function_calling_config")
    val functionCallingConfig: FunctionCallingConfig? = null
)

@Serializable
data class FunctionCallingConfig(
    @SerialName("mode")
    val mode: String, // "AUTO", "ANY", "NONE"

    @SerialName("allowed_function_names")
    val allowedFunctionNames: List<String>? = null
)

/**
 * Generation configuration
 */
@Serializable
data class GenerationConfig(
    @SerialName("temperature")
    val temperature: Double? = null,

    @SerialName("top_p")
    val topP: Double? = null,

    @SerialName("top_k")
    val topK: Int? = null,

    @SerialName("max_output_tokens")
    val maxOutputTokens: Int? = null
)

/**
 * Stitch API response
 */
@Serializable
data class StitchContentResponse(
    @SerialName("candidates")
    val candidates: List<Candidate>,

    @SerialName("usage_metadata")
    val usageMetadata: UsageMetadata? = null,

    @SerialName("prompt_feedback")
    val promptFeedback: PromptFeedback? = null
)

@Serializable
data class Candidate(
    @SerialName("content")
    val content: Content,

    @SerialName("finish_reason")
    val finishReason: String? = null,

    @SerialName("avg_logprobs")
    val avgLogprobs: Double? = null,

    @SerialName("index")
    val index: Int? = null
)

@Serializable
data class UsageMetadata(
    @SerialName("prompt_token_count")
    val promptTokenCount: Int,

    @SerialName("total_token_count")
    val totalTokenCount: Int,

    @SerialName("cached_content_token_count")
    val cachedContentTokenCount: Int? = null
)

@Serializable
data class PromptFeedback(
    @SerialName("block_reason")
    val blockReason: String? = null,

    @SerialName("safety_ratings")
    val safetyRatings: List<SafetyRating>? = null
)

@Serializable
data class SafetyRating(
    @SerialName("category")
    val category: String,

    @SerialName("probability")
    val probability: String
)

/**
 * Error response
 */
@Serializable
data class StitchErrorResponse(
    @SerialName("error")
    val error: ErrorDetail
)

@Serializable
data class ErrorDetail(
    @SerialName("code")
    val code: Int,

    @SerialName("message")
    val message: String,

    @SerialName("status")
    val status: String
)
