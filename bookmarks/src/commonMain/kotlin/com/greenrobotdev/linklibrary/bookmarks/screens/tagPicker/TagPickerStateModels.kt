package com.greenrobotdev.linklibrary.bookmarks.screens.tagPicker

import com.greenrobotdev.linklibrary.bookmarks.model.Tag
import com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker.DeletePhase

/**
 * Tag picker state: a flat list with search, create, rename and delete (with undo).
 * In multi-select mode tapping a row toggles its check mark; [focusedId] is the last tapped
 * row and is what Rename / Remove act on. In manage mode a tap only focuses.
 */
data class TagPickerState(
    val isLoading: Boolean = true,
    val allTags: List<Tag> = emptyList(),
    val checkedIds: Set<String> = emptySet(),
    val focusedId: String? = null,
    val query: String = "",
    val creating: Boolean = false,
    val renamingId: String? = null,
    val error: String? = null,
    /** "Done" tapped — the host reads [checkedIds] and closes. */
    val applied: Boolean = false,
    val deletePhase: DeletePhase = DeletePhase.NONE,
    val deletingTag: Tag? = null,
) {
    /** Rows shown: everything but a tag hidden while its delete waits out the undo window. */
    val tags: List<Tag>
        get() = if (deletePhase == DeletePhase.UNDO && deletingTag != null) allTags.filter { it.id != deletingTag.id }
        else allTags

    val focused: Tag? get() = tags.firstOrNull { it.id == focusedId }
    val isEditing: Boolean get() = creating || renamingId != null

    /** Rows matching the search, or all rows when not searching. */
    val visibleTags: List<Tag>
        get() = if (query.isBlank()) tags else tags.filter { it.name.contains(query.trim(), ignoreCase = true) }
}

sealed interface TagPickerEvent {
    data class Tap(val tagId: String) : TagPickerEvent
    data class Search(val query: String) : TagPickerEvent
    /** Create "<query>" from the search row. */
    object CreateFromSearch : TagPickerEvent
    /** "+ New tag" — opens the inline name field. */
    object StartCreate : TagPickerEvent
    object StartRename : TagPickerEvent
    /** Submit the inline field (create or rename). */
    data class SubmitName(val name: String) : TagPickerEvent
    object CancelEdit : TagPickerEvent
    object Remove : TagPickerEvent
    object SwipeComplete : TagPickerEvent
    /** Undo bar or the swipe bar's cancel: drop the pending delete. */
    object UndoDelete : TagPickerEvent
    object Done : TagPickerEvent
    /** Select-only "Clear": untick every tag. */
    object ClearChecked : TagPickerEvent
    object ClearError : TagPickerEvent
}

/** null = valid; otherwise the message to show. Names are unique ignoring case. */
fun validateTagName(name: String, tags: List<Tag>, excludeId: String? = null): String? {
    val trimmed = name.trim()
    if (trimmed.isEmpty()) return "Name can't be empty"
    if (tags.any { it.id != excludeId && it.name.equals(trimmed, ignoreCase = true) }) return "A tag with this name already exists"
    return null
}
