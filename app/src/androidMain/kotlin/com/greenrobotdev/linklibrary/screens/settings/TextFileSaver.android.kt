package com.greenrobotdev.linklibrary.screens.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberTextFileSaver(mimeType: String, onDone: (saved: Boolean) -> Unit): (fileName: String, content: String) -> Unit {
    val context = LocalContext.current
    val done by rememberUpdatedState(onDone)
    var pending by remember { mutableStateOf<String?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument(mimeType)) { uri ->
        val content = pending
        pending = null
        if (uri == null || content == null) {
            done(false)
            return@rememberLauncherForActivityResult
        }
        val saved = runCatching {
            context.contentResolver.openOutputStream(uri)!!.use { it.write(content.encodeToByteArray()) }
        }.isSuccess
        done(saved)
    }
    return remember(launcher) {
        { fileName, content ->
            pending = content
            launcher.launch(fileName)
        }
    }
}
