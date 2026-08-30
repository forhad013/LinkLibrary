package com.greenrobotdev.linklibrary.design.components.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * AppCard - Standardized card component for consistent appearance across all form screens
 *
 * Provides a unified card implementation that can be used consistently across:
 * - Add Link screen
 * - Add Tag screen
 * - Add Collection screen
 * - Any other form-based screens
 *
 * Design Philosophy:
 * - Simple, consistent appearance across all screens
 * - Material 3 Expressive styling with proper theming
 * - Standard card functionality with common variants
 * - Reusable patterns for introduction and content cards
 *
 * Features:
 * - Consistent Material 3 Card styling with rounded corners
 * - Proper dark theme support with correct contrast
 * - Built-in elevation and tonal values
 * - Introduction card variant with title and description
 * - Error card variant for error messages
 * - Content card variant for custom content
 */

/**
 * AppIntroductionCard - Introduction card with title and description
 *
 * Usage: Welcome cards, introduction content, informational cards at top of screens
 * Style: primaryContainer color with title and description text
 *
 * @param title Title text for the card
 * @param description Optional description text
 * @param modifier Optional modifier for layout
 */
@Composable
fun AppIntroductionCard(
    title: String,
    description: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

/**
 * AppErrorCard - Error card for error messages
 *
 * Usage: Error messages, validation errors, error states
 * Style: errorContainer color with error text
 *
 * @param error Error message to display
 * @param modifier Optional modifier for layout
 * @param onDismiss Optional callback to dismiss the error
 */
@Composable
fun AppErrorCard(
    error: String,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        if (onDismiss != null) {
            // Interactive error card with dismiss button
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onDismiss
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        } else {
            // Simple error card
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

/**
 * AppContentCard - Generic content card for custom content
 *
 * Usage: Custom content containers, form sections, grouped content
 * Style: surfaceContainer color with custom content slot
 *
 * @param modifier Optional modifier for layout
 * @param content Custom content to display in the card
 */
@Composable
fun AppContentCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}