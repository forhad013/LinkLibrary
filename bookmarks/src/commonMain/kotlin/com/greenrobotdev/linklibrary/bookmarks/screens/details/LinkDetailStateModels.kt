package com.greenrobotdev.linklibrary.bookmarks.screens.details

import com.greenrobotdev.linklibrary.model.Link
import kotlinx.serialization.Serializable

/**
 * State for Link Detail screen
 */
@Serializable
data class LinkDetailState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val link: Link? = null,
    val isDeleted: Boolean = false
)

/**
 * Events for Link Detail screen
 */
sealed interface LinkDetailEvent {
    object LoadLink : LinkDetailEvent
    object ToggleFavorite : LinkDetailEvent
    object Delete : LinkDetailEvent
    object ClearError : LinkDetailEvent
}
