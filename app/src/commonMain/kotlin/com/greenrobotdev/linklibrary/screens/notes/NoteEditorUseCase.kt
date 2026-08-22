package com.greenrobotdev.linklibrary.screens.notes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use Case for Note Editor business logic
 * Handles note saving, loading, and management
 */
@Composable
fun NoteEditorUseCase(
    initialState: NoteEditorState,
    events: Flow<NoteEditorEvent>,
    linkRepository: LinkRepository
): NoteEditorState {

    var state = remember { mutableStateOf(initialState) }

    // Handle events
    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is NoteEditorEvent.LoadNote -> {
                    // Load note logic
                    // For now, we start with empty note
                    state.value = state.value.copy(isLoading = false)
                }
                is NoteEditorEvent.SaveNote -> {
                    state.value = state.value.copy(isSaving = true)
                    // Save note logic here
                    kotlinx.coroutines.delay(500) // Simulate save
                    state.value = state.value.copy(isSaving = false)
                }
                is NoteEditorEvent.ClearError -> {
                    state.value = state.value.copy(error = null)
                }
                is NoteEditorEvent.TitleChanged -> {
                    state.value = state.value.copy(note = state.value.note.copy(title = event.title))
                }
                is NoteEditorEvent.ContentChanged -> {
                    state.value = state.value.copy(note = state.value.note.copy(content = event.content))
                }
                is NoteEditorEvent.AttachLink -> {
                    state.value = state.value.copy(note = state.value.note.copy(attachedLinkId = event.linkId))
                }
                is NoteEditorEvent.DetachLink -> {
                    state.value = state.value.copy(note = state.value.note.copy(attachedLinkId = null))
                }
                is NoteEditorEvent.ToggleFormat -> {
                    // Handle text formatting - UI layer manages this
                }
            }
        }
    }

    // Load available links for attaching
    LaunchedEffect(Unit) {
        linkRepository.getLinks().collect { result ->
            result.onSuccess { links ->
                state.value = state.value.copy(
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

    return state.value
}
