package com.greenrobotdev.linklibrary.design.components.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter

/**
 * Android implementation using Coil for async image loading
 */
@Composable
actual fun PlatformAsyncImage(
    model: String?,
    contentDescription: String?,
    modifier: Modifier,
    loading: (@Composable () -> Unit)?,
    failure: (@Composable () -> Unit)?,
) {
    var state by remember(model) { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }
    Box(modifier, contentAlignment = Alignment.Center) {
        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
            onState = { state = it },
        )
        when (state) {
            is AsyncImagePainter.State.Loading -> loading?.invoke()
            is AsyncImagePainter.State.Error -> failure?.invoke()
            else -> Unit
        }
    }
}
