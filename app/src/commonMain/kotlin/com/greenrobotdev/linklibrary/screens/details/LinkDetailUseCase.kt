package com.greenrobotdev.linklibrary.screens.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

/**
 * Use case for Link Detail screen
 * Handles loading, displaying, and managing a single link
 */
@Composable
fun LinkDetailUseCase(
    initialState: LinkDetailState,
    events: Flow<LinkDetailEvent>,
    linkRepository: LinkRepository,
    linkId: String
): LinkDetailState {
    var state = remember { mutableStateOf(initialState) }

    // Load link on screen init
    LaunchedEffect(linkId) {
        state.value = state.value.copy(isLoading = true, error = null)
        try {
            linkRepository.getLinks()
                .catch { e ->
                    state.value = state.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load link"
                    )
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { links ->
                            val link = links.find { it.id == linkId }?.toDomain()
                            state.value = state.value.copy(
                                isLoading = false,
                                link = link,
                                error = if (link == null) "Link not found" else null
                            )
                        },
                        onFailure = { e ->
                            state.value = state.value.copy(
                                isLoading = false,
                                error = e.message ?: "Failed to load link"
                            )
                        }
                    )
                }
        } catch (e: Exception) {
            state.value = state.value.copy(
                isLoading = false,
                error = e.message ?: "Failed to load link"
            )
        }
    }

    // Handle events
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is LinkDetailEvent.LoadLink -> {
                    state.value = state.value.copy(isLoading = true, error = null)
                    try {
                        linkRepository.getLinks()
                            .catch { e ->
                                state.value = state.value.copy(
                                    isLoading = false,
                                    error = e.message ?: "Failed to load link"
                                )
                            }
                            .collect { result ->
                                result.fold(
                                    onSuccess = { links ->
                                        val link = links.find { it.id == linkId }?.toDomain()
                                        state.value = state.value.copy(
                                            isLoading = false,
                                            link = link,
                                            error = if (link == null) "Link not found" else null
                                        )
                                    },
                                    onFailure = { e ->
                                        state.value = state.value.copy(
                                            isLoading = false,
                                            error = e.message ?: "Failed to load link"
                                        )
                                    }
                                )
                            }
                    } catch (e: Exception) {
                        state.value = state.value.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load link"
                        )
                    }
                }

                is LinkDetailEvent.ToggleFavorite -> {
                    state.value = state.value.copy(isLoading = true)
                    linkRepository.toggleFavorite(linkId)
                        .onStart { state.value = state.value.copy(isLoading = true, error = null) }
                        .catch { e ->
                            state.value = state.value.copy(
                                isLoading = false,
                                error = e.message ?: "Failed to update favorite"
                            )
                        }
                        .collect { result ->
                            result.fold(
                                onSuccess = { updatedLink ->
                                    state.value = state.value.copy(
                                        isLoading = false,
                                        link = updatedLink.toDomain()
                                    )
                                },
                                onFailure = { e ->
                                    state.value = state.value.copy(
                                        isLoading = false,
                                        error = e.message ?: "Failed to update favorite"
                                    )
                                }
                            )
                        }
                }

                is LinkDetailEvent.Delete -> {
                    state.value = state.value.copy(isLoading = true)
                    linkRepository.deleteLink(linkId)
                        .onStart { state.value = state.value.copy(isLoading = true, error = null) }
                        .catch { e ->
                            state.value = state.value.copy(
                                isLoading = false,
                                error = e.message ?: "Failed to delete link"
                            )
                        }
                        .collect { result ->
                            result.fold(
                                onSuccess = {
                                    state.value = state.value.copy(isDeleted = true, isLoading = false)
                                },
                                onFailure = { e ->
                                    state.value = state.value.copy(
                                        error = e.message ?: "Failed to delete link"
                                    )
                                }
                            )
                        }
                }

                is LinkDetailEvent.ClearError -> {
                    state.value = state.value.copy(error = null)
                }
            }
        }
    }

    return state.value
}
