package com.greenrobotdev.linklibrary.bookmarks.screens.details

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.greenrobotdev.linklibrary.design.components.utils.PlatformAsyncImage
import com.greenrobotdev.linklibrary.design.components.icon.AppIcon
import androidx.navigation3.runtime.NavKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Enhanced Link Details Screen - Material 3 Expressive Design matching link_details.html specification
 *
 * Features:
 * - Simple mobile-first layout (not complex desktop grids)
 * - Hero card with image, gradient overlay, and category badge
 * - Metadata section with tags and info cards
 * - Notes section with edit capability
 * - Related links grid
 * - Sticky bottom action bar (Edit, Share, Open Link)
 * - Material 3 Expressive animations and spring physics
 * - Proper responsive design for mobile and desktop
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun LinkDetailScreen(
    routeKey: NavKey,
    linkId: String,
    onBack: () -> Unit,
    onNavigateToNoteEditor: (String) -> Unit = {}
) {
    val viewModel: LinkDetailViewModel = viewModel(key = routeKey.toString()) {
        LinkDetailViewModel(linkId)
    }
    val state by viewModel.models.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var isEditingNotes by remember { mutableStateOf(false) }
    var personalNotes by remember { mutableStateOf(state.link?.description ?: "") }

    // Navigate back after successful deletion
    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) {
            onBack()
        }
    }

    // Navigate back if link not found
    LaunchedEffect(state.error) {
        if (state.error == "Link not found") {
            onBack()
        }
    }

    // Update personal notes when link data changes
    LaunchedEffect(state.link?.description) {
        personalNotes = state.link?.description ?: ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Link Details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Show more options */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        when {
            state.isLoading -> {
                // Loading state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading link details...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            state.link != null -> {
                // Content state - Expressive Material 3 single-column layout
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Hero Card with expressive gradient and molecular imagery
                    HeroCard(
                        imageUrl = state.link?.url,
                        title = state.link?.title ?: "",
                        url = state.link?.url?.toString() ?: "",
                        tags = state.link?.tags ?: emptyList(),
                        description = state.link?.description ?: ""
                    )

                    // Action Buttons Card (moved after hero for better UX)
                    ActionButtonsCard(
                        onOpenLink = { /* Open link */ },
                        onEdit = { /* Navigate to edit */ },
                        onShare = { /* Share link */ },
                        onCopyLink = { /* Copy link */ },
                        onDelete = { showDeleteDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Summary Card
                    SummaryCard(
                        description = state.link?.description ?: "",
                        createdAt = state.link?.createdAt,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Personal Notes Card
                    PersonalNotesCard(
                        notes = personalNotes,
                        isEditing = isEditingNotes,
                        onEditClick = { isEditingNotes = true },
                        onNotesChange = { personalNotes = it },
                        onSave = { isEditingNotes = false },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp)) // Bottom padding
                }
            }

            state.error != null -> {
                // Error state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.error ?: "An error occurred",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onBack) {
                        Text("Go Back")
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Link") },
            text = { Text("Are you sure you want to delete this link? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.take(LinkDetailEvent.Delete)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

/**
 * Hero Card Component - Material 3 Design
 *
 * Hero card matching the Material 3 design specification with:
 * - Light lavender/gray gradient background with abstract molecular 3D imagery
 * - Pill-shaped tags with proper Material 3 styling
 * - Bottom overlay with URL indicator and title
 * - 24dp rounded corners matching Material 3 design system
 */
@Composable
private fun HeroCard(
    imageUrl: String?,
    title: String,
    url: String,
    tags: List<String>,
    description: String
) {
    // Animation for hover effect using spring physics
    var isHovered by remember { mutableStateOf(false) }
    val cardScale by animateFloatAsState(
        targetValue = if (isHovered) 1.02f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "hero_card_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            },
        shape = RoundedCornerShape(24.dp), // Material 3 extra-large rounded corners
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
            hoveredElevation = 2.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Fixed height for mobile
        ) {
            // Hero Image or Expressive Material 3 Gradient Background with molecular imagery
            if (imageUrl != null) {
                PlatformAsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Crop
                )
            } else {
                // Expressive organic gradient background with molecular network imagery
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    // Soft lilac top
                                    Color(0xFFE8E0F0).copy(alpha = 0.8f),
                                    // Light purple mid-top
                                    Color(0xFFD4C4E8).copy(alpha = 0.7f),
                                    // Medium purple mid-bottom
                                    Color(0xFFC4A8E0).copy(alpha = 0.6f),
                                    // Soft lavender bottom
                                    Color(0xFFE0D0F0).copy(alpha = 0.5f)
                                ),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Expressive molecular/sphere 3D visual
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        // Large central molecular sphere
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .graphicsLayer {
                                    scaleX = 1f
                                    scaleY = 1f
                                    alpha = 0.6f
                                }
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF9B7EBD).copy(alpha = 0.4f),
                                            Color(0xFF7B5FA0).copy(alpha = 0.2f),
                                            Color.Transparent
                                        ),
                                        radius = 200f
                                    ),
                                    shape = CircleShape
                                )
                        )

                        // Smaller surrounding molecular spheres
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 40.dp, top = 20.dp)
                                .size(60.dp)
                                .graphicsLayer { alpha = 0.4f }
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFFB8A0D0).copy(alpha = 0.5f),
                                            Color.Transparent
                                        ),
                                        radius = 100f
                                    ),
                                    shape = CircleShape
                                )
                        )

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 50.dp, bottom = 30.dp)
                                .size(80.dp)
                                .graphicsLayer { alpha = 0.35f }
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFFC8B0E0).copy(alpha = 0.6f),
                                            Color.Transparent
                                        ),
                                        radius = 120f
                                    ),
                                    shape = CircleShape
                                )
                        )

                        // Molecular connecting nodes
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = Color(0xFF7B5FA0).copy(alpha = 0.15f),
                            modifier = Modifier
                                .size(100.dp)
                                .graphicsLayer { alpha = 0.3f }
                        )
                    }
                }
            }

            // Material 3 gradient overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            // Floating Pill-shaped Tags (Expressive Material 3 design)
            if (tags.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Primary tag with expressive filled tonal background
                    Surface(
                        shape = RoundedCornerShape(20.dp), // More pronounced pill shape
                        color = Color(0xFF9B7EBD).copy(alpha = 0.85f), // Expressive purple
                        tonalElevation = 3.dp
                    ) {
                        Text(
                            text = tags.firstOrNull() ?: "Design System",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                        )
                    }

                    // Secondary tags with expressive styling
                    tags.drop(1).take(2).forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(20.dp), // More pronounced pill shape
                            color = Color(0xFFE8E0F0).copy(alpha = 0.75f), // Soft lilac
                            tonalElevation = 2.dp
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF5B3F80), // Deep purple text
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }

            // Bottom Content Overlay (Material 3 typography and spacing)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // URL row with icon
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Link,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = url,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }

                // Title with Material 3 typography
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = MaterialTheme.typography.headlineLarge.lineHeight,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Summary Card Component - Expressive Material 3 Design
 *
 * Card displaying link summary/description with metadata badges
 * matching the expressive Material 3 design specification with:
 * - Section header: purple document icon + "Summary" (bold)
 * - Body paragraph with proper expressive Material 3 typography
 * - Meta row with pill-shaped badges (date + read time)
 */
