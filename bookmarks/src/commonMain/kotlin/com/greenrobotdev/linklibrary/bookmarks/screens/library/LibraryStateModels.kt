package com.greenrobotdev.linklibrary.bookmarks.screens.library

import com.greenrobotdev.linklibrary.bookmarks.model.Category
import com.greenrobotdev.linklibrary.bookmarks.model.Link
import com.greenrobotdev.linklibrary.bookmarks.model.Tag
import com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker.subtreeIds
import kotlinx.datetime.DatePeriod
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/** ARCHIVED = only archived links, opened or not. */
@Serializable
enum class OpenedFilter(val label: String) { ALL("All"), UNOPENED("Unopened"), OPENED("Opened"), ARCHIVED("Archived") }

/** Link kinds the Type section offers; NO_PREVIEW = no title fetched yet. */
@Serializable
enum class LinkKind(val label: String) { ARTICLE("Articles"), VIDEO("Videos"), SOCIAL("Social"), NO_PREVIEW("No preview") }

@Serializable
enum class SavedRange(val label: String) { ANY("Any time"), TODAY("Today"), THIS_WEEK("This week"), THIS_MONTH("This month") }

/** Everything the Filters sheet sets. Default = no filtering (archived hidden). */
@Serializable
data class FeedFilters(
    val opened: OpenedFilter = OpenedFilter.ALL,
    val favoritesOnly: Boolean = false,
    val kinds: Set<LinkKind> = emptySet(),
    /** Any of these categories, each including everything inside it. */
    val categoryIds: Set<String> = emptySet(),
    val tagIds: Set<String> = emptySet(),
    /** false: a link needs any of [tagIds]; true: all of them. */
    val tagMatchAll: Boolean = false,
    val saved: SavedRange = SavedRange.ANY,
    val includeArchived: Boolean = false,
) {
    /** For the Filters button badge: one per section that is set. */
    val activeCount: Int
        get() = listOf(
            opened != OpenedFilter.ALL, favoritesOnly, kinds.isNotEmpty(), categoryIds.isNotEmpty(),
            tagIds.isNotEmpty(), saved != SavedRange.ANY, includeArchived && opened != OpenedFilter.ARCHIVED,
        ).count { it }
}

data class LibraryState(
    val isLoading: Boolean = true,
    val links: List<Link> = emptyList(),
    val categories: List<Category> = emptyList(),
    val tags: List<Tag> = emptyList(),
    /** linkId → categoryId */
    val linkCategoryIds: Map<String, String> = emptyMap(),
    val searchQuery: String = "",
    val filters: FeedFilters = FeedFilters(),
    val newestFirst: Boolean = true,
) {
    val filteredLinks: List<Link> get() = filterFeed(this, filters)
    fun categoryOf(link: Link): Category? = linkCategoryIds[link.id]?.let { id -> categories.firstOrNull { it.id == id } }
}

fun Link.kind(): LinkKind = when {
    title.isBlank() || title == url -> LinkKind.NO_PREVIEW
    type == "video" -> LinkKind.VIDEO
    type == "social" -> LinkKind.SOCIAL
    else -> LinkKind.ARTICLE
}

/** Start of the range in epoch ms (device time zone), or null for Any time. */
@OptIn(ExperimentalTime::class)
fun SavedRange.startMs(nowMs: Long, zone: TimeZone = TimeZone.currentSystemDefault()): Long? {
    val today = Instant.fromEpochMilliseconds(nowMs).toLocalDateTime(zone).date
    val start: LocalDate = when (this) {
        SavedRange.ANY -> return null
        SavedRange.TODAY -> today
        SavedRange.THIS_WEEK -> today.minus(DatePeriod(days = today.dayOfWeek.isoDayNumber - 1)) // Monday
        SavedRange.THIS_MONTH -> LocalDate(today.year, today.month, 1)
    }
    return start.atStartOfDayIn(zone).toEpochMilliseconds()
}

/** Search + [filters], then sorted. Used for the Feed and for the sheet's live "Show N saves" count. */
@OptIn(ExperimentalTime::class)
fun filterFeed(
    state: LibraryState,
    filters: FeedFilters,
    nowMs: Long = kotlin.time.Clock.System.now().toEpochMilliseconds(),
): List<Link> {
    val q = state.searchQuery.trim()
    val inCategories: Set<String>? = filters.categoryIds.takeIf { it.isNotEmpty() }
        ?.flatMap { subtreeIds(it, state.categories) }?.toSet()
    val tagNames = state.tags.filter { it.id in filters.tagIds }.map { it.name.lowercase() }.toSet()
    val since = filters.saved.startMs(nowMs)
    return state.links.filter { link ->
        val linkTags = link.tags.map { it.lowercase() }
        when (filters.opened) {
            OpenedFilter.ALL -> filters.includeArchived || !link.isArchived
            OpenedFilter.UNOPENED -> !link.isOpened && (filters.includeArchived || !link.isArchived)
            OpenedFilter.OPENED -> link.isOpened && (filters.includeArchived || !link.isArchived)
            OpenedFilter.ARCHIVED -> link.isArchived
        } &&
            (!filters.favoritesOnly || link.isFavorite) &&
            (filters.kinds.isEmpty() || link.kind() in filters.kinds) &&
            (inCategories == null || state.linkCategoryIds[link.id] in inCategories) &&
            (tagNames.isEmpty() || if (filters.tagMatchAll) linkTags.containsAll(tagNames) else linkTags.any { it in tagNames }) &&
            // undated links (saved before createdAt existed) never match a date range
            (since == null || (link.createdAt ?: Long.MIN_VALUE) >= since) &&
            (q.isEmpty() || listOfNotNull(
                link.title, link.url, link.description, link.fetchedDescription, state.categoryOf(link)?.name,
            ).any { it.contains(q, ignoreCase = true) } || link.tags.any { it.contains(q.removePrefix("#"), ignoreCase = true) })
    }.let { list ->
        // Repo order is newest first (createdAt, then insertion order for undated links); the stable
        // sort keeps that tie-break, and Oldest is its exact reverse so undated links flip too.
        val newest = list.sortedByDescending { it.createdAt ?: Long.MIN_VALUE }
        if (state.newestFirst) newest else newest.asReversed()
    }
}

sealed interface LibraryEvent {
    data class SearchChanged(val query: String) : LibraryEvent
    /** Filters sheet "Show N saves". */
    data class ApplyFilters(val filters: FeedFilters) : LibraryEvent
    data class RemoveCategory(val categoryId: String) : LibraryEvent
    data class RemoveTag(val tagId: String) : LibraryEvent
    /** Reset search and every filter. */
    object ClearFilters : LibraryEvent
    object ToggleSort : LibraryEvent
    /** Card tapped: the link is about to open in the browser. */
    data class Opened(val linkId: String) : LibraryEvent
}
