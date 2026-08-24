package com.greenrobotdev.linklibrary.database.repository

import com.greenrobotdev.linklibrary.database.room.CollectionEntity
import com.greenrobotdev.linklibrary.database.room.DatabaseBuilder
import com.greenrobotdev.linklibrary.database.room.LinkCollectionEntity
import com.greenrobotdev.linklibrary.database.room.LinkDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Room-based implementation of CollectionRepository for KMP following official Android pattern.
 *
 * This implementation works across all platforms using the DatabaseBuilder interface:
 * - Android: Uses AndroidDatabaseBuilder with optional SQLCipher encryption
 * - iOS/macOS: Can provide iOSDatabaseBuilder when needed
 * - JVM/Desktop: Can provide JvmDatabaseBuilder when needed
 *
 * @param databaseBuilder Platform-specific implementation of DatabaseBuilder interface
 */
class RoomCollectionRepository(
    private val databaseBuilder: DatabaseBuilder
) : CollectionRepository {

    private val database: LinkDatabase = databaseBuilder.getDatabaseBuilder().build()

    private val collectionDao = database.collectionDao()
    private val linkCollectionDao = database.linkCollectionDao()

    override suspend fun getCollections(): Flow<Result<List<CollectionEntity>>> {
        return collectionDao.getAllCollectionsWithCounts()
            .map { collectionWithCounts ->
                val collections = collectionWithCounts.map { collectionWithCount ->
                    CollectionEntity(
                        id = collectionWithCount.id,
                        name = collectionWithCount.name,
                        description = collectionWithCount.description,
                        iconType = collectionWithCount.iconType,
                        createdAt = collectionWithCount.createdAt,
                        updatedAt = collectionWithCount.updatedAt
                    )
                }
                Result.success(collections)
            }
            .catch { e -> emit(Result.failure(e)) }
    }

    override suspend fun getCollectionById(id: String): Flow<Result<CollectionEntity?>> {
        return collectionDao.getCollectionById(id)
            .map { entity -> Result.success(entity) }
            .catch { e -> emit(Result.failure(e)) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addCollection(collection: CollectionEntity): Flow<Result<CollectionEntity>> {
        return try {
            // Generate new ID if not provided
            val collectionToSave = if (collection.id.isEmpty()) {
                collection.copy(id = Uuid.random().toString())
            } else {
                collection
            }

            collectionDao.insertCollection(collectionToSave)
            flow { emit(Result.success(collectionToSave)) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    override suspend fun updateCollection(collection: CollectionEntity): Flow<Result<CollectionEntity>> {
        return try {
            collectionDao.updateCollection(collection)
            flow { emit(Result.success(collection)) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    override suspend fun deleteCollection(collectionId: String): Flow<Result<Unit>> {
        return try {
            collectionDao.deleteCollectionById(collectionId)
            flow { emit(Result.success(Unit)) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    override suspend fun getCollectionsForLink(linkId: String): Flow<Result<List<CollectionEntity>>> {
        return collectionDao.getCollectionsForLink(linkId)
            .map { entities -> Result.success(entities) }
            .catch { e -> emit(Result.failure(e)) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun assignLinkToCollection(linkId: String, collectionId: String): Flow<Result<Unit>> {
        return try {
            val linkCollection = LinkCollectionEntity(
                id = Uuid.random().toString(),
                linkId = linkId,
                collectionId = collectionId
            )
            linkCollectionDao.assignLinkToCollection(linkCollection)
            flow { emit(Result.success(Unit)) }
        } catch (e: Exception) {
            flow { emit(Result.failure(e)) }
        }
    }

    override suspend fun removeLinkFromCollection(linkId: String, collectionId: String): Flow<Result<Unit>> {
        return try {
            linkCollectionDao.removeLinkFromCollection(linkId, collectionId)
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