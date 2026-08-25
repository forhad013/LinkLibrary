package com.greenrobotdev.linklibrary.model

import com.greenrobotdev.linklibrary.database.room.CollectionEntity
import com.greenrobotdev.linklibrary.database.room.LinkEntity
import com.greenrobotdev.linklibrary.database.room.TagEntity
import com.greenrobotdev.linklibrary.model.Collection
import com.greenrobotdev.linklibrary.model.Tag

/**
 * Extension functions to convert between database entities and UI models
 * These are placed in the app module to avoid circular dependencies
 */

// Link extensions
fun Link.toEntity() = LinkEntity(
    id = id,
    title = title,
    url = url,
    description = description,
    isFavorite = isFavorite,
    createdAt = createdAt,
    tags = tags
)

fun LinkEntity.toDomain() = Link(
    id = id,
    title = title,
    url = url,
    description = description,
    isFavorite = isFavorite,
    createdAt = createdAt,
    tags = tags ?: emptyList(),
    collections = collections ?: emptyList()
)

// Tag extensions
fun TagEntity.toTag(count: Int = 0) = Tag(
    id = id,
    name = name,
    description = description,
    count = count,
    iconType = iconType
)

fun Tag.toEntity() = TagEntity(
    id = id,
    name = name,
    description = description,
    iconType = iconType,
    createdAt = System.currentTimeMillis(),
    updatedAt = System.currentTimeMillis()
)

// Collection extensions
fun CollectionEntity.toCollection(count: Int = 0) = Collection(
    id = id,
    name = name,
    description = description,
    count = count,
    iconType = iconType
)

fun Collection.toEntity() = CollectionEntity(
    id = id,
    name = name,
    description = description,
    iconType = iconType,
    createdAt = System.currentTimeMillis(),
    updatedAt = System.currentTimeMillis()
)