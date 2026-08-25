package com.greenrobotdev.linklibrary.bookmarks.screens.add

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey

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

    // Material 3 color scheme - using primary color scheme for save button
    val saveButtonColor = MaterialTheme.colorScheme.primary

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
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
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint =  MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "Add New Link",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        onClick = { viewModel.take(AddLinkEvent.Submit) },
                        enabled = state.isFormValid && !state.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = saveButtonColor
                        ),
                        modifier = Modifier.padding(end = 8.dp),
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Text("Save", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

    },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp) // 16dp major spacing system
        ) {
            // URL Input Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "URL",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(54.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = state.url,
                        onValueChange = { viewModel.take(AddLinkEvent.UrlChanged(it)) },
                        placeholder = { Text("https://example.com") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Link,
                                contentDescription = null,
                                tint =  MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        isError = state.error != null,
                        enabled = !state.isLoading,
                        shape = MaterialTheme.shapes.small,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    AutoFetchButton(
                        modifier = Modifier.fillMaxHeight(),
                        isFetching = state.isFetching,
                        onClick = { viewModel.take(AddLinkEvent.FetchMetadata) },
                        enabled = state.url.isNotBlank() && !state.isLoading
                    )
                }
                Text(
                    text = "Paste a URL to automatically fetch title and description.",
                    style = MaterialTheme.typography.bodySmall,
                    color =  MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 0.dp)
                )
            }

            Divider()

            // Metadata Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Title Field
                Column {
                    Text(
                        text = "Title",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    OutlinedTextField(
                        value = state.title,
                        onValueChange = { viewModel.take(AddLinkEvent.TitleChanged(it)) },
                        placeholder = { Text("Enter link title") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Title,
                                contentDescription = null,
                                tint =  MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !state.isLoading,
                        shape = MaterialTheme.shapes.small,
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
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp) // MD3 uses tonal elevation
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp) // 16dp spacing system
                ) {
                    Text(
                        text = "Organization",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Collection Dropdown
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Collection",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Collection selection dropdown
                        CollectionSelector(
                            collections = state.availableCollections,
                            selectedCollections = state.selectedCollections,
                            onCollectionToggle = { collectionId ->
                                viewModel.take(AddLinkEvent.ToggleCollection(collectionId))
                            },
                            onClearAllCollections = {
                                // Clear all selected collections by toggling each one
                                state.selectedCollections.forEach { collectionId ->
                                    viewModel.take(AddLinkEvent.ToggleCollection(collectionId))
                                }
                            },
                            onAddCollection = onAddCollection,
                            isLoading = state.isLoadingTagsAndCollections
                        )
                    }

                    // Tags Section
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Tags",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                            leadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer
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
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }

                            // Add tag button
                            IconButton(
                                onClick = onAddTag,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(start = 0.dp) // Remove extra padding for consistency
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
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Personal Notes",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = state.notes ?: "",
                    onValueChange = { viewModel.take(AddLinkEvent.NotesChanged(it)) },
                    placeholder = { Text("Add any personal notes, reminders, or context here...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    enabled = !state.isLoading,
                    shape = MaterialTheme.shapes.large, // Use Material 3 shape tokens
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }

            // Add to Tasks Section (Card)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp) // MD3 uses tonal elevation
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp) // 16dp spacing system
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
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Priority Selection
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                                        OutlinedButton(
                                            onClick = { viewModel.take(AddLinkEvent.SetTaskPriority(priority)) },
                                            modifier = Modifier.weight(1f),
                                            shape = MaterialTheme.shapes.small,
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
                                            Text(priority, style = MaterialTheme.typography.labelLarge)
                                        }
                                    }
                                }
                            }

                            // Due Time
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Due Time",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                    shape = MaterialTheme.shapes.small,
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
            .background( MaterialTheme.colorScheme.surfaceContainerHighest)
    )
}

