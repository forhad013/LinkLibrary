package com.greenrobotdev.linklibrary.model

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
