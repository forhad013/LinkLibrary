package com.greenrobotdev.linklibrary.screens.organize.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.greenrobotdev.linklibrary.bookmarks.screens.details.LinkDetailScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.saved.SavedListScreen
import com.greenrobotdev.linklibrary.design.motion.LocalSharedTransitionScope
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.greenrobotdev.linklibrary.bookmarks.screens.library.LibraryTabScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.manage.ManageScreen
import com.greenrobotdev.linklibrary.screens.root.TabNavigator
import com.greenrobotdev.linklibrary.screens.root.rememberTabNavigationState

/**
 * Organize Tab Navigation - Manages all screens within the Organize tab
 */
@Composable
fun OrganizeTabNavigation() {
    val tabNavState = rememberTabNavigationState(
        startRoute = OrganizeNavRoutes.OrganizeBase
    )

    val tabNavigator = remember { TabNavigator(tabNavState) }

    // Shared-element host: tiles grow into list headers, cards into Link details
    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavDisplay(
                backStack = tabNavState.backStack,
                entryProvider = { route ->
                    NavEntry(route) {
                        when (route) {
                            // Library tab base (design: library_categories_amp_tags)
                            is OrganizeNavRoutes.OrganizeBase -> LibraryTabScreen(
                                onManage = { tabNavigator.navigate(OrganizeNavRoutes.Manage) },
                                onOpenScope = { tabNavigator.navigate(OrganizeNavRoutes.SavedList(it)) },
                            )

                            is OrganizeNavRoutes.SavedList -> SavedListScreen(
                                scope = route.scope,
                                onBack = { tabNavigator.goBack() },
                                onOpenScope = { tabNavigator.navigate(OrganizeNavRoutes.SavedList(it)) },
                                onOpenDetails = { tabNavigator.navigate(OrganizeNavRoutes.LinkDetail(it)) },
                            )

                            is OrganizeNavRoutes.LinkDetail -> LinkDetailScreen(
                                routeKey = route,
                                linkId = route.linkId,
                                onBack = { tabNavigator.goBack() },
                            )

                            // Manage categories and tags
                            is OrganizeNavRoutes.Manage -> ManageScreen(
                                onBack = { tabNavigator.goBack() }
                            )

                            else -> error("Unknown organize route: $route")
                        }
                    }
                },
                sharedTransitionScope = this@SharedTransitionLayout,
                onBack = { tabNavigator.goBack() },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}