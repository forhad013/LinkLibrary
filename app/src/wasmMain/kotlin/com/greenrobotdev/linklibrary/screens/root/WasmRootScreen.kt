package com.greenrobotdev.linklibrary.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.greenrobotdev.linklibrary.bookmarks.model.SharedContent
import com.greenrobotdev.linklibrary.bookmarks.screens.add.AddLinkScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.addCollection.AddCollectionScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.addTag.AddTagScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.collections.CollectionsScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.details.LinkDetailScreen
import com.greenrobotdev.linklibrary.screens.home.HomeScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.library.LibraryScreen
import com.greenrobotdev.linklibrary.screens.notes.NoteEditorScreen
import com.greenrobotdev.linklibrary.screens.settings.SettingsScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.tags.TagsScreen

// Simplified version for WASM prototype - no serialization, no AI assistant
@Composable
fun RootScreen(
    sharedContent: SharedContent? = null,
    onSharedContentHandled: () -> Unit = {}
) {
    val navigationState = rememberNavigationState(
        startRoute = RootScreens.HomeTab,
        topLevelRoutes = TOP_LEVEL_ROUTES.keys
    )

    val navigator = remember { Navigator(navigationState) }

    // Determine if we should show bottom navigation (hide on detail/add screens)
    val currentRoute = navigationState.backStacks[navigationState.topLevelRoute]?.last()
    val showBottomNav = currentRoute !is RootScreens.LinkDetail &&
                       currentRoute !is RootScreens.AddLink &&
                       currentRoute !is RootScreens.AddCollection &&
                       currentRoute !is RootScreens.NoteEditor &&
                       currentRoute !is RootScreens.Tags &&
                       currentRoute !is RootScreens.AddTag

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeDrawingPadding(),
        bottomBar = {
            if (showBottomNav) {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    TOP_LEVEL_ROUTES.forEach { (key, value) ->
                        val isSelected = key == navigationState.topLevelRoute
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { navigator.navigate(key) },
                            icon = {
                                Icon(
                                    imageVector = value.icon,
                                    contentDescription = value.title
                                )
                            },
                            label = { Text(value.title) }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (showBottomNav) {
                FloatingActionButton(
                    onClick = { navigator.navigate(RootScreens.AddLink(null)) },
                    containerColor = MaterialTheme.colorScheme.tertiary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Link")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavDisplay(
                entries = navigationState.toEntries { route ->
                    NavEntry(route) {
                        when (route) {
                            // Home Tab
                            is RootScreens.HomeTab -> HomeScreen(
                                routeKey = route,
                                onNavigateToDetail = { linkId -> navigator.navigate(RootScreens.LinkDetail(linkId)) },
                                onAddLink = { initialUrl -> navigator.navigate(RootScreens.AddLink(initialUrl)) }
                            )

                            // Library Tab
                            is RootScreens.LibraryTab -> LibraryScreen(
                                routeKey = route,
                                onNavigateToDetail = { linkId -> navigator.navigate(RootScreens.LinkDetail(linkId)) },
                                onAddLink = { initialUrl -> navigator.navigate(RootScreens.AddLink(initialUrl)) },
                                onAddCollection = { navigator.navigate(RootScreens.AddCollection) },
                                onNavigateToCollections = { navigator.navigate(RootScreens.Collections) },
                                onNavigateToTags = { navigator.navigate(RootScreens.Tags) }
                            )

                            // Settings Tab (simplified for WASM - no AI)
                            is RootScreens.SettingsTab -> SettingsScreen(
                                routeKey = route,
                                onNavigateToAI = {} // No AI assistant in WASM prototype
                            )

                            // Detail Screens (shared across all tabs)
                            is RootScreens.LinkDetail -> LinkDetailScreen(
                                routeKey = route,
                                linkId = route.linkId,
                                onBack = { navigator.goBack() },
                                onNavigateToNoteEditor = { linkId ->
                                    navigator.navigate(RootScreens.NoteEditor(null))
                                }
                            )

                            is RootScreens.AddLink -> AddLinkScreen(
                                routeKey = route,
                                initialUrl = route.initialUrl,
                                onBack = { navigator.goBack() }
                            )

                            is RootScreens.AddCollection -> AddCollectionScreen(
                                routeKey = route,
                                onBack = { navigator.goBack() }
                            )

                            // Collections Screen
                            is RootScreens.Collections -> CollectionsScreen(
                                routeKey = route,
                                onBack = { navigator.goBack() },
                                onCreateCollection = { navigator.navigate(RootScreens.AddCollection) }
                            )

                            is RootScreens.NoteEditor -> NoteEditorScreen(
                                routeKey = route,
                                noteId = route.noteId,
                                onBack = { navigator.goBack() }
                            )

                            // Tags Screens
                            is RootScreens.Tags -> TagsScreen(
                                routeKey = route,
                                onBack = { navigator.goBack() },
                                onCreateTag = { navigator.navigate(RootScreens.AddTag) }
                            )

                            is RootScreens.AddTag -> AddTagScreen(
                                routeKey = route,
                                onBack = { navigator.goBack() }
                            )

                            else -> error("Unknown route: $route")
                        }
                    }
                },
                onBack = { navigator.goBack() },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}