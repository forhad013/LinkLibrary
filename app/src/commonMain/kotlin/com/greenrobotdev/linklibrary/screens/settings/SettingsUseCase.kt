package com.greenrobotdev.linklibrary.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.utils.preferences.ThemePreferenceManager
import com.greenrobotdev.linklibrary.bookmarks.backup.BackupReadException
import com.greenrobotdev.linklibrary.bookmarks.backup.LinksBackupService
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/** Settings presenter: the saved theme, and saving a new one. */
@OptIn(ExperimentalTime::class)
@Composable
fun SettingsPresenter(
    events: Flow<SettingsEvent>,
    backupService: LinksBackupService,
): SettingsState {
    val themePreferences = remember { ThemePreferenceManager() }
    var state by remember {
        mutableStateOf(
            SettingsState(
                themeMode = when (themePreferences.getThemeMode()) {
                    is ThemePreferenceManager.ThemeMode.Light -> ThemeMode.Light
                    is ThemePreferenceManager.ThemeMode.Dark -> ThemeMode.Dark
                    is ThemePreferenceManager.ThemeMode.System -> ThemeMode.System
                },
            ),
        )
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is SettingsEvent.SetThemeMode -> {
                    themePreferences.setThemeMode(
                        when (event.themeMode) {
                            is ThemeMode.Light -> ThemePreferenceManager.ThemeMode.Light
                            is ThemeMode.Dark -> ThemePreferenceManager.ThemeMode.Dark
                            is ThemeMode.System -> ThemePreferenceManager.ThemeMode.System
                        },
                    )
                    state = state.copy(themeMode = event.themeMode)
                }
                SettingsEvent.Backup, SettingsEvent.ExportForBrowsers -> {
                    state = state.copy(isWorking = true, dataMessage = null)
                    state = runCatching {
                        if (event == SettingsEvent.Backup) backupService.backup(Clock.System.now().toEpochMilliseconds())
                        else backupService.browserExport()
                    }.fold(
                        onSuccess = { state.copy(pendingFile = it, isWorking = false) },
                        onFailure = { state.copy(isWorking = false, dataMessage = "Couldn't read your links. Nothing was saved.") },
                    )
                }
                is SettingsEvent.FileSaved -> state = state.copy(
                    pendingFile = null,
                    dataMessage = when {
                        !event.saved -> null
                        event.isBackup -> "Backed up ${event.linkCount} links."
                        else -> "Exported ${event.linkCount} links for browsers."
                    },
                )
                is SettingsEvent.Restore -> {
                    val text = event.text ?: return@collect
                    state = state.copy(isWorking = true, dataMessage = null)
                    val message = runCatching { backupService.restore(text) }.fold(
                        onSuccess = { r ->
                            buildList {
                                add(if (r.added == 1) "Added 1 link" else "Added ${r.added} links")
                                if (r.skipped > 0) add("skipped ${r.skipped} already saved")
                                if (r.categoriesCreated > 0) add("created ${r.categoriesCreated} categories")
                                if (r.tagsCreated > 0) add("created ${r.tagsCreated} tags")
                            }.joinToString(", ") + "."
                        },
                        onFailure = { (it as? BackupReadException)?.message ?: "Couldn't restore this backup. Nothing was changed." },
                    )
                    state = state.copy(isWorking = false, dataMessage = message)
                }
            }
        }
    }
    return state
}
