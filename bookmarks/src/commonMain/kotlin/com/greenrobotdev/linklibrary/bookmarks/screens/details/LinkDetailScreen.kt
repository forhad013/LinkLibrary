package com.greenrobotdev.linklibrary.bookmarks.screens.details

import com.greenrobotdev.linklibrary.bookmarks.motion.sharedThumb
import com.greenrobotdev.linklibrary.bookmarks.motion.sharedTitle
import com.greenrobotdev.linklibrary.design.components.layout.appVerticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.linklibrary.bookmarks.domain.hostOf
import com.greenrobotdev.linklibrary.bookmarks.model.Link
import com.greenrobotdev.linklibrary.bookmarks.screens.add.components.CategoryField
import com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker.CategoryPickerSheet
import com.greenrobotdev.linklibrary.bookmarks.screens.library.savedAtLabel
import com.greenrobotdev.linklibrary.bookmarks.screens.tagPicker.TagPickerSheet
import com.greenrobotdev.linklibrary.design.components.feedback.ErrorMessageCard
import com.greenrobotdev.linklibrary.design.components.layout.AppActionBar
import com.greenrobotdev.linklibrary.design.components.layout.AppBarPill
import com.greenrobotdev.linklibrary.design.components.textfield.AppTextFieldEnhanced
import com.greenrobotdev.linklibrary.design.extensions.dashBorder
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon
import com.greenrobotdev.linklibrary.design.theme.LordsExtraColors

