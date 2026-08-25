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
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer

// Configuration for serialization
private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(RootScreens.HomeTab::class, serializer<RootScreens.HomeTab>())
            subclass(RootScreens.LibraryTab::class, serializer<RootScreens.LibraryTab>())
            subclass(RootScreens.SettingsTab::class, serializer<RootScreens.SettingsTab>())
            subclass(RootScreens.LinkDetail::class, serializer<RootScreens.LinkDetail>())
            subclass(RootScreens.AddLink::class, serializer<RootScreens.AddLink>())
            subclass(RootScreens.AddCollection::class, serializer<RootScreens.AddCollection>())
            subclass(RootScreens.AIAssistantDemo::class, serializer<RootScreens.AIAssistantDemo>())
            subclass(RootScreens.Collections::class, serializer<RootScreens.Collections>())
            subclass(RootScreens.NoteEditor::class, serializer<RootScreens.NoteEditor>())
            subclass(RootScreens.Tags::class, serializer<RootScreens.Tags>())
            subclass(RootScreens.AddTag::class, serializer<RootScreens.AddTag>())
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
            navigator.navigateToNew(RootScreens.AddLink(sharedContent.url))
            // Clear the shared content after navigation
            onSharedContentHandled()
        }
    }

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
                    onClick = { navigator.navigateToNew(RootScreens.AddLink(null)) },
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
                                onAddLink = { initialUrl -> navigator.navigateToNew(RootScreens.AddLink(initialUrl)) }
                            )

                            // Library Tab
                            is RootScreens.LibraryTab -> LibraryScreen(
                                routeKey = route,
                                onNavigateToDetail = { linkId -> navigator.navigate(RootScreens.LinkDetail(linkId)) },
                                onAddLink = { initialUrl -> navigator.navigateToNew(RootScreens.AddLink(initialUrl)) },
                                onAddCollection = { navigator.navigate(RootScreens.AddCollection) },
                                onNavigateToCollections = { navigator.navigate(RootScreens.Collections) },
                                onNavigateToTags = { navigator.navigate(RootScreens.Tags) }
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
                                onBack = { navigator.goBack() },
                                onAddTag = { navigator.navigate(RootScreens.AddTag) },
                                onAddCollection = { navigator.navigate(RootScreens.AddCollection) }
                            )

                            is RootScreens.AddCollection -> AddCollectionScreen(
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
