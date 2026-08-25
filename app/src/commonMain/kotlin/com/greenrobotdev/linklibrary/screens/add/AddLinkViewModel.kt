package com.greenrobotdev.linklibrary.screens.add

import androidx.compose.runtime.Composable
import com.greenrobotdev.linklibrary.data.MetadataFetchService
import com.greenrobotdev.linklibrary.database.repository.CollectionRepository
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.database.repository.TagRepository
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddLinkViewModel(
    private val initialUrl: String? = null
) : MoleculeViewModel<AddLinkEvent, AddLinkState>(), KoinComponent {

    private val linkRepository: LinkRepository by inject()
    private val tagRepository: TagRepository by inject()
    private val collectionRepository: CollectionRepository by inject()
    private val metadataFetchService: MetadataFetchService by inject()

    
    @Composable
    override fun models(events: Flow<AddLinkEvent>): AddLinkState {
        return AddLinkPresenter(
            initialState = AddLinkState(url = initialUrl.orEmpty()),
            events = events,
            linkRepository = linkRepository,
            tagRepository = tagRepository,
            collectionRepository = collectionRepository,
            metadataFetchService = metadataFetchService,
            initialUrl = initialUrl
        )
    }

    
    override fun initialValue(): AddLinkState {
        return AddLinkState()
    }
}
