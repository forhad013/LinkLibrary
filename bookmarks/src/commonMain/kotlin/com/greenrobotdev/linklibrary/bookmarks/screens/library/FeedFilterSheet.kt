package com.greenrobotdev.linklibrary.bookmarks.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.BrowseAllButton
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.ChoiceChip
import com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker.CategoryFilterSheet
import com.greenrobotdev.linklibrary.bookmarks.screens.tagPicker.TagPickerSheet
import com.greenrobotdev.linklibrary.design.components.layout.BaseBottomSheet
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon

/**
 * "Filter saves" sheet (design: Feed filter screen). Edits a draft of [LibraryState.filters];
 * nothing reaches the Feed until "Show N saves". Close/back discards the draft.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FeedFilterSheet(
    state: LibraryState,
    onApply: (FeedFilters) -> Unit,
    onDismiss: () -> Unit,
) {
    var draft by remember { mutableStateOf(state.filters) }
    var pickCategories by remember { mutableStateOf(false) }
    var pickTags by remember { mutableStateOf(false) }
    val matching = filterFeed(state, draft).size

    if (pickCategories) {
        CategoryFilterSheet(
            initialChecked = draft.categoryIds,
            onDismiss = { pickCategories = false },
            onApplied = { draft = draft.copy(categoryIds = it) },
        )
    }
    if (pickTags) {
        TagPickerSheet(
            onDismiss = { pickTags = false },
            initialChecked = draft.tagIds,
            onApplied = { draft = draft.copy(tagIds = it) },
            selectOnly = true,
        )
    }

    BaseBottomSheet(title = "Filter saves", onClose = onDismiss, closeContentDescription = "Close filters") {
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            FilterSection("Show") {
                Segmented(
                    options = OpenedFilter.entries,
                    selected = draft.opened,
                    label = { it.label },
                    onSelect = { draft = draft.copy(opened = it) },
                )
                SwitchRow("Favorites only", null, draft.favoritesOnly) { draft = draft.copy(favoritesOnly = it) }
            }

            FilterSection("Type") {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    LinkKind.entries.forEach { kind ->
                        val on = kind in draft.kinds
                        ChoiceChip(
                            text = if (on) "✓ ${kind.label}" else kind.label,
                            selected = on,
                            onClick = { draft = draft.copy(kinds = if (on) draft.kinds - kind else draft.kinds + kind) },
                        )
                    }
                }
            }

            FilterSection("Categories") {
                val picked = state.categories.filter { it.id in draft.categoryIds }
                PickerField(
                    label = "In categories",
                    value = picked.joinToString(", ") { it.path(state.categories) }.ifEmpty { "All categories" },
                    active = picked.isNotEmpty(),
                    onClick = { pickCategories = true },
                )
                if (picked.isNotEmpty()) {
                    Text(
                        "Includes everything inside them.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            FilterSection(
                "Tags",
                trailing = {
                    Segmented(
                        options = listOf(false, true),
                        selected = draft.tagMatchAll,
                        label = { if (it) "All" else "Any" },
                        onSelect = { draft = draft.copy(tagMatchAll = it) },
                        compact = true,
                    )
                },
            ) {
                // Selected tags first, then the most used, so a pick never scrolls out of view
                val shown = (state.tags.filter { it.id in draft.tagIds } +
                    state.tags.sortedByDescending { it.count }.take(8)).distinctBy { it.id }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    itemVerticalAlignment = Alignment.CenterVertically,
                ) {
                    shown.forEach { tag ->
                        val on = tag.id in draft.tagIds
                        ChoiceChip(
                            text = "#${tag.name}  ${tag.count}",
                            selected = on,
                            onClick = { draft = draft.copy(tagIds = if (on) draft.tagIds - tag.id else draft.tagIds + tag.id) },
                        )
                    }
                    BrowseAllButton("All tags", onClick = { pickTags = true })
                }
            }

            FilterSection("Saved") {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SavedRange.entries.forEach { range ->
                        ChoiceChip(text = range.label, selected = draft.saved == range, onClick = { draft = draft.copy(saved = range) })
                    }
                }
            }

            SwitchRow("Include archived", "Archived saves are hidden by default", draft.includeArchived) {
                draft = draft.copy(includeArchived = it)
            }
        }

        // Actions
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            ActionButton(
                text = "Reset",
                container = MaterialTheme.colorScheme.surfaceContainer,
                content = MaterialTheme.colorScheme.onSurface,
                enabled = draft != FeedFilters(),
                onClick = { draft = FeedFilters() },
                modifier = Modifier.weight(1f),
            )
            ActionButton(
                text = when (matching) { 0 -> "No saves match"; 1 -> "Show 1 save"; else -> "Show $matching saves" },
                container = MaterialTheme.colorScheme.primary,
                content = MaterialTheme.colorScheme.onPrimary,
                enabled = true,
                onClick = { onApply(draft); onDismiss() },
                modifier = Modifier.weight(2f),
            )
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
            trailing?.invoke()
        }
        content()
    }
}

/** Pill-shaped segmented control (radio group). */
@Composable
private fun <T> Segmented(
    options: List<T>,
    selected: T,
    label: (T) -> String,
    onSelect: (T) -> Unit,
    compact: Boolean = false,
) {
    Row(
        modifier = Modifier
            .then(if (compact) Modifier else Modifier.fillMaxWidth())
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        options.forEach { option ->
            val on = option == selected
            Box(
                modifier = Modifier
                    .then(if (compact) Modifier else Modifier.weight(1f))
                    .clip(CircleShape)
                    .background(if (on) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                    .selectable(selected = on, role = Role.RadioButton, onClick = { onSelect(option) })
                    .heightIn(min = if (compact) 32.dp else 40.dp)
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    label(option),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (on) FontWeight.ExtraBold else FontWeight.SemiBold,
                    color = if (on) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun SwitchRow(title: String, subtitle: String?, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .toggleable(value = checked, role = Role.Switch, onValueChange = onChange),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            subtitle?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Switch(checked = checked, onCheckedChange = null) // the row handles the toggle
    }
}

@Composable
private fun PickerField(label: String, value: String, active: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        AppIcon(Icons.Outlined.Folder, contentDescription = null, color = MaterialTheme.colorScheme.onSurface)
        Column(Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (active) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        AppIcon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun ActionButton(
    text: String,
    container: Color,
    content: Color,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (enabled) container else container.copy(alpha = 0.5f))
            .clickable(role = Role.Button, enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = if (enabled) content else content.copy(alpha = 0.6f),
        )
    }
}
