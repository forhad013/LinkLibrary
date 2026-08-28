package com.greenrobotdev.linklibrary.design.components.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * BulkOperationsBar - Bottom bar for bulk selection operations
 *
 * Material 3 Expressive component for handling bulk operations with:
 * - Animated enter/exit transitions
 * - Action buttons with icons
 * - Selection count display
 * - Confirm/Destructive action handling
 * - Accessible interactions
 */

data class BulkAction(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val isDestructive: Boolean = false,
    val enabled: Boolean = true,
    val color: Color
)

@Composable
fun BulkOperationsBar(
    selectedCount: Int,
    onClearSelection: () -> Unit,
    actions: List<BulkAction>,
    onActionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = selectedCount > 0,
    label: String = "selected"
) {
    // Animated visibility
    val enterTransition = expandVertically(
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        expandFrom = Alignment.Bottom
    ) + fadeIn(
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    val exitTransition = shrinkVertically(
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        shrinkTowards = Alignment.Bottom
    ) + fadeOut(
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
    )

    AnimatedVisibility(
        visible = visible,
        enter = enterTransition,
        exit = exitTransition,
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Selection info row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$selectedCount",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "$label",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = onClearSelection,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Clear selection",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                HorizontalDivider()

                // Action buttons row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    actions.forEach { action ->
                        val buttonScale by animateFloatAsState(
                            targetValue = if (action.enabled) 1f else 0.8f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "action_button_scale_${action.id}"
                        )

                        Button(
                            onClick = { onActionClick(action.id) },
                            enabled = action.enabled,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (action.isDestructive) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    action.color
                                }
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .graphicsLayer { scaleX = buttonScale; scaleY = buttonScale },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    action.icon,
                                    contentDescription = action.label,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    action.label,
                                    style = MaterialTheme.typography.labelMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Compact bulk operations bar for smaller screens
 */
@Composable
fun BulkOperationsBarCompact(
    selectedCount: Int,
    onClearSelection: () -> Unit,
    actions: List<BulkAction>,
    onActionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = selectedCount > 0
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            expandFrom = Alignment.Bottom
        ) + fadeIn(),
        exit = shrinkVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            shrinkTowards = Alignment.Bottom
        ) + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Selection count
                Text(
                    text = "$selectedCount selected",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )

                // Action icons
                actions.forEach { action ->
                    IconButton(
                        onClick = { onActionClick(action.id) },
                        enabled = action.enabled,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            action.icon,
                            contentDescription = action.label,
                            tint = if (action.isDestructive) {
                                MaterialTheme.colorScheme.error
                            } else {
                                action.color
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Clear selection
                IconButton(
                    onClick = onClearSelection,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Clear selection",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * Floating bulk operations button for minimal UI
 */
@Composable
fun BulkOperationsFloatingButton(
    selectedCount: Int,
    onClearSelection: () -> Unit,
    actions: List<BulkAction>,
    onActionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = selectedCount > 0
) {
    var expanded by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(),
        exit = scaleOut(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            // Action buttons (when expanded)
            if (expanded) {
                Column(
                    modifier = Modifier.padding(end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Bottom)
                ) {
                    actions.forEach { action ->
                        FloatingActionButton(
                            onClick = {
                                onActionClick(action.id)
                                expanded = false
                            },
                            containerColor = if (action.isDestructive) {
                                MaterialTheme.colorScheme.error
                            } else {
                                action.color
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(action.icon, contentDescription = action.label)
                        }
                    }
                }
            }

            // Main floating action button
            FloatingActionButton(
                onClick = { expanded = !expanded },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(16.dp)
                    .size(56.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    if (expanded) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                            Text(
                                text = "$selectedCount",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Quick action chips for bulk operations
 */
@Composable
fun BulkActionChips(
    selectedCount: Int,
    actions: List<BulkAction>,
    onActionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = selectedCount > 0
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) { initialHeight -> -initialHeight } + fadeIn(),
        exit = slideOutVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) { targetHeight -> -targetHeight } + fadeOut(),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$selectedCount selected",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )

            actions.forEach { action ->
                FilterChip(
                    selected = false,
                    onClick = { onActionClick(action.id) },
                    label = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                action.icon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(action.label)
                        }
                    },
                    enabled = action.enabled,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = if (action.isDestructive) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer
                        },
                        labelColor = if (action.isDestructive) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        }
                    )
                )
            }
        }
    }
}