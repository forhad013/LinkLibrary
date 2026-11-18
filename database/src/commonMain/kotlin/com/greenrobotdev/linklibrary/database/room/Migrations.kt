package com.greenrobotdev.linklibrary.database.room

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

/** v5: links keep the page's preview image URL (og:image), fetched when the link is saved. */
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE links ADD COLUMN imageUrl TEXT")
    }
}

/**
 * v6: forget "page has no image" markers written while the fetcher couldn't read pages (it got
 * compressed HTML), so those links are fetched again. Data only, no schema change.
 */
val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("UPDATE links SET imageUrl = NULL WHERE imageUrl = ''")
    }
}

/** Every real migration, in order. Both platform builders add these. */
val ALL_MIGRATIONS = arrayOf(MIGRATION_4_5, MIGRATION_5_6)
