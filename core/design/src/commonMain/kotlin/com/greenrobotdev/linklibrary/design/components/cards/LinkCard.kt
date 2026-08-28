package com.greenrobotdev.linklibrary.design.components.cards

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.components.utils.PlatformAsyncImage

/**
 * LinkCard - Enhanced link display component with images and Material 3 Expressive interactions
 *
 * Features:
 * - Hero image with aspect ratio
 * - Title and description
 * - Tag chips
 * - Favorite/bookmark actions
 * - Expressive hover effects
 * - Spring-based animations
 * - Proper elevation shadows
 */
@Composable
fun LinkCard(
    title: String,
    url: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    imageUrl: String? = null,
    tags: List<String> = emptyList(),
    isFavorite: Boolean = false,
    isBookmarked: Boolean = false,
    onClick: () -> Unit = {},
    onFavoriteClick: (() -> Unit)? = null,
    onBookmarkClick: (() -> Unit)? = null,
    elevation: Int = 1
) {
    var isHovered by  remember {   mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    val cardElevation by animateDpAsState(
        targetValue = when {
            isPressed -> 4.dp
            isHovered -> 3.dp
            else -> elevation.dp
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_elevation"
    )

    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.98f
            isHovered -> 1.02f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_scale"
    )

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = cardElevation
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable(
                    interactionSource = androidx.compose.foundation.interaction.MutableInteractionSource(),
                    indication = null
                ) {
                    isHovered = !isHovered
                }
        ) {
            // Hero Image
            imageUrl?.let { url ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    PlatformAsyncImage(
                        model = url,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Action buttons overlay
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (onFavoriteClick != null) {
                            IconButton(
                                onClick = onFavoriteClick,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                                    tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        if (onBookmarkClick != null) {
                            IconButton(
                                onClick = onBookmarkClick,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = if (isBookmarked) "Remove bookmark" else "Add bookmark",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Description
                description?.let { desc ->
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // URL
                Text(
                    text = url,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Tags
                if (tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        tags.take(3).forEach { tag ->
                            TagChip(tag = tag)
                        }
                        if (tags.size > 3) {
                            Text(
                                text = "+${tags.size - 3}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Compact tag chip for link cards
 */
@Composable
private fun TagChip(tag: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier.padding(0.dp)
    ) {
        Text(
            text = tag,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/**
 * Grid layout variant for LinkCard with consistent sizing
 */
@Composable
fun LinkCardGridItem(
    title: String,
    url: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    imageUrl: String? = null,
    tags: List<String> = emptyList(),
    isFavorite: Boolean = false,
    isBookmarked: Boolean = false,
    onClick: () -> Unit = {},
    onFavoriteClick: (() -> Unit)? = null,
    onBookmarkClick: (() -> Unit)? = null
) {
    LinkCard(
        title = title,
        url = url,
        modifier = modifier,
        description = description,
        imageUrl = imageUrl,
        tags = tags,
        isFavorite = isFavorite,
        isBookmarked = isBookmarked,
        onClick = onClick,
        onFavoriteClick = onFavoriteClick,
        onBookmarkClick = onBookmarkClick,
        elevation = 2
    )
}

/**
 * Compact list variant for LinkCard
 */
@Composable
fun LinkCardListItem(
    title: String,
    url: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    imageUrl: String? = null,
    tags: List<String> = emptyList(),
    isFavorite: Boolean = false,
    isBookmarked: Boolean = false,
    onClick: () -> Unit = {},
    onFavoriteClick: (() -> Unit)? = null,
    onBookmarkClick: (() -> Unit)? = null
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Optional thumbnail
            imageUrl?.let { url ->
                PlatformAsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } ?: Spacer(modifier = Modifier.width(56.dp))

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                description?.let { desc ->
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = url,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Actions
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (onFavoriteClick != null) {
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                if (onBookmarkClick != null) {
                    IconButton(
                        onClick = onBookmarkClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}