package com.greenrobotdev.linklibrary.bookmarks.screens.tags.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.bookmarks.model.Tag
import com.greenrobotdev.linklibrary.bookmarks.screens.tags.TagsState

/**
 * TagsTopBar - Top app bar for Tags screen
 *
 * Features:
 * - App title
 * - Account and settings action buttons
 */
@Composable
fun TagsTopBar(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
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

/**
 * TagsHeader - Header section with title and description
 *
 * Features:
 * - Main title
 * - Description text
 */
@Composable
fun TagsHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
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
}

/**
 * TagsManageButton - Manage tags action button
 *
 * Features:
 * - Edit icon
 * - Rounded pill shape
 * - Secondary container styling
 */
@Composable
fun TagsManageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            shape = RoundedCornerShape(999.dp),
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
}

/**
 * TagsContent - Main content area for tags display
 *
 * Features:
 * - Empty state handling
 * - Tags cloud with horizontal wrapping
 * - Enhanced tag chips with animations
 */
@Composable
fun TagsContent(
    state: TagsState,
    onTagClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        if (state.tags.isEmpty()) {
            EmptyTagsState()
        } else {
            TagsCloud(
                tags = state.tags,
                onTagClick = onTagClick
            )
        }
    }
}

/**
 * EmptyTagsState - Empty state display when no tags exist
 */
@Composable
fun EmptyTagsState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
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
}

/**
 * TagsCloud - Horizontal wrapping layout for tags
 *
 * Features:
 * - FlowRow for automatic wrapping
 * - Enhanced tag chips with different styles
 * - Staggered animations
 */
@Composable
fun TagsCloud(
    tags: List<Tag>,
    onTagClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        tags.forEachIndexed { index, tag ->
            val isFeatured = index == 0 && tags.size > 1
            val usageCount = 0 // Would come from actual data

            EnhancedTagChip(
                name = tag.name,
                usageCount = usageCount,
                isFeatured = isFeatured,
                index = index,
                onClick = { onTagClick(tag.id) }
            )
        }
    }
}

/**
 * EnhancedTagChip - Expressive tag chip with multiple styles
 *
 * Creates expressive tag chips with different styles based on featured status:
 * - Featured tags: primary-container background with filled icon and bold text
 * - Standard tags: surface-container-high background
 * - Outlined tags: border with outlined icon
 * - Usage count badges on all tags
 * - Expressive spring-based animations
 */
@Composable
fun EnhancedTagChip(
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

    when {
        isFeatured -> {
            FeaturedTagChip(
                name = name,
                usageCount = usageCount,
                scale = scale,
                onClick = onClick
            )
        }
        isOutlined -> {
            OutlinedTagChip(
                name = name,
                usageCount = usageCount,
                scale = scale,
                onClick = onClick
            )
        }
        else -> {
            StandardTagChip(
                name = name,
                usageCount = usageCount,
                scale = scale,
                onClick = onClick
            )
        }
    }
}

/**
 * FeaturedTagChip - Featured tag with primary-container background
 */
@Composable
private fun FeaturedTagChip(
    name: String,
    usageCount: Int,
    scale: Float,
    onClick: () -> Unit
) {
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
            UsageCountBadge(count = usageCount, style = BadgeStyle.FEATURED)
        }
    }
}

/**
 * OutlinedTagChip - Outlined tag with border
 */
@Composable
private fun OutlinedTagChip(
    name: String,
    usageCount: Int,
    scale: Float,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
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
            UsageCountBadge(count = usageCount, style = BadgeStyle.OUTLINED)
        }
    }
}

/**
 * StandardTagChip - Standard tag with surface-container-high background
 */
@Composable
private fun StandardTagChip(
    name: String,
    usageCount: Int,
    scale: Float,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
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
            Spacer(modifier = Modifier.weight(1f))
            UsageCountBadge(count = usageCount, style = BadgeStyle.STANDARD)
        }
    }
}

/**
 * Badge style enum for different usage count badge appearances
 */
private enum class BadgeStyle {
    FEATURED,
    STANDARD,
    OUTLINED
}

/**
 * UsageCountBadge - Badge showing tag usage count
 */
@Composable
private fun UsageCountBadge(
    count: Int,
    style: BadgeStyle
) {
    val (color, textColor) = when (style) {
        BadgeStyle.FEATURED -> MaterialTheme.colorScheme.surface.copy(alpha = 0.5f) to MaterialTheme.colorScheme.onSurface
        BadgeStyle.STANDARD -> MaterialTheme.colorScheme.surfaceContainerLowest to MaterialTheme.colorScheme.onSurfaceVariant
        BadgeStyle.OUTLINED -> MaterialTheme.colorScheme.surfaceContainerLowest to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = color,
        border = null
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/**
 * Get appropriate icon for tag based on name/category
 */
@Composable
fun getTagIcon(tagName: String): ImageVector {
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
 * StatsRow - Stats row showing tag information
 */
@Composable
fun StatsRow(
    allTags: List<Tag>,
    visibleCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem(
            icon = Icons.Default.LocalOffer,
            label = "Total Tags",
            value = allTags.size.toString()
        )

        StatItem(
            icon = Icons.Default.Visibility,
            label = "Visible",
            value = visibleCount.toString()
        )

        StatItem(
            icon = Icons.Default.TrendingUp,
            label = "Most Used",
            value = "Design"
        )
    }
}

/**
 * StatItem - Individual stat item
 */
@Composable
private fun StatItem(
    icon: ImageVector,
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
 * TagMergeDialog - Tag merge dialog
 */
@Composable
fun TagMergeDialog(
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