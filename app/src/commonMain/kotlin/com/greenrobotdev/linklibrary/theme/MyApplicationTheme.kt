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

// Design System Colors based on LordOfTheLinks specification
private val PrimaryBlue = Color(0xFF1A237E)
private val PrimaryLight = Color(0xFF534BAE)
private val PrimaryDark = Color(0xFF000051)
private val SecondaryTeal = Color(0xFF00BFA5)
private val SecondaryTealLight = Color(0xFF5DF2D6)
private val AccentOrange = Color(0xFFFF6D00)
private val BackgroundLight = Color(0xFFF5F7FA)
private val SurfaceLight = Color(0xFFFFFFFF)
private val TextPrimaryLight = Color(0xFF263238)
private val TextSecondaryLight = Color(0xFF546E7A)
private val TextTertiaryLight = Color(0xFF90A4AE)
private val BorderLight = Color(0xFFECEFF1)

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
            background = Color(0xFF121212),
            onBackground = Color(0xFFE0E0E0),
            surface = Color(0xFF1E1E1E),
            onSurface = Color(0xFFE0E0E0)
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
            onTertiary = Color.White,
            background = BackgroundLight,
            onBackground = TextPrimaryLight,
            surface = SurfaceLight,
            onSurface = TextPrimaryLight,
            onSurfaceVariant = TextSecondaryLight,
            outline = BorderLight
        )
    }
    val typography = Typography(
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
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
