package com.greenrobotdev.linklibrary.screens.tags

import kotlinx.serialization.Serializable

/**
 * Tag data model
 */
@Serializable
data class Tag(
    val id: String,
    val name: String,
    val description: String? = null,
    val count: Int,
    val iconType: String = "psychology" // For Compose icon mapping
)

/**
 * State for Tags screen
 */
@Serializable
data class TagsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val tags: List<Tag> = emptyList()
)

/**
 * Events for Tags screen
 */
sealed interface TagsEvent {
    object LoadTags : TagsEvent
    object ClearError : TagsEvent
}
