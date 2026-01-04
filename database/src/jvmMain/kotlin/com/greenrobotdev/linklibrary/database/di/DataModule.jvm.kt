package com.greenrobotdev.linklibrary.database.di

import androidx.room.RoomDatabase
import com.greenrobotdev.linklibrary.database.room.DatabaseBuilder
import com.greenrobotdev.linklibrary.database.room.LinkDatabase
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

/**
 * JVM-specific platform module.
 * Provides a file-system based DatabaseBuilder for desktop/JVM environments.
 */
actual val platformModule: Module = module {
    single<DatabaseBuilder> {
        object : DatabaseBuilder {
            override fun getDatabaseBuilder(): RoomDatabase.Builder<LinkDatabase> {
                // TODO: Implement proper JVM DatabaseBuilder with file path
                // For now, this is a placeholder
                throw NotImplementedError("JVM DatabaseBuilder not implemented yet. Use Android target.")
            }
        }
    }
}
