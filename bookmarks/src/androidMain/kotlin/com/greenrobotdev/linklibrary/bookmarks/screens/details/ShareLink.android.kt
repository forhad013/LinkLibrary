package com.greenrobotdev.linklibrary.bookmarks.screens.details

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
internal actual fun rememberShareLink(): (title: String, url: String) -> Unit {
    val context = LocalContext.current
    return remember(context) {
        { title, url ->
            val send = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, url)
                if (title.isNotBlank()) putExtra(Intent.EXTRA_SUBJECT, title)
            }
            val chooser = Intent.createChooser(send, null)
            if (context !is Activity) chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        }
    }
}
