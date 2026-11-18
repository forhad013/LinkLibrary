package com.greenrobotdev.linklibrary.screens.organize.navigation

import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.linklibrary.bookmarks.screens.saved.SavedScope

/**
 * Organize Tab Navigation Routes
 * Manages all screens within the Organize tab
 */
sealed class OrganizeNavRoutes : NavKey {
    // Base organize screen - categoryNames and tags overview
    data object OrganizeBase : OrganizeNavRoutes()

    // Manage categories and tags (rename/move/delete)
    data object Manage : OrganizeNavRoutes()

    // Saved links of one smart list, category (with subcategories) or tag
    data class SavedList(val scope: SavedScope) : OrganizeNavRoutes()

    // A link's details, opened from a saved list
    data class LinkDetail(val linkId: String) : OrganizeNavRoutes()
}