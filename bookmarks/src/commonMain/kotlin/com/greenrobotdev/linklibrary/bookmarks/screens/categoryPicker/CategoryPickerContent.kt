package com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.components.button.DashedPillButton
import com.greenrobotdev.linklibrary.design.components.feedback.ErrorMessageCard
import com.greenrobotdev.linklibrary.design.components.layout.AppActionBar
import com.greenrobotdev.linklibrary.design.components.layout.AppBarAction
import com.greenrobotdev.linklibrary.design.components.layout.AppBarPill
import com.greenrobotdev.linklibrary.design.components.layout.AppUndoBar
import com.greenrobotdev.linklibrary.design.components.layout.SwipeConfirmBar
import com.greenrobotdev.linklibrary.design.components.textfield.SearchField

/** One visible entry in the browse list: a category row, or an inline editing field. */
internal sealed interface PickerItem {
    val key: String
    data class Row(val row: TreeRow) : PickerItem { override val key = row.category.id }
    data class SubField(val depth: Int) : PickerItem { override val key = "add-sub-field" }
    data class RenameField(val depth: Int, val categoryId: String) : PickerItem { override val key = "rename-$categoryId" }
}

/** Interleave inline edit fields at the right tree position. */
internal fun buildPickerItems(rows: List<TreeRow>, creatingSubOf: String?, renamingId: String?): List<PickerItem> = buildList {
    val parentIdx = if (creatingSubOf != null) rows.indexOfFirst { it.category.id == creatingSubOf } else -1
    val parentDepth = if (parentIdx >= 0) rows[parentIdx].depth else -1
    val insertAfter = if (parentIdx >= 0) {
        var last = parentIdx
        for (i in parentIdx + 1 until rows.size) { if (rows[i].depth > parentDepth) last = i else break }
        last
    } else -1

    for ((i, row) in rows.withIndex()) {
        if (row.category.id == renamingId) add(PickerItem.RenameField(row.depth, row.category.id))
        else add(PickerItem.Row(row))
        if (i == insertAfter) add(PickerItem.SubField(parentDepth + 1))
    }
}

/**
 * Picker body shared by the sheet and full-screen hosts: search, recent chips, tree/search list,
 * footer, and the mode-dependent action bar.
 */
