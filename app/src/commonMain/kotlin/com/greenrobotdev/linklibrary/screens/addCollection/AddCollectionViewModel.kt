package com.greenrobotdev.linklibrary.screens.addCollection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

/**
 * ViewModel for Add Collection screen
 * Follows MVVM+UseCase architecture pattern
 */
class AddCollectionViewModel : ViewModel(), KoinComponent {

    private val initialState: AddCollectionState = AddCollectionState()

    private val eventsFlow: MutableSharedFlow<AddCollectionEvent> = MutableSharedFlow(10)

    val states by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            AddCollectionUseCase(initialState, eventsFlow)
        }.stateIn(viewModelScope, SharingStarted.Lazily, initialState)
    }

    fun onEvent(event: AddCollectionEvent) {
        viewModelScope.launch {
            eventsFlow.emit(event)
        }
    }
}
