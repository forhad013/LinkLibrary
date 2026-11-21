package com.greenrobotdev.lordofthelinks.bookmarks.screens.add.sheet

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

internal actual fun Modifier.imeNestedScroll(): Modifier = this

@Composable
internal actual fun imeAnimationTargetInsets(): WindowInsets = WindowInsets(0)
