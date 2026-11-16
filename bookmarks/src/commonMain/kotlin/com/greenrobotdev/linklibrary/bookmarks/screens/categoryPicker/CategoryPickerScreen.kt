package com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.components.feedback.ErrorMessageCard
import com.greenrobotdev.linklibrary.design.components.textfield.AppTextFieldEnhanced
import com.greenrobotdev.linklibrary.design.components.textfield.SearchField
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon

/**
 * Category picker as a modal bottom sheet (GRE-44).
 * Close/swipe/back leaves the link's category untouched; "Use this" applies it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPickerSheet(
    onDismiss: () -> Unit,
    linkId: String? = null,
    initialSelection: String? = null,
    onApplied: (categoryId: String?) -> Unit = {},
) {
    val viewModel = remember { CategoryPickerViewModel(linkId, initialSelection) }
    val state by viewModel.models.collectAsState()

    LaunchedEffect(state.applied) {
        if (state.applied) {
            onApplied(state.selectedCategoryId)
            onDismiss()
        }
    }

    // Fixed height from the window, not fillMaxHeight(): the sheet's constraints shrink with the keyboard,
    // and a height that follows them feeds back into imePadding, so the sheet bounces up and down.
    val sheetHeight = with(LocalDensity.current) { (LocalWindowInfo.current.containerSize.height * 0.92f).toDp() }

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true, confirmValueChange = { true }),
        containerColor = MaterialTheme.colorScheme.surface, // mock: plain white sheet
    ) {
        CategoryPickerContent(
            state = state,
            onEvent = viewModel::take,
            onBack = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(sheetHeight) // mock: scrim stays visible above the sheet
                .imePadding() // sheet rises with the keyboard instead of being pushed (same as Add-link sheet)
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        )
    }
}

/** Full-screen host for the same picker UI (Organize tab's "move category" entry). */
@Composable
fun CategoryPickerScreen(
    initialSelection: String? = null,
    onDone: (categoryId: String?) -> Unit,
    onBack: () -> Unit = {},
) {
    val viewModel = remember { CategoryPickerViewModel(linkId = null, initialSelection) }
    val state by viewModel.models.collectAsState()

    LaunchedEffect(state.applied) {
        if (state.applied) onDone(state.selectedCategoryId)
    }

    CategoryPickerContent(
        state = state,
        onEvent = viewModel::take,
        onBack = onBack,
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    )
}

private sealed interface PickerItem {
    val key: String
    data class Row(val row: TreeRow) : PickerItem { override val key = row.category.id }
    data class SubField(val depth: Int) : PickerItem { override val key = "add-sub-field" }
    data class RenameField(val depth: Int, val categoryId: String) : PickerItem { override val key = "rename-$categoryId" }
}

