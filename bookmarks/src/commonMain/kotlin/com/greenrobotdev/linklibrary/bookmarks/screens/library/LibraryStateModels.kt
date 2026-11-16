package com.greenrobotdev.linklibrary.bookmarks.screens.library

import com.greenrobotdev.linklibrary.bookmarks.model.Category
import com.greenrobotdev.linklibrary.bookmarks.model.Link
import com.greenrobotdev.linklibrary.bookmarks.model.Tag
import com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker.subtreeIds

/** Feed quick filters (the chip row under search). */
enum class FeedFilter(val label: String) {
    ALL("All"), UNOPENED("Unopened"), ARTICLES("Articles"), VIDEOS("Videos"), SOCIAL("Social"), FAVORITES("Favorites")
}

data class LibraryState(
    val isLoading: Boolean = true,
    val links: List<Link> = emptyList(),
    val categories: List<Category> = emptyList(),
    val tags: List<Tag> = emptyList(),
    /** linkId → categoryId */
    val linkCategoryIds: Map<String, String> = emptyMap(),
    val searchQuery: String = "",
    val filter: FeedFilter = FeedFilter.ALL,
    /** Category filter; includes everything inside it. */
    val categoryId: String? = null,
    /** Tag filter by id; a link matches if it has any of them. */
    val tagIds: Set<String> = emptySet(),
    val showArchived: Boolean = false,
    val newestFirst: Boolean = true,
) {
    val filteredLinks: List<Link> get() = filterFeed(this)
    val category: Category? get() = categories.firstOrNull { it.id == categoryId }
    fun categoryOf(link: Link): Category? = linkCategoryIds[link.id]?.let { id -> categories.firstOrNull { it.id == id } }
}

/** Search + quick filter + category subtree + tags + archived, then sort by createdAt. */
fun filterFeed(state: LibraryState): List<Link> {
    val q = state.searchQuery.trim()
    val inCategory = state.categoryId?.let { subtreeIds(it, state.categories) }
    val tagNames = state.tags.filter { it.id in state.tagIds }.map { it.name.lowercase() }.toSet()
    return state.links.filter { link ->
        (state.showArchived || !link.isArchived) &&
            when (state.filter) {
                FeedFilter.ALL -> true
                FeedFilter.UNOPENED -> !link.isOpened
                FeedFilter.ARTICLES -> link.type == "article"
                FeedFilter.VIDEOS -> link.type == "video"
                FeedFilter.SOCIAL -> link.type == "social"
                FeedFilter.FAVORITES -> link.isFavorite
            } &&
            (inCategory == null || state.linkCategoryIds[link.id] in inCategory) &&
            (tagNames.isEmpty() || link.tags.any { it.lowercase() in tagNames }) &&
            (q.isEmpty() || listOfNotNull(
                link.title, link.url, link.description, link.fetchedDescription, state.categoryOf(link)?.name,
            ).any { it.contains(q, ignoreCase = true) } || link.tags.any { it.contains(q.removePrefix("#"), ignoreCase = true) })
    }.let { list ->
        // null createdAt (links saved before it was stamped) sorts as oldest
        if (state.newestFirst) list.sortedByDescending { it.createdAt ?: 0L } else list.sortedBy { it.createdAt ?: 0L }
    }
}

sealed interface LibraryEvent {
    data class SearchChanged(val query: String) : LibraryEvent
    data class SetFilter(val filter: FeedFilter) : LibraryEvent
    data class SetCategory(val categoryId: String?) : LibraryEvent
    data class SetTags(val tagIds: Set<String>) : LibraryEvent
    object ToggleArchived : LibraryEvent
    object ToggleSort : LibraryEvent
    /** Card tapped: the link is about to open in the browser. */
    data class Opened(val linkId: String) : LibraryEvent
}
