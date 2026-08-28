package com.greenrobotdev.linklibrary.bookmarks.screens.add.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.bookmarks.model.Collection
import com.greenrobotdev.linklibrary.bookmarks.model.Tag
import com.greenrobotdev.linklibrary.design.components.advanced.CollectionDropdown
import com.greenrobotdev.linklibrary.design.components.advanced.CollectionItem
import com.greenrobotdev.linklibrary.design.components.advanced.FetchState

/**
 * AddLinkTopBar - Top app bar for Add Link screen
 *
 * Features:
 * - Close button for navigation
 * - Screen title
 * - Save button with form validation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLinkTopBar(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    isFormValid: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
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
                onClick = onBackClick,
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
                onClick = onSaveClick,
                enabled = isFormValid && !isLoading,
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
}

/**
 * UrlInputSection - URL input with auto-fetch functionality
 *
 * Features:
 * - URL text input with validation
 * - Auto-fetch button with loading states
 * - Fetch state indicators
 * - Helper text for user guidance
 */
@Composable
fun UrlInputSection(
    url: String,
    onUrlChange: (String) -> Unit,
    onFetchClick: () -> Unit,
    isFetching: Boolean,
    fetchError: String?,
    titleFetched: Boolean,
    urlNotEmpty: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
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
                value = url,
                onValueChange = onUrlChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("https://example.com") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Link,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline
                    )
                },
                enabled = !isLoading,
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )

            // Auto-Fetch Button
            val fetchState = when {
                isFetching -> FetchState.Loading
                fetchError != null -> FetchState.Error
                titleFetched && urlNotEmpty -> FetchState.Success
                else -> FetchState.Idle
            }

            Button(
                onClick = onFetchClick,
                enabled = url.isNotBlank() && !isLoading,
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
    }
}

/**
 * TitleInputSection - Title input field
 *
 * Features:
 * - Labeled title input
 * - Leading icon
 * - Validation styling
 */
@Composable
fun TitleInputSection(
    title: String,
    onTitleChange: (String) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Title",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 4.dp)
        )
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter link title") },
            leadingIcon = {
                Icon(
                    Icons.Default.Title,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline
                )
            },
            enabled = !isLoading,
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            )
        )
    }
}

/**
 * OrganizationSection - Collection and tags selection
 *
 * Features:
 * - Collection dropdown with inline creation
 * - Tags chips display with selection
 * - Add collection/tag buttons
 */
@Composable
fun OrganizationSection(
    availableCollections: List<Collection>,
    selectedCollections: List<String>,
    availableTags: List<Tag>,
    selectedTags: List<String>,
    onCollectionToggle: (String) -> Unit,
    onTagToggle: (String) -> Unit,
    onAddTag: () -> Unit,
    onAddCollection: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
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
            CollectionInputRow(
                collections = availableCollections,
                selectedCollectionId = selectedCollections.firstOrNull(),
                onCollectionSelected = { collectionId ->
                    selectedCollections.forEach { existingId ->
                        onCollectionToggle(existingId)
                    }
                    collectionId?.let { onCollectionToggle(it) }
                },
                onCollectionCreate = { _, _ -> onAddCollection() },
                onAddCollection = onAddCollection,
                isLoading = isLoading
            )

            // Tags Section
            TagsSection(
                availableTags = availableTags,
                selectedTags = selectedTags,
                onTagToggle = onTagToggle,
                onAddTag = onAddTag
            )
        }
    }
}

/**
 * CollectionInputRow - Collection input with dropdown and add button
 */
