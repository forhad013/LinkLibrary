package com.greenrobotdev.linklibrary.screens.collections

import androidx.compose.runtime.Composable
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

/**
 * ViewModel for Collections screen
 * Follows MoleculeViewModel architecture pattern
 */
class CollectionsViewModel : MoleculeViewModel<CollectionsEvent, CollectionsState>(), KoinComponent {

    
    @Composable
    override fun models(events: Flow<CollectionsEvent>): CollectionsState {
        return CollectionsPresenter(initialState = CollectionsState(), events)
    }

    
    override fun initialValue(): CollectionsState {
        return CollectionsState()
    }
}
