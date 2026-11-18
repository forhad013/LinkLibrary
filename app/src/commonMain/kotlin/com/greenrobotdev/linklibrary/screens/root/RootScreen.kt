package com.greenrobotdev.linklibrary.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RssFeed
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.savedstate.serialization.SavedStateConfiguration
import com.greenrobotdev.linklibrary.bookmarks.model.SharedContent
import com.greenrobotdev.linklibrary.bookmarks.screens.add.sheet.AddLinkSheet
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

    // Step one of Add link is a modal sheet; Continue opens the existing full add-link screen
    var addLinkSheetUrl by remember { mutableStateOf<String?>(null) }
    val openAddLinkSheet = { url: String? -> addLinkSheetUrl = url ?: "" }

    // Share sheet: open Add link on the Feed tab
    LaunchedEffect(sharedContent) {
        if (sharedContent != null && sharedContent.url != null) {
            rootNavigator.switchTab(RootScreens.FeedTab)
            openAddLinkSheet(sharedContent.url)
            onSharedContentHandled()
        }
    }
    addLinkSheetUrl?.let { url ->
        AddLinkSheet(
            initialUrl = url.takeIf { it.isNotEmpty() },
            onContinue = { continued ->
                addLinkSheetUrl = null
                feedTabNavigator.navigateToNew(LibraryNavRoutes.AddLink(continued))
            },
            onDismiss = { addLinkSheetUrl = null },
        )
    }

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            // systemBars only (not IME) so the keyboard doesn't push the background content up
            .systemBarsPadding(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
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
            // FAB only on the Feed list itself (hidden on Add link, link details, …); opens Add link
            if (rootNavigator.getCurrentTab() == RootScreens.FeedTab &&
                feedTabNavState.backStack.lastOrNull() is LibraryNavRoutes.LibraryBase
            ) {
                FloatingActionButton(
                    onClick = { openAddLinkSheet(null) },
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add link")
                }
            }
        }
    ) { padding ->
        // consume so inner imePadding doesn't re-add the bottom nav bar's height
        Box(modifier = Modifier.padding(padding).consumeWindowInsets(padding)) {
            when (rootNavigator.getCurrentTab()) {
                RootScreens.FeedTab -> LibraryTabNavigation(
                    tabNavigator = feedTabNavigator,
                    onAddLink = openAddLinkSheet,
                )
                RootScreens.LibraryTab -> OrganizeTabNavigation()
                RootScreens.SettingsTab -> SettingsTabNavigation()
            }
        }
    }
}
