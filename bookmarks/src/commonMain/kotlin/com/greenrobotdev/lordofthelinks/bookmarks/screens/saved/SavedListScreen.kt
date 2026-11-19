package com.greenrobotdev.lordofthelinks.bookmarks.screens.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.greenrobotdev.lordofthelinks.bookmarks.model.Category
import com.greenrobotdev.lordofthelinks.bookmarks.model.Link
import com.greenrobotdev.lordofthelinks.bookmarks.motion.sharedScope
import com.greenrobotdev.lordofthelinks.bookmarks.screens.library.FeedLinkCard
import com.greenrobotdev.lordofthelinks.bookmarks.screens.library.FeedSectionHeader
import com.greenrobotdev.lordofthelinks.bookmarks.screens.library.SwipeableFeedCard
import com.greenrobotdev.lordofthelinks.bookmarks.screens.library.feedSection
import com.greenrobotdev.lordofthelinks.design.components.layout.AppLazyColumn
import com.greenrobotdev.lordofthelinks.design.components.layout.AppLazyRow
import com.greenrobotdev.lordofthelinks.design.foundation.icon.AppIcon
import com.greenrobotdev.lordofthelinks.design.theme.LordsExtraColors
import kotlinx.coroutines.launch

/** Colors and icon a list's header shares with the Library tile it opens from. */
data class ScopeLook(val container: Color, val content: Color, val icon: ImageVector)

@Composable
fun SavedScope.look(): ScopeLook = when (this) {
    SavedScope.Unopened -> ScopeLook(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary, Icons.Filled.Inbox)
    SavedScope.Favorites -> ScopeLook(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer, Icons.Filled.Star)
    SavedScope.Archived -> ScopeLook(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer, Icons.Filled.Archive)
    SavedScope.Uncategorized -> ScopeLook(LordsExtraColors.successContainer, LordsExtraColors.onSuccessContainer, Icons.Filled.Label)
    is SavedScope.InCategory -> ScopeLook(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer, Icons.Filled.FolderOpen)
    is SavedScope.WithTag -> ScopeLook(MaterialTheme.colorScheme.surfaceContainerHigh, MaterialTheme.colorScheme.onSurface, Icons.Filled.Tag)
}

/**
 * One Library list: a colored header (grown from the tile that opened it), subcategory chips for a
 * category, then the links in date sections with the Feed's cards and swipes.
 */
@Composable
fun SavedListScreen(
    scope: SavedScope,
    onBack: () -> Unit,
    onOpenScope: (SavedScope) -> Unit,
    onOpenDetails: (String) -> Unit,
) {
    val viewModel: SavedListViewModel = viewModel(key = "saved-${scope.key}") { SavedListViewModel() }
    val state by viewModel.models.collectAsState()
    val uriHandler = LocalUriHandler.current
    val links = state.visible(scope)

    val snackbar = remember { SnackbarHostState() }
    val coroutines = rememberCoroutineScope()
    var pendingDelete by remember { mutableStateOf<Link?>(null) }
    fun archive(link: Link) {
        viewModel.take(SavedListEvent.Archive(link.id))
        coroutines.launch {
            snackbar.currentSnackbarData?.dismiss()
            if (snackbar.showSnackbar("Link archived", "Undo", duration = SnackbarDuration.Short) == SnackbarResult.ActionPerformed) {
                viewModel.take(SavedListEvent.Restore(link))
            }
        }
    }
    pendingDelete?.let { link ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text("Delete this link?") },
            text = { Text("It is removed with its note, tags and category. This can't be undone.") },
            confirmButton = {
                TextButton(onClick = { pendingDelete = null; viewModel.take(SavedListEvent.Delete(link.id)) }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { pendingDelete = null }) { Text("Cancel") } },
        )
    }

    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        AppLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(key = "header") { ScopeHeader(scope, state, links.size, onBack, onOpenScope) }

            if (!state.isLoading && links.isEmpty()) {
                item(key = "empty") { EmptyScope(scope) }
            }
            // Sorted newest first, so each date section is one consecutive run
            links.groupBy { feedSection(it.createdAt) }.forEach { (section, sectionLinks) ->
                item(key = "section-$section") {
                    FeedSectionHeader(section, Modifier.padding(horizontal = 16.dp).animateItem())
                }
                items(sectionLinks, key = { it.id }) { link ->
                    SwipeableFeedCard(
                        link = link,
                        onArchive = { archive(link) },
                        onRequestDelete = { pendingDelete = link },
                        modifier = Modifier.padding(horizontal = 16.dp).animateItem(),
                    ) {
                        FeedLinkCard(
                            link = link,
                            categoryPath = state.categoryOf(link)?.path(state.categories),
                            onOpen = {
                                runCatching { uriHandler.openUri(link.url) }
                                viewModel.take(SavedListEvent.Opened(link.id))
                            },
                            onDetails = { onOpenDetails(link.id) },
                        )
                    }
                }
            }
        }
        SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter).padding(16.dp))
    }
}

