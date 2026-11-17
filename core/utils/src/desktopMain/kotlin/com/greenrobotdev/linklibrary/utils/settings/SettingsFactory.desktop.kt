package com.greenrobotdev.linklibrary.utils.settings

import com.russhwolf.settings.PropertiesSettings
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.util.Properties

/**
 * Desktop implementation of SettingsFactory
 */
actual object SettingsFactory {
    actual fun createSettings(): Settings {
        val settingsFile = File(System.getProperty("user.home"), ".linklibrary/settings.properties")
        settingsFile.parentFile?.mkdirs()
        if (!settingsFile.exists()) settingsFile.createNewFile()
        return settingsFile.inputStream().use { props ->
            val properties = Properties().apply { load(props) }
            PropertiesSettings(properties) { p ->
                settingsFile.outputStream().use { out -> p.store(out, null) }
            }
        }
    }

    /**
     * Get the coroutine context for Desktop (Main thread dispatcher)
     */
    fun getCoroutineContext() = Dispatchers.Main
}