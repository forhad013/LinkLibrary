package com.greenrobotdev.linklibrary.utils.preferences

import com.russhwolf.settings.Settings
import com.greenrobotdev.linklibrary.utils.settings.createPlatformSettings

/**
 * Theme preference manager using multiplatform-settings for cross-platform storage
 *
 * Handles theme preference persistence
 */
class ThemePreferenceManager {

    private val settings: Settings = createPlatformSettings()

    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_THEME_MODE_INT = "theme_mode_int"

        // Theme mode values
        private const val THEME_MODE_SYSTEM = 0
        private const val THEME_MODE_LIGHT = 1
        private const val THEME_MODE_DARK = 2
    }

    /**
     * Sealed class representing theme modes
     */
    sealed class ThemeMode {
        data object System : ThemeMode()
        data object Light : ThemeMode()
        data object Dark : ThemeMode()
    }

    /**
     * Get the current theme mode preference
     * Defaults to System theme if no preference is set
     */
    fun getThemeMode(): ThemeMode {
        val modeInt = settings.getIntOrNull(KEY_THEME_MODE_INT)
        return when (modeInt) {
            THEME_MODE_LIGHT -> ThemeMode.Light
            THEME_MODE_DARK -> ThemeMode.Dark
            else -> ThemeMode.System
        }
    }

    /**
     * Set the theme mode preference
     */
    fun setThemeMode(mode: ThemeMode) {
        val modeInt = when (mode) {
            ThemeMode.System -> THEME_MODE_SYSTEM
            ThemeMode.Light -> THEME_MODE_LIGHT
            ThemeMode.Dark -> THEME_MODE_DARK
        }
        settings.putInt(KEY_THEME_MODE_INT, modeInt)
    }

    /**
     * Clear all theme preferences
     */
    fun clearThemePreferences() {
        settings.remove(KEY_THEME_MODE_INT)
    }

    /**
     * Check if dark mode should be enabled based on current preference
     * Returns null if system default should be used
     */
    fun isDarkModeEnabled(): Boolean? {
        val mode = getThemeMode()
        return when (mode) {
            ThemeMode.System -> null
            ThemeMode.Light -> false
            ThemeMode.Dark -> true
        }
    }

    /**
     * Helper to convert boolean darkTheme preference to ThemeMode
     */
    fun darkThemeToMode(isDarkTheme: Boolean?): ThemeMode {
        return when (isDarkTheme) {
            true -> ThemeMode.Dark
            false -> ThemeMode.Light
            null -> ThemeMode.System
        }
    }

    /**
     * Helper to convert ThemeMode to boolean darkTheme preference
     */
    fun modeToDarkTheme(mode: ThemeMode): Boolean? {
        return when (mode) {
            ThemeMode.System -> null
            ThemeMode.Light -> false
            ThemeMode.Dark -> true
        }
    }
}