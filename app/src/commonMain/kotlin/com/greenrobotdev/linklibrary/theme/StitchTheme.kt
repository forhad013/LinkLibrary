package com.greenrobotdev.linklibrary.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Google Stitch AI Brand Colors
 * Inspired by Google's brand colors and Material Design 3
 */

// Primary Colors - Google Blue
private val StitchPrimary = Color(0xFF4285F4)        // Google Blue
private val StitchPrimaryLight = Color(0xFF82B1FF)   // Lighter Blue
private val StitchPrimaryDark = Color(0xFF0D47A1)   // Darker Blue

// Secondary Colors - Google Green
private val StitchSecondary = Color(0xFF34A853)       // Google Green
private val StitchSecondaryLight = Color(0xFF5CD65C) // Lighter Green
private val StitchSecondaryDark = Color(0xFF006924)   // Darker Green

// Tertiary Colors - Google Yellow
private val StitchTertiary = Color(0xFFFBBC05)       // Google Yellow
private val StitchTertiaryLight = Color(0xFFFFD54F)  // Lighter Yellow
private val StitchTertiaryDark = Color(0xFFF57F17)   // Darker Yellow

// Accent Colors - Google Red
private val StitchAccent = Color(0xFFEA4335)         // Google Red
private val StitchAccentLight = Color(0xFFFF8A80)    // Lighter Red
private val StitchAccentDark = Color(0xFFB71C1C)     // Darker Red

// AI-Specific Colors
private val StitchAIPurple = Color(0xFF9C27B0)       // AI Assistant Purple
private val StitchAIPurpleLight = Color(0xFFD05CE3)  // Lighter Purple
private val StitchAIPurpleDark = Color(0xFF6A0080)   // Darker Purple

// Neutral Colors - Light Mode
private val StitchBackground = Color(0xFFF8F9FA)      // Light Gray Background
private val StitchSurface = Color(0xFFFFFFFF)        // White Surface
private val StitchSurfaceVariant = Color(0xFFF1F3F4)  // Surface Variant
private val StitchTextPrimary = Color(0xFF202124)     // Primary Text
private val StitchTextSecondary = Color(0xFF5F6368)   // Secondary Text
private val StitchTextTertiary = Color(0xFF9AA0A6)    // Tertiary Text
private val StitchBorder = Color(0xFFE8EAED)          // Border Color
private val StitchDivider = Color(0xFFDADCE0)        // Divider Color

// Neutral Colors - Dark Mode
private val StitchBackgroundDark = Color(0xFF1A1A1A) // Dark Background
private val StitchSurfaceDark = Color(0xFF242424)     // Dark Surface
private val StitchSurfaceVariantDark = Color(0xFF2D2D2D) // Dark Surface Variant
private val StitchTextPrimaryDark = Color(0xFFE8EAED)  // Light Text
private val StitchTextSecondaryDark = Color(0xFF9AA0A6) // Light Secondary Text
private val StitchTextTertiaryDark = Color(0xFF5F6368)  // Light Tertiary Text
private val StitchBorderDark = Color(0xFF3C4043)      // Dark Border
private val StitchDividerDark = Color(0xFF5F6368)    // Dark Divider

/**
 * Stitch Theme Color Extensions
 * Provides easy access to Stitch-specific colors
 */
object StitchColors {
    // Primary
    val Primary = StitchPrimary
    val PrimaryLight = StitchPrimaryLight
    val PrimaryDark = StitchPrimaryDark

    // Secondary
    val Secondary = StitchSecondary
    val SecondaryLight = StitchSecondaryLight
    val SecondaryDark = StitchSecondaryDark

    // Tertiary
    val Tertiary = StitchTertiary
    val TertiaryLight = StitchTertiaryLight
    val TertiaryDark = StitchTertiaryDark

    // Accent
    val Accent = StitchAccent
    val AccentLight = StitchAccentLight
    val AccentDark = StitchAccentDark

    // AI Colors
    val AIPurple = StitchAIPurple
    val AIPurpleLight = StitchAIPurpleLight
    val AIPurpleDark = StitchAIPurpleDark

    // Neutrals (Light)
    val Background = StitchBackground
    val Surface = StitchSurface
    val SurfaceVariant = StitchSurfaceVariant
    val TextPrimary = StitchTextPrimary
    val TextSecondary = StitchTextSecondary
    val TextTertiary = StitchTextTertiary
    val Border = StitchBorder
    val Divider = StitchDivider

    // Neutrals (Dark)
    val BackgroundDark = StitchBackgroundDark
    val SurfaceDark = StitchSurfaceDark
    val SurfaceVariantDark = StitchSurfaceVariantDark
    val TextPrimaryDark = StitchTextPrimaryDark
    val TextSecondaryDark = StitchTextSecondaryDark
    val TextTertiaryDark = StitchTextTertiaryDark
    val BorderDark = StitchBorderDark
    val DividerDark = StitchDividerDark
}

/**
 * Stitch Spacing System
 * Consistent spacing based on 8dp grid
 */
object StitchSpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 20.dp
    val xxl = 24.dp
    val xxxl = 32.dp
}

/**
 * Stitch Elevation System
 * Consistent elevation hierarchy
 */
object StitchElevation {
    val none = 0.dp
    val card = 2.dp        // Standard cards
    val elevatedCard = 4.dp // Featured cards
    val modal = 8.dp       // Bottom sheets/dialogs
}
