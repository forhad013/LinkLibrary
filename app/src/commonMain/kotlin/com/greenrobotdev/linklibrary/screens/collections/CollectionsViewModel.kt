package com.greenrobotdev.linklibrary.screens.collections

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
 * ViewModel for Collections screen
 * Follows MVVM+UseCase architecture pattern
 */
class CollectionsViewModel : ViewModel(), KoinComponent {

    private val initialState: CollectionsState = CollectionsState()

    private val eventsFlow: MutableSharedFlow<CollectionsEvent> = MutableSharedFlow(10)

    val states  by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            CollectionsUseCase(initialState, eventsFlow)
        }.stateIn(viewModelScope, SharingStarted.Lazily, initialState)
    }

    fun onEvent(event: CollectionsEvent) {
        viewModelScope.launch {
            eventsFlow.emit(event)
        }
    }
}
