package com.greenrobotdev.linklibrary.database

import androidx.room.Room
import com.greenrobotdev.linklibrary.database.room.CategoryEntity
import com.greenrobotdev.linklibrary.database.room.LinkDatabase
import com.greenrobotdev.linklibrary.database.room.MaxDepthExceededException
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CategoryDaoTest {

    private lateinit var db: LinkDatabase

    @BeforeTest
    fun setup() {
        db = Room.inMemoryDatabaseBuilder<LinkDatabase>()
            .setDriver(androidx.sqlite.driver.bundled.BundledSQLiteDriver())
            .build()
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    private suspend fun insert(id: String, parentId: String? = null) =
        db.categoryDao().insertCategory(CategoryEntity(id = id, name = id, parentId = parentId))

    @Test
    fun allowsUpToFiveLevels() = runTest {
        insert("l1")
        insert("l2", "l1")
        insert("l3", "l2")
        insert("l4", "l3")
        insert("l5", "l4")
        assertEquals(5, db.categoryDao().depthOf("l5"))
    }

    @Test
    fun rejectsInsertBeyondFiveLevels() = runTest {
        insert("l1"); insert("l2", "l1"); insert("l3", "l2"); insert("l4", "l3"); insert("l5", "l4")
        assertFailsWith<MaxDepthExceededException> { insert("l6", "l5") }
    }

    @Test
    fun rejectsMoveThatWouldExceedFiveLevels() = runTest {
        // chain A: a1..a5 (full depth)
        insert("a1"); insert("a2", "a1"); insert("a3", "a2"); insert("a4", "a3"); insert("a5", "a4")
        // separate chain B: b1 -> b2
        insert("b1"); insert("b2", "b1")
        // moving b1 (subtree depth 2) under a4 would put b2 at depth 6
        assertFailsWith<Exception> { db.categoryDao().moveCategory("b1", "a4") }
        // moving b1 under a4's shallower sibling-level a3 is also too deep (b2 at 5 is ok? a3 depth 3 + b2 => 5) -> allowed
        db.categoryDao().moveCategory("b1", "a3")
        assertEquals(5, db.categoryDao().depthOf("b2"))
    }

    @Test
    fun rejectsMoveUnderOwnDescendant() = runTest {
        insert("p"); insert("c", "p")
        assertFailsWith<Exception> { db.categoryDao().moveCategory("p", "c") }
    }

    @Test
    fun oneCategoryPerLink() = runTest {
        val dao = db.categoryDao()
        insert("c1"); insert("c2")
        db.linkDao().insertLink(
            com.greenrobotdev.linklibrary.database.room.LinkEntity(
                id = "link", title = "t", url = "https://example.com", description = "", isFavorite = false
            )
        )
        dao.assignLinkToCategory(
            com.greenrobotdev.linklibrary.database.room.LinkCategoryEntity(linkId = "link", categoryId = "c1")
        )
        dao.assignLinkToCategory(
            com.greenrobotdev.linklibrary.database.room.LinkCategoryEntity(linkId = "link", categoryId = "c2")
        )
        val category = dao.getCategoryForLink("link")
        // REPLACE on unique linkId: last assignment wins
        assertTrue(category?.id == "c2")
    }
}
