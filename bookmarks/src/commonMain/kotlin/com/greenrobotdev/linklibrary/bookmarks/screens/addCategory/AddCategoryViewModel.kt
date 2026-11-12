package com.greenrobotdev.linklibrary.bookmarks.screens.addCategory

import androidx.compose.runtime.Composable
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

/**
 * ViewModel for Add Category screen
 * Follows MoleculeViewModel architecture pattern
 */
class AddCategoryViewModel : MoleculeViewModel<AddCategoryEvent, AddCategoryState>(), KoinComponent {

    
    @Composable
    override fun models(events: Flow<AddCategoryEvent>): AddCategoryState {
        return AddCategoryPresenter(initialState = AddCategoryState(),events)
    }

    
    override fun initialValue(): AddCategoryState {
        return AddCategoryState()
    }
}
