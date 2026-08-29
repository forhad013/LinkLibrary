package com.greenrobotdev.linklibrary.utils.settings

import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import java.io.File

/**
 * Desktop implementation of SettingsFactory
 */
actual object SettingsFactory {
    actual fun createSettings(): Settings {
        val settingsFile = File(System.getProperty("user.home"), ".linklibrary/settings.properties")
        settingsFile.parentFile?.mkdirs()
        return Settings(settingsFile)
    }

    /**
     * Get the coroutine context for Desktop (Main thread dispatcher)
     */
    fun getCoroutineContext() = Dispatchers.Main
}