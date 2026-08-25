package com.greenrobotdev.linklibrary.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import com.greenrobotdev.linklibrary.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun HomePresenter(
    initialState: HomeState,
    events: Flow<HomeEvent>,
    linkRepository: LinkRepository
): HomeState {

    var links by remember { mutableStateOf(initialState.links) }
    var isLoading by remember { mutableStateOf(initialState.isLoading) }
    var error by remember { mutableStateOf(initialState.error) }

    LaunchedEffect(Unit) {
        isLoading = true
        linkRepository.getLinks().collect { result ->
            isLoading = false
            result.onSuccess {
                // Show only recent 20 links, sorted by createdAt descending
                links = it.map { it.toDomain() }
                    .sortedByDescending { it.createdAt }
                    .take(20)
            }
            result.onFailure { error = it.message }
        }
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is HomeEvent.Refresh -> {
                    isLoading = true
                    linkRepository.getLinks().collect { result ->
                        isLoading = false
                        result.onSuccess {
                            // Show only recent 20 links, sorted by createdAt descending
                            links = it.map { it.toDomain() }
                                .sortedByDescending { entity -> entity.createdAt }
                                .take(20)
                        }
                        result.onFailure { error = it.message }
                    }
                }
                is HomeEvent.ToggleFavorite -> {
                    linkRepository.toggleFavorite(event.id).collect { result ->
                        result.onSuccess { updatedLink ->
                            val domainLink = updatedLink.toDomain()
                            links = links?.map { link ->
                                if (link.id == domainLink.id) domainLink else link
                            }
                        }
                        result.onFailure { error = it.message }
                    }
                }
            }
        }
    }
    
    return HomeState(
        isLoading = isLoading,
        error = error,
        links = links
    )
}
