package com.greenrobotdev.linklibrary.bookmarks.screens.manage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.bookmarks.model.Category
import com.greenrobotdev.linklibrary.bookmarks.model.Tag
import com.greenrobotdev.linklibrary.bookmarks.model.toCategory
import com.greenrobotdev.linklibrary.bookmarks.model.toTag
import com.greenrobotdev.linklibrary.database.repository.CategoryRepository
import com.greenrobotdev.linklibrary.database.repository.TagRepository
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class ManageState(
    val categories: List<Category> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val selectedCategoryId: String? = null,
    val selectedTagId: String? = null,
    // pair of (kind label, name) pending delete confirmation
    val deleteTarget: Pair<String, String>? = null
)

sealed interface ManageEvent {
    data class SelectCategory(val id: String?) : ManageEvent
    data class SelectTag(val id: String?) : ManageEvent
    data class RenameCategory(val id: String, val newName: String) : ManageEvent
    data class MoveCategory(val id: String, val newParentId: String?) : ManageEvent
    data class DeleteCategory(val id: String) : ManageEvent
    data class RenameTag(val id: String, val newName: String) : ManageEvent
    data class DeleteTag(val id: String) : ManageEvent
    object ConfirmDelete : ManageEvent
    object CancelDelete : ManageEvent
}

class ManageViewModel : MoleculeViewModel<ManageEvent, ManageState>(), KoinComponent {

    private val categoryRepository: CategoryRepository by inject()
    private val tagRepository: TagRepository by inject()
    private var pendingDelete: PendingDelete? = null

    private sealed interface PendingDelete {
        data class Category(val id: String) : PendingDelete
        data class Tag(val id: String) : PendingDelete
    }

    @Composable
    override fun models(events: Flow<ManageEvent>): ManageState {
        var state by remember { mutableStateOf(ManageState()) }

        LaunchedEffect(Unit) {
            categoryRepository.getCategoriesWithCounts().collect { result ->
                result.onSuccess { rows -> state = state.copy(categories = rows.map { it.toCategory() }) }
            }
        }
        LaunchedEffect(Unit) {
            tagRepository.getTags().collect { result ->
                result.onSuccess { tags -> state = state.copy(tags = tags.map { it.toTag() }) }
            }
        }
        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    is ManageEvent.SelectCategory -> state = state.copy(selectedCategoryId = event.id, selectedTagId = null)
                    is ManageEvent.SelectTag -> state = state.copy(selectedTagId = event.id, selectedCategoryId = null)
                    is ManageEvent.RenameCategory -> categoryRepository.renameCategory(event.id, event.newName).first()
                    is ManageEvent.MoveCategory -> categoryRepository.moveCategory(event.id, event.newParentId).first()
                    is ManageEvent.DeleteCategory -> {
                        pendingDelete = PendingDelete.Category(event.id)
                        state = state.copy(deleteTarget = "category" to (state.categories.firstOrNull { it.id == event.id }?.name ?: ""))
                    }
                    is ManageEvent.RenameTag -> {
                        tagRepository.getTagById(event.id).first().getOrNull()?.let { tag ->
                            tagRepository.updateTag(tag.copy(name = event.newName)).first()
                        }
                    }
                    is ManageEvent.DeleteTag -> {
                        pendingDelete = PendingDelete.Tag(event.id)
                        state = state.copy(deleteTarget = "tag" to (state.tags.firstOrNull { it.id == event.id }?.name ?: ""))
                    }
                    ManageEvent.ConfirmDelete -> {
                        when (val pending = pendingDelete) {
                            is PendingDelete.Category -> categoryRepository.deleteCategory(pending.id).first()
                            is PendingDelete.Tag -> tagRepository.deleteTag(pending.id).first()
                            null -> Unit
                        }
                        pendingDelete = null
                        state = state.copy(deleteTarget = null)
                    }
                    ManageEvent.CancelDelete -> {
                        pendingDelete = null
                        state = state.copy(deleteTarget = null)
                    }
                }
            }
        }

        return state
    }

    override fun initialValue(): ManageState = ManageState()
}
