package com.greenrobotdev.linklibrary.screens.share

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey

/**
 * Maps SharingPlatform to Compose icon
 */
private fun getIconForPlatform(platform: SharingPlatform): ImageVector {
    return when (platform) {
        is SharingPlatform.Twitter -> Icons.Default.ContentCopy // Placeholder - use proper Twitter icon
        is SharingPlatform.Facebook -> Icons.Default.Facebook
        is SharingPlatform.LinkedIn -> Icons.Default.Link // Placeholder - use proper LinkedIn icon
        is SharingPlatform.WhatsApp -> Icons.Default.ContentCopy // Placeholder - use proper WhatsApp icon
        is SharingPlatform.Reddit -> Icons.Default.ContentCopy // Placeholder - use proper Reddit icon
        is SharingPlatform.Email -> Icons.Default.Email
        is SharingPlatform.CopyLink -> Icons.Default.ContentCopy
        is SharingPlatform.More -> Icons.Default.MoreHoriz
    }
}

/**
 * Gets platform-specific color
 */
private fun getColorForPlatform(platform: SharingPlatform): Color {
    return when (platform) {
        is SharingPlatform.Twitter -> Color(0xFF1DA1F2)
        is SharingPlatform.Facebook -> Color(0xFF4267B2)
        is SharingPlatform.LinkedIn -> Color(0xFF0077B5)
        is SharingPlatform.WhatsApp -> Color(0xFF25D366)
        is SharingPlatform.Reddit -> Color(0xFFFF4500)
        is SharingPlatform.Email -> Color(0xFFEA4335)
        is SharingPlatform.CopyLink -> MaterialTheme.colorScheme.primary
        is SharingPlatform.More -> MaterialTheme.colorScheme.secondary
    }
}

/**
 * Share Pop-up Dialog
 * Matches Figma design for sharing functionality
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareDialog(
    routeKey: NavKey,
    linkId: String,
    onDismiss: () -> Unit = {}
) {
    val viewModel: ShareViewModel = viewModel(key = routeKey.toString()) {
        ShareViewModel(linkId)
    }

    val state by viewModel.states.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Share Link",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Close"
                        )
                    }
                }

                // Link preview
                if (state.linkTitle.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = state.linkTitle,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1
                            )
                            Text(
                                text = state.linkUrl,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Custom message
                OutlinedTextField(
                    value = state.customMessage,
                    onValueChange = { viewModel.onEvent(ShareEvent.UpdateMessage(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Add a message") },
                    placeholder = { Text("What's on your mind?") },
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(12.dp)
                )

                // Social platforms grid
                Text(
                    text = "Share to",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                val platforms = listOf(
                    SharingPlatform.Twitter,
                    SharingPlatform.Facebook,
                    SharingPlatform.LinkedIn,
                    SharingPlatform.WhatsApp,
                    SharingPlatform.Reddit,
                    SharingPlatform.Email,
                    SharingPlatform.CopyLink,
                    SharingPlatform.More
                )

                LazyColumn(
                    modifier = Modifier.height(200.dp)
                ) {
                    items(platforms.chunked(2)) { rowPlatforms ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            rowPlatforms.forEach { platform ->
                                PlatformButton(
                                    platform = platform,
                                    isSelected = state.selectedPlatform == platform,
                                    onClick = {
                                        viewModel.onEvent(ShareEvent.SelectPlatform(platform))
                                        viewModel.onEvent(ShareEvent.Share)
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            // Fill empty space if odd number of platforms
                            if (rowPlatforms.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            viewModel.onEvent(ShareEvent.Share)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = state.selectedPlatform != null && !state.isSharing,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (state.isSharing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Share")
                        }
                    }
                }

                // Error message
                state.error?.let { error ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = error,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = { viewModel.onEvent(ShareEvent.ClearError) }
                            ) {
                                Text("Dismiss")
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Individual platform button
 */
@Composable
private fun PlatformButton(
    platform: SharingPlatform,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            null
        },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                getColorForPlatform(platform).copy(alpha = 0.1f)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = getIconForPlatform(platform),
                contentDescription = platform.displayName,
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    getColorForPlatform(platform)
                },
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = platform.displayName,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = 1
            )
        }
    }
}
