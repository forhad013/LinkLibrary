package com.greenrobotdev.linklibrary.android

import android.app.Application
import com.greenrobotdev.linklibrary.database.di.databaseModule
import com.greenrobotdev.linklibrary.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(
                appModule,        // App services (HTTP client, metadata fetch service)
                databaseModule,   // Data repositories (common)
            )
        }

    }

}

