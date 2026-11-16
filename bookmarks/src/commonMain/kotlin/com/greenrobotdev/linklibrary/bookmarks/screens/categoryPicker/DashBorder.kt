package com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * Dashed rounded-rectangle outline (the picker's "+ New top-level category" pill).
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
