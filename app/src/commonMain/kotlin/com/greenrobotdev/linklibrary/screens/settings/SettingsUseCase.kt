package com.greenrobotdev.linklibrary.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.utils.preferences.ThemePreferenceManager
import kotlinx.coroutines.flow.Flow

/** Settings presenter: the saved theme, and saving a new one. */
@Composable
fun SettingsPresenter(events: Flow<SettingsEvent>): SettingsState {
    val themePreferences = remember { ThemePreferenceManager() }
    var state by remember {
        mutableStateOf(
            SettingsState(
                themeMode = when (themePreferences.getThemeMode()) {
                    is ThemePreferenceManager.ThemeMode.Light -> ThemeMode.Light
                    is ThemePreferenceManager.ThemeMode.Dark -> ThemeMode.Dark
                    is ThemePreferenceManager.ThemeMode.System -> ThemeMode.System
                },
            ),
        )
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is SettingsEvent.SetThemeMode -> {
                    themePreferences.setThemeMode(
                        when (event.themeMode) {
                            is ThemeMode.Light -> ThemePreferenceManager.ThemeMode.Light
                            is ThemeMode.Dark -> ThemePreferenceManager.ThemeMode.Dark
                            is ThemeMode.System -> ThemePreferenceManager.ThemeMode.System
                        },
                    )
                    state = state.copy(themeMode = event.themeMode)
                }
            }
        }
    }
    return state
}
