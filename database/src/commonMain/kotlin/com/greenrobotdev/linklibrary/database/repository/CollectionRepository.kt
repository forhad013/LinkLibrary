package com.greenrobotdev.linklibrary.database.repository

import com.greenrobotdev.linklibrary.database.room.CollectionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Collection data operations
 */
interface CollectionRepository {
    /**
     * Get all collections with their link counts
     */
    suspend fun getCollections(): Flow<Result<List<CollectionEntity>>>

    /**
     * Get a specific collection by ID
     */
    suspend fun getCollectionById(id: String): Flow<Result<CollectionEntity?>>

    /**
     * Add a new collection
     */
    suspend fun addCollection(collection: CollectionEntity): Flow<Result<CollectionEntity>>

    /**
     * Update an existing collection
     */
    suspend fun updateCollection(collection: CollectionEntity): Flow<Result<CollectionEntity>>

    /**
     * Delete a collection
     */
    suspend fun deleteCollection(collectionId: String): Flow<Result<Unit>>

    /**
     * Get collections for a specific link
     */
    suspend fun getCollectionsForLink(linkId: String): Flow<Result<List<CollectionEntity>>>

    /**
     * Assign a link to a collection
     */
    suspend fun assignLinkToCollection(linkId: String, collectionId: String): Flow<Result<Unit>>

    /**
     * Remove a link from a collection
     */
    suspend fun removeLinkFromCollection(linkId: String, collectionId: String): Flow<Result<Unit>>
}