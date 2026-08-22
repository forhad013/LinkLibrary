package com.greenrobotdev.linklibrary.screens.addCollection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow

/**
 * Use case for Add Collection screen
 * Handles collection creation form logic
 * Note: Currently using mock save, can be extended to use CollectionRepository when available
 */
@Composable
fun AddCollectionUseCase(
    initialState: AddCollectionState,
    events: Flow<AddCollectionEvent>
): AddCollectionState {
    var state = remember { mutableStateOf(initialState) }

    // Handle events
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is AddCollectionEvent.NameChanged -> {
                    state.value = state.value.copy(
                        name = event.name,
                        isFormValid = isFormValid(event.name, state.value.description)
                    )
                }

                is AddCollectionEvent.DescriptionChanged -> {
                    state.value = state.value.copy(
                        description = event.description,
                        isFormValid = isFormValid(state.value.name, event.description)
                    )
                }

                is AddCollectionEvent.Submit -> {
                    if (state.value.isFormValid) {
                        state.value = state.value.copy(isLoading = true, error = null)
                        try {
                            // TODO: Replace with repository call when CollectionRepository is available
                            // val collection = Collection(
                            //     id = UUID.randomUUID().toString(),
                            //     name = state.value.name,
                            //     description = state.value.description
                            // )
                            // collectionRepository.addCollection(collection)

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
                                error = e.message ?: "Failed to create collection"
                            )
                        }
                    }
                }

                is AddCollectionEvent.ClearError -> {
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
