package com.greenrobotdev.linklibrary.design.theme

/**
 * Lords of Links — Material 3 color scheme from the wireframes.
 * Font throughout: Roboto Flex.
 */
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────
// Light Theme (matches design/screens)
// ─────────────────────────────────────────────────────────────

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2F4FD8),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDDE3FF),
    onPrimaryContainer = Color(0xFF1E36A8),
    secondaryContainer = Color(0xFFE9DDFF),
    onSecondaryContainer = Color(0xFF432A8F),
    tertiary = Color(0xFFFF8A3D),
    onTertiary = Color(0xFF16181D),
    tertiaryContainer = Color(0xFFFFE3CF),
    onTertiaryContainer = Color(0xFF7A3200),
    background = Color(0xFFF4F5FA),
    onBackground = Color(0xFF16181D),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF16181D),
    surfaceVariant = Color(0xFFE6E8F2),
    onSurfaceVariant = Color(0xFF5A5F6B),
    surfaceContainer = Color(0xFFEEF0F7),
    outline = Color(0xFF70768A),
    outlineVariant = Color(0xFFC9CDD9),
    error = Color(0xFF9A2418),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFBE9E7),
    onErrorContainer = Color(0xFF9A2418),
    inverseSurface = Color(0xFF16181D),
    inverseOnSurface = Color(0xFFFFFFFF),
    inversePrimary = Color(0xFFFFB27A),
    scrim = Color.Black
)

// shortcut: wireframes are light-only; dark keeps the same hues darkened, revisit when a dark design exists
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB7C4FF),
    onPrimary = Color(0xFF12236B),
    primaryContainer = Color(0xFF1E36A8),
    onPrimaryContainer = Color(0xFFDDE3FF),
    tertiary = Color(0xFFFFB27A),
    onTertiary = Color(0xFF3A1C00),
    tertiaryContainer = Color(0xFF7A3200),
    onTertiaryContainer = Color(0xFFFFE3CF),
    background = Color(0xFF121318),
    onBackground = Color(0xFFE4E2EC),
    surface = Color(0xFF1A1B21),
    onSurface = Color(0xFFE4E2EC),
    surfaceVariant = Color(0xFF2A2B33),
    onSurfaceVariant = Color(0xFFA0A5B4),
    surfaceContainer = Color(0xFF23242B),
    outline = Color(0xFF70768A),
    outlineVariant = Color(0xFF3E4150),
    error = Color(0xFFFFB4A6),
    onError = Color(0xFF530000),
    errorContainer = Color(0xFF7A1006),
    onErrorContainer = Color(0xFFFFB4A6),
    inverseSurface = Color(0xFFE4E2EC),
    inverseOnSurface = Color(0xFF2E3038),
    inversePrimary = Color(0xFF2F4FD8),
    scrim = Color.Black
)

// ─────────────────────────────────────────────────────────────
// Extra colors outside the Material roles (from the wireframes)
// ─────────────────────────────────────────────────────────────

object LordsExtraColors {
    val noteContainer = Color(0xFFFFF4D6)      // "Your note" box
    val onNoteContainer = Color(0xFF6B4B00)    // note label / note line on cards
    val success = Color(0xFF17633F)            // "Saved" state, "No links are deleted"
    val successContainer = Color(0xFFD7F0E2)   // Uncategorized tile
    val onSuccessContainer = Color(0xFF135C3A)
    val mediaPlaceholder = Color(0xFF1F2A44)   // video thumbnail bg
    val archivedSurface = Color(0xFFECEEF4)    // archived result in search
    val divider = Color(0xFFE3E5EE)            // line above the nav bar
    val textStrong = Color(0xFF3B404B)         // labels, icons on light surfaces
}
