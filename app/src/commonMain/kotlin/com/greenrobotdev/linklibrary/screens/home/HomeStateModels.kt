package com.greenrobotdev.linklibrary.screens.home
import com.greenrobotdev.linklibrary.model.Link
import kotlinx.serialization.Serializable

@Serializable
data class HomeState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val links: List<Link>? = null,
    val searchQuery: String = ""
)

sealed interface HomeEvent {
    // Define events that can be triggered from the UI
    object Refresh : HomeEvent
    data class ToggleFavorite(val id : String) : HomeEvent
    // Add other events here
}
