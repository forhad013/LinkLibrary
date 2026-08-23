package com.greenrobotdev.linklibrary.screens.addTag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow

/**
 * Use case for Add Tag screen
 * Handles tag creation form logic
 * Note: Currently using mock save, can be extended to use TagRepository when available
 */
@Composable
fun AddTagUseCase(
    initialState: AddTagState,
    events: Flow<AddTagEvent>
): AddTagState {
    var state = remember { mutableStateOf(initialState) }

    // Handle events
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is AddTagEvent.NameChanged -> {
                    state.value = state.value.copy(
                        name = event.name,
                        isFormValid = isFormValid(event.name, state.value.description)
                    )
                }

                is AddTagEvent.DescriptionChanged -> {
                    state.value = state.value.copy(
                        description = event.description,
                        isFormValid = isFormValid(state.value.name, event.description)
                    )
                }

                is AddTagEvent.Submit -> {
                    if (state.value.isFormValid) {
                        state.value = state.value.copy(isLoading = true, error = null)
                        try {
                            // TODO: Replace with repository call when TagRepository is available
                            // val tag = Tag(
                            //     id = UUID.randomUUID().toString(),
                            //     name = state.value.name,
                            //     description = state.value.description
                            // )
                            // tagRepository.addTag(tag)

                            // Mock delay to simulate saving
                            kotlinx.coroutines.delay(500)

                            // Set success flag after successful submission
                            state.value = state.value.copy(
                                isLoading = false,
                                success = true
                            )
                        } catch (e: Exception) {
                            state.value = state.value.copy(
                                isLoading = false,
                                error = e.message ?: "Failed to create tag"
                            )
                        }
                    }
                }

                is AddTagEvent.ClearError -> {
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
    return name.isNotBlank() && description.isNotBlank()
}
