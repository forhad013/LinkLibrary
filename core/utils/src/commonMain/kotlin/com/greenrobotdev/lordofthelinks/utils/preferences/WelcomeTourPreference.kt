package com.greenrobotdev.lordofthelinks.utils.preferences

import com.greenrobotdev.lordofthelinks.utils.settings.createPlatformSettings
import com.russhwolf.settings.Settings

/** Whether this device has been through the welcome tour, so first launch shows it only once. */
class WelcomeTourPreference(private val settings: Settings = createPlatformSettings()) {
    val seen: Boolean get() = settings.getBoolean(KEY, false)

    fun markSeen() = settings.putBoolean(KEY, true)

    private companion object {
        const val KEY = "welcome_tour_seen"
    }
}
