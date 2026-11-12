package com.greenrobotdev.linklibrary.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val name: String,
    val parentId: String? = null,
    val count: Int = 0,
    val iconType: String = "psychology"
)
