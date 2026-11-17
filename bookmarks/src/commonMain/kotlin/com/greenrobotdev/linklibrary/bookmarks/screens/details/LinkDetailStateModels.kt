package com.greenrobotdev.linklibrary.bookmarks.screens.details

import com.greenrobotdev.linklibrary.bookmarks.model.Category
import com.greenrobotdev.linklibrary.bookmarks.model.Link
import com.greenrobotdev.linklibrary.bookmarks.model.Tag

/** Link details (design: Link details). Every edit writes straight to the DB; there is no Save. */
data class LinkDetailState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val link: Link? = null,
    val categories: List<Category> = emptyList(),
    val categoryId: String? = null,
    /** Tags on this link (with ids, for the tag picker). */
    val linkTags: List<Tag> = emptyList(),
    /** True while a note edit waits to be written; the card shows "Saving…". */
    val noteSaving: Boolean = false,
    val isDeleted: Boolean = false,
) {
    val category: Category? get() = categories.firstOrNull { it.id == categoryId }
}

sealed interface LinkDetailEvent {
    object ToggleFavorite : LinkDetailEvent
    object ToggleArchived : LinkDetailEvent
    object ToggleOpened : LinkDetailEvent
    /** Open link tapped: also marks it opened. */
    object MarkOpened : LinkDetailEvent
    /** video | article | social | other */
    data class SetType(val type: String) : LinkDetailEvent
    data class Rename(val title: String) : LinkDetailEvent
    data class SetCategory(val categoryId: String?) : LinkDetailEvent
    data class SetTags(val tagIds: Set<String>) : LinkDetailEvent
    /** Typing in the note; written after a short pause. */
    data class NoteChanged(val note: String) : LinkDetailEvent
    object Delete : LinkDetailEvent
    object ClearError : LinkDetailEvent
}
