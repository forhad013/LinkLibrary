package com.greenrobotdev.linklibrary.bookmarks.backup

import com.greenrobotdev.linklibrary.bookmarks.domain.isSameLink
import com.greenrobotdev.linklibrary.bookmarks.model.Category
import com.greenrobotdev.linklibrary.bookmarks.model.Link
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * A full backup of the library as JSON: every link field, the category tree, tags, and which link
 * is in which category / has which tags. [version] lets later app versions read old files.
 */
@Serializable
data class LinksBackup(
    val version: Int = CURRENT_VERSION,
    val app: String = "Lords of Links",
    val exportedAt: Long,
    val categories: List<BackupCategory> = emptyList(),
    val tags: List<String> = emptyList(),
    val links: List<BackupLink> = emptyList(),
) {
    companion object {
        const val CURRENT_VERSION = 1
    }
}

@Serializable
data class BackupCategory(val id: String, val name: String, val parentId: String? = null)

@Serializable
data class BackupLink(
    val url: String,
    val title: String = "",
    /** The user's note. */
    val description: String = "",
    val fetchedDescription: String? = null,
    val imageUrl: String? = null,
    val type: String = "other",
    val isFavorite: Boolean = false,
    val isOpened: Boolean = false,
    val isArchived: Boolean = false,
    val createdAt: Long? = null,
    val tags: List<String> = emptyList(),
    val categoryId: String? = null,
)

private val json = Json { ignoreUnknownKeys = true; prettyPrint = true; encodeDefaults = false }

fun buildBackup(
    links: List<Link>,
    categories: List<Category>,
    linkCategoryIds: Map<String, String>,
    tagNames: List<String>,
    nowMs: Long,
): LinksBackup = LinksBackup(
    exportedAt = nowMs,
    categories = categories.map { BackupCategory(it.id, it.name, it.parentId) },
    tags = tagNames.sorted(),
    links = links.map { link ->
        BackupLink(
            url = link.url, title = link.title, description = link.description,
            fetchedDescription = link.fetchedDescription, imageUrl = link.imageUrl, type = link.type,
            isFavorite = link.isFavorite, isOpened = link.isOpened, isArchived = link.isArchived,
            createdAt = link.createdAt, tags = link.tags, categoryId = linkCategoryIds[link.id],
        )
    },
)

fun LinksBackup.toJson(): String = json.encodeToString(LinksBackup.serializer(), this)

/** Thrown for a file that isn't a backup this version can read; the message is shown to the user. */
class BackupReadException(message: String) : Exception(message)

/** Reads a backup file. Untrusted input: anything that isn't a backup we understand is rejected. */
fun readBackup(text: String): LinksBackup {
    val backup = runCatching { json.decodeFromString(LinksBackup.serializer(), text) }
        .getOrElse { throw BackupReadException("This file isn't a Lords of Links backup.") }
    if (backup.version > LinksBackup.CURRENT_VERSION) {
        throw BackupReadException("This backup comes from a newer version of the app. Update the app, then try again.")
    }
    return backup
}

/** What a restore will do, worked out before anything is written. */
data class ImportPlan(
    /** Categories to create, parents before children; parentKey is the backup id of the parent. */
    val categoriesToCreate: List<BackupCategory>,
    /** Backup category id → existing local category id (matched by path). */
    val matchedCategories: Map<String, String>,
    /** Tag names to create (not present locally, ignoring case). */
    val tagsToCreate: List<String>,
    val linksToAdd: List<BackupLink>,
    /** Links skipped because the same link is already saved (or repeats within the file). */
    val skipped: Int,
)

/**
 * Merges [backup] into what's saved: links already saved (same link, see isSameLink) are skipped,
 * categories are matched by their path ("Dev › Android", ignoring case) and missing ones created
 * with the same tree, tags are matched by name. Nothing saved is changed or removed.
 */
fun planImport(
    backup: LinksBackup,
    existingLinks: List<Link>,
    existingCategories: List<Category>,
    existingTags: List<String>,
): ImportPlan {
    val byId = backup.categories.associateBy { it.id }
    fun backupPath(c: BackupCategory): String {
        val names = mutableListOf(c.name)
        var parent = c.parentId?.let(byId::get)
        val seen = mutableSetOf(c.id)
        while (parent != null && seen.add(parent.id)) { // a broken file could loop
            names.add(0, parent.name)
            parent = parent.parentId?.let(byId::get)
        }
        return names.joinToString(" › ").lowercase()
    }
    val localByPath = existingCategories.associateBy { it.path(existingCategories).lowercase() }

    val matched = mutableMapOf<String, String>()
    val toCreate = mutableListOf<BackupCategory>()
    // Parents first: a category is placed once its parent is matched or created
    val pending = backup.categories.toMutableList()
    while (pending.isNotEmpty()) {
        val ready = pending.filter { c -> c.parentId == null || c.parentId !in byId || c.parentId in matched || toCreate.any { it.id == c.parentId } }
        if (ready.isEmpty()) break // a cycle in the file: leave those out
        ready.forEach { c ->
            val local = localByPath[backupPath(c)]
            if (local != null) matched[c.id] = local.id
            else toCreate += c.copy(parentId = c.parentId?.takeIf { it in byId })
        }
        pending.removeAll(ready)
    }

    val kept = mutableListOf<BackupLink>()
    var skipped = 0
    backup.links.filter { it.url.isNotBlank() }.forEach { link ->
        val already = existingLinks.any { isSameLink(it.url, link.url) } || kept.any { isSameLink(it.url, link.url) }
        if (already) skipped++ else kept += link
    }
    skipped += backup.links.count { it.url.isBlank() }

    val have = existingTags.map { it.lowercase() }.toSet()
    val tagsToCreate = kept.flatMap { it.tags }.filter { it.isNotBlank() }
        .distinctBy { it.lowercase() }.filter { it.lowercase() !in have }

    return ImportPlan(toCreate, matched, tagsToCreate, kept, skipped)
}
