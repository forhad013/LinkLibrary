package com.greenrobotdev.linklibrary.bookmarks.screens.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.bookmarks.model.toCategory
import com.greenrobotdev.linklibrary.bookmarks.model.toDomain
import com.greenrobotdev.linklibrary.bookmarks.model.toTag
import com.greenrobotdev.linklibrary.database.repository.CategoryRepository
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.database.repository.TagRepository
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LibraryViewModel : MoleculeViewModel<LibraryEvent, LibraryState>(), KoinComponent {

    private val linkRepository: LinkRepository by inject()
    private val categoryRepository: CategoryRepository by inject()
    private val tagRepository: TagRepository by inject()

    @Composable
    override fun models(events: Flow<LibraryEvent>): LibraryState =
        LibraryPresenter(events, linkRepository, categoryRepository, tagRepository)

    override fun initialValue(): LibraryState = LibraryState()
}

/** Feed presenter: four live sources (links, categories, link→category, tags) plus filter events. */
@Composable
fun LibraryPresenter(
    events: Flow<LibraryEvent>,
    linkRepository: LinkRepository,
    categoryRepository: CategoryRepository,
    tagRepository: TagRepository,
): LibraryState {
    var state by remember { mutableStateOf(LibraryState()) }

    LaunchedEffect(Unit) {
        linkRepository.getLinksWithTags().collect { result ->
            result.onSuccess { entities -> state = state.copy(links = entities.map { it.toDomain() }, isLoading = false) }
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
        tagRepository.getTagsWithCounts().collect { result ->
            result.onSuccess { rows -> state = state.copy(tags = rows.map { it.toTag() }) }
        }
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            state = when (event) {
                is LibraryEvent.SearchChanged -> state.copy(searchQuery = event.query)
                is LibraryEvent.ApplyFilters -> state.copy(filters = event.filters)
                is LibraryEvent.RemoveCategory -> state.copy(filters = state.filters.copy(categoryIds = state.filters.categoryIds - event.categoryId))
                is LibraryEvent.RemoveTag -> state.copy(filters = state.filters.copy(tagIds = state.filters.tagIds - event.tagId))
                LibraryEvent.ClearFilters -> state.copy(searchQuery = "", filters = FeedFilters())
                LibraryEvent.ToggleSort -> state.copy(newestFirst = !state.newestFirst)
                is LibraryEvent.Opened -> {
                    // the links flow re-emits with isOpened = true; nothing to change here
                    linkRepository.markOpened(event.linkId).first()
                    state
                }
            }
        }
    }

    return state
}