@Composable
private fun SummaryCard(
    description: String,
    createdAt: Long?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp), // Material 3 extra-large rounded corners
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with icon - Expressive Material 3 design
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppIcon(
                    icon = Icons.Default.Article,
                    backgroundColor = Color(0xFF7B5FA0)
                )

                Text(
                    text = "Summary",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Description text with expressive Material 3 typography
            Text(
                text = description.takeIf { it.isNotBlank() }
                    ?: "No description available for this link.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
            )

            // Metadata badges - Expressive Material 3 pill-shaped design
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date added badge - Expressive pill design with soft lilac
                Surface(
                    shape = RoundedCornerShape(14.dp), // More pronounced pill shape
                    color = Color(0xFFE8E0F0).copy(alpha = 0.6f), // Soft lilac
                    tonalElevation = 2.dp
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Label,
                            contentDescription = null,
                            tint = Color(0xFF7B5FA0).copy(alpha = 0.8f), // Deep purple
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Added ${formatTimestamp(createdAt)}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF5B3F80) // Deep purple text
                        )
                    }
                }

                // Read time badge - Expressive pill design with soft lilac
                Surface(
                    shape = RoundedCornerShape(14.dp), // More pronounced pill shape
                    color = Color(0xFFE8E0F0).copy(alpha = 0.6f), // Soft lilac
                    tonalElevation = 2.dp
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = null,
                            tint = Color(0xFF7B5FA0).copy(alpha = 0.8f), // Deep purple
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "5 min read",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF5B3F80) // Deep purple text
                        )
                    }
                }
            }
        }
    }
}

