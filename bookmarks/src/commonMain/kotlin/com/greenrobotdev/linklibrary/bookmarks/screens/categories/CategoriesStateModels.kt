package com.greenrobotdev.linklibrary.bookmarks.screens.categories

import com.greenrobotdev.linklibrary.bookmarks.model.Category
import kotlinx.serialization.Serializable

/**
 * State for Categories screen
 */
@Serializable
data class CategoriesState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val categoryNames: List<Category> = emptyList()
)

/**
 * Events for Categories screen
 */
sealed interface CategoriesEvent {
    object LoadCategories : CategoriesEvent
    object ClearError : CategoriesEvent
}
