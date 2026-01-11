package com.greenrobotdev.linklibrary.database.di

import com.greenrobotdev.linklibrary.database.room.DatabaseBuilder
import com.greenrobotdev.linklibrary.database.room.JvmDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * JVM-specific platform module.
 * Provides JvmDatabaseBuilder for desktop/JVM environments.
 */
actual val platformModule: Module = module {
    single<DatabaseBuilder> {
        JvmDatabaseBuilder()
    }
}
