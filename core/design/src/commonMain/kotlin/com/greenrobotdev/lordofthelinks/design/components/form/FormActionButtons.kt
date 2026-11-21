package com.greenrobotdev.lordofthelinks.design.components.form

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
                                .height(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = primaryText,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

