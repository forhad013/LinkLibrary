package com.greenrobotdev.linklibrary.design.components.selection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * SelectionPill - Pill-shaped selection button with Material 3 expressive styling
 *
 * A reusable pill-shaped selection component perfect for tags, filters, or any
 * binary selection states. Features expressive animations and clear selection feedback.
 *
 * Design Philosophy:
 * - Consistent Material 3 Expressive styling
 * - Clear selection state with visual and icon feedback
 * - Smooth animations and transitions
 * - Proper dark theme support
 *
 * Features:
 * - Pill-shaped design (50dp corner radius)
 * - Selection state with border and color changes
 * - Optional checkmark icon for selected state
 * - Custom icons support
 * - Consistent sizing and spacing
 *
 * @param selected Whether the pill is currently selected
 * @param onClick Callback when pill is clicked
 * @param text Text to display on the pill
 * @param modifier Optional modifier for layout
 * @param enabled Whether pill is enabled for interaction
 * @param selectedIcon Optional icon to show when selected (default: checkmark)
 * @param unselectedIcon Optional icon to show when not selected
 * @param selectedColor Optional color for selected state
 * @param unselectedColor Optional color for unselected state
 */
@Composable
fun SelectionPill(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedIcon: ImageVector? = Icons.Default.Check,
    unselectedIcon: ImageVector? = null,
    selectedColor: Color? = null,
    unselectedColor: Color? = null
) {
    val pillScale: Float = if (selected) 1.05f else 1f
    val containerColor = selectedColor ?: MaterialTheme.colorScheme.primaryContainer
    val defaultColor = unselectedColor ?: MaterialTheme.colorScheme.surfaceContainerHighest

    Surface(
        onClick = { if (enabled) onClick() },
        modifier = modifier.scale(pillScale),
        shape = RoundedCornerShape(50.dp), // Pill shape
        color = if (selected) {
            containerColor
        } else {
            defaultColor
        },
        border = if (selected) {
            BorderStroke(width = 1.dp, containerColor)
        } else {
            BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline)
        },
        enabled = enabled
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon based on selection state
            if (selected && selectedIcon != null) {
                Icon(
                    imageVector = selectedIcon,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(16.dp)
                )
            } else if (!selected && unselectedIcon != null) {
                Icon(
                    imageVector = unselectedIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

/**
 * AddPill - Specialized pill button for adding new items
 *
 * A convenience component for "Add New" actions with a plus icon.
 * Commonly used for adding new tags, collections, or similar items.
 *
 * @param onClick Callback when add pill is clicked
 * @param text Text to display (default: "New")
 * @param modifier Optional modifier for layout
 * @param enabled Whether pill is enabled for interaction
 * @param icon Optional icon to use instead of default Add icon
 */
@Composable
fun AddPill(
    onClick: () -> Unit,
    text: String = "New",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = Icons.Default.Add
) {
    SelectionPill(
        selected = false,
        onClick = onClick,
        text = text,
        modifier = modifier,
        enabled = enabled,
        selectedIcon = null,
        unselectedIcon = icon,
        selectedColor = MaterialTheme.colorScheme.surface,
        unselectedColor = MaterialTheme.colorScheme.surface
    )
}