package com.greenrobotdev.linklibrary.screens.organize.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.greenrobotdev.linklibrary.bookmarks.screens.details.LinkDetailScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.saved.SavedListScreen
import com.greenrobotdev.linklibrary.design.motion.LocalSharedTransitionScope
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.greenrobotdev.linklibrary.bookmarks.screens.library.LibraryTabScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.manage.ManageScreen
import com.greenrobotdev.linklibrary.screens.root.TabNavigator
import com.greenrobotdev.linklibrary.screens.root.DetailPanePlaceholder
import com.greenrobotdev.linklibrary.screens.root.rememberTabNavigationState

/**
 * Organize Tab Navigation - Manages all screens within the Organize tab
 *
 * On expanded window sizes the saved list and link details render side by side; compact windows
 * keep single-pane navigation with shared-element morphs.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun OrganizeTabNavigation() {
    val tabNavState = rememberTabNavigationState(
        startRoute = OrganizeNavRoutes.OrganizeBase
    )

    val tabNavigator = remember { TabNavigator(tabNavState) }

    // Shared-element morphs only make sense single-pane. With list and detail visible at once the
    // same keys would fight over their bounds forever (layout write loop, 100% CPU).
    // Same signal the strategy uses: >1 horizontal partition means two panes fit side by side.
    val twoPanesFit = calculatePaneScaffoldDirective(currentWindowAdaptiveInfoV2())
        .maxHorizontalPartitions > 1

    if (twoPanesFit) {
        NavDisplay(
            backStack = tabNavState.backStack,
            sceneStrategies = listOf(rememberListDetailSceneStrategy<NavKey>()),
            entryProvider = { route ->
                // Categories and tags = list, saved list = detail, link details = extra. Two panes
                // show the last two: Library | saved list, then saved list | link details.
                // Manage has no metadata, so it stays full-screen.
                val paneMetadata = when (route) {
                    is OrganizeNavRoutes.OrganizeBase -> ListDetailSceneStrategy.listPane(
                        detailPlaceholder = {
                            DetailPanePlaceholder(
                                title = "Pick a category or tag",
                                description = "Its saved links show here.",
                            )
                        }
                    )
                    is OrganizeNavRoutes.SavedList -> ListDetailSceneStrategy.detailPane()
                    is OrganizeNavRoutes.LinkDetail -> ListDetailSceneStrategy.extraPane()
                    else -> emptyMap()
                }
                NavEntry(route, metadata = paneMetadata) {
                    OrganizeTabEntry(route, tabNavigator)
                }
            },
            onBack = { tabNavigator.goBack() },
            modifier = Modifier.fillMaxSize()
        )
    } else {
        // Shared-element host: tiles grow into list headers, cards into Link details
        SharedTransitionLayout {
            CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                NavDisplay(
                    backStack = tabNavState.backStack,
                    entryProvider = { route ->
                        NavEntry(route) {
                            OrganizeTabEntry(route, tabNavigator)
                        }
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    onBack = { tabNavigator.goBack() },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun OrganizeTabEntry(route: NavKey, tabNavigator: TabNavigator) {
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
