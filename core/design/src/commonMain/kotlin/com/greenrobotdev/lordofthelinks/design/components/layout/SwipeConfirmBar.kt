package com.greenrobotdev.lordofthelinks.design.components.layout

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.greenrobotdev.lordofthelinks.design.foundation.icon.AppIcon
import kotlin.math.roundToInt

/**
 * Dark action bar with a cancel button and a drag-to-confirm track
 * (the category picker's swipe-to-delete confirm bar).
 *
 * @param cancelLabel label of the non-destructive escape button
 * @param trackLabel label shown inside the un-dragged track
 * @param confirmIcon icon on the draggable thumb
 * @param confirmThreshold fraction of the track the thumb must pass to confirm (0..1]
 */
@Composable
fun SwipeConfirmBar(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    cancelLabel: String = "Keep",
    trackLabel: String = "Swipe to delete",
    confirmIcon: ImageVector = Icons.Filled.Delete,
    confirmThreshold: Float = 0.7f,
) {
    val trackBg = MaterialTheme.colorScheme.surfaceContainerHighest
    val fillColor = MaterialTheme.colorScheme.errorContainer
    val thumbSize = 48.dp
    val trackHeight = 56.dp
    val trackRadius = 28.dp

    var trackWidthPx by remember { mutableIntStateOf(0) }
    val thumbSizePx = with(LocalDensity.current) { thumbSize.toPx() }
    val maxDrag = (trackWidthPx - thumbSizePx).coerceAtLeast(0f)
    var dragOffset by remember { mutableFloatStateOf(0f) }
    val progress = if (maxDrag > 0f) (dragOffset / maxDrag).coerceIn(0f, 1f) else 0f

    AppActionBar(modifier) {
        Box(
            modifier = Modifier
                .width(84.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onCancel),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                cancelLabel,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(trackHeight)
                .padding(end = 8.dp)
                .clip(RoundedCornerShape(trackRadius))
                .onSizeChanged { trackWidthPx = it.width }
                .drawBehind {
                    drawRoundRect(trackBg, cornerRadius = CornerRadius(trackRadius.toPx()))
                    if (progress > 0f) {
                        drawRoundRect(
                            fillColor.copy(alpha = 0.5f + progress * 0.5f),
                            size = Size(size.width * progress, size.height),
                            cornerRadius = CornerRadius(trackRadius.toPx()),
                        )
                    }
                }
                .draggable(
                    orientation = Orientation.Horizontal,
                    enabled = enabled,
                    state = rememberDraggableState { delta ->
                        dragOffset = (dragOffset + delta).coerceIn(0f, maxDrag)
                    },
                    onDragStopped = {
                        if (progress >= confirmThreshold) onConfirm()
                        dragOffset = 0f
                    },
                ),
        ) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(dragOffset.roundToInt(), 0) }
                    .padding(4.dp)
                    .size(thumbSize)
                    .clip(CircleShape)
                    .background(
                        if (progress > 0.5f) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.errorContainer
                    ),
                contentAlignment = Alignment.Center,
            ) {
                AppIcon(confirmIcon, contentDescription = trackLabel, color = Color.White, modifier = Modifier.size(22.dp))
            }

            if (progress < 0.3f) {
                Row(
                    modifier = Modifier.matchParentSize(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        trackLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(start = thumbSize),
                    )
                    Text(" ›››", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.4f))
                }
            }
        }
    }
}

/** Dark bar with a countdown ring, a message, and an accent undo pill. */
@Composable
fun AppUndoBar(
    title: String,
    onUndo: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    durationMillis: Int = 5_000,
    undoLabel: String = "Undo",
    undoIcon: ImageVector = Icons.Filled.Refresh,
) {
    val countdownProgress = remember { Animatable(1f) }
    val ringColor = MaterialTheme.colorScheme.errorContainer
    LaunchedEffect(Unit) {
        countdownProgress.animateTo(0f, tween(durationMillis = durationMillis, easing = LinearEasing))
    }

    AppActionBar(modifier) {
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(36.dp)
                .drawBehind {
                    drawCircle(ringColor.copy(alpha = 0.3f), style = Stroke(width = 3.dp.toPx()))
                    drawArc(
                        ringColor,
                        startAngle = -90f,
                        sweepAngle = 360f * countdownProgress.value,
                        useCenter = false,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(
                Icons.Filled.Delete,
                contentDescription = null,
                color = ringColor,
                modifier = Modifier.size(16.dp),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.7f),
                    maxLines = 1,
                )
            }
        }

        AppBarPill(text = undoLabel, onClick = onUndo, icon = undoIcon)
        Spacer(Modifier.width(4.dp))
    }
}
