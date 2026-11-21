package com.greenrobotdev.lordofthelinks.database.di

import com.greenrobotdev.lordofthelinks.database.room.AndroidDatabaseBuilder
import com.greenrobotdev.lordofthelinks.database.room.DatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule = module {
    single<DatabaseBuilder> {
        AndroidDatabaseBuilder(get())
    }
}
