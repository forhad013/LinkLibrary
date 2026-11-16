package com.greenrobotdev.linklibrary.bookmarks.screens.tagPicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.bookmarks.model.Tag
import com.greenrobotdev.linklibrary.bookmarks.model.toEntity
import com.greenrobotdev.linklibrary.bookmarks.model.toTag
import com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker.DELETE_UNDO_MS
import com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker.DeletePhase
import com.greenrobotdev.linklibrary.database.repository.TagRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Tag picker presenter. Same delete flow as the category picker: Remove → CONFIRM,
 * SwipeComplete hides the row and starts a [DELETE_UNDO_MS] timer, and only when the timer
 * fires (still UNDO, same tag) is the DB written. Close/back never writes.
 */
@Composable
fun TagPickerPresenter(
    events: Flow<TagPickerEvent>,
    multiSelect: Boolean,
    initialChecked: Set<String>,
    tagRepository: TagRepository = koinInject(),
): TagPickerState {
    var state by remember { mutableStateOf(TagPickerState(checkedIds = initialChecked)) }

    LaunchedEffect(Unit) {
        tagRepository.getTagsWithCounts().collect { result ->
            result.fold(
                onSuccess = { rows ->
                    state = state.copy(isLoading = false, allTags = rows.map { it.toTag() })
                },
                onFailure = { state = state.copy(isLoading = false, error = it.message) },
            )
        }
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is TagPickerEvent.Tap -> {
                    if (state.isEditing) return@collect
                    val id = event.tagId
                    state = if (multiSelect) {
                        state.copy(
                            checkedIds = if (id in state.checkedIds) state.checkedIds - id else state.checkedIds + id,
                            focusedId = id,
                        )
                    } else {
                        state.copy(focusedId = if (state.focusedId == id) null else id)
                    }
                }

                is TagPickerEvent.Search -> state = state.copy(query = event.query)

                TagPickerEvent.CreateFromSearch -> state = create(state, state.query, tagRepository, multiSelect)(state)

                TagPickerEvent.StartCreate -> state = state.copy(creating = true, renamingId = null)

                TagPickerEvent.StartRename -> {
                    val id = state.focusedId ?: return@collect
                    state = state.copy(renamingId = id, creating = false)
                }

                is TagPickerEvent.SubmitName -> {
                    val renamingId = state.renamingId
                    val update = if (renamingId == null) create(state, event.name, tagRepository, multiSelect)
                    else rename(state, renamingId, event.name, tagRepository)
                    state = update(state)
                }

                TagPickerEvent.CancelEdit -> state = state.copy(creating = false, renamingId = null)

                TagPickerEvent.Remove -> {
                    val tag = state.focused ?: return@collect
                    state = state.copy(deletePhase = DeletePhase.CONFIRM, deletingTag = tag)
                }

                TagPickerEvent.SwipeComplete -> {
                    val tag = state.deletingTag ?: return@collect
                    state = state.copy(deletePhase = DeletePhase.UNDO)
                    // sibling coroutine: the collector must stay free to receive UndoDelete
                    launch {
                        delay(DELETE_UNDO_MS)
                        if (state.deletePhase == DeletePhase.UNDO && state.deletingTag?.id == tag.id) {
                            val failure = tagRepository.deleteTag(tag.id).first().exceptionOrNull()
                            state = state.copy(
                                deletePhase = DeletePhase.NONE,
                                deletingTag = null,
                                error = failure?.let { it.message ?: "Could not delete tag" } ?: state.error,
                                checkedIds = if (failure == null) state.checkedIds - tag.id else state.checkedIds,
                                focusedId = if (failure == null && state.focusedId == tag.id) null else state.focusedId,
                            )
                        }
                    }
                }

                TagPickerEvent.UndoDelete -> state = state.copy(deletePhase = DeletePhase.NONE, deletingTag = null)

                TagPickerEvent.Done -> state = state.copy(applied = true)

                TagPickerEvent.ClearChecked -> state = state.copy(checkedIds = emptySet())

                TagPickerEvent.ClearError -> state = state.copy(error = null)
            }
        }
    }

    return state
}

/** Helpers return an update applied to the state *after* the DB call, so tag-list emissions that land meanwhile aren't overwritten. */
private typealias Update = (TagPickerState) -> TagPickerState

private suspend fun create(snapshot: TagPickerState, rawName: String, repo: TagRepository, multiSelect: Boolean): Update {
    val name = rawName.trim()
    validateTagName(name, snapshot.allTags)?.let { msg -> return { it.copy(error = msg) } }
    return repo.addTag(Tag(id = "", name = name, count = 0).toEntity()).first().fold(
        onSuccess = { created ->
            { state -> state.copy(
                creating = false,
                query = "",
                focusedId = created.id,
                checkedIds = if (multiSelect) state.checkedIds + created.id else state.checkedIds,
            ) }
        },
        onFailure = { e -> { it.copy(error = e.message ?: "Could not create tag") } },
    )
}

private suspend fun rename(snapshot: TagPickerState, id: String, rawName: String, repo: TagRepository): Update {
    val name = rawName.trim()
    validateTagName(name, snapshot.allTags, excludeId = id)?.let { msg -> return { it.copy(error = msg) } }
    val entity = repo.getTagById(id).first().getOrNull() ?: return { it.copy(renamingId = null) }
    return repo.updateTag(entity.copy(name = name)).first().fold(
        onSuccess = { { it.copy(renamingId = null) } },
        onFailure = { e -> { it.copy(renamingId = null, error = e.message ?: "Could not rename") } },
    )
}
