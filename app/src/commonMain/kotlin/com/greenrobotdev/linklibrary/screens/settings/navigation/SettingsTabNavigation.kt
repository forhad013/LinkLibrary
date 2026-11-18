package com.greenrobotdev.linklibrary.screens.settings.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.greenrobotdev.linklibrary.design.demo.ComponentDemoScreen
import com.greenrobotdev.linklibrary.screens.root.TabNavigator
import com.greenrobotdev.linklibrary.screens.root.rememberTabNavigationState
import com.greenrobotdev.linklibrary.screens.root.toEntries
import com.greenrobotdev.linklibrary.screens.settings.SettingsScreen

/**
 * Settings Tab Navigation - Manages all screens within the Settings tab
 */
@Composable
fun SettingsTabNavigation() {
    val tabNavState = rememberTabNavigationState(
        startRoute = SettingsNavRoutes.SettingsBase
    )

    val tabNavigator = remember { TabNavigator(tabNavState) }

    NavDisplay(
        entries = tabNavState.toEntries { route ->
            NavEntry(route) {
                when (route) {
                    is SettingsNavRoutes.SettingsBase -> SettingsScreen(
                        routeKey = route,
                        onOpenComponentDemo = { tabNavigator.navigate(SettingsNavRoutes.ComponentDemo) },
                    )

                    is SettingsNavRoutes.ComponentDemo -> ComponentDemoScreen(
                        onBack = { tabNavigator.goBack() }
                    )

                    else -> error("Unknown settings route: $route")
                }
            }
        },
        onBack = { tabNavigator.goBack() },
        modifier = Modifier.fillMaxSize()
    )
}