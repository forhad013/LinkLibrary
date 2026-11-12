package com.greenrobotdev.linklibrary.bookmarks.screens.add.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import com.greenrobotdev.linklibrary.design.components.appbar.AppTopBar
import com.greenrobotdev.linklibrary.design.components.button.IconButton
import com.greenrobotdev.linklibrary.design.components.cards.FormCard
import com.greenrobotdev.linklibrary.design.components.cards.InputCard
import com.greenrobotdev.linklibrary.design.components.cards.ToggleCard
import com.greenrobotdev.linklibrary.design.components.progress.AppCircularProgressSmall
import com.greenrobotdev.linklibrary.design.components.selection.AddPill
import com.greenrobotdev.linklibrary.design.components.selection.SelectionPill
import com.greenrobotdev.linklibrary.design.components.textfield.AppTextFieldEnhanced
import com.greenrobotdev.linklibrary.design.components.textfield.AppTextFieldWithCharacterLimit
import com.greenrobotdev.linklibrary.design.theme.ComponentSize
import com.greenrobotdev.linklibrary.design.theme.ComponentStyle
import com.greenrobotdev.linklibrary.design.components.feedback.ErrorMessageCard as DesignErrorMessageCard
import com.greenrobotdev.linklibrary.design.components.selection.PrioritySelector as DesignPrioritySelector

/**
 * AddLinkTopBar - Standardized top app bar for Add Link screen
 *
 * Now uses AppTopBar from design module for consistent styling across all screens.
 *
 * Features:
 * - Consistent Material 3 Expressive styling
 * - Standard navigation and action buttons
 * - Proper dark theme support
 * - Unified appearance with other form screens
 */
@Composable
fun AddLinkTopBar(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    isFormValid: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    AppTopBar(
        title = "Add New Link",
        onNavigationClick = onBackClick,
        onActionClick = onSaveClick,
        modifier = modifier,
        actionText = "Save",
        isActionEnabled = isFormValid,
        isLoading = isLoading,
        navigationIcon = Icons.Default.Close
    )
}

/**
 * UrlTitleCard - Combined URL and Title input card using design module template
 *
 * Features:
 * - Single card containing both URL and Title inputs
 * - Uses ContentCard template for consistent Material 3 Expressive styling
 * - Enhanced contrast with atmospheric layering
 * - Integrated auto-fetch button with loading states
 */
@Composable
fun UrlTitleCard(
    url: String,
    onUrlChange: (String) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    onFetchClick: () -> Unit,
    isFetching: Boolean,
    fetchError: String?,
    titleFetched: Boolean,
    urlNotEmpty: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    InputCard(
        modifier = modifier
    ) {
        // URL Input Section
        UrlInputSection(
            url = url,
            onUrlChange = onUrlChange,
            onFetchClick = onFetchClick,
            isFetching = isFetching,
            fetchError = fetchError,
            titleFetched = titleFetched,
            urlNotEmpty = urlNotEmpty,
            isLoading = isLoading
        )

        // Title Input Section
        TitleInputSection(
            title = title,
            onTitleChange = onTitleChange,
            isLoading = isLoading
        )
    }
}

/**
 * UrlInputSection - URL input with integrated auto-fetch functionality
 *
 * DESIGN DECISION: This component preserves custom implementation due to complexity
 *
 * Why this component was NOT migrated to standardized components:
 * - Integrated auto-fetch button with multiple states (Loading, Success, Error, Idle)
 * - Complex state management (fetchError, titleFetched, urlNotEmpty, isFetching, isLoading)
 * - Dynamic accent colors based on multiple state conditions
 * - Unique UI pattern: leading icon + text field + integrated button in single Surface
 * - Conditional helper text with progress indicators
 *
 * Standardization achieved:
 * - Progress indicators migrated to AppCircularProgressSmall ✅
 * - Consistent Material 3 Expressive styling maintained ✅
 *
 * Future consideration: Could be candidate for AppTextFieldWithButton component
 *
 * Features:
 * - Clean URL input without image preview
 * - Integrated auto-fetch button with loading states
 * - Dynamic border colors based on fetch state
 * - Helper text with progress/error indicators
 */
