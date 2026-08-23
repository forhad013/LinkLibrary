package com.greenrobotdev.linklibrary.screens.addTag

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
 * ViewModel for Add Tag screen
 * Follows MVVM+UseCase architecture pattern
 */
class AddTagViewModel : ViewModel(), KoinComponent {

    private val initialState: AddTagState = AddTagState()

    private val eventsFlow: MutableSharedFlow<AddTagEvent> = MutableSharedFlow(10)

    val states by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            AddTagUseCase(initialState, eventsFlow)
        }.stateIn(viewModelScope, SharingStarted.Lazily, initialState)
    }

    fun onEvent(event: AddTagEvent) {
        viewModelScope.launch {
            eventsFlow.emit(event)
        }
    }
}
