package com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreateNewFolder
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
import androidx.compose.runtime.LaunchedEffect
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
import com.greenrobotdev.linklibrary.design.components.cards.ContentCard
import com.greenrobotdev.linklibrary.design.components.feedback.ErrorMessageCard
import com.greenrobotdev.linklibrary.design.components.icon.AppIcon
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Category picker (GRE-44): single selection with breadcrumb path,
 * "New" creates a category inside the selected parent, depth ≤ 5 enforced by DAO.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPickerScreen(
    initialSelection: String? = null,
    onDone: (categoryId: String?) -> Unit,
    onBack: () -> Unit = {},
) {
    val viewModel: CategoryPickerViewModel = viewModel { CategoryPickerViewModel() }
    val state by viewModel.models.collectAsState()
    val events = remember { MutableSharedFlow<CategoryPickerEvent>() }
    var createForParent by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(initialSelection) {
        if (initialSelection != null) events.emit(CategoryPickerEvent.Select(initialSelection))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Category") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { onDone(state.selectedCategoryId) }) { Text("Done") }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    state.selectedPath ?: "One per link. Up to 5 levels deep.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            state.error?.let { error ->
                item { ErrorMessageCard(error = error, onDismiss = { events.tryEmit(CategoryPickerEvent.ClearError) }) }
            }

            items(state.categories) { category ->
                CategoryRow(
                    category = category,
                    all = state.categories,
                    isSelected = category.id == state.selectedCategoryId,
                    onClick = {
                        events.tryEmit(
                            CategoryPickerEvent.Select(
                                if (state.selectedCategoryId == category.id) null else category.id
                            )
                        )
                    },
                    onNewInside = { createForParent = category.id }
                )
            }

            item {
                TextButton(onClick = { createForParent = null }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("New top-level category")
                }
            }
        }

        createForParent?.let { parentId ->
            NewCategoryDialog(
                parentPath = state.categories.firstOrNull { it.id == parentId }?.path(state.categories),
                onDismiss = { createForParent = null },
                onCreate = { name ->
                    events.tryEmit(CategoryPickerEvent.CreateCategory(name, parentId))
                    createForParent = null
                }
            )
        }
    }
}

@Composable
private fun CategoryRow(
    category: Category,
    all: List<Category>,
    isSelected: Boolean,
    onClick: () -> Unit,
    onNewInside: () -> Unit,
) {
    ContentCard(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppIcon(
                icon = Icons.Default.CreateNewFolder,
                backgroundColor = MaterialTheme.colorScheme.primary
            )
            Column(Modifier.weight(1f)) {
                Text(category.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    category.path(all),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                "${category.count}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = "Selected", tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onNewInside) {
                Icon(Icons.Default.Add, contentDescription = "New category inside ${category.name}")
            }
        }
    }
}

@Composable
private fun NewCategoryDialog(
    parentPath: String?,
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        ContentCard {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    if (parentPath != null) "New category in $parentPath" else "New category",
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(Modifier.weight(1f))
                    TextButton(
                        onClick = { if (name.isNotBlank()) onCreate(name.trim()) },
                        enabled = name.isNotBlank()
                    ) { Text("Create") }
                }
            }
        }
    }
}
