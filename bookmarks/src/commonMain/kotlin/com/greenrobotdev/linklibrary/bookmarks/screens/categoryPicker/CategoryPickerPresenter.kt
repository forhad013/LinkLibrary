package com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.bookmarks.model.Category
import com.greenrobotdev.linklibrary.bookmarks.model.toCategory
import com.greenrobotdev.linklibrary.bookmarks.model.toEntity
import com.greenrobotdev.linklibrary.database.repository.CategoryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/** Undo window for a swiped category (ms). */
const val DELETE_UNDO_MS = 5_000L

/**
 * Category picker presenter (GRE-44): event → state reducer over the nested tree.
 * "Use this" assigns the link's category and flips [CategoryPickerState.applied];
 * the host observes it and closes. Close/back never writes anything.
 *
 * Delete flow: Remove sets CONFIRM with the target and link count; SwipeComplete hides the
 * subtree in memory ([CategoryPickerState.categories] filters it) and starts a [DELETE_UNDO_MS]
 * timer as a sibling coroutine (the event collector must stay free for UndoDelete). Only when
 * the timer fires — and its mandate (same category, still UNDO) holds — is the DB written.
 * KEEP_CHILDREN reparents children to the deleted category's parent; DELETE_ALL cascades.
 */
@Composable
fun CategoryPickerPresenter(
    initialState: CategoryPickerState,
    events: Flow<CategoryPickerEvent>,
    linkId: String?,
    initialSelection: String?,
    categoryRepository: CategoryRepository = koinInject(),
): CategoryPickerState {
    var state by remember { mutableStateOf(initialState) }

    // Tree stream: first emission seeds selection (link's current category) and expands ancestors
    LaunchedEffect(Unit) {
        categoryRepository.getCategoriesWithCounts().collect { result ->
            result.fold(
                onSuccess = { rows ->
                    val categories = rows.map { it.toCategory() }
                    state = if (state.isLoading) {
                        val selected = initialSelection
                            ?: linkId?.let { id -> categoryRepository.getCategoryForLink(id).first().getOrNull()?.id }
                        state.copy(
                            isLoading = false,
                            allCategories = categories,
                            selectedCategoryId = selected,
                            // open the tree down to the link's category, or to every ticked filter category
                            expandedIds = (listOfNotNull(selected) + state.checkedIds)
                                .flatMap { ancestorsOf(it, categories) }.toSet(),
                        )
                    } else {
                        state.copy(allCategories = categories) // categories getter re-filters the pending subtree
                    }
                },
                onFailure = { state = state.copy(isLoading = false, error = it.message) }
            )
        }
    }

    LaunchedEffect(Unit) {
        categoryRepository.getRecentCategories().first()
            .onSuccess { state = state.copy(recent = it.map { e -> e.toCategory() }) }
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is CategoryPickerEvent.Select -> {
                    val id = event.categoryId
                    state = if (id == null) {
                        state.copy(selectedCategoryId = null)
                    } else {
                        state.copy(
                            selectedCategoryId = id,
                            expandedIds = state.expandedIds + ancestorsOf(id, state.allCategories),
                        )
                    }
                }

                is CategoryPickerEvent.Search -> state = state.copy(query = event.query)

                is CategoryPickerEvent.ToggleExpand -> {
                    val id = event.categoryId
                    state = state.copy(
                        expandedIds = if (id in state.expandedIds) state.expandedIds - id else state.expandedIds + id
                    )
                }

                CategoryPickerEvent.CreateFromSearch -> {
                    val name = state.query.trim()
                    val parent = state.selectedCategoryId
                    val error = validateCategoryName(name, parent, state.allCategories)
                        ?: if (!canAddChildUnder(parent, state.allCategories))
                            "Categories can be up to $MAX_CATEGORY_DEPTH levels deep" else null
                    if (error != null) {
                        state = state.copy(error = error)
                    } else {
                        val entity = Category(id = "", name = name, parentId = parent).toEntity()
                        categoryRepository.addCategory(entity).first().fold(
                            onSuccess = { created ->
                                state = state.copy(
                                    selectedCategoryId = created.id,
                                    query = "",
                                    expandedIds = state.expandedIds + listOfNotNull(parent),
                                )
                            },
                            onFailure = { state = state.copy(error = it.message ?: "Could not create category") }
                        )
                    }
                }

                CategoryPickerEvent.AddSub -> {
                    val parent = state.selectedCategoryId ?: return@collect
                    state = state.copy(dialog = CategoryPickerDialog(CategoryPickerDialog.Kind.ADD_SUB, parent))
                }

                CategoryPickerEvent.AddTopLevel -> {
                    state = state.copy(dialog = CategoryPickerDialog(CategoryPickerDialog.Kind.ADD_SUB, null))
                }

                CategoryPickerEvent.Rename -> {
                    val id = state.selectedCategoryId ?: return@collect
                    val name = state.allCategories.firstOrNull { it.id == id }?.name ?: return@collect
                    state = state.copy(dialog = CategoryPickerDialog(
                        CategoryPickerDialog.Kind.RENAME, id, initialName = name
                    ))
                }

                is CategoryPickerEvent.DialogName -> {
                    val dialog = state.dialog ?: return@collect
                    val name = event.name.trim()
                    when (dialog.kind) {
                        CategoryPickerDialog.Kind.ADD_SUB -> {
                            val parent = dialog.categoryId
                            val error = validateCategoryName(name, parent, state.allCategories)
                                ?: if (!canAddChildUnder(parent, state.allCategories))
                                    "Categories can be up to $MAX_CATEGORY_DEPTH levels deep" else null
                            if (error != null) {
                                state = state.copy(error = error)
                            } else {
                                val entity = Category(id = "", name = name, parentId = parent).toEntity()
                                categoryRepository.addCategory(entity).first().fold(
                                    onSuccess = { created ->
                                        state = state.copy(
                                            dialog = null,
                                            selectedCategoryId = created.id,
                                            expandedIds = state.expandedIds + listOfNotNull(parent),
                                        )
                                    },
                                    onFailure = { state = state.copy(dialog = null, error = it.message ?: "Could not create category") }
                                )
                            }
                        }

                        CategoryPickerDialog.Kind.RENAME -> {
                            val id = dialog.categoryId ?: return@collect
                            val category = state.allCategories.firstOrNull { it.id == id }
                            if (category == null) {
                                state = state.copy(dialog = null)
                            } else {
                                val error = validateCategoryName(
                                    name, category.parentId, state.allCategories, excludeId = category.id
                                )
                                if (error != null) {
                                    state = state.copy(error = error)
                                } else {
                                    categoryRepository.renameCategory(category.id, name).first().fold(
                                        onSuccess = { state = state.copy(dialog = null) },
                                        onFailure = { state = state.copy(dialog = null, error = it.message ?: "Could not rename") }
                                    )
                                }
                            }
                        }

                        CategoryPickerDialog.Kind.CONFIRM_REMOVE -> Unit
                    }
                }

                CategoryPickerEvent.Remove -> {
                    val category = state.selected ?: return@collect
                    val hasChildren = state.allCategories.any { it.parentId == category.id }
                    state = state.copy(
                        dialog = null,
                        deletePhase = DeletePhase.CONFIRM,
                        deletingCategory = category,
                        deletedLinkCount = category.count,
                        // no children → the parent-choice card never applies; DELETE_ALL of a leaf == plain delete
                        parentDeleteChoice = if (hasChildren) null else ParentDeleteChoiceKind.DELETE_ALL,
                    )
                }

                CategoryPickerEvent.SwipeComplete -> {
                    val category = state.deletingCategory ?: return@collect
                    state = state.copy(deletePhase = DeletePhase.UNDO)
                    // sibling coroutine: the collector must stay free to receive UndoDelete
                    launch {
                        delay(DELETE_UNDO_MS)
                        val stillPending = state.deletePhase == DeletePhase.UNDO &&
                            state.deletingCategory?.id == category.id
                        if (stillPending) {
                            val failure = performDelete(categoryRepository, category.id, state.parentDeleteChoice)
                            state = resetDeleteState(state, failure)
                        }
                    }
                }

                CategoryPickerEvent.UndoDelete -> {
                    // rows were only hidden by the categories getter; dropping the delete state restores them
                    state = resetDeleteState(state)
                }

                is CategoryPickerEvent.ToggleChecked -> {
                    val id = event.categoryId
                    state = state.copy(checkedIds = if (id in state.checkedIds) state.checkedIds - id else state.checkedIds + id)
                }

                CategoryPickerEvent.ClearChecked -> state = state.copy(checkedIds = emptySet())

                is CategoryPickerEvent.ParentDeleteChoice -> {
                    state = state.copy(parentDeleteChoice = event.choice)
                }

                CategoryPickerEvent.ConfirmRemove -> {
                    val category = state.deletingCategory ?: return@collect
                    val failure = performDelete(categoryRepository, category.id, state.parentDeleteChoice)
                    state = resetDeleteState(state, failure)
                }

                CategoryPickerEvent.CancelDialog -> state = state.copy(dialog = null)

                CategoryPickerEvent.UseThis -> {
                    val linkId = linkId
                    if (linkId == null) {
                        state = state.copy(applied = true) // move-parent mode: nothing to assign
                    } else {
                        val selected = state.selectedCategoryId
                        val result = if (selected == null) {
                            categoryRepository.clearLinkCategory(linkId).first()
                        } else {
                            categoryRepository.assignLinkToCategory(linkId, selected).first()
                        }
                        result.fold(
                            onSuccess = {
                                categoryRepository.getRecentCategories().first()
                                    .onSuccess { r -> state = state.copy(recent = r.map { it.toCategory() }) }
                                state = state.copy(applied = true)
                            },
                            onFailure = { state = state.copy(error = it.message ?: "Could not assign") }
                        )
                    }
                }

                CategoryPickerEvent.ClearError -> state = state.copy(error = null)
            }
        }
    }

    return state
}

/** The real DB write. null = done; a message means the delete failed and the row comes back. */
private suspend fun performDelete(
    repo: CategoryRepository,
    categoryId: String,
    choice: ParentDeleteChoiceKind?,
): String? {
    val result = when (choice ?: ParentDeleteChoiceKind.KEEP_CHILDREN) {
        ParentDeleteChoiceKind.DELETE_ALL -> repo.deleteCategoryTree(categoryId).first()
        ParentDeleteChoiceKind.KEEP_CHILDREN -> repo.deleteCategoryKeepChildren(categoryId).first()
    }
    return result.exceptionOrNull()?.message
}

/** Clear delete state; on success the selection falls to the deleted category's parent (or null). */
private fun resetDeleteState(state: CategoryPickerState, failure: String? = null): CategoryPickerState {
    val deleted = state.deletingCategory
    return state.copy(
        deletePhase = DeletePhase.NONE,
        deletingCategory = null,
        deletedLinkCount = 0,
        parentDeleteChoice = null,
        error = failure ?: state.error,
        selectedCategoryId = if (failure != null || deleted == null) state.selectedCategoryId
        else if (state.selectedCategoryId == deleted.id) deleted.parentId else state.selectedCategoryId,
    )
}
