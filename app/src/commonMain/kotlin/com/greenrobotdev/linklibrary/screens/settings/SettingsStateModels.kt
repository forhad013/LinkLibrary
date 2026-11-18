package com.greenrobotdev.linklibrary.screens.settings

import com.greenrobotdev.linklibrary.bookmarks.backup.ExportedFile
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
    val pendingFile: ExportedFile? = null,
    /** Backing up or restoring; the buttons wait. */
    val isWorking: Boolean = false,
    /** Result of the last backup, restore or export, shown under the buttons. */
    val dataMessage: String? = null,
)

/** Events for the Settings screen. */
sealed interface SettingsEvent {
    data class SetThemeMode(val themeMode: ThemeMode) : SettingsEvent
    /** Full JSON backup of every link, category and tag. */
    object Backup : SettingsEvent
    /** Browser bookmarks (HTML) for Chrome, Firefox and other apps. */
    object ExportForBrowsers : SettingsEvent
    /** The screen handed the file to the saver; [saved] = false if cancelled or it failed. */
    data class FileSaved(val saved: Boolean, val linkCount: Int, val isBackup: Boolean) : SettingsEvent
    /** A backup file the user picked, as text; null if nothing was picked or it couldn't be read. */
    data class Restore(val text: String?) : SettingsEvent
}
