package com.greenrobotdev.linklibrary.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Lords of Links Design System Colors
 *
 * Based on Material 3 Expressive with custom brand colors:
 * Primary: #4c22bd (Violet) - Main brand color for primary actions
 * Secondary: #6349bf (Light Purple) - Secondary accents and highlights
 * Tertiary: #4a464b (Gray) - Supporting elements and neutral tones
 */
private val LordsLightColorScheme = lightColorScheme(
    // Primary Colors
    primary = Color(0xFF4c22bd),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFFded4ff),

    // Secondary Colors
    secondary = Color(0xFF6349bf),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF9f86ff),
    onSecondaryContainer = Color(0xFF340d90),

    // Tertiary Colors
    tertiary = Color(0xFF4a464b),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFF8F1F6),
    onTertiaryContainer = Color(0xFFdfd8de),

    // Error Colors
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFffdad6),
    onErrorContainer = Color(0xFF93000a),

    // Background & Surface Colors
    background = Color(0xFFfcf8f9),
    onBackground = Color(0xFF1b1b1c),
    surface = Color(0xFFFEFBFF),
    onSurface = Color(0xFF1b1b1c),
    surfaceVariant = Color(0xFFe5e2e3),
    onSurfaceVariant = Color(0xFF484554),

    // Surface Container Colors (Tonal elevation system)
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFf6f3f4),
    surfaceContainer = Color(0xFFF0EDED),
    surfaceContainerHigh = Color(0xFFeae7e8),
    surfaceContainerHighest = Color(0xFFe5e2e3),

    // Inverse Colors
    inverseSurface = Color(0xFF303031),
    inverseOnSurface = Color(0xFFf3f0f1),
    inversePrimary = Color(0xFFcbbeff),

    // Outline Colors
    outline = Color(0xFF797586),
    outlineVariant = Color(0xFFcac4d7),

    // Surface Brightness & Dim
    surfaceDim = Color(0xFFdcd9da),
    surfaceBright = Color(0xFFfcf8f9),

    // Fixed Colors (for dynamic color scenarios)
    primaryFixed = Color(0xFFe7deff),
    primaryFixedDim = Color(0xFFcbbeff),
    onPrimaryFixed = Color(0xFF1e0061),
    onPrimaryFixedVariant = Color(0xFF4b21bd),

    secondaryFixed = Color(0xFFe7deff),
    secondaryFixedDim = Color(0xFFccbeff),
    onSecondaryFixed = Color(0xFF1e0060),
    onSecondaryFixedVariant = Color(0xFF4b2ea6),

    tertiaryFixed = Color(0xFFe8e0e6),
    tertiaryFixedDim = Color(0xFFcbc4ca),
    onTertiaryFixed = Color(0xFF1d1b1f),
    onTertiaryFixedVariant = Color(0xFF49454a),

    // Surface Tint
    surfaceTint = Color(0xFF6341d5)
)

/**
 * Dark theme color scheme for Lords of Links
 */
private val LordsDarkColorScheme = darkColorScheme(
    // Primary Colors
    primary = Color(0xFFcbbeff),
    onPrimary = Color(0xFF1e0061),
    primaryContainer = Color(0xFF4b21bd),
    onPrimaryContainer = Color(0xFFEADDFF),

    // Secondary Colors
    secondary = Color(0xFFccbeff),
    onSecondary = Color(0xFF1e0060),
    secondaryContainer = Color(0xFF4b2ea6),
    onSecondaryContainer = Color(0xFFe7deff),

    // Tertiary Colors
    tertiary = Color(0xFFcbc4ca),
    onTertiary = Color(0xFF1d1b1f),
    tertiaryContainer = Color(0xFF49454a),
    onTertiaryContainer = Color(0xFFe8e0e6),

    // Error Colors
    error = Color(0xFFffb4ab),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000a),
    onErrorContainer = Color(0xFFffdad6),

    // Background & Surface Colors
    background = Color(0xFF141218),
    onBackground = Color(0xFFe6e0e9),
    surface = Color(0xFF141218),
    onSurface = Color(0xFFe6e0e9),
    surfaceVariant = Color(0xFF49454f),
    onSurfaceVariant = Color(0xFFcac4d0),

    // Surface Container Colors
    surfaceContainerLowest = Color(0xFF000000),
    surfaceContainerLow = Color(0xFF1d1b1f),
    surfaceContainer = Color(0xFF211f26),
    surfaceContainerHigh = Color(0xFF2b2930),
    surfaceContainerHighest = Color(0xFF36343c),

    // Inverse Colors
    inverseSurface = Color(0xFFe6e0e9),
    inverseOnSurface = Color(0xFF303031),
    inversePrimary = Color(0xFF6341d5),

    // Outline Colors
    outline = Color(0xFF938f99),
    outlineVariant = Color(0xFF49454f),

    // Surface Brightness & Dim
    surfaceDim = Color(0xFF141218),
    surfaceBright = Color(0xFF3a383e),

    // Fixed Colors
    primaryFixed = Color(0xFFe7deff),
    primaryFixedDim = Color(0xFFcbbeff),
    onPrimaryFixed = Color(0xFF1e0061),
    onPrimaryFixedVariant = Color(0xFF4b21bd),

    secondaryFixed = Color(0xFFe7deff),
    secondaryFixedDim = Color(0xFFccbeff),
    onSecondaryFixed = Color(0xFF1e0060),
    onSecondaryFixedVariant = Color(0xFF4b2ea6),

    tertiaryFixed = Color(0xFFe8e0e6),
    tertiaryFixedDim = Color(0xFFcbc4ca),
    onTertiaryFixed = Color(0xFF1d1b1f),
    onTertiaryFixedVariant = Color(0xFF49454a),

    // Surface Tint
    surfaceTint = Color(0xFFcbbeff)
)

/**
 * Material 3 Expressive Theme Configuration for Lords of Links
 *
 * This theme implements the complete Lords of Links design system with:
 * - Custom color palette matching HTML design specifications exactly
 * - Spring-based motion physics via MaterialExpressiveTheme
 * - Enhanced ripple effects and expressive interactions
 * - Dynamic color schemes with proper tonal elevation
 * - Expressive component behaviors and animations
 *
 * Color System:
 * - Primary: #4c22bd (Violet) - Main brand color for primary actions
 * - Secondary: #6349bf (Light Purple) - Secondary accents and highlights
 * - Tertiary: #4a464b (Gray) - Supporting elements and neutral tones
 * - Surface: Expressive tonal elevation system with proper contrast
 *
 * Based on Compose Multiplatform with Material 3 Expressive support
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MaterialExpressiveAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }


    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        content = content
    )
}

/**
 * Material 3 Expressive Theme with modifier support
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MaterialExpressiveAppTheme(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        LordsDarkColorScheme
    } else {
        LordsLightColorScheme
    }

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        motionScheme = MaterialTheme.motionScheme,
        content = content
    )
}

/**
 * Standard MaterialTheme wrapper for compatibility with Lords of Links colors
 */
@Composable
fun MaterialAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        LordsDarkColorScheme
    } else {
        LordsLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

/**
 * MaterialTheme with modifier support for compatibility
 */
@Composable
fun MaterialAppTheme(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        LordsDarkColorScheme
    } else {
        LordsLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

/**
 * Surface with Material 3 standard features
 */
@Composable
fun ExpressiveSurface(
    content: @Composable () -> Unit
) {
    Surface(
        content = content
    )
}

/**
 * Surface with Material 3 standard features and modifier support
 */
@Composable
fun ExpressiveSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        content = content
    )
}
