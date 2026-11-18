package com.greenrobotdev.linklibrary.screens.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import java.io.ByteArrayOutputStream

@Composable
actual fun rememberTextFileOpener(onPicked: (text: String?) -> Unit): () -> Unit {
    val context = LocalContext.current
    val picked by rememberUpdatedState(onPicked)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult picked(null)
        val text = runCatching {
            context.contentResolver.openInputStream(uri)!!.use { input ->
                // Stop one byte past the limit, so an oversized file is refused, not loaded
                val out = ByteArrayOutputStream()
                val buffer = ByteArray(64 * 1024)
                while (out.size() <= MAX_IMPORT_BYTES) {
                    val n = input.read(buffer)
                    if (n < 0) break
                    out.write(buffer, 0, n)
                }
                if (out.size() > MAX_IMPORT_BYTES) null else out.toByteArray().decodeToString()
            }
        }.getOrNull()
        picked(text)
    }
    // Backups are .json; some file managers report them as octet-stream or plain text
    return remember(launcher) { { launcher.launch(arrayOf("application/json", "application/octet-stream", "text/plain")) } }
}
