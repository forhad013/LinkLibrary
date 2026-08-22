package com.greenrobotdev.linklibrary.screens.settings

import kotlinx.serialization.Serializable

/**
 * Settings item data model
 */
@Serializable
data class SettingItem(
    val id: String,
    val title: String,
    val iconType: String,
    val isEnabled: Boolean = true
)

/**
 * User profile data
 */
@Serializable
data class UserProfile(
    val name: String,
    val email: String
)

/**
 * State for Settings screen
 */
@Serializable
data class SettingsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userProfile: UserProfile? = null,
    val settingItems: List<SettingItem> = emptyList()
)

/**
 * Events for Settings screen
 */
sealed interface SettingsEvent {
    object LoadSettings : SettingsEvent
    data class NavigateToSetting(val settingId: String) : SettingsEvent
    object ClearError : SettingsEvent
}
