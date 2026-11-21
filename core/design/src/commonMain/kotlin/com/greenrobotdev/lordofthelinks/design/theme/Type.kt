package com.greenrobotdev.lordofthelinks.design.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.greenrobotdev.lordofthelinks.design.resources.Res
import com.greenrobotdev.lordofthelinks.design.resources.roboto_flex
import org.jetbrains.compose.resources.Font

/**
 * Lords of Links Typography — Roboto Flex (variable font), per the approved wireframes.
 * File: core/design/src/commonMain/composeResources/font/roboto_flex.ttf
 */
val RobotoFlex: FontFamily
    // One entry per weight with the variable "wght" axis set; a single Font() renders every weight as 400.
    @Composable get() = FontFamily(
        listOf(
            FontWeight.Normal,
            FontWeight.Medium,
            FontWeight.SemiBold,
            FontWeight.Bold,
            FontWeight.ExtraBold,
            FontWeight.Black
        )
            .map { w ->
                Font(
                    Res.font.roboto_flex,
                    weight = w,
                    variationSettings = FontVariation.Settings(FontVariation.weight(w.weight))
                )
            }
    )

@Composable
internal fun getTypography() = Typography(
    displaySmall = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 34.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 28.sp,
        letterSpacing = (-0.3).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 26.sp,
        letterSpacing = (-0.3).sp
    ),
    titleLarge = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    titleSmall = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.5.sp // 1.5x
    ),
    bodySmall = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp
    ),
    labelLarge = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp
    ),
    labelSmall = TextStyle(
        fontFamily = RobotoFlex,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp
    )
)

//// Large primary buttons (Save, Continue): 18sp / weight 800
//val LargeButtonTextStyle = TextStyle(
//    fontFamily = RobotoFlex,
//    fontWeight = FontWeight.ExtraBold,
//    fontSize = 18.sp
//)
