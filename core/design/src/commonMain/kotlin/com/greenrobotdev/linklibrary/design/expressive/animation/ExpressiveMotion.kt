package com.greenrobotdev.linklibrary.design.expressive.animation

import androidx.compose.animation.AnimationSpec
import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FloatAnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalAccessibilityManager
import kotlin.math.max

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
 * Material 3 Expressive easing curves for transitions
 * These are used for enter/exit/shared-axis transitions, not for component state changes
 */
object EasingCurves {
    /** Emphasized easing: cubic-bezier(0.2, 0, 0, 1) */
    val Emphasized = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

    /** Emphasized decelerate: cubic-bezier(0.05, 0.7, 0.1, 1) */
    val EmphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)

    /** Emphasized accelerate: cubic-bezier(0.3, 0, 0.8, 0.15) */
    val EmphasizedAccelerate = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)

    /** Standard easing: cubic-bezier(0.2, 0, 0, 1) */
    val Standard = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

    /** Standard decelerate: cubic-bezier(0, 0, 0, 1) */
    val StandardDecelerate = CubicBezierEasing(0.0f, 0.0f, 0.0f, 1.0f)

    /** Standard accelerate: cubic-bezier(0.3, 0, 1, 1) */
    val StandardAccelerate = CubicBezierEasing(0.3f, 0.0f, 1.0f, 1.0f)
}

/**
 * Duration tokens for different motion scenarios (in milliseconds)
 * Based on Material 3 motion duration system
 */
object DurationTokens {
    /** Emphasized duration for begin and end on screen */
    const val Emphasized = 500

    /** Emphasized decelerate duration for enter screen */
    const val EmphasizedDecelerate = 400

    /** Emphasized accelerate duration for exit screen */
    const val EmphasizedAccelerate = 200

    /** Standard duration for utility animations */
    const val Standard = 300

    /** Standard decelerate duration for utility enter animations */
    const val StandardDecelerate = 250

    /** Standard accelerate duration for utility exit animations */
    const val StandardAccelerate = 200

    /** Short duration for micro-interactions */
    const val Short = 150

    /** Medium duration for standard component interactions */
    const val Medium = 200

    /** Long duration for complex transitions */
    const val Long = 400
}

/**
 * Spring physics configuration for component state changes
 * Material 3 Expressive uses spring-based motion for natural, fluid interactions
 */
object SpringPhysics {
    /** Default spring for component interactions - balanced and natural */
    fun defaultSpring() = spring(
        dampingRatio = 0.8f,  // Slightly underdamped for natural feel
        stiffness = 400f      // Medium stiffness for responsive but smooth motion
    )

    /** Bouncy spring for playful interactions (FAB, success states) */
    fun bouncySpring() = spring(
        dampingRatio = 0.6f,  // More underdamped for bounce
        stiffness = 350f      // Lower stiffness for more bounce
    )

    /** Stiff spring for precise, snappy interactions (switches, sliders) */
    fun stiffSpring() = spring(
        dampingRatio = 0.9f,  // Near critical damping
        stiffness = 600f      // Higher stiffness for quick response
    )

    /** Gentle spring for subtle animations (fade, elevation changes) */
    fun gentleSpring() = spring(
        dampingRatio = 0.85f,
        stiffness = 300f      // Lower stiffness for gentle motion
    )
}

/**
 * Animation specs following Material 3 guidelines
 * Combines easing curves with durations for transition animations
 */
object AnimationSpecs {
    /** Emphasized enter animation */
    fun emphasizedEnter(): FloatAnimationSpec = tween(
        durationMillis = DurationTokens.EmphasizedDecelerate,
        easing = EasingCurves.EmphasizedDecelerate
    )

    /** Emphasized exit animation */
    fun emphasizedExit(): FloatAnimationSpec = tween(
        durationMillis = DurationTokens.EmphasizedAccelerate,
        easing = EasingCurves.EmphasizedAccelerate
    )

    /** Standard utility animation */
    fun standard(): FloatAnimationSpec = tween(
        durationMillis = DurationTokens.Standard,
        easing = EasingCurves.Standard
    )

    /** Short micro-interaction animation */
    fun short(): FloatAnimationSpec = tween(
        durationMillis = DurationTokens.Short,
        easing = EasingCurves.Standard
    )

    /** Medium component interaction animation */
    fun medium(): FloatAnimationSpec = tween(
        durationMillis = DurationTokens.Medium,
        easing = EasingCurves.Emphasized
    )
}

/**
 * Remember animation spec with accessibility support
 * Reduces motion duration when user prefers reduced animation
 */
@Composable
fun rememberAccessibleAnimationSpec(
    defaultSpec: FloatAnimationSpec
): FloatAnimationSpec {
    val accessibilityManager = LocalAccessibilityManager.current

    return remember(accessibilityManager) {
        // Use instant or very fast animation when motion reduction is enabled
        if (accessibilityManager != null) {
            tween(durationMillis = 50, easing = EasingCurves.StandardDecelerate)
        } else {
            defaultSpec
        }
    }
}

/**
 * Material 3 Expressive motion configuration
 * Provides convenient access to spring physics and animation specs
 */
object ExpressiveMotion {
    // Spring specs for component state changes
    fun defaultSpring() = SpringPhysics.defaultSpring()
    fun bouncySpring() = SpringPhysics.bouncySpring()
    fun stiffSpring() = SpringPhysics.stiffSpring()
    fun gentleSpring() = SpringPhysics.gentleSpring()

    // Pre-configured animation specs
    fun enter() = AnimationSpecs.emphasizedEnter()
    fun exit() = AnimationSpecs.emphasizedExit()
    fun standard() = AnimationSpecs.standard()
    fun short() = AnimationSpecs.short()
    fun medium() = AnimationSpecs.medium()
}