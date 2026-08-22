package com.greenrobotdev.linklibrary.screens.library

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * AI-powered search component for LibraryScreen
 * Properly follows MVVM architecture by using callbacks instead of direct repository access
 *
 * Helps users find links using natural language queries
 */
@Composable
fun AISearchBar(
    searchQuery: String,
    isSearching: Boolean = false,
    onSearchQueryChanged: (String) -> Unit,
    onAISearch: (String) -> Unit = {}
) {
    var showAIHint by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = {
            onSearchQueryChanged(it)
            // Show AI hint after typing
            showAIHint = it.length > 3
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Search links...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (isSearching) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }
        },
        supportingText = if (showAIHint) {
            {
                Text(
                    "💡 Try: 'find all programming tutorials' or 'show me saved articles about AI'",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else null,
        singleLine = true
    )

    // AI-enhanced search (debounced)
    LaunchedEffect(searchQuery) {
        if (searchQuery.length > 5) {
            onAISearch(searchQuery)
        }
    }
}

/**
 * AI Search Results Component
 * Shows AI-analyzed results with relevance scores
 */
@Composable
fun AISearchResults(
    results: List<AISearchResult>,
    onResultClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(results) { result ->
            AISearchResultCard(
                result = result,
                onClick = { onResultClick(result.url) }
            )
        }
    }
}

/**
 * Data class for AI search results
 */
data class AISearchResult(
    val url: String,
    val title: String,
    val description: String,
    val relevanceScore: Float, // 0.0 to 1.0
    val matchReason: String, // Why this result matches
    val tags: List<String>
)

@Composable
fun AISearchResultCard(
    result: AISearchResult,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = result.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1
                )
                // Relevance badge
                Surface(
                    color = when {
                        result.relevanceScore > 0.8f -> MaterialTheme.colorScheme.primaryContainer
                        result.relevanceScore > 0.5f -> MaterialTheme.colorScheme.secondaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "${(result.relevanceScore * 100).toInt()}% match",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = result.url,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )

            if (result.description.isNotEmpty()) {
                Text(
                    text = result.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }

            // Match reason
            Text(
                text = "🤖 ${result.matchReason}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
