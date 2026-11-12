package com.greenrobotdev.linklibrary.database.repository

import com.greenrobotdev.linklibrary.database.room.CategoryDao
import com.greenrobotdev.linklibrary.database.room.LinkDao
import com.greenrobotdev.linklibrary.database.room.LinkEntity
import com.greenrobotdev.linklibrary.database.room.TagDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class RoomLinkRepository(
    private val linkDao: LinkDao,
    private val tagDao: TagDao,
    private val categoryDao: CategoryDao
) : LinkRepository {

    override suspend fun getLinks(): Flow<Result<List<LinkEntity>>> =
        linkDao.getAllLinks()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }

    override suspend fun getLinksWithTags(): Flow<Result<List<LinkEntity>>> {
        return flow {
            try {
                linkDao.getAllLinks().collect { entities ->
                    val enriched = entities.map { entity ->
                        val tagNames = tagDao.getTagsForLink(entity.id).first()
                            .map { it.name }
                        entity.copy(tags = tagNames)
                    }
                    emit(Result.success(enriched))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }

    override suspend fun toggleFavorite(linkId: String): Flow<Result<LinkEntity>> {
        return try {
            linkDao.toggleFavorite(linkId)
            val entity = linkDao.getLinkById(linkId)
            entity?.let { flow { emit(Result.success(it)) } }
                ?: flow { emit(Result.failure(Exception("Link not found"))) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addLink(link: LinkEntity): Flow<Result<LinkEntity>> {
        return try {
            val linkToSave = if (link.id.isEmpty()) link.copy(id = Uuid.random().toString()) else link
            linkDao.insertLink(linkToSave)
            flow { emit(Result.success(linkToSave)) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    override suspend fun deleteLink(linkId: String): Flow<Result<Unit>> {
        return try {
            linkDao.deleteLinkById(linkId)
            flow { emit(Result.success(Unit)) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }
}
