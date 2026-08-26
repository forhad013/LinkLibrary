package com.greenrobotdev.linklibrary.bookmarks.model

/**
 * Common data model for shared content from external apps
 * This is used to receive shared data from Android intents
 */
data class SharedContent(
    val url: String? = null,
    val text: String? = null,
    val title: String? = null
)
