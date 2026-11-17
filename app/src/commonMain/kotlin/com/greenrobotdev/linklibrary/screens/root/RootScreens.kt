package com.greenrobotdev.linklibrary.screens.root

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Root Screens - 4 top-level tabs (GRE-49)
 */
@Serializable
sealed class RootScreens : NavKey {

    // Feed Tab - saved links feed (empty state until GRE-15)
    @Serializable
    data object FeedTab : RootScreens()

    // Search Tab - placeholder until GRE-19
    @Serializable
    data object SearchTab : RootScreens()

    // Library Tab - categories and tags (until GRE-46)
    @Serializable
    data object LibraryTab : RootScreens()

    // Settings Tab
    @Serializable
    data object SettingsTab : RootScreens()
}