private fun buildPickerItems(rows: List<TreeRow>, creatingSubOf: String?, renamingId: String?): List<PickerItem> = buildList {
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

@Composable
internal fun CategoryPickerContent(
    state: CategoryPickerState,
    onEvent: (CategoryPickerEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
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

    val pickerItems = if (state.browsing) buildPickerItems(rows, creatingSubOf, renamingId) else emptyList()
    val listState = rememberLazyListState()
    LaunchedEffect(creatingTopLevel) {
        if (creatingTopLevel) listState.animateScrollToItem(listState.layoutInfo.totalItemsCount.coerceAtLeast(1) - 1)
    }
    LaunchedEffect(creatingSubOf) {
        if (creatingSubOf != null) {
            val idx = pickerItems.indexOfFirst { it is PickerItem.SubField }
            if (idx >= 0) listState.animateScrollToItem(idx + 1) // +1 for "no-category" header
        }
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Header: big title + round tonal close button (44dp)
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Category",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center,
            ) {
                AppIcon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close category picker",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

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

        // Recent: label and chips share one scrolling line, chips are plain outlined pills
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
                            onClick = { onEvent(CategoryPickerEvent.Select(category.id)) },
                        )
                    }
                }
            }
        }

        // Browse tree / search results
        LazyColumn(modifier = Modifier.weight(1f), state = listState) {
            if (state.browsing) {
                item(key = "no-category") {
                    SelectableRow(
                        text = "No category",
                        count = null,
                        selected = selected == null,
                        muted = true,
                        onClick = { onEvent(CategoryPickerEvent.Select(null)) },
                    )
                }
                items(pickerItems, key = { it.key }) { item ->
                    when (item) {
                        is PickerItem.Row -> SelectableRow(
                            text = item.row.category.name,
                            count = item.row.category.count,
                            depth = item.row.depth,
                            selected = item.row.category.id == state.selectedCategoryId,
                            expandable = item.row.hasChildren,
                            expanded = item.row.category.id in state.expandedIds,
                            onClick = { onEvent(CategoryPickerEvent.Select(item.row.category.id)) },
                            onToggle = { onEvent(CategoryPickerEvent.ToggleExpand(item.row.category.id)) },
                        )
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
                        selected = row.category.id == state.selectedCategoryId,
                        onClick = { onEvent(CategoryPickerEvent.Select(row.category.id)) },
                    )
                }
                if (needsCreateRow(state.categories, state.query)) {
                    item(key = "create") {
                        CreateRow(
                            text = state.query.trim(),
                            onClick = { onEvent(CategoryPickerEvent.CreateFromSearch) },
                        )
                    }
                }
                if (rows.isEmpty() && !needsCreateRow(state.categories, state.query)) {
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

            if (state.browsing) {
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
                        DashedPill(
                            text = "New top-level category",
                            modifier = Modifier.padding(top = 8.dp),
                            onClick = { onEvent(CategoryPickerEvent.AddTopLevel) },
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
                    creatingTopLevel -> "New top-level: "
                    creatingSubOf != null -> "New sub in: "
                    renamingId != null -> "Renaming: "
                    else -> "Selected: "
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                when {
                    creatingTopLevel -> newName.trim()
                    creatingSubOf != null -> subParent?.path(state.categories) ?: ""
                    renamingId != null -> renamingCategory?.path(state.categories) ?: ""
                    else -> state.selectedPath ?: "No category"
                },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        // Delete confirm (kept until swipe-to-delete lands in the VM)
        state.dialog?.let { dialog ->
            if (dialog.kind == CategoryPickerDialog.Kind.CONFIRM_REMOVE) {
                val category = state.categories.firstOrNull { it.id == dialog.categoryId }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Delete \"${category?.name ?: ""}\"? No links are deleted. They just lose this category.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = { onEvent(CategoryPickerEvent.CancelDialog) }) { Text("Cancel") }
                        TextButton(onClick = { onEvent(CategoryPickerEvent.ConfirmRemove) }) {
                            Text("Delete", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }

        // Bar: mode-dependent
        when {
            creatingTopLevel || creatingSubOf != null -> BarContainer {
                BarAction(Icons.Filled.Close, "Cancel", { onEvent(CategoryPickerEvent.CancelDialog) }, enabled = true)
                BarPill(
                    "Create",
                    onClick = if (creatingTopLevel) createTopLevel else createSub,
                    enabled = if (creatingTopLevel) canCreate else canCreateSub,
                    modifier = Modifier.weight(1f),
                )
            }
            renamingId != null -> BarContainer {
                BarAction(Icons.Filled.Close, "Cancel", { onEvent(CategoryPickerEvent.CancelDialog) }, enabled = true)
                BarPill("Save name", onClick = doRename, enabled = canRename, modifier = Modifier.weight(1f))
            }
            else -> ActionBar(
                hasSelection = selected != null,
                canAddSub = selected != null && canAddChildUnder(selected.id, state.categories),
                onAddSub = { onEvent(CategoryPickerEvent.AddSub) },
                onRename = { onEvent(CategoryPickerEvent.Rename) },
                onRemove = { onEvent(CategoryPickerEvent.Remove) },
                onUseThis = { onEvent(CategoryPickerEvent.UseThis) },
            )
        }
    }
}

/** Inline field for creating or renaming a category at a given tree depth (design: add-sub / rename / new top-level). */
@Composable
private fun InlineTreeField(
    name: String,
    onNameChange: (String) -> Unit,
    taken: Boolean,
    hint: String,
    errorHint: String,
    onDone: () -> Unit,
    depth: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Filled.CreateNewFolder,
    modifier: Modifier = Modifier,
) {
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) { focus.requestFocus() }
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        // Gutter matching SelectableRow's indent
        if (depth > 0) Box(modifier = Modifier.width((40 + (depth - 1) * 20).dp))
        Column(
            modifier = Modifier.weight(1f).padding(top = 4.dp, bottom = 6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            AppTextFieldEnhanced(
                value = name,
                onValueChange = onNameChange,
                placeholder = "Category name",
                leadingIcon = icon,
                isError = taken,
                showClear = true,
                openStyle = true,
                textFieldModifier = Modifier.focusRequester(focus),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onDone() }),
            )
            if (taken) {
                Row(
                    modifier = Modifier.padding(start = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    AppIcon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        errorHint,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            } else {
                Text(
                    hint,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 14.dp),
                )
            }
        }
    }
}


/**
 * One tree/search result row (mock: 50dp tall). A 40dp gutter holds the chevron; each level indents the
 * label 20dp. The selected row gets a 14dp-rounded primaryContainer fill that starts at the label's level.
 */
@Composable
private fun SelectableRow(
    text: String,
    count: Int?,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    depth: Int = 0,
    expandable: Boolean = false,
    expanded: Boolean = false,
    onToggle: () -> Unit = {},
    muted: Boolean = false,
) {
    val labelColor = when {
        selected -> MaterialTheme.colorScheme.onPrimaryContainer
        muted -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onSurface
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.width((40 + depth * 20).dp).fillMaxHeight(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            if (expandable) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onToggle),
                    contentAlignment = Alignment.Center,
                ) {
                    AppIcon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowDown else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "$text, ${if (expanded) "expanded" else "collapsed"}, ${count ?: 0} links",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = when {
                    selected -> FontWeight.Bold // spec: "bold text"; ExtraBold reads as a black shadow on the pill
                    muted -> FontWeight.Medium
                    else -> FontWeight.SemiBold
                },
                color = labelColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            count?.let {
                Text(
                    "[$it]",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/** Plain outlined pill used in the Recent row (mock: white fill, light outline, dark text, no check). */
@Composable
private fun RecentChip(text: String, onClick: () -> Unit) {
    Text(
        text,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        modifier = Modifier
            .clip(CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp),
    )
}

@Composable
private fun CreateRow(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AppIcon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            "Create \"$text\"",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
        )
    }
}

/** Dashed-outline pill button (mock: "+ New top-level category", 48dp, bold blue label). */
@Composable
private fun DashedPill(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val accent = MaterialTheme.colorScheme.onPrimaryContainer
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(CircleShape)
            .dashBorder(MaterialTheme.colorScheme.outline)
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AppIcon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            color = accent,
        )
        Text(
            text,
            style = MaterialTheme.typography.titleMedium,
            color = accent,
        )
    }
}

/** Dark rounded action bar: Add sub, Rename, Remove (icon + label), Use this. */
@Composable
private fun ActionBar(
    hasSelection: Boolean,
    canAddSub: Boolean,
    onAddSub: () -> Unit,
    onRename: () -> Unit,
    onRemove: () -> Unit,
    onUseThis: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BarContainer(modifier) {
        BarAction(Icons.Filled.CreateNewFolder, "Add sub", onAddSub, enabled = hasSelection && canAddSub)
        BarAction(Icons.Filled.Edit, "Rename", onRename, enabled = hasSelection)
        BarAction(Icons.Filled.Delete, "Remove", onRemove, enabled = hasSelection, isError = true)
        BarPill("Use this", onClick = onUseThis, modifier = Modifier.weight(1f))
    }
}

/** Mock: 72dp fully-rounded dark bar holding 72dp icon+label actions and an orange pill. */
@Composable
private fun BarContainer(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.inverseSurface)
            .padding(horizontal = 4.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/** Orange check pill ("Use this" / "Create"). Own Row: StandardButton's inner padding wraps the label at this width. */
@Composable
private fun BarPill(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    val container = if (enabled) MaterialTheme.colorScheme.tertiary
    else MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.12f)
    val content = if (enabled) MaterialTheme.colorScheme.onTertiary
    else MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.38f)
    Row(
        modifier = modifier
            .padding(start = 4.dp, end = 6.dp)
            .fillMaxHeight()
            .clip(CircleShape)
            .background(container)
            .clickable(enabled = enabled, onClick = onClick)
            .semantics { if (!enabled) contentDescription = "$text (disabled)" },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
        AppIcon(imageVector = Icons.Filled.Check, contentDescription = null, color = content)
        Text(text, style = MaterialTheme.typography.titleMedium, color = content, maxLines = 1)
    }
}

@Composable
private fun BarAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    enabled: Boolean,
    isError: Boolean = false,
) {
    // errorContainer reads as the mock's salmon on the dark bar (and dark red on the light bar in dark theme)
    val tint = if (isError) MaterialTheme.colorScheme.errorContainer
    else MaterialTheme.colorScheme.inverseOnSurface
    Column(
        modifier = Modifier
            .width(72.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .semantics { if (!enabled) contentDescription = "$label (disabled)" },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
    ) {
        AppIcon(
            imageVector = icon,
            contentDescription = label,
            color = if (enabled) tint else tint.copy(alpha = 0.38f),
        )
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = if (enabled) tint else tint.copy(alpha = 0.38f),
        )
    }
}
