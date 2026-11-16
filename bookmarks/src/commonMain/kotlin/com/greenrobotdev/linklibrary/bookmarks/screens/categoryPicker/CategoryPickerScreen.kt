package com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.components.feedback.ErrorMessageCard
import com.greenrobotdev.linklibrary.design.components.textfield.AppTextFieldEnhanced
import com.greenrobotdev.linklibrary.design.components.textfield.SearchField
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon
import kotlin.math.roundToInt

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
                        onClick = { if (!isEditing) onEvent(CategoryPickerEvent.Select(null)) },
                        disabled = isEditing,
                    )
                }
                items(pickerItems, key = { it.key }) { item ->
                    when (item) {
                        is PickerItem.Row -> {
                            val catId = item.row.category.id
                            val isDeleteTarget = state.deletePhase != DeletePhase.NONE &&
                                catId in deletingIds
                            SelectableRow(
                                text = item.row.category.name,
                                count = item.row.category.count,
                                depth = item.row.depth,
                                selected = !isEditing && item.row.category.id == state.selectedCategoryId,
                                expandable = item.row.hasChildren,
                                expanded = item.row.category.id in state.expandedIds,
                                onClick = { if (!isEditing) onEvent(CategoryPickerEvent.Select(item.row.category.id)) },
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
                        selected = !isEditing && row.category.id == state.selectedCategoryId,
                        onClick = { if (!isEditing) onEvent(CategoryPickerEvent.Select(row.category.id)) },
                        disabled = isEditing,
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
                    state.deletePhase != DeletePhase.NONE -> "Deleting: "
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
                    state.deletePhase != DeletePhase.NONE -> state.deletingCategory?.name ?: ""
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
            state.deletePhase == DeletePhase.UNDO -> UndoBar(
                categoryName = state.deletingCategory?.name ?: "",
                linkCount = state.deletedLinkCount,
                onUndo = { onEvent(CategoryPickerEvent.UndoDelete) },
            )
            state.deletePhase == DeletePhase.CONFIRM || state.deletePhase == DeletePhase.SWIPING ->
                SwipeDeleteBar(
                    onKeep = { onEvent(CategoryPickerEvent.UndoDelete) },
                    onSwipeComplete = { onEvent(CategoryPickerEvent.SwipeComplete) },
                    enabled = !hasChildren || state.parentDeleteChoice != null,
                )
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
    isDeleting: Boolean = false,
    isSwiping: Boolean = false,
    disabled: Boolean = false,
) {
    val dangerBg = Color(0xFFFBE9E7)
    val dangerFg = Color(0xFF9A2418)
    val labelColor = when {
        isDeleting -> dangerFg
        selected -> MaterialTheme.colorScheme.onPrimaryContainer
        muted -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onSurface
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .then(if (isSwiping) Modifier.alpha(0.45f).scale(0.96f) else if (disabled) Modifier.alpha(0.38f) else Modifier)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = !disabled,
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
                        color = if (isDeleting) dangerFg else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    when {
                        isDeleting -> dangerBg
                        selected -> MaterialTheme.colorScheme.primaryContainer
                        else -> Color.Transparent
                    }
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = when {
                    isDeleting -> FontWeight.ExtraBold
                    selected -> FontWeight.Bold
                    muted -> FontWeight.Medium
                    else -> FontWeight.SemiBold
                },
                color = labelColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
                textDecoration = if (isSwiping) TextDecoration.LineThrough else TextDecoration.None,
            )
            count?.let {
                Text(
                    "[$it]",
                    style = MaterialTheme.typography.labelLarge,
                    color = when {
                        isDeleting -> dangerFg
                        selected -> MaterialTheme.colorScheme.onPrimaryContainer
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
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
        BarPill(text = "Use this", onClick = onUseThis, modifier = Modifier.weight(1f))
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
private fun BarPill(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Filled.Check,
) {
    val container = if (enabled) MaterialTheme.colorScheme.tertiary
    else MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.12f)
    val content = if (enabled) MaterialTheme.colorScheme.onTertiary
    else MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.38f)
    Row(
        modifier = modifier
            .fillMaxHeight()
            .clip(CircleShape)
            .background(container)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(start = 8.dp, end = 8.dp)
            .semantics { if (!enabled) contentDescription = "$text (disabled)" },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
        AppIcon(
            imageVector = icon,
            contentDescription = null,
            color = content
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = content, maxLines = 1
        )
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

// ─── Delete-flow composables ────────────────────────────────────────

/** Red-bordered card showing the delete impact: trash icon circle, title, link count, reassurance. */
@Composable
private fun ImpactCard(
    categoryName: String,
    linkCount: Int,
    isSwiping: Boolean,
    modifier: Modifier = Modifier,
) {
    val borderColor = Color(0xFFF3C9C2)
    val iconBg = Color(0xFFFFCDD2)
    val dangerFg = Color(0xFF9A2418)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(Icons.Filled.Delete, contentDescription = null, color = dangerFg, modifier = Modifier.size(20.dp))
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.weight(1f)) {
            Text(
                if (isSwiping) "Deleting…" else "Delete \"$categoryName\"?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = dangerFg,
            )
            if (linkCount > 0) {
                Text(
                    "$linkCount link${if (linkCount != 1) "s" else ""} move to No category",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                "The links themselves are never deleted.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** Radio choice card for how to handle a parent category's children. */
@Composable
private fun ParentChoiceCard(
    parentName: String,
    childNames: List<String>,
    choice: ParentDeleteChoiceKind?,
    onChoice: (ParentDeleteChoiceKind) -> Unit,
    modifier: Modifier = Modifier,
) {
    val childLabel = when {
        childNames.size == 1 -> childNames.first()
        childNames.size <= 3 -> childNames.joinToString(", ")
        else -> "${childNames.take(2).joinToString(", ")} +${childNames.size - 2}"
    }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Keep children option (recommended)
        val keepSelected = choice == ParentDeleteChoiceKind.KEEP_CHILDREN
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(if (keepSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                .border(
                    1.dp,
                    if (keepSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(16.dp),
                )
                .clickable { onChoice(ParentDeleteChoiceKind.KEEP_CHILDREN) }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            RadioButton(
                selected = keepSelected,
                onClick = { onChoice(ParentDeleteChoiceKind.KEEP_CHILDREN) },
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary),
            )
            Column {
                Text(
                    "Keep $childLabel",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    "Move up a level",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // Delete all option
        val deleteAllSelected = choice == ParentDeleteChoiceKind.DELETE_ALL
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(if (deleteAllSelected) Color(0xFFFBE9E7) else Color.Transparent)
                .border(
                    1.dp,
                    if (deleteAllSelected) Color(0xFF9A2418) else MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(16.dp),
                )
                .clickable { onChoice(ParentDeleteChoiceKind.DELETE_ALL) }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            RadioButton(
                selected = deleteAllSelected,
                onClick = { onChoice(ParentDeleteChoiceKind.DELETE_ALL) },
                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF9A2418)),
            )
            Text(
                "Delete $parentName and $childLabel",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

/** Dark bar with "Keep" button + horizontal swipe track. User drags the thumb to confirm delete. */
@Composable
private fun SwipeDeleteBar(
    onKeep: () -> Unit,
    onSwipeComplete: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val trackBg = Color(0xFF3A3D45)
    val fillColor = Color(0xFFFFB4A6)
    val thumbSize = 48.dp
    val trackHeight = 56.dp
    val trackRadius = 28.dp

    var trackWidthPx by remember { mutableIntStateOf(0) }
    val thumbSizePx = with(LocalDensity.current) { thumbSize.toPx() }
    val maxDrag = (trackWidthPx - thumbSizePx).coerceAtLeast(0f)
    var dragOffset by remember { mutableFloatStateOf(0f) }
    val progress = if (maxDrag > 0f) (dragOffset / maxDrag).coerceIn(0f, 1f) else 0f

    BarContainer(modifier) {
        // Keep button
        Box(
            modifier = Modifier
                .width(84.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onKeep),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "Keep",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                fontWeight = FontWeight.SemiBold,
            )
        }

        // Swipe track
        Box(
            modifier = Modifier
                .weight(1f)
                .height(trackHeight)
                .padding(end = 8.dp)
                .clip(RoundedCornerShape(trackRadius))
                .onSizeChanged { trackWidthPx = it.width }
                .drawBehind {
                    drawRoundRect(trackBg, cornerRadius = CornerRadius(trackRadius.toPx()))
                    if (progress > 0f) {
                        drawRoundRect(
                            fillColor.copy(alpha = 0.5f + progress * 0.5f),
                            size = Size(size.width * progress, size.height),
                            cornerRadius = CornerRadius(trackRadius.toPx()),
                        )
                    }
                }
                .draggable(
                    orientation = Orientation.Horizontal,
                    enabled = enabled,
                    state = rememberDraggableState { delta ->
                        dragOffset = (dragOffset + delta).coerceIn(0f, maxDrag)
                    },
                    onDragStopped = {
                        if (progress >= 0.7f) {
                            onSwipeComplete()
                        }
                        dragOffset = 0f
                    },
                ),
        ) {
            // Thumb
            Box(
                modifier = Modifier
                    .offset { IntOffset(dragOffset.roundToInt(), 0) }
                    .padding(4.dp)
                    .size(thumbSize)
                    .clip(CircleShape)
                    .background(if (progress > 0.5f) Color(0xFFD32F2F) else MaterialTheme.colorScheme.errorContainer),
                contentAlignment = Alignment.Center,
            ) {
                AppIcon(Icons.Filled.Delete, contentDescription = "Swipe to delete", color = Color.White, modifier = Modifier.size(22.dp))
            }

            // Label
            if (progress < 0.3f) {
                Row(
                    modifier = Modifier.matchParentSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Swipe to delete",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(start = thumbSize),
                    )
                    Text(" ›››", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.4f))
                }
            }
        }
    }
}

/** Dark bar with countdown ring, deleted message, and orange Undo button. */
@Composable
private fun UndoBar(
    categoryName: String,
    linkCount: Int,
    onUndo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val countdownProgress = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        countdownProgress.animateTo(0f, tween(durationMillis = 5000, easing = LinearEasing))
    }

    BarContainer(modifier) {
        // Countdown ring
        Spacer(Modifier.width(8.dp))
        val ringColor = MaterialTheme.colorScheme.errorContainer
        Box(
            modifier = Modifier
                .size(36.dp)
                .drawBehind {
                    drawCircle(ringColor.copy(alpha = 0.3f), style = Stroke(width = 3.dp.toPx()))
                    drawArc(
                        ringColor,
                        startAngle = -90f,
                        sweepAngle = 360f * countdownProgress.value,
                        useCenter = false,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(Icons.Filled.Delete, contentDescription = null, color = ringColor, modifier = Modifier.size(16.dp))
        }

        // Message
        Column(
            modifier = Modifier
                .weight(1f).
                padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            Text(
                text = "$categoryName deleted",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (linkCount > 0) {
                Text(
                    "$linkCount link${if (linkCount != 1) "s" else ""} now uncategorized",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.7f),
                    maxLines = 1,
                )
            }
        }

        // Undo button
        BarPill(
            text = "Undo",
            onClick = onUndo,
            icon = Icons.Filled.Refresh,
        )
        Spacer(Modifier.width(4.dp))
    }
}
