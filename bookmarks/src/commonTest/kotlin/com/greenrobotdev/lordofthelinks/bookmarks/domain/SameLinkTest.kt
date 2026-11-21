package com.greenrobotdev.lordofthelinks.bookmarks.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SameLinkTest {
    @Test
    fun harmlessDifferencesMatch() {
        assertTrue(isSameLink("https://www.Example.com/post/", "http://example.com/post"))
        assertTrue(isSameLink("https://a.com/x?utm_source=share&id=7#top", "https://a.com/x?id=7"))
        assertTrue(isSameLink("https://a.com/x?b=2&a=1", "https://a.com/x?a=1&b=2"))
    }

    @Test
    fun differentPagesDoNotMatch() {
        assertFalse(isSameLink("https://a.com/x", "https://a.com/y"))
        assertFalse(isSameLink("https://youtube.com/watch?v=1", "https://youtube.com/watch?v=2"))
        assertFalse(isSameLink("https://blog.a.com/", "https://a.com/"))
    }
}
