package com.greenrobotdev.linklibrary.screens.library.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.greenrobotdev.linklibrary.bookmarks.screens.add.AddLinkScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.addCategory.AddCategoryScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.addTag.AddTagScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.categories.CategoriesScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.details.LinkDetailScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.library.LibraryScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.tags.TagsScreenEnhanced
import com.greenrobotdev.linklibrary.screens.notes.NoteEditorScreen
import com.greenrobotdev.linklibrary.screens.root.TabNavigator
import com.greenrobotdev.linklibrary.screens.root.rememberTabNavigationState
import com.greenrobotdev.linklibrary.screens.root.toEntries

/**
 * Library Tab Navigation - Manages all screens within the Library tab
 */
@Composable
fun LibraryTabNavigation(
    tabNavigator: TabNavigator
) {
    val tabNavState = tabNavigator.state

    NavDisplay(
        entries = tabNavState.toEntries { route ->
            NavEntry(route) {
                when (route) {
                    // Base Library Screen
                    is LibraryNavRoutes.LibraryBase -> LibraryScreen(
                        routeKey = route,
                        onNavigateToDetail = { linkId ->
                            tabNavigator.navigate(LibraryNavRoutes.LinkDetail(linkId))
                        },
                        onAddLink = { initialUrl ->
                            tabNavigator.navigateToNew(LibraryNavRoutes.AddLink(initialUrl))
                        },
                        onAddCategory = {
                            tabNavigator.navigate(LibraryNavRoutes.AddCategory)
                        },
                        onNavigateToCategories = {
                            tabNavigator.navigate(LibraryNavRoutes.Categories)
                        },
                        onNavigateToTags = {
                            tabNavigator.navigate(LibraryNavRoutes.Tags)
                        }
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
                        onAddTag = {
                            tabNavigator.navigate(LibraryNavRoutes.AddTag)
                        },
                        onAddCategory = {
                            tabNavigator.navigate(LibraryNavRoutes.AddCategory)
                        }
                    )

                    // Note Editor Screen
                    is LibraryNavRoutes.NoteEditor -> NoteEditorScreen(
                        routeKey = route,
                        noteId = route.noteId,
                        onBack = { tabNavigator.goBack() }
                    )

                    // Categories Screen
                    is LibraryNavRoutes.Categories -> CategoriesScreen(
                        routeKey = route,
                        onBack = { tabNavigator.goBack() },
                        onCreateCategory = {
                            tabNavigator.navigate(LibraryNavRoutes.AddCategory)
                        }
                    )

                    // Tags Screen
                    is LibraryNavRoutes.Tags -> TagsScreenEnhanced(
                        routeKey = route,
                        onBack = { tabNavigator.goBack() },
                        onCreateTag = {
                            tabNavigator.navigate(LibraryNavRoutes.AddTag)
                        }
                    )

                    // Add Category Screen
                    is LibraryNavRoutes.AddCategory -> AddCategoryScreen(
                        routeKey = route,
                        onBack = { tabNavigator.goBack() }
                    )

                    // Add Tag Screen
                    is LibraryNavRoutes.AddTag -> AddTagScreen(
                        routeKey = route,
                        onBack = { tabNavigator.goBack() }
                    )

                    else -> error("Unknown library route: $route")
                }
            }
        },
        onBack = { tabNavigator.goBack() },
        modifier = Modifier.fillMaxSize()
    )
}