package com.greenrobotdev.linklibrary.database.repository

import com.greenrobotdev.linklibrary.database.room.LinkEntity
import kotlinx.coroutines.flow.Flow

interface LinkRepository {
    suspend fun getLinks(): Flow<Result<List<LinkEntity>>>
    suspend fun getLinksWithTags(): Flow<Result<List<LinkEntity>>>
    suspend fun toggleFavorite(linkId: String): Flow<Result<LinkEntity>>
    suspend fun addLink(link: LinkEntity): Flow<Result<LinkEntity>>
    suspend fun deleteLink(linkId: String): Flow<Result<Unit>>
    suspend fun updateLink(link: LinkEntity): Flow<Result<LinkEntity>>
    suspend fun markOpened(linkId: String): Flow<Result<Unit>>
    /** Stores the preview image URL ("" = the page has none, so it isn't fetched again). */
    suspend fun setImageUrl(linkId: String, imageUrl: String): Flow<Result<Unit>>
}
