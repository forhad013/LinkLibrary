package com.greenrobotdev.linklibrary.design.tokens

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween

/**
 * Material Design 3 Motion Tokens
 * Animation timings and easing curves for consistent motion design
 */
object MotionTokens {
    // Duration tokens (in milliseconds)

    /** Very short duration for subtle micro-interactions */
    const val DurationShort = 150

    /** Standard duration for common interactions */
    const val DurationMedium = 250

    /** Long duration for major transitions */
    const val DurationLong = 350

    /** Extra long duration for complex, deliberate transitions */
    const val DurationExtraLong = 500

    // Easing curves
    /** Standard easing for most animations */
    val StandardEasing = FastOutSlowInEasing

    // Animation specs

    /**
     * Standard animation spec for most UI transitions
     * Uses medium duration with standard easing
     */
    fun standardAnimationSpec(): AnimationSpec<Float> = tween(
        durationMillis = DurationMedium,
        easing = StandardEasing
    )

    /**
     * Quick animation spec for micro-interactions
     * Uses short duration with standard easing
     */
    fun quickAnimationSpec(): AnimationSpec<Float> = tween(
        durationMillis = DurationShort,
        easing = StandardEasing
    )

    /**
     * Deliberate animation spec for major transitions
     * Uses long duration with standard easing
     */
    fun deliberateAnimationSpec(): AnimationSpec<Float> = tween(
        durationMillis = DurationLong,
        easing = StandardEasing
    )
}