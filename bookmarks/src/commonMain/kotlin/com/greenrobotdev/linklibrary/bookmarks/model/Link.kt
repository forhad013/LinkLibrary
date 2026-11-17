package com.greenrobotdev.linklibrary.bookmarks.model

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
    val tags: List<String> = emptyList(),
    val categoryName: String? = null
)
