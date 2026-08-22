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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.greenrobotdev.linklibrary.model.SharedContent
import com.greenrobotdev.linklibrary.screens.add.AddLinkScreen
import com.greenrobotdev.linklibrary.screens.addCollection.AddCollectionScreen
import com.greenrobotdev.linklibrary.screens.collections.CollectionsScreen
import com.greenrobotdev.linklibrary.screens.details.LinkDetailScreen
import com.greenrobotdev.linklibrary.screens.home.HomeScreen
import com.greenrobotdev.linklibrary.screens.library.LibraryScreen
import com.greenrobotdev.linklibrary.screens.notes.NoteEditorScreen
import com.greenrobotdev.linklibrary.screens.settings.SettingsScreen
import com.greenrobotdev.linklibrary.screens.stitch.AIAssistantDemoScreen
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
            subclass(RootScreens.AIAssistantDemo::class, RootScreens.AIAssistantDemo.serializer())
            subclass(RootScreens.Collections::class, RootScreens.Collections.serializer())
            subclass(RootScreens.NoteEditor::class, RootScreens.NoteEditor.serializer())
        }
    }
}

// Define top level routes for bottom navigation
private val TOP_LEVEL_ROUTES = mapOf<NavKey, NavBarItem>(
    RootScreens.HomeTab to NavBarItem(icon = Icons.Filled.Home, title = "Home"),
    RootScreens.LibraryTab to NavBarItem(icon = Icons.Filled.MenuBook, title = "Library"),
    RootScreens.SettingsTab to NavBarItem(icon = Icons.Filled.Settings, title = "Settings"),
)

data class NavBarItem(
    val icon: ImageVector,
    val title: String
)

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

    // Handle external share content - automatically navigate to Add Link screen
    LaunchedEffect(sharedContent) {
        if (sharedContent != null && sharedContent.url != null) {
            // Navigate to Add Link screen with the shared URL
            navigator.navigate(RootScreens.AddLink(sharedContent.url))
            // Clear the shared content after navigation
            onSharedContentHandled()
        }
    }

    // Determine if we should show bottom navigation (hide on detail/add screens)
    val currentRoute = navigationState.backStacks[navigationState.topLevelRoute]?.last()
    val showBottomNav = currentRoute !is RootScreens.LinkDetail &&
                       currentRoute !is RootScreens.AddLink &&
                       currentRoute !is RootScreens.AddCollection &&
                       currentRoute !is RootScreens.NoteEditor

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
                                onNavigateToCollections = { navigator.navigate(RootScreens.Collections) }
                            )

                            // Settings Tab
                            is RootScreens.SettingsTab -> SettingsScreen(
                                routeKey = route,
                                onNavigateToAI = { navigator.navigate(RootScreens.AIAssistantDemo) }
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

                            // Demo/Utility Screens
                            is RootScreens.AIAssistantDemo -> AIAssistantDemoScreen(
                                routeKey = route,
                                onBack = { navigator.goBack() }
                            )

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

// Helper function to get icons by name (kept for backwards compatibility)
@Composable
fun getIcon(iconName: String): ImageVector {
    return when (iconName) {
        "home" -> Icons.Filled.Home
        "library_books" -> Icons.Filled.MenuBook
        "collections" -> Icons.Filled.Collections
        "settings" -> Icons.Filled.Settings
        else -> Icons.Filled.Home
    }
}
