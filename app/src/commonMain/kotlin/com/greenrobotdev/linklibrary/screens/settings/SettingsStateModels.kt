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
    /** Built and waiting for the screen to save; cleared once handed over. */
    val exportFile: ExportFile? = null,
    val exportMessage: String? = null,
)

/** A bookmarks file ready to save, and how many links it holds. */
class ExportFile(val fileName: String, val html: String, val linkCount: Int)

/** Events for the Settings screen. */
sealed interface SettingsEvent {
    data class SetThemeMode(val themeMode: ThemeMode) : SettingsEvent
    /** Build the bookmarks file from every saved link. */
    object Export : SettingsEvent
    /** The screen handed the file to the saver; [saved] = false if cancelled or it failed. */
    data class ExportFinished(val saved: Boolean, val linkCount: Int) : SettingsEvent
}
