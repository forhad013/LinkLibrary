package com.greenrobotdev.lordofthelinks.screens.settings

import androidx.compose.runtime.Composable
import com.greenrobotdev.lordofthelinks.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import com.greenrobotdev.lordofthelinks.database.repository.CategoryRepository
import com.greenrobotdev.lordofthelinks.database.repository.LinkRepository
import com.greenrobotdev.lordofthelinks.database.repository.TagRepository
import com.greenrobotdev.lordofthelinks.bookmarks.backup.LinksBackupService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * ViewModel for Settings screen
 * Follows MoleculeViewModel architecture pattern
 */
class SettingsViewModel : MoleculeViewModel<SettingsEvent, SettingsState>(), KoinComponent {

    private val linkRepository: LinkRepository by inject()
    private val categoryRepository: CategoryRepository by inject()
    private val tagRepository: TagRepository by inject()
    private val backupService by lazy { LinksBackupService(linkRepository, categoryRepository, tagRepository) }

    @Composable
    override fun models(events: Flow<SettingsEvent>): SettingsState {
        return SettingsPresenter(events, backupService)
    }

    
    override fun initialValue(): SettingsState {
        return SettingsState()
    }
}
