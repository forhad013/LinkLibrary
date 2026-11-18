package com.greenrobotdev.linklibrary.bookmarks.screens.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.bookmarks.model.toCategory
import com.greenrobotdev.linklibrary.bookmarks.model.toDomain
import com.greenrobotdev.linklibrary.bookmarks.model.toEntity
import com.greenrobotdev.linklibrary.bookmarks.model.toTag
import com.greenrobotdev.linklibrary.bookmarks.service.MetadataFetchService
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
    private val metadataFetchService: MetadataFetchService by inject()
    private val filterStore = FeedFilterStore()

    @Composable
    override fun models(events: Flow<LibraryEvent>): LibraryState =
        LibraryPresenter(events, linkRepository, categoryRepository, tagRepository, filterStore, metadataFetchService)

    override fun initialValue(): LibraryState = LibraryState()
}

/** Feed presenter: four live sources (links, categories, link→category, tags) plus filter events. */
@Composable
fun LibraryPresenter(
    events: Flow<LibraryEvent>,
    linkRepository: LinkRepository,
    categoryRepository: CategoryRepository,
    tagRepository: TagRepository,
    filterStore: FeedFilterStore,
    metadataFetchService: MetadataFetchService,
): LibraryState {
    var state by remember { mutableStateOf(LibraryState(filters = filterStore.load())) }

    // Saved on every change, so the Feed reopens with the same filters after the app is closed
    LaunchedEffect(state.filters) { filterStore.save(state.filters) }

    LaunchedEffect(Unit) {
        linkRepository.getLinksWithTags().collect { result ->
            result.onSuccess { entities -> state = state.copy(links = entities.map { it.toDomain() }, isLoading = false) }
                .onFailure { state = state.copy(isLoading = false) }
        }
    }
    // Preview images for links saved before they were kept: one page at a time, in the background.
    // A failed fetch (offline, blocked) stays null and is retried next time.
    LaunchedEffect(Unit) {
        val missing = linkRepository.getLinks().first().getOrNull().orEmpty().filter { it.imageUrl == null }
        for (link in missing) {
            metadataFetchService.fetchMetadata(link.url).first().onSuccess { metadata ->
                linkRepository.setImageUrl(link.id, metadata.imageUrl.orEmpty()).first()
            }
        }
    }
    LaunchedEffect(Unit) {
        categoryRepository.getCategories().collect { result ->
            result.onSuccess { rows ->
                // Drop picks whose category was deleted, or the Feed would stay empty with no chip to remove
                val categories = rows.map { it.toCategory() }
                state = state.copy(categories = categories, filters = state.filters.copy(categoryIds = state.filters.categoryIds.filterTo(mutableSetOf()) { id -> categories.any { it.id == id } }))
            }
        }
    }
    LaunchedEffect(Unit) {
        categoryRepository.getLinkCategoryIds().collect { result ->
            result.onSuccess { state = state.copy(linkCategoryIds = it) }
        }
    }
    LaunchedEffect(Unit) {
        tagRepository.getTagsWithCounts().collect { result ->
            result.onSuccess { rows ->
                val tags = rows.map { it.toTag() }
                state = state.copy(tags = tags, filters = state.filters.copy(tagIds = state.filters.tagIds.filterTo(mutableSetOf()) { id -> tags.any { it.id == id } }))
            }
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
                is LibraryEvent.Archive -> {
                    // the links flow re-emits with isArchived = true, which hides it from the Feed
                    state.links.firstOrNull { it.id == event.linkId }
                        ?.let { linkRepository.updateLink(it.copy(isArchived = true).toEntity()).first() }
                    state
                }
                is LibraryEvent.Delete -> {
                    // the links flow re-emits without it; tags and category rows go with it (FK cascade)
                    linkRepository.deleteLink(event.linkId).first()
                    state
                }
                is LibraryEvent.Restore -> {
                    linkRepository.updateLink(event.link.toEntity()).first()
                    state
                }
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
