// shortcut: not runnable until bookmarks jvm() target matches core/utils jvm("desktop") — run after GRE-42 follow-up fixes target naming
package com.greenrobotdev.linklibrary.bookmarks

import com.greenrobotdev.linklibrary.bookmarks.model.Category
import kotlin.test.Test
import kotlin.test.assertEquals

class CategoryPathTest {

    private val all = listOf(
        Category(id = "1", name = "Health"),
        Category(id = "2", name = "Sleep", parentId = "1"),
        Category(id = "3", name = "Deep", parentId = "2"),
    )

    @Test
    fun rootPathIsOwnName() {
        assertEquals("Health", all[0].path(all))
    }

    @Test
    fun nestedPathUsesBreadcrumbs() {
        assertEquals("Health › Sleep › Deep", all[2].path(all))
    }
}
