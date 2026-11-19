package com.greenrobotdev.lordofthelinks.screens.welcome

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.greenrobotdev.lordofthelinks.design.components.button.StandardButton
import com.greenrobotdev.lordofthelinks.design.foundation.icon.AppIcon
import com.greenrobotdev.lordofthelinks.design.motion.AppMotion
import com.greenrobotdev.lordofthelinks.design.theme.LordsExtraColors
import kotlinx.coroutines.launch

private class TourPage(val eyebrow: String, val headline: String, val body: String)

private val PAGES = listOf(
    TourPage(
        "WELCOME", "Your links, all in one place",
        "Save articles, videos and posts from any app, and find them again in seconds. Everything stays on this phone.",
    ),
    TourPage(
        "SAVE FROM ANYWHERE", "Share it, and it's kept",
        "In any app tap Share, then Lords of Links. The link is saved at once; title, category and tags are optional. In the Feed, + adds a link too.",
    ),
    TourPage(
        "YOUR FEED", "Pick up where you left off",
        "New saves wait at the top until you open them. Swipe a card right to archive it, or left to delete it. Archive can be undone.",
    ),
    TourPage(
        "ORGANIZE", "Categories, tags and lists",
        "Put links in categories (with subcategories) and add tags. The Library opens any category, tag, or your Unopened, Favorites and Archived lists.",
    ),
    TourPage(
        "FIND & KEEP", "Find it again in seconds",
        "Search everything you saved, narrow it down with Filters, and export a backup whenever you like from Settings.",
    ),
)

/**
 * "How Lords of Links works": five swipeable pages (design: Welcome tour). Shown once on first
 * launch and any time from Settings. [onDone] runs on "Start saving" (and on Back from Settings).
 */
