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
    // rowid breaks ties (and orders links saved before createdAt was stamped) by insertion order
    @Query("SELECT * FROM links ORDER BY createdAt DESC, rowid DESC")
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

    @Query("UPDATE links SET isOpened = 1 WHERE id = :linkId")
    suspend fun markOpened(linkId: String): Int

    /** Only this column, so a background preview fetch never overwrites the user's own edits. */
    @Query("UPDATE links SET imageUrl = :imageUrl WHERE id = :linkId")
    suspend fun setImageUrl(linkId: String, imageUrl: String): Int

    @Query("UPDATE links SET isFavorite = NOT isFavorite WHERE id = :linkId")
    suspend fun toggleFavorite(linkId: String): Int

    @Query("DELETE FROM links WHERE id = :linkId")
    suspend fun deleteLinkById(linkId: String): Int
}
