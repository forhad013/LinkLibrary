package com.greenrobotdev.linklibrary.design.components.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

/**
 * Platform-specific async image loading. [loading] shows while the image downloads and [failure]
 * if it can't be loaded, both centered over the image's bounds (Android; other platforms still
 * draw a placeholder and ignore them).
 */
@Composable
expect fun PlatformAsyncImage(
    model: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    loading: (@Composable () -> Unit)? = null,
    failure: (@Composable () -> Unit)? = null,
    /** Crop fills the bounds (thumbnails); Fit shows the whole image (Link details). */
    contentScale: ContentScale = ContentScale.Crop,
)
