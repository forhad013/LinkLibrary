package com.greenrobotdev.lordofthelinks.screens.home

import androidx.compose.runtime.Composable
import com.greenrobotdev.lordofthelinks.model.repository.LinkRepository
import com.greenrobotdev.lordofthelinks.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel : MoleculeViewModel<HomeEvent, HomeState>(), KoinComponent {

    private val linkRepository: LinkRepository by inject()

    
    @Composable
    override fun models(events: Flow<HomeEvent>): HomeState {
        return HomePresenter(initialState = HomeState(), events, linkRepository)
    }

    
    override fun initialValue(): HomeState {
        return HomeState()
    }

    fun onRefresh() { take(HomeEvent.Refresh) }
    fun onToggleFavorite(linkId: String) { take(HomeEvent.ToggleFavorite(linkId)) }
}
