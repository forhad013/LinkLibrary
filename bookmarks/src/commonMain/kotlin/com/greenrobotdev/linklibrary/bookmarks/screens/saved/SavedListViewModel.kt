package com.greenrobotdev.linklibrary.bookmarks.screens.saved

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.bookmarks.model.Category
import com.greenrobotdev.linklibrary.bookmarks.model.Link
import com.greenrobotdev.linklibrary.bookmarks.model.toCategory
import com.greenrobotdev.linklibrary.bookmarks.model.toDomain
import com.greenrobotdev.linklibrary.bookmarks.model.toEntity
import com.greenrobotdev.linklibrary.database.repository.CategoryRepository
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class SavedListState(
    val isLoading: Boolean = true,
    val links: List<Link> = emptyList(),
    val categories: List<Category> = emptyList(),
    /** linkId → categoryId */
    val linkCategoryIds: Map<String, String> = emptyMap(),
) {
    fun visible(scope: SavedScope): List<Link> = savedInScope(scope, links, categories, linkCategoryIds)
    fun categoryOf(link: Link): Category? = linkCategoryIds[link.id]?.let { id -> categories.firstOrNull { it.id == id } }
}

sealed interface SavedListEvent {
    data class Opened(val linkId: String) : SavedListEvent
    data class Archive(val linkId: String) : SavedListEvent
    data class Delete(val linkId: String) : SavedListEvent
    /** Undo: write the link back as it was before the last action. */
    data class Restore(val link: Link) : SavedListEvent
}

/** One Library list (a smart list, category or tag). Live: edits elsewhere show up here at once. */
class SavedListViewModel : MoleculeViewModel<SavedListEvent, SavedListState>(), KoinComponent {

    private val linkRepository: LinkRepository by inject()
    private val categoryRepository: CategoryRepository by inject()

    @Composable
    override fun models(events: Flow<SavedListEvent>): SavedListState {
        var state by remember { mutableStateOf(SavedListState()) }

        LaunchedEffect(Unit) {
            linkRepository.getLinksWithTags().collect { result ->
                result.onSuccess { rows -> state = state.copy(links = rows.map { it.toDomain() }, isLoading = false) }
                    .onFailure { state = state.copy(isLoading = false) }
            }
        }
        LaunchedEffect(Unit) {
            categoryRepository.getCategories().collect { result ->
                result.onSuccess { rows -> state = state.copy(categories = rows.map { it.toCategory() }) }
            }
        }
        LaunchedEffect(Unit) {
            categoryRepository.getLinkCategoryIds().collect { result ->
                result.onSuccess { state = state.copy(linkCategoryIds = it) }
            }
        }
        LaunchedEffect(Unit) {
            // The links flow re-emits after each write, so these only write
            events.collect { event ->
                when (event) {
                    is SavedListEvent.Opened -> linkRepository.markOpened(event.linkId).first()
                    is SavedListEvent.Archive -> state.links.firstOrNull { it.id == event.linkId }
                        ?.let { linkRepository.updateLink(it.copy(isArchived = true).toEntity()).first() }
                    is SavedListEvent.Delete -> linkRepository.deleteLink(event.linkId).first()
                    is SavedListEvent.Restore -> linkRepository.updateLink(event.link.toEntity()).first()
                }
            }
        }
        return state
    }

    override fun initialValue(): SavedListState = SavedListState()
}
