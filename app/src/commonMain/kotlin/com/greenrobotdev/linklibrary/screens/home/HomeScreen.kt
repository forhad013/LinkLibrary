package com.greenrobotdev.linklibrary.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.linklibrary.bookmarks.model.Link
import com.greenrobotdev.linklibrary.design.components.button.IconButton as DesignIconButton
import com.greenrobotdev.linklibrary.design.components.card.ElevatedCard
import com.greenrobotdev.linklibrary.design.components.progress.CircularProgress
import com.greenrobotdev.linklibrary.design.components.textfield.OutlinedTextField
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon
import com.greenrobotdev.linklibrary.design.foundation.layout.HorizontalDivider
import com.greenrobotdev.linklibrary.design.foundation.layout.MediumVerticalSpacer
import com.greenrobotdev.linklibrary.design.foundation.layout.SmallVerticalSpacer
import com.greenrobotdev.linklibrary.design.foundation.text.AppText

@Composable
fun HomeScreen(
    routeKey: NavKey,
    onNavigateToDetail: (String) -> Unit,
    onAddLink: (String?) -> Unit
) {
    val viewModel: HomeViewModel = viewModel<HomeViewModel>(key = routeKey.toString()) { HomeViewModel() }
    val state by viewModel.models.collectAsState()

    HomeView(
        state = state,
        onRefresh = viewModel::onRefresh,
        onToggleFavorite = viewModel::onToggleFavorite,
        onLinkClick = onNavigateToDetail,
        onAddLink = onAddLink
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeView(
    state: HomeState,
    onRefresh: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    onLinkClick: (String) -> Unit,
    onAddLink: (String?) -> Unit
) {
    LaunchedEffect(Unit) {
        onRefresh()
    }

    // View mode state
    var isGridView by remember { mutableStateOf(true) }
    var showSortMenu by remember { mutableStateOf(false) }
    var selectedSort by remember { mutableStateOf("Recent") }

    Scaffold(
        topBar = {
            // Modern search bar as top bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { /* TODO: Handle search */ },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Search links...",
                    leadingIcon = Icons.Default.Search
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddLink(null) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Article,
                    contentDescription = "Add new link"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Quick Access Section
            QuickAccessSection(
                onAddLink = { onAddLink(null) },
                onViewCategories = { /* TODO: Navigate to categoryNames */ },
                onViewTags = { /* TODO: Navigate to tags */ },
                onOpenSettings = { /* TODO: Navigate to settings */ }
            )

            SmallVerticalSpacer()

            // Filter and View Controls
            FilterAndControlsSection(
                isGridView = isGridView,
                onViewModeToggle = { isGridView = !isGridView },
                showSortMenu = showSortMenu,
                onSortMenuToggle = { showSortMenu = !showSortMenu },
                selectedSort = selectedSort,
                onSortSelected = {
                    selectedSort = it
                    showSortMenu = false
                },
                onSortMenuDismiss = { showSortMenu = false }
            )

            // Content
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    state.isLoading && state.links.isNullOrEmpty() -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgress()
                    }

                    state.error != null -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "Something went wrong",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = state.error,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    state.links.isNullOrEmpty() -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            AppIcon(
                                imageVector = Icons.AutoMirrored.Filled.Article,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp)
                            )
                            MediumVerticalSpacer()
                            AppText(
                                text = "No links yet",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            SmallVerticalSpacer()
                            AppText(
                                text = "Tap the + button to add your first link",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    else -> ArticleGrid(
                        links = state.links,
                        isGridView = isGridView,
                        onArticleClick = onLinkClick,
                        onFavoriteClick = onToggleFavorite
                    )
                }
            }
        }
    }
}

/**
 * Quick Access Section - Circular quick access buttons
 */
