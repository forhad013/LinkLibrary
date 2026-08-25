package com.greenrobotdev.linklibrary.bookmarks.screens.addCollection

import androidx.compose.runtime.Composable
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

/**
 * ViewModel for Add Collection screen
 * Follows MoleculeViewModel architecture pattern
 */
class AddCollectionViewModel : MoleculeViewModel<AddCollectionEvent, AddCollectionState>(), KoinComponent {

    
    @Composable
    override fun models(events: Flow<AddCollectionEvent>): AddCollectionState {
        return AddCollectionPresenter(initialState = AddCollectionState(),events)
    }

    
    override fun initialValue(): AddCollectionState {
        return AddCollectionState()
    }
}
