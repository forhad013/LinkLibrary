package com.greenrobotdev.linklibrary.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Lords of Links theme — light scheme from the approved wireframes.
 * Dark scheme kept from before; a wireframe-matched dark design is pending.
 */
@Composable
fun LinkLibraryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalLordsExtraColors provides LordsExtraColors) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
            typography = getTypography(),
            content = content
        )
    }
}

val LocalLordsExtraColors = staticCompositionLocalOf { LordsExtraColors }
