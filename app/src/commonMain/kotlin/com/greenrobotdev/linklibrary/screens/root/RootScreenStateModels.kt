package com.greenrobotdev.linklibrary.screens.root

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class RootScreens : NavKey {
    // Bottom Navigation Tabs
    @Serializable
    data object HomeTab : RootScreens()

    @Serializable
    data object LibraryTab : RootScreens()

    @Serializable
    data object SettingsTab : RootScreens()

    // Detail Screens
    @Serializable
    data class LinkDetail(val linkId: String) : RootScreens()

    @Serializable
    data class AddLink(val initialUrl: String? = null) : RootScreens()

    @Serializable
    data object AddCollection : RootScreens()

    // Demo/Utility Screens
    @Serializable
    data object AIAssistantDemo : RootScreens()

    @Serializable
    data object Collections : RootScreens()

    // Note Editor
    @Serializable
    data class NoteEditor(val noteId: String? = null) : RootScreens()
}

// Bottom navigation items data class
data class BottomNavItem(
    val route: RootScreens,
    val title: String,
    val icon: String
)

val bottomNavItems = listOf(
    BottomNavItem(RootScreens.HomeTab, "Home", "home"),
    BottomNavItem(RootScreens.LibraryTab, "Library", "library_books"),
    BottomNavItem(RootScreens.SettingsTab, "Settings", "settings")
)
