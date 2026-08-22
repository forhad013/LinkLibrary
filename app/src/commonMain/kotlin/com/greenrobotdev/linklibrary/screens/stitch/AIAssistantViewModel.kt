package com.greenrobotdev.linklibrary.screens.stitch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import com.greenrobotdev.linklibrary.data.stitch.StitchRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * ViewModel for AI Assistant screen
 * Follows MVVM+UseCase architecture pattern
 */
class AIAssistantViewModel : ViewModel(), KoinComponent {

    private val initialState: AIAssistantState = AIAssistantState()

    private val eventsFlow: MutableSharedFlow<AIAssistantEvent> = MutableSharedFlow(10)
    private val stitchRepository: StitchRepository by inject()

    val states by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            AIAssistantUseCase(initialState, eventsFlow, stitchRepository)
        }.stateIn(viewModelScope, SharingStarted.Lazily, initialState)
    }

    fun onEvent(event: AIAssistantEvent) {
        viewModelScope.launch {
            eventsFlow.emit(event)
        }
    }
}
