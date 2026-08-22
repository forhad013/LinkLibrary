package com.greenrobotdev.linklibrary.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * ViewModel for Note Editor screen
 * Follows the pattern used in other ViewModels in the project
 */
class NoteEditorViewModel(
    private val noteId: String? = null
) : ViewModel(), KoinComponent {

    private val eventsFlow: MutableSharedFlow<NoteEditorEvent> = MutableSharedFlow(10)
    private val linkRepository: LinkRepository by inject()
    private val initialState: NoteEditorState = NoteEditorState()

    val states by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            NoteEditorUseCase(initialState, eventsFlow, linkRepository)
        }.stateIn(viewModelScope, SharingStarted.Lazily, initialState)
    }

    fun onEvent(event: NoteEditorEvent) {
        viewModelScope.launch {
            eventsFlow.emit(event)
        }
    }
}
