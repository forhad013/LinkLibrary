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
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Category picker (GRE-44): single selection over the nested tree.
 * "Use this" assigns the link's category and flips [CategoryPickerState.applied];
 * the sheet observes it and closes. Close/back never writes anything.
 */
class CategoryPickerViewModel(
    private val linkId: String?,
    initialSelection: String? = null,
) : MoleculeViewModel<CategoryPickerEvent, CategoryPickerState>(), KoinComponent {

    private val categoryRepository: CategoryRepository by inject()
    private val initialSelection = initialSelection

    @Composable
    override fun models(events: Flow<CategoryPickerEvent>): CategoryPickerState {
        var state by remember { mutableStateOf(CategoryPickerState()) }

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
                                categories = categories,
                                selectedCategoryId = selected,
                                expandedIds = selected?.let { ancestorsOf(it, categories).toSet() } ?: emptySet(),
                            )
                        } else {
                            state.copy(categories = categories)
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
                                expandedIds = state.expandedIds + ancestorsOf(id, state.categories),
                            )
                        }
                    }

                    is CategoryPickerEvent.Search -> state = state.copy(query = event.query)

                    is CategoryPickerEvent.ToggleExpand -> {
                        println("LLDBG toggle ${event.categoryId} was=${state.expandedIds}") // TEMP debug
                        val id = event.categoryId
                        state = state.copy(
                            expandedIds = if (id in state.expandedIds) state.expandedIds - id else state.expandedIds + id
                        )
                    }

                    CategoryPickerEvent.CreateFromSearch -> {
                        val name = state.query.trim()
                        val parent = state.selectedCategoryId
                        val error = validateCategoryName(name, parent, state.categories)
                            ?: if (!canAddChildUnder(parent, state.categories))
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
                        val name = state.categories.firstOrNull { it.id == id }?.name ?: return@collect
                        state = state.copy(dialog = CategoryPickerDialog(
                            CategoryPickerDialog.Kind.RENAME, id, initialName = name
                        ))
                    }

                    is CategoryPickerEvent.DialogName -> {                        val dialog = state.dialog ?: return@collect
                        val name = event.name.trim()
                        when (dialog.kind) {
                            CategoryPickerDialog.Kind.ADD_SUB -> {
                                val parent = dialog.categoryId
                                val error = validateCategoryName(name, parent, state.categories)
                                    ?: if (!canAddChildUnder(parent, state.categories))
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
                                val category = state.categories.firstOrNull { it.id == id }
                                if (category == null) {
                                    state = state.copy(dialog = null)
                                } else {
                                    val error = validateCategoryName(
                                        name, category.parentId, state.categories, excludeId = category.id
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
                        val id = state.selectedCategoryId ?: return@collect
                        state.categories.firstOrNull { it.id == id } ?: return@collect
                        state = state.copy(dialog = CategoryPickerDialog(
                            CategoryPickerDialog.Kind.CONFIRM_REMOVE, id
                        ))
                    }

                    CategoryPickerEvent.ConfirmRemove -> {
                        val dialog = state.dialog ?: return@collect
                        val id = dialog.categoryId ?: return@collect
                        val hasChildren = state.categories.any { it.parentId == id }
                        if (hasChildren) {
                            state = state.copy(dialog = null, error = "Move or remove subcategories first.")
                        } else {
                            categoryRepository.deleteCategory(id).first().fold(
                                onSuccess = {
                                    state = if (state.selectedCategoryId == id) {
                                        state.copy(dialog = null, selectedCategoryId = null)
                                    } else {
                                        state.copy(dialog = null)
                                    }
                                },
                                onFailure = { state = state.copy(dialog = null, error = it.message ?: "Could not remove") }
                            )
                        }
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

    override fun initialValue(): CategoryPickerState = CategoryPickerState()
}
