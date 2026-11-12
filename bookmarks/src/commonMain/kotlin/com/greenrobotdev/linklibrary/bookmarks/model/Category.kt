package com.greenrobotdev.linklibrary.bookmarks.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val name: String,
    val parentId: String? = null,
    val count: Int = 0,
    val iconType: String = "psychology"
) {
    /** Breadcrumb path like "Health › Sleep", resolved against all known categories. */
    fun path(all: List<Category>): String {
        val names = mutableListOf(name)
        var current = all.firstOrNull { it.id == parentId }
        while (current != null) {
            names.add(0, current.name)
            current = all.firstOrNull { it.id == current.parentId }
        }
        return names.joinToString(" › ")
    }
}