@Composable
fun WelcomeTourScreen(onDone: () -> Unit) {
    val pager = rememberPagerState { PAGES.size }
    val scope = rememberCoroutineScope()
    val page = pager.currentPage
    val last = page == PAGES.lastIndex
    fun goTo(target: Int) = scope.launch { pager.animateScrollToPage(target) }

    // Back steps through the pages before leaving the tour
    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = page > 0,
        onBackCompleted = { goTo(page - 1) },
    )

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.size(48.dp)) {
                if (page > 0) {
                    Box(
                        Modifier.size(48.dp).clip(CircleShape).clickable(onClickLabel = "Previous page") { goTo(page - 1) },
                        contentAlignment = Alignment.Center,
                    ) {
                        AppIcon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
            Text(
                "${page + 1} of ${PAGES.size}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
            Box(Modifier.width(72.dp), contentAlignment = Alignment.CenterEnd) {
                if (!last) {
                    Text(
                        "Skip",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clip(RoundedCornerShape(22.dp))
                            .clickable(onClickLabel = "Skip to the last page") { goTo(PAGES.lastIndex) }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                    )
                }
            }
        }

        HorizontalPager(state = pager, modifier = Modifier.weight(1f), overscrollEffect = null) { index ->
            Column {
                Box(
                    modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp).fillMaxWidth().height(320.dp)
                        .clip(RoundedCornerShape(36.dp)).background(heroColor(index)),
                    contentAlignment = Alignment.Center,
                ) {
                    when (index) {
                        0 -> WelcomeArt()
                        1 -> SaveArt()
                        2 -> FeedArt()
                        3 -> OrganizeArt()
                        else -> FindArt()
                    }
                }
                Column(Modifier.padding(horizontal = 28.dp, vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        PAGES[index].eyebrow,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        PAGES[index].headline,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.semantics { heading() },
                    )
                    Text(PAGES[index].body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f).semantics { contentDescription = "Page ${page + 1} of ${PAGES.size}" },
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PAGES.indices.forEach { i ->
                    val width by animateDpAsState(if (i == page) 24.dp else 8.dp, AppMotion.spatialFast(), label = "tour dot")
                    Box(
                        Modifier.height(8.dp).width(width).clip(CircleShape)
                            .background(if (i == page) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
                    )
                }
            }
            StandardButton(
                text = if (last) "Start saving" else "Next",
                icon = Icons.AutoMirrored.Filled.ArrowForward,
                onClick = { if (last) onDone() else goTo(page + 1) },
            )
        }
    }
}

@Composable
private fun heroColor(page: Int): Color = when (page) {
    1 -> MaterialTheme.colorScheme.tertiaryContainer
    2 -> MaterialTheme.colorScheme.secondaryContainer
    3 -> LordsExtraColors.successContainer
    else -> MaterialTheme.colorScheme.primaryContainer
}

// --- Page pictures: small mock-ups of the real screens, decorative only ---

@Composable
private fun Tile(width: Dp, height: Dp, color: Color, radius: Dp = 24.dp, modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit = {}) =
    Box(modifier.width(width).height(height).clip(RoundedCornerShape(radius)).background(color), contentAlignment = Alignment.Center, content = content)

@Composable
private fun Bar(width: Dp, color: Color, height: Dp = 10.dp) = Box(Modifier.width(width).height(height).clip(CircleShape).background(color))

@Composable
private fun MiniCard(thumb: Color, icon: ImageVector? = null, iconColor: Color = Color.Unspecified) {
    Row(
        Modifier.width(280.dp).clip(RoundedCornerShape(22.dp)).background(MaterialTheme.colorScheme.surface).padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Tile(56.dp, 56.dp, thumb, radius = 16.dp) { icon?.let { AppIcon(it, contentDescription = null, color = iconColor) } }
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Bar(80.dp, MaterialTheme.colorScheme.outlineVariant, 8.dp)
            Bar(140.dp, MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun WelcomeArt() {
    Box(Modifier.size(260.dp, 230.dp)) {
        Tile(190.dp, 120.dp, MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.offset(20.dp, 50.dp).rotate(-9f))
        Tile(190.dp, 120.dp, MaterialTheme.colorScheme.tertiaryContainer, modifier = Modifier.offset(50.dp, 40.dp).rotate(7f))
        Column(Modifier.offset(34.dp, 52.dp).width(196.dp).clip(RoundedCornerShape(24.dp)).background(MaterialTheme.colorScheme.surface)) {
            Box(Modifier.fillMaxWidth().height(64.dp).background(LordsExtraColors.mediaPlaceholder), contentAlignment = Alignment.Center) {
                Tile(36.dp, 36.dp, MaterialTheme.colorScheme.tertiary, radius = 12.dp) {
                    AppIcon(Icons.Filled.PlayArrow, contentDescription = null, color = MaterialTheme.colorScheme.onTertiary, modifier = Modifier.size(20.dp))
                }
            }
            Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Bar(110.dp, MaterialTheme.colorScheme.primaryContainer, 8.dp)
                Bar(160.dp, MaterialTheme.colorScheme.onSurface)
            }
        }
        Tile(64.dp, 64.dp, MaterialTheme.colorScheme.primary, radius = 22.dp, modifier = Modifier.align(Alignment.TopEnd).offset((-6).dp, 6.dp)) {
            AppIcon(Icons.Filled.Bookmark, contentDescription = null, color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(30.dp))
        }
    }
}

@Composable
private fun SaveArt() {
    Column(
        Modifier.width(290.dp).clip(RoundedCornerShape(28.dp)).background(MaterialTheme.colorScheme.surface).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(Modifier.align(Alignment.CenterHorizontally).width(36.dp).height(4.dp).clip(CircleShape).background(MaterialTheme.colorScheme.outlineVariant))
        Text("Share", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("Messages", "Lords of Links", "Mail", "Copy").forEach { name ->
                val ours = name == "Lords of Links"
                Column(Modifier.width(62.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Tile(48.dp, 48.dp, if (ours) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh, radius = 16.dp) {
                        if (ours) AppIcon(Icons.Filled.Bookmark, contentDescription = null, color = MaterialTheme.colorScheme.onPrimary)
                    }
                    Text(
                        name,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (ours) FontWeight.ExtraBold else FontWeight.Normal,
                        color = if (ours) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                    )
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(LordsExtraColors.successContainer).padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppIcon(Icons.Filled.Check, contentDescription = null, color = LordsExtraColors.onSuccessContainer, modifier = Modifier.size(16.dp))
            Text("Link kept. Details are optional.", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = LordsExtraColors.onSuccessContainer)
        }
    }
}

/** A card shown mid-swipe over the action it reveals. */
@Composable
private fun SwipedCard(shift: Dp, background: Color, content: Color, icon: ImageVector, label: String, thumb: Color) {
    Box(Modifier.width(280.dp).clip(RoundedCornerShape(22.dp))) {
        Row(
            Modifier.matchParentSize().background(background).padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, if (shift > 0.dp) Alignment.Start else Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppIcon(icon, contentDescription = null, color = content, modifier = Modifier.size(18.dp))
            Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.ExtraBold, color = content)
        }
        Box(Modifier.offset(x = shift)) { MiniCard(thumb) }
    }
}

@Composable
private fun FeedArt() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        SwipedCard(96.dp, MaterialTheme.colorScheme.inverseSurface, MaterialTheme.colorScheme.inverseOnSurface, Icons.Filled.Archive, "Archive", MaterialTheme.colorScheme.primaryContainer)
        SwipedCard((-96).dp, MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.onError, Icons.Filled.Delete, "Delete", MaterialTheme.colorScheme.secondaryContainer)
        Row(Modifier.width(280.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("→ Swipe right: archive", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
            Text("Swipe left: delete ←", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}

@Composable
private fun OrganizeArt() {
    Column(Modifier.width(290.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf(
                Triple("Unopened", MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary),
                Triple("Favorites", MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer),
            ).forEach { (name, bg, fg) ->
                Column(
                    Modifier.weight(1f).height(60.dp).clip(RoundedCornerShape(18.dp)).background(bg).padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = fg)
                    Text(if (name == "Unopened") "5 links" else "3 links", style = MaterialTheme.typography.labelSmall, color = fg)
                }
            }
        }
        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.surface).padding(vertical = 6.dp)) {
            listOf(Triple("Dev", 0, false), Triple("Android", 1, true), Triple("Kotlin", 1, false)).forEach { (name, depth, picked) ->
                Text(
                    name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (picked) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                        .background(if (picked) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                        .padding(start = (14 + depth * 22).dp, top = 8.dp, bottom = 8.dp),
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("#kotlin", "#recipes", "#travel").forEach {
                Text(
                    it,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface).padding(horizontal = 12.dp, vertical = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun FindArt() {
    Column(Modifier.width(290.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Row(
                Modifier.weight(1f).height(48.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceContainerHigh).padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppIcon(Icons.Filled.Search, contentDescription = null, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                Text("kotlin offline", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(
                Modifier.height(48.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary).padding(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppIcon(Icons.Filled.Tune, contentDescription = null, color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
                Text("2", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
        MiniCard(MaterialTheme.colorScheme.primaryContainer, Icons.AutoMirrored.Outlined.Article, MaterialTheme.colorScheme.onPrimaryContainer)
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(LordsExtraColors.successContainer).padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppIcon(Icons.Filled.FileDownload, contentDescription = null, color = LordsExtraColors.onSuccessContainer)
            Text("Export a backup anytime: Settings → Your data", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = LordsExtraColors.onSuccessContainer)
        }
    }
}
