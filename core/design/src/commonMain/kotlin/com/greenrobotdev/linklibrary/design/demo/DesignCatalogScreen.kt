package com.greenrobotdev.linklibrary.design.demo

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.components.button.ActionButton
import com.greenrobotdev.linklibrary.design.components.button.DashedPillButton
import com.greenrobotdev.linklibrary.design.components.button.IconButton
import com.greenrobotdev.linklibrary.design.components.button.OutlinedButton
import com.greenrobotdev.linklibrary.design.components.button.StandardButton
import com.greenrobotdev.linklibrary.design.components.button.TextButton
import com.greenrobotdev.linklibrary.design.components.cards.ContentCard
import com.greenrobotdev.linklibrary.design.components.cards.FormCard
import com.greenrobotdev.linklibrary.design.components.cards.InputCard
import com.greenrobotdev.linklibrary.design.components.cards.SectionContentCard
import com.greenrobotdev.linklibrary.design.components.cards.ToggleCard
import com.greenrobotdev.linklibrary.design.components.emptystate.EmptyState
import com.greenrobotdev.linklibrary.design.components.feedback.ErrorMessageCard
import com.greenrobotdev.linklibrary.design.components.icon.IconBackgroundVariant
import com.greenrobotdev.linklibrary.design.components.layout.AppActionBar
import com.greenrobotdev.linklibrary.design.components.layout.AppBarAction
import com.greenrobotdev.linklibrary.design.components.layout.AppBarPill
import com.greenrobotdev.linklibrary.design.components.layout.AppLazyRow
import com.greenrobotdev.linklibrary.design.components.layout.AppUndoBar
import com.greenrobotdev.linklibrary.design.components.layout.SwipeConfirmBar
import com.greenrobotdev.linklibrary.design.components.layout.appVerticalScroll
import com.greenrobotdev.linklibrary.design.components.progress.CircularProgress
import com.greenrobotdev.linklibrary.design.components.selection.AppCheckbox
import com.greenrobotdev.linklibrary.design.components.selection.AppRadioGroup
import com.greenrobotdev.linklibrary.design.components.selection.AppSlider
import com.greenrobotdev.linklibrary.design.components.selection.AppSwitch
import com.greenrobotdev.linklibrary.design.components.selection.SelectionPill
import com.greenrobotdev.linklibrary.design.components.textfield.AppTextFieldEnhanced
import com.greenrobotdev.linklibrary.design.components.textfield.AppTextFieldWithCharacterLimit
import com.greenrobotdev.linklibrary.design.components.textfield.OutlinedTextField
import com.greenrobotdev.linklibrary.design.components.textfield.SearchField
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon
import com.greenrobotdev.linklibrary.design.foundation.icon.IconSize
import com.greenrobotdev.linklibrary.design.motion.AppMotion
import com.greenrobotdev.linklibrary.design.motion.shimmer
import com.greenrobotdev.linklibrary.design.theme.ComponentSize
import com.greenrobotdev.linklibrary.design.theme.ComponentStyle
import com.greenrobotdev.linklibrary.design.theme.LinkLibraryTheme
import com.greenrobotdev.linklibrary.design.theme.LordsExtraColors
import com.greenrobotdev.linklibrary.design.components.icon.AppIcon as BackgroundIcon

/**
 * Debug-only design catalog: every component the app builds with, plus the mandatory templates
 * (see CLAUDE.md), each under its exact name. The Dark switch re-themes the whole catalog so both
 * themes can be checked side by side.
 */
@Composable
fun DesignCatalogScreen(onBack: () -> Unit) {
    var dark by remember { mutableStateOf(false) }
    LinkLibraryTheme(darkTheme = dark) {
        Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.size(48.dp).clip(CircleShape).clickable(onClickLabel = "Back", onClick = onBack),
                    contentAlignment = Alignment.Center,
                ) {
                    AppIcon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", color = MaterialTheme.colorScheme.onSurface)
                }
                Text(
                    "Design catalog",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                Text("Dark", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface)
                Switch(checked = dark, onCheckedChange = { dark = it }, modifier = Modifier.padding(horizontal = 12.dp))
            }
            Column(
                modifier = Modifier.fillMaxSize().appVerticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                FoundationsSection()
                IconsSection()
                ButtonsSection()
                InputsSection()
                CardsSection()
                SelectionSection()
                FeedbackSection()
                LayoutSection()
                MotionSection()
            }
        }
    }
}

