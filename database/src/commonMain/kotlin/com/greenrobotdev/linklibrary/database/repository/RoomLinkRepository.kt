package com.greenrobotdev.linklibrary.database.repository

import com.greenrobotdev.linklibrary.database.room.DatabaseBuilder
import com.greenrobotdev.linklibrary.database.room.LinkDatabase
import com.greenrobotdev.linklibrary.database.room.LinkEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Room-based implementation of LinkRepository for KMP following official Android pattern.
 *
 * This implementation works across all platforms using the DatabaseBuilder interface:
 * - Android: Uses AndroidDatabaseBuilder with optional SQLCipher encryption
 * - iOS/macOS: Can provide iOSDatabaseBuilder when needed
 * - JVM/Desktop: Can provide JvmDatabaseBuilder when needed
 *
 * @param databaseBuilder Platform-specific implementation of DatabaseBuilder interface
 * @param tagRepository Repository for loading tags associated with links
 * @param collectionRepository Repository for loading collections associated with links
 */
class RoomLinkRepository(
    private val databaseBuilder: DatabaseBuilder,
    private val tagRepository: TagRepository,
    private val collectionRepository: CollectionRepository
) : LinkRepository {

    private val database: LinkDatabase = databaseBuilder.getDatabaseBuilder().build()

    private val linkDao = database.linkDao()

    override suspend fun getLinks(): Flow<Result<List<LinkEntity>>> {
        return linkDao.getAllLinks()
            .map { entities -> Result.success(entities) }
            .catch { e -> emit(Result.failure(e)) }
    }

    override suspend fun getLinksWithTags(): Flow<Result<List<LinkEntity>>> {
        return flow {
            try {
                linkDao.getAllLinks().collect { entities ->
                    val entitiesWithTags = entities.map { entity ->
                        val tagsResult = tagRepository.getTagsForLink(entity.id).first()
                        val tagEntities = tagsResult.getOrElse { emptyList() }
                        val tagNames = tagEntities.map { it.name }
                        entity.copy(tags = tagNames)
                    }
                    emit(Result.success(entitiesWithTags))
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
            entity?.let { linkEntity ->
                flow { emit(Result.success(linkEntity)) }
            } ?: flow { emit(Result.failure(Exception("Link not found"))) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addLink(link: LinkEntity): Flow<Result<LinkEntity>> {
        return try {
            // Generate new ID if not provided
            val linkToSave = if (link.id.isEmpty()) {
                link.copy(id = Uuid.random().toString())
            } else {
                link
            }

            linkDao.insertLink(linkToSave)
            flow { emit(Result.success(linkToSave)) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    override suspend fun deleteLink(linkId: String): Flow<Result<Unit>> {
        return try {
            linkDao.deleteLinkById(linkId)
            kotlinx.coroutines.flow.flow {
                emit(Result.success(Unit))
            }
        } catch (e: Exception) {
            kotlinx.coroutines.flow.flow {
                emit(Result.failure(e))
            }
        }
    }

    /**
     * Close the database connection.
     */
    fun close() {
        database.close()
    }
}
