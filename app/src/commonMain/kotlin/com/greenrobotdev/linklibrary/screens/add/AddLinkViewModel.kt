package com.greenrobotdev.linklibrary.screens.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import com.greenrobotdev.linklibrary.domain.repository.LinkRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddLinkViewModel(
    private val initialUrl: String? = null
) : ViewModel(), KoinComponent {

    private val initialState: AddLinkState = AddLinkState()

    private val eventsFlow: MutableSharedFlow<AddLinkEvent> = MutableSharedFlow(10)
    private val linkRepository: LinkRepository by inject()

    val states by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            AddLinkUseCase(initialState, eventsFlow, linkRepository, initialUrl)
        }.stateIn(this.viewModelScope, SharingStarted.Lazily, initialState)
    }

    fun onEvent(event: AddLinkEvent) {
        this.viewModelScope.launch { eventsFlow.emit(event) }
    }
}
