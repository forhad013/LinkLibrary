package com.greenrobotdev.linklibrary.bookmarks.screens.library

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CaughtUpTest {
    @Test
    fun openingOrArchivingTheLastUnopenedCelebrates() {
        assertTrue(celebrateCaughtUp(false, lastUnopened = 1, lastLinkCount = 10, unopened = 0, linkCount = 10))
    }

    @Test
    fun noCelebrationAfterRestoreOrOnLaunch() {
        // first reading after launch (or after a restore into an empty app): nothing was unopened before
        assertFalse(celebrateCaughtUp(false, lastUnopened = -1, lastLinkCount = -1, unopened = 0, linkCount = 19))
        assertFalse(celebrateCaughtUp(false, lastUnopened = 0, lastLinkCount = 0, unopened = 0, linkCount = 19))
    }

    @Test
    fun deletingTheLastUnopenedDoesNotCelebrate() {
        assertFalse(celebrateCaughtUp(false, lastUnopened = 1, lastLinkCount = 10, unopened = 0, linkCount = 9))
    }

    @Test
    fun aNewUnopenedSaveEndsTheCelebration() {
        assertTrue(celebrateCaughtUp(true, lastUnopened = 0, lastLinkCount = 10, unopened = 0, linkCount = 10))
        assertFalse(celebrateCaughtUp(true, lastUnopened = 0, lastLinkCount = 10, unopened = 1, linkCount = 11))
    }
}
