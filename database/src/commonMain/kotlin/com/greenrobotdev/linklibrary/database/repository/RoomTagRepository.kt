package com.greenrobotdev.linklibrary.database.repository

import com.greenrobotdev.linklibrary.database.room.DatabaseBuilder
import com.greenrobotdev.linklibrary.database.room.LinkDatabase
import com.greenrobotdev.linklibrary.database.room.LinkTagEntity
import com.greenrobotdev.linklibrary.database.room.TagEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Room-based implementation of TagRepository for KMP following official Android pattern.
 *
 * This implementation works across all platforms using the DatabaseBuilder interface:
 * - Android: Uses AndroidDatabaseBuilder with optional SQLCipher encryption
 * - iOS/macOS: Can provide iOSDatabaseBuilder when needed
 * - JVM/Desktop: Can provide JvmDatabaseBuilder when needed
 *
 * @param databaseBuilder Platform-specific implementation of DatabaseBuilder interface
 */
class RoomTagRepository(
    private val databaseBuilder: DatabaseBuilder
) : TagRepository {

    private val database: LinkDatabase = databaseBuilder.getDatabaseBuilder().build()

    private val tagDao = database.tagDao()
    private val linkTagDao = database.linkTagDao()

    override suspend fun getTags(): Flow<Result<List<TagEntity>>> {
        return tagDao.getAllTagsWithCounts()
            .map { tagWithCounts ->
                val tags = tagWithCounts.map { tagWithCount ->
                    TagEntity(
                        id = tagWithCount.id,
                        name = tagWithCount.name,
                        description = tagWithCount.description,
                        iconType = tagWithCount.iconType,
                        createdAt = tagWithCount.createdAt,
                        updatedAt = tagWithCount.updatedAt
                    )
                }
                Result.success(tags)
            }
            .catch { e -> emit(Result.failure(e)) }
    }

    override suspend fun getTagById(id: String): Flow<Result<TagEntity?>> {
        return tagDao.getTagById(id)
            .map { entity -> Result.success(entity) }
            .catch { e -> emit(Result.failure(e)) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addTag(tag: TagEntity): Flow<Result<TagEntity>> {
        return try {
            // Generate new ID if not provided
            val tagToSave = if (tag.id.isEmpty()) {
                tag.copy(id = Uuid.random().toString())
            } else {
                tag
            }

            tagDao.insertTag(tagToSave)
            flow { emit(Result.success(tagToSave)) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    override suspend fun updateTag(tag: TagEntity): Flow<Result<TagEntity>> {
        return try {
            tagDao.updateTag(tag)
            flow { emit(Result.success(tag)) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    override suspend fun deleteTag(tagId: String): Flow<Result<Unit>> {
        return try {
            tagDao.deleteTagById(tagId)
            flow { emit(Result.success(Unit)) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    override suspend fun getTagsForLink(linkId: String): Flow<Result<List<TagEntity>>> {
        return tagDao.getTagsForLink(linkId)
            .map { entities -> Result.success(entities) }
            .catch { e -> emit(Result.failure(e)) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun assignTagToLink(linkId: String, tagId: String): Flow<Result<Unit>> {
        return try {
            val linkTag = LinkTagEntity(
                id = Uuid.random().toString(),
                linkId = linkId,
                tagId = tagId
            )
            linkTagDao.assignTagToLink(linkTag)
            flow { emit(Result.success(Unit)) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    override suspend fun removeTagFromLink(linkId: String, tagId: String): Flow<Result<Unit>> {
        return try {
            linkTagDao.removeTagFromLink(linkId, tagId)
            flow { emit(Result.success(Unit)) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    /**
     * Close the database connection.
     */
    fun close() {
        database.close()
    }
}