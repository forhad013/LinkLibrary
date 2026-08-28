package com.greenrobotdev.linklibrary.bookmarks.screens.add.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.bookmarks.model.Collection

@Composable
internal fun CollectionSelector(
    collections: Set<Collection>,
    selectedCollections: Set<String>,
    onCollectionToggle: (String) -> Unit,
    onClearAllCollections: () -> Unit,
    onAddCollection: () -> Unit,
    isLoading: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    
    // Get all selected collections and display them as comma-separated list
    val selectedCollectionsList = collections.filter { collection -> selectedCollections.contains(collection.id) }
    val displayText = if (selectedCollectionsList.isEmpty()) {
        "Select collections"
    } else {
        selectedCollectionsList.joinToString(", ") { collection -> collection.name }
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
