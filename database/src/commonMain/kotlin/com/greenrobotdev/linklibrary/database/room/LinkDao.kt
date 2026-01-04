package com.greenrobotdev.linklibrary.database.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LinkDao {
    @Query("SELECT * FROM links ORDER BY createdAt DESC")
    fun getAllLinks(): Flow<List<LinkEntity>>

    @Query("SELECT * FROM links WHERE id = :linkId")
    suspend fun getLinkById(linkId: String): LinkEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLink(link: LinkEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinks(links: List<LinkEntity>)

    @Update
    suspend fun updateLink(link: LinkEntity)

    @Delete
    suspend fun deleteLink(link: LinkEntity)

    @Query("UPDATE links SET isFavorite = NOT isFavorite WHERE id = :linkId")
    suspend fun toggleFavorite(linkId: String): Int

    @Query("DELETE FROM links WHERE id = :linkId")
    suspend fun deleteLinkById(linkId: String): Int
}
