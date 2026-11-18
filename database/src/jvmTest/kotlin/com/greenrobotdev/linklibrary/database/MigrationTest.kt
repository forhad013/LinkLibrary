package com.greenrobotdev.linklibrary.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import com.greenrobotdev.linklibrary.database.room.ALL_MIGRATIONS
import com.greenrobotdev.linklibrary.database.room.LinkDatabase
import com.greenrobotdev.linklibrary.database.room.LinkEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MigrationTest {

    private fun open(file: File) = Room.databaseBuilder<LinkDatabase>(name = file.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .addMigrations(*ALL_MIGRATIONS)
        .build() // no destructive fallback here: a missing or broken migration must fail the test

    @Test
    fun v4LinksSurviveTheImageUrlMigration() = runTest {
        val file = File.createTempFile("links", ".db").also { it.delete() }
        try {
            // Make a v5 database, then turn it back into v4: drop the new column and the version
            open(file).apply {
                linkDao().insertLink(LinkEntity(id = "a", title = "Kept", url = "https://a.com", description = "", isFavorite = true, createdAt = 42))
                close()
            }
            BundledSQLiteDriver().open(file.absolutePath).apply {
                execSQL("ALTER TABLE links DROP COLUMN imageUrl")
                execSQL("PRAGMA user_version = 4")
                close()
            }

            val db = open(file)
            val link = db.linkDao().getAllLinks().first().single()
            assertEquals("Kept", link.title)
            assertEquals(true, link.isFavorite)
            assertEquals(42L, link.createdAt)
            assertNull(link.imageUrl)
            db.close()
        } finally {
            file.delete()
            File(file.path + "-wal").delete()
            File(file.path + "-shm").delete()
        }
    }
}
