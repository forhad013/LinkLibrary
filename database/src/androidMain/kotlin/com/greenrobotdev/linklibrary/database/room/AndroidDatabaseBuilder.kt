package com.greenrobotdev.linklibrary.database.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Android-specific database builder implementation.
 *
 * TODO: Add SQLCipher encryption for Android using Android Keystore.
 * This requires custom SQLiteDriver implementation for Room KMP.
 */
class AndroidDatabaseBuilder(
    private val context: Context
) : DatabaseBuilder {

    /**
     * Android database builder.
     */
    override fun getDatabaseBuilder(): RoomDatabase.Builder<LinkDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(LinkDatabase.DATABASE_NAME)

        return Room.databaseBuilder<LinkDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
            // Real migrations keep saved links across schema changes; the destructive fallback only
            // covers versions older than the first migration (dev builds before v4).
            .addMigrations(*ALL_MIGRATIONS)
            .fallbackToDestructiveMigration(dropAllTables = true)
    }
}
