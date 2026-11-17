package com.greenrobotdev.linklibrary.bookmarks.screens.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Manage categories and tags (design: Manage categories & tags).
 * Categories/Tags toggle, pill rows; selecting a row shows a bottom action bar
 * (Rename / Move / Delete). Delete confirms; links are never deleted.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageScreen(onBack: () -> Unit, onMoveCategory: (String) -> Unit = {}) {
    val viewModel: ManageViewModel = viewModel { ManageViewModel() }
    val state by viewModel.models.collectAsState()

    val showCategories = state.categoriesVisible
    val selectedName = state.categories.firstOrNull { it.id == state.selectedCategoryId }?.name
        ?: state.tags.firstOrNull { it.id == state.selectedTagId }?.name
    val hasSelection = selectedName != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            if (hasSelection) {
                BottomActionBar(
                    label = selectedName ?: "",
                    isCategory = state.selectedCategoryId != null,
                    onRename = { viewModel.take(ManageEvent.StartRename) },
                    onMove = { state.selectedCategoryId?.let(onMoveCategory) },
                    onDelete = {
                        if (state.selectedCategoryId != null) {
                            viewModel.take(ManageEvent.DeleteCategory(state.selectedCategoryId!!))
                        } else {
                            viewModel.take(ManageEvent.DeleteTag(state.selectedTagId!!))
                        }
                    }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
            // Toggle pills
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TogglePill(
                    "Categories",
                    active = showCategories,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.take(ManageEvent.ShowCategories()) }
                )
                TogglePill(
                    "Tags",
                    active = !showCategories,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.take(ManageEvent.ShowTags) }
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val rows = if (showCategories) state.categories.map { Triple(it.id, it.name, it.count) }
                else state.tags.map { Triple(it.id, it.name, it.count) }
                items(rows) { (id, name, count) ->
                    val selectedId = if (showCategories) state.selectedCategoryId else state.selectedTagId
                    ManageRow(
                        label = name,
                        count = count,
                        selected = selectedId == id,
                        onClick = {
                            val next = if (selectedId == id) null else id
                            if (showCategories) viewModel.take(ManageEvent.SelectCategory(next))
                            else viewModel.take(ManageEvent.SelectTag(next))
                        }
                    )
                }
                item {
                    Text(
                        if (showCategories) "Categories can be nested up to 5 levels. Sorted by name."
                        else "Tags sorted by name.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                    )
                }
            }
        }
    }

    // Rename dialog
    if (state.renaming != null) {
        val target = state.renaming!!
        var nameField by remember(target) { mutableStateOf(target.second) }
        AlertDialog(
            onDismissRequest = { viewModel.take(ManageEvent.CancelRename) },
            title = { Text("Rename \"${target.second}\"") },
            text = {
                OutlinedTextField(
                    value = nameField,
                    onValueChange = { nameField = it },
                    singleLine = true,
                    label = { Text("New name") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (state.selectedCategoryId != null) {
                            viewModel.take(ManageEvent.RenameCategory(target.first, nameField.trim()))
                        } else {
                            viewModel.take(ManageEvent.RenameTag(target.first, nameField.trim()))
                        }
                    },
                    enabled = nameField.isNotBlank()
                ) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.take(ManageEvent.CancelRename) }) { Text("Cancel") }
            }
        )
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
private fun TogglePill(label: String, active: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .background(
                if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer,
                RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ManageRow(label: String, count: Int, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            "[$count]",
            style = MaterialTheme.typography.bodySmall,
            color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun BottomActionBar(
    label: String,
    isCategory: Boolean,
    onRename: () -> Unit,
    onMove: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .background(MaterialTheme.colorScheme.inverseSurface, RoundedCornerShape(28.dp))
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.inverseOnSurface,
            modifier = Modifier.weight(1f),
            maxLines = 1
        )
        TextButton(onClick = onRename) { Text("Rename", color = MaterialTheme.colorScheme.inverseOnSurface) }
        if (isCategory) {
            TextButton(onClick = onMove) { Text("Move", color = MaterialTheme.colorScheme.inverseOnSurface) }
        }
        TextButton(
            onClick = onDelete,
            modifier = Modifier.background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(50))
        ) { Text("Delete", color = MaterialTheme.colorScheme.onTertiary) }
    }
}
