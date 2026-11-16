package com.greenrobotdev.linklibrary.bookmarks.screens.tagPicker

import androidx.compose.runtime.Composable
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/** Thin shell over [TagPickerPresenter]. */
class TagPickerViewModel(
    private val multiSelect: Boolean,
    private val initialChecked: Set<String> = emptySet(),
) : MoleculeViewModel<TagPickerEvent, TagPickerState>() {

    init {
        // Keep-alive, same reason as CategoryPickerViewModel: molecule drops remember{} state with no collectors
        moleculeScope.launch { models.collect {} }
    }

    @Composable
    override fun models(events: Flow<TagPickerEvent>): TagPickerState =
        TagPickerPresenter(events = events, multiSelect = multiSelect, initialChecked = initialChecked)

    override fun initialValue(): TagPickerState = TagPickerState(checkedIds = initialChecked)
}
