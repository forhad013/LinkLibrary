package com.greenrobotdev.linklibrary.utils.settings

import android.content.Context
import android.content.SharedPreferences
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Android implementation of SettingsFactory
 */
actual object SettingsFactory {
    private var applicationContext: Context? = null

    /**
     * Initialize the settings factory with application context
     * Should be called early in app startup, typically in Application.onCreate()
     */
    fun initialize(context: Context) {
        applicationContext = context.applicationContext
    }

    actual fun createSettings(): Settings {
        val context = applicationContext
            ?: throw IllegalStateException("SettingsFactory not initialized. Call SettingsFactory.initialize(context) first.")

        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "app_settings",
            Context.MODE_PRIVATE
        )

        // Use fallback implementation directly
        return createFallbackSettings(sharedPreferences)
    }

    /**
     * Get the coroutine context for Android (Main thread dispatcher)
     */
    fun getCoroutineContext(): CoroutineContext {
        return Dispatchers.Main
    }

    /**
     * Create a fallback Settings implementation if the library doesn't work
     */
    private fun createFallbackSettings(sharedPreferences: SharedPreferences): Settings {
        return object : Settings {
            override val keys: Set<String> get() = sharedPreferences.all?.keys ?: emptySet()
            override val size: Int get() = sharedPreferences.all?.size ?: 0

            override fun clear() {
                sharedPreferences.edit().clear().apply()
            }

            override fun remove(key: String) {
                sharedPreferences.edit().remove(key).apply()
            }

            override fun hasKey(key: String): Boolean = sharedPreferences.contains(key)

            override fun putBoolean(key: String, value: Boolean) {
                sharedPreferences.edit().putBoolean(key, value).apply()
            }

            override fun putInt(key: String, value: Int) {
                sharedPreferences.edit().putInt(key, value).apply()
            }

            override fun putLong(key: String, value: Long) {
                sharedPreferences.edit().putLong(key, value).apply()
            }

            override fun putString(key: String, value: String) {
                sharedPreferences.edit().putString(key, value).apply()
            }

            override fun putFloat(key: String, value: Float) {
                sharedPreferences.edit().putFloat(key, value).apply()
            }

            override fun putDouble(key: String, value: Double) {
                sharedPreferences.edit().putLong(key, value.toRawBits()).apply()
            }

            override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
                sharedPreferences.getBoolean(key, defaultValue)

            override fun getInt(key: String, defaultValue: Int): Int =
                sharedPreferences.getInt(key, defaultValue)

            override fun getLong(key: String, defaultValue: Long): Long =
                sharedPreferences.getLong(key, defaultValue)

            override fun getString(key: String, defaultValue: String): String =
                sharedPreferences.getString(key, defaultValue) ?: defaultValue

            override fun getFloat(key: String, defaultValue: Float): Float =
                sharedPreferences.getFloat(key, defaultValue)

            override fun getDouble(key: String, defaultValue: Double): Double =
                Double.fromBits(sharedPreferences.getLong(key, defaultValue.toRawBits()))

            override fun getBooleanOrNull(key: String): Boolean? =
                if (sharedPreferences.contains(key)) {
                    sharedPreferences.getBoolean(key, false)
                } else null

            override fun getIntOrNull(key: String): Int? =
                if (sharedPreferences.contains(key)) {
                    sharedPreferences.getInt(key, 0)
                } else null

            override fun getLongOrNull(key: String): Long? =
                if (sharedPreferences.contains(key)) {
                    sharedPreferences.getLong(key, 0L)
                } else null

            override fun getStringOrNull(key: String): String? =
                if (sharedPreferences.contains(key)) {
                    sharedPreferences.getString(key, null)
                } else null

            override fun getFloatOrNull(key: String): Float? =
                if (sharedPreferences.contains(key)) {
                    sharedPreferences.getFloat(key, 0f)
                } else null

            override fun getDoubleOrNull(key: String): Double? =
                if (sharedPreferences.contains(key)) {
                    Double.fromBits(sharedPreferences.getLong(key, 0L))
                } else null
        }
    }
}