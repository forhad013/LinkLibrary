package com.greenrobotdev.lordofthelinks.bookmarks.screens.tagPicker

import com.greenrobotdev.lordofthelinks.design.components.layout.AppLazyColumn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.greenrobotdev.lordofthelinks.bookmarks.screens.categoryPicker.CreateRow
import com.greenrobotdev.lordofthelinks.bookmarks.screens.categoryPicker.DELETE_UNDO_MS
import com.greenrobotdev.lordofthelinks.bookmarks.screens.categoryPicker.DeletePhase
import com.greenrobotdev.lordofthelinks.bookmarks.screens.categoryPicker.ImpactCard
import com.greenrobotdev.lordofthelinks.bookmarks.screens.categoryPicker.InlineTreeField
import com.greenrobotdev.lordofthelinks.bookmarks.screens.categoryPicker.SelectableRow
import com.greenrobotdev.lordofthelinks.design.components.button.DashedPillButton
import com.greenrobotdev.lordofthelinks.design.components.feedback.ErrorMessageCard
import com.greenrobotdev.lordofthelinks.design.components.layout.AppActionBar
import com.greenrobotdev.lordofthelinks.design.components.layout.AppBarAction
import com.greenrobotdev.lordofthelinks.design.components.layout.AppBarPill
import com.greenrobotdev.lordofthelinks.design.components.layout.AppUndoBar
import com.greenrobotdev.lordofthelinks.design.components.layout.BaseBottomSheet
import com.greenrobotdev.lordofthelinks.design.components.layout.SwipeConfirmBar
import com.greenrobotdev.lordofthelinks.design.components.textfield.SearchField
import com.greenrobotdev.lordofthelinks.design.foundation.icon.AppIcon

/**
 * Tag picker body, shared by the sheet (multi-select, "Done") and Manage's Tags tab
 * (single focus, no confirm pill). Flat version of the category picker.
 */
