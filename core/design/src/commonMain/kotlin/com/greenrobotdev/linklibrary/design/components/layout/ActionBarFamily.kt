package com.greenrobotdev.linklibrary.design.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon

/**
 * 72dp fully-rounded dark bar holding icon+label actions and a confirm pill
 * (the category picker's "Add sub / Rename / Remove / Use this" bar).
 */
@Composable
fun AppActionBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.inverseSurface)
            .padding(horizontal = 4.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/** Icon + label action inside an [AppActionBar]. [isError] tints it as destructive. */
@Composable
fun AppBarAction(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    enabled: Boolean,
    isError: Boolean = false,
    modifier: Modifier = Modifier,
) {
    // errorContainer reads salmon on the dark bar (and dark red on a light bar in dark theme)
    val tint = if (isError) MaterialTheme.colorScheme.errorContainer
    else MaterialTheme.colorScheme.inverseOnSurface
    Column(
        modifier = modifier
            .width(72.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .semantics { if (!enabled) contentDescription = "$label (disabled)" },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
    ) {
        AppIcon(
            imageVector = icon,
            contentDescription = label,
            color = if (enabled) tint else tint.copy(alpha = 0.38f),
        )
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = if (enabled) tint else tint.copy(alpha = 0.38f),
        )
    }
}

/**
 * Accent confirm pill ("Use this" / "Create" / "Undo") inside an [AppActionBar].
 * Own Row instead of StandardButton: the button's inner padding wraps the label at this width.
 */
@Composable
fun AppBarPill(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector = Icons.Filled.Check,
) {
    val container = if (enabled) MaterialTheme.colorScheme.tertiary
    else MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.12f)
    val content = if (enabled) MaterialTheme.colorScheme.onTertiary
    else MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.38f)
    Row(
        modifier = modifier
            .fillMaxHeight()
            .clip(CircleShape)
            .background(container)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(start = 8.dp, end = 8.dp)
            .semantics { if (!enabled) contentDescription = "$text (disabled)" },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
        AppIcon(imageVector = icon, contentDescription = null, color = content)
        Text(text = text, style = MaterialTheme.typography.titleMedium, color = content, maxLines = 1)
    }
}
