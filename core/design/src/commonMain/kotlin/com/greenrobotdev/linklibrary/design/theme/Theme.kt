package com.greenrobotdev.linklibrary.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Material Design 3 Theme
 *
 * Main theme composable providing complete Material Design 3 styling
 * with custom colors, typography, and shapes based on HTML design template.
 */
@Composable
fun LinkLibraryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            // Primary colors
            primary = Primary,
            onPrimary = OnPrimary,
            primaryContainer = PrimaryContainer,
            onPrimaryContainer = OnPrimaryContainer,

            // Secondary colors
            secondary = Secondary,
            onSecondary = OnSecondary,
            secondaryContainer = SecondaryContainer,
            onSecondaryContainer = OnSecondaryContainer,

            // Tertiary colors
            tertiary = Tertiary,
            onTertiary = OnTertiary,
            tertiaryContainer = TertiaryContainer,
            onTertiaryContainer = OnTertiaryContainer,

            // Error colors
            error = Error,
            onError = OnError,
            errorContainer = ErrorContainer,
            onErrorContainer = OnErrorContainer,

            // Background colors
            background = InverseSurface,
            onBackground = InverseOnSurface,

            // Surface colors
            surface = InverseSurface,
            onSurface = InverseOnSurface,
            onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF9AA0A6),
            surfaceVariant = SurfaceVariant,
            surfaceContainerLowest = androidx.compose.ui.graphics.Color(0xFF1A1A1A),
            surfaceContainerLow = androidx.compose.ui.graphics.Color(0xFF242424),
            surfaceContainer = androidx.compose.ui.graphics.Color(0xFF2C2C2C),
            surfaceContainerHigh = androidx.compose.ui.graphics.Color(0xFF343434),
            surfaceContainerHighest = androidx.compose.ui.graphics.Color(0xFF3C3C3C),
            surfaceDim = androidx.compose.ui.graphics.Color(0xFF202020),
            surfaceBright = androidx.compose.ui.graphics.Color(0xFF363636),

            // Outline colors
            outline = androidx.compose.ui.graphics.Color(0xFF727785),
            outlineVariant = androidx.compose.ui.graphics.Color(0xFF5F6368),

            // Inverse colors
            inversePrimary = InversePrimary,
            inverseSurface = androidx.compose.ui.graphics.Color(0xFFE8EAED),
            inverseOnSurface = androidx.compose.ui.graphics.Color(0xFF1A1A1A)
        )
    } else {
        lightColorScheme(
            // Primary colors
            primary = Primary,
            onPrimary = OnPrimary,
            primaryContainer = PrimaryContainer,
            onPrimaryContainer = OnPrimaryContainer,

            // Secondary colors
            secondary = Secondary,
            onSecondary = OnSecondary,
            secondaryContainer = SecondaryContainer,
            onSecondaryContainer = OnSecondaryContainer,

            // Tertiary colors
            tertiary = Tertiary,
            onTertiary = OnTertiary,
            tertiaryContainer = TertiaryContainer,
            onTertiaryContainer = OnTertiaryContainer,

            // Error colors
            error = Error,
            onError = OnError,
            errorContainer = ErrorContainer,
            onErrorContainer = OnErrorContainer,

            // Background colors
            background = Background,
            onBackground = OnBackground,

            // Surface colors
            surface = Surface,
            onSurface = OnSurface,
            onSurfaceVariant = OnSurfaceVariant,
            surfaceVariant = SurfaceVariant,
            surfaceContainerLowest = SurfaceContainerLowest,
            surfaceContainerLow = SurfaceContainerLow,
            surfaceContainer = SurfaceContainer,
            surfaceContainerHigh = SurfaceContainerHigh,
            surfaceContainerHighest = SurfaceContainerHighest,
            surfaceDim = SurfaceDim,
            surfaceBright = SurfaceBright,

            // Outline colors
            outline = Outline,
            outlineVariant = OutlineVariant,

            // Inverse colors
            inversePrimary = InversePrimary,
            inverseSurface = InverseSurface,
            inverseOnSurface = InverseOnSurface,

            // Surface tint
            surfaceTint = SurfaceTint
        )
    }

    val typography = getTypography()
    val shapes = getShapes()

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
