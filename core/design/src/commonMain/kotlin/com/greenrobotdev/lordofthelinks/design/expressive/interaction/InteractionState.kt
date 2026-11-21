package com.greenrobotdev.lordofthelinks.design.expressive.interaction

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

