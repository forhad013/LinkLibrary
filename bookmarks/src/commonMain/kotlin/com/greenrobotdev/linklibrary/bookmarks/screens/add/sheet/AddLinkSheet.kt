package com.greenrobotdev.linklibrary.bookmarks.screens.add.sheet

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.bookmarks.domain.isValidLink
import com.greenrobotdev.linklibrary.design.components.button.StandardButton
import com.greenrobotdev.linklibrary.design.components.textfield.AppTextFieldEnhanced
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon
import com.greenrobotdev.linklibrary.design.theme.ComponentStyle
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull

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

    // The keyboard starts later than the sheet's own slide-in, so keep the sheet below the
    // screen and uncover it by exactly as much as the keyboard has risen: one motion, not two.
    var imeNow by remember { mutableStateOf<WindowInsets?>(null) }
    var imeTarget by remember { mutableStateOf<WindowInsets?>(null) }
    val fallback = remember { Animatable(0f) } // reveal when there is no keyboard to follow
    var opened by remember { mutableStateOf(false) } // latched, so a closing keyboard never hides the sheet

    // 0 = keyboard down, 1 = keyboard fully up
    fun keyboardProgress(density: Density): Float {
        val target = imeTarget?.getBottom(density) ?: 0
        val now = imeNow?.getBottom(density) ?: 0
        return if (target > 0) (now.toFloat() / target).coerceAtMost(1f) else 0f
    }
    // 0 = sheet fully below the screen, 1 = in place
    fun shown(density: Density): Float =
        if (opened) 1f else maxOf(keyboardProgress(density), fallback.value)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        // Read in the draw phase so the sheet moves on the same frame as the keyboard
        modifier = Modifier.graphicsLayer { translationY = (1f - shown(this)) * size.height },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        // Taken inside the sheet: it is its own window with its own keyboard insets
        val ime = WindowInsets.ime
        val target = imeAnimationTargetInsets()
        SideEffect { imeNow = ime; imeTarget = target }
        val density = LocalDensity.current
        LaunchedEffect(Unit) {
            // shortcut: fixed waits when no keyboard shows (hardware keyboard, desktop) or it stops
            // part-way; raise them if a slow IME beats them
            val started = withTimeoutOrNull(500) { snapshotFlow { keyboardProgress(density) > 0f }.first { it } }
            val arrived = started != null &&
                withTimeoutOrNull(1000) { snapshotFlow { shown(density) >= 1f }.first { it } } != null
            if (!arrived) fallback.animateTo(1f, tween(250))
            opened = true
        }
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
    val keyboard = LocalSoftwareKeyboardController.current
    // The sheet is its own window; Android ignores a keyboard request until that window has focus.
    // Asking at that exact moment starts the keyboard while the sheet is still sliding in.
    val windowFocused = LocalWindowInfo.current.isWindowFocused
    LaunchedEffect(windowFocused) {
        if (windowFocused) {
            runCatching { focusRequester.requestFocus() }
            keyboard?.show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            // Dragging the content down takes the keyboard with it; needs a scroll container to hook into
            .imeNestedScroll()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
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
                // gap between the buttons and the keyboard
                .padding(bottom = 16.dp),
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
