package com.greenrobotdev.linklibrary.bookmarks.screens.add

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.*

/**
 * Enhanced Add Link Screen - Material 3 Expressive Design
 *
 * Comprehensive link creation screen with:
 * - Real-time URL validation
 * - Auto-fetch metadata with loading states
 * - Advanced tag management
 * - Collection selection with inline creation
 * - Personal notes with character count
 * - Form validation and error handling
 * - Expressive animations and micro-interactions
 *
 * This screen has been refactored into logical components for better maintainability:
 * - AddLinkView: Handles screen-level logic and state management
 * - AddLinkFormScaffold: Provides the standard Material 3 layout
 * - AddLinkFormContent: Orchestrates all form sections
 * - Individual components for each section (UrlInputSection, OrganizationSection, etc.)
 */
@Composable
fun AddLinkScreenEnhanced(
    routeKey: NavKey,
    initialUrl: String?,
    onBack: () -> Unit,
    onAddTag: () -> Unit = {},
    onAddCollection: () -> Unit = {}
) {
    AddLinkView(
        routeKey = routeKey,
        initialUrl = initialUrl,
        onBack = onBack,
        onAddTag = onAddTag,
        onAddCollection = onAddCollection
    ) { state, onEvent, isTaskEnabled, onTaskEnabledChange, selectedPriority, onPriorityChange, dueTime, onDueTimeChange, modifier ->

        AddLinkFormScaffold(
            onSaveClick = { onEvent(AddLinkEvent.Submit) },
            onBackClick = onBack,
            isFormValid = state.isFormValid,
            isLoading = state.isLoading,
            modifier = modifier
        ) { padding ->
            AddLinkFormContent(
                state = state,
                onEvent = onEvent,
                isTaskEnabled = isTaskEnabled,
                onTaskEnabledChange = onTaskEnabledChange,
                selectedPriority = selectedPriority,
                onPriorityChange = onPriorityChange,
                dueTime = dueTime,
                onDueTimeChange = onDueTimeChange,
                modifier = Modifier.padding(padding),
                onAddTag = onAddTag,
                onAddCollection = onAddCollection
            )
        }
    }
}