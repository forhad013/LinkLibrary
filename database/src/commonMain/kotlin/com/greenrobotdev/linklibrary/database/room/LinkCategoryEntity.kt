package com.greenrobotdev.linklibrary.database.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * One category per link (linkId is unique).
 */
@Entity(
    tableName = "link_categories",
    foreignKeys = [
        ForeignKey(
            entity = LinkEntity::class,
            parentColumns = ["id"],
            childColumns = ["linkId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["linkId"], unique = true), Index(value = ["categoryId"])]
)
data class LinkCategoryEntity(
    @PrimaryKey
    val linkId: String,
    val categoryId: String
)
