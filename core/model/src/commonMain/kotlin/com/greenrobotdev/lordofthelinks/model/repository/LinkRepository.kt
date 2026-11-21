package com.greenrobotdev.lordofthelinks.model.repository

import com.greenrobotdev.lordofthelinks.model.Link
import kotlinx.coroutines.flow.Flow

interface LinkRepository {
    suspend fun getLinks(): Flow<Result<List<Link>>>
    suspend fun getLinksWithTags(): Flow<Result<List<Link>>>
    suspend fun toggleFavorite(linkId: String): Flow<Result<Link>>
    suspend fun addLink(link: Link): Flow<Result<Link>>
    suspend fun deleteLink(linkId: String): Flow<Result<Unit>>
    suspend fun updateLink(link: Link): Flow<Result<Link>>
    suspend fun markOpened(linkId: String): Flow<Result<Unit>>
    /** Stores the preview image URL ("" = the page has none, so it isn't fetched again). */
    suspend fun setImageUrl(linkId: String, imageUrl: String): Flow<Result<Unit>>
}
