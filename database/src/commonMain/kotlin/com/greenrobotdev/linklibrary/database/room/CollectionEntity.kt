package com.greenrobotdev.linklibrary.database.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Room entity for Collection data persistence
 */
@Entity(tableName = "collections")
@Serializable
data class CollectionEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String? = null,
    val iconType: String = "psychology",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)