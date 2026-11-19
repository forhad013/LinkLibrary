package com.greenrobotdev.lordofthelinks.bookmarks.screens.saved

import com.greenrobotdev.lordofthelinks.bookmarks.model.Category
import com.greenrobotdev.lordofthelinks.bookmarks.model.Link
import kotlin.test.Test
import kotlin.test.assertEquals

class SavedScopeTest {
    private val categories = listOf(Category("dev", "Dev"), Category("android", "Android", parentId = "dev"), Category("food", "Food"))
    private val links = listOf(
        Link(id = "new", title = "a", url = "u1", createdAt = 3, tags = listOf("Kotlin")),
        Link(id = "fav", title = "b", url = "u2", createdAt = 2, isFavorite = true, isOpened = true),
        Link(id = "favArchived", title = "c", url = "u3", createdAt = 1, isFavorite = true, isArchived = true),
        Link(id = "loose", title = "d", url = "u4", isOpened = true),
    )
    private val linkCategoryIds = mapOf("new" to "android", "fav" to "dev", "favArchived" to "food")

    private fun ids(scope: SavedScope) = savedInScope(scope, links, categories, linkCategoryIds).map { it.id }

    @Test
    fun smartListsMatchTheLibraryCounts() {
        assertEquals(listOf("new"), ids(SavedScope.Unopened))
        assertEquals(listOf("fav"), ids(SavedScope.Favorites)) // archived favorites are left out
        assertEquals(listOf("favArchived"), ids(SavedScope.Archived))
        assertEquals(listOf("loose"), ids(SavedScope.Uncategorized))
    }

    @Test
    fun categoryIncludesSubcategoriesNewestFirst() {
        assertEquals(listOf("new", "fav"), ids(SavedScope.InCategory("dev")))
        assertEquals(listOf("new"), ids(SavedScope.InCategory("android")))
    }

    @Test
    fun tagMatchIgnoresCase() {
        assertEquals(listOf("new"), ids(SavedScope.WithTag("kotlin")))
    }
}
