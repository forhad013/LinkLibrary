package com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker

import androidx.compose.runtime.Composable
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Thin ViewModel shell for the category picker; all logic lives in [CategoryPickerPresenter]
 * (thin ViewModel shell → presenter reducer).
 */
class CategoryPickerViewModel(
    private val linkId: String?,
    initialSelection: String? = null,
) : MoleculeViewModel<CategoryPickerEvent, CategoryPickerState>() {

    private val initialSelection = initialSelection

    init {
        // Keep-alive: SharingStarted.Lazily stops (and molecule discards all remember{} state,
        // incl. expandedIds) whenever models has zero collectors; a permanent self-collector
        // prevents the expanded tree collapsing on its own.
        moleculeScope.launch { models.collect {} }
    }

    @Composable
    override fun models(events: Flow<CategoryPickerEvent>): CategoryPickerState =
        CategoryPickerPresenter(
            initialState = CategoryPickerState(),
            events = events,
            linkId = linkId,
            initialSelection = initialSelection,
        )

    override fun initialValue(): CategoryPickerState = CategoryPickerState()
}
