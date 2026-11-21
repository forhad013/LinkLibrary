package com.greenrobotdev.lordofthelinks.database.repository

import com.greenrobotdev.lordofthelinks.database.room.CategoryDao
import com.greenrobotdev.lordofthelinks.database.room.LinkDao
import com.greenrobotdev.lordofthelinks.database.room.TagDao
import com.greenrobotdev.lordofthelinks.model.Link
import com.greenrobotdev.lordofthelinks.model.repository.LinkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class RoomLinkRepository(
    private val linkDao: LinkDao,
    private val tagDao: TagDao,
    private val categoryDao: CategoryDao
) : LinkRepository {

    override suspend fun getLinks(): Flow<Result<List<Link>>> =
        linkDao.getAllLinks()
            .map { entities -> Result.success(entities.map { it.toDomain() }) }
            .catch { emit(Result.failure(it)) }

    override suspend fun getLinksWithTags(): Flow<Result<List<Link>>> {
        return flow {
            try {
                // tag counts change with link_tags, so tags assigned after a save re-emit too
                combine(linkDao.getAllLinks(), tagDao.getAllTagsWithCounts()) { entities, _ -> entities }.collect { entities ->
                    val enriched = entities.map { entity ->
                        val tagNames = tagDao.getTagsForLink(entity.id).first()
                            .map { it.name }
                        entity.copy(tags = tagNames).toDomain()
                    }
                    emit(Result.success(enriched))
                }
            } catch (e: kotlin.coroutines.cancellation.CancellationException) {
                // cancellation (incl. first() AbortFlow) must propagate, not become Result.failure
                throw e
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }

    override suspend fun toggleFavorite(linkId: String): Flow<Result<Link>> {
        return try {
            linkDao.toggleFavorite(linkId)
            val entity = linkDao.getLinkById(linkId)
            entity?.let { flow { emit(Result.success(it.toDomain())) } }
                ?: flow { emit(Result.failure(Exception("Link not found"))) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    override suspend fun addLink(link: Link): Flow<Result<Link>> {
        return try {
            val entity = link.toEntity()
            val withId = if (entity.id.isEmpty()) entity.copy(id = Uuid.random().toString()) else entity
            val linkToSave = if (withId.createdAt == null) withId.copy(createdAt = Clock.System.now().toEpochMilliseconds()) else withId
            linkDao.insertLink(linkToSave)
            flow { emit(Result.success(linkToSave.toDomain())) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    /** Plain UPDATE: unlike addLink's REPLACE it keeps the link's tag/category rows (FK cascade). */
    override suspend fun updateLink(link: Link): Flow<Result<Link>> {
        return try {
            linkDao.updateLink(link.toEntity())
            flow { emit(Result.success(link)) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    override suspend fun markOpened(linkId: String): Flow<Result<Unit>> {
        return try {
            linkDao.markOpened(linkId)
            flow { emit(Result.success(Unit)) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    override suspend fun setImageUrl(linkId: String, imageUrl: String): Flow<Result<Unit>> {
        return try {
            linkDao.setImageUrl(linkId, imageUrl)
            flow { emit(Result.success(Unit)) }
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
