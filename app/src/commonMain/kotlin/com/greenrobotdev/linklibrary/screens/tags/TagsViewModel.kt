package com.greenrobotdev.linklibrary.screens.tags

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
 * ViewModel for Tags screen
 * Follows MVVM+UseCase architecture pattern
 */
class TagsViewModel : ViewModel(), KoinComponent {

    private val initialState: TagsState = TagsState()

    private val eventsFlow: MutableSharedFlow<TagsEvent> = MutableSharedFlow(10)

    val states  by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            TagsUseCase(initialState, eventsFlow)
        }.stateIn(viewModelScope, SharingStarted.Lazily, initialState)
    }

    fun onEvent(event: TagsEvent) {
        viewModelScope.launch {
            eventsFlow.emit(event)
        }
    }
}
