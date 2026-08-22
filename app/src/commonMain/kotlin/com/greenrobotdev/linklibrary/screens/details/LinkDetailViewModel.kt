package com.greenrobotdev.linklibrary.screens.details

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
 * ViewModel for Link Detail screen
 * Follows MVVM+UseCase architecture pattern
 */
class LinkDetailViewModel(
    private val linkId: String
) : ViewModel(), KoinComponent {

    private val initialState: LinkDetailState = LinkDetailState()

    private val eventsFlow: MutableSharedFlow<LinkDetailEvent> = MutableSharedFlow(10)
    private val linkRepository: LinkRepository by inject()

    val states by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            LinkDetailUseCase(initialState, eventsFlow, linkRepository, linkId)
        }.stateIn(viewModelScope, SharingStarted.Lazily, initialState)
    }

    fun onEvent(event: LinkDetailEvent) {
        viewModelScope.launch {
            eventsFlow.emit(event)
        }
    }
}
