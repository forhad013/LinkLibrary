package com.greenrobotdev.linklibrary.bookmarks.screens.collections

import com.greenrobotdev.linklibrary.model.Collection
import kotlinx.serialization.Serializable

/**
 * State for Collections screen
 */
@Serializable
data class CollectionsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val collections: List<Collection> = emptyList()
)

/**
 * Events for Collections screen
 */
sealed interface CollectionsEvent {
    object LoadCollections : CollectionsEvent
    object ClearError : CollectionsEvent
}
