package com.greenrobotdev.linklibrary.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.Flow
import com.greenrobotdev.linklibrary.utils.preferences.ThemePreferenceManager

/**
 * Use case for Settings screen
 * Loads user profile and settings items
 * Note: Currently using static data, can be extended to use UserRepository when available
 */
@Composable
fun SettingsPresenter(
    initialState: SettingsState,
    events: Flow<SettingsEvent>
): SettingsState {
    var state = remember { mutableStateOf(initialState) }
    val themePreferenceManager = remember { ThemePreferenceManager() }

    // Load settings on init
    LaunchedEffect(Unit) {
        state.value = state.value.copy(
            isLoading = true,
            error = null
        )
        try {
            // TODO: Replace with repository calls when UserRepository is available
            // val userProfile = userRepository.getProfile()
            // val settings = userRepository.getSettings()
            val userProfile = loadStaticUserProfile()
            val settingItems = loadStaticSettings()

            // Load theme preference from storage
            val currentThemeMode = themePreferenceManager.getThemeMode()

            state.value = state.value.copy(
                isLoading = false,
                userProfile = userProfile,
                settingItems = settingItems,
                themeMode = when (currentThemeMode) {
                    is ThemePreferenceManager.ThemeMode.Light -> ThemeMode.Light
                    is ThemePreferenceManager.ThemeMode.Dark -> ThemeMode.Dark
                    is ThemePreferenceManager.ThemeMode.System -> ThemeMode.System
                }
            )
        } catch (e: Exception) {
            state.value = state.value.copy(
                isLoading = false,
                error = e.message ?: "Failed to load settings"
            )
        }
    }

    // Handle events
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is SettingsEvent.LoadSettings -> {
                    state.value = state.value.copy(isLoading = true, error = null)
                    try {
                        val userProfile = loadStaticUserProfile()
                        val settingItems = loadStaticSettings()

                        // Load theme preference from storage
                        val currentThemeMode = themePreferenceManager.getThemeMode()

                        state.value = state.value.copy(
                            isLoading = false,
                            userProfile = userProfile,
                            settingItems = settingItems,
                            themeMode = when (currentThemeMode) {
                                is ThemePreferenceManager.ThemeMode.Light -> ThemeMode.Light
                                is ThemePreferenceManager.ThemeMode.Dark -> ThemeMode.Dark
                                is ThemePreferenceManager.ThemeMode.System -> ThemeMode.System
                            }
                        )
                    } catch (e: Exception) {
                        state.value = state.value.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load settings"
                        )
                    }
                }

                is SettingsEvent.NavigateToSetting -> {
                    // Navigation is handled by the screen, nothing to do here
                    // This event can be used for analytics or logging
                }

                is SettingsEvent.ClearError -> {
                    state.value = state.value.copy(error = null)
                }

                is SettingsEvent.SetThemeMode -> {
                    // Update theme preference in storage
                    val preferenceMode = when (event.themeMode) {
                        is ThemeMode.Light -> ThemePreferenceManager.ThemeMode.Light
                        is ThemeMode.Dark -> ThemePreferenceManager.ThemeMode.Dark
                        is ThemeMode.System -> ThemePreferenceManager.ThemeMode.System
                    }

                    themePreferenceManager.setThemeMode(preferenceMode)

                    // Update state
                    state.value = state.value.copy(themeMode = event.themeMode)
                }
            }
        }
    }

    return state.value
}

/**
 * Static user profile data
 * TODO: Remove this when UserRepository is implemented
 */
private fun loadStaticUserProfile(): UserProfile {
    return UserProfile(
        name = "John Doe",
        email = "john.doe@example.com"
    )
}

/**
 * Static settings items data
 * TODO: Remove this when UserRepository is implemented
 */
private fun loadStaticSettings(): List<SettingItem> = listOf(
    SettingItem(
        id = "reading_preferences",
        title = "Reading Preferences",
        iconType = "text_fields"
    ),
    SettingItem(
        id = "notifications",
        title = "Notifications",
        iconType = "notifications"
    ),
    SettingItem(
        id = "sync_backup",
        title = "Sync & Backup",
        iconType = "sync"
    ),
    SettingItem(
        id = "privacy_security",
        title = "Privacy & Security",
        iconType = "lock"
    ),
    SettingItem(
        id = "about",
        title = "About",
        iconType = "info"
    )
)
