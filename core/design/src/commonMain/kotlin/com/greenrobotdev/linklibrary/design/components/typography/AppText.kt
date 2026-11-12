package com.greenrobotdev.linklibrary.design.components.typography

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * AppText - Comprehensive text component system with contextual usage styles
 *
 * Provides a complete set of text components optimized for different contexts
 * throughout the application. Each component is pre-configured with appropriate
 * Material 3 typography scales, colors, and behaviors.
 *
 * Design Philosophy:
 * - Consistent Material 3 Expressive styling
 * - Context-appropriate sizing and weights
 * - Enhanced dark theme support with proper contrast
 * - Flexible overflow and line limiting options
 * - Reusable across all application screens
 *
 * Categories:
 * - Headings: Large, prominent text for titles and headers
 * - Body: Content text for descriptions and information
 * - Labels: Form labels and UI indicators
 * - Metadata: Secondary information like dates, tags, counts
 * - Special: Empty states, status messages, emphasis
 */

// ==================== HEADING TEXT COMPONENTS ====================

/**
 * HeadingText - Large, prominent heading for main titles and sections
 *
 * Usage: Screen titles, section headers, major headings
 * Style: Material 3 headlineLarge with semi-bold weight
 * Color: onSurface for maximum contrast
 *
 * @param text The heading text to display
 * @param modifier Optional modifier for layout
 * @param maxLines Maximum lines (default: 2)
 */
@Composable
fun HeadingText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 2
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

/**
 * SubheadingText - Medium heading for subsections and card titles
 *
 * Usage: Card titles, subsection headers, medium emphasis headings
 * Style: Material 3 titleLarge with semi-bold weight
 * Color: onSurface for hierarchy
 *
 * @param text The subheading text
 * @param modifier Optional modifier
 * @param maxLines Maximum lines (default: 2)
 */
@Composable
fun SubheadingText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 2
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

/**
 * SectionTitleText - Small heading for section grouping
 *
 * Usage: Form section titles, group headers, small sections
 * Style: Material 3 titleMedium with semi-bold weight
 * Color: onSurface
 *
 * @param text The section title
 * @param modifier Optional modifier
 * @param maxLines Maximum lines (default: 1)
 */
@Composable
fun SectionTitleText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

// ==================== BODY TEXT COMPONENTS ====================

/**
 * BodyText - Standard body text for content and descriptions
 *
 * Usage: Descriptions, content, information text
 * Style: Material 3 bodyLarge with normal weight
 * Color: onSurface for readability
 *
 * @param text The body text
 * @param modifier Optional modifier
 * @param maxLines Maximum lines (default: Int.MAX_VALUE)
 */
@Composable
fun BodyText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = maxLines,
        overflow = if (maxLines == Int.MAX_VALUE) {
            TextOverflow.Clip
        } else {
            TextOverflow.Ellipsis
        },
        modifier = modifier
    )
}

/**
 * BodyMediumText - Medium-sized body text
 *
 * Usage: Secondary descriptions, shorter content blocks
 * Style: Material 3 bodyMedium
 * Color: onSurface
 *
 * @param text The body medium text
 * @param modifier Optional modifier
 * @param maxLines Maximum lines (default: 3)
 */
@Composable
fun BodyMediumText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 3
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

/**
 * DescriptionText - Text for descriptions and helper content
 *
 * Usage: Item descriptions, helper text, supplementary content
 * Style: Material 3 bodyMedium with reduced contrast
 * Color: onSurfaceVariant for hierarchy
 *
 * @param text The description text
 * @param modifier Optional modifier
 * @param maxLines Maximum lines (default: 2)
 */
@Composable
fun DescriptionText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 2
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

/**
 * BodySmallText - Small body text for compact content
 *
 * Usage: Compact descriptions, secondary information
 * Style: Material 3 bodySmall
 * Color: onSurfaceVariant
 *
 * @param text The small body text
 * @param modifier Optional modifier
 * @param maxLines Maximum lines (default: 2)
 */
@Composable
fun BodySmallText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 2
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

// ==================== LABEL TEXT COMPONENTS ====================

/**
 * LabelText - Form label and UI indicator text
 *
 * Usage: Form field labels, input indicators, section labels
 * Style: Material 3 labelLarge with medium weight
 * Color: onSurface with prominence
 *
 * @param text The label text
 * @param modifier Optional modifier
 * @param maxLines Maximum lines (default: 1)
 */
