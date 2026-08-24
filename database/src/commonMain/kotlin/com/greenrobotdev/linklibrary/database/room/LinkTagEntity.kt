package com.greenrobotdev.linklibrary.database.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Junction entity for many-to-many relationship between Links and Tags
 * A link can have multiple tags, and a tag can be assigned to multiple links
 */
@Entity(
    tableName = "link_tags",
    foreignKeys = [
        ForeignKey(
            entity = LinkEntity::class,
            parentColumns = ["id"],
            childColumns = ["linkId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["linkId"]),
        Index(value = ["tagId"]),
        Index(value = ["linkId", "tagId"], unique = true)
    ]
)
data class LinkTagEntity(
    @PrimaryKey
    val id: String,
    val linkId: String,
    val tagId: String,
    val createdAt: Long = System.currentTimeMillis()
)