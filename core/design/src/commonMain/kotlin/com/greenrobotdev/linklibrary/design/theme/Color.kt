package com.greenrobotdev.linklibrary.design.theme

/**
 * Material Design 3 Color Scheme
 * Separate color definitions for Light and Dark themes
 */
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────
// TheLordsOfLinks — Brand Colors
// ─────────────────────────────────────────────────────────────

val RoyalPurple = Color(0xFF6D28D9)
val RoyalPurpleDark = Color(0xFF5B21B6)
val RoyalPurpleLight = Color(0xFF8B5CF6)

val Gold = Color(0xFFF59E0B)
val GoldDark = Color(0xFFD97706)
val GoldLight = Color(0xFFFBBF24)


// ─────────────────────────────────────────────────────────────
// Light Theme
// ─────────────────────────────────────────────────────────────

val LightColorScheme = lightColorScheme(
    primary = RoyalPurple,
    onPrimary = Color.White,

    primaryContainer = Color(0xFFEDE9FE),
    onPrimaryContainer = Color(0xFF2E1065),

    secondary = Gold,
    onSecondary = Color(0xFF3B2500),

    secondaryContainer = Color(0xFFFFE8B0),
    onSecondaryContainer = Color(0xFF4A3000),

    tertiary = Color(0xFF7C3AED),
    onTertiary = Color.White,

    tertiaryContainer = Color(0xFFEDE9FE),
    onTertiaryContainer = Color(0xFF2E1065),

    background = Color(0xFFFAF9FC),
    onBackground = Color(0xFF18181B),

    surface = Color.White,
    onSurface = Color(0xFF18181B),

    surfaceVariant = Color(0xFFEDEAF2),
    onSurfaceVariant = Color(0xFF625F69),

    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFD0CBD5),

    inverseSurface = Color(0xFF302E33),
    inverseOnSurface = Color(0xFFF5F0F7),
    inversePrimary = Color(0xFFCBB5FF),

    scrim = Color.Black
)


// ─────────────────────────────────────────────────────────────
// Dark Theme
// ─────────────────────────────────────────────────────────────

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8B5CF6),
    onPrimary = Color(0xFF2E1065),

    primaryContainer = Color(0xFF2E1065),
    onPrimaryContainer = Color(0xFFE9DDFF),

    secondary = Color(0xFFFBBF24),
    onSecondary = Color(0xFF3B2500),

    secondaryContainer = Color(0xFF5C4300),
    onSecondaryContainer = Color(0xFFFFE8B0),

    tertiary = Color(0xFFA78BFA),
    onTertiary = Color(0xFF32105F),

    tertiaryContainer = Color(0xFF4C1D95),
    onTertiaryContainer = Color(0xFFE9DDFF),

    background = Color(0xFF100B1A),
    onBackground = Color(0xFFF5F3FF),

    surface = Color(0xFF1C1428),
    onSurface = Color(0xFFF5F3FF),

    surfaceVariant = Color(0xFF2A2135),
    onSurfaceVariant = Color(0xFFA1A1AA),

    outline = Color(0xFF938D9B),
    outlineVariant = Color(0xFF403747),

    inverseSurface = Color(0xFFF5F3FF),
    inverseOnSurface = Color(0xFF302E33),
    inversePrimary = Color(0xFF6D28D9),

    scrim = Color.Black
)