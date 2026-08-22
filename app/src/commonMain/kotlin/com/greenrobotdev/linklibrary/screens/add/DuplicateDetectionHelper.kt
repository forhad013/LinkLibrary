package com.greenrobotdev.linklibrary.screens.add

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.model.Link
import kotlinx.coroutines.delay

/**
 * Duplicate detection component
 * Warns users if they're adding a link that already exists
 */
@Composable
fun DuplicateDetectionWarning(
    url: String,
    existingLinks: List<Link>,
    onContinueAnyway: () -> Unit
) {
    var isChecking by remember { mutableStateOf(false) }
    var duplicateLink by remember { mutableStateOf<Link?>(null) }
    var showWarning by remember { mutableStateOf(false) }

    // Only check if URL is valid
    val urlToCheck = if (url.startsWith("http")) url else null

    LaunchedEffect(urlToCheck) {
        if (urlToCheck != null) {
            isChecking = true
            duplicateLink = null
            showWarning = false

            // Simulate checking (in production, use Stitch AI for smart matching)
            delay(800)

            // Simple URL matching - in production, use Stitch for semantic similarity
            duplicateLink = existingLinks.find { existing ->
                existing.url.equals(urlToCheck, ignoreCase = true) ||
                existing.url.removePrefix("www.").removePrefix("https://")
                    .removePrefix("http://") ==
                urlToCheck.removePrefix("www.").removePrefix("https://")
                    .removePrefix("http://")
            }

            showWarning = duplicateLink != null
            isChecking = false
        }
    }

    if (isChecking) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (showWarning && duplicateLink != null) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "Possible Duplicate Found!",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }

                Divider(color = MaterialTheme.colorScheme.onErrorContainer)

                Text(
                    text = "You already have this link saved:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = duplicateLink?.title ?: "No title",
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1
                        )
                        Text(
                            text = duplicateLink?.url.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        duplicateLink?.description?.let { desc ->
                            Text(
                                text = desc,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* Navigate to existing link */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("View Existing")
                    }

                    Button(
                        onClick = onContinueAnyway,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Add Anyway")
                    }
                }
            }
        }
    }
}

/**
 * Advanced AI-based duplicate detection (production version)
 * Properly follows MVVM architecture by using callbacks instead of direct repository access
 */
@Composable
fun AIDuplicateDetection(
    url: String,
    title: String,
    existingLinks: List<Link>,
    isAnalyzing: Boolean = false,
    similarLinks: List<Link> = emptyList(),
    onAnalyze: () -> Unit = {}
) {
    LaunchedEffect(url, title) {
        if (url.isNotBlank() && url.startsWith("http")) {
            onAnalyze()
        }
    }

    if (isAnalyzing) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Checking for duplicates...", style = MaterialTheme.typography.bodySmall)
        }
    }

    if (similarLinks.isNotEmpty()) {
        // Show similar links warning
        // TODO: Implement UI for showing similar links with similarity scores
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "⚠️ Similar Links Found",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = "Found ${similarLinks.size} potentially similar links in your collection.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}
