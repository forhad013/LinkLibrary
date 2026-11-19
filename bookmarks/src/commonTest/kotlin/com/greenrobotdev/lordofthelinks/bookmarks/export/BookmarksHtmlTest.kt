package com.greenrobotdev.lordofthelinks.bookmarks.export

import com.greenrobotdev.lordofthelinks.bookmarks.model.Category
import com.greenrobotdev.lordofthelinks.bookmarks.model.Link
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BookmarksHtmlTest {
    private val categories = listOf(Category("dev", "Dev"), Category("android", "Android", parentId = "dev"))

    @Test
    fun linksGoIntoCategoryFoldersWithTagsNotesAndFlags() {
        val html = bookmarksHtml(
            links = listOf(
                Link(id = "1", title = "Kotlin", url = "https://kotlinlang.org", description = "read later",
                    tags = listOf("kotlin", "kmp"), isFavorite = true, createdAt = 1_700_000_000_000),
                Link(id = "2", title = "", url = "https://a.com", isArchived = true),
            ),
            categories = categories,
            linkCategoryIds = mapOf("1" to "android"),
        )
        assertTrue(html.startsWith("<!DOCTYPE NETSCAPE-Bookmark-file-1>"))
        assertTrue("<DT><H3>Dev › Android</H3>" in html)
        assertTrue("""<DT><A HREF="https://kotlinlang.org" ADD_DATE="1700000000" TAGS="kotlin,kmp" LOL_FAVORITE="1">Kotlin</A>""" in html)
        assertTrue("<DD>read later" in html)
        // untitled links use their URL; archived flag kept; top level (no category)
        assertTrue("""    <DT><A HREF="https://a.com" LOL_ARCHIVED="1">https://a.com</A>""" in html)
    }

    @Test
    fun untrustedTextIsEscaped() {
        assertEquals("&lt;b&gt;Tom &amp; &quot;Jerry&quot;&lt;/b&gt;", escape("""<b>Tom & "Jerry"</b>"""))
    }
}
