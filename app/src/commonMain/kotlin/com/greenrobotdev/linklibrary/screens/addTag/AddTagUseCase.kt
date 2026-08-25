package com.greenrobotdev.linklibrary.screens.addTag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.greenrobotdev.linklibrary.database.repository.TagRepository
import com.greenrobotdev.linklibrary.model.toEntity
import com.greenrobotdev.linklibrary.screens.tags.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Use case for Add Tag screen
 * Handles tag creation form logic
 */
@OptIn(ExperimentalUuidApi::class)
@Composable
fun AddTagPresenter(
    initialState: AddTagState,
    events: Flow<AddTagEvent>,
    tagRepository: TagRepository
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
                            // Create the tag entity
                            val tag = Tag(
                                id = Uuid.random().toString(),
                                name = state.value.name,
                                description = state.value.description,
                                count = 0,
                            )

                            // Save to database using the repository
                            val result = tagRepository.addTag(tag.toEntity()).first()

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
                                        error = error.message ?: "Failed to create tag"
                                    )
                                }
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
    return name.isNotBlank() // Description is now optional
}
