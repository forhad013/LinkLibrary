package com.greenrobotdev.lordofthelinks.database.di

import com.greenrobotdev.lordofthelinks.database.room.DatabaseBuilder
import com.greenrobotdev.lordofthelinks.database.room.JvmDatabaseBuilder
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
