package com.greenrobotdev.linklibrary.screens.root

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class RootScreens : NavKey {
    @Serializable
    data object Home : RootScreens()

    @Serializable
    data class LinkDetail(val linkId: String) : RootScreens()

    @Serializable
    data class AddLink(val initialUrl: String? = null) : RootScreens()
}
