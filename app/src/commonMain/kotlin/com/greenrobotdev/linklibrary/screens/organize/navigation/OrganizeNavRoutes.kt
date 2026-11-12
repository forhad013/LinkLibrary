package com.greenrobotdev.linklibrary.screens.organize.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Organize Tab Navigation Routes
 * Manages all screens within the Organize tab
 */
sealed class OrganizeNavRoutes : NavKey {
    // Base organize screen - categoryNames and tags overview
    data object OrganizeBase : OrganizeNavRoutes()

    // Category management
    data class CategoryDetail(val categoryId: String) : OrganizeNavRoutes()
    data class CategoryEdit(val categoryId: String? = null) : OrganizeNavRoutes()

    // Tag management
    data class TagDetail(val tagId: String) : OrganizeNavRoutes()
    data class TagEdit(val tagId: String? = null) : OrganizeNavRoutes()

    // Bulk operations
    data object BulkOrganize : OrganizeNavRoutes()

    // Manage categories and tags (rename/move/delete)
    data object Manage : OrganizeNavRoutes()

    // Move a category: reuse the category picker to choose the new parent
    data class MoveCategory(val categoryId: String) : OrganizeNavRoutes()
}