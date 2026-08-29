package com.greenrobotdev.linklibrary.utils.settings

import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import platform.Foundation.NSUserDefaults

/**
 * iOS implementation of SettingsFactory
 */
actual object SettingsFactory {
    actual fun createSettings(): Settings {
        return Settings(NSUserDefaults.standardUserDefaults)
    }

    /**
     * Get the coroutine context for iOS (Main thread dispatcher)
     */
    fun getCoroutineContext() = Dispatchers.Main
}