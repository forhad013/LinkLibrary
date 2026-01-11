package com.greenrobotdev.linklibrary.database.room

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

/**
 * JVM-specific database builder implementation.
 *
 * Uses file-system based database storage for desktop/JVM environments.
 * Database is stored in the user's home directory under .LinkLibrary folder.
 */
class JvmDatabaseBuilder(
    private val databasePath: String = defaultDatabasePath()
) : DatabaseBuilder {

    /**
     * JVM database builder.
     * Creates database in user's home directory.
     */
    override fun getDatabaseBuilder(): RoomDatabase.Builder<LinkDatabase> {
        val dbDir = File(databasePath)
        dbDir.mkdirs()

        val dbFile = File(dbDir, LinkDatabase.DATABASE_NAME)

        return Room.databaseBuilder<LinkDatabase>(
            name = dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
    }

    companion object {
        /**
         * Default database path for JVM platforms.
         * Uses user's home directory with .LinkLibrary subdirectory.
         */
        fun defaultDatabasePath(): String {
            val userHome = System.getProperty("user.home")
            return "$userHome/.LinkLibrary/database"
        }
    }
}
