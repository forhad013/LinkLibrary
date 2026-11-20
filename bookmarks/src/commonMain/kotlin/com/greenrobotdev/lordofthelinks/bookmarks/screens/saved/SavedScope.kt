package com.greenrobotdev.lordofthelinks.bookmarks.screens.saved

import com.greenrobotdev.lordofthelinks.model.Category
import com.greenrobotdev.lordofthelinks.model.Link
import com.greenrobotdev.lordofthelinks.bookmarks.screens.categoryPicker.subtreeIds

/** Which saved links a list shows: one of the Library's smart lists, a category or a tag. */
sealed interface SavedScope {
    /** Stable id: shared-transition key and ViewModel key. */
    val key: String

    data object Unopened : SavedScope { override val key = "unopened" }
    data object Favorites : SavedScope { override val key = "favorites" }
    data object Archived : SavedScope { override val key = "archived" }
    data object Uncategorized : SavedScope { override val key = "uncategorized" }
    /** The category and everything inside its subcategories. */
    data class InCategory(val categoryId: String) : SavedScope { override val key = "category-$categoryId" }
    data class WithTag(val tagName: String) : SavedScope { override val key = "tag-$tagName" }
}

/**
 * The links [scope] shows, newest first. Matches the Library tile counts: Unopened and Favorites
 * leave archived links out; Archived and Uncategorized (and categories) include them.
 */
fun savedInScope(
    scope: SavedScope,
    links: List<Link>,
    categories: List<Category>,
    linkCategoryIds: Map<String, String>,
): List<Link> {
    val keep: (Link) -> Boolean = when (scope) {
        SavedScope.Unopened -> { link -> !link.isOpened && !link.isArchived }
        SavedScope.Favorites -> { link -> link.isFavorite && !link.isArchived }
        SavedScope.Archived -> { link -> link.isArchived }
        SavedScope.Uncategorized -> { link -> linkCategoryIds[link.id] == null }
        is SavedScope.InCategory -> {
            val ids = subtreeIds(scope.categoryId, categories)
            ({ link -> linkCategoryIds[link.id] in ids })
        }
        is SavedScope.WithTag -> { link -> link.tags.any { it.equals(scope.tagName, ignoreCase = true) } }
    }
    return links.filter(keep).sortedByDescending { it.createdAt ?: Long.MIN_VALUE }
}

/** Header title for [scope]. */
fun SavedScope.title(categories: List<Category>): String = when (this) {
    SavedScope.Unopened -> "Unopened"
    SavedScope.Favorites -> "Favorites"
    SavedScope.Archived -> "Archived"
    SavedScope.Uncategorized -> "Uncategorized"
    is SavedScope.InCategory -> categories.firstOrNull { it.id == categoryId }?.name ?: "Category"
    is SavedScope.WithTag -> "#$tagName"
}

/** What an empty list says, and how to fill it. */
fun SavedScope.emptyMessage(): Pair<String, String> = when (this) {
    SavedScope.Unopened -> "All caught up" to "You've opened everything you saved."
    SavedScope.Favorites -> "No favorites yet" to "Tap the star on a link's details to keep it here."
    SavedScope.Archived -> "Nothing archived" to "Swipe a link right in the Feed to archive it."
    SavedScope.Uncategorized -> "Everything is sorted" to "Every link has a category."
    is SavedScope.InCategory -> "Nothing here yet" to "Links you put in this category show up here."
    is SavedScope.WithTag -> "Nothing tagged yet" to "Links with this tag show up here."
}
