package com.greenrobotdev.linklibrary.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

/**
 * Base ViewModel class that integrates Molecule for state management.
 *
 * This class provides a standardized pattern for managing UI state using Molecule's
 * reactive composition system. It handles event flows and produces state models
 * through composable presenter functions.
 *
 * @param Event The type of user events/actions this ViewModel handles
 * @param Model The type of UI state/model this ViewModel produces
 *
 * Usage:
 * ```kotlin
 * class MyViewModel : MoleculeViewModel<MyEvent, MyModel>() {
 *
 *   @Composable
 *   override fun models(events: Flow<Event>): Model {
 *     return myPresenter(events, dependencies...)
 *   }
 * }
 * ```
 *
 * In your screen:
 * ```kotlin
 *
 * fun MyScreen(viewModel: MyViewModel = viewModel()) {
 *   val model by viewModel.models.collectAsState()
 *
 *   // Handle user interactions
 *   Button(onClick = { viewModel.take(MyEvent.Clicked) }) {
 *     Text("Click me")
 *   }
 * }
 * ```
 */
expect abstract class MoleculeViewModel<Event, Model> {

    /**
     * Scope for Molecule composition and state management.
     * Platform-specific implementation provided by expect/actual.
     */
    protected val moleculeScope: CoroutineScope

    /**
     * StateFlow of models produced by the Molecule presenter.
     * Lazily initialized to avoid unnecessary composition when ViewModel isn't active.
     *
     * This is the main output of the ViewModel - UI components observe this
     * StateFlow to get the current model/state.
     */
    val models: StateFlow<Model>

    /**
     * Submits an event to be processed by the presenter.
     *
     * This is the main entry point for user interactions. Events are buffered
     * and processed sequentially by the presenter function.
     *
     * @param event The user event to process
     * @throws IllegalStateException if the event buffer is full (indicates backpressure issues)
     */
    open fun take(event: Event)

    /**
     * Cleanup method to be called when the ViewModel is no longer needed.
     * Platform-specific implementation provided by expect/actual.
     * Protected visibility to match AndroidX ViewModel's onCleared().
     */
    protected open fun onCleared()
}
