package com.greenrobotdev.linklibrary.screens.add

import com.greenrobotdev.linklibrary.data.LinkMetadata
import com.greenrobotdev.linklibrary.model.Link
import com.greenrobotdev.linklibrary.screens.collections.Collection
import com.greenrobotdev.linklibrary.screens.tags.Tag
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
    val success: Boolean = false,
    // Auto-fetch related fields
    val isFetching: Boolean = false,
    val fetchError: String? = null,
    val fetchedMetadata: LinkMetadata? = null,
    // Tag and Collection selection fields
    val selectedTags: Set<String> = emptySet(),
    val selectedCollections: Set<String> = emptySet(),
    val availableTags: List<Tag> = emptyList(),
    val availableCollections: List<Collection> = emptyList(),
    val isLoadingTagsAndCollections: Boolean = false
)

sealed interface AddLinkEvent {
    data class TitleChanged(val title: String) : AddLinkEvent
    data class UrlChanged(val url: String) : AddLinkEvent
    data class DescriptionChanged(val description: String) : AddLinkEvent
    data class ToggleFavorite(val isFavorite: Boolean) : AddLinkEvent
    object Submit : AddLinkEvent
    object ClearError : AddLinkEvent
    // Auto-fetch events
    object FetchMetadata : AddLinkEvent
    object ClearFetchError : AddLinkEvent
    data class MetadataFetchResult(val metadata: LinkMetadata) : AddLinkEvent
    data class MetadataFetchError(val error: String) : AddLinkEvent
    // Tag and Collection selection events
    object LoadAvailableTagsAndCollections : AddLinkEvent
    data class ToggleTag(val tagId: String) : AddLinkEvent
    data class ToggleCollection(val collectionId: String) : AddLinkEvent
}
