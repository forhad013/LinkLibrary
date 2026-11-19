package com.greenrobotdev.lordofthelinks.database.repository

import com.greenrobotdev.lordofthelinks.database.room.TagEntity
import com.greenrobotdev.lordofthelinks.database.room.TagWithCount
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Tag data operations
 */
interface TagRepository {
    /**
     * Get all tags with their link counts
     */
    suspend fun getTags(): Flow<Result<List<TagEntity>>>

    /** All tags with how many links use each, sorted by name. */
    suspend fun getTagsWithCounts(): Flow<Result<List<TagWithCount>>>

    /**
     * Get a specific tag by ID
     */
    suspend fun getTagById(id: String): Flow<Result<TagEntity?>>

    /**
     * Add a new tag
     */
    suspend fun addTag(tag: TagEntity): Flow<Result<TagEntity>>

    /**
     * Update an existing tag
     */
    suspend fun updateTag(tag: TagEntity): Flow<Result<TagEntity>>

    /**
     * Delete a tag
     */
    suspend fun deleteTag(tagId: String): Flow<Result<Unit>>

    /**
     * Get tags for a specific link
     */
    suspend fun getTagsForLink(linkId: String): Flow<Result<List<TagEntity>>>

    /**
     * Assign a tag to a link
     */
    suspend fun assignTagToLink(linkId: String, tagId: String): Flow<Result<Unit>>

    /**
     * Remove a tag from a link
     */
    suspend fun removeTagFromLink(linkId: String, tagId: String): Flow<Result<Unit>>
}