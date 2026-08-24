package com.greenrobotdev.linklibrary.database.di

import com.greenrobotdev.linklibrary.database.repository.CollectionRepository
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.database.repository.RoomCollectionRepository
import com.greenrobotdev.linklibrary.database.repository.RoomLinkRepository
import com.greenrobotdev.linklibrary.database.repository.RoomTagRepository
import com.greenrobotdev.linklibrary.database.repository.TagRepository
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
    single<TagRepository> {
        RoomTagRepository(
            databaseBuilder = get() // Gets platformModule's DatabaseBuilder
        )
    }

    single<CollectionRepository> {
        RoomCollectionRepository(
            databaseBuilder = get() // Gets platformModule's DatabaseBuilder
        )
    }

    single<LinkRepository> {
        RoomLinkRepository(
            databaseBuilder = get(), // Gets platformModule's DatabaseBuilder
            tagRepository = get(),  // Gets TagRepository from above
            collectionRepository = get() // Gets CollectionRepository from above
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
