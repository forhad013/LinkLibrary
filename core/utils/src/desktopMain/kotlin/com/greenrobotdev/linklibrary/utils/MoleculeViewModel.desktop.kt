package com.greenrobotdev.linklibrary.utils

import androidx.compose.runtime.Composable
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancelScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * Desktop-specific actual implementation of MoleculeViewModel.
 * Uses custom lifecycle management instead of AndroidX ViewModel for desktop compatibility.
 */
actual abstract class MoleculeViewModel<Event, Model> {

    /**
     * Job for managing coroutine lifecycle on desktop.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * Scope for Molecule composition and state management.
     * Custom scope for desktop targets with proper lifecycle management.
     */
    actual protected val moleculeScope: CoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Event flow that buffers user events for processing by the presenter.
     * Has capacity to handle simultaneous UI events while detecting backpressure issues.
     */
    private val events: MutableSharedFlow<Event> = MutableSharedFlow(
        extraBufferCapacity = 20 // Large enough for simultaneous events, small enough to surface issues
    )

    /**
     * StateFlow of models produced by the Molecule presenter.
     * Lazily initialized to avoid unnecessary composition when ViewModel isn't active.
     *
     * This is the main output of the ViewModel - UI components observe this
     * StateFlow to get the current model/state.
     */
    actual val models: StateFlow<Model> by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            models(events)
        }.stateIn(
            scope = moleculeScope,
            started = kotlinx.coroutines.flow.SharingStarted.Lazily,
            initialValue = initialValue()
        )
    }

    /**
     * Submits an event to be processed by the presenter.
     *
     * This is the main entry point for user interactions. Events are buffered
     * and processed sequentially by the presenter function.
     *
     * @param event The user event to process
     * @throws IllegalStateException if the event buffer is full (indicates backpressure issues)
     */
    actual open fun take(event: Event) {
        if (!events.tryEmit(event)) {
            error("Event buffer overflow. Events are being produced faster than they can be processed.")
        }
    }

    /**
     * Composable function that implements the presenter logic.
     *
     * This function receives events and produces the current model/state.
     * It's recomposed whenever events or dependencies change, similar to
     * how Jetpack Compose recomposes UI.
     *
     * @param events Flow of events to process
     * @return The current model/state
     */
    @Composable
    protected abstract fun models(events: Flow<Event>): Model

    /**
     * Provides the initial value for the models StateFlow.
     *
     * Subclasses can override this to provide a custom initial state.
     * This is called lazily when models is first accessed.
     *
     * @return The initial model value
     */
    protected open fun initialValue(): Model {
        // Default implementation - subclasses should override this
        // to provide the actual initial state
        error("initialValue() must be overridden in subclasses")
    }

    /**
     * Cleanup method to be called when the ViewModel is no longer needed.
     * Desktop implementation uses custom coroutine job cancellation.
     */
    actual open fun onCleared() {
        viewModelJob.cancel()
    }
}
