package com.greenrobotdev.linklibrary.design.components.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * AppButton - Standardized button component for consistent appearance across all form screens
 *
 * Provides a unified button implementation that can be used consistently across:
 * - Add Link screen
 * - Add Tag screen
 * - Add Collection screen
 * - Any other form-based screens
 *
 * Design Philosophy:
 * - Simple, consistent appearance across all screens
 * - Material 3 Expressive styling with proper theming
 * - Standard button functionality with loading states
 * - No complex variants - one unified button for form actions
 *
 * Features:
 * - Consistent Material 3 Button styling with rounded corners
 * - Proper dark theme support with correct contrast
 * - Loading state with circular progress indicator
 * - Icon support for visual enhancement
 * - Standard button text with proper typography
 * - Enabled/disabled states with visual feedback
 *
 * @param text Button text to display
 * @param onClick Callback when button is clicked
 * @param modifier Optional modifier for layout
 * @param enabled Whether button is enabled for interaction
 * @param isLoading Whether button is in loading state
 * @param icon Optional leading icon
 */
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = Icons.Default.Check
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    ) {
        if (isLoading) {
            // Loading state
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Loading...")
        } else {
            // Normal state with optional icon
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(text)
        }
    }
}