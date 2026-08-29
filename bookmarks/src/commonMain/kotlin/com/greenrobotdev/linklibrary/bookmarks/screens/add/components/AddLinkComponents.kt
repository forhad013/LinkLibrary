package com.greenrobotdev.linklibrary.bookmarks.screens.add.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.scale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
 * - Screen title with Material 3 expressive styling
 * - Save button with form validation and enhanced visual feedback
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
    // Animate button state for better visual feedback
    val buttonScale by animateFloatAsState(
        targetValue = if (isFormValid && !isLoading) 1f else 0.95f,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        ),
        label = "save_button_scale"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Enhanced close button with better visual feedback
            Surface(
                onClick = onBackClick,
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
                tonalElevation = 1.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Enhanced title with better typography
            Text(
                text = "Add New Link",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Enhanced save button with Material 3 expressive styling
            Surface(
                onClick = onSaveClick,
                enabled = isFormValid && !isLoading,
                modifier = Modifier
                    .scale(buttonScale)
                    .padding(end = 8.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(20.dp),
                color = when {
                    isFormValid && !isLoading -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.surfaceContainerHighest
                },
                tonalElevation = if (isFormValid && !isLoading) 2.dp else 0.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Text(
                        "Save",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                        color = when {
                            isFormValid && !isLoading -> MaterialTheme.colorScheme.onPrimary
                            else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        }
                    )
                }
            }
        }
    }
}

/**
 * UrlInputSection - URL input with integrated auto-fetch functionality
 *
 * Features:
 * - URL text input with integrated auto-fetch button
 * - Auto-fetch button with loading states integrated into text field
 * - Enhanced Material 3 expressive styling for both light and dark themes
 * - Animated state transitions
 * - Fetch state indicators with improved visual feedback
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
    // Animate container based on fetch state
    val containerColor by animateFloatAsState(
        targetValue = when {
            isFetching -> 0.08f
            fetchError != null -> 0.05f
            titleFetched && urlNotEmpty -> 0.12f
            else -> 0f
        },
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "container_color_alpha"
    )

    val accentColor = when {
        fetchError != null -> MaterialTheme.colorScheme.error
        titleFetched && urlNotEmpty -> MaterialTheme.colorScheme.primary
        isFetching -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // URL label with enhanced styling
        Text(
            text = "URL",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 4.dp)
        )

        // Enhanced URL input field with Material 3 expressive styling
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = when {
                isFetching -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                fetchError != null -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
                titleFetched && urlNotEmpty -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                else -> MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f)
            },
            border = BorderStroke(
                width = when {
                    isFetching -> 2.dp
                    fetchError != null -> 2.dp
                    titleFetched && urlNotEmpty -> 2.dp
                    url.isNotBlank() -> 1.dp
                    else -> 1.dp
                },
                color = accentColor
            ),
            tonalElevation = when {
                isFetching -> 2.dp
                titleFetched && urlNotEmpty -> 1.dp
                else -> 0.dp
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Leading icon with animated tint
                Icon(
                    Icons.Default.Link,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )

                // Text input
                Box(modifier = Modifier.weight(1f)) {
                    androidx.compose.material3.OutlinedTextField(
                        value = url,
                        onValueChange = onUrlChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("https://example.com") },
                        enabled = !isLoading,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                            focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                            cursorColor = accentColor
                        )
                    )
                }

                // Integrated auto-fetch button with enhanced styling
                val fetchState = when {
                    isFetching -> FetchState.Loading
                    fetchError != null -> FetchState.Error
                    titleFetched && urlNotEmpty -> FetchState.Success
                    else -> FetchState.Idle
                }

                if (url.isNotBlank()) {
                    Surface(
                        onClick = onFetchClick,
                        enabled = !isLoading && !isFetching && fetchState != FetchState.Success,
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = when (fetchState) {
                            FetchState.Success -> MaterialTheme.colorScheme.primaryContainer
                            FetchState.Error -> MaterialTheme.colorScheme.errorContainer
                            FetchState.Loading -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            else -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                        },
                        tonalElevation = 1.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            when (fetchState) {
                                FetchState.Loading -> {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                FetchState.Success -> {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = "Fetched successfully",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                FetchState.Error -> {
                                    Icon(
                                        imageVector = Icons.Default.Error,
                                        contentDescription = "Fetch failed",
                                        tint = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                else -> {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Auto-fetch metadata",
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Show hint icon when empty
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = "Auto-fetch will appear here",
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Enhanced helper text with better visual feedback
        when {
            isFetching -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Fetching metadata...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            fetchError != null -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        fetchError,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            titleFetched && urlNotEmpty -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        "Metadata fetched successfully",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            url.isNotBlank() -> {
                Text(
                    "Tap the auto-fetch button to get link metadata",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            else -> {
                Text(
                    "Enter a URL to auto-fetch link information",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

/**
 * TitleInputSection - Title input field with Material 3 expressive styling
 *
 * Features:
 * - Enhanced labeled title input
 * - Animated leading icon with color changes
 * - Material 3 expressive container design
 * - Focus state animations
 * - Improved dark theme support
 */
