package com.greenrobotdev.linklibrary.bookmarks.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.components.utils.PlatformAsyncImage

/**
 * A link's preview image filling [modifier]'s bounds, with every state covered: no image URL or a
 * failed load shows [fallback] (the type icon), and a loading indicator shows while it downloads.
 */
@Composable
internal fun LinkPreviewImage(imageUrl: String?, modifier: Modifier, fallback: @Composable () -> Unit) {
    if (imageUrl == null) {
        Box(modifier, contentAlignment = Alignment.Center) { fallback() }
    } else {
        PlatformAsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = modifier,
            // shortcut: plain spinner; switch to the expressive LoadingIndicator once this module's material3 has it
            loading = { CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp) },
            failure = fallback,
        )
    }
}
