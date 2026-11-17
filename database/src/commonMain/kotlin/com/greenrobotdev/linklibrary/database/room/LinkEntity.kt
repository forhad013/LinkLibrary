package com.greenrobotdev.linklibrary.database.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "links")
@TypeConverters(Converters::class)
data class LinkEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val url: String,
    val description: String,
    val isFavorite: Boolean,
    val isOpened: Boolean = false,
    val isArchived: Boolean = false,
    val type: String = "other", // video | article | social | other
    val fetchedDescription: String? = null, // read-only, auto-fetched from the page
    val createdAt: Long? = null, // epoch milliseconds
    val tags: List<String> = emptyList() // populated from link_tags at read time
)
