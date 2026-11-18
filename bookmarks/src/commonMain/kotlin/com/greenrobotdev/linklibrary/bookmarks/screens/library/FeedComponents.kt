package com.greenrobotdev.linklibrary.bookmarks.screens.library

import androidx.compose.material.icons.filled.Inbox
import com.greenrobotdev.linklibrary.design.motion.rememberReducedMotion
import com.greenrobotdev.linklibrary.design.motion.AppMotion
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.runtime.mutableStateOf
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.animateColorAsState
import com.greenrobotdev.linklibrary.bookmarks.components.LinkPreviewImage
import com.greenrobotdev.linklibrary.design.components.layout.AppLazyRow
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.roundToInt
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.layout
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.filled.Check
import androidx.compose.foundation.Canvas
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.greenrobotdev.linklibrary.bookmarks.motion.sharedThumb
import com.greenrobotdev.linklibrary.bookmarks.motion.sharedTitle
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon
import com.greenrobotdev.linklibrary.design.theme.LordsExtraColors
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.time.ExperimentalTime

// Feed pieces (design: Feed)

/**
 * How far the Feed header has collapsed, driven by the list's scroll: scrolling down collapses it
 * before the list moves, and it only expands again once the list is back at the top.
 */
@Stable
internal class CollapsingHeaderState(private val rangePx: Float) {
    private var offsetPx by mutableFloatStateOf(0f)

    /** 0 = expanded, 1 = collapsed. Read it in layout/draw lambdas so scrolling skips recomposition. */
    val fraction: Float get() = if (rangePx == 0f) 0f else -offsetPx / rangePx

    val nestedScroll = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset =
            if (available.y < 0f) Offset(0f, consume(available.y)) else Offset.Zero

        override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset =
            if (available.y > 0f) Offset(0f, consume(available.y)) else Offset.Zero
    }

    private fun consume(dy: Float): Float {
        val next = (offsetPx + dy).coerceIn(-rangePx, 0f)
        val used = next - offsetPx
        offsetPx = next
        return used
    }
}

@Composable
internal fun rememberCollapsingHeaderState(range: Dp = 56.dp): CollapsingHeaderState {
    val px = with(LocalDensity.current) { range.toPx() }
    return remember(px) { CollapsingHeaderState(px) }
}

/**
 * Lays the content out at [scale] of its size (the freed space goes to the list) and draws it scaled
 * from [origin]. Both happen in layout/draw, so a changing scale doesn't recompose.
 */
private fun Modifier.scaleAway(scale: () -> Float, origin: TransformOrigin, fade: Boolean = false) =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val s = scale()
        val height = (placeable.height * s).roundToInt()
        layout(placeable.width, height) {
            placeable.placeWithLayer(0, (height - placeable.height) / 2) {
                scaleX = s
                scaleY = s
                transformOrigin = origin
                if (fade) alpha = s
            }
        }
    }

/**
 * "Lords of Links" / "Your saves" title at the top of the Feed, with the inbox progress ring. As
 * [collapse] goes 0 → 1, the overline fades out and the title and ring shrink.
 */
@Composable
internal fun FeedHeader(opened: Int, total: Int, celebrate: Boolean, modifier: Modifier = Modifier, collapse: () -> Float = { 0f }) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(
                "Lords of Links",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.scaleAway({ 1f - collapse() }, TransformOrigin(0f, 0.5f), fade = true),
            )
            Text(
                "Your saves",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.scaleAway({ 1f - 0.3f * collapse() }, TransformOrigin(0f, 0.5f)),
            )
        }
        if (total > 0) {
            InboxProgress(opened, total, celebrate, Modifier.scaleAway({ 1f - 0.25f * collapse() }, TransformOrigin(1f, 0.5f)))
        }
    }
}

/**
 * Ring that fills as saves get opened: "9 left · 3 of 12 opened". At zero it turns green "Done"
 * only right after the user cleared the last one ([celebrate]); otherwise it's a calm gray
 * "0 new · 19 opened" (e.g. after a restore, where everything was already opened).
 */
