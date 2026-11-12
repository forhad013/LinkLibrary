package com.greenrobotdev.linklibrary.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.savedstate.serialization.SavedStateConfiguration
import com.greenrobotdev.linklibrary.bookmarks.model.SharedContent
import com.greenrobotdev.linklibrary.design.components.emptystate.EmptyState
import com.greenrobotdev.linklibrary.screens.library.navigation.LibraryNavRoutes
import com.greenrobotdev.linklibrary.screens.library.navigation.LibraryTabNavigation
import com.greenrobotdev.linklibrary.screens.organize.navigation.OrganizeTabNavigation
import com.greenrobotdev.linklibrary.screens.settings.navigation.SettingsTabNavigation
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(RootScreens::class) {
            subclass(RootScreens.FeedTab::class, serializer<RootScreens.FeedTab>())
            subclass(RootScreens.SearchTab::class, serializer<RootScreens.SearchTab>())
            subclass(RootScreens.LibraryTab::class, serializer<RootScreens.LibraryTab>())
            subclass(RootScreens.SettingsTab::class, serializer<RootScreens.SettingsTab>())
        }
    }
}

data class TabDefinition(
    val tab: RootScreens,
    val icon: ImageVector,
    val title: String
)

val APP_TABS = listOf(
    TabDefinition(RootScreens.FeedTab, Icons.Filled.RssFeed, "Feed"),
    TabDefinition(RootScreens.SearchTab, Icons.Filled.Search, "Search"),
    TabDefinition(RootScreens.LibraryTab, Icons.Filled.MenuBook, "Library"),
    TabDefinition(RootScreens.SettingsTab, Icons.Filled.Settings, "Settings")
)

@Composable
fun RootScreen(
    sharedContent: SharedContent? = null,
    onSharedContentHandled: () -> Unit = {}
) {
    val rootNavigationState = rememberRootNavigationState()
    val rootNavigator = remember { RootNavigator(rootNavigationState) }

    // Feed tab reuses the Library tab's navigation (Add link, details, notes)
    val feedTabNavState = rememberTabNavigationState(
        startRoute = LibraryNavRoutes.LibraryBase
    )
    val feedTabNavigator = remember { TabNavigator(feedTabNavState) }

    // Share sheet: open Add link on the Feed tab
    LaunchedEffect(sharedContent) {
        if (sharedContent != null && sharedContent.url != null) {
            rootNavigator.switchTab(RootScreens.FeedTab)
            feedTabNavigator.navigateToNew(LibraryNavRoutes.AddLink(sharedContent.url))
            onSharedContentHandled()
        }
    }

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeDrawingPadding(),
        bottomBar = {
            NavigationBar {
                APP_TABS.forEach { tabDef ->
                    NavigationBarItem(
                        selected = rootNavigator.isTabActive(tabDef.tab),
                        onClick = { rootNavigator.switchTab(tabDef.tab) },
                        icon = { Icon(tabDef.icon, contentDescription = tabDef.title) },
                        label = { Text(tabDef.title) }
                    )
                }
            }
        },
        floatingActionButton = {
            // FAB only on Feed; opens Add link
            if (rootNavigator.getCurrentTab() == RootScreens.FeedTab) {
                FloatingActionButton(
                    onClick = { feedTabNavigator.navigateToNew(LibraryNavRoutes.AddLink(null)) },
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add link")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (rootNavigator.getCurrentTab()) {
                RootScreens.FeedTab -> LibraryTabNavigation(feedTabNavigator)
                RootScreens.SearchTab -> EmptyState(
                    title = "Search",
                    description = "Search is coming soon."
                )
                RootScreens.LibraryTab -> OrganizeTabNavigation()
                RootScreens.SettingsTab -> SettingsTabNavigation()
            }
        }
    }
}
