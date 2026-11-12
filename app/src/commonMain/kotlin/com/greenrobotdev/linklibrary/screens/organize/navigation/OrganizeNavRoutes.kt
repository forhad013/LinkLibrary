package com.greenrobotdev.linklibrary.screens.organize.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Organize Tab Navigation Routes
 * Manages all screens within the Organize tab
 */
sealed class OrganizeNavRoutes : NavKey {
    // Base organize screen - collections and tags overview
    data object OrganizeBase : OrganizeNavRoutes()

    // Collection management
    data class CollectionDetail(val collectionId: String) : OrganizeNavRoutes()
    data class CollectionEdit(val collectionId: String? = null) : OrganizeNavRoutes()

    // Tag management
    data class TagDetail(val tagId: String) : OrganizeNavRoutes()
    data class TagEdit(val tagId: String? = null) : OrganizeNavRoutes()

    // Bulk operations
    data object BulkOrganize : OrganizeNavRoutes()
}