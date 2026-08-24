package com.greenrobotdev.linklibrary.screens.add

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.linklibrary.components.form.AutoFetchButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLinkScreen(
    routeKey: NavKey,
    initialUrl: String?,
    onBack: () -> Unit,
    onAddTag: () -> Unit = {},
    onAddCollection: () -> Unit = {}
) {

    val viewModel: AddLinkViewModel = viewModel<AddLinkViewModel>(key = routeKey.toString()) { AddLinkViewModel(initialUrl) }

    val state by viewModel.models.collectAsState()

    // Task state - now using state from viewModel
    val isTaskEnabled = state.isTask
    val selectedPriority = state.taskPriority
    val dueTime = state.dueTime

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Link", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = { viewModel.take(AddLinkEvent.Submit) },
                        enabled = state.isFormValid && !state.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(66, 133, 244)
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Save")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceContainer,
                tonalElevation = 2.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onBack,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = { viewModel.take(AddLinkEvent.Submit) },
                        enabled = state.isFormValid && !state.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(66, 133, 244)
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Save Link")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp, 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // URL Input Section
            Column {
                Text(
                    text = "URL",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = state.url,
                        onValueChange = { viewModel.take(AddLinkEvent.UrlChanged(it)) },
                        placeholder = { Text("https://example.com") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Link,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        isError = state.error != null,
                        enabled = !state.isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    AutoFetchButton(
                        isFetching = state.isFetching,
                        onClick = { viewModel.take(AddLinkEvent.FetchMetadata) },
                        enabled = state.url.isNotBlank() && !state.isLoading
                    )
                }
                Text(
                    text = "Paste a URL to automatically fetch title and description.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }

            Divider()

            // Metadata Section
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                // Title Field
                Column {
                    Text(
                        text = "Title",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = state.title,
                        onValueChange = { viewModel.take(AddLinkEvent.TitleChanged(it)) },
                        placeholder = { Text("Enter link title") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Title,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !state.isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }
            }

            // Organization Section (Card)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = "Organization",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Collection Dropdown
                    Column {
                        Text(
                            text = "Collection",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Collection selection dropdown
                        CollectionSelector(
                            collections = state.availableCollections,
                            selectedCollections = state.selectedCollections,
                            onCollectionToggle = { collectionId ->
                                viewModel.take(AddLinkEvent.ToggleCollection(collectionId))
                            },
                            onAddCollection = onAddCollection,
                            isLoading = state.isLoadingTagsAndCollections
                        )
                    }

                    // Tags Section
                    Column {
                        Text(
                            text = "Tags",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Tag chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Selected tags
                            state.selectedTags.forEach { tagId ->
                                val tag = state.availableTags.find { it.id == tagId }
                                tag?.let {
                                    InputChip(
                                        selected = true,
                                        onClick = { viewModel.take(AddLinkEvent.ToggleTag(tagId)) },
                                        label = { Text(tag.name) },
                                        avatar = {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Remove tag",
                                                modifier = Modifier.size(16.dp)
                                            )
                                        },
                                        colors = InputChipDefaults.inputChipColors(
                                            containerColor = MaterialTheme.colorScheme.tertiaryFixedDim.copy(alpha = 0.1f),
                                            labelColor = MaterialTheme.colorScheme.tertiary,
                                            leadingIconColor = MaterialTheme.colorScheme.tertiary
                                        ),
                                        border = null
                                    )
                                }
                            }

                            // Available tags
                            state.availableTags
                                .filter { !state.selectedTags.contains(it.id) }
                                .take(3)
                                .forEach { tag ->
                                    FilterChip(
                                        selected = false,
                                        onClick = { viewModel.take(AddLinkEvent.ToggleTag(tag.id)) },
                                        label = { Text(tag.name) },
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                        colors = FilterChipDefaults.filterChipColors(
                                            containerColor = MaterialTheme.colorScheme.surface
                                        )
                                    )
                                }

                            // Add tag button
                            IconButton(
                                onClick = onAddTag,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(start = 8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Add tag",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Personal Notes Section
            Column {
                Text(
                    text = "Personal Notes",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = state.notes ?: "",
                    onValueChange = { viewModel.take(AddLinkEvent.NotesChanged(it)) },
                    placeholder = { Text("Add any personal notes, reminders, or context here...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    enabled = !state.isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }

            // Add to Tasks Section (Card)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Add to Tasks",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Mark as Task",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Switch(
                                checked = isTaskEnabled,
                                onCheckedChange = { viewModel.take(AddLinkEvent.ToggleTask(it)) }
                            )
                        }
                    }

                    if (isTaskEnabled) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            // Priority Selection
                            Column {
                                Text(
                                    text = "Priority",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf("Low", "Medium", "High").forEach { priority ->
                                        val isSelected = selectedPriority == priority
                                        OutlinedButton(
                                            onClick = { viewModel.take(AddLinkEvent.SetTaskPriority(priority)) },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = if (isSelected) {
                                                ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                            } else {
                                                ButtonDefaults.outlinedButtonColors(
                                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            },
                                            border = if (isSelected) null else BorderStroke(
                                                1.dp,
                                                MaterialTheme.colorScheme.outlineVariant
                                            )
                                        ) {
                                            Text(priority)
                                        }
                                    }
                                }
                            }

                            // Due Time
                            Column {
                                Text(
                                    text = "Due Time",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                OutlinedTextField(
                                    value = dueTime,
                                    onValueChange = { viewModel.take(AddLinkEvent.SetDueTime(it)) },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.AccessTime,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Error message
            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Fetch error message
            state.fetchError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

// Helper components
@Composable
private fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
    )
}

@Composable
private fun CollectionSelector(
    collections: List<com.greenrobotdev.linklibrary.screens.collections.Collection>,
    selectedCollections: Set<String>,
    onCollectionToggle: (String) -> Unit,
    onAddCollection: () -> Unit,
    isLoading: Boolean
) {
    // Simplified collection selector - can be enhanced with dropdown
    Column {
        if (isLoading) {
            Text("Loading collections...", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            if (collections.isEmpty()) {
                OutlinedButton(
                    onClick = onAddCollection,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(" Add Collection")
                }
            } else {
                collections.forEach { collection ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                            .padding(16.dp)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Checkbox(
                                checked = selectedCollections.contains(collection.id),
                                onCheckedChange = { onCollectionToggle(collection.id) }
                            )
                            Text(collection.name)
                        }
                    }
                }
            }
        }
    }
}
