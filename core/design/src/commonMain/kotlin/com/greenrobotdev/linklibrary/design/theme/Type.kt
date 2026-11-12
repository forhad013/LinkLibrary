package com.greenrobotdev.linklibrary.design.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Lords of Links Typography System
 *
 * Dual-font strategy following Material 3 Expressive guidelines:
 * - Plus Jakarta Sans for Display, Headline, and Title roles (expressive headlines)
 * - Be Vietnam Pro for Body and Label roles (optimal readability)
 *
 * Design Specifications:
 * - Display: 57px, -0.25px letter spacing, 700 weight
 * - Headline: 32px, 700 weight (desktop), 28px mobile
 * - Title: 22px, 600 weight
 * - Body: 16px/14px, 0.5px/0.25px letter spacing
 * - Label: 14px/12px, 0.1px/0.5px letter spacing
 *
 * Note: Using FontFamily.Default as fallback. For custom fonts:
 * 1. Add font files to commonMain/resources/fonts/
 * 2. Create FontFamily with platform-specific resource loading
 * 3. Replace FontFamily.Default with custom FontFamily instances
 */
internal fun getTypography() = Typography(
    // Display - Plus Jakarta Sans (hero text, large numbers)
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default, // Replace with Plus Jakarta Sans
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default, // Replace with Plus Jakarta Sans
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default, // Replace with Plus Jakarta Sans
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),

    // Headline - Plus Jakarta Sans (section headers)
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default, // Replace with Plus Jakarta Sans
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default, // Replace with Plus Jakarta Sans
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default, // Replace with Plus Jakarta Sans
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // Title - Plus Jakarta Sans (smaller headers, card titles)
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default, // Replace with Plus Jakarta Sans
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default, // Replace with Plus Jakarta Sans
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default, // Replace with Plus Jakarta Sans
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Body - Be Vietnam Pro (paragraph text, descriptions)
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, // Replace with Be Vietnam Pro
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default, // Replace with Be Vietnam Pro
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default, // Replace with Be Vietnam Pro
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // Label - Be Vietnam Pro (buttons, chips, captions)
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default, // Replace with Be Vietnam Pro
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default, // Replace with Be Vietnam Pro
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default, // Replace with Be Vietnam Pro
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

/**
 * Custom Typography Setup for Lords of Links
 *
 * To implement custom fonts, create font resource files:
 *
 * For Android (androidMain/res/font/):
 * - plus_jakarta_sans.xml
 * - be_vietnam_pro.xml
 *
 * For Desktop/JVM:
 * - Load fonts from resources/
 *
 * For iOS (iosMain/):
 * - Add font files to bundle resources
 *
 * Example FontFamily creation:
 * val PlusJakartaSans = FontFamily(
 *     Font(Res.font.plus_jakarta_sans_regular, FontWeight.Normal),
 *     Font(Res.font.plus_jakarta_sans_bold, FontWeight.Bold)
 * )
 * val BeVietnamPro = FontFamily(
 *     Font(Res.font.be_vietnam_pro_regular, FontWeight.Normal),
 *     Font(Res.font.be_vietnam_pro_medium, FontWeight.Medium)
 * )
 */
