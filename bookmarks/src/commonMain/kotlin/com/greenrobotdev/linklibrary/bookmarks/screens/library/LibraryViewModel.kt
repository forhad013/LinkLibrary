package com.greenrobotdev.linklibrary.bookmarks.screens.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.model.Link
import com.greenrobotdev.linklibrary.model.toDomain
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LibraryViewModel : MoleculeViewModel<LibraryEvent, LibraryState>(), KoinComponent {

    private val linkRepository: LinkRepository by inject()

    
    @Composable
    override fun models(events: Flow<LibraryEvent>): LibraryState {
        return LibraryPresenter(events, linkRepository)
    }

    
    override fun initialValue(): LibraryState {
        return LibraryState()
    }
}

/**
 * Presenter function that processes events and produces the library state.
 * This is where the business logic lives - separate from the ViewModel.
 */

@Composable
fun LibraryPresenter(
    events: Flow<LibraryEvent>,
    linkRepository: LinkRepository
): LibraryState {
    var state by remember { mutableStateOf(LibraryState()) }
    val links = remember { mutableStateListOf<Link>() }

    // Load links reactively
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

    // Handle events
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is LibraryEvent.SearchChanged -> {
                    state = state.copy(
                        searchQuery = event.query,
                        filteredLinks = filterLinks(event.query, links.toList())
                    )
                }
            }
        }
    }

    return state
}

private fun filterLinks(
    query: String,
    allLinks: List<Link>
): List<Link> {
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
