package com.greenrobotdev.linklibrary.screens.tags

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.database.repository.TagRepository
import com.greenrobotdev.linklibrary.model.toTag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject

/**
 * Use case for Tags screen
 * Loads and displays tags using TagRepository
 */
@Composable
fun TagsUseCase(
    initialState: TagsState,
    events: Flow<TagsEvent>,
    tagRepository: TagRepository = koinInject()
): TagsState {
    var state by remember { mutableStateOf(initialState) }

    // Load tags on init
    LaunchedEffect(Unit) {
        state = state.copy(
            isLoading = true,
            error = null
        )
        try {
            val result = tagRepository.getTags().first()
            result.fold(
                onSuccess = { tagEntities ->
                    // Convert entities to UI models with counts
                    val tags = tagEntities.map { entity ->
                        entity.toTag(count = 0) // TODO: Fetch actual counts
                    }
                    state = state.copy(
                        isLoading = false,
                        tags = tags
                    )
                },
                onFailure = { error ->
                    state = state.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load tags"
                    )
                }
            )
        } catch (e: Exception) {
            state = state.copy(
                isLoading = false,
                error = e.message ?: "Failed to load tags"
            )
        }
    }

    // Handle events
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is TagsEvent.LoadTags -> {
                    state = state.copy(isLoading = true, error = null)
                    try {
                        val result = tagRepository.getTags().first()
                        result.fold(
                            onSuccess = { tagEntities ->
                                val tags = tagEntities.map { entity ->
                                    entity.toTag(count = 0) // TODO: Fetch actual counts
                                }
                                state = state.copy(
                                    isLoading = false,
                                    tags = tags
                                )
                            },
                            onFailure = { error ->
                                state = state.copy(
                                    isLoading = false,
                                    error = error.message ?: "Failed to load tags"
                                )
                            }
                        )
                    } catch (e: Exception) {
                        state = state.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load tags"
                        )
                    }
                }

                is TagsEvent.ClearError -> {
                    state = state.copy(error = null)
                }
            }
        }
    }

    return state
}