/**
 * Action Buttons Card Component - Expressive Material 3 Design
 *
 * Card containing primary and secondary action buttons
 * matching the expressive Material 3 design specification with:
 * - Full-width primary "Open Link" button with deep expressive purple color
 * - "Add to Task" button with filled tonal style (light lilac)
 * - "Move to Collection" button with outlined style
 * - Icon-only actions (Share, Copy, Delete) separated by dividers
 */
@Composable
private fun ActionButtonsCard(
    onOpenLink: () -> Unit,
    onEdit: () -> Unit,
    onShare: () -> Unit,
    onCopyLink: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp), // Material 3 extra-large rounded corners
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Primary Action Button (Open Link) - Expressive filled button with deep purple
            Button(
                onClick = onOpenLink,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), // Material 3 rounded corners
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B5FA0) // Deep expressive purple
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Open Link",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }

            // Secondary Actions (2-column grid) - Expressive Material 3 design
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Add to Task button - Expressive filled tonal button with lilac
                Button(
                    onClick = { /* TODO: Add to task */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD4C4E8).copy(alpha = 0.8f) // Light expressive lilac
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color(0xFF5B3F80), // Deep purple
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Add to Task",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF5B3F80) // Deep purple
                        )
                    }
                }

                // Move to Collection button - Expressive outlined button
                OutlinedButton(
                    onClick = { /* TODO: Move to collection */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF7B5FA0) // Deep purple
                    ),
                    border = BorderStroke(1.dp, Color(0xFFD4C4E8).copy(alpha = 0.6f))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Label,
                            contentDescription = null,
                            tint = Color(0xFF7B5FA0), // Deep purple
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Move to Collection",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF7B5FA0) // Deep purple
                        )
                    }
                }
            }

            // Tertiary Actions (icon row with divider) - Expressive Material 3 design
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onShare,
                    modifier = Modifier.size(48.dp) // Material 3 touch target
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color(0xFF7B5FA0).copy(alpha = 0.7f) // Muted purple
                    )
                }

                VerticalDivider(
                    modifier = Modifier.height(24.dp),
                    color = Color(0xFFE8E0F0).copy(alpha = 0.6f) // Soft lilac divider
                )

                IconButton(
                    onClick = onCopyLink,
                    modifier = Modifier.size(48.dp) // Material 3 touch target
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Copy link",
                        tint = Color(0xFF7B5FA0).copy(alpha = 0.7f) // Muted purple
                    )
                }

                VerticalDivider(
                    modifier = Modifier.height(24.dp),
                    color = Color(0xFFE8E0F0).copy(alpha = 0.6f) // Soft lilac divider
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(48.dp) // Material 3 touch target
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/**
 * Personal Notes Card Component - Expressive Material 3 Design
 *
 * Large editable notes area matching the expressive Material 3 design specification with:
 * - Header with purple notes icon + "Personal Notes" (bold) + "Saved" pill badge
 * - Note body inside an inset light-gray rounded box
 * - "Format Note" link, purple, right-aligned at bottom
 * - Edit button with icon
 */
@Composable
private fun PersonalNotesCard(
    notes: String,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onNotesChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp), // Material 3 extra-large rounded corners
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with Expressive Material 3 design
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppIcon(
                        icon = Icons.Default.EditNote,
                        backgroundColor = Color(0xFF9B7EBD)
                    )

                    Text(
                        text = "Personal Notes",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // "Saved" status pill badge - Expressive Material 3 design
                if (!isEditing) {
                    Surface(
                        shape = RoundedCornerShape(14.dp), // More pronounced pill shape
                        color = Color(0xFFE8E0F0).copy(alpha = 0.7f), // Soft lilac
                        tonalElevation = 2.dp
                    ) {
                        Text(
                            text = "Saved",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF5B3F80), // Deep purple text
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            // Notes content with expressive Material 3 inset styling
            if (isEditing) {
                // Edit mode - TextField with expressive Material 3 styling
                androidx.compose.material3.TextField(
                    value = notes,
                    onValueChange = onNotesChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    placeholder = {
                        Text(
                            "Jot down key takeaways, action items, or why you saved this...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    },
                    colors = androidx.compose.material3.TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF9B7EBD), // Expressive purple
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color(0xFFF5F0FF).copy(alpha = 0.5f), // Very light lilac
                        unfocusedContainerColor = Color(0xFFF5F0FF).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp), // Material 3 rounded corners
                    maxLines = 10,
                    textStyle = MaterialTheme.typography.bodyMedium
                )

                // Action row with expressive Material 3 styling
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onSave,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF7B5FA0) // Deep expressive purple
                        )
                    ) {
                        Text(
                            "Save Notes",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF7B5FA0) // Deep expressive purple
                        )
                    }
                }
            } else {
                // View mode - Read-only text area with expressive Material 3 inset styling
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(16.dp), // Material 3 rounded corners
                    color = Color(0xFFF5F0FF).copy(alpha = 0.4f), // Very light lilac
                    tonalElevation = 2.dp
                ) {
                    Box(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = notes.takeIf { it.isNotBlank() }
                                ?: "No notes added yet. Click Edit to add your thoughts...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (notes.isNotBlank()) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            },
                            fontStyle = if (notes.isNotBlank()) {
                                androidx.compose.ui.text.font.FontStyle.Normal
                            } else {
                                androidx.compose.ui.text.font.FontStyle.Italic
                            }
                        )
                    }
                }

                // Format and Edit buttons row - Expressive Material 3 design
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // "Format Note" text button - Expressive Material 3 design
                    TextButton(
                        onClick = { /* TODO: Format notes */ },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF7B5FA0) // Deep expressive purple
                        )
                    ) {
                        Text(
                            "Format Note",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF7B5FA0) // Deep expressive purple
                        )
                    }

                    // Edit button with icon - Expressive Material 3 design
                    TextButton(
                        onClick = onEditClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF7B5FA0) // Deep expressive purple
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = Color(0xFF7B5FA0), // Deep expressive purple
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                "Edit",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFF7B5FA0) // Deep expressive purple
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Format timestamp for display
 */
@OptIn(ExperimentalTime::class)
private fun formatTimestamp(timestamp: Long?): String {
    if (timestamp == null) return "recently"

    val now = Clock.System.now().toEpochMilliseconds()
    val differenceMs = now - timestamp
    val days = differenceMs / (24 * 60 * 60 * 1000)

    return when {
        days == 0L -> "today"
        days == 1L -> "yesterday"
        days < 7L -> "$days days ago"
        days < 30L -> "${days / 7} weeks ago"
        else -> "${days / 30} months ago"
    }
}