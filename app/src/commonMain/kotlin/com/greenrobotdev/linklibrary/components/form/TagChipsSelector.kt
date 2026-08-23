package com.greenrobotdev.linklibrary.components.form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TagChipsSelector(
    availableTags: List<String>,
    selectedTags: List<String>,
    onTagToggle: (String) -> Unit,
    onAddNewTag: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Selected tags
        selectedTags.forEach { tag ->
            TagChip(
                tag = tag,
                isSelected = true,
                onClick = { onTagToggle(tag) },
                onClose = { onTagToggle(tag) }
            )
        }

        // Available tags (limit to 3)
        availableTags
            .filter { it !in selectedTags }
            .take(3)
            .forEach { tag ->
                TagChip(
                    tag = tag,
                    isSelected = false,
                    onClick = { onTagToggle(tag) }
                )
            }

        // Add new tag button
        IconButton(
            onClick = onAddNewTag,
            modifier = Modifier
                .padding(4.dp)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    CircleShape
                ),
            enabled = enabled
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add new tag",
                modifier = Modifier.padding(4.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun TagChip(
    tag: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    onClose: (() -> Unit)? = null
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) {
            MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.15f)
        } else {
            Color.Transparent
        },
        border = if (isSelected) {
            BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        } else {
            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        },
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = tag,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            if (isSelected && onClose != null) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Remove tag"
                    )
                }
            }
        }
    }
}