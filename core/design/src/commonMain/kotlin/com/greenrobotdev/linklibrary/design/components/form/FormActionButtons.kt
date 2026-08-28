package com.greenrobotdev.linklibrary.design.components.form

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * FormActionButtons - Material 3 Expressive bottom action bar component
 *
 * Fixed bottom action bar with animated interactions following Material 3 Expressive guidelines:
 * - Primary/Secondary action buttons
 * - Spring-based animations
 * - Shadow effects
 * - Proper elevation
 * - Responsive layout
 */
@Composable
fun FormActionButtons(
    primaryText: String,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    primaryIcon: ImageVector? = null,
    secondaryText: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
    secondaryIcon: ImageVector? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val scale by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.98f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Secondary button (optional)
            if (secondaryText != null && onSecondaryClick != null) {
                OutlinedButton(
                    onClick = onSecondaryClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .scale(scale),
                    enabled = enabled && !isLoading,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        secondaryIcon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = secondaryText,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            // Primary button
            Button(
                onClick = onPrimaryClick,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .scale(scale),
                enabled = enabled && !isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 2.dp,
                    hoveredElevation = 4.dp
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    primaryIcon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = primaryText,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

/**
 * Fixed bottom action bar for forms with surface background
 *
 * Sticks to bottom of screen with proper elevation and Material 3 Expressive styling
 */
@Composable
fun FixedFormActionsBar(
    primaryText: String,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    primaryIcon: ImageVector? = null,
    secondaryText: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
    secondaryIcon: ImageVector? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 3.dp,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 24.dp
            )
        ) {
            FormActionButtons(
                primaryText = primaryText,
                onPrimaryClick = onPrimaryClick,
                primaryIcon = primaryIcon,
                secondaryText = secondaryText,
                onSecondaryClick = onSecondaryClick,
                secondaryIcon = secondaryIcon,
                enabled = enabled,
                isLoading = isLoading
            )
        }
    }
}

/**
 * Compact action buttons row for inline form actions
 */
@Composable
fun CompactFormActions(
    primaryText: String,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    primaryIcon: ImageVector? = null,
    secondaryText: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (secondaryText != null && onSecondaryClick != null) {
            TextButton(
                onClick = onSecondaryClick,
                modifier = Modifier.height(40.dp),
                enabled = enabled
            ) {
                Text(secondaryText)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onPrimaryClick,
            modifier = Modifier.height(40.dp),
            enabled = enabled,
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                primaryIcon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.width(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = primaryText,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

/**
 * Single centered action button for focused actions
 */
@Composable
fun CenteredActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val scale by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.98f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .scale(scale),
            enabled = enabled && !isLoading,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 2.dp,
                hoveredElevation = 4.dp
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.width(24.dp).height(24.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}