@Composable
private fun InboxProgress(opened: Int, total: Int, celebrate: Boolean, modifier: Modifier = Modifier) {
    val done = opened >= total
    val fraction by animateFloatAsState(opened.toFloat() / total, label = "inbox progress")
    val track = MaterialTheme.colorScheme.primaryContainer
    val fill = when {
        done && celebrate -> LordsExtraColors.success
        done -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.primary
    }
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 6.dp, end = 14.dp, top = 6.dp, bottom = 6.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = if (done) "Nothing new, all $total saves opened" else "${total - opened} unopened, $opened of $total opened"
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Canvas(Modifier.size(40.dp)) {
            val stroke = 5.dp.toPx()
            val inset = stroke / 2
            val arcSize = Size(size.width - stroke, size.height - stroke)
            drawArc(track, 0f, 360f, false, Offset(inset, inset), arcSize, style = Stroke(stroke))
            drawArc(fill, -90f, 360f * fraction, false, Offset(inset, inset), arcSize, style = Stroke(stroke, cap = StrokeCap.Round))
        }
        Column {
            Text(
                when { done && celebrate -> "Done"; done -> "0 new"; else -> "${total - opened} left" },
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.ExtraBold,
            )
            Text(
                when { done && celebrate -> "all opened"; done -> "$total opened"; else -> "$opened of $total opened" },
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** The celebration, right after the user opened (or archived) the last unopened save. */
@Composable
internal fun AllCaughtUpCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(LordsExtraColors.successContainer)
            .padding(horizontal = 18.dp, vertical = 20.dp)
            .semantics(mergeDescendants = true) {},
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 12.dp, bottomEnd = 28.dp, bottomStart = 12.dp))
                .background(LordsExtraColors.success),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(Icons.Filled.Check, contentDescription = null, color = Color.White, modifier = Modifier.size(28.dp))
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                "All caught up",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = LordsExtraColors.onSuccessContainer,
            )
            Text(
                "You opened everything you saved. New saves show up here.",
                style = MaterialTheme.typography.bodySmall,
                color = LordsExtraColors.onSuccessContainer,
            )
        }
    }
}

/**
 * "All saves" row right above the list: Filters sits next to what it changes.
 * [picked] is the row of chosen category/tag chips, shown under it.
 */
@Composable
internal fun AllSavesHeader(
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
            FiltersButton(activeCount = activeFilters, onClick = onFilters)
        }
        picked()
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
        TypeThumb(type = if (noPreview) "none" else link.type, muted = link.isArchived, linkId = link.id, imageUrl = link.previewImage)

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
                // Moves into the Link details title (real titles only; the URL fallback differs there)
                modifier = if (noPreview) Modifier else Modifier.sharedTitle(link.id),
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

/** Thumbnail colors and icon per link type ("none" = no preview yet). */
private data class TypeLook(val bg: Color, val fg: Color, val icon: ImageVector)

@Composable
private fun typeLook(type: String): TypeLook = when (type) {
    "video" -> TypeLook(LordsExtraColors.mediaPlaceholder, MaterialTheme.colorScheme.secondary, Icons.Filled.PlayArrow)
    "social" -> TypeLook(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer, Icons.Outlined.ChatBubbleOutline)
    "article" -> TypeLook(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer, Icons.AutoMirrored.Outlined.Article)
    else -> TypeLook(MaterialTheme.colorScheme.surfaceContainerHigh, MaterialTheme.colorScheme.onSurfaceVariant, Icons.Filled.Link)
}

