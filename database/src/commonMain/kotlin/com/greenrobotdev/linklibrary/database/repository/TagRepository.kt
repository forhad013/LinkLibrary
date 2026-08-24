package com.greenrobotdev.linklibrary.database.repository

import com.greenrobotdev.linklibrary.database.room.TagEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Tag data operations
 */
interface TagRepository {
    /**
     * Get all tags with their link counts
     */
    suspend fun getTags(): Flow<Result<List<TagEntity>>>

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