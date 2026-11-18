package com.greenrobotdev.linklibrary.screens.settings

import androidx.compose.runtime.Composable
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import com.greenrobotdev.linklibrary.database.repository.CategoryRepository
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * ViewModel for Settings screen
 * Follows MoleculeViewModel architecture pattern
 */
class SettingsViewModel : MoleculeViewModel<SettingsEvent, SettingsState>(), KoinComponent {

    private val linkRepository: LinkRepository by inject()
    private val categoryRepository: CategoryRepository by inject()

    @Composable
    override fun models(events: Flow<SettingsEvent>): SettingsState {
        return SettingsPresenter(events, linkRepository, categoryRepository)
    }

    
    override fun initialValue(): SettingsState {
        return SettingsState()
    }
}
