package com.greenrobotdev.lordofthelinks.bookmarks.screens.library

import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals

class FeedSectionTest {
    private val utc = TimeZone.UTC
    private val day = 24 * 60 * 60 * 1000L
    private val now = 1_763_337_600_000L + 12 * 60 * 60 * 1000L // a midday, so ±hours stay on the same date

    private fun section(ms: Long?) = feedSection(ms, now, utc)

    @Test
    fun bucketsByCalendarDay() {
        assertEquals("Today", section(now - 60_000))
        assertEquals("Yesterday", section(now - day))
        assertEquals("This week", section(now - 2 * day))
        assertEquals("This week", section(now - 6 * day))
        assertEquals("Earlier", section(now - 7 * day))
    }

    @Test
    fun feedCardDropsTheDayOnlyUnderTodayAndYesterday() {
        fun label(ms: Long) = savedAtLabel(ms, now, utc, timeOnlyIfRecent = true)
        assertEquals("10:00", label(now - 2 * 60 * 60 * 1000L))
        assertEquals("12:00", label(now - day))
        assertEquals("Today, 10:00", savedAtLabel(now - 2 * 60 * 60 * 1000L, now, utc)) // Link details keeps the day
        assertEquals("Saturday, 12:00", label(now - 2 * day))
    }

    @Test
    fun undatedAndFutureSaves() {
        assertEquals("Earlier", section(null))
        assertEquals("Earlier", section(0))
        assertEquals("Today", section(now + 3 * day)) // wrong device clock
    }
}
