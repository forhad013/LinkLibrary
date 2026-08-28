package com.greenrobotdev.linklibrary.bookmarks.screens.add

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.linklibrary.design.components.advanced.AutoFetchButton
import com.greenrobotdev.linklibrary.design.components.advanced.CollectionDropdown
import com.greenrobotdev.linklibrary.design.components.advanced.CollectionItem
import com.greenrobotdev.linklibrary.design.components.advanced.TagChipsSelector
import com.greenrobotdev.linklibrary.design.components.advanced.FetchState
import com.greenrobotdev.linklibrary.design.components.advanced.TagChip
import com.greenrobotdev.linklibrary.design.components.form.ValidatedUrlInput
import com.greenrobotdev.linklibrary.design.components.form.FixedFormActionsBar
import com.greenrobotdev.linklibrary.bookmarks.model.Collection
import com.greenrobotdev.linklibrary.bookmarks.model.Tag

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
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLinkScreenEnhanced(
    routeKey: NavKey,
    initialUrl: String?,
    onBack: () -> Unit,
    onAddTag: () -> Unit = {},
    onAddCollection: () -> Unit = {}
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

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Add New Link",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Button(
                        onClick = { viewModel.take(AddLinkEvent.Submit) },
                        enabled = state.isFormValid && !state.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            "Save",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },
        bottomBar = {
            FixedFormActionsBar(
                primaryText = "Capture Link",
                onPrimaryClick = { viewModel.take(AddLinkEvent.Submit) },
                enabled = state.isFormValid && !state.isLoading,
                isLoading = state.isLoading
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // URL Section with Inline Auto-Fetch
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // URL input row with auto-fetch button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // URL Input
                    OutlinedTextField(
                        value = state.url,
                        onValueChange = { viewModel.take(AddLinkEvent.UrlChanged(it)) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("https://example.com") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Link,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline
                            )
                        },
                        enabled = !state.isLoading,
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.secondary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    // Auto-Fetch Button
                    val fetchState = when {
                        state.isFetching -> FetchState.Loading
                        state.fetchError != null -> FetchState.Error
                        state.title.isNotEmpty() && state.url.isNotEmpty() -> FetchState.Success
                        else -> FetchState.Idle
                    }

                    Button(
                        onClick = { viewModel.take(AddLinkEvent.FetchMetadata) },
                        enabled = state.url.isNotBlank() && !state.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                        ),
                        modifier = Modifier
                            .height(48.dp)
                            .widthIn(min = 120.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            when (fetchState) {
                                FetchState.Loading -> {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                else -> {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Auto-fetch",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Auto-fetch",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

//                // Helper text
//                Text(
//                    text = when (fetchState) {
//                        FetchState.Success -> "Metadata successfully fetched!"
//                        FetchState.Error -> state.fetchError ?: "Failed to fetch metadata"
//                        FetchState.Loading -> "Fetching link information..."
//                        FetchState.Idle -> "Paste a URL to automatically fetch title and description"
//                    },
//                    style = MaterialTheme.typography.bodySmall,
//                    color = when (fetchState) {
//                        FetchState.Success -> MaterialTheme.colorScheme.primary
//                        FetchState.Error -> MaterialTheme.colorScheme.error
//                        else -> MaterialTheme.colorScheme.onSurfaceVariant
//                    },
//                    modifier = Modifier.padding(start = 4.dp)
//                )
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Title Field
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Title",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 4.dp)
                )
                OutlinedTextField(
                    value = state.title,
                    onValueChange = { viewModel.take(AddLinkEvent.TitleChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter link title") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Title,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline
                        )
                    },
                    enabled = !state.isLoading,
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }

            // Organization Section
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                tonalElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = "Organization",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Collection Dropdown
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Collection",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CollectionDropdown(
                                collections = state.availableCollections.map {
                                    CollectionItem(
                                        id = it.id,
                                        name = it.name,
                                        description = it.description,
                                        icon = Icons.Default.Folder,
                                        linkCount = 0,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                },
                                selectedCollectionId = state.selectedCollections.firstOrNull(),
                                onCollectionSelected = { collectionId ->
                                    state.selectedCollections.forEach { existingId ->
                                        viewModel.take(AddLinkEvent.ToggleCollection(existingId))
                                    }
                                    collectionId?.let { viewModel.take(AddLinkEvent.ToggleCollection(it)) }
                                },
                                onCollectionCreate = { name, description ->
                                    onAddCollection()
                                },
                                modifier = Modifier.weight(1f),
                                placeholder = "Select collections",
                                showLinkCounts = true,
                                allowNone = true,
                                enabled = !state.isLoading
                            )

                            // Add Collection Button
                            IconButton(
                                onClick = onAddCollection,
                                enabled = !state.isLoading,
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                        RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add collection",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }

                    // Tags Section
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Tags",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Selected tags as chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val selectedTags = state.availableTags.filter { it.id in state.selectedTags }
                            selectedTags.forEach { tag ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    border = BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.outlineVariant
                                    ),
                                    onClick = { viewModel.take(AddLinkEvent.ToggleTag(tag.id)) }
                                ) {
                                    Text(
                                        text = tag.name,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }
                            }

                            // Add Tag Button
                            IconButton(
                                onClick = onAddTag,
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        RoundedCornerShape(16.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add tag",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Personal Notes Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Personal Notes",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 4.dp)
                )

                val notesLength = state.notes?.length ?: 0
                val maxNotesLength = 500

                OutlinedTextField(
                    value = state.notes ?: "",
                    onValueChange = {
                        if (it.length <= maxNotesLength) {
                            viewModel.take(AddLinkEvent.NotesChanged(it))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = { Text("Add any personal notes, reminders, or context here...") },
                    enabled = !state.isLoading,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }

            // Add to Tasks Section
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                tonalElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Task toggle header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Add to Tasks",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Mark as Task",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Switch(
                                checked = isTaskEnabled,
                                onCheckedChange = { isTaskEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier.width(52.dp)
                            )
                        }
                    }

                    if (isTaskEnabled) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Priority Selection
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Priority",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf("Low", "Medium", "High").forEach { priority ->
                                        val isSelected = selectedPriority == priority
                                        Button(
                                            onClick = { selectedPriority = priority },
                                            colors = if (isSelected) {
                                                ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.primary.copy(
                                                        alpha = 0.6f
                                                    )
                                                )
                                            } else {
                                                ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.surface
                                                )
                                            },
                                            shape = RoundedCornerShape(16.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(48.dp),
                                            border = if (!isSelected) {
                                                BorderStroke(
                                                    1.dp,
                                                    MaterialTheme.colorScheme.outlineVariant
                                                )
                                            } else null
                                        ) {
                                            Text(
                                                priority,
                                                style = MaterialTheme.typography.labelLarge,
                                                color = if (isSelected) {
                                                    MaterialTheme.colorScheme.onPrimary
                                                } else {
                                                    MaterialTheme.colorScheme.onSurface
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            // Due Time
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Due Time",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                OutlinedTextField(
                                    value = dueTime,
                                    onValueChange = { dueTime = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    enabled = !state.isLoading,
                                    singleLine = true,
                                    shape = RoundedCornerShape(16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Error Messages
            state.error?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}