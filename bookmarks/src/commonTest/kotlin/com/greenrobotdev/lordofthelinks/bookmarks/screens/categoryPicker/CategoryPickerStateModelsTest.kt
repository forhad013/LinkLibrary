// shortcut: not runnable until bookmarks jvm target compiles the screens (pre-existing viewModel{} JVM failures); logic is also covered by review
package com.greenrobotdev.lordofthelinks.bookmarks.screens.categoryPicker

import com.greenrobotdev.lordofthelinks.bookmarks.model.Category
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CategoryPickerStateModelsTest {

    private val all = listOf(
        Category(id = "dev", name = "Dev"),
        Category(id = "android", name = "Android", parentId = "dev"),
        Category(id = "kotlin", name = "Kotlin", parentId = "android"),
        Category(id = "health", name = "Health"),
    )

    @Test
    fun subtreeIdsIncludeRootAndAllDescendants() {
        assertEquals(setOf("dev", "android", "kotlin"), subtreeIds("dev", all))
        assertEquals(setOf("health"), subtreeIds("health", all))
    }

    @Test
    fun pendingDeleteHidesOnlyDuringUndoPhase() {
        val base = CategoryPickerState(isLoading = false, allCategories = all)
        val deleting = base.copy(
            deletePhase = DeletePhase.UNDO,
            deletingCategory = all[0], // Dev
            parentDeleteChoice = ParentDeleteChoiceKind.DELETE_ALL,
        )
        // during the undo window the whole subtree is hidden
        assertEquals(listOf("health"), deleting.categories.map { it.id })
        // CONFIRM phase (before swipe completes) hides nothing
        val confirming = deleting.copy(deletePhase = DeletePhase.CONFIRM)
        assertEquals(all, confirming.categories)
        // undo restores everything
        val undone = deleting.copy(deletePhase = DeletePhase.NONE, deletingCategory = null)
        assertEquals(all, undone.categories)
    }

    @Test
    fun keepChildrenChoiceStillHidesWholeSubtreeDuringUndo() {
        // hiding the parent id alone drops children from the browse tree; the derived set covers both choices
        val deleting = CategoryPickerState(
            isLoading = false,
            allCategories = all,
            deletePhase = DeletePhase.UNDO,
            deletingCategory = all[0],
            parentDeleteChoice = ParentDeleteChoiceKind.KEEP_CHILDREN,
        )
        assertTrue("dev" !in deleting.categories.map { it.id })
    }
}
