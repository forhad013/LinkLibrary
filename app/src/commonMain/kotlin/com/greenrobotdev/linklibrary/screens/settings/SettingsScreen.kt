package com.greenrobotdev.linklibrary.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.greenrobotdev.linklibrary.design.components.cards.SectionContentCard
import com.greenrobotdev.linklibrary.design.components.layout.appVerticalScroll
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon

/**
 * Settings: only what works today. Appearance (theme) and About. The version row hides the
 * developer component gallery behind a long-press.
 */
@Composable
fun SettingsScreen(
    routeKey: NavKey,
    onOpenComponentDemo: () -> Unit = {},
) {
    val viewModel: SettingsViewModel = viewModel(key = routeKey.toString()) { SettingsViewModel() }
    val state by viewModel.models.collectAsState()

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

        SectionContentCard(title = "About") {
            AboutRow(version = rememberAppVersion(), onLongPress = onOpenComponentDemo)
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
private fun AboutRow(version: String, onLongPress: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(onClick = {}, onLongClick = onLongPress, onLongClickLabel = "Open component gallery"),
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
