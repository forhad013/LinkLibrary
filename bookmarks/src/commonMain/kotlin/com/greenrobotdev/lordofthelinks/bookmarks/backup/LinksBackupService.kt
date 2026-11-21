package com.greenrobotdev.lordofthelinks.bookmarks.backup

import com.greenrobotdev.lordofthelinks.bookmarks.export.bookmarksHtml
import com.greenrobotdev.lordofthelinks.model.repository.CategoryRepository
import com.greenrobotdev.lordofthelinks.model.repository.LinkRepository
import com.greenrobotdev.lordofthelinks.model.repository.TagRepository
import com.greenrobotdev.lordofthelinks.model.Category
import com.greenrobotdev.lordofthelinks.model.Link
import com.greenrobotdev.lordofthelinks.model.Tag
import kotlinx.coroutines.flow.first

/** A file ready to save, and how many links it holds. */
class ExportedFile(val fileName: String, val mimeType: String, val content: String, val linkCount: Int)

/** What a restore did. */
data class RestoreResult(val added: Int, val skipped: Int, val categoriesCreated: Int, val tagsCreated: Int)

/** Backup (JSON), restore and browser export, against the app's database. */
class LinksBackupService(
    private val linkRepository: LinkRepository,
    private val categoryRepository: CategoryRepository,
    private val tagRepository: TagRepository,
) {
    private class Library(
        val links: List<Link>,
        val categories: List<Category>,
        val linkCategoryIds: Map<String, String>,
        val tags: List<Tag>,
    )

    private suspend fun library() = Library(
        links = linkRepository.getLinksWithTags().first().getOrThrow(),
        categories = categoryRepository.getCategories().first().getOrThrow(),
        linkCategoryIds = categoryRepository.getLinkCategoryIds().first().getOrThrow(),
        tags = tagRepository.getTags().first().getOrThrow(),
    )

    /** Everything, as a backup this app can restore exactly. */
    suspend fun backup(nowMs: Long): ExportedFile {
        val lib = library()
        val json = buildBackup(lib.links, lib.categories, lib.linkCategoryIds, lib.tags.map { it.name }, nowMs).toJson()
        return ExportedFile("lords-of-links-backup.json", "application/json", json, lib.links.size)
    }

    /** A bookmarks file for browsers and other apps (not for restoring here). */
    suspend fun browserExport(): ExportedFile {
        val lib = library()
        return ExportedFile("lords-of-links-bookmarks.html", "text/html", bookmarksHtml(lib.links, lib.categories, lib.linkCategoryIds), lib.links.size)
    }

    /**
     * Merges a backup file into the library (see [planImport]): adds what's missing, never changes
     * or removes what's saved. Throws [BackupReadException] for a file it can't use.
     */
    suspend fun restore(text: String): RestoreResult {
        val backup = readBackup(text)
        val lib = library()
        val plan = planImport(backup, lib.links, lib.categories, lib.tags.map { it.name })

        // Categories, parents first; a backup id now maps to a local id
        val categoryIds = plan.matchedCategories.toMutableMap()
        var categoriesCreated = 0
        plan.categoriesToCreate.forEach { c ->
            val parent = c.parentId?.let(categoryIds::get)
            categoryRepository.addCategory(Category(id = "", name = c.name, parentId = parent)).first()
                .onSuccess { categoryIds[c.id] = it.id; categoriesCreated++ }
            // A category the tree can't take (too deep) is left out; its links stay uncategorized
        }

        val tagIds = lib.tags.associate { it.name.lowercase() to it.id }.toMutableMap()
        var tagsCreated = 0
        plan.tagsToCreate.forEach { name ->
            tagRepository.addTag(Tag(id = "", name = name, count = 0)).first()
                .onSuccess { tagIds[name.lowercase()] = it.id; tagsCreated++ }
        }

        var added = 0
        plan.linksToAdd.forEach { link ->
            val saved = linkRepository.addLink(
                Link(
                    id = "", title = link.title, url = link.url, description = link.description,
                    isFavorite = link.isFavorite, isOpened = link.isOpened, isArchived = link.isArchived,
                    type = link.type, fetchedDescription = link.fetchedDescription,
                    // 0 = date unknown (the app shows no time and files it under Earlier); null would
                    // let addLink stamp "now", moving an old undated link to the top of Today
                    createdAt = link.createdAt ?: 0L, imageUrl = link.imageUrl,
                ),
            ).first().getOrNull() ?: return@forEach
            added++
            link.categoryId?.let(categoryIds::get)?.let { categoryRepository.assignLinkToCategory(saved.id, it).first() }
            link.tags.mapNotNull { tagIds[it.lowercase()] }.distinct().forEach { tagRepository.assignTagToLink(saved.id, it).first() }
        }
        return RestoreResult(added, plan.skipped, categoriesCreated, tagsCreated)
    }
}