@Composable
private fun TypeThumb(type: String, muted: Boolean = false, linkId: String? = null, imageUrl: String? = null) {
    val look = typeLook(type)
    // Archived: same shape and icon, neutral colors
    val bg = if (muted) MaterialTheme.colorScheme.surfaceContainerHigh else look.bg
    val fg = if (muted) MaterialTheme.colorScheme.onSurfaceVariant else look.fg
    Box(
        // Grows into the Link details badge when the card opens details
        modifier = (if (linkId != null) Modifier.sharedThumb(linkId) else Modifier)
            .size(72.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(bg),
        contentAlignment = Alignment.Center,
    ) {
        LinkPreviewImage(
            imageUrl = imageUrl,
            modifier = Modifier.matchParentSize().graphicsLayer { if (muted && imageUrl != null) alpha = 0.5f },
            fallback = { AppIcon(look.icon, contentDescription = null, color = fg) },
        )
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

/**
 * "Pick up where you left off": the newest unopened saves as a sideways row of large cards.
 * Tapping one opens the link, which marks it opened, so it leaves the row. "Mark as opened" does the
 * same without opening it.
 */
@Composable
internal fun PickUpStrip(
    links: List<Link>,
    unopenedCount: Int,
    onOpen: (Link) -> Unit,
    onMarkOpened: (Link) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Pick up where you left off",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.weight(1f).padding(start = 4.dp).semantics { heading() },
            )
            Text(
                "$unopenedCount unopened",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        AppLazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(links, key = { it.id }) { link ->
                PickUpCard(link, onOpen = { onOpen(link) }, onMarkOpened = { onMarkOpened(link) }, modifier = Modifier.animateItem())
            }
        }
    }
}

@Composable
private fun PickUpCard(link: Link, onOpen: () -> Unit, onMarkOpened: () -> Unit, modifier: Modifier = Modifier) {
    val noPreview = link.title.isBlank() || link.title == link.url
    val look = typeLook(if (noPreview) "none" else link.type)
    val meta = listOfNotNull(hostOf(link.url), savedAtLabel(link.createdAt)).joinToString(" · ")
    Column(
        modifier = modifier
            .width(220.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(role = Role.Button, onClickLabel = "Open link", onClick = onOpen),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(96.dp).background(look.bg),
            contentAlignment = Alignment.Center,
        ) {
            LinkPreviewImage(
                imageUrl = link.previewImage,
                modifier = Modifier.matchParentSize(),
                fallback = { AppIcon(look.icon, contentDescription = null, color = look.fg, modifier = Modifier.size(28.dp)) },
            )
            MarkOpenedCheck(onMarked = onMarkOpened, modifier = Modifier.align(Alignment.TopEnd))
        }
        Column(Modifier.padding(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 14.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                meta,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                if (noPreview) link.url else link.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                minLines = 2, // cards in the row line up
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/**
 * "You saved this 3 weeks ago": one forgotten save with Open, Archive and (when there are more)
 * a button that shows another.
 */
@Composable
internal fun ResurfaceCard(
    link: Link,
    label: String,
    categoryPath: String?,
    onOpen: () -> Unit,
    onArchive: () -> Unit,
    onShowAnother: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val fg = MaterialTheme.colorScheme.onSecondaryContainer
    val noPreview = link.title.isBlank() || link.title == link.url
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AppIcon(Icons.Filled.History, contentDescription = null, color = fg, modifier = Modifier.size(18.dp))
            Text(
                label.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.ExtraBold,
                color = fg,
                modifier = Modifier.semantics { heading() },
            )
        }
        Text(
            if (noPreview) link.url else link.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = fg,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            listOfNotNull(hostOf(link.url), categoryPath).joinToString(" · "),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = fg,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            CardAction("Open", MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary, onOpen, Modifier.weight(1f))
            CardAction("Archive", MaterialTheme.colorScheme.surface, fg, onArchive, Modifier.weight(1f))
            if (onShowAnother != null) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(role = Role.Button, onClick = onShowAnother)
                        .semantics { contentDescription = "Show another" },
                    contentAlignment = Alignment.Center,
                ) {
                    AppIcon(Icons.Filled.Refresh, contentDescription = null, color = fg, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun CardAction(text: String, container: Color, content: Color, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .heightIn(min = 44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(container)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = content)
    }
}

/**
 * Feed card with swipe actions: right archives (the card leaves the list; Undo is offered by the
 * caller), left asks to delete (the card snaps back while the caller confirms). Archived links
 * (shown via Filters) can't be archived again.
 */
@Composable
internal fun SwipeableFeedCard(
    link: Link,
    onArchive: () -> Unit,
    onRequestDelete: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    // Plain remember, not rememberSwipeToDismissBoxState: that one is saveable, and the list keeps saved
    // state per item key, so a card archived by a swipe came back from Undo still "swiped right", and
    // SwipeToDismissBox archived it again. A fresh state per appearance always starts settled.
    val threshold = SwipeToDismissBoxDefaults.positionalThreshold
    val state = remember { SwipeToDismissBoxState(SwipeToDismissBoxValue.Settled, threshold) }
    val scope = rememberCoroutineScope()
    // SwipeToDismissBox re-runs onDismiss whenever this lambda changes while the card is still swiped
    // away, so it must stay the same instance: a new one on each recomposition (Undo causes one)
    // archived the link again and again.
    val archive by rememberUpdatedState(onArchive)
    val requestDelete by rememberUpdatedState(onRequestDelete)
    val onDismiss: (SwipeToDismissBoxValue) -> Unit = remember(state, scope) {
        { direction ->
            when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> archive()
                SwipeToDismissBoxValue.EndToStart -> {
                    requestDelete()
                    scope.launch { state.reset() } // stays put until the delete is confirmed
                }
                SwipeToDismissBoxValue.Settled -> Unit
            }
        }
    }
    // Archived but still listed (Filters include archived, or Undo while it was leaving): bring it back
    LaunchedEffect(link.isArchived) {
        if (state.currentValue != SwipeToDismissBoxValue.Settled) state.reset()
    }
    SwipeToDismissBox(
        state = state,
        modifier = modifier.clip(RoundedCornerShape(24.dp)),
        enableDismissFromStartToEnd = !link.isArchived,
        onDismiss = onDismiss,
        backgroundContent = {
            val archive = state.dismissDirection == SwipeToDismissBoxValue.StartToEnd
            val delete = state.dismissDirection == SwipeToDismissBoxValue.EndToStart
            val bg = when {
                archive -> MaterialTheme.colorScheme.inverseSurface
                delete -> MaterialTheme.colorScheme.error
                else -> Color.Transparent
            }
            val fg = if (archive) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onError
            Row(
                modifier = Modifier.fillMaxSize().background(bg).padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, if (delete) Alignment.End else Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (archive || delete) {
                    AppIcon(
                        if (archive) Icons.Outlined.Archive else Icons.Outlined.Delete,
                        contentDescription = null,
                        color = fg,
                        modifier = Modifier.size(22.dp),
                    )
                    Text(
                        if (archive) "Archive" else "Delete",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = fg,
                    )
                }
            }
        },
    ) { content() }
}

/**
 * The pick-up card's "Mark as opened" check: gray while unopened. A tap fills it with the brand
 * color, the check pops (a bouncy spring) with a haptic, and only then [onMarked] runs, so the card
 * leaves after the user has seen it land. With animations off it changes at once.
 */
@Composable
private fun MarkOpenedCheck(onMarked: () -> Unit, modifier: Modifier = Modifier) {
    var done by remember { mutableStateOf(false) }
    val reduced = rememberReducedMotion()
    val haptics = LocalHapticFeedback.current
    val marked by rememberUpdatedState(onMarked)
    val fill by animateColorAsState(
        if (done) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        AppMotion.effectsFast(), label = "check fill",
    )
    val tint by animateColorAsState(
        if (done) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        AppMotion.effectsFast(), label = "check tint",
    )
    val pop = remember { Animatable(1f) }
    LaunchedEffect(done) {
        if (!done) return@LaunchedEffect
        haptics.performHapticFeedback(HapticFeedbackType.Confirm)
        if (!reduced) {
            // Hero moment, so a bouncy spring rather than the calm motion-scheme default
            pop.animateTo(1.35f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium))
            pop.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium))
            delay(150)
        }
        marked()
    }
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(enabled = !done, role = Role.Button, onClickLabel = "Mark as opened") { done = true }
            .semantics { contentDescription = if (done) "Marked as opened" else "Mark as opened" },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .graphicsLayer { scaleX = pop.value; scaleY = pop.value }
                .clip(CircleShape)
                .background(fill),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(Icons.Filled.Check, contentDescription = null, color = tint, modifier = Modifier.size(18.dp))
        }
    }
}

/** Nothing unopened, but not just cleared (after a restore, or on a later visit): calm, no celebration. */
@Composable
internal fun NothingNewCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 18.dp, vertical = 18.dp)
            .semantics(mergeDescendants = true) {},
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.surfaceContainerHigh),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(Icons.Filled.Inbox, contentDescription = null, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Nothing new waiting", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
            Text(
                "Everything here is opened. Share a link from any app, or tap + to save one.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
