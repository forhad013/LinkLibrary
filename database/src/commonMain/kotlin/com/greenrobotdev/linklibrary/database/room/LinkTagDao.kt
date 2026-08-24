package com.greenrobotdev.linklibrary.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Link-Tag junction table
 */
@Dao
interface LinkTagDao {
    /**
     * Assign a tag to a link
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun assignTagToLink(linkTag: LinkTagEntity)

    /**
     * Remove a tag from a link
     */
    @Query("DELETE FROM link_tags WHERE linkId = :linkId AND tagId = :tagId")
    suspend fun removeTagFromLink(linkId: String, tagId: String)

    /**
     * Remove all tags from a link
     */
    @Query("DELETE FROM link_tags WHERE linkId = :linkId")
    suspend fun removeAllTagsFromLink(linkId: String)

    /**
     * Remove all links from a tag
     */
    @Query("DELETE FROM link_tags WHERE tagId = :tagId")
    suspend fun removeAllLinksFromTag(tagId: String)

    /**
     * Check if a link has a specific tag
     */
    @Query("SELECT COUNT(*) > 0 FROM link_tags WHERE linkId = :linkId AND tagId = :tagId")
    suspend fun hasLinkTag(linkId: String, tagId: String): Boolean

    /**
     * Get all tag IDs for a specific link
     */
    @Query("SELECT tagId FROM link_tags WHERE linkId = :linkId")
    fun getTagIdsForLink(linkId: String): Flow<List<String>>

    /**
     * Get all link IDs for a specific tag
     */
    @Query("SELECT linkId FROM link_tags WHERE tagId = :tagId")
    fun getLinkIdsForTag(tagId: String): Flow<List<String>>
}