package com.greenrobotdev.lordofthelinks.bookmarks.screens.library

import com.greenrobotdev.lordofthelinks.design.foundation.icon.AppIcon
import com.greenrobotdev.lordofthelinks.design.components.button.DashedPillButton
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import com.greenrobotdev.lordofthelinks.bookmarks.motion.sharedScope
import com.greenrobotdev.lordofthelinks.bookmarks.screens.saved.SavedScope
import com.greenrobotdev.lordofthelinks.design.components.layout.AppLazyColumn
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.greenrobotdev.lordofthelinks.model.Category
import com.greenrobotdev.lordofthelinks.design.theme.LordsExtraColors

/**
 * Library tab base (design: Library categories & tags):
 * 2x2 smart-list tiles, category tree card, tags. Manage opens the manage screen.
 */
@Composable
fun LibraryTabScreen(
    onManage: () -> Unit,
    onOpenScope: (SavedScope) -> Unit,
) {
    val viewModel: LibraryTabViewModel = viewModel { LibraryTabViewModel() }
    val state by viewModel.models.collectAsState()

    AppLazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Library",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.weight(1f)
                )
                // Same pill style as the mock's "+ Category" (creation lives in Manage)
                Text(
                    "Manage",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable(onClick = onManage)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }

        // Smart lists — 2x2 colored tiles
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    SmartTile(
                        SavedScope.Unopened,
                        "Unopened",
                        "${state.unopenedCount} links",
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.onPrimary,
                        Modifier.weight(1f),
                        onOpenScope
                    )
                    SmartTile(
                        SavedScope.Favorites,
                        "Favorites",
                        "${state.favoritesCount} links",
                        MaterialTheme.colorScheme.tertiaryContainer,
                        MaterialTheme.colorScheme.onTertiaryContainer,
                        Modifier.weight(1f),
                        onOpenScope
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    SmartTile(
                        SavedScope.Archived,
                        "Archived",
                        "${state.archivedCount} links",
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.onSecondaryContainer,
                        Modifier.weight(1f),
                        onOpenScope
                    )
                    SmartTile(
                        SavedScope.Uncategorized,
                        "Uncategorized",
                        "${state.uncategorizedCount} links",
                        LordsExtraColors.successContainer,
                        LordsExtraColors.onSuccessContainer,
                        Modifier.weight(1f),
                        onOpenScope
                    )
                }
            }
        }

        item { SectionLabel("Categories", Modifier.padding(top = 8.dp)) }

        // Category tree card, or how to make the first category
        if (state.categories.isEmpty()) {
            item {
                LibraryEmptyHint(
                    icon = Icons.Filled.FolderOpen,
                    title = "No categories yet",
                    message = "Group links by topic, like Dev › Android. Create your first category in Manage, or while saving a link.",
                    actionLabel = "Add a category",
                    onAction = onManage,
                )
            }
        }
        if (state.categories.isNotEmpty()) {
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(vertical = 6.dp)
                ) {
                    state.categories.filter { it.parentId == null }.forEach { category ->
                        CategoryTreeRow(category, state.categories, onOpen = { onOpenScope(SavedScope.InCategory(it)) })
                    }
                }
            }
        }

        // Tags — two-column grid of white pills, or how to make the first tag
        item { SectionLabel("Tags", Modifier.padding(top = 8.dp)) }
        if (state.tags.isEmpty()) {
            item {
                LibraryEmptyHint(
                    icon = Icons.Filled.Tag,
                    title = "No tags yet",
                    message = "Tags are quick labels like #kotlin or #recipes. Add them in Manage, or while saving a link.",
                    actionLabel = "Add a tag",
                    onAction = onManage,
                )
            }
        }
        if (state.tags.isNotEmpty()) {
            items(state.tags.chunked(2)) { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    pair.forEach { tag -> TagTile("#${tag.name}", Modifier.weight(1f)) { onOpenScope(SavedScope.WithTag(tag.name)) } }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
}

@Composable
private fun TagTile(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Text(
        text,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClickLabel = "Open $text", onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp)
    )
}

@Composable
private fun SmartTile(
    scope: SavedScope,
    label: String,
    countLabel: String,
    container: Color,
    content: Color,
    modifier: Modifier = Modifier,
    onOpen: (SavedScope) -> Unit,
) {
    Column(
        modifier = modifier
            .height(84.dp)
            // Grows into the header of the list it opens
            .sharedScope(scope.key)
            .clip(RoundedCornerShape(20.dp))
            .background(container)
            .clickable(onClickLabel = "Open $label") { onOpen(scope) }
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = content)
        Text(countLabel, style = MaterialTheme.typography.bodySmall, color = content)
    }
}

@Composable
private fun CategoryTreeRow(
    category: Category,
    all: List<Category>,
    onOpen: (String) -> Unit,
    depth: Int = 0,
) {
    val children = all.filter { it.parentId == category.id }
    var expanded by rememberSaveable(category.id) { mutableStateOf(false) }

    Column(Modifier.animateContentSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                // The row opens the category's saves; the chevron expands its subcategories
                .clickable(onClickLabel = "Open ${category.name}") { onOpen(category.id) }
                .padding(start = (2 + depth * 20).dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Chevron gutter: always reserved so leaf names line up with their siblings
            Box(
                Modifier.size(44.dp).clip(CircleShape)
                    .then(if (children.isNotEmpty()) Modifier.clickable { expanded = !expanded } else Modifier),
                contentAlignment = Alignment.Center,
            ) {
                if (children.isNotEmpty()) {
                    Icon(
                        if (expanded) Icons.Default.KeyboardArrowDown else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "${category.name}, ${if (expanded) "expanded" else "collapsed"}",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Text(
                category.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(
                "[${category.count}]",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (expanded) {
            children.forEach { child -> CategoryTreeRow(child, all, onOpen, depth + 1) }
        }
    }
}

/** An empty Library section: what it's for, and a way to start it (Manage). */
@Composable
private fun LibraryEmptyHint(icon: ImageVector, title: String, message: String, actionLabel: String, onAction: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                AppIcon(icon, contentDescription = null, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
                Text(message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        DashedPillButton(text = actionLabel, onClick = onAction)
    }
}
