package com.greenrobotdev.linklibrary.bookmarks.screens.details

import androidx.compose.runtime.Composable
import com.greenrobotdev.linklibrary.database.repository.CategoryRepository
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.database.repository.TagRepository
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LinkDetailViewModel(
    private val linkId: String
) : MoleculeViewModel<LinkDetailEvent, LinkDetailState>(), KoinComponent {

    private val linkRepository: LinkRepository by inject()
    private val categoryRepository: CategoryRepository by inject()
    private val tagRepository: TagRepository by inject()

    @Composable
    override fun models(events: Flow<LinkDetailEvent>): LinkDetailState =
        LinkDetailPresenter(events, linkRepository, categoryRepository, tagRepository, linkId)

    override fun initialValue(): LinkDetailState = LinkDetailState()
}
