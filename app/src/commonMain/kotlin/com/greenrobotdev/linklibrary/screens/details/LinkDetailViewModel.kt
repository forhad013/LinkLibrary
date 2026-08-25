package com.greenrobotdev.linklibrary.screens.details

import androidx.compose.runtime.Composable
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * ViewModel for Link Detail screen
 * Follows MoleculeViewModel architecture pattern
 */
class LinkDetailViewModel(
    private val linkId: String
) : MoleculeViewModel<LinkDetailEvent, LinkDetailState>(), KoinComponent {

    private val linkRepository: LinkRepository by inject()

    
    @Composable
    override fun models(events: Flow<LinkDetailEvent>): LinkDetailState {
        return LinkDetailPresenter(initialState = LinkDetailState(), events, linkRepository, linkId)
    }

    
    override fun initialValue(): LinkDetailState {
        return LinkDetailState()
    }
}
