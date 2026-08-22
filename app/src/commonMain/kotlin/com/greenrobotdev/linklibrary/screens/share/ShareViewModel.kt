package com.greenrobotdev.linklibrary.screens.share

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenrobotdev.linklibrary.data.LinkRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * ViewModel for Share Pop-up screen
 * Follows the pattern used in other ViewModels in the project
 */
class ShareViewModel(
    private val linkId: String,
    private val linkRepository: LinkRepository = inject()
) : ViewModel(), KoinComponent {

    private val _events: MutableSharedFlow<ShareEvent> = MutableSharedFlow()

    private val initialState: ShareState = ShareState(linkId = linkId)

    val states: StateFlow<ShareState> = kotlinx.coroutines.flow.MutableStateFlow(initialState)

    init {
        // Initialize with load event
        viewModelScope.launch {
            _events.emit(ShareEvent.Initialize)
        }

        // Handle events in the UseCase
        viewModelScope.launch {
            _events.collect { event ->
                // Events will be processed by the UseCase
            }
        }
    }

    fun onEvent(event: ShareEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}
