package com.greenrobotdev.linklibrary.database.room

import androidx.room.RoomDatabase

/**
 * Interface for platform-specific database builders.
 * Each platform provides its own implementation to handle
 * platform-specific database configuration (e.g., encryption, file paths).
 *
 * Android implementation uses SQLCipher encryption via AndroidSQLiteDriver.
 * iOS and JVM can provide their own implementations when needed.
 */
interface DatabaseBuilder {
    /**
     * Returns a configured RoomDatabase.Builder for the platform.
     *
     * @return RoomDatabase.Builder configured with platform-specific settings
     */
    fun getDatabaseBuilder(): RoomDatabase.Builder<LinkDatabase>
}
