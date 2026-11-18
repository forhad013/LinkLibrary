package com.greenrobotdev.linklibrary.screens.settings.navigation

import androidx.navigation3.runtime.NavKey

/** Screens in the Settings tab. */
sealed class SettingsNavRoutes : NavKey {
    data object SettingsBase : SettingsNavRoutes()

    /** Debug builds only: every reusable component of the design system. */
    data object DesignCatalog : SettingsNavRoutes()
}
