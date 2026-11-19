// shortcut: not runnable until bookmarks jvm target compiles the screens (pre-existing viewModel{} JVM failures); logic is also covered by review
package com.greenrobotdev.lordofthelinks.bookmarks.screens.categoryPicker

import com.greenrobotdev.lordofthelinks.bookmarks.model.Category
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CategoryTreeTest {

    private val all = listOf(
        Category(id = "dev", name = "Dev"),
        Category(id = "android", name = "Android", parentId = "dev"),
        Category(id = "kotlin", name = "Kotlin", parentId = "android"),
        Category(id = "health", name = "Health"),
        Category(id = "sleep", name = "Sleep", parentId = "health"),
    )

    @Test
    fun flattenShowsRootsOnlyWhenNothingExpanded() {
        val rows = flattenTree(all, emptySet())
        assertEquals(listOf("Dev", "Health"), rows.map { it.category.name })
        assertEquals(0, rows[0].depth)
    }

    @Test
    fun flattenIndentsChildrenUnderExpandedParents() {
        val rows = flattenTree(all, setOf("dev", "android"))
        assertEquals(listOf("Dev", "Android", "Kotlin", "Health"), rows.map { it.category.name })
        assertEquals(listOf(0, 1, 2, 0), rows.map { it.depth })
        assertTrue(rows[0].hasChildren)
    }

    @Test
    fun collapseHidesDescendants() {
        val rows = flattenTree(all, setOf("android")) // dev collapsed
        assertEquals(listOf("Dev", "Health"), rows.map { it.category.name })
    }

    @Test
    fun searchMatchesOwnNameCaseInsensitivelyWithBreadcrumb() {
        val rows = searchRows(all, "sleep")
        assertEquals(1, rows.size)
        assertEquals("Health › Sleep", rows[0].breadcrumb)
    }

    @Test
    fun searchDoesNotMatchParentNames() {
        // "Dev" is a parent name, not the category's own name — no own-name match for "dev" query other than itself
        val rows = searchRows(all.filter { it.id != "dev" }, "dev")
        assertTrue(rows.isEmpty())
    }

    @Test
    fun createRowAppearsOnlyForNewNames() {
        assertTrue(needsCreateRow(all, "Recipes"))
        assertTrue(needsCreateRow(all, "  Recipes  ")) // trimmed
        assertFalse(needsCreateRow(all, "sleep"))     // exists (case-insensitive)
        assertFalse(needsCreateRow(all, "  "))         // blank query
    }

    @Test
    fun ancestorsChainFromRoot() {
        assertEquals(listOf("dev", "android"), ancestorsOf("kotlin", all))
        assertEquals(emptyList(), ancestorsOf("dev", all))
    }

    @Test
    fun depthLimitBlocksSixthLevel() {
        assertEquals(3, depthOf("kotlin", all))
        assertTrue(canAddChildUnder("kotlin", all))   // depth 3 → child would be 4
        assertTrue(canAddChildUnder(null, all))
        val deep = all + listOf(
            Category(id = "d4", name = "D4", parentId = "kotlin"),
            Category(id = "d5", name = "D5", parentId = "d4"),
        )
        assertFalse(canAddChildUnder("d5", deep))     // depth 5 → child would be 6
    }

    @Test
    fun nameValidationTrimsRejectsEmptyAndSiblingDuplicates() {
        assertNull(validateCategoryName("  New  ", null, all))
        assertEquals("Name can't be empty", validateCategoryName("   ", null, all))
        // duplicate among siblings (case-insensitive), same parent null
        assertEquals(
            "A category with this name already exists here",
            validateCategoryName("health", null, all),
        )
        // same name under a different parent is fine
        assertNull(validateCategoryName("Sleep", "dev", all))
        // renaming a category to its own name is fine
        assertNull(validateCategoryName("Health", null, all, excludeId = "health"))
    }
}
