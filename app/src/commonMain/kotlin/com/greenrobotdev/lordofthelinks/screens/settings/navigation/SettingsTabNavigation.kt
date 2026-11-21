package com.greenrobotdev.lordofthelinks.screens.settings.navigation

import com.greenrobotdev.lordofthelinks.screens.welcome.WelcomeTourScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.greenrobotdev.lordofthelinks.design.demo.DesignCatalogScreen
import com.greenrobotdev.lordofthelinks.screens.root.TabNavigator
import com.greenrobotdev.lordofthelinks.screens.root.rememberTabNavigationState
import com.greenrobotdev.lordofthelinks.screens.settings.SettingsScreen

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
        backStack = tabNavState.backStack,
                entryProvider = { route ->
            NavEntry(route) {
                when (route) {
                    is SettingsNavRoutes.SettingsBase -> SettingsScreen(
                        routeKey = route,
                        onOpenDesignCatalog = { tabNavigator.navigate(SettingsNavRoutes.DesignCatalog) },
                        onOpenTour = { tabNavigator.navigate(SettingsNavRoutes.WelcomeTour) },
                    )

                    is SettingsNavRoutes.WelcomeTour -> WelcomeTourScreen(
                        onDone = { tabNavigator.goBack() }
                    )

                    is SettingsNavRoutes.DesignCatalog -> DesignCatalogScreen(
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