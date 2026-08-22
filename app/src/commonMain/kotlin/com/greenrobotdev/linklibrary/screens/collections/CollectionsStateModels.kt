package com.greenrobotdev.linklibrary.screens.collections

import kotlinx.serialization.Serializable

/**
 * Collection data model
 */
@Serializable
data class Collection(
    val id: String,
    val name: String,
    val description: String? = null,
    val count: Int,
    val iconType: String = "psychology" // For Compose icon mapping
)

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
