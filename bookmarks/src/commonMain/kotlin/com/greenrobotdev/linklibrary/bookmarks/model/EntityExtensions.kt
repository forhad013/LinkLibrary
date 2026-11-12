package com.greenrobotdev.linklibrary.bookmarks.model

import com.greenrobotdev.linklibrary.database.room.CategoryEntity
import com.greenrobotdev.linklibrary.database.room.LinkEntity
import com.greenrobotdev.linklibrary.database.room.TagEntity

// Link extensions
fun Link.toEntity() = LinkEntity(
    id = id,
    title = title,
    url = url,
    description = description,
    isFavorite = isFavorite,
    isOpened = isOpened,
    isArchived = isArchived,
    type = type,
    fetchedDescription = fetchedDescription,
    createdAt = createdAt,
    tags = tags
)

fun LinkEntity.toDomain(categoryName: String? = null) = Link(
    id = id,
    title = title,
    url = url,
    description = description,
    isFavorite = isFavorite,
    isOpened = isOpened,
    isArchived = isArchived,
    type = type,
    fetchedDescription = fetchedDescription,
    createdAt = createdAt,
    tags = tags,
    categoryName = categoryName
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

// Category extensions
fun CategoryEntity.toCategory(count: Int = 0) = Category(
    id = id,
    name = name,
    parentId = parentId,
    count = count,
    iconType = iconType
)

fun com.greenrobotdev.linklibrary.database.room.CategoryWithCount.toCategory() = Category(
    id = id,
    name = name,
    parentId = parentId,
    count = linkCount,
    iconType = iconType
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    parentId = parentId,
    iconType = iconType,
    createdAt = System.currentTimeMillis(),
    updatedAt = System.currentTimeMillis()
)
