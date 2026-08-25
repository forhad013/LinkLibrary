package com.greenrobotdev.linklibrary.screens.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

/**
 * Use Case for Share Pop-up business logic
 * Handles social media sharing and link management
 */
@Composable
fun SharePresenter(
    initialState: ShareState,
    events: Flow<ShareEvent>,
    linkRepository: LinkRepository
): ShareState {

    var state = remember { mutableStateOf(initialState) }

    // Handle events
    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is ShareEvent.Initialize -> {
                    // Load link data if linkId is provided
                    if (state.value.linkId.isNotEmpty()) {
                        state.value = state.value.copy(isLoading = true)
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
                                        val link = links.find { it.id == state.value.linkId }
                                        state.value = state.value.copy(
                                            isLoading = false,
                                            linkTitle = link?.title ?: "",
                                            linkUrl = link?.url?.toString() ?: "",
                                            customMessage = "Check out this link: ${link?.title ?: ""}",
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
                    }
                }
                is ShareEvent.SelectPlatform -> {
                    state.value = state.value.copy(selectedPlatform = event.platform)
                }
                is ShareEvent.UpdateMessage -> {
                    state.value = state.value.copy(customMessage = event.message)
                }
                is ShareEvent.Share -> {
                    state.value = state.value.copy(isSharing = true)
                    // Perform share action
                    kotlinx.coroutines.delay(500) // Simulate share
                    state.value = state.value.copy(isSharing = false)
                    // Note: Actual platform-specific sharing would be handled here
                }
                is ShareEvent.ClearError -> {
                    state.value = state.value.copy(error = null)
                }
                is ShareEvent.Dismiss -> {
                    // Reset state for next time
                    state.value = initialState
                }
            }
        }
    }

    return state.value
}