@Composable
internal fun CategoryPickerContent(
    state: CategoryPickerState,
    onEvent: (CategoryPickerEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    showUseThis: Boolean = true,
    /** Filtering: pick one category only. No create, rename or delete; bar is Clear + Apply. */
    selectOnly: Boolean = false,
) {
    val rows = if (state.browsing) flattenTree(state.categories, state.expandedIds)
    else searchRows(state.categories, state.query)
    val selected = state.selected

    // Inline editing modes derived from the dialog
    val creatingTopLevel = state.dialog?.let { it.kind == CategoryPickerDialog.Kind.ADD_SUB && it.categoryId == null } == true
    val creatingSubOf = state.dialog?.let { if (it.kind == CategoryPickerDialog.Kind.ADD_SUB && it.categoryId != null) it.categoryId else null }
    val renamingId = state.dialog?.let { if (it.kind == CategoryPickerDialog.Kind.RENAME) it.categoryId else null }
    val isEditing = creatingTopLevel || creatingSubOf != null || renamingId != null

    // New top-level name
    var newName by remember(creatingTopLevel) { mutableStateOf("") }
    val newNameTaken = newName.isNotBlank() && validateCategoryName(newName, null, state.categories) != null
    val canCreate = newName.isNotBlank() && !newNameTaken
    val createTopLevel = { if (canCreate) onEvent(CategoryPickerEvent.DialogName(newName.trim())) }

    // Add sub-category name
    var subName by remember(creatingSubOf) { mutableStateOf("") }
    val subParent = creatingSubOf?.let { id -> state.categories.firstOrNull { it.id == id } }
    val subNameTaken = subName.isNotBlank() && creatingSubOf != null &&
        validateCategoryName(subName, creatingSubOf, state.categories) != null
    val canCreateSub = subName.isNotBlank() && !subNameTaken
    val createSub = { if (canCreateSub) onEvent(CategoryPickerEvent.DialogName(subName.trim())) }

    // Rename
    val renamingCategory = renamingId?.let { id -> state.categories.firstOrNull { it.id == id } }
    var renameName by remember(renamingId) { mutableStateOf(state.dialog?.initialName ?: "") }
    val renameParent = renamingCategory?.parentId?.let { pid -> state.categories.firstOrNull { it.id == pid } }
    val renameNameTaken = renameName.isNotBlank() && renamingCategory != null &&
        validateCategoryName(renameName, renamingCategory.parentId, state.categories, excludeId = renamingId) != null
    val canRename = renameName.isNotBlank() && !renameNameTaken && renameName.trim() != (state.dialog?.initialName ?: "")
    val doRename = { if (canRename) onEvent(CategoryPickerEvent.DialogName(renameName.trim())) }

    // Delete flow: which rows are in the danger zone
    val deletingIds: Set<String> = remember(state.deletingCategory, state.allCategories) {
        val dc = state.deletingCategory ?: return@remember emptySet()
        subtreeIds(dc.id, state.allCategories)
    }
    val hasChildren = state.deletingCategory?.let { dc ->
        state.allCategories.any { it.parentId == dc.id }
    } == true
    val childNames = remember(state.deletingCategory, state.allCategories) {
        val dc = state.deletingCategory ?: return@remember emptyList()
        state.allCategories.filter { it.parentId == dc.id }.map { it.name }
    }

    val pickerItems = if (state.browsing) buildPickerItems(rows, creatingSubOf, renamingId) else emptyList()
    val listState = rememberLazyListState()
    LaunchedEffect(creatingTopLevel) {
        if (creatingTopLevel) listState.animateScrollToItem(listState.layoutInfo.totalItemsCount.coerceAtLeast(1) - 1)
    }
    LaunchedEffect(creatingSubOf) {
        if (creatingSubOf != null) {
            val idx = pickerItems.indexOfFirst { it is PickerItem.SubField }
            if (idx >= 0) listState.animateScrollToItem(idx)
        }
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Search / create — filled pill field like the mock
        SearchField(
            query = state.query,
            onQueryChange = { onEvent(CategoryPickerEvent.Search(it)) },
            placeholder = "Search or create a category",
            singleLine = true,
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        state.error?.let { error ->
            ErrorMessageCard(error = error, onDismiss = { onEvent(CategoryPickerEvent.ClearError) })
        }

        // Recent: label and chips share one scrolling line
        if (state.browsing && state.recent.isNotEmpty() && !isEditing) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Recent",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(end = 8.dp),
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.recent, key = { it.id }) { category ->
                        RecentChip(
                            text = category.path(state.categories),
                            onClick = {
                                onEvent(if (selectOnly) CategoryPickerEvent.ToggleChecked(category.id) else CategoryPickerEvent.Select(category.id))
                            },
                        )
                    }
                }
            }
        }

        // Browse tree / search results
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .statusBarsPadding(),
            state = listState
        ) {
            if (state.browsing) {
                items(pickerItems, key = { it.key }) { item ->
                    when (item) {
                        is PickerItem.Row -> {
                            val catId = item.row.category.id
                            val isDeleteTarget = state.deletePhase != DeletePhase.NONE && catId in deletingIds
                            SelectableRow(
                                text = item.row.category.name,
                                count = item.row.category.count,
                                depth = item.row.depth,
                                selected = if (selectOnly) item.row.category.id in state.checkedIds
                                else !isEditing && item.row.category.id == state.selectedCategoryId,
                                expandable = item.row.hasChildren,
                                expanded = item.row.category.id in state.expandedIds,
                                onClick = {
                                    if (selectOnly) onEvent(CategoryPickerEvent.ToggleChecked(item.row.category.id))
                                    else if (!isEditing) onEvent(CategoryPickerEvent.Select(item.row.category.id))
                                },
                                onToggle = { onEvent(CategoryPickerEvent.ToggleExpand(item.row.category.id)) },
                                isDeleting = isDeleteTarget && state.deletePhase == DeletePhase.CONFIRM,
                                isSwiping = isDeleteTarget && state.deletePhase == DeletePhase.SWIPING,
                                disabled = isEditing,
                            )
                        }
                        is PickerItem.SubField -> InlineTreeField(
                            name = subName,
                            onNameChange = { subName = it },
                            taken = subNameTaken,
                            hint = "Inside ${subParent?.name ?: "?"} · you can nest deeper after",
                            errorHint = "\"${subName.trim()}\" already exists in ${subParent?.name ?: "this category"}",
                            onDone = createSub,
                            depth = item.depth,
                        )
                        is PickerItem.RenameField -> InlineTreeField(
                            name = renameName,
                            onNameChange = { renameName = it },
                            taken = renameNameTaken,
                            hint = if (renameParent != null) "Inside ${renameParent.name} · its links keep this category"
                            else "Top level · its links keep this category",
                            errorHint = "\"${renameName.trim()}\" already exists ${
                                if (renameParent != null) "in ${renameParent.name}" else "at the top level"
                            }",
                            onDone = doRename,
                            depth = item.depth,
                            icon = Icons.Filled.Edit,
                        )
                    }
                }
            } else {
                items(rows, key = { it.category.id }) { row ->
                    SelectableRow(
                        text = row.breadcrumb,
                        count = row.category.count,
                        selected = if (selectOnly) row.category.id in state.checkedIds
                        else !isEditing && row.category.id == state.selectedCategoryId,
                        onClick = {
                            if (selectOnly) onEvent(CategoryPickerEvent.ToggleChecked(row.category.id))
                            else if (!isEditing) onEvent(CategoryPickerEvent.Select(row.category.id))
                        },
                        disabled = isEditing,
                    )
                }
                if (!selectOnly && needsCreateRow(state.categories, state.query)) {
                    item(key = "create") {
                        CreateRow(
                            text = state.query.trim(),
                            onClick = { onEvent(CategoryPickerEvent.CreateFromSearch) },
                        )
                    }
                }
                if (rows.isEmpty() && (selectOnly || !needsCreateRow(state.categories, state.query))) {
                    item(key = "empty") {
                        Text(
                            "No matching category",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(12.dp),
                        )
                    }
                }
            }

            if (state.browsing && !selectOnly) {
                item(key = "new-top-level") {
                    if (creatingTopLevel) {
                        InlineTreeField(
                            name = newName,
                            onNameChange = { newName = it },
                            taken = newNameTaken,
                            hint = "Top level · you can add subcategories after",
                            errorHint = "\"${newName.trim()}\" already exists at the top level",
                            onDone = createTopLevel,
                            depth = 0,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    } else {
                        DashedPillButton(
                            text = "New top-level category",
                            onClick = { onEvent(CategoryPickerEvent.AddTopLevel) },
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                }
            }
        }

        // Footer
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                when {
                    state.deletePhase != DeletePhase.NONE -> "Deleting: "
                    creatingTopLevel -> "New top-level: "
                    creatingSubOf != null -> "New sub in: "
                    renamingId != null -> "Renaming: "
                    selectOnly -> "Filtering by: "
                    else -> "Selected: "
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                when {
                    state.deletePhase != DeletePhase.NONE -> state.deletingCategory?.name ?: ""
                    creatingTopLevel -> newName.trim()
                    creatingSubOf != null -> subParent?.path(state.categories) ?: ""
                    renamingId != null -> renamingCategory?.path(state.categories) ?: ""
                    selectOnly -> state.categories.filter { it.id in state.checkedIds }
                        .joinToString(", ") { it.path(state.categories) }.ifEmpty { "all categories" }
                    else -> state.selectedPath ?: "No category"
                },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        // Delete flow: impact card + parent choice
        if (state.deletePhase == DeletePhase.CONFIRM || state.deletePhase == DeletePhase.SWIPING) {
            state.deletingCategory?.let { dc ->
                ImpactCard(
                    categoryName = dc.name,
                    linkCount = state.deletedLinkCount,
                    isSwiping = state.deletePhase == DeletePhase.SWIPING,
                )
                if (hasChildren) {
                    ParentChoiceCard(
                        parentName = dc.name,
                        childNames = childNames,
                        choice = state.parentDeleteChoice,
                        onChoice = { onEvent(CategoryPickerEvent.ParentDeleteChoice(it)) },
                    )
                }
            }
        }

        // Bar: mode-dependent
        when {
            state.deletePhase == DeletePhase.UNDO -> AppUndoBar(
                title = "${state.deletingCategory?.name ?: ""} deleted",
                subtitle = if (state.deletedLinkCount > 0)
                    "${state.deletedLinkCount} link${if (state.deletedLinkCount != 1) "s" else ""} now uncategorized" else null,
                onUndo = { onEvent(CategoryPickerEvent.UndoDelete) },
                durationMillis = DELETE_UNDO_MS.toInt(),
            )
            state.deletePhase == DeletePhase.CONFIRM || state.deletePhase == DeletePhase.SWIPING ->
                SwipeConfirmBar(
                    onCancel = { onEvent(CategoryPickerEvent.UndoDelete) },
                    onConfirm = { onEvent(CategoryPickerEvent.SwipeComplete) },
                    enabled = !hasChildren || state.parentDeleteChoice != null,
                )
            creatingTopLevel || creatingSubOf != null -> AppActionBar {
                AppBarAction(Icons.Filled.Close, "Cancel", { onEvent(CategoryPickerEvent.CancelDialog) }, enabled = true)
                AppBarPill(
                    "Create",
                    onClick = if (creatingTopLevel) createTopLevel else createSub,
                    enabled = if (creatingTopLevel) canCreate else canCreateSub,
                    modifier = Modifier.weight(1f),
                )
            }
            renamingId != null -> AppActionBar {
                AppBarAction(Icons.Filled.Close, "Cancel", { onEvent(CategoryPickerEvent.CancelDialog) }, enabled = true)
                AppBarPill("Save name", onClick = doRename, enabled = canRename, modifier = Modifier.weight(1f))
            }
            selectOnly -> AppActionBar {
                AppBarAction(
                    Icons.Filled.Close, "Clear", { onEvent(CategoryPickerEvent.ClearChecked) },
                    enabled = state.checkedIds.isNotEmpty(),
                )
                val n = state.checkedIds.size
                AppBarPill(
                    text = if (n == 0) "Show all" else "Apply ($n)",
                    onClick = { onEvent(CategoryPickerEvent.UseThis) },
                    modifier = Modifier.weight(1f),
                )
            }
            else -> AppActionBar {
                // Without the confirm pill (e.g. Manage's embedded tab), spread actions evenly
                val spread = if (showUseThis) Modifier else Modifier.weight(1f)
                AppBarAction(
                    Icons.Filled.CreateNewFolder, "Add sub",
                    { onEvent(CategoryPickerEvent.AddSub) },
                    enabled = selected != null && canAddChildUnder(selected.id, state.categories),
                    modifier = spread,
                )
                AppBarAction(Icons.Filled.Edit, "Rename", { onEvent(CategoryPickerEvent.Rename) }, enabled = selected != null, modifier = spread)
                AppBarAction(Icons.Filled.Delete, "Remove", { onEvent(CategoryPickerEvent.Remove) }, enabled = selected != null, isError = true, modifier = spread)
                if (showUseThis) AppBarPill(text = "Use this", onClick = { onEvent(CategoryPickerEvent.UseThis) }, modifier = Modifier.weight(1f))
            }
        }
    }
}
