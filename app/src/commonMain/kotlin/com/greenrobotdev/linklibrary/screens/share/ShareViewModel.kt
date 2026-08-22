package com.greenrobotdev.linklibrary.screens.share

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * ViewModel for Share Pop-up screen
 * Follows the pattern used in other ViewModels in the project
 */
class ShareViewModel(
    private val linkId: String
) : ViewModel(), KoinComponent {

    private val eventsFlow: MutableSharedFlow<ShareEvent> = MutableSharedFlow(10)
    private val linkRepository: LinkRepository by inject()
    private val initialState: ShareState = ShareState(linkId = linkId)

    val states by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            ShareUseCase(initialState, eventsFlow, linkRepository)
        }.stateIn(viewModelScope, SharingStarted.Lazily, initialState)
    }

    init {
        // Initialize with load event
        viewModelScope.launch {
            eventsFlow.emit(ShareEvent.Initialize)
        }
    }

    fun onEvent(event: ShareEvent) {
        viewModelScope.launch {
            eventsFlow.emit(event)
        }
    }
}
