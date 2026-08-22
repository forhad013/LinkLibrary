package com.greenrobotdev.linklibrary.di

import com.greenrobotdev.linklibrary.config.StitchClientConfig
import com.greenrobotdev.linklibrary.config.StitchConfig
import com.greenrobotdev.linklibrary.data.stitch.StitchApiClient
import com.greenrobotdev.linklibrary.data.stitch.StitchRepository
import com.greenrobotdev.linklibrary.data.stitch.StitchRepositoryImpl
import org.koin.dsl.module

/**
 * Koin DI module for Google Stitch AI dependencies
 */
val stitchModule = module {

    // Stitch API Configuration
    single<StitchClientConfig> {
        val config = StitchConfig.getConfig()
        StitchClientConfig(
            apiKey = config.apiKey,
            endpoint = config.endpoint,
            timeoutMs = 30000L
        )
    }

    // Stitch API Client
    single<StitchApiClient> {
        StitchApiClient(get())
    }

    // Stitch Repository
    single<StitchRepository> {
        StitchRepositoryImpl(get())
    }

    // Optional: Singleton for managing Stitch client lifecycle
    factory {
        get<StitchApiClient>()
    }
}
