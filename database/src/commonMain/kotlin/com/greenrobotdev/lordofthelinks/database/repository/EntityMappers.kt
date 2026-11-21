package com.greenrobotdev.lordofthelinks.database.repository

import com.greenrobotdev.lordofthelinks.database.room.CategoryDao
import com.greenrobotdev.lordofthelinks.database.room.CategoryEntity
import com.greenrobotdev.lordofthelinks.database.room.CategoryWithCount
import com.greenrobotdev.lordofthelinks.database.room.LinkEntity
import com.greenrobotdev.lordofthelinks.database.room.TagEntity
import com.greenrobotdev.lordofthelinks.database.room.TagWithCount
import com.greenrobotdev.lordofthelinks.model.Category
import com.greenrobotdev.lordofthelinks.model.Link
import com.greenrobotdev.lordofthelinks.model.SmartListCounts
import com.greenrobotdev.lordofthelinks.model.Tag

// Room entities <-> domain models; only the Room repositories use these

// Link extensions
internal fun Link.toEntity() = LinkEntity(
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
    imageUrl = imageUrl,
    tags = tags
)

internal fun LinkEntity.toDomain(categoryName: String? = null) = Link(
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
    imageUrl = imageUrl,
    tags = tags,
    categoryName = categoryName
)

// Tag extensions
internal fun TagEntity.toTag(count: Int = 0) = Tag(
    id = id,
    name = name,
    description = description,
    count = count,
    iconType = iconType
)

internal fun TagWithCount.toTag() = Tag(
    id = id,
    name = name,
    description = description,
    count = linkCount,
    iconType = iconType
)

internal fun Tag.toEntity() = TagEntity(
    id = id,
    name = name,
    description = description,
    iconType = iconType,
    createdAt = System.currentTimeMillis(),
    updatedAt = System.currentTimeMillis()
)

// Category extensions
internal fun CategoryEntity.toCategory(count: Int = 0) = Category(
    id = id,
    name = name,
    parentId = parentId,
    count = count,
    iconType = iconType
)

internal fun CategoryWithCount.toCategory() = Category(
    id = id,
    name = name,
    parentId = parentId,
    count = linkCount,
    iconType = iconType
)

internal fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    parentId = parentId,
    iconType = iconType,
    createdAt = System.currentTimeMillis(),
    updatedAt = System.currentTimeMillis()
)

internal fun CategoryDao.SmartListCounts.toDomain() = SmartListCounts(
    unopened = unopened,
    favorites = favorites,
    archived = archived,
    uncategorized = uncategorized
)
