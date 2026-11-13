package com.greenrobotdev.linklibrary.bookmarks.screens.add.sheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow

class AddLinkSheetViewModel(
    private val clipboardUrl: String?,
    private val initialUrl: String? = null,
) : MoleculeViewModel<AddLinkSheetEvent, AddLinkSheetState>() {

    @Composable
    override fun models(events: Flow<AddLinkSheetEvent>): AddLinkSheetState {
        var state by remember {
            mutableStateOf(AddLinkSheetState(url = initialUrl.orEmpty(), clipboardUrl = clipboardUrl))
        }
        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    is AddLinkSheetEvent.UrlChanged -> state = state.copy(url = event.url)
                }
            }
        }
        return state
    }

    override fun initialValue(): AddLinkSheetState =
        AddLinkSheetState(url = initialUrl.orEmpty(), clipboardUrl = clipboardUrl)
}
