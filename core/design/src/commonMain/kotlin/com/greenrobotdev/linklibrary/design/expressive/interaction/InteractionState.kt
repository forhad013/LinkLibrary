package com.greenrobotdev.linklibrary.design.expressive.interaction

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.Flow

/**
 * Material Design 3 Expressive Interaction States
 *
 * Tracks and manages component interaction states for expressive animations.
 * Provides reactive state management for hover, press, focus, and drag interactions.
 */

/**
 * Sealed class representing all possible interaction states
 */
sealed class InteractionState {
    /** Default state when no interactions are active */
    object Idle : InteractionState()

    /** Pointer is hovering over the component (desktop) */
    object Hover : InteractionState()

    /** Component is being pressed/clicked */
    object Pressed : InteractionState()

    /** Component has keyboard focus */
    object Focused : InteractionState()

    /** Component is being dragged */
    object Dragging : InteractionState()

    /** Component is disabled */
    object Disabled : InteractionState()

    /** Multiple interactions active (hover + focused, etc.) */
    data class Combined(val states: Set<InteractionState>) : InteractionState()
}

/**
 * Remember interaction state based on Material 3 Interaction API
 *
 * @param interactions Flow of Material 3 interaction events
 * @param enabled Whether the component is enabled
 * @return Current interaction state
 */
@Composable
fun rememberInteractionState(
    interactions: Flow<Interaction>,
    enabled: Boolean = true
): InteractionState {
    // Track active interactions
    val activeInteractions = remember { mutableStateMapOf<Interaction, Any>() }
    var currentState = if (enabled) InteractionState.Idle else InteractionState.Disabled

    // Process interaction events
    LaunchedEffect(interactions) {
        snapshotFlow { interactions }.collect { flow ->
            flow.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> {
                        activeInteractions[interaction] = true
                    }
                    is PressInteraction.Release,
                    is PressInteraction.Cancel -> {
                        activeInteractions.remove(interaction.press)
                    }
                    is HoverInteraction.Enter -> {
                        activeInteractions[interaction] = true
                    }
                    is HoverInteraction.Exit -> {
                        activeInteractions.remove(interaction.enter)
                    }
                    is FocusInteraction.Focus -> {
                        activeInteractions[interaction] = true
                    }
                    is FocusInteraction.Unfocus -> {
                        activeInteractions.remove(interaction.focus)
                    }
                    is DragInteraction.Start -> {
                        activeInteractions[interaction] = true
                    }
                    is DragInteraction.Cancel,
                    is DragInteraction.Stop -> {
                        activeInteractions.remove(interaction.start)
                    }
                }
            }
        }
    }

    // Determine current state from active interactions
    if (!enabled) {
        currentState = InteractionState.Disabled
    } else {
        val states = mutableSetOf<InteractionState>()

        if (activeInteractions.keys.any { it is PressInteraction.Press }) {
            states.add(InteractionState.Pressed)
        }
        if (activeInteractions.keys.any { it is HoverInteraction.Enter }) {
            states.add(InteractionState.Hover)
        }
        if (activeInteractions.keys.any { it is FocusInteraction.Focus }) {
            states.add(InteractionState.Focused)
        }
        if (activeInteractions.keys.any { it is DragInteraction.Start }) {
            states.add(InteractionState.Dragging)
        }

        currentState = when {
            states.isEmpty() -> InteractionState.Idle
            states.size == 1 -> states.first()
            else -> InteractionState.Combined(states)
        }
    }

    return currentState
}

/**
 * Check if interaction state represents a pressed state
 */
fun isPressed(state: InteractionState): Boolean {
    return when (state) {
        is InteractionState.Pressed -> true
        is InteractionState.Combined -> state.states.contains(InteractionState.Pressed)
        else -> false
    }
}

/**
 * Check if interaction state represents a hovered state
 */
fun isHovered(state: InteractionState): Boolean {
    return when (state) {
        is InteractionState.Hover -> true
        is InteractionState.Combined -> state.states.contains(InteractionState.Hover)
        else -> false
    }
}

/**
 * Check if interaction state represents a focused state
 */
fun isFocused(state: InteractionState): Boolean {
    return when (state) {
        is InteractionState.Focused -> true
        is InteractionState.Combined -> state.states.contains(InteractionState.Focused)
        else -> false
    }
}

/**
 * Check if interaction state represents a dragging state
 */
fun isDragging(state: InteractionState): Boolean {
    return when (state) {
        is InteractionState.Dragging -> true
        is InteractionState.Combined -> state.states.contains(InteractionState.Dragging)
        else -> false
    }
}

/**
 * Check if component is disabled
 */
fun isDisabled(state: InteractionState): Boolean {
    return state is InteractionState.Disabled
}

/**
 * Check if component is idle (no interactions)
 */
fun isIdle(state: InteractionState): Boolean {
    return state is InteractionState.Idle
}

/**
 * Calculate scale value based on interaction state
 * Used for press-to-scale animations
 */
fun getPressScale(state: InteractionState): Float {
    return when {
        isPressed(state) -> 0.97f  // Slightly scale down when pressed
        isHovered(state) -> 1.02f // Slightly scale up when hovered (desktop)
        else -> 1.0f              // Normal scale
    }
}

/**
 * Calculate elevation offset based on interaction state
 * Used for dynamic elevation changes
 */
fun getElevationOffset(state: InteractionState, baseElevation: Float = 0f): Float {
    return when {
        isPressed(state) -> baseElevation + 4f  // Elevate further when pressed
        isHovered(state) || isFocused(state) -> baseElevation + 2f // Slight elevation on hover/focus
        else -> baseElevation
    }
}