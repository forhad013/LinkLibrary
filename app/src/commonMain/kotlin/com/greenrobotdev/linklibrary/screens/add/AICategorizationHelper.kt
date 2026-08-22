package com.greenrobotdev.linklibrary.screens.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * AI-powered collection suggestion component
 * Properly follows MVVM architecture by using callbacks instead of direct repository access
 *
 * Suggests which collection a link should be added to
 */
@Composable
fun AICollectionSuggestion(
    url: String,
    title: String,
    availableCollections: List<String>,
    suggestedCollection: String? = null,
    isAnalyzing: Boolean = false,
    onAnalyze: () -> Unit = {},
    onCollectionSelected: (String) -> Unit = {}
) {
    var confidence by remember { mutableStateOf<Float?>(null) }

    // Only show if we have URL and collections
    if (url.isBlank() || availableCollections.isEmpty()) {
        return
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Divider()

        if (isAnalyzing) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "AI analyzing your link...",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        suggestedCollection?.let { collection ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "💡 Suggested Collection",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = collection,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    confidence?.let { score ->
                        LinearProgressIndicator(
                            progress = { score },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Confidence: ${(score * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Button(
                        onClick = { onCollectionSelected(collection) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add to '$collection'")
                    }
                }
            }
        }

        // Analyze button
        if (suggestedCollection == null && !isAnalyzing) {
            OutlinedButton(
                onClick = onAnalyze,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🔮 Suggest Best Collection")
            }
        }
    }
}
