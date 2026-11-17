package com.greenrobotdev.linklibrary.database.repository

import com.greenrobotdev.linklibrary.database.room.LinkEntity
import kotlinx.coroutines.flow.Flow

interface LinkRepository {
    suspend fun getLinks(): Flow<Result<List<LinkEntity>>>
    suspend fun getLinksWithTags(): Flow<Result<List<LinkEntity>>>
    suspend fun toggleFavorite(linkId: String): Flow<Result<LinkEntity>>
    suspend fun addLink(link: LinkEntity): Flow<Result<LinkEntity>>
    suspend fun deleteLink(linkId: String): Flow<Result<Unit>>
}
