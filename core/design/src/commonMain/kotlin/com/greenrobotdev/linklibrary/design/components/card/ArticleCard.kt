package com.greenrobotdev.linklibrary.design.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Material Design 3 Article Card
 *
 * A standardized card component for displaying articles/links with thumbnail,
 * title, description, and optional metadata
 */
@Composable
fun ArticleCard(
    title: String,
    description: String = "",
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    thumbnailIcon: ImageVector = Icons.Default.Article,
    thumbnailTint: Color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.4f),
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    subtitle: String = "",
    trailingContent: @Composable() (() -> Unit)? = null
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column {
            // Thumbnail Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = thumbnailIcon,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = thumbnailTint
                )
            }

            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))

                    // Description
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (subtitle.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    // Subtitle and optional trailing content
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (trailingContent != null) {
                            androidx.compose.foundation.layout.Arrangement.SpaceBetween
                        } else {
                            androidx.compose.foundation.layout.Arrangement.Start
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        trailingContent?.invoke()
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
