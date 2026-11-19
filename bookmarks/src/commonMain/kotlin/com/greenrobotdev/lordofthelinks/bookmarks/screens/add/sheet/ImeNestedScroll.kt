package com.greenrobotdev.lordofthelinks.bookmarks.screens.add.sheet

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Lets a drag on scrollable content pull the on-screen keyboard down and up with the finger.
 * Android 11+ only; a no-op everywhere else. Must sit before the scroll modifier.
 */
internal expect fun Modifier.imeNestedScroll(): Modifier

/**
 * Where the on-screen keyboard will be once its current animation ends (its full height while
 * opening). Android only; empty insets everywhere else.
 */
@Composable
internal expect fun imeAnimationTargetInsets(): WindowInsets
