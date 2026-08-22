package com.greenrobotdev.linklibrary.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Google Stitch AI Brand Colors (v1 Implementation)
private val PrimaryBlue = Color(0xFF4285F4)       // Google Blue
private val PrimaryLight = Color(0xFF82B1FF)     // Lighter Blue
private val PrimaryDark = Color(0xFF0D47A1)      // Darker Blue
private val SecondaryTeal = Color(0xFF34A853)    // Google Green
private val SecondaryTealLight = Color(0xFF5CD65C) // Lighter Green
private val AccentOrange = Color(0xFFFBBC05)     // Google Yellow
private val AccentRed = Color(0xFFEA4335)        // Google Red
private val AIPurple = Color(0xFF9C27B0)         // AI Assistant Purple
private val BackgroundLight = Color(0xFFF8F9FA)   // Google-style Background
private val SurfaceLight = Color(0xFFFFFFFF)     // White Surface
private val TextPrimaryLight = Color(0xFF202124)  // Google Text Primary
private val TextSecondaryLight = Color(0xFF5F6368) // Google Text Secondary
private val TextTertiaryLight = Color(0xFF9AA0A6)  // Google Text Tertiary
private val BorderLight = Color(0xFFE8EAED)       // Google Border Color

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = PrimaryBlue,
            onPrimary = Color.White,
            primaryContainer = PrimaryLight,
            onPrimaryContainer = Color.White,
            secondary = SecondaryTeal,
            onSecondary = Color.White,
            secondaryContainer = SecondaryTealLight,
            onSecondaryContainer = PrimaryBlue,
            tertiary = AccentOrange,
            onTertiary = Color.White,
            error = AccentRed,
            onError = Color.White,
            background = Color(0xFF1A1A1A),
            onBackground = Color(0xFFE8EAED),
            surface = Color(0xFF242424),
            onSurface = Color(0xFFE8EAED),
            onSurfaceVariant = Color(0xFF9AA0A6),
            outline = Color(0xFF3C4043),
            outlineVariant = Color(0xFF5F6368)
        )
    } else {
        lightColorScheme(
            primary = PrimaryBlue,
            onPrimary = Color.White,
            primaryContainer = PrimaryLight,
            onPrimaryContainer = Color.White,
            secondary = SecondaryTeal,
            onSecondary = Color.White,
            secondaryContainer = SecondaryTealLight,
            onSecondaryContainer = PrimaryBlue,
            tertiary = AccentOrange,
            onTertiary = TextPrimaryLight,
            tertiaryContainer = Color(0xFFFFD54F),
            onTertiaryContainer = Color(0xFFF57F17),
            error = AccentRed,
            onError = Color.White,
            errorContainer = Color(0xFFFF8A80),
            onErrorContainer = Color(0xFFB71C1C),
            background = BackgroundLight,
            onBackground = TextPrimaryLight,
            surface = SurfaceLight,
            onSurface = TextPrimaryLight,
            onSurfaceVariant = TextSecondaryLight,
            outline = BorderLight,
            outlineVariant = Color(0xFFDADCE0),
            surfaceVariant = Color(0xFFF1F3F4)
        )
    }
    val typography = Typography(
        // Display
        displayLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 57.sp,
            lineHeight = 64.sp
        ),
        displayMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 45.sp,
            lineHeight = 52.sp
        ),
        displaySmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp,
            lineHeight = 44.sp
        ),
        // Headline
        headlineLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 36.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 32.sp
        ),
        // Title
        titleLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 28.sp
        ),
        titleMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        titleSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        // Body
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        bodySmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ),
        // Label
        labelLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        labelMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ),
        labelSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
