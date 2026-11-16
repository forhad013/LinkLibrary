package com.greenrobotdev.linklibrary.bookmarks.screens.library

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import com.greenrobotdev.linklibrary.design.components.emptystate.EmptyState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.platform.LocalUriHandler
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.ChoiceChip
import com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker.CategoryPickerSheet
import com.greenrobotdev.linklibrary.bookmarks.screens.tagPicker.TagPickerSheet
import com.greenrobotdev.linklibrary.design.components.textfield.SearchField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.greenrobotdev.linklibrary.design.components.utils.PlatformAsyncImage
import com.greenrobotdev.linklibrary.design.components.button.AppButton
import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.linklibrary.bookmarks.model.Link
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Enhanced Library Screen - Material 3 Expressive Design matching lords_of_links_library HTML
 *
 * Features:
 * - Desktop Side Navigation (hidden on mobile)
 * - Top App Bar with account/settings actions
 * - Sticky Search and Filters Section with filter chips
 * - Responsive Grid Layout (1/2/3 columns based on screen size)
 * - Modern Link Cards with hero images and hover effects
 * - Mobile Bottom Navigation and FAB
 * - Material 3 Expressive animations and spring physics
 */
@Composable
fun LibraryScreen(
    routeKey: NavKey,
    onNavigateToDetail: (String) -> Unit,
    onAddLink: (String?) -> Unit,
    onNavigateToCategories: () -> Unit = {},
    onNavigateToTags: () -> Unit = {}
) {
    val viewModel: LibraryViewModel = viewModel(key = routeKey.toString()) { LibraryViewModel() }
    val state by viewModel.models.collectAsState()
    val uriHandler = LocalUriHandler.current

    var pickCategory by remember { mutableStateOf(false) }
    var pickTags by remember { mutableStateOf(false) }
    if (pickCategory) {
        CategoryPickerSheet(
            onDismiss = { pickCategory = false },
            initialSelection = state.categoryId,
            onApplied = { viewModel.take(LibraryEvent.SetCategory(it)) },
        )
    }
    if (pickTags) {
        TagPickerSheet(
            onDismiss = { pickTags = false },
            initialChecked = state.tagIds,
            onApplied = { viewModel.take(LibraryEvent.SetTags(it)) },
        )
    }

    val firstLaunch = !state.isLoading && state.links.isEmpty()
    if (firstLaunch) {
        FirstLaunchEmptyState(onAddLink = onAddLink, modifier = Modifier.background(MaterialTheme.colorScheme.background))
        return
    }

    val links = state.filteredLinks
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Fixed top: header, search, quick filters, secondary filters
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            FeedHeader(newestFirst = state.newestFirst, onToggleSort = { viewModel.take(LibraryEvent.ToggleSort) })
            SearchField(
                query = state.searchQuery,
                onQueryChange = { viewModel.take(LibraryEvent.SearchChanged(it)) },
                placeholder = "Search everything you saved",
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(FeedFilter.entries) { f ->
                ChoiceChip(
                    text = f.label,
                    selected = state.filter == f,
                    onClick = { viewModel.take(LibraryEvent.SetFilter(f)) },
                )
            }
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                FeedFilterButton(
                    text = state.category?.name ?: "Category",
                    icon = Icons.Outlined.Folder,
                    active = state.categoryId != null,
                    onClick = { pickCategory = true },
                )
            }
            item {
                FeedFilterButton(
                    text = if (state.tagIds.isEmpty()) "Tags" else "Tags · ${state.tagIds.size}",
                    icon = Icons.AutoMirrored.Outlined.Label,
                    active = state.tagIds.isNotEmpty(),
                    onClick = { pickTags = true },
                )
            }
            item {
                FeedFilterButton(
                    text = "Show archived",
                    icon = null,
                    active = state.showArchived,
                    onClick = { viewModel.take(LibraryEvent.ToggleArchived) },
                )
            }
        }
        HorizontalDivider(Modifier.padding(top = 12.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        if (links.isEmpty()) {
            EmptyState(
                title = "Nothing here",
                description = "No saved link matches these filters.",
                icon = Icons.Default.Search,
                actionLabel = "Clear filters",
                onActionClick = {
                    viewModel.take(LibraryEvent.SearchChanged(""))
                    viewModel.take(LibraryEvent.SetFilter(FeedFilter.ALL))
                    viewModel.take(LibraryEvent.SetCategory(null))
                    viewModel.take(LibraryEvent.SetTags(emptySet()))
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp), // room for the FAB
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(links, key = { it.id }) { link ->
                    FeedLinkCard(
                        link = link,
                        categoryPath = state.categoryOf(link)?.path(state.categories),
                        onOpen = {
                            runCatching { uriHandler.openUri(link.url) }
                            viewModel.take(LibraryEvent.Opened(link.id))
                        },
                        onDetails = { onNavigateToDetail(link.id) },
                    )
                }
            }
        }
    }
}

/**
 * Modern Link Card matching lords_of_links_library HTML design
 *
 * Features:
 * - Hero image (140px) with hover zoom effect
 * - Title and description with proper line clamping
 * - Tag chips at bottom
 * - More options menu button
 * - Rounded corners and border styling
 * - Shadow effects on hover
 * - Expressive animations
 */
@Composable
fun ModernLinkCard(
    link: Link,
    onClick: () -> Unit
) {
    // Animation states
    var isHovered by androidx.compose.runtime.remember { mutableStateOf(false) }
    var isPressed by androidx.compose.runtime.remember { mutableStateOf(false) }

    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_scale"
    )

    val imageScale by animateFloatAsState(
        targetValue = if (isHovered) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "image_scale"
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            hoveredElevation = 4.dp,
            pressedElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            // Hero Image Section (140dp height)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            ) {
                if (link.url != null) {
                    PlatformAsyncImage(
                        model = link.url,
                        contentDescription = link.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = imageScale
                                scaleY = imageScale
                            }
                    )
                } else {
                    // Fallback with gradient and icon
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                androidx.compose.ui.graphics.Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.secondaryContainer,
                                        MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Article,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            }

            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title
                Text(
                    text = link.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium
                )

                // Description
                Text(
                    text = link.description.takeIf { it.isNotBlank() }
                        ?: "No description available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))

                // Bottom section with tags and menu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tag chips
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        link.tags.take(2).forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = null
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        if (link.tags.size > 2) {
                            Text(
                                text = "+${link.tags.size - 2}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // More options menu
                    IconButton(
                        onClick = { /* Show menu */ },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * First-launch empty state shown only when no link has ever been saved.
 * Matches design/screens/Feed first launch.png.
 */
@Composable
fun FirstLaunchEmptyState(
    onAddLink: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Outer shape: larger, differently-rounded corner frame behind the icon tile
        Surface(
            shape = RoundedCornerShape(
                topStart = 44.dp,
                topEnd = 12.dp,
                bottomStart = 12.dp,
                bottomEnd = 44.dp
            ),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            // Inner icon tile
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(30.dp)
                    .rotate(-15f)
            ) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(20.dp)
                        .size(40.dp)
                )
            }
        }


        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Your saves",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Save your first link",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Keep articles, videos and posts in one place, and find them again later.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(24.dp))

        // How-to steps
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val steps = listOf(
                "Open a page or video in any app",
                "Tap Share",
                "Choose Lords of Links"
            )
            steps.forEachIndexed { index, step ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        AppButton(
            text = "Or paste a link here",
            onClick = { onAddLink(null) },
            icon = Icons.Default.Add,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

