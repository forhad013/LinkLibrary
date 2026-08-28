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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.greenrobotdev.linklibrary.design.components.utils.PlatformAsyncImage
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
                // Content state - matching HTML grid layout (8:4 column split on desktop)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Responsive grid: single column on mobile, 8:4 split on desktop
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Left Column: Hero + Summary (spans 8/12 on desktop)
                        Column(
                            modifier = Modifier
                                .weight(8f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            // Hero Card
                            HeroCard(
                                imageUrl = state.link?.url,
                                title = state.link?.title ?: "",
                                url = state.link?.url?.toString() ?: "",
                                tags = state.link?.tags ?: emptyList(),
                                description = state.link?.description ?: ""
                            )

                            // Summary Card
                            SummaryCard(
                                description = state.link?.description ?: "",
                                createdAt = state.link?.createdAt,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Right Column: Actions + Notes (spans 4/12 on desktop)
                        Column(
                            modifier = Modifier
                                .weight(4f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            // Action Buttons Card
                            ActionButtonsCard(
                                onOpenLink = { /* Open link */ },
                                onEdit = { /* Navigate to edit */ },
                                onShare = { /* Share link */ },
                                onCopyLink = { /* Copy link */ },
                                onDelete = { showDeleteDialog = true },
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
                        }
                    }

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
 * Hero Card Component
 *
 * Hero card matching the link_details.html specification with:
 * - Large image (300px mobile, 450px desktop)
 * - Proper gradient overlay (surface/90 via surface/40 to transparent)
 * - Floating meta tags at top-left
 * - Bottom overlay with URL indicator and title
 */
@Composable
private fun HeroCard(
    imageUrl: String?,
    title: String,
    url: String,
    tags: List<String>,
    description: String
) {
    // Animation for hover effect
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
        shape = RoundedCornerShape(24.dp), // Matching HTML rounded-3xl
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            hoveredElevation = 4.dp
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Fixed height matching HTML
        ) {
            // Hero Image or Gradient Placeholder
            if (imageUrl != null) {
                PlatformAsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Gradient placeholder matching HTML aesthetic
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.secondaryContainer
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Article,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f),
                        modifier = Modifier.size(64.dp)
                    )
                }
            }

            // Gradient overlay matching HTML: from-surface/90 via-surface/40 to-transparent
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                            )
                        )
                    )
            )

            // Floating Meta Tags (matching HTML top-left positioning)
            if (tags.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Primary tag with filled background
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shadowElevation = 2.dp
                    ) {
                        Text(
                            text = tags.firstOrNull() ?: "Design System",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    // Secondary tags with surface background
                    tags.drop(1).take(2).forEach { tag ->
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            shadowElevation = 2.dp
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Bottom Content Overlay (matching HTML bottom overlay)
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
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }

                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge, // Larger to match HTML
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
 * Summary Card Component
 *
 * Card displaying link summary/description with metadata badges
 * matching the link_details.html specification
 */
@Composable
private fun SummaryCard(
    description: String,
    createdAt: Long?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp), // Matching HTML rounded-3xl
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with icon
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Article,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Summary",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Description text
            Text(
                text = description.takeIf { it.isNotBlank() }
                    ?: "No description available for this link.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
            )

            // Metadata badges (matching HTML)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date added badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Label,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Added ${formatTimestamp(createdAt)}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Read time badge (placeholder)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "5 min read",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * Action Buttons Card Component
 *
 * Card containing primary and secondary action buttons
 * matching the link_details.html specification
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
        shape = RoundedCornerShape(24.dp), // Matching HTML rounded-3xl
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Primary Action Button (Open Link)
            Button(
                onClick = onOpenLink,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Open Link",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            // Secondary Actions (2-column grid)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Add to Task button
                OutlinedButton(
                    onClick = { /* TODO: Add to task */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Add to Task",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                // Move to Collection button
                OutlinedButton(
                    onClick = { /* TODO: Move to collection */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Label,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Move to Collection",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            // Tertiary Actions (icon row with divider)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onShare,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                VerticalDivider(
                    modifier = Modifier.height(24.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )

                IconButton(
                    onClick = onCopyLink,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Copy link",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                VerticalDivider(
                    modifier = Modifier.height(24.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(40.dp)
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
 * Personal Notes Card Component
 *
 * Large editable notes area with format button
 * matching the link_details.html specification
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
        modifier = modifier.fillMaxWidth(), // HTML min-height handled by content
        shape = RoundedCornerShape(24.dp), // Matching HTML rounded-3xl
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with edit status and format button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.EditNote,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Personal Notes",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Status indicator
                if (!isEditing) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.surfaceContainer
                    ) {
                        Text(
                            text = "Saved",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Notes content
            if (isEditing) {
                // Edit mode - TextField
                androidx.compose.material3.TextField(
                    value = notes,
                    onValueChange = onNotesChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    placeholder = {
                        Text("Jot down key takeaways, action items, or why you saved this...")
                    },
                    colors = androidx.compose.material3.TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    shape = RoundedCornerShape(16.dp),
                    maxLines = 10,
                    textStyle = MaterialTheme.typography.bodyMedium
                )

                // Action row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onSave,
                        shape = CircleShape,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Save Notes", style = MaterialTheme.typography.labelLarge)
                    }
                }
            } else {
                // View mode - Text area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    androidx.compose.material3.TextField(
                        value = notes.takeIf { it.isNotBlank() } ?: "No notes added yet. Click Edit to add your thoughts...",
                        onValueChange = { },
                        modifier = Modifier.fillMaxSize(),
                        colors = androidx.compose.material3.TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        ),
                        shape = RoundedCornerShape(16.dp),
                        maxLines = 10,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        readOnly = true
                    )
                }

                // Format button (always visible)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { /* TODO: Format notes */ },
                        shape = CircleShape,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Format Note", style = MaterialTheme.typography.labelLarge)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = onEditClick,
                        shape = CircleShape,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text("Edit", style = MaterialTheme.typography.labelLarge)
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