package com.greenrobotdev.lordofthelinks.design.motion

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * The app's animation timings, all from the theme's motion scheme (springs, so animations stay
 * smooth when interrupted). Screens use these instead of hand-written tween/spring specs.
 * Spatial = position, size, scale, shape. Effects = color, alpha.
 * The motion scheme is still experimental in Material 3; the opt-in stays here, not in screens.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object AppMotion {
    @Composable fun <T> spatial(): FiniteAnimationSpec<T> = MaterialTheme.motionScheme.defaultSpatialSpec()
    @Composable fun <T> spatialFast(): FiniteAnimationSpec<T> = MaterialTheme.motionScheme.fastSpatialSpec()
    @Composable fun <T> spatialSlow(): FiniteAnimationSpec<T> = MaterialTheme.motionScheme.slowSpatialSpec()
    @Composable fun <T> effects(): FiniteAnimationSpec<T> = MaterialTheme.motionScheme.defaultEffectsSpec()
    @Composable fun <T> effectsFast(): FiniteAnimationSpec<T> = MaterialTheme.motionScheme.fastEffectsSpec()
}

/** true when the user turned animations off; animations then jump straight to their end state. */
@Composable
expect fun rememberReducedMotion(): Boolean
