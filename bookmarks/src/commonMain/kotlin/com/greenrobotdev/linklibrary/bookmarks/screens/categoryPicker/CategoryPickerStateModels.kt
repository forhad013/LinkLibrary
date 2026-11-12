package com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker

import com.greenrobotdev.linklibrary.bookmarks.model.Category
import kotlinx.serialization.Serializable

@Serializable
data class CategoryPickerState(
    val isLoading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null,
    val error: String? = null
) {
    val selected: Category? get() = categories.firstOrNull { it.id == selectedCategoryId }
    val selectedPath: String? get() = selected?.path(categories)
}

sealed interface CategoryPickerEvent {
    data class Select(val categoryId: String?) : CategoryPickerEvent
    data class CreateCategory(val name: String, val parentId: String?) : CategoryPickerEvent
    object ClearError : CategoryPickerEvent
}