@Composable
internal fun TagPickerContent(
    state: TagPickerState,
    onEvent: (TagPickerEvent) -> Unit,
    modifier: Modifier = Modifier,
    multiSelect: Boolean = true,
    /** Filtering: pick tags only. No create, rename or delete; bar is Clear + Apply. */
    selectOnly: Boolean = false,
) {
    val rows = state.visibleTags
    val renaming = state.renamingId?.let { id -> state.tags.firstOrNull { it.id == id } }

    var newName by remember(state.creating) { mutableStateOf("") }
    val newNameTaken = newName.isNotBlank() && validateTagName(newName, state.allTags) != null
    val canCreate = newName.isNotBlank() && !newNameTaken
    val doCreate = { if (canCreate) onEvent(TagPickerEvent.SubmitName(newName.trim())) }

    var renameName by remember(state.renamingId) { mutableStateOf(renaming?.name ?: "") }
    val renameTaken = renameName.isNotBlank() && validateTagName(renameName, state.allTags, excludeId = state.renamingId) != null
    val canRename = renameName.isNotBlank() && !renameTaken && renameName.trim() != renaming?.name
    val doRename = { if (canRename) onEvent(TagPickerEvent.SubmitName(renameName.trim())) }

    val showCreateRow = !selectOnly && state.query.isNotBlank() && state.tags.none { it.name.equals(state.query.trim(), ignoreCase = true) }

    val listState = rememberLazyListState()
    LaunchedEffect(state.creating) {
        if (state.creating) listState.animateScrollToItem(listState.layoutInfo.totalItemsCount.coerceAtLeast(1) - 1)
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SearchField(
            query = state.query,
            onQueryChange = { onEvent(TagPickerEvent.Search(it)) },
            placeholder = "Search or create a tag",
            singleLine = true,
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        state.error?.let { ErrorMessageCard(error = it, onDismiss = { onEvent(TagPickerEvent.ClearError) }) }

        AppLazyColumn(modifier = Modifier.weight(1f), state = listState) {
            items(rows, key = { it.id }) { tag ->
                if (tag.id == state.renamingId) {
                    InlineTreeField(
                        name = renameName,
                        onNameChange = { renameName = it },
                        taken = renameTaken,
                        hint = "Links with this tag keep it",
                        errorHint = "\"${renameName.trim()}\" already exists",
                        onDone = doRename,
                        depth = 0,
                        icon = Icons.Filled.Edit,
                        placeholder = "Tag name",
                    )
                } else {
                    val isDeleteTarget = state.deletePhase != DeletePhase.NONE && tag.id == state.deletingTag?.id
                    val checked = tag.id in state.checkedIds
                    SelectableRow(
                        text = tag.name,
                        count = tag.count,
                        selected = !selectOnly && !state.isEditing && tag.id == state.focusedId,
                        onClick = { onEvent(TagPickerEvent.Tap(tag.id)) },
                        isDeleting = isDeleteTarget && state.deletePhase == DeletePhase.CONFIRM,
                        isSwiping = isDeleteTarget && state.deletePhase == DeletePhase.SWIPING,
                        disabled = state.isEditing,
                        leading = if (multiSelect && checked) {
                            { AppIcon(Icons.Filled.Check, contentDescription = "Selected", color = MaterialTheme.colorScheme.primary) }
                        } else null,
                    )
                }
            }
            if (showCreateRow) {
                item(key = "create") {
                    CreateRow(text = state.query.trim(), onClick = { onEvent(TagPickerEvent.CreateFromSearch) })
                }
            }
            if (rows.isEmpty() && !showCreateRow) {
                item(key = "empty") {
                    Text(
                        if (state.query.isBlank()) "No tags yet" else "No matching tag",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(12.dp),
                    )
                }
            }
            if (state.query.isBlank() && !selectOnly) {
                item(key = "new-tag") {
                    if (state.creating) {
                        InlineTreeField(
                            name = newName,
                            onNameChange = { newName = it },
                            taken = newNameTaken,
                            hint = "New tag",
                            errorHint = "\"${newName.trim()}\" already exists",
                            onDone = doCreate,
                            depth = 0,
                            icon = Icons.Filled.Tag,
                            placeholder = "Tag name",
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    } else {
                        DashedPillButton(
                            text = "New tag",
                            onClick = { onEvent(TagPickerEvent.StartCreate) },
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                }
            }
        }

        // Footer
        Row(modifier = Modifier.fillMaxWidth().padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                when {
                    state.deletePhase != DeletePhase.NONE -> "Deleting: "
                    state.creating -> "New tag: "
                    renaming != null -> "Renaming: "
                    selectOnly -> "Filtering by: "
                    else -> "Selected: "
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                when {
                    state.deletePhase != DeletePhase.NONE -> state.deletingTag?.name ?: ""
                    state.creating -> newName.trim()
                    renaming != null -> renaming.name
                    multiSelect -> state.allTags.filter { it.id in state.checkedIds }.joinToString(", ") { "#" + it.name }
                        .ifEmpty { if (selectOnly) "all tags" else "No tags" }
                    else -> state.focused?.name ?: "None"
                },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (state.deletePhase == DeletePhase.CONFIRM || state.deletePhase == DeletePhase.SWIPING) {
            state.deletingTag?.let { tag ->
                ImpactCard(
                    categoryName = tag.name,
                    linkCount = tag.count,
                    isSwiping = state.deletePhase == DeletePhase.SWIPING,
                    consequence = "lose this tag",
                )
            }
        }

        when {
            state.deletePhase == DeletePhase.UNDO -> AppUndoBar(
                title = "${state.deletingTag?.name ?: ""} deleted",
                subtitle = state.deletingTag?.count?.takeIf { it > 0 }?.let { "$it link${if (it != 1) "s" else ""} lose this tag" },
                onUndo = { onEvent(TagPickerEvent.UndoDelete) },
                durationMillis = DELETE_UNDO_MS.toInt(),
            )
            state.deletePhase == DeletePhase.CONFIRM || state.deletePhase == DeletePhase.SWIPING -> SwipeConfirmBar(
                onCancel = { onEvent(TagPickerEvent.UndoDelete) },
                onConfirm = { onEvent(TagPickerEvent.SwipeComplete) },
                enabled = true,
            )
            state.creating -> AppActionBar {
                AppBarAction(Icons.Filled.Close, "Cancel", { onEvent(TagPickerEvent.CancelEdit) }, enabled = true)
                AppBarPill("Create", onClick = doCreate, enabled = canCreate, modifier = Modifier.weight(1f))
            }
            renaming != null -> AppActionBar {
                AppBarAction(Icons.Filled.Close, "Cancel", { onEvent(TagPickerEvent.CancelEdit) }, enabled = true)
                AppBarPill("Save name", onClick = doRename, enabled = canRename, modifier = Modifier.weight(1f))
            }
            selectOnly -> AppActionBar {
                AppBarAction(
                    Icons.Filled.Close, "Clear", { onEvent(TagPickerEvent.ClearChecked) },
                    enabled = state.checkedIds.isNotEmpty(),
                )
                val n = state.checkedIds.size
                AppBarPill(if (n == 0) "Show all" else "Apply ($n)", onClick = { onEvent(TagPickerEvent.Done) }, modifier = Modifier.weight(1f))
            }
            else -> AppActionBar {
                val spread = if (multiSelect) Modifier else Modifier.weight(1f)
                val focused = state.focused != null
                AppBarAction(Icons.Filled.Edit, "Rename", { onEvent(TagPickerEvent.StartRename) }, enabled = focused, modifier = spread)
                AppBarAction(Icons.Filled.Delete, "Remove", { onEvent(TagPickerEvent.Remove) }, enabled = focused, isError = true, modifier = spread)
                if (multiSelect) {
                    val n = state.checkedIds.size
                    AppBarPill(if (n == 0) "Done" else "Done ($n)", onClick = { onEvent(TagPickerEvent.Done) }, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

/**
 * Multi-select tag picker sheet. Close/swipe/back leaves the link's tags untouched;
 * "Done" hands the checked ids to [onApplied]. Create/rename/delete write immediately.
 */
@Composable
fun TagPickerSheet(
    onDismiss: () -> Unit,
    initialChecked: Set<String>,
    onApplied: (Set<String>) -> Unit,
    /** Filtering mode: see [TagPickerContent]'s selectOnly. */
    selectOnly: Boolean = false,
) {
    val viewModel = remember { TagPickerViewModel(multiSelect = true, initialChecked = initialChecked) }
    val state by viewModel.models.collectAsState()

    LaunchedEffect(state.applied) {
        if (state.applied) {
            onApplied(state.checkedIds)
            onDismiss()
        }
    }

    BaseBottomSheet(title = if (selectOnly) "Filter by tags" else "Tags", onClose = onDismiss, closeContentDescription = "Close tag picker") {
        TagPickerContent(state = state, onEvent = viewModel::take, modifier = Modifier.fillMaxWidth().weight(1f), selectOnly = selectOnly)
    }
}
