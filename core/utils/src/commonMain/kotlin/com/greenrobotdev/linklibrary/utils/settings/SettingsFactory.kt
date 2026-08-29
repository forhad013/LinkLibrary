package com.greenrobotdev.linklibrary.utils.settings

import com.russhwolf.settings.Settings

/**
 * Factory interface for creating platform-specific Settings instances
 */
expect object SettingsFactory {
    /**
     * Creates a Settings instance for the current platform
     */
    fun createSettings(): Settings
}

/**
 * Creates a Settings instance with default configuration
 */
fun createPlatformSettings(): Settings = SettingsFactory.createSettings()