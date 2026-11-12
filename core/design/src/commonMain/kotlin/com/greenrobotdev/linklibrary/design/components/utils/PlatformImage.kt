package com.greenrobotdev.linklibrary.design.components.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Expect declaration for platform-specific async image loading
 * Actual implementations provided for Android, JVM, and other platforms
 */
@Composable
expect fun PlatformAsyncImage(
    model: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier
)
