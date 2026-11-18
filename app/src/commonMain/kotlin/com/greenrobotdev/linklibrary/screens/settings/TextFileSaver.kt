package com.greenrobotdev.linklibrary.screens.settings

import androidx.compose.runtime.Composable

/**
 * Returns a function that saves text to a file the user picks (Android: the system "save to" picker,
 * no storage permission). [onDone] gets true once written, false if cancelled or it failed.
 */
@Composable
expect fun rememberTextFileSaver(mimeType: String, onDone: (saved: Boolean) -> Unit): (fileName: String, content: String) -> Unit