@Composable
private fun QuickAccessSection(
    onAddLink: () -> Unit,
    onViewCategories: () -> Unit,
    onViewTags: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Quick Access",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuickAccessButton(
                icon = Icons.AutoMirrored.Filled.Article,
                label = "Add Link",
                onClick = onAddLink,
                colors = MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
            )

            QuickAccessButton(
                icon = Icons.Outlined.GridView,
                label = "Categories",
                onClick = onViewCategories,
                colors = MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
            )

            QuickAccessButton(
                icon = Icons.Default.Favorite,
                label = "Tags",
                onClick = onViewTags,
                colors = MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
            )

            QuickAccessButton(
                icon = Icons.Default.Settings,
                label = "Settings",
                onClick = onOpenSettings,
                colors = MaterialTheme.colorScheme.surfaceContainerHighest to MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun QuickAccessButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    colors: Pair<Color, Color>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Surface(
            onClick = onClick,
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(16.dp),
            color = colors.first,
            tonalElevation = 2.dp
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = colors.second,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        AppText(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Filter and View Controls Section
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterAndControlsSection(
    isGridView: Boolean,
    onViewModeToggle: () -> Unit,
    showSortMenu: Boolean,
    onSortMenuToggle: () -> Unit,
    selectedSort: String,
    onSortSelected: (String) -> Unit,
    onSortMenuDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Filter Chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(
                onClick = { /* TODO: Filter by recent */ },
                label = { Text("Recent") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        // View Mode and Sort Controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Grid/List Toggle
            FilterChip(
                selected = isGridView,
                onClick = onViewModeToggle,
                label = {
                    Icon(
                        imageVector = if (isGridView) Icons.Outlined.GridView else Icons.AutoMirrored.Outlined.List,
                        contentDescription = if (isGridView) "Grid view" else "List view"
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )

            // Sort Dropdown
            Box {
                DesignIconButton(
                    onClick = onSortMenuToggle,
                    icon = Icons.AutoMirrored.Outlined.Sort,
                    contentDescription = "Sort"
                )

                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = onSortMenuDismiss,
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    val sortOptions = listOf("Recent", "Name", "Favorite")
                    sortOptions.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    if (option == selectedSort) {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = "Selected",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            onClick = { onSortSelected(option) },
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        )
                        if (option != sortOptions.last()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ArticleGrid(
    links: List<Link>,
    isGridView: Boolean,
    onArticleClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit
) {
    if (isGridView) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(links) { link ->
                ArticleCard(
                    link = link,
                    onClick = { onArticleClick(link.id) },
                    onFavoriteClick = { onFavoriteClick(link.id) }
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            links.forEach { link ->
                ArticleListItem(
                    link = link,
                    onClick = { onArticleClick(link.id) },
                    onFavoriteClick = { onFavoriteClick(link.id) }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Composable
private fun ArticleCard(
    link: Link,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        Column {
            // Article Image/Thumbnail Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Article,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.4f)
                )
            }

            // Article Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Title
                Text(
                    text = link.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Description
                if (link.description.isNotBlank()) {
                    Text(
                        text = link.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Tags
                if (link.tags.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        link.tags.take(2).forEach { tag ->
                            AssistChip(
                                onClick = { /* TODO: Navigate to tag */ },
                                label = {
                                    Text(
                                        text = tag,
                                        style = MaterialTheme.typography.labelSmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.height(24.dp)
                            )
                        }
                        if (link.tags.size > 2) {
                            Text(
                                text = "+${link.tags.size - 2}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Meta info and Favorite
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = getReadingTimeText(link),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    DesignIconButton(
                        onClick = onFavoriteClick,
                        icon = if (link.isFavorite)
                            Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (link.isFavorite) "Remove from favorites" else "Add to favorites",
                        colors = com.greenrobotdev.linklibrary.design.components.button.ComponentColors(
                            content = if (link.isFavorite)
                                MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ArticleListItem(
    link: Link,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(80.dp, 80.dp)
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Article,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.4f)
                )
            }

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Title
                Text(
                    text = link.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Description
                if (link.description.isNotBlank()) {
                    Text(
                        text = link.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Tags and Meta
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (link.tags.isNotEmpty()) {
                        Text(
                            text = link.tags.first(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (link.tags.size > 1) {
                            Text(
                                text = "+${link.tags.size - 1}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = getReadingTimeText(link),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Favorite Button
            DesignIconButton(
                onClick = onFavoriteClick,
                icon = if (link.isFavorite)
                    Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (link.isFavorite) "Remove from favorites" else "Add to favorites",
                colors = com.greenrobotdev.linklibrary.design.components.button.ComponentColors(
                    content = if (link.isFavorite)
                        MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

// Calculate reading time based on description length
@Composable
private fun getReadingTimeText(link: Link): String {
    val wordsPerMinute = 200
    val textToAnalyze = link.description.ifBlank { link.title }
    val wordCount = textToAnalyze.split(" ").size
    val minutes = (wordCount / wordsPerMinute).coerceAtLeast(1)
    return "$minutes min read"
}