/**
 * Link details (design: Link details). Everything edits in place and saves at once:
 * type, title, category, tags, note (after a short pause), favorite / archive / opened.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LinkDetailScreen(
    routeKey: NavKey,
    linkId: String,
    onBack: () -> Unit,
    @Suppress("UNUSED_PARAMETER") onNavigateToNoteEditor: (String) -> Unit = {}, // note edits inline now
) {
    val viewModel: LinkDetailViewModel = viewModel(key = routeKey.toString()) { LinkDetailViewModel(linkId) }
    val state by viewModel.models.collectAsState()
    val uriHandler = LocalUriHandler.current
    val clipboard = LocalClipboardManager.current
    val share = rememberShareLink()

    LaunchedEffect(state.isDeleted) { if (state.isDeleted) onBack() }

    var pickCategory by remember { mutableStateOf(false) }
    var pickTags by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }
    // Tag picker result that would remove tags; held until the user confirms
    var pendingTags by remember { mutableStateOf<Set<String>?>(null) }

    val link = state.link
    if (link == null) {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            BackButton(onBack, Modifier.padding(16.dp))
            state.error?.let { ErrorMessageCard(error = it, onDismiss = onBack, modifier = Modifier.align(Alignment.Center).padding(24.dp)) }
        }
        return
    }

    if (pickCategory) {
        CategoryPickerSheet(
            onDismiss = { pickCategory = false },
            initialSelection = state.categoryId,
            onApplied = { viewModel.take(LinkDetailEvent.SetCategory(it)) },
        )
    }
    if (pickTags) {
        TagPickerSheet(
            onDismiss = { pickTags = false },
            initialChecked = state.linkTags.map { it.id }.toSet(),
            onApplied = { picked ->
                val removing = state.linkTags.any { it.id !in picked }
                if (removing) pendingTags = picked else viewModel.take(LinkDetailEvent.SetTags(picked))
            },
        )
    }
    pendingTags?.let { picked ->
        val removed = state.linkTags.filter { it.id !in picked }
        val current = state.linkTags.map { it.id }.toSet()
        AlertDialog(
            onDismissRequest = { pendingTags = null },
            title = { Text(if (removed.size == 1) "Remove a tag?" else "Remove ${removed.size} tags?") },
            text = { Text("${removed.joinToString(", ") { "#" + it.name }} will be removed from this link. The tags themselves stay.") },
            confirmButton = {
                TextButton(onClick = { pendingTags = null; viewModel.take(LinkDetailEvent.SetTags(picked)) }) {
                    Text("Remove", color = MaterialTheme.colorScheme.error)
                }
            },
            // Keep: still add any newly picked tags, just don't remove the old ones
            dismissButton = {
                TextButton(onClick = { pendingTags = null; viewModel.take(LinkDetailEvent.SetTags(current + picked)) }) {
                    Text("Keep them")
                }
            },
        )
    }
    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete this link?") },
            text = { Text("It is removed with its note, tags and category. This can't be undone.") },
            confirmButton = {
                TextButton(onClick = { confirmDelete = false; viewModel.take(LinkDetailEvent.Delete) }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Cancel") } },
        )
    }

    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).imePadding()) {
        Column(Modifier.fillMaxSize().appVerticalScroll(rememberScrollState())) {
            Hero(
                linkId = link.id,
                type = link.type,
                noPreview = link.title.isBlank() || link.title == link.url,
                onBack = onBack,
                onShare = { share(link.title, link.url) },
            )

            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    TypeChip(type = link.type, onPick = { viewModel.take(LinkDetailEvent.SetType(it)) })
                    Text(
                        hostOf(link.url),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                TitleBlock(link = link, onRename = { viewModel.take(LinkDetailEvent.Rename(it)) })

                CategoryField(
                    selectedPath = state.category?.path(state.categories),
                    onClick = { pickCategory = true },
                    label = "Category",
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    itemVerticalAlignment = Alignment.CenterVertically,
                ) {
                    state.linkTags.forEach { tag ->
                        Text(
                            "#${tag.name}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .clickable(onClickLabel = "Edit tags", onClick = { pickTags = true })
                                .padding(horizontal = 12.dp, vertical = 9.dp),
                        )
                    }
                    Text(
                        "+ Tag",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .clip(CircleShape)
                            .dashBorder(MaterialTheme.colorScheme.outline)
                            .clickable(role = Role.Button, onClick = { pickTags = true })
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                    )
                }

                NoteCard(
                    note = link.description,
                    saving = state.noteSaving,
                    onChange = { viewModel.take(LinkDetailEvent.NoteChanged(it)) },
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text("Description from the page", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold)
                    Text(
                        link.fetchedDescription?.takeIf { it.isNotBlank() } ?: "The page didn't provide a description.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (link.fetchedDescription.isNullOrBlank()) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurface,
                    )
                }

                state.error?.let { ErrorMessageCard(error = it, onDismiss = { viewModel.take(LinkDetailEvent.ClearError) }) }

                Spacer(Modifier.height(96.dp)) // keeps the last card clear of the action bar
            }
        }

        // Bottom action bar
        Box(Modifier.align(Alignment.BottomCenter).padding(horizontal = 16.dp, vertical = 16.dp)) {
            AppActionBar {
                BarIcon(
                    icon = if (link.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    label = if (link.isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = MaterialTheme.colorScheme.tertiary,
                    onClick = { viewModel.take(LinkDetailEvent.ToggleFavorite) },
                )
                BarIcon(
                    icon = if (link.isArchived) Icons.Filled.Archive else Icons.Outlined.Archive,
                    label = if (link.isArchived) "Unarchive" else "Archive",
                    onClick = { viewModel.take(LinkDetailEvent.ToggleArchived) },
                )
                BarIcon(
                    icon = if (link.isOpened) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                    label = if (link.isOpened) "Mark as unopened" else "Mark as opened",
                    onClick = { viewModel.take(LinkDetailEvent.ToggleOpened) },
                )
                MoreMenu(
                    onCopy = { clipboard.setText(AnnotatedString(link.url)) },
                    onDelete = { confirmDelete = true },
                )
                AppBarPill(
                    text = "Open link",
                    icon = Icons.AutoMirrored.Filled.OpenInNew,
                    onClick = {
                        runCatching { uriHandler.openUri(link.url) }
                        viewModel.take(LinkDetailEvent.MarkOpened)
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

/** Top band coloured by link type, with a large type badge, Back (left) and Share (right). */
@Composable
private fun Hero(linkId: String, type: String, noPreview: Boolean, onBack: () -> Unit, onShare: () -> Unit) {
    data class Look(val bg: Color, val badgeBg: Color, val badgeFg: Color, val icon: ImageVector)
    val look = when {
        noPreview -> Look(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurfaceVariant, Icons.Filled.Link)
        type == "video" -> Look(LordsExtraColors.mediaPlaceholder, MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.onTertiary, Icons.Filled.PlayArrow)
        type == "social" -> Look(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary, Icons.Outlined.ChatBubbleOutline)
        else -> Look(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary, Icons.AutoMirrored.Outlined.Article)
    }
    Box(Modifier.fillMaxWidth().height(220.dp).background(look.bg)) {
        BackButton(onBack, Modifier.padding(16.dp))
        Box(
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp).size(48.dp).clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .clickable(role = Role.Button, onClickLabel = "Share link", onClick = onShare),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(Icons.Outlined.Share, contentDescription = "Share", color = MaterialTheme.colorScheme.onSurface)
        }
        Box(
            // The Feed card's thumbnail grows into this badge
            modifier = Modifier.align(Alignment.Center).sharedThumb(linkId).size(88.dp).clip(RoundedCornerShape(24.dp)).background(look.badgeBg),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(look.icon, contentDescription = null, color = look.badgeFg, modifier = Modifier.size(40.dp))
        }
    }
}

@Composable
private fun BackButton(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(48.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface)
            .clickable(role = Role.Button, onClickLabel = "Back", onClick = onBack),
        contentAlignment = Alignment.Center,
    ) {
        AppIcon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", color = MaterialTheme.colorScheme.onSurface)
    }
}

