package com.greenrobotdev.linklibrary.screens.notes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey

/**
 * Note Editor Screen - Rich text editor for annotating articles
 * Matches Figma design "Lords of Links Note Editor"
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    routeKey: NavKey,
    noteId: String? = null,
    onBack: () -> Unit = {}
) {
    val viewModel: NoteEditorViewModel = viewModel(key = routeKey.toString()) {
        NoteEditorViewModel(noteId)
    }

    // Use the molecule flow from ViewModel
    val state by viewModel.models.collectAsState()

    // Rich text editor state
    var titleValue by remember { mutableStateOf(TextFieldValue(text = state.note.title)) }
    var contentValue by remember { mutableStateOf(TextFieldValue(text = state.note.content)) }
    var showLinkDialog by remember { mutableStateOf(false) }

    // Text formatting state
    var isBold by remember { mutableStateOf(false) }
    var isItalic by remember { mutableStateOf(false) }
    var isUnderline by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Note Editor") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.take(NoteEditorEvent.SaveNote) },
                        enabled = !state.isSaving && titleValue.text.isNotBlank()
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = "Save Note"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Title Field
            OutlinedTextField(
                value = titleValue,
                onValueChange = {
                    titleValue = it
                    viewModel.take(NoteEditorEvent.TitleChanged(it.text))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text("Title") },
                placeholder = { Text("Enter note title...") },
                leadingIcon = {
                    Icon(Icons.Default.Title, contentDescription = null)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Formatting Toolbar
            FormattingToolbar(
                isBold = isBold,
                isItalic = isItalic,
                isUnderline = isUnderline,
                onBoldClick = { isBold = !it },
                onItalicClick = { isItalic = !it },
                onUnderlineClick = { isUnderline = !it },
                onAttachLinkClick = { showLinkDialog = true }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Content Editor
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                SelectionContainer {
                    BasicTextField(
                        value = contentValue,
                        onValueChange = {
                            contentValue = it
                            viewModel.take(NoteEditorEvent.ContentChanged(it.text))
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .weight(1f),
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                            fontFamily = FontFamily.Default,
                            textDecoration = if (isUnderline)
                                TextDecoration.Underline else TextDecoration.None
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (contentValue.text.isEmpty()) {
                                    Text(
                                        "Start writing your note...",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            // Attach Link info
            if (state.note.attachedLinkId != null) {
                AttachedLinkCard(
                    linkId = state.note.attachedLinkId!!,
                    onDetach = { viewModel.take(NoteEditorEvent.DetachLink) }
                )
            }
        }
    }

    // Link Attachment Dialog
    if (showLinkDialog) {
        LinkAttachmentDialog(
            availableLinks = state.availableLinks,
            onLinkSelected = { linkId ->
                viewModel.take(NoteEditorEvent.AttachLink(linkId))
                showLinkDialog = false
            },
            onDismiss = { showLinkDialog = false }
        )
    }
}

/**
 * Formatting toolbar for rich text editing
 */
@Composable
private fun FormattingToolbar(
    isBold: Boolean,
    isItalic: Boolean,
    isUnderline: Boolean,
    onBoldClick: (Boolean) -> Unit,
    onItalicClick: (Boolean) -> Unit,
    onUnderlineClick: (Boolean) -> Unit,
    onAttachLinkClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Bold
        FormattingButton(
            icon = Icons.Default.FormatBold,
            isSelected = isBold,
            onClick = { onBoldClick(!isBold) }
        )

        // Italic
        FormattingButton(
            icon = Icons.Default.FormatItalic,
            isSelected = isItalic,
            onClick = { onItalicClick(!isItalic) }
        )

        // Underline
        FormattingButton(
            icon = Icons.Default.FormatUnderlined,
            isSelected = isUnderline,
            onClick = { onUnderlineClick(!isUnderline) }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Attach Link
        IconButton(
            onClick = onAttachLinkClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AttachFile,
                contentDescription = "Attach Link",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Individual formatting button
 */
@Composable
private fun FormattingButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            Color.Transparent
        },
        modifier = Modifier.size(36.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier.padding(8.dp)
        )
    }
}

/**
 * Card showing attached link
 */
@Composable
private fun AttachedLinkCard(
    linkId: String,
    onDetach: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Link attached",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            IconButton(onClick = onDetach) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Detach",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

/**
 * Dialog for selecting links to attach
 */
@Composable
private fun LinkAttachmentDialog(
    availableLinks: List<LinkSuggestion>,
    onLinkSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Attach Link") },
        text = {
            if (availableLinks.isEmpty()) {
                Text("No links available. Create some links first.")
            } else {
                LazyColumn(
                    modifier = Modifier.height(200.dp)
                ) {
                    items(availableLinks) { link ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = { onLinkSelected(link.id) }
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = link.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 1
                                )
                                Text(
                                    text = link.url,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
