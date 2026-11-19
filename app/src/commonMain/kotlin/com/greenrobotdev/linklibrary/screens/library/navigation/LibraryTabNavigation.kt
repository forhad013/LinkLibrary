package com.greenrobotdev.linklibrary.screens.library.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.greenrobotdev.linklibrary.design.motion.LocalSharedTransitionScope
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.greenrobotdev.linklibrary.bookmarks.screens.add.AddLinkScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.details.LinkDetailScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.library.LibraryScreen
import com.greenrobotdev.linklibrary.screens.notes.NoteEditorScreen
import com.greenrobotdev.linklibrary.screens.root.DetailPanePlaceholder
import com.greenrobotdev.linklibrary.screens.root.TabNavigator

/**
 * Library Tab Navigation - Manages all screens within the Library tab
 *
 * On expanded window sizes (desktop, tablet) the Feed and Link details render side by side as a
 * list-detail scaffold; on compact windows it stays the single-pane stack navigation.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun LibraryTabNavigation(
    tabNavigator: TabNavigator,
    onAddLink: (String?) -> Unit = { initialUrl ->
        tabNavigator.navigateToNew(LibraryNavRoutes.AddLink(initialUrl))
    }
) {
    val tabNavState = tabNavigator.state

    // Yields to single-pane on compact windows (calculateScene returns null when the scaffold
    // can't show 2+ panes), so phones keep the existing full-screen navigation.
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

    // Shared-element morphs only make sense single-pane (one screen hides as the other shows).
    // With list and detail visible at once the same keys would fight over their bounds forever.
    // Same signal the strategy uses: >1 horizontal partition means two panes fit side by side.
    val twoPanesFit = calculatePaneScaffoldDirective(currentWindowAdaptiveInfoV2())
        .maxHorizontalPartitions > 1

    if (twoPanesFit) {
        NavDisplay(
            backStack = tabNavState.backStack,
            sceneStrategies = listOf(listDetailStrategy),
            entryProvider = { route ->
                // Pane metadata drives the list-detail scaffold: Feed = list, details = detail.
                // Routes without metadata (AddLink, NoteEditor) fall through to single-pane.
                val paneMetadata = when (route) {
                    is LibraryNavRoutes.LibraryBase -> ListDetailSceneStrategy.listPane(
                        detailPlaceholder = { DetailPanePlaceholder(onAddLink = { onAddLink(null) }) }
                    )
                    is LibraryNavRoutes.LinkDetail -> ListDetailSceneStrategy.detailPane()
                    else -> emptyMap()
                }
                NavEntry(route, metadata = paneMetadata) {
                    LibraryTabEntry(route, tabNavigator, onAddLink)
                }
            },
            onBack = { tabNavigator.goBack() },
            modifier = Modifier.fillMaxSize()
        )
    } else {
        // Shared-element host: lets a Feed card's thumbnail grow into the Link details badge
        SharedTransitionLayout {
            CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                NavDisplay(
                    backStack = tabNavState.backStack,
                    entryProvider = { route ->
                        NavEntry(route) {
                            LibraryTabEntry(route, tabNavigator, onAddLink)
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
private fun LibraryTabEntry(
    route: NavKey,
    tabNavigator: TabNavigator,
    onAddLink: (String?) -> Unit
) {
    when (route) {
        // Base screen — the Feed (list + empty state)
        is LibraryNavRoutes.LibraryBase -> LibraryScreen(
            routeKey = route,
            onNavigateToDetail = { linkId ->
                tabNavigator.navigate(LibraryNavRoutes.LinkDetail(linkId))
            },
            onAddLink = onAddLink
        )

        // Link Detail Screen
        is LibraryNavRoutes.LinkDetail -> LinkDetailScreen(
            routeKey = route,
            linkId = route.linkId,
            onBack = { tabNavigator.goBack() },
            onNavigateToNoteEditor = { linkId ->
                tabNavigator.navigate(LibraryNavRoutes.NoteEditor(null))
            }
        )

        // Add Link Screen
        is LibraryNavRoutes.AddLink -> AddLinkScreen(
            routeKey = route,
            initialUrl = route.initialUrl,
            onBack = { tabNavigator.goBack() },
            // Already saved: swap this screen for that link's details
            onOpenDetails = { linkId ->
                tabNavigator.goBack()
                tabNavigator.navigate(LibraryNavRoutes.LinkDetail(linkId))
            },
        )

        // Note Editor Screen
        is LibraryNavRoutes.NoteEditor -> NoteEditorScreen(
            routeKey = route,
            noteId = route.noteId,
            onBack = { tabNavigator.goBack() }
        )

        else -> error("Unknown library route: $route")
    }
}