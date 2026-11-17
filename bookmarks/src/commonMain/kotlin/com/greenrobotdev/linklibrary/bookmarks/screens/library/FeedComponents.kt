package com.greenrobotdev.linklibrary.bookmarks.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.bookmarks.domain.hostOf
import com.greenrobotdev.linklibrary.bookmarks.model.Link
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon
import com.greenrobotdev.linklibrary.design.theme.LordsExtraColors
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.time.ExperimentalTime

// Feed pieces (design: Feed)

/** "Lords of Links" / "Your saves" title at the top of the Feed. */
@Composable
internal fun FeedHeader(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        Text(
            "Lords of Links",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text("Your saves", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
    }
}

/**
 * "All saves" row right above the list: Newest/Oldest and Filters sit next to what they change.
 * [picked] is the row of chosen category/tag chips, shown under it.
 */
@Composable
internal fun AllSavesHeader(
    newestFirst: Boolean,
    onToggleSort: () -> Unit,
    activeFilters: Int,
    onFilters: () -> Unit,
    modifier: Modifier = Modifier,
    picked: @Composable () -> Unit = {},
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "All saves",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.weight(1f).padding(start = 4.dp).semantics { heading() },
            )
            SortButton(newestFirst, onToggleSort)
            FiltersButton(activeCount = activeFilters, onClick = onFilters)
        }
        picked()
    }
}

