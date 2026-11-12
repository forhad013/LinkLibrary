package com.greenrobotdev.linklibrary.screens.organize.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.greenrobotdev.linklibrary.bookmarks.screens.addCategory.AddCategoryScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.addTag.AddTagScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.categories.CategoriesScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.library.LibraryTabScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.manage.ManageScreen
import com.greenrobotdev.linklibrary.bookmarks.screens.tags.TagsScreenEnhanced
import com.greenrobotdev.linklibrary.screens.root.TabNavigator
import com.greenrobotdev.linklibrary.screens.root.rememberTabNavigationState
import com.greenrobotdev.linklibrary.screens.root.toEntries

/**
 * Organize Tab Navigation - Manages all screens within the Organize tab
 */
@Composable
fun OrganizeTabNavigation() {
    val tabNavState = rememberTabNavigationState(
        startRoute = OrganizeNavRoutes.OrganizeBase
    )

    val tabNavigator = remember { TabNavigator(tabNavState) }

    NavDisplay(
        entries = tabNavState.toEntries { route ->
            NavEntry(route) {
                when (route) {
                    // Library tab base (design: library_categories_amp_tags)
                    is OrganizeNavRoutes.OrganizeBase -> LibraryTabScreen(
                        onManage = { tabNavigator.navigate(OrganizeNavRoutes.Manage) },
                        onCreateCategory = {
                            tabNavigator.navigate(OrganizeNavRoutes.CategoryEdit(null))
                        },
                        onCategoryClick = { categoryId ->
                            tabNavigator.navigate(OrganizeNavRoutes.CategoryDetail(categoryId))
                        }
                    )

                    // Manage categories and tags
                    is OrganizeNavRoutes.Manage -> ManageScreen(
                        onBack = { tabNavigator.goBack() }
                    )

                    // Category Detail Screen
                    is OrganizeNavRoutes.CategoryDetail -> CategoriesScreen(
                        routeKey = route,
                        onBack = { tabNavigator.goBack() },
                        onCreateCategory = {
                            tabNavigator.navigate(OrganizeNavRoutes.CategoryEdit(null))
                        }
                    )

                    // Category Edit Screen
                    is OrganizeNavRoutes.CategoryEdit -> AddCategoryScreen(
                        routeKey = route,
                        onBack = { tabNavigator.goBack() }
                    )

                    // Tag Detail Screen
                    is OrganizeNavRoutes.TagDetail -> TagsScreenEnhanced(
                        routeKey = route,
                        onBack = { tabNavigator.goBack() },
                        onCreateTag = {
                            tabNavigator.navigate(OrganizeNavRoutes.TagEdit(null))
                        }
                    )

                    // Tag Edit Screen
                    is OrganizeNavRoutes.TagEdit -> AddTagScreen(
                        routeKey = route,
                        onBack = { tabNavigator.goBack() }
                    )

                    // Bulk Organize Screen
                    is OrganizeNavRoutes.BulkOrganize -> CategoriesScreen(
                        routeKey = route,
                        onBack = { tabNavigator.goBack() },
                        onCreateCategory = {
                            tabNavigator.navigate(OrganizeNavRoutes.CategoryEdit(null))
                        }
                    )

                    else -> error("Unknown organize route: $route")
                }
            }
        },
        onBack = { tabNavigator.goBack() },
        modifier = Modifier.fillMaxSize()
    )
}