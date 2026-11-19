package com.greenrobotdev.lordofthelinks.screens.home.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.greenrobotdev.lordofthelinks.bookmarks.screens.add.AddLinkScreen
import com.greenrobotdev.lordofthelinks.screens.home.HomeScreen
import com.greenrobotdev.lordofthelinks.screens.root.TabNavigator
import com.greenrobotdev.lordofthelinks.screens.root.rememberTabNavigationState
import com.greenrobotdev.lordofthelinks.screens.root.toEntries

/**
 * Home Tab Navigation - Manages all screens within the Home tab
 */
@Composable
fun HomeTabNavigation(
    tabNavigator: TabNavigator
) {
    val tabNavState = tabNavigator.state

    NavDisplay(
        entries = tabNavState.toEntries { route ->
            NavEntry(route) {
                when (route) {
                    // Base Home Screen
                    is HomeNavRoutes.HomeBase -> HomeScreen(
                        routeKey = route,
                        onNavigateToDetail = { linkId ->
                            // Navigate to Library tab for detail view
                            // This will be handled by root navigation
                        },
                        onAddLink = { initialUrl ->
                            tabNavigator.navigateToNew(HomeNavRoutes.QuickAdd(initialUrl))
                        }
                    )

                    // Quick Add Screen
                    is HomeNavRoutes.QuickAdd -> AddLinkScreen(
                        routeKey = route,
                        initialUrl = route.initialUrl,
                        onBack = { tabNavigator.goBack() }
                    )

                    // Activity Screen
                    is HomeNavRoutes.Activity -> HomeScreen(
                        routeKey = route,
                        onNavigateToDetail = { /* Activity navigation */ },
                        onAddLink = { initialUrl ->
                            tabNavigator.navigateToNew(HomeNavRoutes.QuickAdd(initialUrl))
                        }
                    )

                    // Stats Screen
                    is HomeNavRoutes.Stats -> HomeScreen(
                        routeKey = route,
                        onNavigateToDetail = { /* Stats navigation */ },
                        onAddLink = { initialUrl ->
                            tabNavigator.navigateToNew(HomeNavRoutes.QuickAdd(initialUrl))
                        }
                    )

                    else -> error("Unknown home route: $route")
                }
            }
        },
        onBack = { tabNavigator.goBack() },
        modifier = Modifier.fillMaxSize()
    )
}