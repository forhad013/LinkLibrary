package com.greenrobotdev.linklibrary.bookmarks.screens.add

import com.greenrobotdev.linklibrary.design.debug.DebugLog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.bookmarks.service.MetadataFetchService
import com.greenrobotdev.linklibrary.database.repository.CategoryRepository
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.database.repository.TagRepository
import com.greenrobotdev.linklibrary.bookmarks.model.Link
import com.greenrobotdev.linklibrary.bookmarks.model.toCategory
import com.greenrobotdev.linklibrary.bookmarks.model.toEntity
import com.greenrobotdev.linklibrary.bookmarks.model.Tag
import com.greenrobotdev.linklibrary.bookmarks.model.toTag
import com.greenrobotdev.linklibrary.bookmarks.domain.isSameLink
import com.greenrobotdev.linklibrary.bookmarks.domain.isValidLink
import com.greenrobotdev.linklibrary.bookmarks.domain.linkTypeOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Composable
fun AddLinkPresenter(
    initialState: AddLinkState,
    events: Flow<AddLinkEvent>,
    linkRepository: LinkRepository,
    tagRepository: TagRepository = koinInject(),
    categoryRepository: CategoryRepository = koinInject(),
    metadataFetchService: MetadataFetchService = koinInject(),
    initialUrl: String?
): AddLinkState {
    var state by remember { mutableStateOf(initialState) }

    // Set initial URL if provided
    LaunchedEffect(initialUrl) {
        initialUrl?.let { url ->
            state = state.copy(url = url, isFormValid = url.isNotBlank())
        }
    }

    // Load available tags and categoryNames reactively
    // These flows will continuously listen to database changes
    LaunchedEffect(Unit) {
        // Subscribe to tags changes reactively
        tagRepository.getTagsWithCounts().collect { result ->
            val tags = result.getOrElse { emptyList() }.map { it.toTag() }
            state = state.copy(
                availableTags = tags,
                isLoadingTagsAndCategories = false
            )
        }
    }

    LaunchedEffect(Unit) {
        // Subscribe to categoryNames changes reactively
        categoryRepository.getCategories().collect { result ->
            val categoryNames = result.getOrElse { emptyList() }.map { it.toCategory() }
            state = state.copy(
                availableCategories = categoryNames,
                isLoadingTagsAndCategories = false
            )
        }
    }
    LaunchedEffect(Unit) {
        categoryRepository.getRecentCategories().first()
            .onSuccess { rows -> state = state.copy(recentCategories = rows.map { it.toCategory() }) }
    }

    // Builds the stored row from the form; notes go in description, the page text in fetchedDescription
    fun linkFromState(id: String) = Link(
        id = id,
        title = state.title.ifBlank { extractTitleFromUrl(state.url) },
        url = state.url.trim(),
        description = state.notes.orEmpty(),
        isFavorite = state.isFavorite,
        type = linkTypeOf(state.url),
        fetchedDescription = state.fetchedMetadata?.description?.takeIf { it.isNotBlank() },
        createdAt = state.createdAt,
        imageUrl = state.fetchedMetadata?.let { it.imageUrl.orEmpty() }, // "": fetched, no image
    ).toEntity()

    fun stampCreatedAt() {
        if (state.createdAt == null) state = state.copy(createdAt = Clock.System.now().toEpochMilliseconds())
    }

    // The existing save of [url], if any: adding it again would only make a second copy
    suspend fun existingSave(url: String): SavedDuplicate? =
        linkRepository.getLinks().first().getOrNull()
            ?.firstOrNull { isSameLink(it.url, url) }
            ?.let { SavedDuplicate(it.id, it.title.ifBlank { it.url }, it.createdAt) }

    LaunchedEffect(Unit) {
        // "Link kept": store the link before handling any event, so Save/Discard always see savedLinkId
        val url = initialUrl?.trim().orEmpty()
        val duplicate = if (isValidLink(url)) existingSave(url) else null
        if (duplicate != null) state = state.copy(duplicate = duplicate)
        if (duplicate == null && state.savedLinkId == null && isValidLink(url)) {
            stampCreatedAt()
            linkRepository.addLink(linkFromState(Uuid.random().toString())).first()
                .onSuccess { state = state.copy(savedLinkId = it.id) }
                .onFailure { state = state.copy(error = it.message ?: "Could not save link") }
        }
        events.collect { event ->
            when (event) {
                is AddLinkEvent.TitleChanged -> {
                    state = state.copy(
                        title = event.title,
                        isFormValid = isFormValid(state.url, event.title)
                    )
                }
                is AddLinkEvent.UrlChanged -> {
                    state = state.copy(
                        url = event.url,
                        isFormValid = isFormValid(event.url, state.title,)
                    )
                }
                is AddLinkEvent.ToggleFavorite -> {
                    state = state.copy(isFavorite = event.isFavorite)
                }
                is AddLinkEvent.Submit -> {
                    // title may still be blank while fetching; linkFromState falls back to one from the URL
                    // A typed URL that's already saved: show that save instead of adding a copy
                    val typedDuplicate = if (state.savedLinkId == null && state.url.isNotBlank()) existingSave(state.url) else null
                    if (typedDuplicate != null) {
                        state = state.copy(duplicate = typedDuplicate)
                    } else if (state.url.isNotBlank() && !state.isLoading) {
                        state = state.copy(isLoading = true, error = null)
                        try {
                            stampCreatedAt()
                            val savedId = state.savedLinkId
                            // must collect: these wrap an eager write whose failure lands in the Result
                            val saved = (if (savedId != null) linkRepository.updateLink(linkFromState(savedId))
                            else linkRepository.addLink(linkFromState(Uuid.random().toString()))).first()
                                .getOrElse { e ->
                                    state = state.copy(isLoading = false, error = e.message ?: "Failed to add link")
                                    return@collect
                                }

                            // After successfully adding the link, assign tags and categoryNames
                            val linkId = saved.id
                            try {
                                // Assign selected tags
                                state.selectedTags.forEach { tagId ->
                                    tagRepository.assignTagToLink(linkId, tagId).first()
                                }

                                // Assign selected categoryNames
                                state.selectedCategoryIds.forEach { categoryId ->
                                    categoryRepository.assignLinkToCategory(linkId, categoryId).first()
                                }
                            } catch (e: Exception) {
                                // Log error but don't fail the entire operation
                                DebugLog.d("AddLink") { "Error assigning tags/categoryNames: ${e.message}" }
                            }

                            // Set success flag after successful submission
                            state = state.copy(
                                isLoading = false,
                                success = true
                            )
                        } catch (e: Exception) {
                            state = state.copy(
                                isLoading = false,
                                error = e.message ?: "Failed to add link"
                            )
                        }
                    }
                }
                is AddLinkEvent.ClearError -> {
                    state = state.copy(error = null)
                }
                is AddLinkEvent.Consumed -> {
                    state = state.copy(success = false)
                }
                // Auto-fetch event handlers
                is AddLinkEvent.FetchMetadata -> {
                    // The screen can ask more than once while it opens; one fetch per URL is enough
                    val alreadyFetched = state.fetchedMetadata?.url == state.url
                    if (state.url.isNotBlank() && !state.isFetching && !alreadyFetched && state.duplicate == null) {
                        state = state.copy(isFetching = true, fetchError = null)

                        try {
                            val result = metadataFetchService.fetchMetadata(state.url).first()
                            result.fold(
                                onSuccess = { metadata ->
                                    // Auto-fill title from metadata (description removed)
                                    val autoTitle = metadata.title.ifBlank {
                                        // Fallback: generate title from URL
                                        extractTitleFromUrl(state.url)
                                    }
                                    state = state.copy(
                                        isFetching = false,
                                        fetchedMetadata = metadata,
                                        title = if (metadata.title.isNotBlank()) metadata.title else autoTitle,
                                        isFormValid = isFormValid(state.url, autoTitle)
                                    )
                                    // keep the stored link's title/description current even if the user never taps Save
                                    state.savedLinkId?.let { linkRepository.updateLink(linkFromState(it)).first() }
                                },
                                onFailure = { error ->
                                    // Provide fallback title even on failure
                                    val fallbackTitle = extractTitleFromUrl(state.url)
                                    state = state.copy(
                                        isFetching = false,
                                        fetchError = error.message ?: "Failed to fetch metadata. Using fallback title.",
                                        title = fallbackTitle,
                                        isFormValid = isFormValid(state.url, fallbackTitle)
                                    )
                                }
                            )
                        } catch (e: Exception) {
                            // Provide fallback title even on exception
                            val fallbackTitle = extractTitleFromUrl(state.url)
                            state = state.copy(
                                isFetching = false,
                                fetchError = e.message ?: "Failed to fetch metadata. Using fallback title.",
                                title = fallbackTitle,
                                isFormValid = isFormValid(state.url, fallbackTitle)
                            )
                        }
                    }
                }
                is AddLinkEvent.ClearFetchError -> {
                    state = state.copy(fetchError = null)
                }
                is AddLinkEvent.MetadataFetchResult -> {
                    // Handle successful metadata fetch
                    state = state.copy(
                        fetchedMetadata = event.metadata,
                        title = event.metadata.title.ifBlank { state.title },
                        isFormValid = isFormValid(state.url,
                            event.metadata.title.ifBlank { state.title })
                    )
                }
                is AddLinkEvent.MetadataFetchError -> {
                    state = state.copy(
                        fetchError = event.error
                    )
                }
                // Tag and Category selection handlers
                is AddLinkEvent.LoadAvailableTagsAndCategories -> {
                    // No longer needed - tags and categoryNames are now reactive
                    // This event is kept for backwards compatibility but does nothing
                }
                is AddLinkEvent.ToggleTag -> {
                    val newSelectedTags = if (state.selectedTags.contains(event.tagId)) {
                        state.selectedTags - event.tagId
                    } else {
                        state.selectedTags + event.tagId
                    }
                    state = state.copy(selectedTags = newSelectedTags)
                }
                is AddLinkEvent.ToggleCategory -> {
                    val newSelectedCategories = if (state.selectedCategoryIds.contains(event.categoryId)) {
                        state.selectedCategoryIds - event.categoryId
                    } else {
                        state.selectedCategoryIds + event.categoryId
                    }
                    state = state.copy(selectedCategoryIds = newSelectedCategories)
                }
                is AddLinkEvent.SetTags -> state = state.copy(selectedTags = event.tagIds)
                is AddLinkEvent.AddTagByName -> {
                    val name = event.name.trim().removePrefix("#").trim()
                    if (name.isNotEmpty()) {
                        val existing = state.availableTags.firstOrNull { it.name.equals(name, ignoreCase = true) }
                        if (existing != null) {
                            state = state.copy(selectedTags = state.selectedTags + existing.id)
                        } else {
                            tagRepository.addTag(Tag(id = "", name = name, count = 0).toEntity()).first().fold(
                                onSuccess = { created -> state = state.copy(selectedTags = state.selectedTags + created.id) },
                                onFailure = { state = state.copy(error = it.message ?: "Could not create tag") },
                            )
                        }
                    }
                }
                is AddLinkEvent.Discard -> {
                    val id = state.savedLinkId
                    val failure = id?.let { linkRepository.deleteLink(it).first().exceptionOrNull() }
                    state = if (failure != null) state.copy(error = failure.message ?: "Could not discard link")
                    else state.copy(savedLinkId = null, success = true)
                }
                is AddLinkEvent.SetCategory -> {
                    // Single selection mode: set exactly one category (or none)
                    state = state.copy(selectedCategoryIds = if (event.categoryId != null) {
                        setOf(event.categoryId)
                    } else {
                        emptySet()
                    })
                }
                is AddLinkEvent.ClearAllCategories -> {
                    // Clear all selected categoryNames
                    state = state.copy(selectedCategoryIds = emptySet())
                }
                // Task-related event handlers
                is AddLinkEvent.ToggleTask -> {
                    state = state.copy(isTask = event.isTask)
                }
                is AddLinkEvent.SetTaskPriority -> {
                    state = state.copy(taskPriority = event.priority)
                }
                is AddLinkEvent.SetDueTime -> {
                    state = state.copy(dueTime = event.time)
                }
                // Notes event handler
                is AddLinkEvent.NotesChanged -> {
                    state = state.copy(notes = event.notes)
                }
            }
        }
    }

    return state
}

