package com.greenrobotdev.lordofthelinks.design.components.feedback

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * ErrorMessageCard - Enhanced error message display with Material 3 expressive styling
 *
 * A reusable error message card component that provides clear visual feedback for errors
 * throughout the application. Features animated appearance, dismiss functionality, and
 * consistent Material 3 Expressive design.
 *
 * Features:
 * - Enhanced error icon and message with Material 3 expressive design
 * - Animated appearance with scale effects
 * - Optional dismiss functionality
 * - Improved dark theme support with enhanced contrast
 * - Consistent error styling across the app
 *
 * Usage:
 * ```kotlin
 * ErrorMessageCard(
 *     error = "Failed to load data. Please try again.",
 *     onDismiss = { /* Dismiss action */ }
 * )
 * ```
 */
@Composable
fun ErrorMessageCard(
    error: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f)
        ),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Enhanced error icon with background
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
            ) {
                androidx.compose.foundation.layout.Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Error message with enhanced typography
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Error",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.9f)
                )
            }

            // Dismiss button
            Surface(
                onClick = onDismiss,
                modifier = Modifier.size(32.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
            ) {
                androidx.compose.foundation.layout.Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Dismiss error",
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

