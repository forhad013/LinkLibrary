package com.greenrobotdev.linklibrary.bookmarks.screens.collections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.database.repository.CollectionRepository
import com.greenrobotdev.linklibrary.bookmarks.model.toCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject

/**
 * Use case for Collections screen
 * Loads and displays collections using CollectionRepository
 */
@Composable
fun CollectionsPresenter(
    initialState: CollectionsState,
    events: Flow<CollectionsEvent>,
    collectionRepository: CollectionRepository = koinInject()
): CollectionsState {
    var state by remember { mutableStateOf(initialState) }

    // Load collections on init
    LaunchedEffect(Unit) {
        state = state.copy(
            isLoading = true,
            error = null
        )
        try {
            val result = collectionRepository.getCollections().first()
            result.fold(
                onSuccess = { collectionEntities ->
                    // Convert entities to UI models with counts
                    val collections = collectionEntities.map { entity ->
                        entity.toCollection(count = 0) // TODO: Fetch actual counts
                    }
                    state = state.copy(
                        isLoading = false,
                        collections = collections
                    )
                },
                onFailure = { error ->
                    state = state.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load collections"
                    )
                }
            )
        } catch (e: Exception) {
            state = state.copy(
                isLoading = false,
                error = e.message ?: "Failed to load collections"
            )
        }
    }

    // Handle events
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is CollectionsEvent.LoadCollections -> {
                    state = state.copy(isLoading = true, error = null)
                    try {
                        val result = collectionRepository.getCollections().first()
                        result.fold(
                            onSuccess = { collectionEntities ->
                                val collections = collectionEntities.map { entity ->
                                    entity.toCollection(count = 0) // TODO: Fetch actual counts
                                }
                                state = state.copy(
                                    isLoading = false,
                                    collections = collections
                                )
                            },
                            onFailure = { error ->
                                state = state.copy(
                                    isLoading = false,
                                    error = error.message ?: "Failed to load collections"
                                )
                            }
                        )
                    } catch (e: Exception) {
                        state = state.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load collections"
                        )
                    }
                }

                is CollectionsEvent.ClearError -> {
                    state = state.copy(error = null)
                }
            }
        }
    }

    return state
}
