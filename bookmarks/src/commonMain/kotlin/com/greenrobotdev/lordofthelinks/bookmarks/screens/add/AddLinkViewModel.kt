package com.greenrobotdev.lordofthelinks.bookmarks.screens.add

import androidx.compose.runtime.Composable
import com.greenrobotdev.lordofthelinks.bookmarks.service.MetadataFetchService
import com.greenrobotdev.lordofthelinks.database.repository.CategoryRepository
import com.greenrobotdev.lordofthelinks.database.repository.LinkRepository
import com.greenrobotdev.lordofthelinks.database.repository.TagRepository
import com.greenrobotdev.lordofthelinks.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddLinkViewModel(
    private val initialUrl: String? = null
) : MoleculeViewModel<AddLinkEvent, AddLinkState>(), KoinComponent {

    private val linkRepository: LinkRepository by inject()
    private val tagRepository: TagRepository by inject()
    private val categoryRepository: CategoryRepository by inject()
    private val metadataFetchService: MetadataFetchService by inject()

    
    @Composable
    override fun models(events: Flow<AddLinkEvent>): AddLinkState {
        return AddLinkPresenter(
            initialState = AddLinkState(url = initialUrl.orEmpty(), askForUrl = initialUrl.isNullOrBlank()),
            events = events,
            linkRepository = linkRepository,
            tagRepository = tagRepository,
            categoryRepository = categoryRepository,
            metadataFetchService = metadataFetchService,
            initialUrl = initialUrl
        )
    }

    
    override fun initialValue(): AddLinkState {
        return AddLinkState()
    }
}
