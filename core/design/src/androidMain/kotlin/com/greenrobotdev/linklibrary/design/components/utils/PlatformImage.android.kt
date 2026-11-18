package com.greenrobotdev.linklibrary.design.components.utils

import com.greenrobotdev.linklibrary.design.debug.DebugLog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import kotlinx.coroutines.delay

private const val RETRIES = 2

/**
 * One loader for the app (memory + disk cache). It ignores the server's cache headers: many image
 * CDNs send no-cache or short lifetimes, which made every app start download previews again, and
 * a page's preview image practically never changes.
 */
private var appImageLoader: ImageLoader? = null

private fun imageLoader(context: Context): ImageLoader =
    appImageLoader ?: ImageLoader.Builder(context.applicationContext)
        .respectCacheHeaders(false)
        .build()
        .also { appImageLoader = it }
// Image CDNs (Medium, Cloudflare) often refuse OkHttp's default user agent
private const val BROWSER_USER_AGENT =
    "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0 Mobile Safari/537.36"

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
    contentScale: ContentScale,
) {
    val context = LocalContext.current
    var state by remember(model) { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }
    var attempt by remember(model) { mutableIntStateOf(0) }
    val request = remember(model, attempt) {
        ImageRequest.Builder(context)
            .data(model)
            // Fixed size: don't wait for layout, which is still animating during shared transitions.
            // shortcut: decodes full size even for small thumbnails; pass a size per call site if long lists get heavy
            .size(Size.ORIGINAL)
            .crossfade(true)
            .addHeader("User-Agent", BROWSER_USER_AGENT)
            // A new parameter makes Coil treat a retry as a new request
            .setParameter("attempt", attempt, memoryCacheKey = null)
            .build()
    }
    // First loads often fail on a cold connection; retry a couple of times before giving up
    val failed = state is AsyncImagePainter.State.Error
    LaunchedEffect(failed, attempt) {
        if (failed && attempt < RETRIES) {
            delay(1_000L * (attempt + 1))
            attempt++
        }
    }
    Box(modifier, contentAlignment = Alignment.Center) {
        AsyncImage(
            model = request,
            imageLoader = imageLoader(context),
            contentDescription = contentDescription,
            // Neutral backing once a cropped image loads: transparent logos otherwise show the tile's
            // color through. Fitted images keep the caller's background around them instead.
            modifier = Modifier.matchParentSize()
                .then(if (state is AsyncImagePainter.State.Success && contentScale == ContentScale.Crop) Modifier.background(MaterialTheme.colorScheme.surface) else Modifier),
            contentScale = contentScale,
            onState = {
                state = it
                // Debug builds: filter logcat for "LoL/Image" to see why an image is missing
                when (it) {
                    // from MEMORY_CACHE / DISK / NETWORK: shows whether the cache is doing its job
                    is AsyncImagePainter.State.Success -> DebugLog.d("Image") { "loaded from ${it.result.dataSource} (try ${attempt + 1}) $model" }
                    is AsyncImagePainter.State.Error -> DebugLog.d("Image") { "failed (try ${attempt + 1}) $model: ${it.result.throwable}" }
                    else -> Unit
                }
            },
        )
        when {
            // Empty = the request hasn't started yet; treat it like loading rather than an empty tile
            state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Empty ||
                (failed && attempt < RETRIES) -> loading?.invoke()
            failed -> failure?.invoke()
        }
    }
}
