package com.greenrobotdev.linklibrary.bookmarks.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.greenrobotdev.linklibrary.design.components.utils.PlatformAsyncImage
import com.greenrobotdev.linklibrary.design.motion.shimmer

/**
 * A link's preview image filling [modifier]'s bounds, with every state covered: no image URL or a
 * failed load shows [fallback] (the type icon), and a shimmer shows while it downloads.
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
            loading = { Box(Modifier.fillMaxSize().shimmer()) },
            failure = fallback,
        )
    }
}
