package com.greenrobotdev.linklibrary.design.foundation.layout

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.greenrobotdev.linklibrary.design.tokens.SpacingTokens

/**
 * Foundation spacer component using design token spacing system
 *
 * This component provides a convenient way to add spacing using the
 * established design token system, ensuring consistent spacing throughout
 * the application.
 *
 * @param size Spacing size from the design token system
 * @param horizontal If true, creates horizontal spacing. If false, creates vertical spacing
 * @param modifier Additional modifier to apply
 */
@Composable
fun AppSpacer(
    size: Dp = SpacingTokens.Base,
    horizontal: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (horizontal) {
        Spacer(modifier = modifier.width(size))
    } else {
        Spacer(modifier = modifier.height(size))
    }
}

/**
 * Convenience composables for specific spacing sizes
 */

/** Extra small vertical spacer (4dp) */
@Composable
fun TinyVerticalSpacer(modifier: Modifier = Modifier) {
    AppSpacer(size = SpacingTokens.Tiny, horizontal = false, modifier = modifier)
}

/** Small vertical spacer (8dp) */
@Composable
fun SmallVerticalSpacer(modifier: Modifier = Modifier) {
    AppSpacer(size = SpacingTokens.ExtraSmall, horizontal = false, modifier = modifier)
}

/** Medium vertical spacer (16dp) */
@Composable
fun MediumVerticalSpacer(modifier: Modifier = Modifier) {
    AppSpacer(size = SpacingTokens.Medium, horizontal = false, modifier = modifier)
}

/** Large vertical spacer (24dp) */
@Composable
fun LargeVerticalSpacer(modifier: Modifier = Modifier) {
    AppSpacer(size = SpacingTokens.Large, horizontal = false, modifier = modifier)
}

/** Extra large vertical spacer (32dp) */
@Composable
fun ExtraLargeVerticalSpacer(modifier: Modifier = Modifier) {
    AppSpacer(size = SpacingTokens.ExtraLarge, horizontal = false, modifier = modifier)
}

/** Horizontal spacer (8dp) */
@Composable
fun HorizontalSpacer(modifier: Modifier = Modifier) {
    AppSpacer(size = SpacingTokens.ExtraSmall, horizontal = true, modifier = modifier)
}