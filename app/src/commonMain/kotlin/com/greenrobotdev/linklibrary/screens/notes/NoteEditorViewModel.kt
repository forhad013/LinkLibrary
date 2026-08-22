package com.greenrobotdev.linklibrary.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenrobotdev.linklibrary.data.LinkRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * ViewModel for Note Editor screen
 * Follows the pattern used in other ViewModels in the project
 */
class NoteEditorViewModel(
    private val noteId: String? = null,
    private val linkRepository: LinkRepository = inject()
) : ViewModel(), KoinComponent {

    private val _events: MutableSharedFlow<NoteEditorEvent> = MutableSharedFlow()

    private val initialState: NoteEditorState = NoteEditorState()

    val states: StateFlow<NoteEditorState> = kotlinx.coroutines.flow.MutableStateFlow(initialState)

    init {
        // Load note if noteId is provided
        if (noteId != null) {
            // TODO: Load note from repository
        }

        // Handle events in the UseCase
        viewModelScope.launch {
            _events.collect { event ->
                // Events will be processed by the UseCase
            }
        }
    }

    fun onEvent(event: NoteEditorEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}
