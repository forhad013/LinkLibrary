package com.greenrobotdev.linklibrary.bookmarks.screens.tags

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.linklibrary.bookmarks.screens.tags.components.*

/**
 * Enhanced Tags Overview Screen - Material 3 Expressive Design
 *
 * Simple and clean tags overview screen matching lords_of_links_tags HTML design:
 * - Clean horizontal wrapping tag cloud layout
 * - Simple header with app title and action icons
 * - Featured tags with primary-container background
 * - Standard tags with surface-container-high background
 * - Outlined tags with border
 * - Usage count badges
 * - Material 3 Expressive animations
 * - Fixed FAB for tag creation
 *
 * This screen has been refactored into logical components for better maintainability:
 * - TagsView: Handles screen-level logic and state management
 * - TagsScreenScaffold: Provides the standard Material 3 layout
 * - Individual components for each section (TagsTopBar, TagsHeader, TagsContent, etc.)
 */
@Composable
fun TagsScreenEnhanced(
    routeKey: NavKey,
    onBack: () -> Unit,
    onCreateTag: () -> Unit = {},
    onManageTags: () -> Unit = {}
) {
    TagsView(
        routeKey = routeKey,
        onCreateTag = onCreateTag,
        onManageTags = onManageTags
    ) { state, onCreateTagCallback, onManageTagsCallback, modifier ->

        TagsScreenScaffold(
            onCreateTag = onCreateTagCallback,
            modifier = modifier
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Header Section
                TagsHeader()

                // Manage Tags Action Button
                TagsManageButton(onClick = onManageTagsCallback)

                // Tags Content (Empty state or Tags Cloud)
                TagsContent(
                    state = state,
                    onTagClick = { /* Navigate to tag detail */ }
                )
            }
        }
    }
}