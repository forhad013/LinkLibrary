package com.greenrobotdev.linklibrary.screens.library.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.greenrobotdev.linklibrary.design.motion.LocalSharedTransitionScope
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.greenrobotdev.linklibrary.bookmarks.screens.add.AddLinkScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.details.LinkDetailScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.library.LibraryScreen
import com.greenrobotdev.linklibrary.screens.notes.NoteEditorScreen
import com.greenrobotdev.linklibrary.screens.root.TabNavigator
import com.greenrobotdev.linklibrary.screens.root.toEntries

/**
 * Library Tab Navigation - Manages all screens within the Library tab
 */
@Composable
fun LibraryTabNavigation(
    tabNavigator: TabNavigator,
    onAddLink: (String?) -> Unit = { initialUrl ->
        tabNavigator.navigateToNew(LibraryNavRoutes.AddLink(initialUrl))
    }
) {
    val tabNavState = tabNavigator.state

    // Shared-element host: lets a Feed card's thumbnail grow into the Link details badge
    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavDisplay(
                entries = tabNavState.toEntries { route ->
                    NavEntry(route) {
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
                },
                sharedTransitionScope = this@SharedTransitionLayout,
                onBack = { tabNavigator.goBack() },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}