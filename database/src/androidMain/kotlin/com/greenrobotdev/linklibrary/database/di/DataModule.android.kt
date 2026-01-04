package com.greenrobotdev.linklibrary.database.di

import com.greenrobotdev.linklibrary.database.room.AndroidDatabaseBuilder
import com.greenrobotdev.linklibrary.database.room.DatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule = module {
    single<DatabaseBuilder> {
        AndroidDatabaseBuilder(get())
    }
}
