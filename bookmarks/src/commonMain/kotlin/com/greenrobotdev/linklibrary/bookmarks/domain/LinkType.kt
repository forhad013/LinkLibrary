package com.greenrobotdev.linklibrary.bookmarks.domain

/** Host without scheme, "www." or path, e.g. "youtube.com". Empty if the URL has none. */
fun hostOf(url: String): String =
    url.trim().substringAfter("://").substringBefore('/').substringBefore('?').removePrefix("www.").lowercase()

private val videoHosts = listOf("youtube.com", "youtu.be", "vimeo.com", "twitch.tv", "tiktok.com", "dailymotion.com")
private val socialHosts = listOf("twitter.com", "x.com", "instagram.com", "facebook.com", "threads.net", "reddit.com", "linkedin.com", "mastodon.social", "bsky.app")

/** Link type stored on LinkEntity.type: video | social | article. Guessed from the host only. */
fun linkTypeOf(url: String): String {
    val host = hostOf(url)
    fun matches(list: List<String>) = list.any { host == it || host.endsWith(".$it") }
    return when {
        matches(videoHosts) -> "video"
        matches(socialHosts) -> "social"
        else -> "article"
    }
}
