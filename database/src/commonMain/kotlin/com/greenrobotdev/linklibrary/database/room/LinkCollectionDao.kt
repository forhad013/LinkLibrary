package com.greenrobotdev.linklibrary.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Link-Collection junction table
 */
@Dao
interface LinkCollectionDao {
    /**
     * Assign a link to a collection
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun assignLinkToCollection(linkCollection: LinkCollectionEntity)

    /**
     * Remove a link from a collection
     */
    @Query("DELETE FROM link_collections WHERE linkId = :linkId AND collectionId = :collectionId")
    suspend fun removeLinkFromCollection(linkId: String, collectionId: String)

    /**
     * Remove all collections from a link
     */
    @Query("DELETE FROM link_collections WHERE linkId = :linkId")
    suspend fun removeAllCollectionsFromLink(linkId: String)

    /**
     * Remove all links from a collection
     */
    @Query("DELETE FROM link_collections WHERE collectionId = :collectionId")
    suspend fun removeAllLinksFromCollection(collectionId: String)

    /**
     * Check if a link is in a specific collection
     */
    @Query("SELECT COUNT(*) > 0 FROM link_collections WHERE linkId = :linkId AND collectionId = :collectionId")
    suspend fun hasLinkCollection(linkId: String, collectionId: String): Boolean

    /**
     * Get all collection IDs for a specific link
     */
    @Query("SELECT collectionId FROM link_collections WHERE linkId = :linkId")
    fun getCollectionIdsForLink(linkId: String): Flow<List<String>>

    /**
     * Get all link IDs for a specific collection
     */
    @Query("SELECT linkId FROM link_collections WHERE collectionId = :collectionId")
    fun getLinkIdsForCollection(collectionId: String): Flow<List<String>>
}