package com.greenrobotdev.lordofthelinks.bookmarks.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class IsValidLinkTest {

    @Test
    fun validUrls() {
        listOf(
            "https://example.com",
            "http://example.com/path?query=1",
            "HTTPS://EXAMPLE.COM",
            "http://a.co",
        ).forEach { assertEquals(true, isValidLink(it), it) }
    }

    @Test
    fun invalidUrls() {
        listOf(
            "",
            "example.com",
            "www.example.com",
            "ftp://example.com",
            "https://",
            "https:// space",
        ).forEach { assertEquals(false, isValidLink(it), it) }
    }
}