@Composable
private fun CollectionInputRow(
    collections: List<Collection>,
    selectedCollectionId: String?,
    onCollectionSelected: (String?) -> Unit,
    onCollectionCreate: (String, String) -> Unit,
    onAddCollection: () -> Unit,
    isLoading: Boolean
) {
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
                collections = collections.map {
                    CollectionItem(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        icon = Icons.Default.Folder,
                        linkCount = 0,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                selectedCollectionId = selectedCollectionId,
                onCollectionSelected = onCollectionSelected,
                onCollectionCreate = onCollectionCreate,
                modifier = Modifier.weight(1f),
                placeholder = "Select collections",
                showLinkCounts = true,
                allowNone = true,
                enabled = !isLoading
            )

            // Add Collection Button
            IconButton(
                onClick = onAddCollection,
                enabled = !isLoading,
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
}

/**
 * TagsSection - Tags display with chips and add button
 */
@Composable
private fun TagsSection(
    availableTags: List<Tag>,
    selectedTags: List<String>,
    onTagToggle: (String) -> Unit,
    onAddTag: () -> Unit
) {
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
            val selectedTagsList = availableTags.filter { it.id in selectedTags }
            selectedTagsList.forEach { tag ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant
                    ),
                    onClick = { onTagToggle(tag.id) }
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

/**
 * PersonalNotesSection - Personal notes input with character limit
 *
 * Features:
 * - Multi-line text input
 * - Character counting
 * - 500 character limit
 */
@Composable
fun PersonalNotesSection(
    notes: String,
    onNotesChange: (String) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    maxLength: Int = 500
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Personal Notes",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 4.dp)
        )

        OutlinedTextField(
            value = notes,
            onValueChange = {
                if (it.length <= maxLength) {
                    onNotesChange(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text("Add any personal notes, reminders, or context here...") },
            enabled = !isLoading,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            )
        )
    }
}

/**
 * TaskSection - Add to tasks functionality
 *
 * Features:
 * - Task enable/disable toggle
 * - Priority selection (Low/Medium/High)
 * - Due time input
 * - Conditional visibility based on enabled state
 */
@Composable
fun TaskSection(
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    selectedPriority: String,
    onPriorityChange: (String) -> Unit,
    dueTime: String,
    onDueTimeChange: (String) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
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
            TaskToggleHeader(
                isEnabled = isEnabled,
                onEnabledChange = onEnabledChange
            )

            if (isEnabled) {
                TaskOptionsContent(
                    selectedPriority = selectedPriority,
                    onPriorityChange = onPriorityChange,
                    dueTime = dueTime,
                    onDueTimeChange = onDueTimeChange,
                    isLoading = isLoading
                )
            }
        }
    }
}

/**
 * TaskToggleHeader - Toggle switch for task functionality
 */
@Composable
private fun TaskToggleHeader(
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit
) {
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
                checked = isEnabled,
                onCheckedChange = onEnabledChange,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.width(52.dp)
            )
        }
    }
}

/**
 * TaskOptionsContent - Priority and due time inputs
 */
@Composable
private fun TaskOptionsContent(
    selectedPriority: String,
    onPriorityChange: (String) -> Unit,
    dueTime: String,
    onDueTimeChange: (String) -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Priority Selection
        PrioritySelector(
            selectedPriority = selectedPriority,
            onPriorityChange = onPriorityChange
        )

        // Due Time
        DueTimeInput(
            dueTime = dueTime,
            onDueTimeChange = onDueTimeChange,
            isLoading = isLoading
        )
    }
}

/**
 * PrioritySelector - Priority selection buttons
 */
@Composable
private fun PrioritySelector(
    selectedPriority: String,
    onPriorityChange: (String) -> Unit
) {
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
                    onClick = { onPriorityChange(priority) },
                    colors = if (isSelected) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
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
                        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
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
}

/**
 * DueTimeInput - Due time input field
 */
@Composable
private fun DueTimeInput(
    dueTime: String,
    onDueTimeChange: (String) -> Unit,
    isLoading: Boolean
) {
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
            onValueChange = onDueTimeChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            enabled = !isLoading,
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            )
        )
    }
}

/**
 * ErrorMessageCard - Error message display card
 *
 * Features:
 * - Error icon and message
 * - Material 3 error styling
 * - Optional dismiss functionality
 */
@Composable
fun ErrorMessageCard(
    error: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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