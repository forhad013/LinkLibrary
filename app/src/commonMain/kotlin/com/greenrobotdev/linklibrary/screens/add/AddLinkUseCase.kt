package com.greenrobotdev.linklibrary.screens.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.data.MetadataFetchService
import com.greenrobotdev.linklibrary.database.repository.CollectionRepository
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.database.repository.TagRepository
import com.greenrobotdev.linklibrary.model.Link
import com.greenrobotdev.linklibrary.model.toCollection
import com.greenrobotdev.linklibrary.model.toEntity
import com.greenrobotdev.linklibrary.model.toTag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun AddLinkUseCase(
    initialState: AddLinkState,
    events: Flow<AddLinkEvent>,
    linkRepository: LinkRepository,
    tagRepository: TagRepository = koinInject(),
    collectionRepository: CollectionRepository = koinInject(),
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

    // Load available tags and collections reactively
    LaunchedEffect(Unit) {
        // Subscribe to tags changes reactively
        tagRepository.getTags().collect { result ->
            val tags = result.getOrElse { emptyList() }.map { it.toTag() }
            state = state.copy(
                availableTags = tags,
                isLoadingTagsAndCollections = false
            )
        }
    }

    LaunchedEffect(Unit) {
        // Subscribe to collections changes reactively
        collectionRepository.getCollections().collect { result ->
            val collections = result.getOrElse { emptyList() }.map { it.toCollection() }
            state = state.copy(
                availableCollections = collections,
                isLoadingTagsAndCollections = false
            )
        }
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is AddLinkEvent.TitleChanged -> {
                    state = state.copy(
                        title = event.title,
                        isFormValid = isFormValid(state.url, event.title, state.description)
                    )
                }
                is AddLinkEvent.UrlChanged -> {
                    state = state.copy(
                        url = event.url,
                        isFormValid = isFormValid(event.url, state.title, state.description)
                    )
                }
                is AddLinkEvent.DescriptionChanged -> {
                    state = state.copy(
                        description = event.description,
                        isFormValid = isFormValid(state.url, state.title, event.description)
                    )
                }
                is AddLinkEvent.ToggleFavorite -> {
                    state = state.copy(isFavorite = event.isFavorite)
                }
                is AddLinkEvent.Submit -> {
                    if (state.isFormValid) {
                        state = state.copy(isLoading = true, error = null)
                        try {
                            val link = Link(
                                id = Uuid.random().toString(),
                                title = state.title.ifBlank { state.url },
                                url = state.url,
                                description = state.description,
                                isFavorite = state.isFavorite,
                                createdAt = System.currentTimeMillis()
                            )
                            val result = linkRepository.addLink(link.toEntity())

                            // After successfully adding the link, assign tags and collections
                            val linkId = link.id
                            try {
                                // Assign selected tags
                                state.selectedTags.forEach { tagId ->
                                    tagRepository.assignTagToLink(linkId, tagId).first()
                                }

                                // Assign selected collections
                                state.selectedCollections.forEach { collectionId ->
                                    collectionRepository.assignLinkToCollection(linkId, collectionId).first()
                                }
                            } catch (e: Exception) {
                                // Log error but don't fail the entire operation
                                println("Error assigning tags/collections: ${e.message}")
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
                // Auto-fetch event handlers
                is AddLinkEvent.FetchMetadata -> {
                    if (state.url.isNotBlank() && !state.isFetching) {
                        state = state.copy(isFetching = true, fetchError = null)

                        try {
                            val result = metadataFetchService.fetchMetadata(state.url).first()
                            result.fold(
                                onSuccess = { metadata ->
                                    // Auto-fill title and description from metadata
                                    val autoTitle = if (metadata.title.isNotBlank()) metadata.title else {
                                        // Fallback: generate title from URL
                                        extractTitleFromUrl(state.url)
                                    }
                                    state = state.copy(
                                        isFetching = false,
                                        fetchedMetadata = metadata,
                                        title = if (metadata.title.isNotBlank()) metadata.title else autoTitle,
                                        description = if (metadata.description.isNotBlank()) metadata.description else state.description,
                                        isFormValid = isFormValid(state.url, autoTitle, if (metadata.description.isNotBlank()) metadata.description else state.description)
                                    )
                                },
                                onFailure = { error ->
                                    // Provide fallback title even on failure
                                    val fallbackTitle = extractTitleFromUrl(state.url)
                                    state = state.copy(
                                        isFetching = false,
                                        fetchError = error.message ?: "Failed to fetch metadata. Using fallback title.",
                                        title = fallbackTitle,
                                        isFormValid = isFormValid(state.url, fallbackTitle, state.description)
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
                                isFormValid = isFormValid(state.url, fallbackTitle, state.description)
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
                        title = if (event.metadata.title.isNotBlank()) event.metadata.title else state.title,
                        description = if (event.metadata.description.isNotBlank()) event.metadata.description else state.description,
                        isFormValid = isFormValid(state.url, if (event.metadata.title.isNotBlank()) event.metadata.title else state.title, if (event.metadata.description.isNotBlank()) event.metadata.description else state.description)
                    )
                }
                is AddLinkEvent.MetadataFetchError -> {
                    state = state.copy(
                        fetchError = event.error
                    )
                }
                // Tag and Collection selection handlers
                is AddLinkEvent.LoadAvailableTagsAndCollections -> {
                    // No longer needed - tags and collections are now reactive
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
                is AddLinkEvent.ToggleCollection -> {
                    val newSelectedCollections = if (state.selectedCollections.contains(event.collectionId)) {
                        state.selectedCollections - event.collectionId
                    } else {
                        state.selectedCollections + event.collectionId
                    }
                    state = state.copy(selectedCollections = newSelectedCollections)
                }
            }
        }
    }

    return state
}

private fun isFormValid(url: String, title: String, description: String): Boolean {
    return url.isNotBlank() && (title.isNotBlank() || description.isNotBlank())
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
