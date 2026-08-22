package com.greenrobotdev.linklibrary.screens.notes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import app.cash.molecule.moleculeFlow
import app.cash.molecule.RecompositionMode
import com.greenrobotdev.linklibrary.data.LinkRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

/**
 * Use Case for Note Editor business logic
 * Handles note saving, loading, and management
 */
@Composable
fun NoteEditorUseCase(
    initialState: NoteEditorState,
    events: kotlinx.coroutines.flow.SharedFlow<NoteEditorEvent>,
    linkRepository: com.greenrobotdev.linklibrary.data.LinkRepository
): StateFlow<NoteEditorState> {

    // Collect state as Compose State
    var state by remember { mutableStateOf(initialState) }

    // Handle events
    kotlinx.coroutines.flow.LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is NoteEditorEvent.LoadNote -> {
                    // Load note logic
                    // For now, we start with empty note
                    state = state.copy(isLoading = false)
                }
                is NoteEditorEvent.SaveNote -> {
                    state = state.copy(isSaving = true)
                    // Save note logic here
                    kotlinx.coroutines.delay(500) // Simulate save
                    state = state.copy(isSaving = false)
                }
                is NoteEditorEvent.ClearError -> {
                    state = state.copy(error = null)
                }
                is NoteEditorEvent.TitleChanged -> { title ->
                    state = state.copy(note = state.note.copy(title = title))
                }
                is NoteEditorEvent.ContentChanged -> { content ->
                    state = state.copy(note = state.note.copy(content = content))
                }
                is NoteEditorEvent.AttachLink -> { linkId ->
                    // Attach link to note
                    state = state.copy(note = state.note.copy(attachedLinkId = linkId))
                }
                is NoteEditorEvent.DetachLink -> {
                    state = state.copy(note = state.note.copy(attachedLinkId = null))
                }
                is NoteEditorEvent.ToggleFormat -> { format ->
                    // Handle text formatting - UI layer manages this
                }
            }
        }
    }

    // Load available links for attaching
    kotlinx.coroutines.flow.LaunchedEffect(Unit) {
        linkRepository.getLinks().collect { result ->
            result.onSuccess { links ->
                state = state.copy(
                    availableLinks = links.map { link ->
                        LinkSuggestion(
                            id = link.id,
                            title = link.title,
                            url = link.url.toString()
                        )
                    }
                )
            }
        }
    }

    return moleculeFlow(RecompositionMode.Immediate) {
        state
    }
}
