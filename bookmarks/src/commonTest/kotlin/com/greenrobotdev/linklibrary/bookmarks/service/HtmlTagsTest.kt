package com.greenrobotdev.linklibrary.bookmarks.service

import kotlin.test.Test
import kotlin.test.assertEquals

class HtmlTagsTest {
    private fun ogImage(html: String) = findTagAttribute(html, "meta", "property", "og:image", "content")

    @Test
    fun attributeOrderDoesNotMatter() {
        assertEquals("https://a.com/1.png", ogImage("""<meta property="og:image" content="https://a.com/1.png"/>"""))
        assertEquals("https://a.com/2.png", ogImage("""<META content='https://a.com/2.png' Property="OG:IMAGE">"""))
    }

    @Test
    fun exactKeyOnly() {
        val html = """<meta property="og:image:width" content="1200"><meta property="og:image" content="https://a.com/x.png">"""
        assertEquals("https://a.com/x.png", ogImage(html))
        assertEquals("", ogImage("""<meta property="og:title" content="Hi">"""))
    }

    @Test
    fun linkHref() {
        assertEquals("/i.png", findTagAttribute("""<link href="/i.png" rel="image_src">""", "link", "rel", "image_src", "href"))
    }
}