@Composable
fun LabelText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

/**
 * LabelMediumText - Medium-sized label text
 *
 * Usage: Secondary labels, button labels, toggle labels
 * Style: Material 3 labelMedium
 * Color: onSurface
 *
 * @param text The label text
 * @param modifier Optional modifier
 * @param maxLines Maximum lines (default: 1)
 */
@Composable
fun LabelMediumText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

/**
 * LabelSmallText - Small label text for compact labels
 *
 * Usage: Compact labels, chip labels, small indicators
 * Style: Material 3 labelSmall
 * Color: onSurfaceVariant
 *
 * @param text The small label text
 * @param modifier Optional modifier
 * @param maxLines Maximum lines (default: 1)
 */
@Composable
fun LabelSmallText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

// ==================== METADATA TEXT COMPONENTS ====================

/**
 * MetadataText - Secondary information text
 *
 * Usage: Dates, file sizes, counts, secondary metadata
 * Style: Material 3 bodySmall with reduced prominence
 * Color: onSurfaceVariant for hierarchy
 *
 * @param text The metadata text
 * @param modifier Optional modifier
 * @param maxLines Maximum lines (default: 1)
 */
@Composable
fun MetadataText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

/**
 * CaptionText - Caption and subtitle text
 *
 * Usage: Image captions, subtitles, secondary titles
 * Style: Material 3 labelSmall
 * Color: onSurfaceVariant
 *
 * @param text The caption text
 * @param modifier Optional modifier
 * @param maxLines Maximum lines (default: 2)
 */
@Composable
fun CaptionText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 2
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

// ==================== SPECIAL TEXT COMPONENTS ====================

/**
 * EmptyStateText - Text for empty state messages
 *
 * Usage: Empty state messages, placeholder text
 * Style: Material 3 bodyLarge with centered appearance
 * Color: onSurfaceVariant for reduced emphasis
 *
 * @param text The empty state message
 * @param modifier Optional modifier
 */
@Composable
fun EmptyStateText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(16.dp)
    )
}

/**
 * ErrorText - Error message text
 *
 * Usage: Form validation errors, error messages
 * Style: Material 3 bodySmall with error color
 * Color: error for visibility
 *
 * @param text The error message
 * @param modifier Optional modifier
 * @param maxLines Maximum lines (default: 2)
 */
@Composable
fun ErrorText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 2
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

/**
 * WarningText - Warning message text
 *
 * Usage: Warning messages, important notices
 * Style: Material 3 bodySmall with warning color
 * Color: onTertiaryContainer
 *
 * @param text The warning message
 * @param modifier Optional modifier
 * @param maxLines Maximum lines (default: 2)
 */
@Composable
fun WarningText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 2
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onTertiaryContainer,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

/**
 * StatusText - Status indicator text
 *
 * Usage: Status badges, state indicators, status messages
 * Style: Material 3 labelSmall with medium weight
 * Color: Uses provided status color
 *
 * @param text The status text
 * @param modifier Optional modifier
 * @param statusColor Color for status indication
 * @param maxLines Maximum lines (default: 1)
 */
@Composable
fun StatusText(
    text: String,
    modifier: Modifier = Modifier,
    statusColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    maxLines: Int = 1
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Medium,
        color = statusColor,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

/**
 * EmphasizedText - Text with extra emphasis
 *
 * Usage: Important information, highlighted content
 * Style: Material 3 bodyMedium with semi-bold weight
 * Color: onSurface with prominence
 *
 * @param text The emphasized text
 * @param modifier Optional modifier
 * @param maxLines Maximum lines (default: 2)
 */
@Composable
fun EmphasizedText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 2
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

/**
 * LinkText - Hyperlink-style text
 *
 * Usage: Links, clickable text references
 * Style: Material 3 bodyMedium with link color
 * Color: primary for link appearance
 *
 * @param text The link text
 * @param modifier Optional modifier
 * @param maxLines Maximum lines (default: 1)
 */
@Composable
fun LinkText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

/**
 * CountText - Number/count display text
 *
 * Usage: Item counts, badge numbers, statistics
 * Style: Material 3 labelMedium with semi-bold weight
 * Color: onSurfaceVariant
 *
 * @param text The count text (typically numeric)
 * @param modifier Optional modifier
 */
@Composable
fun CountText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
}