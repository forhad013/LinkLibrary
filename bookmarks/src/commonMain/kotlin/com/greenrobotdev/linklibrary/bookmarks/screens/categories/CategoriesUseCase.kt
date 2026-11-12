package com.greenrobotdev.linklibrary.bookmarks.screens.categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.database.repository.CategoryRepository
import com.greenrobotdev.linklibrary.bookmarks.model.toCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject

/**
 * Use case for Categories screen
 * Loads and displays categoryNames using CategoryRepository
 */
@Composable
fun CategoriesPresenter(
    initialState: CategoriesState,
    events: Flow<CategoriesEvent>,
    categoryRepository: CategoryRepository = koinInject()
): CategoriesState {
    var state by remember { mutableStateOf(initialState) }

    // Load categoryNames on init
    LaunchedEffect(Unit) {
        state = state.copy(
            isLoading = true,
            error = null
        )
        try {
            val result = categoryRepository.getCategories().first()
            result.fold(
                onSuccess = { categoryEntities ->
                    // Convert entities to UI models with counts
                    val categoryNames = categoryEntities.map { entity ->
                        entity.toCategory(count = 0) // TODO: Fetch actual counts
                    }
                    state = state.copy(
                        isLoading = false,
                        categoryNames = categoryNames
                    )
                },
                onFailure = { error ->
                    state = state.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load categoryNames"
                    )
                }
            )
        } catch (e: Exception) {
            state = state.copy(
                isLoading = false,
                error = e.message ?: "Failed to load categoryNames"
            )
        }
    }

    // Handle events
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is CategoriesEvent.LoadCategories -> {
                    state = state.copy(isLoading = true, error = null)
                    try {
                        val result = categoryRepository.getCategories().first()
                        result.fold(
                            onSuccess = { categoryEntities ->
                                val categoryNames = categoryEntities.map { entity ->
                                    entity.toCategory(count = 0) // TODO: Fetch actual counts
                                }
                                state = state.copy(
                                    isLoading = false,
                                    categoryNames = categoryNames
                                )
                            },
                            onFailure = { error ->
                                state = state.copy(
                                    isLoading = false,
                                    error = error.message ?: "Failed to load categoryNames"
                                )
                            }
                        )
                    } catch (e: Exception) {
                        state = state.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load categoryNames"
                        )
                    }
                }

                is CategoriesEvent.ClearError -> {
                    state = state.copy(error = null)
                }
            }
        }
    }

    return state
}
