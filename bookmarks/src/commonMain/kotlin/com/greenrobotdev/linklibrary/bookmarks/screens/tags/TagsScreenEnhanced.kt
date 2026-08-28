package com.greenrobotdev.linklibrary.bookmarks.screens.tags

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.linklibrary.design.components.cards.TagCard
import com.greenrobotdev.linklibrary.design.components.cards.TagCardCreate
import com.greenrobotdev.linklibrary.design.components.cards.TagCardData
import com.greenrobotdev.linklibrary.design.components.layout.BentoGrid
import com.greenrobotdev.linklibrary.design.components.layout.BentoItem
import com.greenrobotdev.linklibrary.design.components.layout.BentoItemSize
import com.greenrobotdev.linklibrary.design.components.layout.BulkOperationsBar
import com.greenrobotdev.linklibrary.design.components.layout.BulkAction
import com.greenrobotdev.linklibrary.bookmarks.model.Tag

/**
 * Enhanced Tags Overview Screen - Material 3 Expressive Design
 *
 * Simple and clean tags overview screen matching lords_of_links_tags HTML design:
 * - Clean horizontal wrapping tag cloud layout
 * - Simple header with app title and action icons
 * - Featured tags with primary-container background
 * - Standard tags with surface-container-high background
 * - Outlined tags with border
 * - Usage count badges
 * - Material 3 Expressive animations
 * - Fixed FAB for tag creation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsScreenEnhanced(
    routeKey: NavKey,
    onBack: () -> Unit,
    onCreateTag: () -> Unit = {},
    onManageTags: () -> Unit = {}
) {
    val viewModel: TagsViewModel = viewModel<TagsViewModel>(key = routeKey.toString()) { TagsViewModel() }
    val state by viewModel.models.collectAsState()

    Scaffold(
        topBar = {
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
        },
        floatingActionButton = {
            // Animated FAB
            val fabScale by animateFloatAsState(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "fab_scale"
            )

            FloatingActionButton(
                onClick = onCreateTag,
                modifier = Modifier
                    .size(56.dp)
                    .graphicsLayer { scaleX = fabScale; scaleY = fabScale },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create new tag",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header Section
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Tags Overview",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Organize and find your collections effortlessly.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Manage Tags Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onManageTags,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    shape = RoundedCornerShape(999.dp), // Full rounded
                    modifier = Modifier.height(40.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Manage Tags",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            // Tags Cloud (Horizontal Wrapping Layout)
            if (state.tags.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "No tags yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Create your first tag to get started",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // Tags cloud with horizontal wrapping
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    state.tags.forEachIndexed { index, tag ->
                        val isFeatured = index == 0 && state.tags.size > 1
                        val usageCount = 0 // Would come from actual data

                        EnhancedTagChip(
                            name = tag.name,
                            usageCount = usageCount,
                            isFeatured = isFeatured,
                            index = index,
                            onClick = { /* Navigate to tag detail */ }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Enhanced Tag Chip Component matching lords_of_links_tags HTML design
 *
 * Creates expressive tag chips with different styles based on featured status:
 * - Featured tags: primary-container background with filled icon and bold text
 * - Standard tags: surface-container-high background
 * - Outlined tags: border with outlined icon
 * - Usage count badges on all tags
 * - Expressive spring-based animations
 */
@Composable
private fun EnhancedTagChip(
    name: String,
    usageCount: Int,
    isFeatured: Boolean = false,
    index: Int = 0,
    onClick: () -> Unit = {}
) {
    // Animation for press effect
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "tag_chip_scale"
    )

    // Determine chip style based on position and featured status
    val isOutlined = index >= 5 // Last few tags are outlined
    // Note: weight modifier not available in LazyRow, using fixed sizing instead
    val chipModifier = Modifier // In LazyRow, use fixed widths or content-based sizing

    when {
        isFeatured -> {
            // Featured tag chip with primary-container background
            Surface(
                onClick = onClick,
                modifier = Modifier
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                border = null
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    // Usage count badge
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                        border = null
                    ) {
                        Text(
                            text = usageCount.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
        isOutlined -> {
            // Outlined tag chip with border
            Surface(
                onClick = onClick,
                modifier = chipModifier
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.Transparent,
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = getTagIcon(name),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    // Usage count badge
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        border = null
                    ) {
                        Text(
                            text = usageCount.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
        else -> {
            // Standard tag chip with surface-container-high background
            Surface(
                onClick = onClick,
                modifier = chipModifier
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                border = null
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = getTagIcon(name),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    // Usage count badge
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        border = null
                    ) {
                        Text(
                            text = usageCount.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Get appropriate icon for tag based on name/category
 */
@Composable
private fun getTagIcon(tagName: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when {
        tagName.contains("design", ignoreCase = true) -> Icons.Default.DesignServices
        tagName.contains("code", ignoreCase = true) || tagName.contains("css", ignoreCase = true) -> Icons.Default.Code
        tagName.contains("article", ignoreCase = true) -> Icons.Default.Article
        tagName.contains("tool", ignoreCase = true) -> Icons.Default.Build
        tagName.contains("video", ignoreCase = true) -> Icons.Default.Movie
        tagName.contains("color", ignoreCase = true) -> Icons.Default.Palette
        else -> Icons.Default.LocalOffer
    }
}

/**
 * Stats row showing tag information
 */
@Composable
private fun StatsRow(
    allTags: List<Tag>,
    visibleCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Total tags
        StatItem(
            icon = Icons.Default.LocalOffer,
            label = "Total Tags",
            value = allTags.size.toString()
        )

        // Visible tags
        StatItem(
            icon = Icons.Default.Visibility,
            label = "Visible",
            value = visibleCount.toString()
        )

        // Most used (placeholder)
        StatItem(
            icon = Icons.Default.TrendingUp,
            label = "Most Used",
            value = "Design"
        )
    }
}

/**
 * Individual stat item
 */
@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )

        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Tag merge dialog
 */
@Composable
private fun TagMergeDialog(
    selectedTagIds: Set<String>,
    tags: List<Tag>,
    onDismiss: () -> Unit,
    onMerge: (String, Set<String>) -> Unit
) {
    var targetTagId by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Merge ${selectedTagIds.size} Tags",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Select the target tag to merge into. The other tags will be removed and their links will be reassigned.",
                    style = MaterialTheme.typography.bodyMedium
                )

                tags.filter { it.id in selectedTagIds }.forEach { tag ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { targetTagId = tag.id }
                            .background(
                                if (targetTagId == tag.id) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = targetTagId == tag.id,
                            onClick = { targetTagId = tag.id }
                        )
                        Text(tag.name)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    targetTagId?.let {
                        onMerge(it, selectedTagIds - it)
                    }
                },
                enabled = targetTagId != null
            ) {
                Text("Merge")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}