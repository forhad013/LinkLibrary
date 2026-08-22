package com.greenrobotdev.linklibrary.screens.addCollection

import kotlinx.serialization.Serializable

/**
 * State for Add Collection screen
 */
@Serializable
data class AddCollectionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val name: String = "",
    val description: String = "",
    val isFormValid: Boolean = false,
    val success: Boolean = false
)

/**
 * Events for Add Collection screen
 */
sealed interface AddCollectionEvent {
    data class NameChanged(val name: String) : AddCollectionEvent
    data class DescriptionChanged(val description: String) : AddCollectionEvent
    object Submit : AddCollectionEvent
    object ClearError : AddCollectionEvent
}