private fun isFormValid(url: String, title: String): Boolean {
    return url.isNotBlank() && title.isNotBlank()
}

/**
 * Extracts a readable title from URL when metadata fetch fails
 * Falls back to generating a title from the hostname and path
 */
private fun extractTitleFromUrl(url: String): String {
    return try {
        val cleanUrl = url.removePrefix("https://").removePrefix("http://").removePrefix("www.")
        val parts = cleanUrl.split("/")

        val host = parts.firstOrNull() ?: "Unknown Site"
        val path = parts.drop(1).lastOrNull()

        if (path.isNullOrBlank() || path.isEmpty()) {
            host.replaceFirstChar { it.uppercaseChar() }
        } else {
            // Convert path to readable title (e.g., "my-article" -> "My Article")
            val readablePath = path.split("-")
                .joinToString(" ") { word ->
                    if (word.isNotEmpty()) word.replaceFirstChar { it.uppercaseChar() } else ""
                }
            "$host - $readablePath"
        }
    } catch (e: Exception) {
        // Final fallback: use part of URL
        try {
            val cleanUrl = url.removePrefix("https://").removePrefix("http://").removePrefix("www.")
            val domainAndPath = cleanUrl.split("/").take(2).joinToString(" - ") { part ->
                if (part.isNotEmpty()) part.replaceFirstChar { it.uppercaseChar() } else ""
            }
            domainAndPath
        } catch (e: Exception) {
            "Link from ${url.take(30)}..."
        }
    }
}
