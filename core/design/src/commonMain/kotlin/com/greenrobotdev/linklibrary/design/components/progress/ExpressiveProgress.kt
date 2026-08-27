package com.greenrobotdev.linklibrary.design.components.progress

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Experimentalexpressive.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Expressive Progress Indicators with Custom Shapes
 *
 * Material 3 Expressive supports creative progress indicator shapes:
 * - Curly progress with wavy animation
 * - Rounded square progress with smooth corners
 * - Squircle (superellipse) progress
 * - Star-shaped progress with animated points
 * - Diamond-shaped progress with elegant rotation
 */

/**
 * Curly progress indicator with wavy animation
 *
 * Expressive circular progress with a curly, wavy stroke that animates
 * around the circle in a flowing motion pattern.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CurlyCircularProgress(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 4.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "curly_progress")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = androidx.compose.animation.core.Restart
        )
    )

    Canvas(
        modifier = modifier.size(size),
        onDraw = {
            drawCurlyProgress(
                size = size,
                color = color,
                strokeWidth = strokeWidth,
                rotation = rotation
            )
        }
    )
}

/**
 * Rounded square progress indicator
 *
 * Expressive progress with rounded corners that maintains the square shape
 * but with smooth, friendly rounded edges during rotation.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RoundedSquareProgress(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 4.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "rounded_square_progress")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = androidx.compose.animation.core.Restart
        )
    )

    Canvas(
        modifier = modifier.size(size),
        onDraw = {
            drawRoundedSquareProgress(
                size = size,
                color = color,
                strokeWidth = strokeWidth,
                rotation = rotation
            )
        }
    )
}

/**
 * Squircle (superellipse) progress indicator
 *
 * Expressive progress that morphs between circle and square using
 * a superellipse formula for a unique, modern shape.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SquircleProgress(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 4.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "squirrelce_progress")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = androidx.compose.animation.core.Restart
        )
    )

    Canvas(
        modifier = modifier.size(size),
        onDraw = {
            drawSquircleProgress(
                size = size,
                color = color,
                strokeWidth = strokeWidth,
                rotation = rotation
            )
        }
    )
}

/**
 * Diamond-shaped progress indicator
 *
 * Expressive progress with elegant diamond shape that rotates
 smoothly with a sophisticated, geometric appeal.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DiamondProgress(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 4.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "diamond_progress")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300, easing = LinearEasing),
            repeatMode = androidx.compose.animation.core.Restart
        )
    )

    Canvas(
        modifier = modifier.size(size),
        onDraw = {
            drawDiamondProgress(
                size = size,
                color = color,
                strokeWidth = strokeWidth,
                rotation = rotation
            )
        }
    )
}

/**
 * Star-shaped progress indicator
 *
 * Expressive progress with star shape that animates with point-based
 * effects, creating a playful and engaging loading experience.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun StarProgress(
    modifier: Modifier = Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 3.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "star_progress")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = androidx.compose.animation.core.Restart
        )
    )
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = androidx.compose.animation.core.Restart
        )
    )

    Canvas(
        modifier = modifier.size(size),
        onDraw = {
            drawStarProgress(
                size = size,
                color = color,
                strokeWidth = strokeWidth,
                rotation = rotation,
                progress = progress
            )
        }
    )
}

// Drawing functions for each shape

private fun DrawScope.drawCurlyProgress(
    size: Dp,
    color: Color,
    strokeWidth: Dp,
    rotation: Float
) {
    val strokeWidthPx = strokeWidth.toPx()
    val sizePx = size.toPx()
    val center = Offset(sizePx / 2, sizePx / 2)
    val radius = (sizePx - strokeWidthPx) / 2

    val curlyPath = androidx.compose.ui.graphics.Path().apply {
        // Create wavy curly circle
        for (angle in 0..360 step 5) {
            val radians = Math.toRadians(angle.toDouble() + rotation)
            val waveOffset = sin(radians * 3) * 8 // Create wavy effect
            val adjustedRadius = radius + waveOffset
            val x = center.x + cos(radians) * adjustedRadius
            val y = center.y + sin(radians) * adjustedRadius

            if (angle == 0) {
                moveTo(x.toFloat(), y.toFloat())
            } else {
                lineTo(x.toFloat(), y.toFloat())
            }
        }
        close()
    }

    drawPath(
        path = curlyPath,
        color = color,
        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawRoundedSquareProgress(
    size: Dp,
    color: Color,
    strokeWidth: Dp,
    rotation: Float
) {
    val strokeWidthPx = strokeWidth.toPx()
    val sizePx = size.toPx()
    val cornerRadius = sizePx * 0.2f // 20% rounded corners

    // Draw rounded square with rotating corner
    val rotationRad = Math.toRadians(rotation.toDouble())
    val halfSize = sizePx / 2
    val quarterSize = sizePx / 4

    // Create rounded square path
    val roundedPath = androidx.compose.ui.graphics.Path().apply {
        // Start from center and draw rotating rounded square
        val cosRot = cos(rotationRad)
        val sinRot = sin(rotationRad)

        // Calculate rotated corners
        val corners = listOf(
            Pair(-quarterSize, -halfSize), // Top-left
            Pair(quarterSize, -halfSize),   // Top-right
            Pair(quarterSize, halfSize),    // Bottom-right
            Pair(-quarterSize, halfSize)    // Bottom-left
        ).map { (x, y) ->
            // Rotate each corner
            val rotatedX = x * cosRot - y * sinRot + sizePx / 2
            val rotatedY = x * sinRot + y * cosRot + sizePx / 2
            Offset(rotatedX.toFloat(), rotatedY.toFloat())
        }

        // Draw rounded square with arcTo for corners
        moveTo(corners[0].x, corners[0].y)
        arcTo(
            rectCenter = corners[0],
            size = Size(cornerRadius, cornerRadius),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 90f,
            forceMoveTo = true
        )
        lineTo(corners[1].x, corners[1].y)
        arcTo(
            rectCenter = corners[1],
            size = Size(cornerRadius, cornerRadius),
            startAngleDegrees = 270f,
            sweepAngleDegrees = 90f,
            forceMoveTo = true
        )
        lineTo(corners[2].x, corners[2].y)
        arcTo(
            rectCenter = corners[2],
            size = Size(cornerRadius, cornerRadius),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 90f,
            forceMoveTo = true
        )
        lineTo(corners[3].x, corners[3].y)
        arcTo(
            rectCenter = corners[3],
            size = Size(cornerRadius, cornerRadius),
            startAngleDegrees = 90f,
            sweepAngleDegrees = 90f,
            forceMoveTo = true
        )
        close()
    }

    drawPath(
        path = roundedPath,
        color = color,
        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawSquircleProgress(
    size: Dp,
    color: Color,
    strokeWidth: Dp,
    rotation: Float
) {
    val strokeWidthPx = strokeWidth.toPx()
    val sizePx = size.toPx()
    val center = Offset(sizePx / 2, sizePx / 2)
    val superellipsePower = 2.5f // Controls how square vs circular it is

    val squirclePath = androidx.compose.ui.graphics.Path().apply {
        // Create superellipse (squircle) using the power formula
        val points = mutableListOf<Offset>()
        val steps = 100

        for (i in 0..steps) {
            val angle = (i / steps.toFloat()) * 2 * Math.PI
            val xAbs = abs(cos(angle).toFloat())
            val yAbs = abs(sin(angle).toFloat())

            // Superellipse formula
            val x = (sizePx / 2) * Math.sign(cos(angle)) *
                xAbs.pow(1 / superellipsePower).toFloat()
            val y = (sizePx / 2) * Math.sign(sin(angle)) *
                yAbs.pow(1 / superellipsePower).toFloat()

            // Rotate point
            val rotationRad = Math.toRadians(rotation.toDouble())
            val rotatedX = (x - sizePx / 2) * cos(rotationRad) -
                         (y - sizePx / 2) * sin(rotationRad) + sizePx / 2
            val rotatedY = (x - sizePx / 2) * sin(rotationRad) +
                         (y - sizePx / 2) * cos(rotationRad) + sizePx / 2

            if (i == 0) {
                moveTo(rotatedX.toFloat(), rotatedY.toFloat())
            } else {
                lineTo(rotatedX.toFloat(), rotatedY.toFloat())
            }
        }
        close()
    }

    drawPath(
        path = squirclePath,
        color = color,
        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawDiamondProgress(
    size: Dp,
    color: Color,
    strokeWidth: Dp,
    rotation: Float
) {
    val strokeWidthPx = strokeWidth.toPx()
    val sizePx = size.toPx()
    val center = Offset(sizePx / 2, sizePx / 2)
    val halfSize = sizePx / 2

    val diamondPath = androidx.compose.ui.graphics.Path().apply {
        // Create rotating diamond shape
        val rotationRad = Math.toRadians(rotation.toDouble())

        // Diamond points (top, right, bottom, left)
        val diamondPoints = listOf(
            Pair(0f, -1f),    // Top
            Pair(1f, 0f),     // Right
            Pair(0f, 1f),     // Bottom
            Pair(-1f, 0f)     // Left
        ).map { (x, y) ->
            // Scale and rotate each point
            val scaledX = x * halfSize
            val scaledY = y * halfSize
            val rotatedX = scaledX * cos(rotationRad).toFloat() - scaledY * sin(rotationRad).toFloat()
            val rotatedY = scaledX * sin(rotationRad).toFloat() + scaledY * cos(rotationRad).toFloat()
            Offset(center.x + rotatedX, center.y + rotatedY)
        }

        moveTo(diamondPoints[0].x, diamondPoints[0].y)
        lineTo(diamondPoints[1].x, diamondPoints[1].y)
        lineTo(diamondPoints[2].x, diamondPoints[2].y)
        lineTo(diamondPoints[3].x, diamondPoints[3].y)
        close()
    }

    drawPath(
        path = diamondPath,
        color = color,
        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawStarProgress(
    size: Dp,
    color: Color,
    strokeWidth: Dp,
    rotation: Float,
    progress: Float
) {
    val strokeWidthPx = strokeWidth.toPx()
    val sizePx = size.toPx()
    val center = Offset(sizePx / 2, sizePx / 2)
    val outerRadius = (sizePx - strokeWidthPx) / 2
    val innerRadius = outerRadius * 0.4f

    val starPath = androidx.compose.ui.graphics.Path().apply {
        val points = 5
        val angleStep = 2 * Math.PI / points
        val rotationRad = Math.toRadians(rotation.toDouble())

        // Draw star with progress-based arc
        for (i in 0 until points * 2) {
            val isOuter = i % 2 == 0
            val angle = i * angleStep / 2 + rotationRad

            val x = if (isOuter) {
                center.x + (outerRadius * cos(angle).toFloat())
            } else {
                center.x + (innerRadius * cos(angle).toFloat())
            }
            val y = if (isOuter) {
                center.y + (outerRadius * sin(angle).toFloat())
            } else {
                center.y + (innerRadius * sin(angle).toFloat())
            }

            if (i == 0) {
                moveTo(x, y)
            } else {
                lineTo(x, y)
            }
        }
        close()

        // Add progress arc
        val arcAngle = 360f * progress
        arcTo(
            rectCenter = center,
            radius = outerRadius * 0.7f,
            startAngleDegrees = rotation - 90f,
            sweepAngleDegrees = arcAngle,
            forceMoveTo = true
        )
    }

    drawPath(
        path = starPath,
        color = color,
        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
    )
}
