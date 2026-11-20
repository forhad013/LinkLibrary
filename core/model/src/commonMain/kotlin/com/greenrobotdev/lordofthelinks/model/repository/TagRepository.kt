package com.greenrobotdev.lordofthelinks.model.repository

import com.greenrobotdev.lordofthelinks.model.Tag
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Tag data operations
 */
interface TagRepository {
    /**
     * Get all tags with their link counts
     */
    suspend fun getTags(): Flow<Result<List<Tag>>>

    /** All tags with how many links use each, sorted by name. */
    suspend fun getTagsWithCounts(): Flow<Result<List<Tag>>>

    /**
     * Get a specific tag by ID
     */
    suspend fun getTagById(id: String): Flow<Result<Tag?>>

    /**
     * Add a new tag
     */
    suspend fun addTag(tag: Tag): Flow<Result<Tag>>

    /**
     * Update an existing tag (name, description, icon)
     */
    suspend fun updateTag(tag: Tag): Flow<Result<Tag>>

    /**
     * Delete a tag
     */
    suspend fun deleteTag(tagId: String): Flow<Result<Unit>>

    /**
     * Get tags for a specific link
     */
    suspend fun getTagsForLink(linkId: String): Flow<Result<List<Tag>>>

    /**
     * Assign a tag to a link
     */
    suspend fun assignTagToLink(linkId: String, tagId: String): Flow<Result<Unit>>

    /**
     * Remove a tag from a link
     */
    suspend fun removeTagFromLink(linkId: String, tagId: String): Flow<Result<Unit>>
}
