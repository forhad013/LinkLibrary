package com.greenrobotdev.linklibrary.screens.add

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * AI-powered tag suggestion component
 * Properly follows MVVM architecture by using callbacks instead of direct repository access
 *
 * Add this to your AddLinkScreen
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AITagSuggestionSection(
    url: String,
    title: String,
    suggestedTags: List<String>,
    isGenerating: Boolean = false,
    onGenerateTags: () -> Unit = {},
    onTagToggle: (String) -> Unit = {},
    onTagsSelected: (List<String>) -> Unit = {}
) {
    var selectedTags by remember { mutableStateOf<Set<String>>(emptySet()) }
    var error by remember { mutableStateOf<String?>(null) }

    // Only show if URL is valid
    if (url.isBlank() || !url.startsWith("http")) {
        return
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header with AI button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AI Tag Suggestions",
                style = MaterialTheme.typography.titleSmall
            )

            if (suggestedTags.isEmpty()) {
                Button(
                    onClick = onGenerateTags,
                    enabled = !isGenerating && url.isNotBlank(),
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Generate Tags", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        // Loading indicator
        if (isGenerating) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "AI is analyzing your link...",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Error state
        error?.let { errorMsg ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = errorMsg,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }

        // Suggested tags chips
        if (suggestedTags.isNotEmpty()) {
            Column {
                Text(
                    text = "Tap to remove, or add your own:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(suggestedTags) { tag ->
                        val isSelected = selectedTags.contains(tag)

                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                onTagToggle(tag)
                                selectedTags = if (isSelected) {
                                    selectedTags - tag
                                } else {
                                    selectedTags + tag
                                }
                                onTagsSelected(selectedTags.toList())
                            },
                            label = { Text(tag, style = MaterialTheme.typography.labelSmall) },
                            leadingIcon = if (isSelected) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            } else null,
                            modifier = Modifier.height(28.dp)
                        )
                    }
                }

                // Regenerate button
                TextButton(
                    onClick = onGenerateTags,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Regenerate Tags")
                }
            }
        }
    }
}
