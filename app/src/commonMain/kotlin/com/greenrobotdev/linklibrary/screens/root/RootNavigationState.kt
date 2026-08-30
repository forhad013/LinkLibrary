package com.greenrobotdev.linklibrary.screens.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * Root Navigation State - Manages the 4 top-level tabs
 * Each tab maintains its own independent navigation state
 */
class RootNavigationState(
    startTab: RootScreens,
    currentTab: MutableState<RootScreens>
) {
    var currentTab: RootScreens by currentTab
    val startTab = startTab

    /**
     * Get all available tabs
     */
    val tabs: List<RootScreens>
        get() = listOf(
            RootScreens.LibraryTab,
            RootScreens.OrganizeTab,
            RootScreens.HomeTab,
            RootScreens.SettingsTab
        )
}

/**
 * Create root navigation state for managing tabs
 */
@Composable
fun rememberRootNavigationState(
    startTab: RootScreens = RootScreens.HomeTab
): RootNavigationState {
    val currentTab = remember { mutableStateOf(startTab) }

    return remember(startTab) {
        RootNavigationState(
            startTab = startTab,
            currentTab = currentTab
        )
    }
}

/**
 * Root Navigator - Manages tab switching
 */
class RootNavigator(val state: RootNavigationState) {
    /**
     * Switch to a different tab
     */
    fun switchTab(tab: RootScreens) {
        state.currentTab = tab
    }

    /**
     * Get the current active tab
     */
    fun getCurrentTab(): RootScreens {
        return state.currentTab
    }

    /**
     * Check if a tab is currently active
     */
    fun isTabActive(tab: RootScreens): Boolean {
        return state.currentTab == tab
    }
}