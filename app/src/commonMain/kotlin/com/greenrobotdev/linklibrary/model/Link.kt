package com.greenrobotdev.linklibrary.model

import kotlinx.serialization.Serializable

/**
 * Common Link model shared across all platforms
 * This is the domain model used throughout the application
 */
@Serializable
data class Link(
    val id: String,
    val title: String,
    val url: String,
    val description: String = "",
    val isFavorite: Boolean = false,
    val createdAt: Long? = null,
    val tags: List<String> = emptyList(),
    val collections: List<String> = emptyList()
)