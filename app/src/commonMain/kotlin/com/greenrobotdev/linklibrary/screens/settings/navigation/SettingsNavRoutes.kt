package com.greenrobotdev.linklibrary.screens.settings.navigation

import androidx.navigation3.runtime.NavKey

/** Screens in the Settings tab. */
sealed class SettingsNavRoutes : NavKey {
    data object SettingsBase : SettingsNavRoutes()

    /** "How it works": the welcome tour, from About or its own row. */
    data object WelcomeTour : SettingsNavRoutes()

    /** Debug builds only: every reusable component of the design system. */
    data object DesignCatalog : SettingsNavRoutes()
}
