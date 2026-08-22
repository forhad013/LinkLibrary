package com.greenrobotdev.linklibrary.screens.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import app.cash.molecule.moleculeFlow
import app.cash.molecule.RecompositionMode
import com.greenrobotdev.linklibrary.data.LinkRepository
import kotlinx.coroutines.flow.StateFlow

/**
 * Use Case for Share Pop-up business logic
 * Handles social media sharing and link management
 */
@Composable
fun ShareUseCase(
    initialState: ShareState,
    events: kotlinx.coroutines.flow.SharedFlow<ShareEvent>,
    linkRepository: LinkRepository
): StateFlow<ShareState> {

    var state by remember { mutableStateOf(initialState) }

    // Handle events
    kotlinx.coroutines.flow.LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is ShareEvent.Initialize -> {
                    // Load link data if linkId is provided
                    if (state.linkId.isNotEmpty()) {
                        linkRepository.getLinkById(state.linkId).collect { result ->
                            result.onSuccess { link ->
                                state = state.copy(
                                    linkTitle = link.title,
                                    linkUrl = link.url.toString(),
                                    customMessage = "Check out this link: ${link.title}"
                                )
                            }
                            result.onFailure { error ->
                                state = state.copy(error = error.message)
                            }
                        }
                    }
                }
                is ShareEvent.SelectPlatform -> { platform ->
                    state = state.copy(selectedPlatform = platform)
                }
                is ShareEvent.UpdateMessage -> { message ->
                    state = state.copy(customMessage = message)
                }
                is ShareEvent.Share -> {
                    state = state.copy(isSharing = true)
                    // Perform share action
                    kotlinx.coroutines.delay(500) // Simulate share
                    state = state.copy(isSharing = false)
                    // Note: Actual platform-specific sharing would be handled here
                }
                is ShareEvent.ClearError -> {
                    state = state.copy(error = null)
                }
                is ShareEvent.Dismiss -> {
                    // Reset state for next time
                    state = initialState
                }
            }
        }
    }

    return moleculeFlow(RecompositionMode.Immediate) {
        state
    }
}
