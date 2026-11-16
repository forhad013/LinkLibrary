package com.greenrobotdev.linklibrary.design.extensions

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.tokens.SpacingTokens

/**
 * Extension functions for Modifier to provide common design system patterns
 */

/**
 * Apply padding using design token spacing system
 */
fun Modifier.appPadding(size: Dp = SpacingTokens.Base): Modifier =
    this.then(padding(size))

/**
 * Apply horizontal padding using design token spacing system
 */
fun Modifier.appPaddingHorizontal(size: Dp = SpacingTokens.Medium): Modifier =
    this.then(padding(horizontal = size))

/**
 * Apply vertical padding using design token spacing system
 */
fun Modifier.appPaddingVertical(size: Dp = SpacingTokens.Medium): Modifier =
    this.then(padding(vertical = size))

/**
 * Apply small padding (8dp) - commonly used for compact spacing
 */
fun Modifier.appPaddingSmall(): Modifier =
    this.then(padding(SpacingTokens.ExtraSmall))

/**
 * Apply medium padding (16dp) - commonly used for standard spacing
 */
fun Modifier.appPaddingMedium(): Modifier =
    this.then(padding(SpacingTokens.Medium))

/**
 * Apply large padding (24dp) - commonly used for loose spacing
 */
fun Modifier.appPaddingLarge(): Modifier =
    this.then(padding(SpacingTokens.Large))

/**
 * Dashed rounded-rectangle outline (dashed pill buttons).
 * shortcut: dash phase is static; animate it only if the design ever asks for a moving dash
 */
fun Modifier.dashBorder(color: Color, strokeWidth: Float = 2f): Modifier = drawBehind {
    val inset = strokeWidth
    val path = Path().apply {
        addRoundRect(
            RoundRect(
                left = inset,
                top = inset,
                right = size.width - inset,
                bottom = size.height - inset,
                cornerRadius = CornerRadius(size.height / 2f, size.height / 2f)
            )
        )
    }
    drawPath(
        path,
        color = color,
        style = Stroke(
            width = strokeWidth,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 4.dp.toPx()))
        )
    )
}