@Composable
private fun SortButton(newestFirst: Boolean, onToggleSort: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(role = Role.Button, onClickLabel = "Change sort order", onClick = onToggleSort)
            .heightIn(min = 44.dp)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        AppIcon(Icons.AutoMirrored.Outlined.Sort, contentDescription = null, color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(18.dp))
        Text(
            if (newestFirst) "Newest" else "Oldest",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

/** "Filters" button in the All saves row; filled with a count badge when any filter is active. */
@Composable
internal fun FiltersButton(activeCount: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val active = activeCount > 0
    val fg = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
            .clickable(role = Role.Button, onClickLabel = "Open filters", onClick = onClick)
            .semantics { contentDescription = if (active) "Filters, $activeCount active" else "Filters" }
            .heightIn(min = 44.dp)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AppIcon(Icons.Filled.Tune, contentDescription = null, color = fg, modifier = Modifier.size(20.dp))
        if (active) {
            Box(
                modifier = Modifier.size(22.dp).clip(CircleShape).background(MaterialTheme.colorScheme.onPrimary),
                contentAlignment = Alignment.Center,
            ) {
                Text("$activeCount", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Text("Filters", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = fg)
        }
    }
}

/** A picked category or tag on the Feed; ✕ removes just that filter. */
@Composable
internal fun SelectedFilterChip(text: String, isTag: Boolean, onRemove: () -> Unit) {
    val bg = if (isTag) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
    val fg = if (isTag) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .clickable(role = Role.Button, onClickLabel = "Remove filter $text", onClick = onRemove)
            .heightIn(min = 36.dp)
            .padding(start = 12.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.ExtraBold, color = fg, maxLines = 1)
        AppIcon(Icons.Filled.Close, contentDescription = null, color = fg, modifier = Modifier.size(16.dp))
    }
}

/**
 * One saved link. Tapping the card opens its details; the ↗ button opens the link itself.
 * A link whose title was never fetched shows its URL and "No preview yet".
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun FeedLinkCard(
    link: Link,
    categoryPath: String?,
    onOpen: () -> Unit,
    onDetails: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val host = hostOf(link.url)
    val noPreview = link.title.isBlank() || link.title == link.url
    val typeLabel = link.type.replaceFirstChar { it.uppercaseChar() }
    val meta = buildList {
        add(if (noPreview) host else "$typeLabel · $host")
        if (noPreview) add("No preview yet")
        // The TODAY / YESTERDAY header already says the day, so those cards show only the time
        savedAtLabel(link.createdAt, timeOnlyIfRecent = true)?.let(::add)
    }.joinToString(" · ")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClickLabel = "Open details", onClick = onDetails)
            .padding(start = 14.dp, top = 14.dp, bottom = 14.dp, end = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TypeThumb(type = if (noPreview) "none" else link.type, muted = link.isArchived)

        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (!link.isOpened) {
                    Box(Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
                }
                Text(
                    meta,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                if (link.isFavorite) {
                    AppIcon(Icons.Filled.Star, contentDescription = "Favorite", color = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(14.dp))
                }
                if (link.isArchived) {
                    ArchivedPill()
                }
            }
            Text(
                if (noPreview) link.url else link.title,
                style = if (noPreview) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (link.isArchived) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            if (categoryPath != null || link.tags.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.padding(top = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    categoryPath?.let {
                        MetaChip(it, MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    link.tags.forEach {
                        MetaChip("#$it", MaterialTheme.colorScheme.surfaceContainerHigh, MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable(role = Role.Button, onClick = onOpen),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "Open link", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun TypeThumb(type: String, muted: Boolean = false) {
    data class Look(val shape: Shape, val bg: Color, val fg: Color, val icon: ImageVector)
    val look = when (type) {
        "video" -> Look(RoundedCornerShape(18.dp), LordsExtraColors.mediaPlaceholder, MaterialTheme.colorScheme.secondary, Icons.Filled.PlayArrow)
        "social" -> Look(CircleShape, MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer, Icons.Outlined.ChatBubbleOutline)
        "article" -> Look(RoundedCornerShape(18.dp), MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer, Icons.AutoMirrored.Outlined.Article)
        else -> Look(RoundedCornerShape(18.dp), MaterialTheme.colorScheme.surfaceContainerHigh, MaterialTheme.colorScheme.onSurfaceVariant, Icons.Filled.Link)
    }
    // Archived: same shape and icon, neutral colors
    val bg = if (muted) MaterialTheme.colorScheme.surfaceContainerHigh else look.bg
    val fg = if (muted) MaterialTheme.colorScheme.onSurfaceVariant else look.fg
    Box(
        modifier = Modifier.size(72.dp).clip(look.shape).background(bg),
        contentAlignment = Alignment.Center,
    ) {
        AppIcon(look.icon, contentDescription = null, color = fg)
    }
}

@Composable
private fun ArchivedPill() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppIcon(Icons.Outlined.Archive, contentDescription = null, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(12.dp))
        Text("Archived", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
    }
}

@Composable
private fun MetaChip(text: String, bg: Color, fg: Color) {
    Text(
        text,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = fg,
        maxLines = 1,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp),
    )
}

/**
 * When a link was saved, in the device's time zone:
 * "Just now" (under a minute), "Today, 14:32", "Yesterday, 09:15", "Monday, 18:04" (last 6 days),
 * "12 Nov, 18:04" (this year), "12 Nov 2025" (older). null when unknown.
 * [timeOnlyIfRecent]: "14:32" instead of "Today, 14:32" / "Yesterday, 09:15", for lists that head those days.
 * shortcut: 24-hour clock and English names; switch to platform formatters if the app gets localized.
 */
@OptIn(ExperimentalTime::class)
internal fun savedAtLabel(
    epochMs: Long?,
    nowMs: Long = Clock.System.now().toEpochMilliseconds(),
    zone: TimeZone = TimeZone.currentSystemDefault(),
    timeOnlyIfRecent: Boolean = false,
): String? {
    if (epochMs == null || epochMs <= 0L) return null
    if (nowMs - epochMs in 0 until 60_000L) return "Just now"
    val then = Instant.fromEpochMilliseconds(epochMs).toLocalDateTime(zone)
    val today = Instant.fromEpochMilliseconds(nowMs).toLocalDateTime(zone).date
    val time = "${then.hour.toString().padStart(2, '0')}:${then.minute.toString().padStart(2, '0')}"
    val daysAgo = then.date.daysUntil(today)
    val month = then.month.name.take(3).lowercase().replaceFirstChar { it.uppercaseChar() }
    val day = then.date.day
    return when {
        daysAgo in 0..1 && timeOnlyIfRecent -> time
        daysAgo == 0 -> "Today, $time"
        daysAgo == 1 -> "Yesterday, $time"
        daysAgo in 2..6 -> "${then.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercaseChar() }}, $time"
        then.year == today.year -> "$day $month, $time"
        else -> "$day $month ${then.year}" // also covers future dates from a wrong clock
    }
}

/**
 * Feed section a save falls under: "Today", "Yesterday", "This week" (2–6 days ago) or "Earlier".
 * Undated saves go to "Earlier"; a future date from a wrong clock counts as "Today".
 */
@OptIn(ExperimentalTime::class)
internal fun feedSection(
    epochMs: Long?,
    nowMs: Long = Clock.System.now().toEpochMilliseconds(),
    zone: TimeZone = TimeZone.currentSystemDefault(),
): String {
    if (epochMs == null || epochMs <= 0L) return "Earlier"
    val then = Instant.fromEpochMilliseconds(epochMs).toLocalDateTime(zone).date
    val today = Instant.fromEpochMilliseconds(nowMs).toLocalDateTime(zone).date
    return when (then.daysUntil(today)) {
        in Int.MIN_VALUE..0 -> "Today"
        1 -> "Yesterday"
        in 2..6 -> "This week"
        else -> "Earlier"
    }
}

/** Section title above a run of saves in the Feed. */
@Composable
internal fun FeedSectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        title.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(start = 4.dp, top = 4.dp).semantics { heading() },
    )
}
