package com.greenrobotdev.lordofthelinks.bookmarks.backup

import com.greenrobotdev.lordofthelinks.model.Category
import com.greenrobotdev.lordofthelinks.model.Link
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LinksBackupTest {
    private val categories = listOf(Category("dev", "Dev"), Category("android", "Android", parentId = "dev"))
    private val links = listOf(
        Link(id = "1", title = "Kotlin", url = "https://kotlinlang.org", description = "note <b>&",
            isFavorite = true, isOpened = true, createdAt = 42, imageUrl = "https://k/i.png",
            fetchedDescription = "desc", type = "article", tags = listOf("kotlin", "kmp")),
        Link(id = "2", title = "Loose", url = "https://a.com", isArchived = true),
    )
    private val backup = buildBackup(links, categories, mapOf("1" to "android"), listOf("kotlin", "kmp", "unused"), nowMs = 7)

    @Test
    fun jsonRoundTripKeepsEverything() {
        assertEquals(backup, readBackup(backup.toJson()))
        val kotlin = backup.links.first()
        assertEquals("android", kotlin.categoryId)
        assertEquals(listOf("kotlin", "kmp"), kotlin.tags)
        assertEquals("https://k/i.png", kotlin.imageUrl)
        assertEquals(true, kotlin.isFavorite)
    }

    @Test
    fun foreignOrNewerFilesAreRejected() {
        assertFailsWith<BackupReadException> { readBackup("<html>bookmarks</html>") }
        assertFailsWith<BackupReadException> { readBackup("""{"version": 99, "exportedAt": 1}""") }
    }

    @Test
    fun intoAnEmptyAppEverythingIsCreated() {
        val plan = planImport(backup, emptyList(), emptyList(), emptyList())
        assertEquals(2, plan.linksToAdd.size)
        assertEquals(0, plan.skipped)
        assertEquals(listOf("dev", "android"), plan.categoriesToCreate.map { it.id }) // parent first
        assertEquals(listOf("kotlin", "kmp"), plan.tagsToCreate)
    }

    @Test
    fun importingTheSameBackupAgainAddsNothing() {
        val plan = planImport(backup, links, categories, listOf("Kotlin", "kmp"))
        assertEquals(0, plan.linksToAdd.size)
        assertEquals(2, plan.skipped)
        assertEquals(emptyList(), plan.categoriesToCreate)
        assertEquals(mapOf("dev" to "dev", "android" to "android"), plan.matchedCategories)
        assertEquals(emptyList(), plan.tagsToCreate)
    }

    @Test
    fun categoriesMatchByPathWhateverTheirIds() {
        val local = listOf(Category("x", "dev"), Category("y", "ANDROID", parentId = "x"))
        val plan = planImport(backup, emptyList(), local, emptyList())
        assertEquals(mapOf("dev" to "x", "android" to "y"), plan.matchedCategories)
        assertEquals(emptyList(), plan.categoriesToCreate)
    }

    @Test
    fun repeatsInsideTheFileAreSkipped() {
        val twice = backup.copy(links = backup.links + backup.links.first().copy(url = "http://www.kotlinlang.org/"))
        val plan = planImport(twice, emptyList(), emptyList(), emptyList())
        assertEquals(2, plan.linksToAdd.size)
        assertEquals(1, plan.skipped)
    }
}
