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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    routeKey: NavKey,
    onNavigateToDetail: (String) -> Unit,
    onAddLink: (String?) -> Unit,
    onAddCategory: () -> Unit,
    onNavigateToCategories: () -> Unit = {},
    onNavigateToTags: () -> Unit = {}
) {
    val viewModel: LibraryViewModel = viewModel(key = routeKey.toString()) { LibraryViewModel() }
    val state by viewModel.models.collectAsState()

    // Filter states
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Design", "Tech", "Articles")

    // Entrance animations
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        contentVisible = true
    }

    val contentAlpha by animateFloatAsState(
        targetValue = if (contentVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "content_alpha"
    )

    val contentOffsetY by animateFloatAsState(
        targetValue = if (contentVisible) 0f else 20f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "content_offset_y"
    )

    // FAB animation
    val fabScale by androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(1f)
    }
    val animatedFabScale by animateFloatAsState(
        targetValue = fabScale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "fab_scale"
    )

    val firstLaunch = !state.isLoading && state.links.isEmpty() && state.searchQuery.isBlank()

    Scaffold(
        topBar = if (!firstLaunch) {
            {} // design: first-launch screen has no app bar
        } else {
            {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background,
                    tonalElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Lords of Links",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconButton(
                                onClick = { /* Navigate to profile */ },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Account",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(
                                onClick = { /* Navigate to settings */ },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .graphicsLayer {
                    alpha = contentAlpha
                    translationY = contentOffsetY
                },
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sticky Search and Filters Section (hidden on first launch)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f)),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (firstLaunch) return@Column
                // Search Input
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = {
                        viewModel.take(LibraryEvent.SearchChanged(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search your library...") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
//                    colors = OutlinedTextFieldDefaults.colors().copy(
//                        foc = MaterialTheme.colorScheme.secondary,
//                        unfocusedBorderColor = Color.Transparent,
//                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
//                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Filter Chips
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(filters) { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            colors = if (selectedFilter == filter) {
                                FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            } else {
                                FilterChipDefaults.filterChipColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            border = if (selectedFilter == filter) null else BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant
                            ),
                            shape = RoundedCornerShape(999.dp),
                            modifier = Modifier.height(32.dp)
                        )
                    }
                }
            }

            // Links Grid - Responsive layout
            if (firstLaunch) {
                FirstLaunchEmptyState(onAddLink = onAddLink)
            } else if (state.filteredLinks.isEmpty()) {
                EmptyState(
                    title = if (state.searchQuery.isNotBlank()) "No links found" else "Your saves",
                    description = if (state.searchQuery.isNotBlank())
                        "Nothing matches this search."
                    else
                        "Save your first link — articles, videos and posts, all in one place.",
                    icon = Icons.Default.Book,
                    actionLabel = "Add a link",
                    onActionClick = { onAddLink(null) },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                // Responsive grid: 1 col mobile, 2 cols tablet, 3 cols desktop
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 300.dp),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp) // Space for FAB
                ) {
                    items(state.filteredLinks) { link ->
                        val itemIndex = state.filteredLinks.indexOf(link)

                        // Staggered entrance animation for each card
                        var itemVisible by remember { mutableStateOf(false) }
                        val itemAlpha by animateFloatAsState(
                            targetValue = if (itemVisible) 1f else 0f,
                            animationSpec = tween(
                                durationMillis = 300,
                                delayMillis = itemIndex * 50, // Stagger each item by 50ms
                                easing = FastOutSlowInEasing
                            ),
                            label = "item_alpha_$itemIndex"
                        )

                        val itemOffsetY by animateFloatAsState(
                            targetValue = if (itemVisible) 0f else 30f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "item_offset_y_$itemIndex"
                        )

                        LaunchedEffect(contentVisible) {
                            if (contentVisible) {
                                itemVisible = true
                            }
                        }

                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    alpha = itemAlpha
                                    translationY = itemOffsetY
                                }
                        ) {
                            ModernLinkCard(
                                link = link,
                                onClick = { onNavigateToDetail(link.id) }
                            )
                        }
                    }
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

