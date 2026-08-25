package com.greenrobotdev.linklibrary.screens.addTag

import androidx.compose.runtime.Composable
import com.greenrobotdev.linklibrary.database.repository.TagRepository
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * ViewModel for Add Tag screen
 * Follows MoleculeViewModel architecture pattern
 */
class AddTagViewModel : MoleculeViewModel<AddTagEvent, AddTagState>(), KoinComponent {

    private val tagRepository: TagRepository by inject()

    
    @Composable
    override fun models(events: Flow<AddTagEvent>): AddTagState {
        return AddTagPresenter(initialState = AddTagState(),events, tagRepository)
    }

    
    override fun initialValue(): AddTagState {
        return AddTagState()
    }
}
