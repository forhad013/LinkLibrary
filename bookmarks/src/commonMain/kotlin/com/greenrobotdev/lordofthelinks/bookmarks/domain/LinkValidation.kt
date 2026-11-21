package com.greenrobotdev.lordofthelinks.bookmarks.domain

/**
 * Validates a user-entered web address. Shared by the add-link sheet and clipboard detection.
 */
fun isValidLink(url: String): Boolean {
    return Regex("^https?://\\S+$", RegexOption.IGNORE_CASE).matches(url.trim())
}
