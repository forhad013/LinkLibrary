package com.greenrobotdev.linklibrary.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Collection entities
 */
@Dao
interface CollectionDao {
    /**
     * Get all collections ordered by name
     */
    @Query("SELECT * FROM collections ORDER BY name ASC")
    fun getAllCollections(): Flow<List<CollectionEntity>>

    /**
     * Get a specific collection by ID
     */
    @Query("SELECT * FROM collections WHERE id = :collectionId")
    fun getCollectionById(collectionId: String): Flow<CollectionEntity?>

    /**
     * Insert a new collection
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionEntity)

    /**
     * Update an existing collection
     */
    @Update
    suspend fun updateCollection(collection: CollectionEntity)

    /**
     * Delete a collection by ID
     */
    @Query("DELETE FROM collections WHERE id = :collectionId")
    suspend fun deleteCollectionById(collectionId: String)

    /**
     * Get all collections for a specific link with collection details
     */
    @Query("""
        SELECT c.* FROM collections c
        INNER JOIN link_collections lc ON c.id = lc.collectionId
        WHERE lc.linkId = :linkId
        ORDER BY c.name ASC
    """)
    fun getCollectionsForLink(linkId: String): Flow<List<CollectionEntity>>

    /**
     * Get all links for a specific collection
     */
    @Query("""
        SELECT l.* FROM links l
        INNER JOIN link_collections lc ON l.id = lc.linkId
        WHERE lc.collectionId = :collectionId
        ORDER BY l.createdAt DESC
    """)
    fun getLinksForCollection(collectionId: String): Flow<List<LinkEntity>>

    /**
     * Get collection count (number of links) for a specific collection
     */
    @Query("""
        SELECT COUNT(lc.linkId) FROM link_collections lc
        WHERE lc.collectionId = :collectionId
    """)
    fun getCollectionLinkCount(collectionId: String): Flow<Int>

    /**
     * Get all collections with their link counts
     */
    @Query("""
        SELECT c.*, COUNT(lc.linkId) as linkCount
        FROM collections c
        LEFT JOIN link_collections lc ON c.id = lc.collectionId
        GROUP BY c.id
        ORDER BY c.name ASC
    """)
    fun getAllCollectionsWithCounts(): Flow<List<CollectionWithCount>>
}

/**
 * Data class for collection with link count
 */
data class CollectionWithCount(
    val id: String,
    val name: String,
    val description: String?,
    val iconType: String,
    val createdAt: Long,
    val updatedAt: Long,
    val linkCount: Int
)