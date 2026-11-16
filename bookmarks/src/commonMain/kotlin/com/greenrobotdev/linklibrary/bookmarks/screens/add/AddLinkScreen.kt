package com.greenrobotdev.linklibrary.bookmarks.screens.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.linklibrary.bookmarks.domain.hostOf
import com.greenrobotdev.linklibrary.bookmarks.domain.linkTypeOf
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.CategoryField
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.ChipRow
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.ChoiceChip
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.LinkKeptLine
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.LinkPreviewCard
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.NoteField
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.SaveActions
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.SaveHeader
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.SectionTitle
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.SelectedTagChip
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.TagInput
import com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker.CategoryPickerSheet
import com.greenrobotdev.linklibrary.bookmarks.screens.tagPicker.TagPickerSheet
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.BrowseAllButton
import androidx.compose.foundation.layout.Row
import com.greenrobotdev.linklibrary.design.components.feedback.ErrorMessageCard
import com.greenrobotdev.linklibrary.design.components.textfield.AppTextFieldEnhanced

/**
 * Save screen (design: "Save screen (share sheet and Add link)").
 * The link is stored as soon as the screen opens ("Link kept"); Save writes the optional
 * details, Discard deletes the link, close/back keeps it as is.
 */
@Composable
fun AddLinkScreen(
    routeKey: NavKey,
    initialUrl: String?,
    onBack: () -> Unit,
) {
    val viewModel: AddLinkViewModel = viewModel<AddLinkViewModel>(key = routeKey.toString()) { AddLinkViewModel(initialUrl) }
    val state by viewModel.models.collectAsState()

    LaunchedEffect(state.success) {
        if (state.success) {
            onBack()
            viewModel.take(AddLinkEvent.Consumed) // VM is cached per route key; don't re-fire on next visit
        }
    }

    LaunchedEffect(state.url) {
        if (shouldAutoFetch(state)) viewModel.take(AddLinkEvent.FetchMetadata)
    }

    var pickCategory by remember { mutableStateOf(false) }
    if (pickCategory) {
        CategoryPickerSheet(
            onDismiss = { pickCategory = false },
            initialSelection = state.selectedCategoryIds.firstOrNull(),
            onApplied = { viewModel.take(AddLinkEvent.SetCategory(it)) },
        )
    }

    var pickTags by remember { mutableStateOf(false) }
    if (pickTags) {
        TagPickerSheet(
            onDismiss = { pickTags = false },
            initialChecked = state.selectedTags,
            onApplied = { viewModel.take(AddLinkEvent.SetTags(it)) },
        )
    }

    AddLinkView(
        state = state,
        onEvent = viewModel::take,
        onClose = onBack,
        onPickCategory = { pickCategory = true },
        onBrowseTags = { pickTags = true },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddLinkView(
    state: AddLinkState,
    onEvent: (AddLinkEvent) -> Unit,
    onClose: () -> Unit,
    onPickCategory: () -> Unit,
    onBrowseTags: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val selectedCategory = state.availableCategories.firstOrNull { it.id in state.selectedCategoryIds }
    val selectedTags = state.availableTags.filter { it.id in state.selectedTags }
    val mostUsed = state.availableTags
        .filter { it.id !in state.selectedTags }
        .sortedByDescending { it.count }
        .take(8)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .padding(horizontal = 20.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 20.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SaveHeader(onClose = onClose)
                if (state.savedLinkId != null) LinkKeptLine()
            }

            if (state.askForUrl) {
                // Opened without a URL (e.g. quick add): ask for it; saved on Save instead of on open
                AppTextFieldEnhanced(
                    value = state.url,
                    onValueChange = { onEvent(AddLinkEvent.UrlChanged(it)) },
                    label = "Link",
                    placeholder = "https://",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Done),
                    openStyle = true,
                )
            } else {
                LinkPreviewCard(
                    type = linkTypeOf(state.url),
                    host = hostOf(state.url),
                    title = state.title,
                    description = state.fetchedMetadata?.description,
                    isFetching = state.isFetching,
                )
            }

            CategoryField(
                selectedPath = selectedCategory?.path(state.availableCategories),
                onClick = onPickCategory,
            )
            if (state.recentCategories.isNotEmpty()) {
                ChipRow(state.recentCategories, key = { it.id }) { category ->
                    val selected = category.id in state.selectedCategoryIds
                    ChoiceChip(
                        text = category.path(state.availableCategories),
                        selected = selected,
                        onClick = { onEvent(AddLinkEvent.SetCategory(if (selected) null else category.id)) },
                    )
                }
            }

            // "All tags" opens the tag picker sheet: search and multi-select when there are many tags
            Row(Modifier.fillMaxWidth().padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                SectionTitle("Tags (optional)", Modifier.weight(1f))
                BrowseAllButton("All tags", onClick = onBrowseTags)
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                itemVerticalAlignment = Alignment.CenterVertically,
            ) {
                selectedTags.forEach { tag ->
                    SelectedTagChip(tag.name, onRemove = { onEvent(AddLinkEvent.ToggleTag(tag.id)) })
                }
                TagInput(onAdd = { onEvent(AddLinkEvent.AddTagByName(it)) })
            }
            if (mostUsed.isNotEmpty()) {
                ChipRow(mostUsed, key = { it.id }, label = "Most used") { tag ->
                    ChoiceChip(text = "+ #${tag.name}", selected = false, onClick = { onEvent(AddLinkEvent.ToggleTag(tag.id)) })
                }
            }

            SectionTitle("Note (optional)", Modifier.padding(top = 4.dp))
            NoteField(value = state.notes.orEmpty(), onValueChange = { onEvent(AddLinkEvent.NotesChanged(it)) })

            state.error?.let { ErrorMessageCard(error = it, onDismiss = { onEvent(AddLinkEvent.ClearError) }) }
        }

        SaveActions(
            onDiscard = { if (state.savedLinkId != null) onEvent(AddLinkEvent.Discard) else onClose() },
            onSave = { onEvent(AddLinkEvent.Submit) },
            saveEnabled = state.url.isNotBlank(),
            isSaving = state.isLoading,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        )
    }
}

private fun shouldAutoFetch(state: AddLinkState): Boolean =
    state.url.isNotBlank() && state.url.startsWith("http") && !state.isFetching && state.title.isBlank()
