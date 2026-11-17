package com.greenrobotdev.linklibrary.bookmarks.screens.add

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.AddLinkTopBar
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.ErrorMessageCard
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.OrganizationSection
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.PersonalNotesSection
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.TaskSection
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.UrlTitleCard
import com.greenrobotdev.linklibrary.design.components.form.FixedFormActionsBar

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
fun AddLinkScreen(
    routeKey: NavKey,
    initialUrl: String?,
    onBack: () -> Unit,
    onAddTag: () -> Unit,
    onAddCategory: () -> Unit
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
        onAddCategory = onAddCategory,
        onBack = onBack
    )
}


@Composable
fun AddLinkView(
    state: AddLinkState,
    onEvent: (AddLinkEvent) -> Unit,
    onAddTag: () -> Unit,
    onAddCategory: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Use ViewModel state for task section instead of local state
    var isTaskEnabled by remember { mutableStateOf(  state.isTask ) }

    val selectedPriority = state.taskPriority
    val dueTime = state.dueTime

    // Animated container for the form
    val contentAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "content_alpha"
    )

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
            onTaskEnabledChange = { onEvent(AddLinkEvent.ToggleTask(it)) },
            selectedPriority = selectedPriority,
            onPriorityChange = { onEvent(AddLinkEvent.SetTaskPriority(it)) },
            dueTime = dueTime,
            onDueTimeChange = { onEvent(AddLinkEvent.SetDueTime(it)) },
            modifier = Modifier.padding(padding),
            onAddTag = onAddTag,
            onAddCategory = onAddCategory
        )
    }
}

/**
 * AddLinkFormContent - Main form content layout
 *
 * Orchestrates all the form sections in a scrollable layout
 */
@Composable
fun AddLinkFormContent(
    state: AddLinkState,
    onEvent: (AddLinkEvent) -> Unit,
    isTaskEnabled: Boolean,
    onTaskEnabledChange: (Boolean) -> Unit,
    selectedPriority: String,
    onPriorityChange: (String) -> Unit,
    dueTime: String,
    onDueTimeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onAddTag: () -> Unit = {},
    onAddCategory: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Combined URL and Title Card with enhanced contrast
        UrlTitleCard(
            url = state.url,
            onUrlChange = { onEvent(AddLinkEvent.UrlChanged(it)) },
            title = state.title,
            onTitleChange = { onEvent(AddLinkEvent.TitleChanged(it)) },
            onFetchClick = { onEvent(AddLinkEvent.FetchMetadata) },
            isFetching = state.isFetching,
            fetchError = state.fetchError,
            titleFetched = state.title.isNotEmpty(),
            urlNotEmpty = state.url.isNotEmpty(),
            isLoading = state.isLoading
        )

        // Organization Section
        OrganizationSection(
            availableCategories = state.availableCategories,
            selectedCategoryIds = state.selectedCategoryIds.toList(),
            availableTags = state.availableTags,
            selectedTags = state.selectedTags.toList(),
            onCategoryToggle = { onEvent(AddLinkEvent.ToggleCategory(it)) },
            onTagToggle = { onEvent(AddLinkEvent.ToggleTag(it)) },
            onAddTag = onAddTag,
            onAddCategory = onAddCategory,
            isLoading = state.isLoading,
            onClearAllCategories = { onEvent(AddLinkEvent.ClearAllCategories) }
        )

        // Personal Notes Section
        PersonalNotesSection(
            notes = state.notes ?: "",
            onNotesChange = { onEvent(AddLinkEvent.NotesChanged(it)) },
            isLoading = state.isLoading
        )

        // Add to Tasks Section
        TaskSection(
            isEnabled = isTaskEnabled,
            onEnabledChange = onTaskEnabledChange,
            selectedPriority = selectedPriority,
            onPriorityChange = onPriorityChange,
            dueTime = dueTime,
            onDueTimeChange = onDueTimeChange,
            isLoading = state.isLoading
        )

        // Error Messages
        state.error?.let { error ->
            ErrorMessageCard(
                error = error,
                onDismiss = { /* Optional: Add error dismiss logic */ }
            )
        }
    }
}

/**
 * AddLinkFormScaffold - Scaffold with top bar and bottom action bar
 *
 * Provides the standard Material 3 layout for the add link form
 */
@Composable
fun AddLinkFormScaffold(
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    isFormValid: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AddLinkTopBar(
                onBackClick = onBackClick,
                onSaveClick = onSaveClick,
                isFormValid = isFormValid,
                isLoading = isLoading
            )
        },
        bottomBar = {
            FixedFormActionsBar(
                primaryText = "Capture Link",
                onPrimaryClick = onSaveClick,
                enabled = isFormValid && !isLoading,
                isLoading = isLoading
            )
        }
    ) { padding ->
        content(padding)
    }
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