package com.greenrobotdev.linklibrary.bookmarks.screens.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.AddLinkView

/**
 * Enhanced Add Link Screen - Material 3 Expressive Design
 *
 * Screen-led architecture approach:
 * - Screen owns ViewModel and business logic
 * - Screen handles navigation and side effects
 * - View components are presentational and receive state/callbacks
 *
 * Business Logic Responsibilities:
 * - ViewModel creation and lifecycle management
 * - Navigation effects (success -> back navigation)
 * - Auto-fetch logic based on URL changes
 * - Event flow to ViewModel
 *
 * UI Composition:
 * - AddLinkView: Presentational component with state and callbacks
 * - Individual sections: Reusable UI components
 */
@Composable
fun AddLinkScreenEnhanced(
    routeKey: NavKey,
    initialUrl: String?,
    onBack: () -> Unit,
    onAddTag: () -> Unit,
    onAddCollection: () -> Unit
) {
    // Screen owns the ViewModel and state
    val viewModel: AddLinkViewModel = viewModel<AddLinkViewModel>(key = routeKey.toString()) { AddLinkViewModel(initialUrl) }
    val state by viewModel.models.collectAsState()

    // Screen handles navigation effects
    LaunchedEffect(state.success) {
        if (state.success) {
            onBack()
        }
    }

    // Screen handles business logic (auto-fetch)
    LaunchedEffect(state.url) {
        if (shouldAutoFetch(state)) {
            viewModel.take(AddLinkEvent.FetchMetadata)
        }
    }

    // Screen passes state and event callbacks to presentational view
    AddLinkView(
        state = state,
        onEvent = { viewModel.take(it) },
        onAddTag = onAddTag,
        onAddCollection = onAddCollection,
        onBack = onBack
    )
}

/**
 * Determines if auto-fetch should be triggered based on current state
 */
private fun shouldAutoFetch(state: AddLinkState): Boolean {
    return state.url.isNotBlank() &&
           state.url.startsWith("http") &&
           !state.isFetching &&
           state.title.isBlank()
}