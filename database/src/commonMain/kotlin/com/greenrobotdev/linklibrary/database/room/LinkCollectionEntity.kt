package com.greenrobotdev.linklibrary.database.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Junction entity for many-to-many relationship between Links and Collections
 * A link can belong to multiple collections, and a collection can contain multiple links
 */
@Entity(
    tableName = "link_collections",
    foreignKeys = [
        ForeignKey(
            entity = LinkEntity::class,
            parentColumns = ["id"],
            childColumns = ["linkId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CollectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["collectionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["linkId"]),
        Index(value = ["collectionId"]),
        Index(value = ["linkId", "collectionId"], unique = true)
    ]
)
data class LinkCollectionEntity(
    @PrimaryKey
    val id: String,
    val linkId: String,
    val collectionId: String,
    val createdAt: Long = System.currentTimeMillis()
)