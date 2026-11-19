package com.greenrobotdev.lordofthelinks.bookmarks.model

import kotlinx.serialization.Serializable

@Serializable
data class Link(
    val id: String,
    val title: String,
    val url: String,
    val description: String = "",
    val isFavorite: Boolean = false,
    val isOpened: Boolean = false,
    val isArchived: Boolean = false,
    val type: String = "other", // video | article | social | other
    val fetchedDescription: String? = null,
    val createdAt: Long? = null,
    /** Page preview image (og:image); null when the page has none or it wasn't fetched yet. */
    val imageUrl: String? = null,
    val tags: List<String> = emptyList(),
    val categoryName: String? = null
) {
    /** The preview image to show, if any ("" means the page has none). */
    val previewImage: String? get() = imageUrl?.takeIf { it.isNotBlank() }
}
