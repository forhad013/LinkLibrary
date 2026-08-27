package com.greenrobotdev.linklibrary.design.components.emptystate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.tokens.SpacingTokens
import com.greenrobotdev.linklibrary.design.components.button.TextButton

/**
 * Empty state component with Material 3 styling
 *
 * Displays a consistent empty state UI with optional icon, title, description,
 * and action button. Use when there's no content to display.
 *
 * @param icon Optional icon to display above the title
 * @param title Main title text for the empty state
 * @param description Optional description text below the title
 * @param actionLabel Optional text for action button
 * @param onActionClick Optional callback for action button click
 * @param modifier Modifier to be applied to the empty state
 * @param iconSize Size for the optional icon. Defaults to 64dp
 */
@Composable
fun EmptyState(
    icon: ImageVector? = null,
    title: String,
    description: String? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    iconSize: Dp = 64.dp
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(SpacingTokens.ExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Spacer(Modifier.height(SpacingTokens.Large))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        if (description != null) {
            Spacer(Modifier.height(SpacingTokens.Small))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        if (actionLabel != null && onActionClick != null) {
            Spacer(Modifier.height(SpacingTokens.ExtraLarge))
            TextButton(
                text = actionLabel,
                onClick = onActionClick
            )
        }
    }
}

/**
 * Compact empty state for smaller spaces
 *
 * Simplified version of empty state with smaller icon and reduced spacing.
 *
 * @param icon Optional icon to display above the title
 * @param title Main title text for the empty state
 * @param description Optional description text below the title
 * @param modifier Modifier to be applied to the empty state
 */
@Composable
fun CompactEmptyState(
    icon: ImageVector? = null,
    title: String,
    description: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(SpacingTokens.Medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Spacer(Modifier.height(SpacingTokens.Small))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        if (description != null) {
            Spacer(Modifier.height(SpacingTokens.Tiny))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

/**
 * Empty state with illustration support
 *
 * Use for custom illustrations via painter resources instead of icons.
 *
 * @param illustrationPainter Painter resource for illustration
 * @param title Main title text for the empty state
 * @param description Optional description text below the title
 * @param actionLabel Optional text for action button
 * @param onActionClick Optional callback for action button click
 * @param modifier Modifier to be applied to the empty state
 */
@Composable
fun EmptyStateWithIllustration(
    illustrationPainter: androidx.compose.ui.graphics.painter.Painter,
    title: String,
    description: String? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(SpacingTokens.ExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        androidx.compose.foundation.Image(
            painter = illustrationPainter,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            alpha = 0.6f
        )

        Spacer(Modifier.height(SpacingTokens.Large))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        if (description != null) {
            Spacer(Modifier.height(SpacingTokens.Small))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        if (actionLabel != null && onActionClick != null) {
            Spacer(Modifier.height(SpacingTokens.ExtraLarge))
            TextButton(
                text = actionLabel,
                onClick = onActionClick
            )
        }
    }
}