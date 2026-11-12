package com.greenrobotdev.linklibrary.bookmarks.screens.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.greenrobotdev.linklibrary.design.components.icon.AppIcon
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Library tab base (design: library_categories_amp_tags):
 * smart lists with counts, categories, tags, Manage entry point.
 */
@Composable
fun LibraryTabScreen(
    onManage: () -> Unit,
    onCreateCategory: () -> Unit,
    onCategoryClick: (String) -> Unit,
) {
    val viewModel: LibraryTabViewModel = viewModel { LibraryTabViewModel() }
    val state by viewModel.models.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Library",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onManage) {
                    Icon(Icons.Default.Tune, contentDescription = "Manage")
                }
            }
        }

        // Smart lists
        item {
            ContentCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SmartListRow(Icons.Default.Inbox, "Unopened", state.unopenedCount)
                    SmartListRow(Icons.Default.Star, "Favorites", state.favoritesCount)
                    SmartListRow(Icons.Default.Archive, "Archived", state.archivedCount)
                    SmartListRow(Icons.Default.Folder, "Uncategorized", state.uncategorizedCount)
                }
            }
        }

        // Categories
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Categories", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                TextButton(onClick = onCreateCategory) { Text("+ Category") }
            }
        }
        items(state.categories) { category ->
            CategoryListRow(category, state.categories, onClick = { onCategoryClick(category.id) })
        }
        item {
            Text(
                "A category shows its own links and everything inside it.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Tags
        item {
            Text(
                state.tags.joinToString(" ") { "#${it.name}" },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun SmartListRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppIcon(icon = icon, backgroundColor = MaterialTheme.colorScheme.secondary)
        Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Text(
            "$count links",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CategoryListRow(category: Category, all: List<Category>, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppIcon(icon = Icons.Default.Folder, backgroundColor = MaterialTheme.colorScheme.primary)
        Column(Modifier.weight(1f)) {
            Text(category.name, style = MaterialTheme.typography.bodyLarge)
            val path = category.path(all)
            if (path != category.name) {
                Text(
                    path,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            "${category.count}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
