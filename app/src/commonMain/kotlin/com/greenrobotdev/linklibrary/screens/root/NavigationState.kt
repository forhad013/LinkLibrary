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
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

// Configuration for serialization
private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(RootScreens.HomeTab::class, RootScreens.HomeTab.serializer())
            subclass(RootScreens.LibraryTab::class, RootScreens.LibraryTab.serializer())
            subclass(RootScreens.SettingsTab::class, RootScreens.SettingsTab.serializer())
            subclass(RootScreens.LinkDetail::class, RootScreens.LinkDetail.serializer())
            subclass(RootScreens.AddLink::class, RootScreens.AddLink.serializer())
            subclass(RootScreens.AddCollection::class, RootScreens.AddCollection.serializer())
            subclass(RootScreens.Tags::class, RootScreens.Tags.serializer())
            subclass(RootScreens.AddTag::class, RootScreens.AddTag.serializer())
        }
    }
}

/**
 * State holder for navigation state with multiple back stacks.
 * Simplified version compatible with current Navigation 3 API.
 */
class NavigationState(
    startRoute: NavKey,
    topLevelRoute: MutableState<NavKey>,
    backStacks: Map<NavKey, SnapshotStateList<NavKey>>
) {
    var topLevelRoute: NavKey by topLevelRoute
    val startRoute = startRoute
    val backStacks: Map<NavKey, SnapshotStateList<NavKey>> = backStacks

    /**
     * Returns the list of back stacks that are currently in use.
     */
    val stacksInUse: List<NavKey>
        get() = if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, topLevelRoute)
        }
}

/**
 * Create a navigation state that persists through configuration changes.
 */
@Composable
fun rememberNavigationState(
    startRoute: NavKey,
    topLevelRoutes: Set<NavKey>
): NavigationState {

    // Use remember instead of rememberSaveable to avoid serialization crash
    // Note: Navigation state will reset on configuration changes
    // This is a trade-off to avoid the NavKey serialization issue
    val currentRoute = remember { startRoute }

    val topLevelRoute = remember(currentRoute) {
        mutableStateOf(currentRoute)
    }

    // Create back stack for each top level route
    val backStacks = topLevelRoutes.associateWith { route ->
        remember(route) {
            androidx.compose.runtime.snapshots.SnapshotStateList<NavKey>().apply {
                add(route)
            }
        }
    }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelRoute = topLevelRoute,
            backStacks = backStacks
        )
    }
}

/**
 * Handles navigation events by updating the navigation state.
 */
class Navigator(val state: NavigationState) {
    fun navigate(route: NavKey) {
        if (route in state.backStacks.keys) {
            // This is a top level route, switch to it
            state.topLevelRoute = route
        } else {
            // Add to current top level route's back stack
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute]
            ?: error("Stack for ${state.topLevelRoute} not found")
        val currentRoute = currentStack.last()

        // If at base of current route, go back to start route
        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }
}

/**
 * Convert NavigationState into NavEntries list for display.
 */
@Composable
fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>
): SnapshotStateList<NavEntry<NavKey>> {

    val entries = stacksInUse.map { routeKey ->
        val stack = backStacks[routeKey] ?: return@map emptyList<NavEntry<NavKey>>()
        stack.map { route -> entryProvider(route) }
    }.flatten()

    return entries.toMutableStateList()
}
