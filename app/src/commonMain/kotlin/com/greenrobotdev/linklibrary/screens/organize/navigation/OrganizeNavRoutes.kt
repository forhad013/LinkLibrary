package com.greenrobotdev.linklibrary.screens.organize.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Organize Tab Navigation Routes
 * Manages all screens within the Organize tab
 */
sealed class OrganizeNavRoutes : NavKey {
    // Base organize screen - categoryNames and tags overview
    data object OrganizeBase : OrganizeNavRoutes()

    // Manage categories and tags (rename/move/delete)
    data object Manage : OrganizeNavRoutes()
}