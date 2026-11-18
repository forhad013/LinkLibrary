package com.greenrobotdev.linklibrary.design.expressive.animation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring

/**
 * Material Design 3 Expressive Motion System
 *
 * Implements spring-based motion physics and easing curves following Material 3 guidelines.
 * Spring animations are used for component interactions, while easing/duration is used for
 * transitions (enter/exit/shared-axis).
 *
 * Based on Material 3 Expressive (May 2025) and Google I/O 2026 updates.
 */

/**
 * Spring physics configuration for component state changes
 * Material 3 Expressive uses spring-based motion for natural, fluid interactions
 */
object SpringPhysics {
    /** Default spring for component interactions - balanced and natural */
    fun defaultSpring(): SpringSpec<Float> = spring(
        dampingRatio = 0.8f,  // Slightly underdamped for natural feel
        stiffness = 400f      // Medium stiffness for responsive but smooth motion
    )

    /** Bouncy spring for playful interactions (FAB, success states) */
    fun bouncySpring(): SpringSpec<Float> = spring(
        dampingRatio = 0.6f,  // More underdamped for bounce
        stiffness = 350f      // Lower stiffness for more bounce
    )

    /** Stiff spring for precise, snappy interactions (switches, sliders) */
    fun stiffSpring(): SpringSpec<Float> = spring(
        dampingRatio = 0.9f,  // Near critical damping
        stiffness = 600f      // Higher stiffness for quick response
    )

    /** Gentle spring for subtle animations (fade, elevation changes) */
    fun gentleSpring(): SpringSpec<Float> = spring(
        dampingRatio = 0.85f,
        stiffness = 300f      // Lower stiffness for gentle motion
    )
}
