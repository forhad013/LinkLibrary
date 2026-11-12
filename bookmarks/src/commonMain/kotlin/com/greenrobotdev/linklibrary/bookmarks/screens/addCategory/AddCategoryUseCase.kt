package com.greenrobotdev.linklibrary.bookmarks.screens.addCategory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.greenrobotdev.linklibrary.database.repository.CategoryRepository
import com.greenrobotdev.linklibrary.bookmarks.model.Category
import com.greenrobotdev.linklibrary.bookmarks.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Use case for Add Category screen
 * Handles category creation form logic
 */
@OptIn(ExperimentalUuidApi::class)
@Composable
fun AddCategoryPresenter(
    initialState: AddCategoryState,
    events: Flow<AddCategoryEvent>,
    categoryRepository: CategoryRepository = koinInject()
): AddCategoryState {
    var state = remember { mutableStateOf(initialState) }

    // Handle events
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is AddCategoryEvent.NameChanged -> {
                    state.value = state.value.copy(
                        name = event.name,
                        isFormValid = isFormValid(event.name, state.value.description)
                    )
                }

                is AddCategoryEvent.DescriptionChanged -> {
                    state.value = state.value.copy(
                        description = event.description,
                        isFormValid = isFormValid(state.value.name, event.description)
                    )
                }

                is AddCategoryEvent.Submit -> {
                    if (state.value.isFormValid) {
                        state.value = state.value.copy(isLoading = true, error = null)
                        try {
                            // Create the category entity
                            val category = Category(
                                id = Uuid.random().toString(),
                                name = state.value.name,
                                count = 0,
                            )

                            // Save to database using the repository
                            val result = categoryRepository.addCategory(category.toEntity()).first()

                            result.fold(
                                onSuccess = {
                                    // Set success flag after successful submission
                                    state.value = state.value.copy(
                                        isLoading = false,
                                        success = true
                                    )
                                },
                                onFailure = { error ->
                                    state.value = state.value.copy(
                                        isLoading = false,
                                        error = error.message ?: "Failed to create category"
                                    )
                                }
                            )
                        } catch (e: Exception) {
                            state.value = state.value.copy(
                                isLoading = false,
                                error = e.message ?: "Failed to create category"
                            )
                        }
                    }
                }

                is AddCategoryEvent.ClearError -> {
                    state.value = state.value.copy(error = null)
                }
            }
        }
    }

    return state.value
}

/**
 * Validates if the form is complete
 */
private fun isFormValid(name: String, description: String): Boolean {
    return name.isNotBlank() // Description is now optional
}
