package com.greenrobotdev.lordofthelinks.screens.settings

import com.greenrobotdev.lordofthelinks.design.theme.ComponentStyle
import com.greenrobotdev.lordofthelinks.design.components.button.TextButton
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.School
import com.greenrobotdev.lordofthelinks.design.debug.DebugLog
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Palette
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.lordofthelinks.design.components.button.StandardButton
import com.greenrobotdev.lordofthelinks.design.components.cards.SectionContentCard
import com.greenrobotdev.lordofthelinks.design.components.layout.appVerticalScroll
import com.greenrobotdev.lordofthelinks.design.foundation.icon.AppIcon

/**
 * Settings: only what works today. Appearance, Your data, About (opens the welcome tour, as does
 * How it works); debug builds add Developer (the design catalog).
 */
@Composable
fun SettingsScreen(
    routeKey: NavKey,
    onOpenDesignCatalog: () -> Unit = {},
    onOpenTour: () -> Unit = {},
) {
    val viewModel: SettingsViewModel = viewModel(key = routeKey.toString()) { SettingsViewModel() }
    val state by viewModel.models.collectAsState()

    // Backup / export: the presenter builds the file, the screen hands it to the system "save to" picker
    val pending = state.pendingFile
    val onSaved: (Boolean) -> Unit = { saved ->
        viewModel.take(SettingsEvent.FileSaved(saved, pending?.linkCount ?: 0, isBackup = pending?.mimeType == "application/json"))
    }
    val saveJson = rememberTextFileSaver("application/json", onSaved)
    val saveHtml = rememberTextFileSaver("text/html", onSaved)
    LaunchedEffect(pending) {
        pending?.let { (if (it.mimeType == "application/json") saveJson else saveHtml)(it.fileName, it.content) }
    }
    // Restore: the user picks a backup file; the presenter merges it
    val openBackup = rememberTextFileOpener { text -> viewModel.take(SettingsEvent.Restore(text)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .appVerticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            "Settings",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp).semantics { heading() },
        )

        SectionContentCard(title = "Appearance") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Theme", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                ThemeSelector(state.themeMode) { viewModel.take(SettingsEvent.SetThemeMode(it)) }
            }
        }

        SectionContentCard(title = "Your data") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Your links are stored only on this phone. Back them up to a file you keep, and restore " +
                        "them on this or another phone. Restoring adds what's missing and never removes anything.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                StandardButton(
                    text = "Back up links",
                    icon = Icons.Filled.FileDownload,
                    enabled = !state.isWorking,
                    onClick = { viewModel.take(SettingsEvent.Backup) },
                    modifier = Modifier.fillMaxWidth(),
                )
                StandardButton(
                    text = "Restore from backup",
                    icon = Icons.Filled.Restore,
                    style = ComponentStyle.Outlined,
                    enabled = !state.isWorking,
                    onClick = openBackup,
                    modifier = Modifier.fillMaxWidth(),
                )
                TextButton(
                    onClick = { viewModel.take(SettingsEvent.ExportForBrowsers) },
                    text = "Export for browsers (bookmarks file)",
                    enabled = !state.isWorking,
                )
                state.dataMessage?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }

        SectionContentCard(title = "About") {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Tapping About opens the tour too
                AboutRow(version = rememberAppVersion(), onClick = onOpenTour)
                NavRow(
                    icon = Icons.Filled.School,
                    title = "How it works",
                    subtitle = "A quick tour of saving, the Feed and organizing",
                    onClick = onOpenTour,
                )
            }
        }

        // Debug builds only (the flag the app sets for debuggable builds)
        if (DebugLog.enabled) {
            SectionContentCard(title = "Developer") {
                NavRow(
                    icon = Icons.Filled.Palette,
                    title = "Design catalog",
                    subtitle = "Every reusable component, in light and dark",
                    onClick = onOpenDesignCatalog,
                )
            }
        }
    }
}

private val THEME_OPTIONS: List<Triple<ThemeMode, String, ImageVector>> = listOf(
    Triple(ThemeMode.System, "System", Icons.Filled.BrightnessAuto),
    Triple(ThemeMode.Light, "Light", Icons.Filled.LightMode),
    Triple(ThemeMode.Dark, "Dark", Icons.Filled.DarkMode),
)

/** System / Light / Dark, chosen in place (no dialog). */
@Composable
private fun ThemeSelector(selected: ThemeMode, onSelect: (ThemeMode) -> Unit) {
    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
        THEME_OPTIONS.forEachIndexed { index, (mode, label, icon) ->
            SegmentedButton(
                selected = selected == mode,
                onClick = { onSelect(mode) },
                shape = SegmentedButtonDefaults.itemShape(index, THEME_OPTIONS.size),
                icon = { AppIcon(icon, contentDescription = null, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp)) },
                label = { Text(label) },
            )
        }
    }
}

@Composable
private fun AboutRow(version: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClickLabel = "Open the welcome tour", onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(Icons.Filled.Bookmarks, contentDescription = null, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Column(Modifier.weight(1f)) {
            Text("Lords of Links", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                "Save & Find. Your links stay on this phone.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                "Version $version",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun NavRow(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClickLabel = "Open $title", onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.tertiaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(icon, contentDescription = null, color = MaterialTheme.colorScheme.onTertiaryContainer)
        }
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        AppIcon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
