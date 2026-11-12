package com.greenrobotdev.linklibrary.design.components.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * iOS implementation with placeholder for async image loading
 * Note: Full async image support would require platform-specific implementation using KMP libraries
 */
@Composable
actual fun PlatformAsyncImage(
    model: String?,
    contentDescription: String?,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Image,
            contentDescription = contentDescription ?: "Image placeholder",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
