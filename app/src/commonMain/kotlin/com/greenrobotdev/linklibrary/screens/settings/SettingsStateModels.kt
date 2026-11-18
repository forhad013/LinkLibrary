package com.greenrobotdev.linklibrary.screens.settings

import kotlinx.serialization.Serializable

/**
 * Theme mode options for the application
 */
@Serializable
sealed class ThemeMode {
    @Serializable
    data object System : ThemeMode()

    @Serializable
    data object Light : ThemeMode()

    @Serializable
    data object Dark : ThemeMode()
}

/** State for the Settings screen: only settings that work today. */
data class SettingsState(
    val themeMode: ThemeMode = ThemeMode.System,
)

/** Events for the Settings screen. */
sealed interface SettingsEvent {
    data class SetThemeMode(val themeMode: ThemeMode) : SettingsEvent
}
