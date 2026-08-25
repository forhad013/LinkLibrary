package com.greenrobotdev.linklibrary.screens.tags

import androidx.compose.runtime.Composable
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

/**
 * ViewModel for Tags screen
 * Follows MoleculeViewModel architecture pattern
 */
class TagsViewModel : MoleculeViewModel<TagsEvent, TagsState>(), KoinComponent {

    
    @Composable
    override fun models(events: Flow<TagsEvent>): TagsState {
        return TagsPresenter(initialState = TagsState(), events)
    }

    
    override fun initialValue(): TagsState {
        return TagsState()
    }
}
