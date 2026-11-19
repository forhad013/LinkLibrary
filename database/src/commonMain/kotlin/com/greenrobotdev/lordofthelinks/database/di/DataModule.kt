package com.greenrobotdev.lordofthelinks.database.di

import com.greenrobotdev.lordofthelinks.database.repository.CategoryRepository
import com.greenrobotdev.lordofthelinks.database.repository.LinkRepository
import com.greenrobotdev.lordofthelinks.database.repository.RoomCategoryRepository
import com.greenrobotdev.lordofthelinks.database.repository.RoomLinkRepository
import com.greenrobotdev.lordofthelinks.database.repository.RoomTagRepository
import com.greenrobotdev.lordofthelinks.database.repository.TagRepository
import com.greenrobotdev.lordofthelinks.database.room.DatabaseBuilder
import com.greenrobotdev.lordofthelinks.database.room.LinkDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Expect declaration for platform-specific Koin modules.
 */
expect val platformModule: Module

/**
 * Common data module. Single LinkDatabase instance shared by all repositories.
 */
val databaseDataModule: Module = module {
    single<LinkDatabase> { get<DatabaseBuilder>().getDatabaseBuilder().build() }

    single<TagRepository> { RoomTagRepository(get<LinkDatabase>().tagDao(), get<LinkDatabase>().linkTagDao()) }
    single<CategoryRepository> { RoomCategoryRepository(get<LinkDatabase>().categoryDao()) }
    single<LinkRepository> {
        RoomLinkRepository(get<LinkDatabase>().linkDao(), get<LinkDatabase>().tagDao(), get<LinkDatabase>().categoryDao())
    }
}

val databaseModule: Module = module {
    includes(databaseDataModule, platformModule)
}
