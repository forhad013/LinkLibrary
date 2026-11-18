package com.greenrobotdev.linklibrary.bookmarks.domain

/**
 * A URL reduced to what identifies the page, so the same link saved twice is spotted: ignores
 * http/https, a leading "www.", letter case in the host, a trailing "/", the #fragment and
 * utm_* tracking parameters. Other query parameters stay (they often pick the page).
 */
fun normalizedLink(url: String): String {
    val noFragment = url.trim().substringBefore('#')
    val withoutScheme = noFragment.substringAfter("://", noFragment)
    val hostAndPath = withoutScheme.substringBefore('?')
    val host = hostAndPath.substringBefore('/').lowercase().removePrefix("www.")
    val path = hostAndPath.substring(hostAndPath.substringBefore('/').length).trimEnd('/')
    val query = withoutScheme.substringAfter('?', "").split('&')
        .filter { it.isNotBlank() && !it.startsWith("utm_", ignoreCase = true) }
        .sorted()
        .joinToString("&")
    return host + path + (if (query.isEmpty()) "" else "?$query")
}

fun isSameLink(a: String, b: String): Boolean = normalizedLink(a) == normalizedLink(b)
