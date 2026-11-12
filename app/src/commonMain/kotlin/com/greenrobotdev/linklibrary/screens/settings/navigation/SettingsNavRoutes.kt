package com.greenrobotdev.linklibrary.screens.settings.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Settings Tab Navigation Routes
 * Manages all screens within the Settings tab
 */
sealed class SettingsNavRoutes : NavKey {
    // Base settings screen
    data object SettingsBase : SettingsNavRoutes()

    // Configuration screens
    data object ThemeSettings : SettingsNavRoutes()
    data object BackupSettings : SettingsNavRoutes()
    data object AccountSettings : SettingsNavRoutes()

    // About and help
    data object About : SettingsNavRoutes()
    data object Help : SettingsNavRoutes()
}