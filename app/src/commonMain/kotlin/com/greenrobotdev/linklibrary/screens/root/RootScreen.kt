package com.greenrobotdev.linklibrary.screens.root

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.greenrobotdev.linklibrary.screens.add.AddLinkScreen
import com.greenrobotdev.linklibrary.screens.details.LinkDetailScreen
import com.greenrobotdev.linklibrary.screens.home.HomeScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

// Create the required serializing configuration for open polymorphism (required for KMP)
private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(RootScreens.Home::class, RootScreens.Home.serializer())
            subclass(RootScreens.LinkDetail::class, RootScreens.LinkDetail.serializer())
            subclass(RootScreens.AddLink::class, RootScreens.AddLink.serializer())
        }
    }
}

@Composable
fun RootScreen() {
    // Create the navigation back stack with proper serialization support for KMP
    val backStack = rememberNavBackStack(config, RootScreens.Home)

    NavDisplay(
        backStack = backStack,

        // Specify what should happen when the user goes back
        onBack = { backStack.removeLastOrNull() },

        // An entry provider converts a route into a NavEntry which contains the content for that route.
        entryProvider = { route ->
            NavEntry(route) {
                when (route) {
                    is RootScreens.Home -> HomeScreen(
                        routeKey = route,
                        onNavigateToDetail = { linkId ->
                            backStack.add(RootScreens.LinkDetail(linkId))
                        },
                        onAddLink = { initialUrl ->
                            backStack.add(RootScreens.AddLink(initialUrl))
                        })


                    is RootScreens.LinkDetail -> LinkDetailScreen(
                        routeKey = route, linkId = route.linkId, onBack = {
                            if (backStack.size > 1) {
                                backStack.removeLast()
                            }
                        })


                    is RootScreens.AddLink -> AddLinkScreen(
                        routeKey = route, initialUrl = route.initialUrl, onBack = {
                            if (backStack.size > 1) {
                                backStack.removeLast()
                            }
                        })

                    else -> Text("Unknown route: $route")
                }
            }
        }
    )

}
