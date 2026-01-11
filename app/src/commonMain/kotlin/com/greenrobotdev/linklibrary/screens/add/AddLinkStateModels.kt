package com.greenrobotdev.linklibrary.screens.add

import com.greenrobotdev.linklibrary.model.Link
import kotlinx.serialization.Serializable

@Serializable
data class AddLinkState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val title: String = "",
    val url: String = "",
    val description: String = "",
    val isFavorite: Boolean = false,
    val isFormValid: Boolean = false,
    val success: Boolean = false
)

sealed interface AddLinkEvent {
    data class TitleChanged(val title: String) : AddLinkEvent
    data class UrlChanged(val url: String) : AddLinkEvent
    data class DescriptionChanged(val description: String) : AddLinkEvent
    data class ToggleFavorite(val isFavorite: Boolean) : AddLinkEvent
    object Submit : AddLinkEvent
    object ClearError : AddLinkEvent
}
