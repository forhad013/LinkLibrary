package com.greenrobotdev.linklibrary.screens.add

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * AI-powered description generator component
 * Properly follows MVVM architecture by using callbacks instead of direct repository access
 *
 * Add this to AddLinkScreen below the description field
 */
@Composable
fun AIDescriptionGenerator(
    url: String,
    title: String,
    currentDescription: String,
    onGenerateDescription: () -> Unit,
    isGenerating: Boolean = false
) {
    var showAIButton by remember { mutableStateOf(true) }

    // Only show if we have a URL and no description yet
    if (url.isBlank() || !url.startsWith("http")) {
        return
    }

    // Hide AI button if user manually edits description
    LaunchedEffect(currentDescription) {
        if (currentDescription.isNotEmpty() && !isGenerating) {
            showAIButton = false
        }
    }

    if (!showAIButton) return

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isGenerating) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Generating description...",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            TextButton(
                onClick = onGenerateDescription,
                enabled = url.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Auto-generate description",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
