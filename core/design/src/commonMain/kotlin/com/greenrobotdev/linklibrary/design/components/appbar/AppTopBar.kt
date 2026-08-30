package com.greenrobotdev.linklibrary.design.components.appbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.components.button.IconButton

/**
 * AppTopBar - Standardized top app bar for consistent screen headers
 *
 * Provides a unified top app bar implementation that can be used consistently across:
 * - Add Link screen
 * - Add Tag screen
 * - Add Collection screen
 * - Detail screens
 * - Any other screens with consistent top bar needs
 *
 * Design Philosophy:
 * - Simple, consistent appearance across all screens
 * - Material 3 Expressive styling with proper theming
 * - Standard navigation and action buttons
 * - Clean, modern design with proper elevation
 *
 * Features:
 * - Consistent Material 3 styling with rounded corners
 * - Proper dark theme support with correct contrast
 * - Standard close/back button with icon support
 * - Action button with conditional styling
 * - Centered title with proper typography
 * - Proper elevation and atmospheric layering
 *
 * @param title Title text to display in the top bar
 * @param onNavigationClick Callback when navigation button is clicked
 * @param onActionClick Optional callback when action button is clicked
 * @param modifier Optional modifier for layout
 * @param actionText Optional text for action button (default: "Save")
 * @param isActionEnabled Whether action button is enabled
 * @param isLoading Whether action button is in loading state
 * @param navigationIcon Optional icon for navigation button (default: close icon)
 */
@Composable
fun AppTopBar(
    title: String,
    onNavigationClick: () -> Unit,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    actionText: String = "Save",
    isActionEnabled: Boolean = true,
    isLoading: Boolean = false,
    navigationIcon: androidx.compose.ui.graphics.vector.ImageVector? = Icons.Default.Close
) {
    // Animate action button state for better visual feedback
    val buttonScale = if (isActionEnabled && !isLoading) 1f else 0.95f

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Navigation button
            IconButton(
                onClick = onNavigationClick,
                icon = navigationIcon,
                contentDescription = "Navigation",
                style = com.greenrobotdev.linklibrary.design.theme.ComponentStyle.Elevated,
                size = com.greenrobotdev.linklibrary.design.theme.ComponentSize.Medium
            )

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Action button (optional)
            if (onActionClick != null) {
                ActionButton(
                    text = actionText,
                    onClick = onActionClick,
                    enabled = isActionEnabled && !isLoading,
                    buttonScale = buttonScale
                )
            } else {
                // Spacer to maintain layout when no action button
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(48.dp))
            }
        }
    }
}

/**
 * ActionButton - Internal action button component for AppTopBar
 *
 * Provides the action button with Material 3 Expressive styling
 * and proper conditional state management.
 */
@Composable
private fun ActionButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    buttonScale: Float
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .scale(buttonScale)
            .padding(end = 8.dp)
            .height(40.dp),
        shape = RoundedCornerShape(20.dp),
        color = if (enabled) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceContainerHighest
        },
        tonalElevation = if (enabled) 2.dp else 0.dp
    ) {
        androidx.compose.foundation.layout.Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Text(
                text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                }
            )
        }
    }
}