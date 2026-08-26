package com.greenrobotdev.linklibrary.bookmarks.screens.tags

import com.greenrobotdev.linklibrary.bookmarks.model.Tag
import kotlinx.serialization.Serializable

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
