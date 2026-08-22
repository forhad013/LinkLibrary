package com.greenrobotdev.linklibrary.screens.notes

import kotlinx.serialization.Serializable

/**
 * State for Note Editor screen
 */
@Serializable
data class NoteEditorState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val note: Note = Note(),
    val availableLinks: List<LinkSuggestion> = emptyList(),
    val error: String? = null
)

/**
 * Note data model
 */
@Serializable
data class Note(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val attachedLinkId: String? = null
)

/**
 * Link suggestion for attaching to notes
 */
@Serializable
data class LinkSuggestion(
    val id: String,
    val title: String,
    val url: String
)

/**
 * Text formatting styles for rich text editing
 */
@Serializable
sealed class TextFormat {
    @Serializable
    data class Bold(val isActive: Boolean) : TextFormat()

    @Serializable
    data class Italic(val isActive: Boolean) : TextFormat()

    @Serializable
    data class Underline(val isActive: Boolean) : TextFormat()

    @Serializable
    data class Highlight(val color: String) : TextFormat()

    @Serializable
    data class Heading(val level: Int) : TextFormat()
}

/**
 * Note Editor Events
 */
sealed interface NoteEditorEvent {
    object LoadNote : NoteEditorEvent
    object SaveNote : NoteEditorEvent
    object ClearError : NoteEditorEvent
    data class TitleChanged(val title: String) : NoteEditorEvent
    data class ContentChanged(val content: String) : NoteEditorEvent
    data class AttachLink(val linkId: String) : NoteEditorEvent
    object DetachLink : NoteEditorEvent
    data class ToggleFormat(val format: TextFormat) : NoteEditorEvent
}
