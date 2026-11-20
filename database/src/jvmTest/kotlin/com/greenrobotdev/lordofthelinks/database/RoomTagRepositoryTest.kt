package com.greenrobotdev.lordofthelinks.database

import androidx.room.Room
import com.greenrobotdev.lordofthelinks.database.repository.RoomTagRepository
import com.greenrobotdev.lordofthelinks.database.room.LinkDatabase
import com.greenrobotdev.lordofthelinks.database.room.TagEntity
import com.greenrobotdev.lordofthelinks.model.Tag
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RoomTagRepositoryTest {

    private lateinit var db: LinkDatabase
    private lateinit var repo: RoomTagRepository

    @BeforeTest
    fun setup() {
        db = Room.inMemoryDatabaseBuilder<LinkDatabase>()
            .setDriver(androidx.sqlite.driver.bundled.BundledSQLiteDriver())
            .build()
        repo = RoomTagRepository(db.tagDao(), db.linkTagDao())
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun renameKeepsCreatedAt() = runTest {
        db.tagDao().insertTag(TagEntity(id = "t", name = "old", createdAt = 1_000L, updatedAt = 1_000L))

        repo.updateTag(Tag(id = "t", name = "new", count = 0)).first().getOrThrow()

        val stored = db.tagDao().getTagById("t").first()!!
        assertEquals("new", stored.name)
        assertEquals(1_000L, stored.createdAt)
        assertTrue(stored.updatedAt > 1_000L)
    }

    @Test
    fun updatingAMissingTagFails() = runTest {
        assertTrue(repo.updateTag(Tag(id = "nope", name = "x", count = 0)).first().isFailure)
    }
}
