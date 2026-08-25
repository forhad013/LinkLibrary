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

// HTML Design Template Colors (Exact RGB values)
private val Primary = Color(0xFF0058BD)           // rgb(0, 88, 189) - Primary
private val PrimaryBlue = Color(0xFF4285F4)       // rgb(66, 133, 244) - Save Button
private val PrimaryFixed = Color(0xFFD8E2FF)      // rgb(216, 226, 255)
private val PrimaryFixedDim = Color(0xFFADC6FF)   // rgb(173, 198, 255)
private val OnPrimary = Color(0xFFFFFFFF)         // rgb(255, 255, 255)
private val OnPrimaryFixed = Color(0xFF001A41)    // rgb(0, 26, 65)
private val OnPrimaryFixedVariant = Color(0xFF004494) // rgb(0, 68, 148)

private val Secondary = Color(0xFF295EA6)         // rgb(41, 94, 166)
private val SecondaryContainer = Color(0xFF83B1FF) // rgb(131, 177, 255)
private val SecondaryFixed = Color(0xFFD6E3FF)   // rgb(214, 227, 255)
private val SecondaryFixedDim = Color(0xFFA9C7FF) // rgb(169, 199, 255)
private val OnSecondary = Color(0xFFFFFFFF)      // rgb(255, 255, 255)
private val OnSecondaryContainer = Color(0xFF004285) // rgb(0, 66, 133)
private val OnSecondaryFixed = Color(0xFF001B3D) // rgb(0, 27, 61)
private val OnSecondaryFixedVariant = Color(0xFF00468C) // rgb(0, 70, 140)

private val Tertiary = Color(0xFF006B2B)          // rgb(0, 107, 43) - Green
private val TertiaryContainer = Color(0xFF008738) // rgb(0, 135, 56)
private val TertiaryFixed = Color(0xFF89FA9B)    // rgb(137, 250, 155)
private val TertiaryFixedDim = Color(0xFF6DDD81)  // rgb(109, 221, 129)
private val OnTertiary = Color(0xFFFFFFFF)       // rgb(255, 255, 255)
private val OnTertiaryContainer = Color(0xFFF7FFF2) // rgb(247, 255, 242)
private val OnTertiaryFixed = Color(0xFF002108)   // rgb(0, 33, 8)
private val OnTertiaryFixedVariant = Color(0xFF005320) // rgb(0, 83, 32)

private val Error = Color(0xFFBA1A1A)            // rgb(186, 26, 26)
private val ErrorContainer = Color(0xFFFFDAD6)   // rgb(255, 218, 214)
private val OnError = Color(0xFFFFFFFF)           // rgb(255, 255, 255)
private val OnErrorContainer = Color(0xFF93000A)  // rgb(147, 0, 10)

private val Background = Color(0xFFFAF9FD)        // rgb(250, 249, 253) - Main background
private val OnBackground = Color(0xFF1A1B1E)     // rgb(26, 27, 30) - Text primary
private val Surface = Color(0xFFFAF9FD)           // rgb(250, 249, 253) - Same as background
private val OnSurface = Color(0xFF1A1B1E)         // rgb(26, 27, 30)
private val OnSurfaceVariant = Color(0xFF424753)   // rgb(66, 71, 83) - Text secondary
private val SurfaceVariant = Color(0xFFE3E2E6)    // rgb(227, 226, 230)
private val SurfaceContainerLowest = Color(0xFFFFFFFF) // rgb(255, 255, 255) - White
private val SurfaceContainerLow = Color(0xFFF4F3F7) // rgb(244, 243, 247)
private val SurfaceContainer = Color(0xFFEFEDF1)  // rgb(239, 237, 241)
private val SurfaceContainerHigh = Color(0xFFE9E7EB) // rgb(233, 231, 235)
private val SurfaceContainerHighest = Color(0xFFE3E2E6) // rgb(227, 226, 230) - Border color
private val SurfaceDim = Color(0xFFDBD9DD)         // rgb(219, 217, 221)
private val SurfaceBright = Color(0xFFFAF9FD)     // rgb(250, 249, 253)
private val Outline = Color(0xFF727785)           // rgb(114, 119, 133)
private val OutlineVariant = Color(0xFFC2C6D5)    // rgb(194, 198, 213) - Border outline

private val InverseSurface = Color(0xFF2F3033)    // rgb(47, 48, 51)
private val InverseOnSurface = Color(0xFFF1F0F4)  // rgb(241, 240, 244)
private val InversePrimary = Color(0xFFADC6FF)    // rgb(173, 198, 255)

private val SurfaceTint = Color(0xFF005AC1)       // rgb(0, 90, 193)
private val PrimaryContainer = Color(0xFF2771DF)   // rgb(39, 113, 223)
private val OnPrimaryContainer = Color(0xFFFEFCFF) // rgb(254, 252, 255)

@Composable
fun MyApplicationTheme(
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
            onSurfaceVariant = Color(0xFF9AA0A6),
            surfaceVariant = SurfaceVariant,
            surfaceContainerLowest = Color(0xFF1A1A1A),
            surfaceContainerLow = Color(0xFF242424),
            surfaceContainer = Color(0xFF2C2C2C),
            surfaceContainerHigh = Color(0xFF343434),
            surfaceContainerHighest = Color(0xFF3C3C3C),
            surfaceDim = Color(0xFF202020),
            surfaceBright = Color(0xFF363636),

            // Outline colors
            outline = Color(0xFF727785),
            outlineVariant = Color(0xFF5F6368),

            // Inverse colors
            inversePrimary = InversePrimary,
            inverseSurface = Color(0xFFE8EAED),
            inverseOnSurface = Color(0xFF1A1A1A)
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