// --- Building blocks of the catalog itself ---

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        content()
    }
}

/** One component on show, under its exact name. */
@Composable
private fun Specimen(name: String, note: String? = null, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(name, style = MaterialTheme.typography.labelLarge, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.primary)
        note?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        content()
    }
}

// --- Sections ---

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FoundationsSection() = Section("Foundations") {
    val c = MaterialTheme.colorScheme
    Specimen("MaterialTheme.colorScheme", "Always use these roles, never hard-coded colors") {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                "primary" to c.primary, "onPrimary" to c.onPrimary, "primaryContainer" to c.primaryContainer,
                "secondary" to c.secondary, "secondaryContainer" to c.secondaryContainer,
                "tertiary" to c.tertiary, "tertiaryContainer" to c.tertiaryContainer,
                "surface" to c.surface, "surfaceContainer" to c.surfaceContainer, "surfaceContainerHigh" to c.surfaceContainerHigh,
                "onSurface" to c.onSurface, "onSurfaceVariant" to c.onSurfaceVariant, "outlineVariant" to c.outlineVariant,
                "error" to c.error, "errorContainer" to c.errorContainer, "scrim" to c.scrim,
            ).forEach { (name, color) -> Swatch(name, color) }
        }
    }
    Specimen("LordsExtraColors", "Brand extras outside the Material roles (fixed, same in both themes)") {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                "noteContainer" to LordsExtraColors.noteContainer, "success" to LordsExtraColors.success,
                "successContainer" to LordsExtraColors.successContainer, "mediaPlaceholder" to LordsExtraColors.mediaPlaceholder,
                "archivedSurface" to LordsExtraColors.archivedSurface, "textStrong" to LordsExtraColors.textStrong,
            ).forEach { (name, color) -> Swatch(name, color) }
        }
    }
    Specimen("MaterialTheme.typography") {
        val t = MaterialTheme.typography
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(
                "headlineLarge" to t.headlineLarge, "headlineSmall" to t.headlineSmall, "titleLarge" to t.titleLarge,
                "titleMedium" to t.titleMedium, "titleSmall" to t.titleSmall, "bodyLarge" to t.bodyLarge,
                "bodyMedium" to t.bodyMedium, "bodySmall" to t.bodySmall, "labelLarge" to t.labelLarge, "labelSmall" to t.labelSmall,
            ).forEach { (name, style) -> TypeSample(name, style) }
        }
    }
    Specimen("Corner radii", "Cards 24, buttons 12, chips 16, small tiles 8") {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf(8, 12, 16, 24).forEach { r ->
                Box(
                    Modifier.size(64.dp).clip(RoundedCornerShape(r.dp)).background(c.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) { Text("${r}dp", style = MaterialTheme.typography.labelMedium, color = c.onPrimaryContainer) }
            }
        }
    }
}

@Composable
private fun Swatch(name: String, color: Color) {
    Column(Modifier.width(96.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            Modifier.fillMaxWidth().height(48.dp).clip(RoundedCornerShape(12.dp)).background(color)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
        )
        Text(name, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
        Text(
            "#" + color.toArgb().toUInt().toString(16).uppercase().padStart(8, '0').takeLast(6),
            style = MaterialTheme.typography.labelSmall,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun TypeSample(name: String, style: TextStyle) {
    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(name, style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(104.dp))
        Text("Save & Find", style = style, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
    }
}

@Composable
private fun IconsSection() = Section("Icons") {
    Specimen("AppIcon", "The only icon to use. size = IconSize, or a Modifier.size") {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            listOf(IconSize.Small, IconSize.Medium, IconSize.Large, IconSize.ExtraLarge).forEach {
                AppIcon(Icons.Filled.Bookmark, contentDescription = null, color = MaterialTheme.colorScheme.primary, size = it)
            }
        }
    }
    Specimen("AppIcon (with background)", "variant = Regular / Compact") {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            BackgroundIcon(Icons.Filled.Star, variant = IconBackgroundVariant.Regular)
            BackgroundIcon(Icons.Filled.Star, variant = IconBackgroundVariant.Compact)
        }
    }
}

@Composable
private fun ButtonsSection() = Section("Buttons") {
    Specimen("StandardButton", "style = Filled / Outlined, size = Small / Medium") {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            StandardButton(onClick = {}, text = "Save link", icon = Icons.Filled.Save, modifier = Modifier.fillMaxWidth())
            StandardButton(onClick = {}, text = "Outlined", style = ComponentStyle.Outlined, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StandardButton(onClick = {}, text = "Small", size = ComponentSize.Small)
                StandardButton(onClick = {}, text = "Disabled", enabled = false)
            }
        }
    }
    Specimen("OutlinedButton / TextButton") {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(text = "Cancel", onClick = {})
            TextButton(onClick = {}, text = "Learn more")
        }
    }
    Specimen("IconButton") {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(onClick = {}, icon = Icons.Filled.Search, contentDescription = "Search")
            IconButton(onClick = {}, icon = Icons.Filled.Star, contentDescription = "Favorite", style = ComponentStyle.Filled)
        }
    }
    Specimen("ActionButton", "Shows a spinner while isLoading") {
        var loading by remember { mutableStateOf(false) }
        ActionButton(text = if (loading) "Fetching…" else "Tap to load", onClick = { loading = !loading }, isLoading = loading)
    }
    Specimen("DashedPillButton", "For \"add\" actions (+ Tag, + Category)") {
        DashedPillButton(text = "Tag", onClick = {})
    }
}

