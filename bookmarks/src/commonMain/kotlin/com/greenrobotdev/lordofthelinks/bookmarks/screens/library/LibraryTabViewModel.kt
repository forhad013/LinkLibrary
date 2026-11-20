package com.greenrobotdev.lordofthelinks.bookmarks.screens.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.lordofthelinks.model.Category
import com.greenrobotdev.lordofthelinks.model.Tag
import com.greenrobotdev.lordofthelinks.model.repository.CategoryRepository
import com.greenrobotdev.lordofthelinks.model.repository.TagRepository
import com.greenrobotdev.lordofthelinks.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class LibraryTabState(
    val unopenedCount: Int = 0,
    val favoritesCount: Int = 0,
    val archivedCount: Int = 0,
    val uncategorizedCount: Int = 0,
    val categories: List<Category> = emptyList(),
    val tags: List<Tag> = emptyList()
)

class LibraryTabViewModel : MoleculeViewModel<LibraryTabEvent, LibraryTabState>(), KoinComponent {

    private val categoryRepository: CategoryRepository by inject()
    private val tagRepository: TagRepository by inject()

    @Composable
    override fun models(events: Flow<LibraryTabEvent>): LibraryTabState {
        var state by remember { mutableStateOf(LibraryTabState()) }

        // shortcut: direct DAO count flows would avoid the Flow-of-Result wrapper; fine at this size
        LaunchedEffect(Unit) {
            categoryRepository.getSmartListCounts().collect { result ->
                result.onSuccess { state = state.copy(
                    unopenedCount = it.unopened,
                    favoritesCount = it.favorites,
                    archivedCount = it.archived,
                    uncategorizedCount = it.uncategorized
                ) }
            }
        }
        LaunchedEffect(Unit) {
            categoryRepository.getCategoriesWithCounts().collect { result ->
                result.onSuccess { rows -> state = state.copy(categories = rows) }
            }
        }
        LaunchedEffect(Unit) {
            tagRepository.getTags().collect { result ->
                result.onSuccess { tags -> state = state.copy(tags = tags) }
            }
        }

        return state
    }

    override fun initialValue(): LibraryTabState = LibraryTabState()
}

sealed interface LibraryTabEvent
