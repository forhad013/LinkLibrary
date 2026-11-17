package com.greenrobotdev.linklibrary.bookmarks.screens.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString

// No system share sheet here; copying the URL is the closest equivalent
@Composable
internal actual fun rememberShareLink(): (title: String, url: String) -> Unit {
    val clipboard = LocalClipboardManager.current
    return remember(clipboard) { { _, url -> clipboard.setText(AnnotatedString(url)) } }
}
