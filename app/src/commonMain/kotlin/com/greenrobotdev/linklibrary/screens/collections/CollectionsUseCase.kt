package com.greenrobotdev.linklibrary.screens.collections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow

/**
 * Use case for Collections screen
 * Loads and displays collections
 * Note: Currently using static data, can be extended to use CollectionRepository when available
 */
@Composable
fun CollectionsUseCase(
    initialState: CollectionsState,
    events: Flow<CollectionsEvent>
): CollectionsState {
    var state = remember { mutableStateOf(initialState) }

    // Load collections on init
    LaunchedEffect(Unit) {
        state.value = state.value.copy(
            isLoading = true,
            error = null
        )
        try {
            // TODO: Replace with repository call when CollectionRepository is available
            // val collections = collectionRepository.getAllCollections()
            val collections = loadStaticCollections()
            state.value = state.value.copy(
                isLoading = false,
                collections = collections
            )
        } catch (e: Exception) {
            state.value = state.value.copy(
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
                    state.value = state.value.copy(isLoading = true, error = null)
                    try {
                        val collections = loadStaticCollections()
                        state.value = state.value.copy(
                            isLoading = false,
                            collections = collections
                        )
                    } catch (e: Exception) {
                        state.value = state.value.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load collections"
                        )
                    }
                }

                is CollectionsEvent.ClearError -> {
                    state.value = state.value.copy(error = null)
                }
            }
        }
    }

    return state.value
}

/**
 * Static collections data
 * TODO: Remove this when CollectionRepository is implemented
 */
private fun loadStaticCollections(): List<Collection> = listOf(
    Collection(
        id = "1",
        name = "AI & Tech",
        description = "Artificial intelligence and technology articles",
        count = 12,
        iconType = "psychology"
    ),
    Collection(
        id = "2",
        name = "Design",
        description = "UI/UX and graphic design resources",
        count = 8,
        iconType = "psychology"
    ),
    Collection(
        id = "3",
        name = "Business",
        description = "Business and entrepreneurship articles",
        count = 15,
        iconType = "psychology"
    ),
    Collection(
        id = "4",
        name = "Science",
        description = "Scientific research and discoveries",
        count = 6,
        iconType = "psychology"
    )
)
