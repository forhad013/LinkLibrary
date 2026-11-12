package com.greenrobotdev.linklibrary.bookmarks.screens.manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DriveFileMove
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.greenrobotdev.linklibrary.bookmarks.model.Category
import com.greenrobotdev.linklibrary.bookmarks.model.Tag
import com.greenrobotdev.linklibrary.design.components.icon.AppIcon

/**
 * Manage categories and tags (design: manage_categories_amp_tags).
 * Select a row, then Rename / Move / Delete. Delete asks for confirmation;
 * links are never deleted.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageScreen(onBack: () -> Unit) {
    val viewModel: ManageViewModel = viewModel { ManageViewModel() }
    val state by viewModel.models.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Text("Categories", style = MaterialTheme.typography.titleMedium) }
            items(state.categories) { category ->
                ManageRow(
                    label = category.name,
                    count = category.count,
                    icon = Icons.Default.Folder,
                    selected = state.selectedCategoryId == category.id,
                    onClick = { viewModel.take(ManageEvent.SelectCategory(if (state.selectedCategoryId == category.id) null else category.id)) },
                    onRename = { viewModel.take(ManageEvent.RenameCategory(category.id, it)) },
                    onMove = { viewModel.take(ManageEvent.MoveCategory(category.id, it)) },
                    onDelete = { viewModel.take(ManageEvent.DeleteCategory(category.id)) }
                )
            }
            item { Text("Tags", style = MaterialTheme.typography.titleMedium) }
            items(state.tags) { tag ->
                ManageRow(
                    label = tag.name,
                    count = tag.count,
                    icon = Icons.Default.Label,
                    selected = state.selectedTagId == tag.id,
                    onClick = { viewModel.take(ManageEvent.SelectTag(if (state.selectedTagId == tag.id) null else tag.id)) },
                    onRename = { viewModel.take(ManageEvent.RenameTag(tag.id, it)) },
                    onMove = null, // tags have no hierarchy
                    onDelete = { viewModel.take(ManageEvent.DeleteTag(tag.id)) }
                )
            }
        }
    }

    // Delete confirmation
    state.deleteTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { viewModel.take(ManageEvent.CancelDelete) },
            title = { Text("Delete \"${target.second}\"?") },
            text = { Text("No links are deleted. They just lose this ${target.first}.") },
            confirmButton = {
                TextButton(onClick = { viewModel.take(ManageEvent.ConfirmDelete) }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.take(ManageEvent.CancelDelete) }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun ManageRow(
    label: String,
    count: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    onRename: (String) -> Unit,
    onMove: ((String?) -> Unit)?,
    onDelete: () -> Unit,
) {
    var renaming by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var moving by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var nameField by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(label) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppIcon(icon = icon, backgroundColor = MaterialTheme.colorScheme.primary)
            Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Text(
                "$count",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (selected) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = { renaming = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Rename")
                }
                if (onMove != null) {
                    IconButton(onClick = { moving = true }) {
                        Icon(Icons.AutoMirrored.Filled.DriveFileMove, contentDescription = "Move")
                    }
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
            if (renaming) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = nameField,
                        onValueChange = { nameField = it },
                        singleLine = true,
                        label = { Text("New name") },
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = { onRename(nameField.trim()); renaming = false },
                        enabled = nameField.isNotBlank()
                    ) { Text("Save") }
                }
            }
            if (moving && onMove != null) {
                Text(
                    "Move: pick the new parent in the category picker (next step)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // shortcut: inline parent picker not built yet; move-to-root for now
                TextButton(onClick = { onMove(null); moving = false }) { Text("Move to top level") }
            }
        }
    }
}