@Composable
private fun InputsSection() = Section("Inputs") {
    Specimen("AppTextFieldEnhanced", "The default text field (showClear, leadingIcon, isError)") {
        var text by remember { mutableStateOf("Kotlin Multiplatform") }
        AppTextFieldEnhanced(value = text, onValueChange = { text = it }, label = "Title", leadingIcon = Icons.Filled.Link, showClear = true)
    }
    Specimen("AppTextFieldEnhanced (isError)") {
        var text by remember { mutableStateOf("not a link") }
        AppTextFieldEnhanced(value = text, onValueChange = { text = it }, label = "Link", isError = true)
    }
    Specimen("AppTextFieldWithCharacterLimit") {
        var text by remember { mutableStateOf("Why am I saving this?") }
        AppTextFieldWithCharacterLimit(value = text, onValueChange = { text = it }, maxLength = 120, label = "Note")
    }
    Specimen("SearchField") {
        var q by remember { mutableStateOf("") }
        SearchField(query = q, onQueryChange = { q = it }, placeholder = "Search everything you saved", shape = CircleShape)
    }
    Specimen("OutlinedTextField", "With an error message") {
        var text by remember { mutableStateOf("") }
        OutlinedTextField(value = text, onValueChange = { text = it }, label = "Category name", isError = true, errorMessage = "A name is required")
    }
}

@Composable
private fun CardsSection() = Section("Cards (mandatory templates)") {
    Specimen("SectionContentCard", "Settings-style group with a title") {
        SectionContentCard(title = "Appearance") { Text("Content goes here", color = MaterialTheme.colorScheme.onSurface) }
    }
    Specimen("FormCard") {
        FormCard(headerIcon = Icons.Filled.Bookmark, headerTitle = "Link details") { Text("Form fields go here", color = MaterialTheme.colorScheme.onSurface) }
    }
    Specimen("ContentCard") {
        ContentCard(headerIcon = Icons.Filled.Notifications, headerTitle = "Reminder") { Text("Read this tonight", color = MaterialTheme.colorScheme.onSurface) }
    }
    Specimen("ToggleCard") {
        var on by remember { mutableStateOf(true) }
        ToggleCard(isEnabled = on, onEnabledChange = { on = it }, title = "Fetch previews", description = "Load page images on Wi-Fi")
    }
    Specimen("InputCard") {
        InputCard(title = "Note") { Text("An input lives here", color = MaterialTheme.colorScheme.onSurface) }
    }
    Specimen("ErrorMessageCard") {
        ErrorMessageCard(error = "Couldn't reach the page. Check your connection.", onDismiss = {})
    }
}

