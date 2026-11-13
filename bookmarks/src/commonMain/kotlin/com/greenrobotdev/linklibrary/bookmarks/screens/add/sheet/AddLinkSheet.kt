package com.greenrobotdev.linklibrary.bookmarks.screens.add.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.bookmarks.domain.isValidLink
import com.greenrobotdev.linklibrary.design.components.button.StandardButton
import com.greenrobotdev.linklibrary.design.components.textfield.AppTextFieldEnhanced
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon
import com.greenrobotdev.linklibrary.design.theme.ComponentStyle
import kotlinx.coroutines.delay

/**
 * Step one of adding a link, as a modal bottom sheet.
 * Continue hands the URL to the existing full add-link screen (category/tags/notes).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLinkSheet(
    onContinue: (String) -> Unit,
    onDismiss: () -> Unit,
    initialUrl: String? = null,
) {
    // Read the clipboard once, not per recomposition (Android 12+ shows a system toast per read)
    val clipboardManager = LocalClipboardManager.current
    val clipboardUrl = remember(clipboardManager) {
        clipboardManager.getText()?.text?.takeIf { isValidLink(it) }
    }
    val viewModel = remember { AddLinkSheetViewModel(clipboardUrl, initialUrl) }
    val state by viewModel.models.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        AddLinkSheetContent(
            state = state,
            onUrlChange = { viewModel.take(AddLinkSheetEvent.UrlChanged(it)) },
            onContinue = onContinue,
            onCancel = onDismiss,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
                .imePadding(),
        )
    }
}

@Composable
private fun AddLinkSheetContent(
    state: AddLinkSheetState,
    onUrlChange: (String) -> Unit,
    onContinue: (String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    // shortcut: fixed delay instead of observing sheet settle state; revisit if enter animation changes
    LaunchedEffect(Unit) {
        delay(250)
        runCatching { focusRequester.requestFocus() }
    }

    Column(modifier = modifier.fillMaxWidth().imePadding(), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                "Add a link",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                "Paste or type a web address. You can add a category, tags and a note next.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        AppTextFieldEnhanced(
            value = state.url,
            onValueChange = onUrlChange,
            label = "Link",
            placeholder = "https://",
            isError = state.showError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { if (state.isValid) onContinue(state.url.trim()) }
            ),
            textFieldModifier = Modifier.focusRequester(focusRequester),
            openStyle = true,
        )

        // Helper line: empty by default, error text only for non-empty invalid input
        Text(
            text = if (state.showError) "This is not a valid link" else "",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = if (state.showError) {
                Modifier.semantics { liveRegion = LiveRegionMode.Assertive }
            } else {
                Modifier
            },
        )

        if (state.showClipboardRow) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onUrlChange(state.clipboardUrl!!) }
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AppIcon(
                    imageVector = Icons.Filled.ContentPaste,
                    contentDescription = "Paste from clipboard",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Column {
                    Text(
                        "Paste from clipboard",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        state.clipboardUrl!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            StandardButton(
                onClick = onCancel,
                text = "Cancel",
                style = ComponentStyle.Tonal,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 56.dp),
                textStyleOverride = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )
            StandardButton(
                onClick = { onContinue(state.url.trim()) },
                text = "Continue →",
                enabled = state.isValid,
                modifier = Modifier
                    .weight(2f)
                    .heightIn(min = 56.dp),
                textStyleOverride = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
            )
        }
    }
}
