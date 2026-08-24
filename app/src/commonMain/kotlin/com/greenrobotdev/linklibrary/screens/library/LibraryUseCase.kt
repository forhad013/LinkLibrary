package com.greenrobotdev.linklibrary.screens.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.model.toDomain
import kotlinx.coroutines.flow.Flow

@Composable
fun LibraryUseCase(
    initialState: LibraryState,
    linkRepository: LinkRepository
): LibraryState {
    var state by remember { mutableStateOf(initialState) }
    val links = remember { mutableStateListOf<com.greenrobotdev.linklibrary.model.Link>() }

    // Load links on first composition using the new method that includes tags and collections
    LaunchedEffect(Unit) {
        linkRepository.getLinksWithTagsAndCollections().collect { result ->
            result.onSuccess { entities ->
                links.clear()
                links.addAll(entities.map { it.toDomain() })
                state = state.copy(
                    searchQuery = state.searchQuery,
                    links = links.toList(),
                    filteredLinks = filterLinks(state.searchQuery, links.toList())
                )
            }
        }
    }

    return state
}

private fun filterLinks(
    query: String,
    allLinks: List<com.greenrobotdev.linklibrary.model.Link>
): List<com.greenrobotdev.linklibrary.model.Link> {
    return if (query.isBlank()) {
        allLinks
    } else {
        allLinks.filter { link ->
            link.title.contains(query, ignoreCase = true) ||
                    link.url.contains(query, ignoreCase = true) ||
                    (link.description?.contains(query, ignoreCase = true) ?: false)
        }
    }
}
