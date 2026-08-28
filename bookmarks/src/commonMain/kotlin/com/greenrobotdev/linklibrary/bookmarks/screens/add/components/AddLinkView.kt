package com.greenrobotdev.linklibrary.bookmarks.screens.add.components

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.bookmarks.screens.add.AddLinkEvent
import com.greenrobotdev.linklibrary.bookmarks.screens.add.AddLinkState
import com.greenrobotdev.linklibrary.design.components.form.FixedFormActionsBar

/**
 * AddLinkView - Presentational component that receives state and event callbacks
 *
 * This component handles:
 * - UI state management (animations, local form state)
 * - Layout orchestration
 * - Event handling via callbacks
 *
 * Business logic (ViewModel, navigation effects) is owned by the parent screen
 */
@Composable
fun AddLinkView(
    state: AddLinkState,
    onEvent: (AddLinkEvent) -> Unit,
    onAddTag: () -> Unit,
    onAddCollection: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Local UI state for task section
    var isTaskEnabled by remember { mutableStateOf(false) }
    var selectedPriority by remember { mutableStateOf("Medium") }
    var dueTime by remember { mutableStateOf("09:00") }

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
            onTaskEnabledChange = { isTaskEnabled = it },
            selectedPriority = selectedPriority,
            onPriorityChange = { selectedPriority = it },
            dueTime = dueTime,
            onDueTimeChange = { dueTime = it },
            modifier = Modifier.padding(padding),
            onAddTag = onAddTag,
            onAddCollection = onAddCollection
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
    onAddCollection: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // URL Section with Inline Auto-Fetch
        UrlInputSection(
            url = state.url,
            onUrlChange = { onEvent(AddLinkEvent.UrlChanged(it)) },
            onFetchClick = { onEvent(AddLinkEvent.FetchMetadata) },
            isFetching = state.isFetching,
            fetchError = state.fetchError,
            titleFetched = state.title.isNotEmpty(),
            urlNotEmpty = state.url.isNotEmpty(),
            isLoading = state.isLoading
        )

        // Title Field
        TitleInputSection(
            title = state.title,
            onTitleChange = { onEvent(AddLinkEvent.TitleChanged(it)) },
            isLoading = state.isLoading
        )

        // Organization Section
        OrganizationSection(
            availableCollections = state.availableCollections,
            selectedCollections = state.selectedCollections.toList(),
            availableTags = state.availableTags,
            selectedTags = state.selectedTags.toList(),
            onCollectionToggle = { onEvent(AddLinkEvent.ToggleCollection(it)) },
            onTagToggle = { onEvent(AddLinkEvent.ToggleTag(it)) },
            onAddTag = onAddTag,
            onAddCollection = onAddCollection,
            isLoading = state.isLoading
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