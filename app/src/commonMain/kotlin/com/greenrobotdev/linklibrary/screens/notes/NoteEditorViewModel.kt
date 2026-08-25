package com.greenrobotdev.linklibrary.screens.notes

import androidx.compose.runtime.Composable
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * ViewModel for Note Editor screen
 * Follows MoleculeViewModel architecture pattern
 */
class NoteEditorViewModel(
    private val noteId: String? = null
) : MoleculeViewModel<NoteEditorEvent, NoteEditorState>(), KoinComponent {

    private val linkRepository: LinkRepository by inject()

    
    @Composable
    override fun models(events: Flow<NoteEditorEvent>): NoteEditorState {
        return NoteEditorPresenter(initialState = NoteEditorState(), events, linkRepository)
    }

    
    override fun initialValue(): NoteEditorState {
        return NoteEditorState()
    }
}
