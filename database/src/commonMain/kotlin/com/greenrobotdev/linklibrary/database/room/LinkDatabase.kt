package com.greenrobotdev.linklibrary.database.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

/**
 * Room Database for KMP following the official Android pattern.
 *
 * Uses @ConstructedBy annotation with expect/actual pattern for
 * platform-specific database construction.
 *
 * @see androidx.room.RoomDatabase
 * @see androidx.room.constructor.ConstructedBy
 */
@Database(
    entities = [LinkEntity::class],
    version = 1,
    exportSchema = false
)
@ConstructedBy(LinkDatabaseConstructor::class)
abstract class LinkDatabase : RoomDatabase() {

    abstract fun linkDao(): LinkDao

    companion object {
        const val DATABASE_NAME = "link_library.db"
    }
}

/**
 * Expect declaration for the Room Database Constructor.
 * The Room compiler generates the actual implementations for each platform.
 */
@Suppress("KotlinNoActualForExpect")
expect object LinkDatabaseConstructor : RoomDatabaseConstructor<LinkDatabase> {
    override fun initialize(): LinkDatabase
}
