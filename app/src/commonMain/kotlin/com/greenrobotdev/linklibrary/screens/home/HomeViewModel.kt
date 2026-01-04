package com.greenrobotdev.linklibrary.screens.home

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

class HomeViewModel : ViewModel(), KoinComponent {

    private val initialState: HomeState = HomeState()

    private val eventsFlow: MutableSharedFlow<HomeEvent> = MutableSharedFlow(5)
    private val linkRepository: LinkRepository by inject()

    val states by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            HomeUseCase(initialState, eventsFlow, linkRepository)
        }.stateIn(viewModelScope, SharingStarted.Lazily, initialState)
    }

    fun onRefresh() { viewModelScope.launch { eventsFlow.emit(HomeEvent.Refresh) } }
    fun onToggleFavorite(linkId: String) { viewModelScope.launch { eventsFlow.emit(HomeEvent.ToggleFavorite(linkId)) } }
}
