package com.greenrobotdev.linklibrary.screens.home.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Home Tab Navigation Routes
 * Manages all screens within the Home tab
 */
sealed class HomeNavRoutes : NavKey {
    // Base home screen - dashboard and recent activity
    data object HomeBase : HomeNavRoutes()

    // Quick actions
    data class QuickAdd(val initialUrl: String? = null) : HomeNavRoutes()
    data object QuickSearch : HomeNavRoutes()

    // Activity and analytics
    data object Activity : HomeNavRoutes()
    data object Stats : HomeNavRoutes()
}