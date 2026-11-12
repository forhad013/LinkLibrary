package com.greenrobotdev.linklibrary.design.components.advanced

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * TagChipsSelector - Advanced multi-select tag interface with inline creation
 *
 * Material 3 Expressive component for selecting and managing tags with:
 * - Multi-select functionality
 * - Inline tag creation
 * - Search and filtering
 * - Color customization
 * - Animated state transitions
 * - Accessible interactions
 */

data class TagChip(
    val id: String,
    val name: String,
    val color: Color = Color.Unspecified,
    val selected: Boolean = false,
    val usageCount: Int = 0
)

@Composable
fun TagChipsSelector(
    availableTags: List<TagChip>,
    selectedTagIds: Set<String>,
    onTagToggle: (String) -> Unit,
    onTagCreate: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Select or create tags...",
    showUsageCounts: Boolean = false,
    maxChips: Int = Int.MAX_VALUE,
    enabled: Boolean = true
) {
    var searchText by remember { mutableStateOf("") }
    var showCreateDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // Search field for filtering tags
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check, // Replace with search icon
                    contentDescription = "Search tags",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = { searchText = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            singleLine = true,
            enabled = enabled,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Selected tags display
        val selectedTags = availableTags.filter { it.id in selectedTagIds }
        if (selectedTags.isNotEmpty()) {
            Text(
                text = "Selected Tags (${selectedTags.size})",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                selectedTags.take(maxChips).forEach { tag ->
                    SelectedTagChip(
                        tag = tag,
                        onRemove = { onTagToggle(tag.id) },
                        enabled = enabled
                    )
                }

                if (selectedTags.size > maxChips) {
                    Text(
                        text = "+${selectedTags.size - maxChips} more",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Available tags section
        val filteredTags = availableTags.filter {
            it.name.contains(searchText, ignoreCase = true) && it.id !in selectedTagIds
        }

        if (filteredTags.isNotEmpty()) {
            Text(
                text = "Available Tags",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTags) { tag ->
                    AvailableTagChip(
                        tag = tag,
                        onClick = { onTagToggle(tag.id) },
                        showUsageCount = showUsageCounts,
                        enabled = enabled
                    )
                }
            }
        }

        // Create new tag suggestion
        if (searchText.isNotEmpty() && !availableTags.any {
            it.name.equals(searchText, ignoreCase = true)
        }) {
            Spacer(modifier = Modifier.height(8.dp))

            CreateTagSuggestion(
                tagName = searchText,
                onClick = {
                    onTagCreate(searchText)
                    searchText = ""
                },
                enabled = enabled
            )
        }
    }
}

@Composable
private fun SelectedTagChip(
    tag: TagChip,
    onRemove: () -> Unit,
    enabled: Boolean
) {
    var isPressed by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.95f
            isHovered -> 1.05f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "chip_scale"
    )

    val elevation by animateFloatAsState(
        targetValue = when {
            isPressed -> 0f
            isHovered -> 4f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "chip_elevation"
    )

    SuggestionChip(
        onClick = {
            isPressed = true
            onRemove()
        },
        label = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(tag.name)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove ${tag.name}",
                    modifier = Modifier.size(16.dp)
                )
            }
        },
        modifier = Modifier
            .scale(scale)
            .graphicsLayer {
                shadowElevation = elevation
            },
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = if (tag.color != Color.Unspecified) {
                tag.color
            } else {
                MaterialTheme.colorScheme.secondaryContainer
            }
        ),
        border = null
    )
}

@Composable
private fun AvailableTagChip(
    tag: TagChip,
    onClick: () -> Unit,
    showUsageCount: Boolean,
    enabled: Boolean
) {
    var isPressed by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isPressed -> MaterialTheme.colorScheme.secondaryContainer
            isHovered -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
            else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
        },
        animationSpec = tween(durationMillis = 200),
        label = "chip_background"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            isPressed -> MaterialTheme.colorScheme.onSecondaryContainer
            isHovered -> MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(durationMillis = 200),
        label = "chip_content"
    )

    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.98f
            isHovered -> 1.02f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "available_chip_scale"
    )

    Surface(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .graphicsLayer {
                shadowElevation = if (isHovered) 2f else 0f
            },
        enabled = enabled,
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isHovered) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Color indicator
                if (tag.color != Color.Unspecified) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .then(
                                Modifier.border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(50)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .then(
                                    Modifier.border(
                                        width = 1.dp,
                                        color = tag.color,
                                        shape = RoundedCornerShape(50)
                                    )
                                )
                        )
                    }
                }

                Column {
                    Text(
                        text = tag.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isPressed || isHovered) {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (showUsageCount && tag.usageCount > 0) {
                        Text(
                            text = "${tag.usageCount} links",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add ${tag.name}",
                tint = if (isPressed || isHovered) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun CreateTagSuggestion(
    tagName: String,
    onClick: () -> Unit,
    enabled: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "create_chip_scale"
    )

    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        enabled = enabled,
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create tag",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "Create \"$tagName\"",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * Compact inline version for smaller spaces
 */
@Composable
fun TagChipsSelectorInline(
    availableTags: List<TagChip>,
    selectedTagIds: Set<String>,
    onTagToggle: (String) -> Unit,
    onTagCreate: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxVisible: Int = 3,
    enabled: Boolean = true
) {
    var showAll by remember { mutableStateOf(false) }
    val selectedTags = availableTags.filter { it.id in selectedTagIds }
    val availableTagsFiltered = availableTags.filter { it.id !in selectedTagIds }

    Column(modifier = modifier) {
        // Selected tags as chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(selectedTags.take(if (showAll) Int.MAX_VALUE else maxVisible)) { tag ->
                InputChip(
                    selected = true,
                    onClick = { onTagToggle(tag.id) },
                    label = { Text(tag.name) },
                    enabled = enabled,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    shape = RoundedCornerShape(8.dp)
                )
            }

            if (selectedTags.size > maxVisible && !showAll) {
                item {
                    TextButton(
                        onClick = { showAll = true },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("+${selectedTags.size - maxVisible} more")
                    }
                }
            }
        }

        // Quick add button
        if (availableTagsFiltered.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            AssistChip(
                onClick = { /* Show tag picker */ },
                label = {
                    Text("Add tag (${availableTagsFiltered.size} available)")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                },
                enabled = enabled,
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}