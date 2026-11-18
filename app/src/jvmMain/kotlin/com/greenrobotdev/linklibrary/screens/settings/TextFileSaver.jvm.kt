package com.greenrobotdev.linklibrary.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import java.io.File

// shortcut: desktop writes straight to ~/Downloads; add a file chooser when desktop ships
@Composable
actual fun rememberTextFileSaver(mimeType: String, onDone: (saved: Boolean) -> Unit): (fileName: String, content: String) -> Unit {
    val done = rememberUpdatedState(onDone)
    return remember {
        { fileName, content ->
            val dir = File(System.getProperty("user.home"), "Downloads").apply { mkdirs() }
            done.value(runCatching { File(dir, fileName).writeText(content) }.isSuccess)
        }
    }
}
