package com.greenrobotdev.linklibrary.model

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
