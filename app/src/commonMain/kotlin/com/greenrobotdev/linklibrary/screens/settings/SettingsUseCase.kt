package com.greenrobotdev.linklibrary.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.utils.preferences.ThemePreferenceManager
import com.greenrobotdev.linklibrary.bookmarks.export.bookmarksHtml
import com.greenrobotdev.linklibrary.bookmarks.model.toCategory
import com.greenrobotdev.linklibrary.bookmarks.model.toDomain
import com.greenrobotdev.linklibrary.database.repository.CategoryRepository
import com.greenrobotdev.linklibrary.database.repository.LinkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/** Settings presenter: the saved theme, and saving a new one. */
@Composable
fun SettingsPresenter(
    events: Flow<SettingsEvent>,
    linkRepository: LinkRepository,
    categoryRepository: CategoryRepository,
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
                SettingsEvent.Export -> {
                    val links = linkRepository.getLinksWithTags().first().getOrNull()
                    val categories = categoryRepository.getCategories().first().getOrNull()
                    val linkCategoryIds = categoryRepository.getLinkCategoryIds().first().getOrNull()
                    state = if (links == null || categories == null || linkCategoryIds == null) {
                        state.copy(exportMessage = "Couldn't read your links. Nothing was exported.")
                    } else {
                        val html = bookmarksHtml(links.map { it.toDomain() }, categories.map { it.toCategory() }, linkCategoryIds)
                        state.copy(exportFile = ExportFile("lords-of-links-bookmarks.html", html, links.size), exportMessage = null)
                    }
                }
                is SettingsEvent.ExportFinished -> state = state.copy(
                    exportFile = null,
                    exportMessage = if (event.saved) "Exported ${event.linkCount} links." else null,
                )
            }
        }
    }
    return state
}
