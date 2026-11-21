package com.greenrobotdev.lordofthelinks.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import java.io.File

// shortcut: desktop reads ~/Downloads/lords-of-links-backup.json; add a file chooser when desktop ships
@Composable
actual fun rememberTextFileOpener(onPicked: (text: String?) -> Unit): () -> Unit {
    val picked = rememberUpdatedState(onPicked)
    return remember {
        {
            val file = File(System.getProperty("user.home"), "Downloads/lords-of-links-backup.json")
            picked.value(runCatching { file.takeIf { it.length() <= MAX_IMPORT_BYTES }?.readText() }.getOrNull())
        }
    }
}