@Composable
fun TitleInputSection(
    title: String,
    onTitleChange: (String) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    // Animate border based on focus and content state
    val hasContent by animateFloatAsState(
        targetValue = if (title.isNotBlank()) 1f else 0.6f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "title_has_content"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Enhanced label with better typography
        Text(
            text = "Title",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 4.dp)
        )

        // Enhanced title input field with Material 3 expressive styling
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
            border = BorderStroke(
                width = if (title.isNotBlank()) 2.dp else 1.dp,
                color = if (title.isNotBlank()) {
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                }
            ),
            tonalElevation = if (title.isNotBlank()) 1.dp else 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Animated leading icon
                Icon(
                    Icons.Default.Title,
                    contentDescription = null,
                    tint = if (title.isNotBlank()) {
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    modifier = Modifier.size(20.dp)
                )

                // Text input
                Box(modifier = Modifier.weight(1f)) {
                    androidx.compose.material3.OutlinedTextField(
                        value = title,
                        onValueChange = onTitleChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter link title") },
                        enabled = !isLoading,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                            focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }
        }

        // Optional character count hint
        if (title.isNotBlank()) {
            Text(
                text = "${title.length} characters",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

/**
 * OrganizationSection - Collection and tags selection with Material 3 expressive styling
 *
 * Features:
 * - Enhanced Material 3 expressive container design
 * - Collection dropdown with inline creation
 * - Tags chips display with improved selection feedback
 * - Animated state transitions
 * - Improved dark theme support
 */
@Composable
fun OrganizationSection(
    availableCollections: List<Collection>,
    selectedCollections: List<String>,
    availableTags: List<Tag>,
    selectedTags: List<String>,
    onCollectionToggle: (String) -> Unit,
    onClearAllCollections: () -> Unit,
    onTagToggle: (String) -> Unit,
    onAddTag: () -> Unit,
    onAddCollection: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    // Animate elevation based on selection state
    val hasSelection = selectedCollections.isNotEmpty() || selectedTags.isNotEmpty()
    val elevation by animateFloatAsState(
        targetValue = if (hasSelection) 1f else 0.5f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "organization_elevation"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.4f)
        ),
        border = BorderStroke(
            width = if (hasSelection) 2.dp else 1.dp,
            color = if (hasSelection) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (hasSelection) 2.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Enhanced section header with icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Folder,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Text(
                    text = "Organization",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            CollectionSelector(
                collections = availableCollections.toSet(),
                selectedCollections = selectedCollections.toSet(),
                onCollectionToggle = onCollectionToggle,
                onClearAllCollections = onClearAllCollections,
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

        when {
            // Empty state - no collections available
            collections.isEmpty() -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onAddCollection,
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading
                    ) {
                        Text(
                            "Create your first collection",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Show collection dropdown
            else -> {
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

        when {
            // Empty state - no tags available
            availableTags.isEmpty() -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onAddTag,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Create your first tag",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Show selected tags as chips
            else -> {

                when {
                    // No tags selected but available
                    availableTags.isEmpty() -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "You haven't created any tags",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f)
                            )

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

                    // Show selected tags chips
                    else -> {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            availableTags.forEach { tag ->
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


                        }
                    }
                }
            }
        }
    }
}

/**
 * PersonalNotesSection - Personal notes input with Material 3 expressive styling
 *
 * Features:
 * - Enhanced multi-line text input with Material 3 expressive design
 * - Character counting with visual feedback
 * - 500 character limit with progress indication
 * - Animated state transitions
 * - Improved dark theme support
 */
@Composable
fun PersonalNotesSection(
    notes: String,
    onNotesChange: (String) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    maxLength: Int = 500
) {
    // Animate character progress
    val characterProgress by animateFloatAsState(
        targetValue = notes.length.toFloat() / maxLength,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "character_progress"
    )

    // Color based on how close to limit
    val progressColor = when {
        notes.length >= maxLength * 0.9 -> MaterialTheme.colorScheme.error
        notes.length >= maxLength * 0.7 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Enhanced label with character count
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Personal Notes",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Animated character counter
            if (notes.isNotBlank()) {
                Text(
                    text = "${notes.length}/$maxLength",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = progressColor.copy(alpha = 0.8f)
                )
            }
        }

        // Enhanced notes container with Material 3 expressive styling
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.4f),
            border = BorderStroke(
                width = when {
                    notes.length >= maxLength -> 2.dp
                    notes.isNotBlank() -> 1.dp
                    else -> 1.dp
                },
                color = when {
                    notes.length >= maxLength -> MaterialTheme.colorScheme.error
                    notes.isNotBlank() -> MaterialTheme.colorScheme.outlineVariant
                    else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                }
            ),
            tonalElevation = if (notes.isNotBlank()) 1.dp else 0.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    androidx.compose.material3.OutlinedTextField(
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
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                            focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                }

                // Character progress bar
                if (notes.isNotBlank()) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHighest
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(characterProgress)
                                .fillMaxHeight()
                                .background(progressColor.copy(alpha = 0.6f))
                        )
                    }
                }
            }
        }

        // Helper text with character limit warning
        if (notes.length >= maxLength * 0.8) {
            val warningText = when {
                notes.length >= maxLength -> "Character limit reached"
                notes.length >= maxLength * 0.9 -> "Almost at character limit"
                else -> "Getting close to character limit"
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Icon(
                    if (notes.length >= maxLength) Icons.Default.Error else Icons.Default.Schedule,
                    contentDescription = null,
                    tint = progressColor,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    warningText,
                    style = MaterialTheme.typography.bodySmall,
                    color = progressColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * TaskSection - Add to tasks functionality with Material 3 expressive styling
 *
 * Features:
 * - Enhanced task enable/disable toggle with Material 3 expressive design
 * - Priority selection (Low/Medium/High) with improved visual feedback
 * - Due time input with animated states
 * - Conditional visibility based on enabled state
 * - Animated state transitions
 * - Improved dark theme support
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
    // Animate card appearance when enabled
    val cardScale by animateFloatAsState(
        targetValue = if (isEnabled) 1f else 0.98f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "task_card_scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(cardScale),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isEnabled) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.4f)
            }
        ),
        border = BorderStroke(
            width = if (isEnabled) 2.dp else 1.dp,
            color = if (isEnabled) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            } else {
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isEnabled) 3.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Enhanced task toggle header with icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Icon that changes based on enabled state
                    Surface(
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = if (isEnabled) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceContainerHighest
                        }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                if (isEnabled) Icons.Default.AutoAwesome else Icons.Default.Schedule,
                                contentDescription = null,
                                tint = if (isEnabled) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "Add to Tasks",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (isEnabled) {
                            Text(
                                text = "Set priority and due date",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Enhanced switch with Material 3 styling
                Switch(
                    checked = isEnabled,
                    onCheckedChange = onEnabledChange,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.width(52.dp)
                )
            }

            // Animated content reveal
            if (isEnabled) {
                androidx.compose.animation.core.animateFloatAsState(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                    label = "task_content_reveal"
                )

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
 * PrioritySelector - Enhanced priority selection with Material 3 expressive styling
 */
@Composable
private fun PrioritySelector(
    selectedPriority: String,
    onPriorityChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Enhanced label with better styling
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Default.Schedule,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Priority Level",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Enhanced priority buttons with Material 3 expressive design
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val priorities = listOf(
                "Low" to MaterialTheme.colorScheme.secondaryContainer,
                "Medium" to MaterialTheme.colorScheme.tertiaryContainer,
                "High" to MaterialTheme.colorScheme.primaryContainer
            )

            priorities.forEach { (priority, containerColor) ->
                val isSelected = selectedPriority == priority
                val buttonScale by animateFloatAsState(
                    targetValue = if (isSelected) 1.05f else 1f,
                    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
                    label = "priority_${priority}_scale"
                )

                Surface(
                    onClick = { onPriorityChange(priority) },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .scale(buttonScale),
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) {
                        containerColor
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f)
                    },
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) {
                            containerColor
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        }
                    ),
                    tonalElevation = if (isSelected) 2.dp else 0.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSelected) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Text(
                                priority,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * DueTimeInput - Enhanced due time input with Material 3 expressive styling
 */
@Composable
private fun DueTimeInput(
    dueTime: String,
    onDueTimeChange: (String) -> Unit,
    isLoading: Boolean
) {
    // Animate border based on content
    val hasContent by animateFloatAsState(
        targetValue = if (dueTime.isNotBlank()) 1f else 0.6f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "due_time_has_content"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Enhanced label with icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Default.Schedule,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Due Time",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Enhanced due time input with Material 3 expressive styling
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
            border = BorderStroke(
                width = if (dueTime.isNotBlank()) 2.dp else 1.dp,
                color = if (dueTime.isNotBlank()) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                }
            ),
            tonalElevation = if (dueTime.isNotBlank()) 1.dp else 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Animated leading icon
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = null,
                    tint = if (dueTime.isNotBlank()) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    modifier = Modifier.size(20.dp)
                )

                // Text input
                Box(modifier = Modifier.weight(1f)) {
                    androidx.compose.material3.OutlinedTextField(
                        value = dueTime,
                        onValueChange = onDueTimeChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Tomorrow, Friday 3PM") },
                        enabled = !isLoading,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                            focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                // Clear button when has content
                if (dueTime.isNotBlank() && !isLoading) {
                    Surface(
                        onClick = { onDueTimeChange("") },
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Clear due time",
                                tint = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Helper text with suggestions
        if (dueTime.isBlank()) {
            Text(
                text = "Try: Tomorrow, Friday 3PM, Next Monday",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

/**
 * ErrorMessageCard - Enhanced error message display with Material 3 expressive styling
 *
 * Features:
 * - Enhanced error icon and message with Material 3 expressive design
 * - Animated appearance
 * - Improved visual feedback
 * - Optional dismiss functionality
 * - Better dark theme support
 */
@Composable
fun ErrorMessageCard(
    error: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animate card appearance
    val cardScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "error_card_scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(cardScale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f)
        ),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Enhanced error icon with animated pulse effect
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Error message with enhanced typography
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Error",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.9f)
                )
            }

            // Dismiss button
            Surface(
                onClick = onDismiss,
                modifier = Modifier.size(32.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Dismiss error",
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}