@Composable
private fun UrlInputSection(
    url: String,
    onUrlChange: (String) -> Unit,
    onFetchClick: () -> Unit,
    isFetching: Boolean,
    fetchError: String?,
    titleFetched: Boolean,
    urlNotEmpty: Boolean,
    isLoading: Boolean
) {
    val accentColor = when {
        fetchError != null -> MaterialTheme.colorScheme.error
        titleFetched && urlNotEmpty -> MaterialTheme.colorScheme.primary
        isFetching -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // URL label matching Vibrant Citadel Dark design
        Text(
            text = "URL",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 4.dp)
        )

        // Enhanced URL input field with Vibrant Citadel Dark styling
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            border = BorderStroke(
                width = 1.dp,
                color = when {
                    fetchError != null -> MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                    titleFetched && urlNotEmpty -> MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                }
            ),
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Leading icon
                Icon(
                    Icons.Default.Link,
                    contentDescription = null,
                    tint = when {
                        fetchError != null -> MaterialTheme.colorScheme.error
                        titleFetched && urlNotEmpty -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(20.dp)
                )

                // Text input
                Box(modifier = Modifier.weight(1f)) {
                    androidx.compose.material3.OutlinedTextField(
                        value = url,
                        onValueChange = onUrlChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("https://example.com/article") },
                        enabled = !isLoading,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                            focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.secondary,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                // Integrated auto-fetch button
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
                        modifier = Modifier.size(36.dp),
                        shape = RoundedCornerShape(18.dp),
                        color = when (fetchState) {
                            FetchState.Success -> MaterialTheme.colorScheme.primaryContainer
                            FetchState.Error -> MaterialTheme.colorScheme.errorContainer
                            FetchState.Loading -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            else -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                        },
                        tonalElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            when (fetchState) {
                                FetchState.Loading -> {
                                    AppCircularProgressSmall(modifier = Modifier.size(18.dp))
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
                }
            }
        }

        // Helper text matching Vibrant Citadel Dark design
        when {
            isFetching -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    AppCircularProgressSmall(modifier = Modifier.size(12.dp))
                    Text(
                        "Fetching metadata...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            fetchError != null -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
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
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            titleFetched && urlNotEmpty -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
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
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * TitleInputSection - Title input using enhanced standardized component
 *
 * Features:
 * - Clean title input using AppTextFieldEnhanced
 * - Material 3 Expressive styling
 * - Consistent appearance across form screens
 */
@Composable
private fun TitleInputSection(
    title: String,
    onTitleChange: (String) -> Unit,
    isLoading: Boolean
) {
    AppTextFieldEnhanced(
        value = title,
        onValueChange = onTitleChange,
        modifier = Modifier.fillMaxWidth(),
        label = "Title",
        placeholder = "Link Title",
        singleLine = true,
        enabled = !isLoading
    )
}

/**
 * OrganizationSection - Collection and tags selection using FormCard from design module
 *
 * Features:
 * - Material 3 Expressive styling with FormCard wrapper
 * - 24px padding matching "spirited padding" design
 * - Pill-shaped tags with proper selection states
 * - Tonal stacking instead of harsh borders
 * - Enhanced contrast and atmospheric layering
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
    FormCard(
        modifier = modifier,
        headerIcon = Icons.Default.Folder,
        headerTitle = "Organization",
    ) {
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

                    // Add Collection Button - using standardized IconButton
                    IconButton(
                        onClick = onAddCollection,
                        icon = Icons.Default.Add,
                        contentDescription = "Add collection",
                        style = ComponentStyle.Filled,
                        size = ComponentSize.Medium,
                        colors = com.greenrobotdev.linklibrary.design.components.button.ComponentColors(
                            container = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                            content = MaterialTheme.colorScheme.onPrimary
                        ),
                        enabled = !isLoading
                    )
                }
            }
        }
    }
}

/**
 * TagsSection - Tags display using standardized SelectionPill components
 *
 * Features:
 * - Material 3 Expressive styling using SelectionPill from design module
 * - Consistent pill appearance and behavior
 * - Proper selection states with visual feedback
 * - Add new tag functionality with AddPill component
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
            style = MaterialTheme.typography.labelMedium,
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

            // Show tags as standardized pill-shaped buttons
            else -> {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    availableTags.forEach { tag ->
                        val isSelected = selectedTags.contains(tag.id)

                        SelectionPill(
                            selected = isSelected,
                            onClick = { onTagToggle(tag.id) },
                            text = tag.name,
                            selectedIcon = Icons.Default.Schedule
                        )
                    }

                    // Add New Tag button
                    AddPill(
                        onClick = onAddTag,
                        text = "New Tag"
                    )
                }
            }
        }
    }
}

/**
 * PersonalNotesSection - Personal notes using enhanced standardized component
 *
 * Features:
 * - Material 3 Expressive styling using AppTextFieldWithCharacterLimit
 * - Automatic character counting and limit enforcement
 * - Dynamic warning indicators based on character usage
 * - Consistent appearance across form screens
 */
@Composable
fun PersonalNotesSection(
    notes: String,
    onNotesChange: (String) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    maxLength: Int = 500
) {
    FormCard(
        modifier = modifier,
        headerIcon = Icons.Default.AutoAwesome,
        headerTitle = "Personal Notes",
    ) {
        AppTextFieldWithCharacterLimit(
            value = notes,
            onValueChange = onNotesChange,
            maxLength = maxLength,
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Why are you saving this link? Add personal thoughts or highlights here...",
            singleLine = false,
            minLines = 3,
            maxLines = 5,
            enabled = !isLoading
        )
    }
}

/**
 * TaskSection - Add to tasks functionality using ToggleCard from design module
 *
 * Features:
 * - Material 3 Expressive styling with ToggleCard wrapper
 * - Priority selection (Low/Medium/High) with improved visual feedback
 * - Due time input with animated states
 * - Conditional visibility based on enabled state
 * - Animated state transitions
 * - Improved dark theme support with enhanced contrast
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
    ToggleCard(
        modifier = modifier,
        isEnabled = isEnabled,
        onEnabledChange = onEnabledChange,
        title = "Add to Tasks",
        description = "Set priority and due date",
        enabledIcon = Icons.Default.AutoAwesome,
        disabledIcon = Icons.Default.Schedule,
        activeColor = MaterialTheme.colorScheme.primary
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Priority Selection using design module component
            DesignPrioritySelector(
                options = listOf("Low", "Medium", "High"),
                selectedOption = selectedPriority,
                onOptionSelected = onPriorityChange,
                optionColors = listOf(
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.tertiaryContainer,
                    MaterialTheme.colorScheme.primaryContainer
                ),
                enabled = !isLoading
            )

            // Due Time
            DueTimeInput(
                dueTime = dueTime,
                onDueTimeChange = onDueTimeChange,
                isLoading = isLoading
            )
        }
    }
}

/**
 * DueTimeInput - Enhanced due time input using standardized components
 *
 * Features:
 * - Material 3 Expressive styling using AppTextFieldEnhanced
 * - Integrated leading icon functionality
 * - Standardized clear button using IconButton from design module
 * - Helper text for suggestions
 */
@Composable
private fun DueTimeInput(
    dueTime: String,
    onDueTimeChange: (String) -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Enhanced due time input with leading icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                AppTextFieldEnhanced(
                    value = dueTime,
                    onValueChange = onDueTimeChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = "Due Time",
                    placeholder = "e.g., Tomorrow, Friday 3PM",
                    leadingIcon = Icons.Default.Schedule,
                    singleLine = true,
                    enabled = !isLoading
                )
            }

            // Clear button when has content - using standardized IconButton
            if (dueTime.isNotBlank() && !isLoading) {
                IconButton(
                    onClick = { onDueTimeChange("") },
                    icon = Icons.Default.Close,
                    contentDescription = "Clear due time",
                    style = ComponentStyle.Filled,
                    size = ComponentSize.Small,
                    colors = com.greenrobotdev.linklibrary.design.components.button.ComponentColors(
                        container = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                        content = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                    )
                )
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
 * ErrorMessageCard - Wrapper using design module component
 */
@Composable
fun ErrorMessageCard(
    error: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    DesignErrorMessageCard(
        error = error,
        onDismiss = onDismiss,
        modifier = modifier
    )
}