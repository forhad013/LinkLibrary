package com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker

import com.greenrobotdev.linklibrary.bookmarks.model.Category
import kotlinx.serialization.Serializable

@Serializable
data class CategoryPickerState(
    val isLoading: Boolean = true,
    /** Raw category list from the DB (never filtered). */
    val allCategories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null,
    val error: String? = null,
    val query: String = "",
    val expandedIds: Set<String> = emptySet(),
    val recent: List<Category> = emptyList(),
    val dialog: CategoryPickerDialog? = null,
    /** "Use this" succeeded — the sheet closes. */
    val applied: Boolean = false,
    /** Delete-flow phase; see [DeletePhase]. */
    val deletePhase: DeletePhase = DeletePhase.NONE,
    /** The category pending delete (confirm card / undo bar target). */
    val deletingCategory: Category? = null,
    /** How many links lose this category if deleted. */
    val deletedLinkCount: Int = 0,
    /** User's choice for children of a deleted parent (parent-choice card); null = card unanswered. */
    val parentDeleteChoice: ParentDeleteChoiceKind? = null,
    /** Select-only (filter) mode: the ticked categories; several allowed. */
    val checkedIds: Set<String> = emptySet(),
) {
    /** Rows the UI lists: everything except the subtree hidden while a delete waits out its undo window. */
    val categories: List<Category>
        get() = if (pendingDeleteIds.isEmpty()) allCategories
        else allCategories.filter { it.id !in pendingDeleteIds }

    val selected: Category? get() = categories.firstOrNull { it.id == selectedCategoryId }
    val selectedPath: String? get() = selected?.path(categories)
    val browsing: Boolean get() = query.isBlank()

    /**
     * Ids hidden during the undo window: the whole subtree. (Filtering the parent id alone
     * already drops its children from the browse tree; hiding the set keeps search consistent.)
     */
    val pendingDeleteIds: Set<String>
        get() {
            val category = deletingCategory ?: return emptySet()
            if (deletePhase != DeletePhase.UNDO) return emptySet()
            return subtreeIds(category.id, allCategories)
        }
}

/** Phases of the swipe/confirm delete flow. SWIPING is UI-local drag progress; state stays CONFIRM. */
@Serializable
enum class DeletePhase { NONE, CONFIRM, SWIPING, UNDO }

/** What happens to a deleted category's children. */
@Serializable
enum class ParentDeleteChoiceKind { KEEP_CHILDREN, DELETE_ALL }

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
    /** User finished the swipe (or the confirm card's Delete) → soft delete, start the undo timer. */
    object SwipeComplete : CategoryPickerEvent
    /** Undo bar tapped → cancel timer, restore the hidden rows. */
    object UndoDelete : CategoryPickerEvent
    /** Parent-choice card: what to do with the children when deleting. */
    data class ParentDeleteChoice(val choice: ParentDeleteChoiceKind) : CategoryPickerEvent
    /** Filter mode: tick or untick a category. */
    data class ToggleChecked(val categoryId: String) : CategoryPickerEvent
    /** Filter mode: untick everything. */
    object ClearChecked : CategoryPickerEvent
}
