package com.greenrobotdev.linklibrary.bookmarks.screens.add.components

import com.greenrobotdev.linklibrary.design.components.layout.AppLazyRow
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.extensions.dashBorder
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon
import com.greenrobotdev.linklibrary.design.theme.LordsExtraColors

// Pieces of the "Save to Lords of Links" screen (design: Save screen, share sheet and Add link)

@Composable
internal fun SaveHeader(onClose: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            "Save to Lords of Links",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .clickable(role = Role.Button, onClick = onClose),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(
                Icons.Filled.Close,
                contentDescription = "Close",
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
internal fun LinkKeptLine(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppIcon(
            Icons.Filled.Check,
            contentDescription = null,
            color = LordsExtraColors.success,
            modifier = Modifier.size(18.dp)
        )
        Text(
            "Link kept. Details are optional.",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = LordsExtraColors.success,
        )
    }
}

/** Thumbnail block + "Type · host", title, two-line description. */
@Composable
internal fun LinkPreviewCard(
    type: String,
    host: String,
    title: String,
    description: String?,
    isFetching: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // shortcut: type icon on the media placeholder, not the page image (bookmarks has no image loader); load imageUrl if the design needs real thumbnails
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(LordsExtraColors.mediaPlaceholder),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(
                imageVector = when (type) {
                    "video" -> Icons.Filled.PlayArrow
                    "social" -> Icons.Filled.Forum
                    "article" -> Icons.AutoMirrored.Filled.Article
                    else -> Icons.Filled.Language
                },
                contentDescription = null,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                "${type.replaceFirstChar { it.uppercaseChar() }} · $host",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                title.ifBlank { if (isFetching) "Fetching details…" else host },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            description?.takeIf { it.isNotBlank() }?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/** Outlined field: folder icon, "Category (optional)" over the selected path, chevron. Opens the picker. */
@Composable
internal fun CategoryField(
    selectedPath: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Category (optional)",
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        AppIcon(
            Icons.Outlined.Folder,
            contentDescription = null,
            color = MaterialTheme.colorScheme.onSurface
        )
        Column(Modifier.weight(1f)) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                selectedPath ?: "No category",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (selectedPath != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        AppIcon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/** Pill chip: filled primary when selected, outlined otherwise. */
@Composable
internal fun ChoiceChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = CircleShape
    Text(
        text,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        modifier = modifier
            .clip(shape)
            .then(
                if (selected) Modifier.background(MaterialTheme.colorScheme.primary)
                else Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            )
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
    )
}

/** Horizontally scrolling chips with an optional leading label ("Most used"). */
@Composable
internal fun <T> ChipRow(
    items: List<T>,
    key: (T) -> Any,
    modifier: Modifier = Modifier,
    label: String? = null,
    chip: @Composable (T) -> Unit,
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        label?.let {
            Text(
                it,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 10.dp),
            )
        }
        AppLazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items, key = key) { chip(it) }
        }
    }
}

/** Selected tag: "#name ✕" on secondaryContainer; ✕ removes it. */
@Composable
internal fun SelectedTagChip(name: String, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(start = 12.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "#$name",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable(role = Role.Button, onClick = onRemove),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(
                Icons.Filled.Close,
                contentDescription = "Remove tag $name",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

/** Dashed pill input: type a tag, Enter adds it (creating the tag if new). */
@Composable
internal fun TagInput(onAdd: (String) -> Unit, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    val submit = {
        if (text.isNotBlank()) {
            onAdd(text); text = ""
        }
    }
    Box(
        modifier = modifier
            .widthIn(min = 200.dp)
            .heightIn(min = 36.dp)
            .dashBorder(MaterialTheme.colorScheme.outline)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        BasicTextField(
            value = text,
            // Enter/comma also commit, for hardware keyboards
            onValueChange = { v ->
                if (v.endsWith("\n") || v.endsWith(",")) {
                    text = v.dropLast(1); submit()
                } else text = v
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { submit() }),
            decorationBox = { inner ->
                if (text.isEmpty()) {
                    Text(
                        "Type a tag, press enter",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                inner()
            },
        )
    }
}

@Composable
internal fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
    )
}

/** Outlined multi-line note box. */
@Composable
internal fun NoteField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        minLines = 2,
        modifier = modifier.fillMaxWidth(),
        decorationBox = { inner ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(16.dp)
                    )
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                if (value.isEmpty()) {
                    Text(
                        "Why are you saving this?",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                inner()
            }
        },
    )
}

/** Discard (danger tonal, 1 part) + Save (primary with bookmark, 2 parts). */
@Composable
internal fun SaveActions(
    onDiscard: () -> Unit,
    onSave: () -> Unit,
    saveEnabled: Boolean,
    isSaving: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.errorContainer)
                .clickable(role = Role.Button, enabled = !isSaving, onClick = onDiscard),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "Discard",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
        }
        Row(
            modifier = Modifier
                .weight(2f)
                .heightIn(min = 56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (saveEnabled) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
                .clickable(
                    role = Role.Button,
                    enabled = saveEnabled && !isSaving,
                    onClick = onSave
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val fg =
                if (saveEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.38f
                )
            AppIcon(
                Icons.Outlined.BookmarkBorder,
                contentDescription = null,
                color = fg,
                modifier = Modifier.size(20.dp)
            )
            Text(
                if (isSaving) "  Saving…" else "  Save",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = fg,
            )
        }
    }
}

/** Small text action with a chevron, e.g. "All tags ›" next to a section title. */
@Composable
internal fun BrowseAllButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .clickable(role = Role.Button, onClick = onClick)
            .heightIn(min = 40.dp)
            .padding(start = 12.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        AppIcon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp),
        )
    }
}
