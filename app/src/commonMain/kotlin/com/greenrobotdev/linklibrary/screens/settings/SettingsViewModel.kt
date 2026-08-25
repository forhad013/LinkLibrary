package com.greenrobotdev.linklibrary.screens.settings

import androidx.compose.runtime.Composable
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

/**
 * ViewModel for Settings screen
 * Follows MoleculeViewModel architecture pattern
 */
class SettingsViewModel : MoleculeViewModel<SettingsEvent, SettingsState>(), KoinComponent {

    
    @Composable
    override fun models(events: Flow<SettingsEvent>): SettingsState {
        return SettingsPresenter(initialState = SettingsState(), events)
    }

    
    override fun initialValue(): SettingsState {
        return SettingsState()
    }
}
