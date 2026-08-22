package com.greenrobotdev.linklibrary.screens.stitch

import kotlinx.serialization.Serializable

/**
 * State for AI Assistant screen
 */
@Serializable
data class AIAssistantState(
    val isLoading: Boolean = false,
    val generatedTags: List<String>? = null,
    val generatedDescription: String? = null,
    val analysisResult: String? = null,
    val error: String? = null
)

/**
 * Events for AI Assistant operations
 */
sealed interface AIAssistantEvent {
    data class GenerateTags(val url: String, val title: String?) : AIAssistantEvent
    data class GenerateDescription(val url: String, val title: String?) : AIAssistantEvent
    data class AnalyzeLinks(val urls: List<String>) : AIAssistantEvent
    object ClearError : AIAssistantEvent
}
