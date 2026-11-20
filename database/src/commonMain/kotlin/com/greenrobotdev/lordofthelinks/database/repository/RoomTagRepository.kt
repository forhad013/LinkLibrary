package com.greenrobotdev.lordofthelinks.database.repository

import com.greenrobotdev.lordofthelinks.database.room.LinkTagDao
import com.greenrobotdev.lordofthelinks.database.room.LinkTagEntity
import com.greenrobotdev.lordofthelinks.database.room.TagDao
import com.greenrobotdev.lordofthelinks.model.Tag
import com.greenrobotdev.lordofthelinks.model.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class RoomTagRepository(
    private val tagDao: TagDao,
    private val linkTagDao: LinkTagDao
) : TagRepository {

    override suspend fun getTags(): Flow<Result<List<Tag>>> = getTagsWithCounts()

    override suspend fun getTagsWithCounts(): Flow<Result<List<Tag>>> {
        return tagDao.getAllTagsWithCounts()
            .map { rows -> Result.success(rows.map { it.toTag() }) }
            .catch { e -> emit(Result.failure(e)) }
    }

    override suspend fun getTagById(id: String): Flow<Result<Tag?>> {
        return tagDao.getTagById(id)
            .map { entity -> Result.success(entity?.toTag()) }
            .catch { e -> emit(Result.failure(e)) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addTag(tag: Tag): Flow<Result<Tag>> {
        return try {
            // Generate new ID if not provided
            val tagToSave = if (tag.id.isEmpty()) {
                tag.toEntity().copy(id = Uuid.random().toString())
            } else {
                tag.toEntity()
            }

            tagDao.insertTag(tagToSave)
            flow { emit(Result.success(tagToSave.toTag())) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    override suspend fun updateTag(tag: Tag): Flow<Result<Tag>> {
        return try {
            // Keeps the stored createdAt; the domain Tag doesn't carry it
            val stored = tagDao.getTagById(tag.id).first()
                ?: return flow { emit(Result.failure(Exception("Tag not found: ${tag.id}"))) }
            tagDao.updateTag(
                stored.copy(name = tag.name, description = tag.description, iconType = tag.iconType, updatedAt = System.currentTimeMillis())
            )
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

    override suspend fun getTagsForLink(linkId: String): Flow<Result<List<Tag>>> {
        return tagDao.getTagsForLink(linkId)
            .map { entities -> Result.success(entities.map { it.toTag() }) }
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
}