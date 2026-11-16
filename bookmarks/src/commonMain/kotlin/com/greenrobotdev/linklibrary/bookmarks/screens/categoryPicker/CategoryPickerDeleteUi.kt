package com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon

/** Red-bordered card showing the delete impact: trash icon circle, title, link count, reassurance. */
@Composable
internal fun ImpactCard(
    categoryName: String,
    linkCount: Int,
    isSwiping: Boolean,
    modifier: Modifier = Modifier,
    consequence: String = "move to No category",
) {
    val dangerFg = MaterialTheme.colorScheme.onErrorContainer
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.errorContainer),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(Icons.Filled.Delete, contentDescription = null, color = dangerFg, modifier = Modifier.size(20.dp))
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.weight(1f)) {
            Text(
                if (isSwiping) "Deleting…" else "Delete \"$categoryName\"?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = dangerFg,
            )
            if (linkCount > 0) {
                Text(
                    "$linkCount link${if (linkCount != 1) "s" else ""} $consequence",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                "The links themselves are never deleted.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** Radio choice card for how to handle a parent category's children. */
@Composable
internal fun ParentChoiceCard(
    parentName: String,
    childNames: List<String>,
    choice: ParentDeleteChoiceKind?,
    onChoice: (ParentDeleteChoiceKind) -> Unit,
    modifier: Modifier = Modifier,
) {
    val childLabel = when {
        childNames.size == 1 -> childNames.first()
        childNames.size <= 3 -> childNames.joinToString(", ")
        else -> "${childNames.take(2).joinToString(", ")} +${childNames.size - 2}"
    }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Keep children option (recommended)
        val keepSelected = choice == ParentDeleteChoiceKind.KEEP_CHILDREN
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(if (keepSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                .border(
                    1.dp,
                    if (keepSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(16.dp),
                )
                .clickable { onChoice(ParentDeleteChoiceKind.KEEP_CHILDREN) }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            RadioButton(
                selected = keepSelected,
                onClick = { onChoice(ParentDeleteChoiceKind.KEEP_CHILDREN) },
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary),
            )
            Column {
                Text(
                    "Keep $childLabel",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    "Move up a level",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // Delete all option
        val deleteAllSelected = choice == ParentDeleteChoiceKind.DELETE_ALL
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(if (deleteAllSelected) MaterialTheme.colorScheme.errorContainer else Color.Transparent)
                .border(
                    1.dp,
                    if (deleteAllSelected) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(16.dp),
                )
                .clickable { onChoice(ParentDeleteChoiceKind.DELETE_ALL) }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            RadioButton(
                selected = deleteAllSelected,
                onClick = { onChoice(ParentDeleteChoiceKind.DELETE_ALL) },
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.error),
            )
            Text(
                "Delete $parentName and $childLabel",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}
