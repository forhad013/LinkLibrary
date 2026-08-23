package com.greenrobotdev.linklibrary.components.form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun CollectionDropdown(
    selectedCollection: String?,
    availableCollections: List<String>,
    onCollectionSelected: (String?) -> Unit,
    onAddNewCollection: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "Select a collection"
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedCollection ?: "",
            onValueChange = { },
            readOnly = true,
            placeholder = { Text(placeholder) },
            trailingIcon = {
                IconButton(onClick = onAddNewCollection) {
                    Icon(Icons.Default.Add, contentDescription = "Add new collection")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            maxLines = 1
        )

        // Dropdown menu would be implemented here
        // For now, this is a placeholder that shows the concept
        // In a full implementation, you'd use ExposedDropdownMenuBox
    }
}

@Composable
fun CollectionSelectorRow(
    selectedCollection: String?,
    availableCollections: List<String>,
    onCollectionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    // Simple row-based collection selector as an alternative to dropdown
    androidx.compose.foundation.lazy.LazyRow(
        modifier = modifier,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        item {
            CollectionChip(
                name = "All",
                isSelected = selectedCollection == null,
                onClick = { onCollectionSelected("") },
                enabled = enabled
            )
        }
        items(availableCollections.size) { index ->
            val collection = availableCollections[index]
            CollectionChip(
                name = collection,
                isSelected = selectedCollection == collection,
                onClick = { onCollectionSelected(collection) },
                enabled = enabled
            )
        }
    }
}

@Composable
private fun CollectionChip(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean
) {
    androidx.compose.material3.Surface(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.small,
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = if (isSelected) {
            null
        } else {
            androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant
            )
        }
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}