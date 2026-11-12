package com.greenrobotdev.linklibrary.bookmarks.screens.categories

import androidx.compose.runtime.Composable
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

/**
 * ViewModel for Categories screen
 * Follows MoleculeViewModel architecture pattern
 */
class CategoriesViewModel : MoleculeViewModel<CategoriesEvent, CategoriesState>(), KoinComponent {

    
    @Composable
    override fun models(events: Flow<CategoriesEvent>): CategoriesState {
        return CategoriesPresenter(initialState = CategoriesState(), events)
    }

    
    override fun initialValue(): CategoriesState {
        return CategoriesState()
    }
}
