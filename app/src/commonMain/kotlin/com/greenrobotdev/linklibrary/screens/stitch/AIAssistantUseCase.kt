package com.greenrobotdev.linklibrary.screens.stitch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.greenrobotdev.linklibrary.data.stitch.StitchRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for AI Assistant functionality
 * Demonstrates integration with Google Stitch AI
 */
@Composable
fun AIAssistantUseCase(
    initialState: AIAssistantState,
    events: Flow<AIAssistantEvent>,
    repository: StitchRepository
): AIAssistantState {
    var state = remember { mutableStateOf(initialState) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is AIAssistantEvent.GenerateTags -> {
                    state.value = state.value.copy(
                        isLoading = true,
                        error = null,
                        generatedTags = null
                    )

                    repository.suggestTags(event.url, event.title)
                        .collect { result ->
                            result.fold(
                                onSuccess = { tags ->
                                    state.value = state.value.copy(
                                        isLoading = false,
                                        generatedTags = tags
                                    )
                                },
                                onFailure = { error ->
                                    state.value = state.value.copy(
                                        isLoading = false,
                                        error = error.message
                                    )
                                }
                            )
                        }
                }

                is AIAssistantEvent.GenerateDescription -> {
                    state.value = state.value.copy(
                        isLoading = true,
                        error = null,
                        generatedDescription = null
                    )

                    repository.generateDescription(event.url, event.title)
                        .collect { result ->
                            result.fold(
                                onSuccess = { description ->
                                    state.value = state.value.copy(
                                        isLoading = false,
                                        generatedDescription = description
                                    )
                                },
                                onFailure = { error ->
                                    state.value = state.value.copy(
                                        isLoading = false,
                                        error = error.message
                                    )
                                }
                            )
                        }
                }

                is AIAssistantEvent.AnalyzeLinks -> {
                    state.value = state.value.copy(
                        isLoading = true,
                        error = null,
                        analysisResult = null
                    )

                    repository.analyzeLinks(event.urls)
                        .collect { result ->
                            result.fold(
                                onSuccess = { analysis ->
                                    state.value = state.value.copy(
                                        isLoading = false,
                                        analysisResult = analysis
                                    )
                                },
                                onFailure = { error ->
                                    state.value = state.value.copy(
                                        isLoading = false,
                                        error = error.message
                                    )
                                }
                            )
                        }
                }

                is AIAssistantEvent.ClearError -> {
                    state.value = state.value.copy(error = null)
                }
            }
        }
    }

    return state.value
}

