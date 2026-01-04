package com.greenrobotdev.linklibrary.database.di

import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.database.repository.RoomLinkRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Expect declaration for platform-specific Koin modules.
 *
 * Actual implementations are provided per platform:
 * - Android: DataModule.android.kt (provides AndroidDatabaseBuilder)
 * - JVM: DataModule.jvm.kt (placeholder, to be implemented)
 */
expect val platformModule: Module

/**
 * Common data module - provides repositories.
 * Works across all platforms (Android, JVM, iOS when added).
 */
val databaseDataModule: Module = module {
    single<LinkRepository> {
        RoomLinkRepository(
            databaseBuilder = get() // Gets platformModule's DatabaseBuilder
        )
    }
}

/**
 * Combined database module.
 * Loads both common repositories and platform-specific database builder.
 */
val databaseModule: Module = module {
    includes(databaseDataModule, platformModule)
}
