package com.greenrobotdev.linklibrary.bookmarks.screens.add.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.linklibrary.bookmarks.screens.add.AddLinkEvent
import com.greenrobotdev.linklibrary.bookmarks.screens.add.AddLinkViewModel
import com.greenrobotdev.linklibrary.bookmarks.screens.add.AddLinkScreenState
import com.greenrobotdev.linklibrary.design.components.form.FixedFormActionsBar

/**
 * AddLinkView - Main view component with screen-level logic and state management
 *
 * This component handles:
 * - View model initialization and state management
 * - LaunchedEffect blocks for navigation and auto-fetch
 * - Animation states
 * - Task state management
 * - Content layout orchestration
 */
@Composable
fun AddLinkView(
    routeKey: NavKey,
    initialUrl: String?,
    onBack: () -> Unit,
    onAddTag: () -> Unit,
    onAddCollection: () -> Unit,
    content: @Composable (
        state: AddLinkScreenState,
        onEvent: (AddLinkEvent) -> Unit,
        isTaskEnabled: Boolean,
        onTaskEnabledChange: (Boolean) -> Unit,
        selectedPriority: String,
        onPriorityChange: (String) -> Unit,
        dueTime: String,
        onDueTimeChange: (String) -> Unit,
        modifier: Modifier
    ) -> Unit
) {
    val viewModel: AddLinkViewModel = viewModel<AddLinkViewModel>(key = routeKey.toString()) { AddLinkViewModel(initialUrl) }
    val state by viewModel.models.collectAsState()

    // Navigate back after successful link addition
    LaunchedEffect(state.success) {
        if (state.success) {
            onBack()
        }
    }

    // Auto-fetch metadata when URL is entered and valid
    LaunchedEffect(state.url) {
        if (state.url.isNotBlank() && state.url.startsWith("http") && !state.isFetching && state.title.isBlank()) {
            viewModel.take(AddLinkEvent.FetchMetadata)
        }
    }

    // Animated container for the form
    val contentAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "content_alpha"
    )

    // Tasks state
    var isTaskEnabled by remember { mutableStateOf(false) }
    var selectedPriority by remember { mutableStateOf("Medium") }
    var dueTime by remember { mutableStateOf("09:00") }

    // Content callback with all necessary state and callbacks
    content(
        state,
        { viewModel.take(it) },
        isTaskEnabled,
        { isTaskEnabled = it },
        selectedPriority,
        { selectedPriority = it },
        dueTime,
        { dueTime = it },
        Modifier
    )
}

/**
 * AddLinkFormContent - Main form content layout
 *
 * Orchestrates all the form sections in a scrollable layout
 */
@Composable
fun AddLinkFormContent(
    state: AddLinkScreenState,
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
            selectedCollections = state.selectedCollections,
            availableTags = state.availableTags,
            selectedTags = state.selectedTags,
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