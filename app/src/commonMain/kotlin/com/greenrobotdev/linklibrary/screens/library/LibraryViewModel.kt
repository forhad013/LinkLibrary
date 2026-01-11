package com.greenrobotdev.linklibrary.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LibraryViewModel : ViewModel(), KoinComponent {
    private val initialState: LibraryState = LibraryState()

    private val linkRepository: LinkRepository by inject()

    val states by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            LibraryUseCase(initialState, linkRepository)
        }.stateIn(this.viewModelScope, SharingStarted.Lazily, initialState)
    }

    fun onEvent(event: LibraryEvent) {
        // Events are handled in the UseCase
    }
}
