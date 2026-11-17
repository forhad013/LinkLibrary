package com.greenrobotdev.linklibrary.bookmarks.screens.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.bookmarks.model.Link
import com.greenrobotdev.linklibrary.bookmarks.model.toCategory
import com.greenrobotdev.linklibrary.bookmarks.model.toDomain
import com.greenrobotdev.linklibrary.bookmarks.model.toEntity
import com.greenrobotdev.linklibrary.bookmarks.model.toTag
import com.greenrobotdev.linklibrary.database.repository.CategoryRepository
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.database.repository.TagRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/** Pause after the last keystroke before the note is written. */
private const val NOTE_SAVE_DELAY_MS = 600L

/**
 * Link details presenter. Live sources: the link (with tag names), its tag rows, categories
 * and its category. Every event writes to the DB; the flows bring the change back.
 */
@Composable
fun LinkDetailPresenter(
    events: Flow<LinkDetailEvent>,
    linkRepository: LinkRepository,
    categoryRepository: CategoryRepository,
    tagRepository: TagRepository,
    linkId: String,
): LinkDetailState {
    var state by remember { mutableStateOf(LinkDetailState()) }
    // Latest note text not yet in the DB; a newer link emission must not overwrite it
    var pendingNote by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(linkId) {
        linkRepository.getLinksWithTags().collect { result ->
            result.fold(
                onSuccess = { entities ->
                    val link = entities.firstOrNull { it.id == linkId }?.toDomain()
                    state = if (link == null && !state.isLoading) state.copy(isDeleted = true) // deleted elsewhere
                    else state.copy(
                        isLoading = false,
                        link = link?.let { l -> pendingNote?.let { l.copy(description = it) } ?: l },
                        error = if (link == null) "Link not found" else null,
                    )
                },
                onFailure = { state = state.copy(isLoading = false, error = it.message ?: "Could not load link") },
            )
        }
    }
    LaunchedEffect(linkId) {
        tagRepository.getTagsForLink(linkId).collect { result ->
            result.onSuccess { rows -> state = state.copy(linkTags = rows.map { it.toTag() }) }
        }
    }
    LaunchedEffect(Unit) {
        categoryRepository.getCategories().collect { result ->
            result.onSuccess { rows -> state = state.copy(categories = rows.map { it.toCategory() }) }
        }
    }
    LaunchedEffect(linkId) {
        categoryRepository.getLinkCategoryIds().collect { result ->
            result.onSuccess { state = state.copy(categoryId = it[linkId]) }
        }
    }

    LaunchedEffect(Unit) {
        var noteJob: Job? = null

        suspend fun save(update: (Link) -> Link) {
            val link = state.link ?: return
            val updated = update(link)
            state = state.copy(link = updated) // instant UI; the links flow confirms
            linkRepository.updateLink(updated.toEntity()).first()
                .onFailure { state = state.copy(link = link, error = it.message ?: "Could not save") }
        }

        events.collect { event ->
            when (event) {
                LinkDetailEvent.ToggleFavorite -> save { it.copy(isFavorite = !it.isFavorite) }
                LinkDetailEvent.ToggleArchived -> save { it.copy(isArchived = !it.isArchived) }
                LinkDetailEvent.ToggleOpened -> save { it.copy(isOpened = !it.isOpened) }
                LinkDetailEvent.MarkOpened -> if (state.link?.isOpened == false) save { it.copy(isOpened = true) }
                is LinkDetailEvent.SetType -> save { it.copy(type = event.type) }
                is LinkDetailEvent.Rename -> {
                    val title = event.title.trim()
                    if (title.isNotEmpty()) save { it.copy(title = title) }
                }

                is LinkDetailEvent.SetCategory -> {
                    val result = event.categoryId?.let { categoryRepository.assignLinkToCategory(linkId, it).first() }
                        ?: categoryRepository.clearLinkCategory(linkId).first()
                    result.onFailure { state = state.copy(error = it.message ?: "Could not change category") }
                }

                is LinkDetailEvent.SetTags -> {
                    val current = state.linkTags.map { it.id }.toSet()
                    val failed = (event.tagIds - current).map { tagRepository.assignTagToLink(linkId, it).first() } +
                        (current - event.tagIds).map { tagRepository.removeTagFromLink(linkId, it).first() }
                    failed.firstOrNull { it.isFailure }?.exceptionOrNull()
                        ?.let { state = state.copy(error = it.message ?: "Could not change tags") }
                }

                is LinkDetailEvent.NoteChanged -> {
                    pendingNote = event.note
                    state = state.copy(link = state.link?.copy(description = event.note), noteSaving = true)
                    noteJob?.cancel()
                    // sibling coroutine so typing isn't blocked by the write
                    noteJob = launch {
                        delay(NOTE_SAVE_DELAY_MS)
                        val link = state.link ?: return@launch
                        linkRepository.updateLink(link.copy(description = event.note).toEntity()).first()
                            .onFailure { state = state.copy(error = it.message ?: "Could not save note") }
                        if (pendingNote == event.note) pendingNote = null
                        state = state.copy(noteSaving = false)
                    }
                }

                LinkDetailEvent.Delete -> {
                    noteJob?.cancel()
                    linkRepository.deleteLink(linkId).first().fold(
                        onSuccess = { state = state.copy(isDeleted = true) },
                        onFailure = { state = state.copy(error = it.message ?: "Could not delete link") },
                    )
                }

                LinkDetailEvent.ClearError -> state = state.copy(error = null)
            }
        }
    }

    return state
}
