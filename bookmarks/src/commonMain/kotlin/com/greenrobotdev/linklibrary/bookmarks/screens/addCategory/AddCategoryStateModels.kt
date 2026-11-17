package com.greenrobotdev.linklibrary.bookmarks.screens.addCategory

import kotlinx.serialization.Serializable

/**
 * State for Add Category screen
 */
@Serializable
data class AddCategoryState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val name: String = "",
    val description: String = "",
    val isFormValid: Boolean = false,
    val success: Boolean = false
)

/**
 * Events for Add Category screen
 */
sealed interface AddCategoryEvent {
    data class NameChanged(val name: String) : AddCategoryEvent
    data class DescriptionChanged(val description: String) : AddCategoryEvent
    object Submit : AddCategoryEvent
    object ClearError : AddCategoryEvent
}
