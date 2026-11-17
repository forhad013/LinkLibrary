package com.greenrobotdev.linklibrary.database.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Room entity for nested Category persistence.
 * Max depth 5 is enforced in CategoryDao.insertCategory/moveCategory (shortcut: not a DB constraint, fine while dev-only destructive migrations).
 */
@Entity(
    tableName = "categories",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["parentId"])]
)
@Serializable
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val parentId: String? = null,
    val iconType: String = "psychology",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
