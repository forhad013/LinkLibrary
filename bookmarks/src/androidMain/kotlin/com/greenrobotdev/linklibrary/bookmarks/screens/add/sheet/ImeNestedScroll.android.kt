package com.greenrobotdev.linklibrary.bookmarks.screens.add.sheet

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.imeNestedScroll as androidImeNestedScroll
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imeAnimationTarget
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalLayoutApi::class)
internal actual fun Modifier.imeNestedScroll(): Modifier = androidImeNestedScroll()

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal actual fun imeAnimationTargetInsets(): WindowInsets = WindowInsets.imeAnimationTarget
