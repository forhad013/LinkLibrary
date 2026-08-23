package com.greenrobotdev.linklibrary.screens.addTag

import kotlinx.serialization.Serializable

/**
 * State for Add Tag screen
 */
@Serializable
data class AddTagState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val name: String = "",
    val description: String = "",
    val isFormValid: Boolean = false,
    val success: Boolean = false
)

/**
 * Events for Add Tag screen
 */
sealed interface AddTagEvent {
    data class NameChanged(val name: String) : AddTagEvent
    data class DescriptionChanged(val description: String) : AddTagEvent
    object Submit : AddTagEvent
    object ClearError : AddTagEvent
}
