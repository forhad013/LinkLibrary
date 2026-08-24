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
    val createdAt: Long?, // Stored as epoch milliseconds
    val tags: List<String> = emptyList(), // Stored as JSON string via TypeConverter
    val collections: List<String> = emptyList() // Stored as JSON string via TypeConverter
)
