package com.greenrobotdev.lordofthelinks.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.greenrobotdev.lordofthelinks.design.components.emptystate.EmptyState

/** Fills the detail pane of a two-pane layout until a link is picked. */
@Composable
fun DetailPanePlaceholder(
    title: String = "Pick a link to see its details",
    description: String = "Its note, tags and category show here.",
    onAddLink: (() -> Unit)? = null,
) {
    Box(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        EmptyState(
            icon = Icons.Default.Link,
            title = title,
            description = description,
            actionLabel = onAddLink?.let { "Add link" },
            onActionClick = onAddLink,
        )
    }
}
