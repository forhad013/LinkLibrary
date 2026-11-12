package com.greenrobotdev.linklibrary.bookmarks.screens.add

import com.greenrobotdev.linklibrary.bookmarks.model.Category
import com.greenrobotdev.linklibrary.bookmarks.model.Tag
import com.greenrobotdev.linklibrary.bookmarks.service.LinkMetadata
import kotlinx.serialization.Serializable

@Serializable
data class AddLinkState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val title: String = "",
    val url: String = "",
    val notes: String? = null,
    val isFavorite: Boolean = false,
    val isFormValid: Boolean = false,
    val success: Boolean = false,
    // Auto-fetch related fields
    val isFetching: Boolean = false,
    val fetchError: String? = null,
    val fetchedMetadata: LinkMetadata? = null,
    // Tag and Category selection fields
    val selectedTags: Set<String> = emptySet(),
    val selectedCategoryIds: Set<String> = emptySet(),
    val availableTags: List<Tag> = emptyList(),
    val availableCategories: List<Category> = emptyList(),
    val isLoadingTagsAndCategories: Boolean = false,
    // Task-related fields
    val isTask: Boolean = false,
    val taskPriority: String = "Medium",
    val dueTime: String = "09:00"
)

sealed interface AddLinkEvent {
    data class TitleChanged(val title: String) : AddLinkEvent
    data class UrlChanged(val url: String) : AddLinkEvent
    data class NotesChanged(val notes: String) : AddLinkEvent
    data class ToggleFavorite(val isFavorite: Boolean) : AddLinkEvent
    object Submit : AddLinkEvent
    object ClearError : AddLinkEvent
    // Auto-fetch events
    object FetchMetadata : AddLinkEvent
    object ClearFetchError : AddLinkEvent
    data class MetadataFetchResult(val metadata: LinkMetadata) : AddLinkEvent
    data class MetadataFetchError(val error: String) : AddLinkEvent
    // Tag and Category selection events
    object LoadAvailableTagsAndCategories : AddLinkEvent
    data class ToggleTag(val tagId: String) : AddLinkEvent
    data class ToggleCategory(val categoryId: String) : AddLinkEvent
    data class SetCategory(val categoryId: String?) : AddLinkEvent
    object ClearAllCategories : AddLinkEvent
    // Task-related events
    data class ToggleTask(val isTask: Boolean) : AddLinkEvent
    data class SetTaskPriority(val priority: String) : AddLinkEvent
    data class SetDueTime(val time: String) : AddLinkEvent
}
