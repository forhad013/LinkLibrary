package com.greenrobotdev.linklibrary.bookmarks.screens.manage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.bookmarks.model.Tag
import com.greenrobotdev.linklibrary.bookmarks.model.toTag
import com.greenrobotdev.linklibrary.database.repository.TagRepository
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// Categories are managed inline by the embedded category picker; this VM covers the Tags tab.
data class ManageState(
    val tags: List<Tag> = emptyList(),
    val categoriesVisible: Boolean = true,
    val selectedTagId: String? = null,
    // pair of (kind label, name) pending delete confirmation
    val deleteTarget: Pair<String, String>? = null,
    // pair of (id, current name) pending rename dialog
    val renaming: Pair<String, String>? = null
)

sealed interface ManageEvent {
    data class ShowCategories(val visible: Boolean = true) : ManageEvent
    object ShowTags : ManageEvent
    data class SelectTag(val id: String?) : ManageEvent
    object StartRename : ManageEvent
    object CancelRename : ManageEvent
    data class RenameTag(val id: String, val newName: String) : ManageEvent
    data class DeleteTag(val id: String) : ManageEvent
    object ConfirmDelete : ManageEvent
    object CancelDelete : ManageEvent
}

class ManageViewModel : MoleculeViewModel<ManageEvent, ManageState>(), KoinComponent {

    private val tagRepository: TagRepository by inject()
    private var pendingDeleteTagId: String? = null

    @Composable
    override fun models(events: Flow<ManageEvent>): ManageState {
        var state by remember { mutableStateOf(ManageState()) }

        LaunchedEffect(Unit) {
            tagRepository.getTags().collect { result ->
                result.onSuccess { tags -> state = state.copy(tags = tags.map { it.toTag() }) }
            }
        }
        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    is ManageEvent.ShowCategories -> state = state.copy(categoriesVisible = true)
                    ManageEvent.ShowTags -> state = state.copy(categoriesVisible = false)
                    is ManageEvent.SelectTag -> state = state.copy(selectedTagId = event.id)
                    ManageEvent.StartRename -> {
                        val tag = state.tags.firstOrNull { it.id == state.selectedTagId }
                        state = state.copy(renaming = tag?.let { it.id to it.name })
                    }
                    ManageEvent.CancelRename -> state = state.copy(renaming = null)
                    is ManageEvent.RenameTag -> {
                        tagRepository.getTagById(event.id).first().getOrNull()?.let { tag ->
                            tagRepository.updateTag(tag.copy(name = event.newName)).first()
                        }
                        state = state.copy(renaming = null)
                    }
                    is ManageEvent.DeleteTag -> {
                        pendingDeleteTagId = event.id
                        state = state.copy(deleteTarget = "tag" to (state.tags.firstOrNull { it.id == event.id }?.name ?: ""))
                    }
                    ManageEvent.ConfirmDelete -> {
                        pendingDeleteTagId?.let { tagRepository.deleteTag(it).first() }
                        pendingDeleteTagId = null
                        state = state.copy(deleteTarget = null, selectedTagId = null)
                    }
                    ManageEvent.CancelDelete -> {
                        pendingDeleteTagId = null
                        state = state.copy(deleteTarget = null)
                    }
                }
            }
        }

        return state
    }

    override fun initialValue(): ManageState = ManageState()
}
