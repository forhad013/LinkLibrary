package com.greenrobotdev.linklibrary.utils.settings

import com.russhwolf.settings.Settings

/**
 * WASM implementation of SettingsFactory
 *
 * Note: multiplatform-settings library is not available for WASM yet.
 * This is a stub implementation that throws an exception if settings are accessed.
 */
actual object SettingsFactory {

    private val unsupportedSettings = object : Settings {
        override fun keys(): Set<String> = emptySet()
        override fun size(): Int = 0
        override fun clear() {}
        override fun remove(key: String) {}
        override fun hasKey(key: String): Boolean = false
        override fun putBoolean(key: String, value: Boolean) {}
        override fun putInt(key: String, value: Int) {}
        override fun putLong(key: String, value: Long) {}
        override fun putString(key: String, value: String?) {}
        override fun putFloat(key: String, value: Float) {}
        override fun putDouble(key: String, value: Double) {}
        override fun getBoolean(key: String, defaultValue: Boolean): Boolean = defaultValue
        override fun getInt(key: String, defaultValue: Int): Int = defaultValue
        override fun getLong(key: String, defaultValue: Long): Long = defaultValue
        override fun getString(key: String, defaultValue: String?): String? = defaultValue
        override fun getFloat(key: String, defaultValue: Float): Float = defaultValue
        override fun getDouble(key: String, defaultValue: Double): Double = defaultValue
    }

    actual fun createSettings(): Settings {
        return unsupportedSettings
    }

    /**
     * Get the coroutine context for WASM
     */
    fun getCoroutineContext() = kotlinx.coroutines.Dispatchers.Default
}
