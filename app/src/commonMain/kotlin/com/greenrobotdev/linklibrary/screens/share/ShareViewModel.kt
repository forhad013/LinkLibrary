package com.greenrobotdev.linklibrary.screens.share

import androidx.compose.runtime.Composable
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * ViewModel for Share Pop-up screen
 * Follows MoleculeViewModel architecture pattern
 */
class ShareViewModel(
    private val linkId: String
) : MoleculeViewModel<ShareEvent, ShareState>(), KoinComponent {

    private val linkRepository: LinkRepository by inject()

    init {
        // Initialize with load event
        take(ShareEvent.Initialize)
    }

    
    @Composable
    override fun models(events: Flow<ShareEvent>): ShareState {
        return SharePresenter(initialState = ShareState(linkId = linkId), events, linkRepository)
    }

    
    override fun initialValue(): ShareState {
        return ShareState(linkId = linkId)
    }
}
