package com.greenrobotdev.linklibrary.data.stitch

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository interface for Stitch AI operations
 */
interface StitchRepository {
    /**
     * Generate text completion using Stitch AI
     * @param prompt The input prompt
     * @param systemInstruction Optional system instruction
     * @return Flow of Result containing the generated text
     */
    fun generateText(
        prompt: String,
        systemInstruction: String? = null
    ): Flow<Result<String>>

    /**
     * Generate content with function calling support
     * @param prompt The input prompt
     * @param tools List of available tools/functions
     * @param systemInstruction Optional system instruction
     * @return Flow of Result containing the response (may include function calls)
     */
    fun generateWithTools(
        prompt: String,
        tools: List<Tool>,
        systemInstruction: String? = null
    ): Flow<Result<StitchContentResponse>>

    /**
     * Execute a function call with Stitch
     * @param conversation History of conversation including function responses
     * @return Flow of Result containing the final response
     */
    fun executeFunctionCall(
        conversation: List<Content>
    ): Flow<Result<StitchContentResponse>>

    /**
     * Analyze links using AI
     * @param urls List of URLs to analyze
     * @return Flow of Result containing analysis
     */
    fun analyzeLinks(urls: List<String>): Flow<Result<String>>

    /**
     * Suggest tags for links based on content
     * @param linkUrl The URL to analyze
     * @param linkTitle Optional title
     * @return Flow of Result containing suggested tags
     */
    fun suggestTags(linkUrl: String, linkTitle: String? = null): Flow<Result<List<String>>>

    /**
     * Generate description for a link
     * @param linkUrl The URL to describe
     * @param linkTitle Optional title
     * @return Flow of Result containing generated description
     */
    fun generateDescription(linkUrl: String, linkTitle: String? = null): Flow<Result<String>>
}

/**
 * Implementation of StitchRepository
 */
class StitchRepositoryImpl(
    private val apiClient: StitchApiClient
) : StitchRepository {

    override fun generateText(
        prompt: String,
        systemInstruction: String?
    ): Flow<Result<String>> = flow {
        try {
            val systemInstructionContent = if (systemInstruction != null) {
                Content(
                    parts = listOf(Part.Text(systemInstruction)),
                    role = "system"
                )
            } else null

            val request = StitchContentRequest(
                contents = listOf(
                    Content(
                        parts = listOf(Part.Text(prompt)),
                        role = "user"
                    )
                ),
                systemInstruction = systemInstructionContent,
                generationConfig = GenerationConfig(
                    temperature = 0.7,
                    maxOutputTokens = 1024
                )
            )

            val response = apiClient.generateContentBlocking(request)

            // Extract the generated text from the response
            val generatedText = response.candidates
                .firstOrNull()
                ?.content?.parts
                ?.filterIsInstance<Part.Text>()
                ?.firstOrNull()
                ?.text
                ?: throw Exception("No text generated")

            emit(Result.success(generatedText))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun generateWithTools(
        prompt: String,
        tools: List<Tool>,
        systemInstruction: String?
    ): Flow<Result<StitchContentResponse>> {
        val systemInstructionContent = if (systemInstruction != null) {
            Content(
                parts = listOf(Part.Text(systemInstruction)),
                role = "system"
            )
        } else null

        val request = StitchContentRequest(
            contents = listOf(
                Content(
                    parts = listOf(Part.Text(prompt)),
                    role = "user"
                )
            ),
            tools = tools,
            systemInstruction = systemInstructionContent,
            toolConfig = ToolConfig(
                functionCallingConfig = FunctionCallingConfig(mode = "AUTO")
            )
        )

        return apiClient.generateContent(request)
    }

    override fun executeFunctionCall(
        conversation: List<Content>
    ): Flow<Result<StitchContentResponse>> {
        val request = StitchContentRequest(
            contents = conversation,
            toolConfig = ToolConfig(
                functionCallingConfig = FunctionCallingConfig(mode = "AUTO")
            )
        )

        return apiClient.generateContent(request)
    }

    override fun analyzeLinks(urls: List<String>): Flow<Result<String>> = flow {
        try {
            val prompt = buildString {
                append("Analyze the following links and provide insights:\n\n")
                urls.forEach { url ->
                    append("- $url\n")
                }
                append("\nPlease provide:\n")
                append("1. Summary of what these links are about\n")
                append("2. Categories they belong to\n")
                append("3. Any patterns or connections\n")
            }

            val result = generateText(
                prompt = prompt,
                systemInstruction = "You are a helpful assistant that analyzes web links and provides organized summaries."
            )

            result.collect { emit(it) }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun suggestTags(linkUrl: String, linkTitle: String?): Flow<Result<List<String>>> = flow {
        try {
            val prompt = buildString {
                append("Generate relevant tags for the following link:\n\n")
                append("URL: $linkUrl\n")
                if (linkTitle != null) {
                    append("Title: $linkTitle\n")
                }
                append("\nProvide 5-10 relevant tags separated by commas.")
            }

            val result = generateText(
                prompt = prompt,
                systemInstruction = "You are a helpful assistant that generates relevant tags for web links."
            )

            result.collect { result ->
                result.fold(
                    onSuccess = { text ->
                        val tags = text.split(",")
                            .map { it.trim() }
                            .filter { it.isNotBlank() }
                        emit(Result.success(tags))
                    },
                    onFailure = { emit(Result.failure(it)) }
                )
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun generateDescription(linkUrl: String, linkTitle: String?): Flow<Result<String>> = flow {
        try {
            val prompt = buildString {
                append("Generate a concise description for this link:\n\n")
                append("URL: $linkUrl\n")
                if (linkTitle != null) {
                    append("Title: $linkTitle\n")
                }
                append("\nProvide a 1-2 sentence description.")
            }

            val result = generateText(
                prompt = prompt,
                systemInstruction = "You are a helpful assistant that writes concise descriptions for web links."
            )

            result.collect { emit(it) }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
