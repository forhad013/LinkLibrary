package com.greenrobotdev.linklibrary.design.extensions

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.greenrobotdev.linklibrary.design.tokens.SpacingTokens

/**
 * Extension functions for Modifier to provide common design system patterns
 */

/**
 * Apply padding using design token spacing system
 */
fun Modifier.appPadding(size: Dp = SpacingTokens.Base): Modifier =
    this.then(padding(size))

/**
 * Apply horizontal padding using design token spacing system
 */
fun Modifier.appPaddingHorizontal(size: Dp = SpacingTokens.Medium): Modifier =
    this.then(padding(horizontal = size))

/**
 * Apply vertical padding using design token spacing system
 */
fun Modifier.appPaddingVertical(size: Dp = SpacingTokens.Medium): Modifier =
    this.then(padding(vertical = size))

/**
 * Apply small padding (8dp) - commonly used for compact spacing
 */
fun Modifier.appPaddingSmall(): Modifier =
    this.then(padding(SpacingTokens.ExtraSmall))

/**
 * Apply medium padding (16dp) - commonly used for standard spacing
 */
fun Modifier.appPaddingMedium(): Modifier =
    this.then(padding(SpacingTokens.Medium))

/**
 * Apply large padding (24dp) - commonly used for loose spacing
 */
fun Modifier.appPaddingLarge(): Modifier =
    this.then(padding(SpacingTokens.Large))