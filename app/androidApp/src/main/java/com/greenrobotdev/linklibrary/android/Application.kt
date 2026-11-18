package com.greenrobotdev.linklibrary.android

import com.greenrobotdev.linklibrary.design.debug.DebugLog
import android.content.pm.ApplicationInfo
import android.app.Application
import com.greenrobotdev.linklibrary.database.di.databaseModule
import com.greenrobotdev.linklibrary.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        // LoL/ logcat lines only in debuggable (debug) builds
        DebugLog.enabled = applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

        startKoin {
            androidContext(this@Application)
            modules(
                appModule,        // App services (HTTP client, metadata fetch service)
                databaseModule,   // Data repositories (common)
            )
        }

    }

}

