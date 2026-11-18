package com.greenrobotdev.linklibrary.screens.settings.navigation

import androidx.navigation3.runtime.NavKey

/** Screens in the Settings tab. */
sealed class SettingsNavRoutes : NavKey {
    data object SettingsBase : SettingsNavRoutes()

    /** Developer component gallery; opened by long-pressing the version in About. */
    data object ComponentDemo : SettingsNavRoutes()
}
