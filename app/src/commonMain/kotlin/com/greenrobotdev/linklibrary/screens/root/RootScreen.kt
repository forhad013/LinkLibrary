package com.greenrobotdev.linklibrary.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
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
import androidx.savedstate.serialization.SavedStateConfiguration
import com.greenrobotdev.linklibrary.bookmarks.model.SharedContent
import com.greenrobotdev.linklibrary.screens.home.navigation.HomeTabNavigation
import com.greenrobotdev.linklibrary.screens.library.navigation.LibraryTabNavigation
import com.greenrobotdev.linklibrary.screens.organize.navigation.OrganizeTabNavigation
import com.greenrobotdev.linklibrary.screens.settings.navigation.SettingsTabNavigation
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer

// Configuration for serialization - New structure
private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(RootScreens::class) {
            subclass(RootScreens.LibraryTab::class, serializer<RootScreens.LibraryTab>())
            subclass(RootScreens.OrganizeTab::class, serializer<RootScreens.OrganizeTab>())
            subclass(RootScreens.HomeTab::class, serializer<RootScreens.HomeTab>())
            subclass(RootScreens.SettingsTab::class, serializer<RootScreens.SettingsTab>())
        }
    }
}

/**
 * Tab definition for bottom navigation
 */
data class TabDefinition(
    val tab: RootScreens,
    val icon: ImageVector,
    val title: String
)

/**
 * Available tabs in the app
 */
val APP_TABS = listOf(
    TabDefinition(RootScreens.LibraryTab, Icons.Filled.MenuBook, "Library"),
    TabDefinition(RootScreens.OrganizeTab, Icons.Filled.Collections, "Organize"),
    TabDefinition(RootScreens.HomeTab, Icons.Filled.Home, "Home"),
    TabDefinition(RootScreens.SettingsTab, Icons.Filled.Settings, "Settings")
)

@Composable
fun RootScreen(
    sharedContent: SharedContent? = null,
    onSharedContentHandled: () -> Unit = {}
) {
    val rootNavigationState = rememberRootNavigationState(
        startTab = RootScreens.HomeTab
    )

    val rootNavigator = remember { RootNavigator(rootNavigationState) }

    // Create tab navigation states for tabs that need FAB access
    val libraryTabNavState = rememberTabNavigationState(
        startRoute = com.greenrobotdev.linklibrary.screens.library.navigation.LibraryNavRoutes.LibraryBase
    )
    val libraryTabNavigator = remember { TabNavigator(libraryTabNavState) }

    val homeTabNavState = rememberTabNavigationState(
        startRoute = com.greenrobotdev.linklibrary.screens.home.navigation.HomeNavRoutes.HomeBase
    )
    val homeTabNavigator = remember { TabNavigator(homeTabNavState) }

    // Handle external share content - navigate to Library tab with Add Link screen
    LaunchedEffect(sharedContent) {
        if (sharedContent != null && sharedContent.url != null) {
            // Switch to Library tab (which contains Add Link navigation)
            rootNavigator.switchTab(RootScreens.LibraryTab)
            // Navigate to Add Link with the shared URL
            libraryTabNavigator.navigateToNew(
                com.greenrobotdev.linklibrary.screens.library.navigation.LibraryNavRoutes.AddLink(sharedContent.url)
            )
            // Clear the shared content after navigation
            onSharedContentHandled()
        }
    }

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeDrawingPadding(),
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                APP_TABS.forEach { tabDef ->
                    val isSelected = rootNavigator.isTabActive(tabDef.tab)
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { rootNavigator.switchTab(tabDef.tab) },
                        icon = {
                            Icon(
                                imageVector = tabDef.icon,
                                contentDescription = tabDef.title
                            )
                        },
                        label = { Text(tabDef.title) }
                    )
                }
            }
        },
        floatingActionButton = {
            // Only show FAB on Library and Home tabs
            when (rootNavigator.getCurrentTab()) {
                RootScreens.LibraryTab -> {
                    FloatingActionButton(
                        onClick = {
                            // Navigate to Add Link screen in Library tab
                            libraryTabNavigator.navigateToNew(
                                com.greenrobotdev.linklibrary.screens.library.navigation.LibraryNavRoutes.AddLink(null)
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Icon(Icons.Default.AddLink, contentDescription = "Add Link")
                    }
                }
                RootScreens.HomeTab -> {
                    FloatingActionButton(
                        onClick = {
                            // Navigate to Quick Add screen in Home tab
                            homeTabNavigator.navigateToNew(
                                com.greenrobotdev.linklibrary.screens.home.navigation.HomeNavRoutes.QuickAdd(null)
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Icon(Icons.Default.AddLink, contentDescription = "Add Link")
                    }
                }
                else -> {}
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            // Render the current tab's navigation
            when (rootNavigator.getCurrentTab()) {
                RootScreens.LibraryTab -> LibraryTabNavigation(libraryTabNavigator)
                RootScreens.OrganizeTab -> OrganizeTabNavigation()
                RootScreens.HomeTab -> HomeTabNavigation(homeTabNavigator)
                RootScreens.SettingsTab -> SettingsTabNavigation()
            }
        }
    }
}