package com.greenrobotdev.linklibrary.screens.notes

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.FontWeight
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
) {
    companion object {
        val Saver: Saver<NoteEditorState, *> = listSaver(
            saver = Saver(
                save = { state ->
                    listOf(
                        state.isLoading,
                        state.isSaving,
                        state.note,
                        state.availableLinks,
                        state.error
                    )
                },
                restore = { list ->
                    NoteEditorState(
                        isLoading = list[0] as Boolean,
                        isSaving = list[1] as Boolean,
                        note = list[2] as Note,
                        availableLinks = list[3] as List<LinkSuggestion>,
                        error = list[4] as String?
                    )
                }
            )
        )
    }
}

/**
 * Note data model
 */
@Serializable
data class Note(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val attachedLinkId: String? = null,
    val createdAt: kotlinx.datetime.Instant = kotlinx.datetime.Clock.System.now(),
    val updatedAt: kotlinx.datetime.Instant = kotlinx.datetime.Clock.System.now()
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
    data class DetachLink : NoteEditorEvent
    data class ToggleFormat(val format: TextFormat) : NoteEditorEvent
}
