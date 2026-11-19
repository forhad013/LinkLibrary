package com.greenrobotdev.lordofthelinks.bookmarks.screens.library

import com.greenrobotdev.lordofthelinks.bookmarks.model.Link
import kotlin.test.Test
import kotlin.test.assertEquals

class ResurfaceTest {
    private val day = 24 * 60 * 60 * 1000L
    private val now = 400 * day

    @Test
    fun labelSaysHowLongAgo() {
        assertEquals("From your older saves", resurfaceLabel(null, now))
        assertEquals("You saved this 7 days ago", resurfaceLabel(now - 7 * day, now))
        assertEquals("You saved this 2 weeks ago", resurfaceLabel(now - 14 * day, now))
        assertEquals("You saved this 2 months ago", resurfaceLabel(now - 60 * day, now))
        assertEquals("You saved this over a year ago", resurfaceLabel(now - 365 * day, now))
    }

    @Test
    fun candidatesSkipThePickUpRowRecentAndDoneSaves() {
        fun link(id: String, ageDays: Int?, opened: Boolean = false, archived: Boolean = false) =
            Link(id = id, title = id, url = "https://$id.com", isOpened = opened, isArchived = archived,
                createdAt = ageDays?.let { now - it * day })
        val newest = (1..PICK_UP_COUNT).map { link("new$it", 0) } // fill the pick-up row
        val state = LibraryState(links = newest + listOf(
            link("recent", 3),
            link("old", 20),
            link("older", 40),
            link("undated", null),
            link("opened", 30, opened = true),
            link("archived", 30, archived = true),
        ))
        assertEquals(listOf("undated", "older", "old"), state.resurfaceCandidates(now).map { it.id })
    }
}
