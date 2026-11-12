package com.greenrobotdev.linklibrary.screens.library.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.greenrobotdev.linklibrary.bookmarks.screens.add.AddLinkScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.addCategory.AddCategoryScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.addTag.AddTagScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.categories.CategoriesScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.details.LinkDetailScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.tags.TagsScreenEnhanced
import com.greenrobotdev.linklibrary.screens.notes.NoteEditorScreen
import com.greenrobotdev.linklibrary.screens.root.TabNavigator
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
                    // Base screen — empty Feed until GRE-15
                    is LibraryNavRoutes.LibraryBase -> com.greenrobotdev.linklibrary.design.components.emptystate.EmptyState(
                        title = "Your saves",
                        description = "Save your first link — articles, videos and posts, all in one place.",
                        icon = androidx.compose.material.icons.Icons.Filled.RssFeed,
                        actionLabel = "Add a link",
                        onActionClick = {
                            tabNavigator.navigateToNew(LibraryNavRoutes.AddLink(null))
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