package com.greenrobotdev.lordofthelinks.bookmarks.screens.library

import com.greenrobotdev.lordofthelinks.utils.settings.createPlatformSettings
import com.russhwolf.settings.Settings
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** Keeps the Feed's [FeedFilters] across app restarts, as one JSON string. */
class FeedFilterStore(private val settings: Settings = createPlatformSettings()) {

    private val json = Json { ignoreUnknownKeys = true }

    /** Saved filters, or defaults when nothing is saved or the saved shape no longer decodes. */
    fun load(): FeedFilters =
        settings.getStringOrNull(KEY)?.let { runCatching { json.decodeFromString<FeedFilters>(it) }.getOrNull() }
            ?: FeedFilters()

    fun save(filters: FeedFilters) = settings.putString(KEY, json.encodeToString(filters))

    private companion object {
        const val KEY = "feed_filters"
    }
}
