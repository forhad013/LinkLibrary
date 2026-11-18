package com.greenrobotdev.linklibrary.design.motion

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf

/**
 * The [SharedTransitionScope] around a navigation host, for shared-element transitions between its
 * screens. null outside one: shared-element modifiers then do nothing.
 */
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
