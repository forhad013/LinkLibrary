package com.greenrobotdev.linklibrary.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Tag entities
 */
@Dao
interface TagDao {
    /**
     * Get all tags ordered by name
     */
    @Query("SELECT * FROM tags ORDER BY name ASC")
    fun getAllTags(): Flow<List<TagEntity>>

    /**
     * Get a specific tag by ID
     */
    @Query("SELECT * FROM tags WHERE id = :tagId")
    fun getTagById(tagId: String): Flow<TagEntity?>

    /**
     * Insert a new tag
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: TagEntity)

    /**
     * Update an existing tag
     */
    @Update
    suspend fun updateTag(tag: TagEntity)

    /**
     * Delete a tag by ID
     */
    @Query("DELETE FROM tags WHERE id = :tagId")
    suspend fun deleteTagById(tagId: String)

    /**
     * Get all tags for a specific link with tag details
     */
    @Query("""
        SELECT t.* FROM tags t
        INNER JOIN link_tags lt ON t.id = lt.tagId
        WHERE lt.linkId = :linkId
        ORDER BY t.name ASC
    """)
    fun getTagsForLink(linkId: String): Flow<List<TagEntity>>

    /**
     * Get all links for a specific tag
     */
    @Query("""
        SELECT l.* FROM links l
        INNER JOIN link_tags lt ON l.id = lt.linkId
        WHERE lt.tagId = :tagId
        ORDER BY l.createdAt DESC
    """)
    fun getLinksForTag(tagId: String): Flow<List<LinkEntity>>

    /**
     * Get tag count (number of links) for a specific tag
     */
    @Query("""
        SELECT COUNT(lt.linkId) FROM link_tags lt
        WHERE lt.tagId = :tagId
    """)
    fun getTagLinkCount(tagId: String): Flow<Int>

    /**
     * Get all tags with their link counts
     */
    @Query("""
        SELECT t.*, COUNT(lt.linkId) as linkCount
        FROM tags t
        LEFT JOIN link_tags lt ON t.id = lt.tagId
        GROUP BY t.id
        ORDER BY t.name ASC
    """)
    fun getAllTagsWithCounts(): Flow<List<TagWithCount>>
}

/**
 * Data class for tag with link count
 */
data class TagWithCount(
    val id: String,
    val name: String,
    val description: String?,
    val iconType: String,
    val createdAt: Long,
    val updatedAt: Long,
    val linkCount: Int
)