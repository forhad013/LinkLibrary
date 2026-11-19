package com.greenrobotdev.lordofthelinks.screens.settings

import androidx.compose.runtime.Composable

/**
 * Returns a function that lets the user pick a file and reads it as text (Android: the system
 * "open" picker, no storage permission). [onPicked] gets null if nothing was picked, it couldn't
 * be read, or it's larger than a backup could reasonably be.
 */
@Composable
expect fun rememberTextFileOpener(onPicked: (text: String?) -> Unit): () -> Unit

/** Upper bound for a file we read into memory (about 100k links' worth of backup). */
internal const val MAX_IMPORT_BYTES = 20 * 1024 * 1024