@Composable
private fun ScopeHeader(
    scope: SavedScope,
    state: SavedListState,
    count: Int,
    onBack: () -> Unit,
    onOpenScope: (SavedScope) -> Unit,
) {
    val look = scope.look()
    val category = (scope as? SavedScope.InCategory)?.let { s -> state.categories.firstOrNull { it.id == s.categoryId } }
    val children = category?.let { c -> state.categories.filter { it.parentId == c.id } }.orEmpty()
    val parentPath = category?.parentId?.let { id -> state.categories.firstOrNull { it.id == id } }?.path(state.categories)
    val ownCount = category?.let { c -> state.links.count { state.linkCategoryIds[it.id] == c.id } }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            // Grows out of the Library tile it was opened from
            .sharedScope(scope.key)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(look.container)
            .padding(start = 8.dp, end = 20.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier.size(48.dp).clip(CircleShape).clickable(onClickLabel = "Back", onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", color = look.content)
        }
        Column(Modifier.padding(start = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            parentPath?.let {
                Text("$it ›", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = look.content.copy(alpha = 0.8f))
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AppIcon(look.icon, contentDescription = null, color = look.content, modifier = Modifier.size(28.dp))
                Text(
                    scope.title(state.categories),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = look.content,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.semantics { heading() },
                )
            }
            Text(
                buildString {
                    append(if (count == 1) "1 save" else "$count saves")
                    // The category tree counts only a category's own links; say where the rest come from
                    if (ownCount != null && count > ownCount) append(" · ${count - ownCount} in subcategories")
                },
                style = MaterialTheme.typography.bodyMedium,
                color = look.content.copy(alpha = 0.85f),
            )
        }
        if (children.isNotEmpty()) {
            AppLazyRow(
                modifier = Modifier.padding(top = 12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(children, key = { it.id }) { child -> SubcategoryChip(child, state, look) { onOpenScope(SavedScope.InCategory(child.id)) } }
            }
        }
    }
}

/** A subcategory to drill into, with how many saves it holds (its own subcategories included). */
@Composable
private fun SubcategoryChip(category: Category, state: SavedListState, look: ScopeLook, onClick: () -> Unit) {
    val count = state.visible(SavedScope.InCategory(category.id)).size
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClickLabel = "Open ${category.name}", onClick = onClick)
            .heightIn(min = 44.dp)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AppIcon(Icons.Filled.FolderOpen, contentDescription = null, color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        Text(category.name, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Text("$count", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun EmptyScope(scope: SavedScope) {
    val look = scope.look()
    val (title, message) = scope.emptyMessage()
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(RoundedCornerShape(topStart = 44.dp, topEnd = 20.dp, bottomEnd = 44.dp, bottomStart = 20.dp))
                .background(look.container),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(look.icon, contentDescription = null, color = look.content, modifier = Modifier.size(40.dp))
        }
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
        Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
    }
}
