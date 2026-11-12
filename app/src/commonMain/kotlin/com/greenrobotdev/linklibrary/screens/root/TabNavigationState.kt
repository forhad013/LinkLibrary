package com.greenrobotdev.linklibrary.screens.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey

/**
 * State holder for a single tab's navigation
 * Each tab has its own independent navigation state and back stack
 */
class TabNavigationState(
    startRoute: NavKey,
    currentRouteState: MutableState<NavKey>,
    backStack: SnapshotStateList<NavKey>
) {
    var currentRoute: NavKey by currentRouteState
    val startRoute = startRoute
    val backStack: SnapshotStateList<NavKey> = backStack

    /**
     * Check if we can go back in this tab's navigation
     */
    fun canGoBack(): Boolean {
        return backStack.size > 1
    }
}

/**
 * Create navigation state for a single tab
 */
@Composable
fun rememberTabNavigationState(
    startRoute: NavKey
): TabNavigationState {
    val currentRoute = remember { mutableStateOf(startRoute) }

    val backStack = remember {
        SnapshotStateList<NavKey>().apply {
            add(startRoute)
        }
    }

    return remember(startRoute) {
        TabNavigationState(
            startRoute = startRoute,
            currentRouteState = currentRoute,
            backStack = backStack
        )
    }
}

/**
 * Navigator for managing navigation within a single tab
 */
class TabNavigator(val state: TabNavigationState) {
    /**
     * Navigate to a new route within this tab
     */
    fun navigate(route: NavKey) {
        state.backStack.add(route)
        state.currentRoute = route
    }

    /**
     * Navigate to a new route, replacing the last one if it's the same type
     */
    fun navigateToNew(route: NavKey) {
        if (state.backStack.isNotEmpty()) {
            val lastRoute = state.backStack.last()
            if (lastRoute::class == route::class) {
                state.backStack.removeLast()
            }
        }

        state.backStack.add(route)
        state.currentRoute = route
    }

    /**
     * Go back in this tab's navigation
     */
    fun goBack() {
        if (state.backStack.size > 1) {
            state.backStack.removeLast()
            state.currentRoute = state.backStack.last()
        }
    }
}

/**
 * Convert TabNavigationState into NavEntries list for display
 */
@Composable
fun TabNavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>
): SnapshotStateList<NavEntry<NavKey>> {
    val entries = this.backStack.map { route -> entryProvider(route) }
    return entries.toMutableStateList()
}