@Composable
private fun SelectionSection() = Section("Selection") {
    Specimen("AppSwitch") {
        var on by remember { mutableStateOf(true) }
        AppSwitch(checked = on, onCheckedChange = { on = it }, label = "Include archived")
    }
    Specimen("AppCheckbox") {
        var on by remember { mutableStateOf(false) }
        AppCheckbox(checked = on, onCheckedChange = { on = it }, label = "Favorites only")
    }
    Specimen("AppRadioGroup") {
        var picked by remember { mutableStateOf<String?>("Newest first") }
        AppRadioGroup(options = listOf("Newest first", "Oldest first"), selectedOption = picked, onOptionSelected = { picked = it })
    }
    Specimen("AppSlider") {
        var value by remember { mutableFloatStateOf(0.4f) }
        AppSlider(value = value, onValueChange = { value = it }, showValue = true)
    }
    Specimen("SelectionPill") {
        var picked by remember { mutableStateOf(true) }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SelectionPill(selected = picked, onClick = { picked = !picked }, text = "Videos")
            SelectionPill(selected = !picked, onClick = { picked = !picked }, text = "Articles")
        }
    }
}

@Composable
private fun FeedbackSection() = Section("Feedback") {
    Specimen("EmptyState") {
        EmptyState(icon = Icons.Filled.Bookmark, title = "Nothing here", description = "No saved link matches these filters.", actionLabel = "Clear filters", onActionClick = {})
    }
    Specimen("CircularProgress", "Indeterminate, and with progress") {
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
            CircularProgress()
            CircularProgress(progress = 0.65f)
        }
    }
    Specimen("Modifier.shimmer()", "Loading placeholder; flat when animations are off") {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(72.dp).clip(RoundedCornerShape(18.dp)).shimmer())
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                Box(Modifier.width(180.dp).height(14.dp).clip(RoundedCornerShape(7.dp)).shimmer())
                Box(Modifier.width(120.dp).height(14.dp).clip(RoundedCornerShape(7.dp)).shimmer())
            }
        }
    }
}

@Composable
private fun LayoutSection() = Section("Layout") {
    Specimen("AppActionBar + AppBarAction + AppBarPill", "Bottom action bar (Link details)") {
        AppActionBar {
            AppBarAction(icon = Icons.Filled.Star, label = "Favorite", onClick = {}, enabled = true)
            AppBarAction(icon = Icons.Filled.Archive, label = "Archive", onClick = {}, enabled = true)
            AppBarPill(text = "Open link", onClick = {}, modifier = Modifier.weight(1f))
        }
    }
    Specimen("SwipeConfirmBar", "Swipe to confirm a destructive action") {
        SwipeConfirmBar(onCancel = {}, onConfirm = {}, enabled = true)
    }
    Specimen("AppUndoBar") {
        AppUndoBar(title = "Category deleted", subtitle = "3 links are now uncategorized", onUndo = {}, durationMillis = 60_000)
    }
    Specimen("AppLazyRow / AppLazyColumn / Modifier.appVerticalScroll", "Use these, never the raw ones: no stretch overscroll, so taps work right after a fling") {
        AppLazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf("Dev", "Health", "Job › Germany", "Recipes", "Travel", "Reading")) { name ->
                Text(
                    name,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.primaryContainer).padding(horizontal = 14.dp, vertical = 10.dp),
                )
            }
        }
    }
}

@Composable
private fun MotionSection() = Section("Motion") {
    Specimen("AppMotion.spatial()", "Spring from the theme's motion scheme. Tap the box") {
        var big by remember { mutableStateOf(false) }
        val size by animateDpAsState(if (big) 120.dp else 64.dp, AppMotion.spatial(), label = "catalog spring")
        Box(
            Modifier.size(size).clip(RoundedCornerShape(24.dp)).background(MaterialTheme.colorScheme.tertiaryContainer)
                .clickable(onClickLabel = "Animate") { big = !big },
        )
    }
    Specimen("Image states (PlatformAsyncImage + LinkPreviewImage)", "Loading = shimmer, failed or none = type icon, loaded = image") {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(72.dp).clip(RoundedCornerShape(18.dp)).shimmer())
            Box(
                Modifier.size(72.dp).clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center,
            ) { AppIcon(Icons.Filled.Link, contentDescription = null, color = MaterialTheme.colorScheme.onSecondaryContainer) }
            Box(
                Modifier.size(72.dp).clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.errorContainer),
                contentAlignment = Alignment.Center,
            ) { AppIcon(Icons.Filled.Delete, contentDescription = null, color = MaterialTheme.colorScheme.onErrorContainer) }
        }
    }
}
