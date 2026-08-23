package com.greenrobotdev.linklibrary.screens.tags

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow

/**
 * Use case for Tags screen
 * Loads and displays tags
 * Note: Currently using static data, can be extended to use TagRepository when available
 */
@Composable
fun TagsUseCase(
    initialState: TagsState,
    events: Flow<TagsEvent>
): TagsState {
    var state = remember { mutableStateOf(initialState) }

    // Load tags on init
    LaunchedEffect(Unit) {
        state.value = state.value.copy(
            isLoading = true,
            error = null
        )
        try {
            // TODO: Replace with repository call when TagRepository is available
            // val tags = tagRepository.getAllTags()
            val tags = loadStaticTags()
            state.value = state.value.copy(
                isLoading = false,
                tags = tags
            )
        } catch (e: Exception) {
            state.value = state.value.copy(
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
                    state.value = state.value.copy(isLoading = true, error = null)
                    try {
                        val tags = loadStaticTags()
                        state.value = state.value.copy(
                            isLoading = false,
                            tags = tags
                        )
                    } catch (e: Exception) {
                        state.value = state.value.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load tags"
                        )
                    }
                }

                is TagsEvent.ClearError -> {
                    state.value = state.value.copy(error = null)
                }
            }
        }
    }

    return state.value
}

/**
 * Static tags data
 * TODO: Remove this when TagRepository is implemented
 */
private fun loadStaticTags(): List<Tag> = listOf(
    Tag(
        id = "1",
        name = "AI & Tech",
        description = "Artificial intelligence and technology articles",
        count = 12,
        iconType = "psychology"
    ),
    Tag(
        id = "2",
        name = "Design",
        description = "UI/UX and graphic design resources",
        count = 8,
        iconType = "psychology"
    ),
    Tag(
        id = "3",
        name = "Business",
        description = "Business and entrepreneurship articles",
        count = 15,
        iconType = "psychology"
    ),
    Tag(
        id = "4",
        name = "Science",
        description = "Scientific research and discoveries",
        count = 6,
        iconType = "psychology"
    )
)
