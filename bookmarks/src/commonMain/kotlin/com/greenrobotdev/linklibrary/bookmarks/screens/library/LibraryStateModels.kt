package com.greenrobotdev.linklibrary.bookmarks.screens.library

import com.greenrobotdev.linklibrary.bookmarks.model.Link
import kotlinx.serialization.Serializable

@Serializable
data class LibraryState(
    val searchQuery: String = "",
    val links: List<Link> = emptyList(),
    val filteredLinks: List<Link> = emptyList(),
    val isLoading: Boolean = true
)

sealed interface LibraryEvent {
    data class SearchChanged(val query: String) : LibraryEvent
}
