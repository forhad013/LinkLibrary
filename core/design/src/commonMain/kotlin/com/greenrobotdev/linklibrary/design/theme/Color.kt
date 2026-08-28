package com.greenrobotdev.linklibrary.design.theme

import androidx.compose.ui.graphics.Color

/**
 * Material Design 3 Expressive Color Scheme
 * Based on lords_of_links_add_link HTML design template with exact RGB values
 * Primary: Purple theme (#4c22bd)
 */

// Primary Colors - Purple theme
internal val Primary = Color(0xFF4C22BD)           // rgb(76, 34, 189) - Primary purple
internal val PrimaryFixed = Color(0xFFE7DEFF)      // rgb(231, 222, 255)
internal val PrimaryFixedDim = Color(0xFFCBBEFF)   // rgb(203, 190, 255)
internal val OnPrimary = Color(0xFFFFFFFF)         // rgb(255, 255, 255)
internal val OnPrimaryFixed = Color(0xFF1E0061)    // rgb(30, 0, 97)
internal val OnPrimaryFixedVariant = Color(0xFF4B21BD) // rgb(75, 33, 189)
internal val PrimaryContainer = Color(0xFFEADDFF)  // rgb(234, 221, 255)
internal val OnPrimaryContainer = Color(0xFFDED4FF) // rgb(222, 212, 255)

// Secondary Colors - Light purple theme
internal val Secondary = Color(0xFF6349BF)         // rgb(99, 73, 191) - Light purple
internal val SecondaryContainer = Color(0xFF9F86FF) // rgb(159, 134, 255)
internal val SecondaryFixed = Color(0xFFE7DEFF)   // rgb(231, 222, 255)
internal val SecondaryFixedDim = Color(0xFFCCBEFF) // rgb(204, 190, 255)
internal val OnSecondary = Color(0xFFFFFFFF)      // rgb(255, 255, 255)
internal val OnSecondaryContainer = Color(0xFF340D90) // rgb(52, 13, 144)
internal val OnSecondaryFixed = Color(0xFF1E0060) // rgb(30, 0, 96)
internal val OnSecondaryFixedVariant = Color(0xFF4B2EA6) // rgb(75, 46, 166)

// Tertiary Colors - Neutral gray theme
internal val Tertiary = Color(0xFF4A464B)         // rgb(74, 70, 75) - Neutral
internal val TertiaryContainer = Color(0xFF625E63) // rgb(98, 94, 99)
internal val TertiaryFixed = Color(0xFFE8E0E6)    // rgb(232, 224, 230)
internal val TertiaryFixedDim = Color(0xFFCBC4CA)  // rgb(203, 196, 202)
internal val OnTertiary = Color(0xFFFFFFFF)       // rgb(255, 255, 255)
internal val OnTertiaryContainer = Color(0xFFDFD8DE) // rgb(223, 216, 222)
internal val OnTertiaryFixed = Color(0xFF1D1B1F)   // rgb(29, 27, 31)
internal val OnTertiaryFixedVariant = Color(0xFF49454A) // rgb(73, 69, 74)

// Error Colors
internal val Error = Color(0xFFBA1A1A)            // rgb(186, 26, 26)
internal val ErrorContainer = Color(0xFFFFDAD6)   // rgb(255, 218, 214)
internal val OnError = Color(0xFFFFFFFF)           // rgb(255, 255, 255)
internal val OnErrorContainer = Color(0xFF93000A)  // rgb(147, 0, 10)

// Background and Surface Colors - Light theme
internal val Background = Color(0xFFFCF8F9)        // rgb(252, 248, 249) - Main background
internal val OnBackground = Color(0xFF1B1B1C)     // rgb(27, 27, 28) - Text primary
internal val Surface = Color(0xFFFEFBFF)           // rgb(254, 251, 255) - Cards
internal val OnSurface = Color(0xFF1B1B1C)         // rgb(27, 27, 28)
internal val OnSurfaceVariant = Color(0xFF484554)   // rgb(72, 69, 84) - Text secondary
internal val SurfaceVariant = Color(0xFFE5E2E3)    // rgb(229, 226, 227)
internal val SurfaceContainerLowest = Color(0xFFFFFFFF) // rgb(255, 255, 255) - White
internal val SurfaceContainerLow = Color(0xFFF6F3F4) // rgb(246, 243, 244)
internal val SurfaceContainer = Color(0xFFF0EDEE)  // rgb(240, 237, 238)
internal val SurfaceContainerHigh = Color(0xFFEAE7E8) // rgb(234, 231, 232)
internal val SurfaceContainerHighest = Color(0xFFE5E2E3) // rgb(229, 226, 223)
internal val SurfaceDim = Color(0xFFDCD9DA)         // rgb(220, 217, 218)
internal val SurfaceBright = Color(0xFFFCF8F9)     // rgb(252, 248, 249)

// Outline and Border Colors
internal val Outline = Color(0xFF797586)           // rgb(121, 117, 134)
internal val OutlineVariant = Color(0xFFCAC4D7)    // rgb(202, 196, 215) - Border outline
internal val SurfaceTint = Color(0xFF6341D5)       // rgb(99, 65, 213) - Surface tint

// Inverse Colors
internal val InverseSurface = Color(0xFF303031)    // rgb(48, 48, 49)
internal val InverseOnSurface = Color(0xFFF3F0F1)  // rgb(243, 240, 241)
internal val InversePrimary = Color(0xFFCBBEFF)    // rgb(203, 190, 255)

// Dark Mode Specific Colors (computed based on light theme)
internal val DarkBackground = Color(0xFF1B1B1C)
internal val DarkSurface = Color(0xFF303031)
internal val DarkSurfaceVariant = Color(0xFF484554)
