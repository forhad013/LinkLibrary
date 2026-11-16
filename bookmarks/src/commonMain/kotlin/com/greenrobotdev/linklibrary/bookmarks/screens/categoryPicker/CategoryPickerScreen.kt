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
import androidx.compose.material3.OutlinedTextField
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
import com.greenrobotdev.linklibrary.design.components.button.ComponentColors
import com.greenrobotdev.linklibrary.design.components.button.StandardButton
import com.greenrobotdev.linklibrary.design.components.feedback.ErrorMessageCard
import com.greenrobotdev.linklibrary.design.components.textfield.AppTextFieldEnhanced
import com.greenrobotdev.linklibrary.design.components.textfield.SearchField
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon
import com.greenrobotdev.linklibrary.design.theme.ComponentStyle

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

    // "New top-level category" flow: the ViewModel's ADD_SUB dialog with no parent, drawn inline in the tree
    val creatingTopLevel = state.dialog?.let { it.kind == CategoryPickerDialog.Kind.ADD_SUB && it.categoryId == null } == true
    var newName by remember(creatingTopLevel) { mutableStateOf("") }
    val newNameTaken = newName.isNotBlank() && validateCategoryName(newName, null, state.categories) != null
    val canCreate = newName.isNotBlank() && !newNameTaken
    val createTopLevel = { if (canCreate) onEvent(CategoryPickerEvent.DialogName(newName.trim())) }
    val listState = rememberLazyListState()
    LaunchedEffect(creatingTopLevel) {
        // keep the field (last item) visible above the keyboard
        if (creatingTopLevel) listState.animateScrollToItem(listState.layoutInfo.totalItemsCount.coerceAtLeast(1) - 1)
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
        if (state.browsing && state.recent.isNotEmpty() && !creatingTopLevel) {
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
                items(rows, key = { it.category.id }) { row ->
                    SelectableRow(
                        text = row.category.name,
                        count = row.category.count,
                        depth = row.depth,
                        selected = row.category.id == state.selectedCategoryId,
                        expandable = row.hasChildren,
                        expanded = row.category.id in state.expandedIds,
                        onClick = { onEvent(CategoryPickerEvent.Select(row.category.id)) },
                        onToggle = { onEvent(CategoryPickerEvent.ToggleExpand(row.category.id)) },
                    )
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
                        NewTopLevelField(
                            name = newName,
                            onNameChange = { newName = it },
                            taken = newNameTaken,
                            onDone = createTopLevel,
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
                if (creatingTopLevel) "New top-level: " else "Selected: ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                if (creatingTopLevel) newName.trim() else state.selectedPath ?: "No category",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        state.dialog?.let { dialog ->
            val category = state.categories.firstOrNull { it.id == dialog.categoryId }
            when (dialog.kind) {
                CategoryPickerDialog.Kind.ADD_SUB -> if (!creatingTopLevel) InlineNamePanel(
                    title = "New category in ${category?.path(state.categories) ?: "top level"}",
                    confirmLabel = "Create",
                    onDismiss = { onEvent(CategoryPickerEvent.CancelDialog) },
                    onConfirm = { onEvent(CategoryPickerEvent.DialogName(it)) },
                    helperText = when (category) {
                        null -> "Top level · you can add subcategories after"
                        else -> "Subcategory of ${category.name}"
                    },
                )

                CategoryPickerDialog.Kind.RENAME -> InlineNamePanel(
                    title = "Rename \"${dialog.initialName}\"",
                    confirmLabel = "Save",
                    onDismiss = { onEvent(CategoryPickerEvent.CancelDialog) },
                    onConfirm = { onEvent(CategoryPickerEvent.DialogName(it)) },
                    initialName = dialog.initialName,
                )

                CategoryPickerDialog.Kind.CONFIRM_REMOVE -> Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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

        if (creatingTopLevel) {
            // Edit bar: Cancel + Create replace the selection actions until the name is saved
            BarContainer {
                BarAction(Icons.Filled.Close, "Cancel", { onEvent(CategoryPickerEvent.CancelDialog) }, enabled = true)
                BarPill("Create", onClick = createTopLevel, enabled = canCreate, modifier = Modifier.weight(1f))
            }
        } else {
            ActionBar(
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

/** Inline name field that replaces the dashed pill while creating a top-level category (design: typing / taken). */
@Composable
private fun NewTopLevelField(
    name: String,
    onNameChange: (String) -> Unit,
    taken: Boolean,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) { focus.requestFocus() } // opens the keyboard
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        AppTextFieldEnhanced(
            value = name,
            onValueChange = onNameChange,
            placeholder = "Category name",
            leadingIcon = Icons.Filled.CreateNewFolder,
            isError = taken,
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
                    "\"${name.trim()}\" already exists at the top level",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        } else {
            Text(
                "Top level · you can add subcategories after",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 14.dp),
            )
        }
    }
}

/** Inline name entry panel (avoids nested dialog windows inside the bottom sheet). */
@Composable
private fun InlineNamePanel(
    title: String,
    confirmLabel: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    initialName: String = "",
    helperText: String? = null,
) {
    // fresh each time: the panel leaves composition whenever state.dialog is null
    var name by remember { mutableStateOf(initialName) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall)
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            singleLine = true,
            placeholder = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
        )
        helperText?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StandardButton(
                onClick = onDismiss,
                text = "Cancel",
                style = ComponentStyle.Outlined,
            )
            StandardButton(
                onClick = { onConfirm(name.trim()) },
                text = confirmLabel,
                enabled = name.isNotBlank(),
                colors = ComponentColors(
                    container = MaterialTheme.colorScheme.tertiary,
                    onContainer = MaterialTheme.colorScheme.onTertiary,
                ),
            )
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
