package com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker

import com.greenrobotdev.linklibrary.bookmarks.model.Category
import kotlinx.serialization.Serializable

@Serializable
data class CategoryPickerState(
    val isLoading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null,
    val error: String? = null,
    val query: String = "",
    val expandedIds: Set<String> = emptySet(),
    val recent: List<Category> = emptyList(),
    val dialog: CategoryPickerDialog? = null,
    /** "Use this" succeeded — the sheet closes. */
    val applied: Boolean = false,
) {
    val selected: Category? get() = categories.firstOrNull { it.id == selectedCategoryId }
    val selectedPath: String? get() = selected?.path(categories)
    val browsing: Boolean get() = query.isBlank()
}

/** Which modal dialog the sheet is showing, if any. */
@Serializable
data class CategoryPickerDialog(
    val kind: Kind,
    /** Category the dialog acts on; for ADD_SUB it is the parent (null = new top-level). */
    val categoryId: String?,
    /** Pre-filled name for RENAME. */
    val initialName: String = "",
) {
    @Serializable
    enum class Kind { ADD_SUB, RENAME, CONFIRM_REMOVE }
}

sealed interface CategoryPickerEvent {
    data class Select(val categoryId: String?) : CategoryPickerEvent
    data class Search(val query: String) : CategoryPickerEvent
    data class ToggleExpand(val categoryId: String) : CategoryPickerEvent
    /** Create "<query>" inside the selected category, or at top level for "No category". */
    object CreateFromSearch : CategoryPickerEvent
    object AddSub : CategoryPickerEvent
    /** "+ New top-level category" — opens the create dialog with no parent. */
    object AddTopLevel : CategoryPickerEvent
    object Rename : CategoryPickerEvent
    /** Submit the open dialog's name field (ADD_SUB / RENAME). */
    data class DialogName(val name: String) : CategoryPickerEvent
    object Remove : CategoryPickerEvent
    object ConfirmRemove : CategoryPickerEvent
    object CancelDialog : CategoryPickerEvent
    /** Assign the selection to the link (or clear it for "No category"). */
    object UseThis : CategoryPickerEvent
    object ClearError : CategoryPickerEvent
}
