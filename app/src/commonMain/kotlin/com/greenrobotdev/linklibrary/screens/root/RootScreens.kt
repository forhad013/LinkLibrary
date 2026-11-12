package com.greenrobotdev.linklibrary.screens.root

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * New Root Screens - Only 4 top-level tabs
 * Each tab manages its own independent navigation tree
 */
@Serializable
sealed class RootScreens : NavKey {

    // Home Tab - Dashboard and recent activity
    @Serializable
    data object HomeTab : RootScreens()
    // Library Tab - Bookmarks management


    @Serializable
    data object LibraryTab : RootScreens()

    // Organize Tab - Categories and tags management
    @Serializable
    data object OrganizeTab : RootScreens()


    // Settings Tab - App configuration
    @Serializable
    data object SettingsTab : RootScreens()
}