@Composable
private fun CollectionSelector(
    collections: List<com.greenrobotdev.linklibrary.screens.collections.Collection>,
    selectedCollections: Set<String>,
    onCollectionToggle: (String) -> Unit,
    onClearAllCollections: () -> Unit = {},
    onAddCollection: () -> Unit,
    isLoading: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    println("whkq63 availableCollections ${collections}")
    println("whkq63 expanded state: $expanded")
    println("whkq63 collections size: ${collections.size}")

    // Get all selected collections and display them as comma-separated list
    val selectedCollectionsList = collections.filter { selectedCollections.contains(it.id) }
    val displayText = if (selectedCollectionsList.isEmpty()) {
        "Select collections"
    } else {
        selectedCollectionsList.joinToString(", ") { it.name }
    }

    Column {
        if (isLoading) {
            Text(
                "Loading collections...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Material 3 Dropdown
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = androidx.compose.foundation.interaction.MutableInteractionSource(),
                            indication = null
                        ) {
                            println("whkq63 clicked! collections size: ${collections.size}")
                            if (collections.isNotEmpty()) {
                                expanded = !expanded
                                println("whkq63 expanded changed to: $expanded")
                            }
                        }
                ) {
                    var textFieldWidth by androidx.compose.runtime.mutableStateOf(0)

                    OutlinedTextField(
                        value = displayText,
                        onValueChange = { }, // Read-only
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                textFieldWidth = coordinates.size.width
                            },
                        enabled = false,
                        readOnly = true,
                        singleLine = true,
                        shape = MaterialTheme.shapes.small,
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Expand dropdown",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    // Dropdown Menu - positioned outside the Box constraints
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                            println("whkq63 onDismissRequest called")
                        },
                        offset = DpOffset(0.dp, 0.dp),
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        println("whkq63 DropdownMenu rendering with ${collections.size} items")

                        // "Clear all selections" option
                        if (selectedCollectionsList.isNotEmpty()) {
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(
                                            "Clear all selections",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        Text(
                                            "Remove ${selectedCollectionsList.size} selected collection(s)",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                onClick = {
                                    println("whkq63 Clear all selections clicked")
                                    onClearAllCollections()
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Clear all",
                                        modifier = Modifier.size(20.dp),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                },
                                modifier = Modifier.background(
                                    MaterialTheme.colorScheme.errorContainer.copy(
                                        alpha = 0.1f
                                    )
                                )
                            )

                            // Add a visual divider
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(MaterialTheme.colorScheme.outlineVariant)
                            )
                        }

                        // Collection options
                        collections.forEach { collection ->
                            println("whkq63 rendering collection item: ${collection.name}")
                            val isSelected = selectedCollections.contains(collection.id)

                            DropdownMenuItem(
                                text = {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            collection.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                                        )
                                        if (collection.description?.isNotBlank() == true) {
                                            Text(
                                                collection.description,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 1,
                                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    println("whkq63 Collection clicked: ${collection.name}")
                                    onCollectionToggle(collection.id)
                                    // Don't close dropdown to allow multiple selections
                                },
                                leadingIcon = {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(MaterialTheme.shapes.extraSmall)
                                            .background(
                                                if (isSelected)
                                                    MaterialTheme.colorScheme.primary
                                                else
                                                    MaterialTheme.colorScheme.surfaceContainerHighest
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Selected",
                                                modifier = Modifier.size(16.dp),
                                                tint = MaterialTheme.colorScheme.onPrimary
                                            )
                                        }
                                    }
                                },
                                trailingIcon = if (isSelected) {
                                    {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Selected",
                                            modifier = Modifier.size(18.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                } else null,
                                modifier = Modifier.background(
                                    if (isSelected)
                                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                                    else
                                        MaterialTheme.colorScheme.surface
                                )
                            )
                        }

                        // Handle empty collections case
                        if (collections.isEmpty()) {
                            println("whkq63 No collections - showing empty message")
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "No collections available",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = { },
                                enabled = false
                            )
                        }
                    }
                }

                // Add Collection Button (Icon only)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable(enabled = !isLoading) { onAddCollection() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add collection",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Helper text when no collections exist
            if (collections.isEmpty() && !isLoading) {
                Text(
                    text = "No collections available. Create one to get started.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