/** "Video ▾" chip; the menu sets the link's type. */
@Composable
private fun TypeChip(type: String, onPick: (String) -> Unit) {
    var open by remember { mutableStateOf(false) }
    Box {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .clickable(role = Role.DropdownList, onClickLabel = "Change type", onClick = { open = true })
                .heightIn(min = 32.dp)
                .padding(start = 10.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                type.replaceFirstChar { it.uppercaseChar() },
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
            )
            AppIcon(Icons.Filled.ArrowDropDown, contentDescription = null, color = MaterialTheme.colorScheme.onTertiaryContainer)
        }
        DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
            listOf("article", "video", "social", "other").forEach { t ->
                DropdownMenuItem(
                    text = { Text(t.replaceFirstChar { it.uppercaseChar() }) },
                    leadingIcon = if (t == type) { { AppIcon(Icons.Filled.Check, contentDescription = null) } } else null,
                    onClick = { open = false; if (t != type) onPick(t) },
                )
            }
        }
    }
}

/** Title with a pencil; editing swaps in a field, Done/✓ saves, an empty title is ignored. */
@Composable
private fun TitleBlock(link: Link, onRename: (String) -> Unit) {
    var editing by remember { mutableStateOf(false) }
    val shown = link.title.takeIf { it.isNotBlank() && it != link.url } ?: link.url
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (editing) {
            var draft by remember { mutableStateOf(shown) }
            val save = { if (draft.isNotBlank()) onRename(draft); editing = false }
            AppTextFieldEnhanced(
                value = draft,
                onValueChange = { draft = it },
                placeholder = "Title",
                showClear = true,
                openStyle = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { save() }),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = { editing = false }) { Text("Cancel") }
                TextButton(onClick = save, enabled = draft.isNotBlank()) { Text("Save title") }
            }
        } else {
            Row(verticalAlignment = Alignment.Top) {
                val hasTitle = shown != link.url
                Text(
                    shown,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    // The Feed card's title moves up into this one
                    modifier = Modifier.weight(1f).then(if (hasTitle) Modifier.sharedTitle(link.id) else Modifier).padding(top = 4.dp),
                )
                Box(
                    modifier = Modifier.size(48.dp).clip(CircleShape).clickable(role = Role.Button, onClick = { editing = true }),
                    contentAlignment = Alignment.Center,
                ) {
                    AppIcon(Icons.Outlined.Edit, contentDescription = "Edit title", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            if (!link.isOpened) Box(Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
            Text(
                listOfNotNull(
                    savedAtLabel(link.createdAt)?.let { label ->
                        // "Saved today, 14:32" / "Saved Monday, 18:04" / "Saved 12 Nov 2025"
                        "Saved " + if (label.startsWith("Today") || label.startsWith("Yesterday") || label.startsWith("Just"))
                            label.replaceFirstChar { it.lowercaseChar() } else label
                    },
                    if (link.isOpened) "Opened" else "Unopened",
                    if (link.isArchived) "Archived" else null,
                ).joinToString(" · "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** Yellow note card; edits save automatically after a short pause. */
@Composable
private fun NoteCard(note: String, saving: Boolean, onChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(LordsExtraColors.noteContainer)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text("Your note", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, color = LordsExtraColors.onNoteContainer)
        BasicTextField(
            value = note,
            onValueChange = onChange,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            cursorBrush = SolidColor(LordsExtraColors.onNoteContainer),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { inner ->
                Box {
                    if (note.isEmpty()) {
                        Text("Why did you save this?", style = MaterialTheme.typography.bodyLarge, color = LordsExtraColors.onNoteContainer.copy(alpha = 0.7f))
                    }
                    inner()
                }
            },
        )
        Text(
            if (saving) "Saving…" else "Saved automatically",
            style = MaterialTheme.typography.labelSmall,
            color = LordsExtraColors.onNoteContainer,
        )
    }
}

@Composable
private fun BarIcon(icon: ImageVector, label: String, onClick: () -> Unit, tint: Color = MaterialTheme.colorScheme.inverseOnSurface) {
    Box(
        modifier = Modifier.size(48.dp).clip(CircleShape).clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        AppIcon(icon, contentDescription = label, color = tint)
    }
}

/** ⋮: copy the URL, or delete (asks first). */
@Composable
private fun MoreMenu(onCopy: () -> Unit, onDelete: () -> Unit) {
    var open by remember { mutableStateOf(false) }
    Box {
        BarIcon(Icons.Filled.MoreVert, "More actions", onClick = { open = true })
        DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
            DropdownMenuItem(text = { Text("Copy link") }, onClick = { open = false; onCopy() })
            DropdownMenuItem(
                text = { Text("Delete link", color = MaterialTheme.colorScheme.error) },
                onClick = { open = false; onDelete() },
            )
        }
    }
}
