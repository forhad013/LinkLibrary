package com.greenrobotdev.linklibrary.screens.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.model.Link
import com.greenrobotdev.linklibrary.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Composable
fun AddLinkUseCase(
    initialState: AddLinkState,
    events: Flow<AddLinkEvent>,
    linkRepository: LinkRepository,
    initialUrl: String?
): AddLinkState {
    var state by remember { mutableStateOf(initialState) }
    
    // Set initial URL if provided
    LaunchedEffect(initialUrl) {
        initialUrl?.let { url ->
            state = state.copy(url = url, isFormValid = url.isNotBlank())
        }
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is AddLinkEvent.TitleChanged -> {
                    state = state.copy(
                        title = event.title,
                        isFormValid = isFormValid(state.url, event.title, state.description)
                    )
                }
                is AddLinkEvent.UrlChanged -> {
                    state = state.copy(
                        url = event.url,
                        isFormValid = isFormValid(event.url, state.title, state.description)
                    )
                }
                is AddLinkEvent.DescriptionChanged -> {
                    state = state.copy(
                        description = event.description,
                        isFormValid = isFormValid(state.url, state.title, event.description)
                    )
                }
                is AddLinkEvent.ToggleFavorite -> {
                    state = state.copy(isFavorite = event.isFavorite)
                }
                is AddLinkEvent.Submit -> {
                    if (state.isFormValid) {
                        state = state.copy(isLoading = true, error = null)
                        try {
                            val link = Link(
                                id = Uuid.random().toString(),
                                title = state.title.ifBlank { state.url },
                                url = state.url,
                                description = state.description,
                                isFavorite = state.isFavorite,
                                createdAt = Clock.System.now()
                            )
                            val result = linkRepository.addLink(link.toEntity())

                            // Set success flag after successful submission
                            state = state.copy(
                                isLoading = false,
                                success = true
                            )
                        } catch (e: Exception) {
                            state = state.copy(
                                isLoading = false,
                                error = e.message ?: "Failed to add link"
                            )
                        }
                    }
                }
                is AddLinkEvent.ClearError -> {
                    state = state.copy(error = null)
                }
            }
        }
    }
    
    return state
}

private fun isFormValid(url: String, title: String, description: String): Boolean {
    return url.isNotBlank() && (title.isNotBlank() || description.isNotBlank())
}
