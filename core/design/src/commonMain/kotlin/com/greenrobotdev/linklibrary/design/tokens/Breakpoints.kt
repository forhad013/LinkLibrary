package com.greenrobotdev.linklibrary.design.tokens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

// Window size class enum for responsive breakpoints
enum class WindowSizeClass { Compact, Medium, Expanded }

/**
 * Material Design 3 Breakpoint Tokens
 * Responsive design breakpoints for adaptive layouts
 */
object BreakpointsTokens {
    /** Compact mode - Mobile devices (< 600dp) */
    val Compact = 0.dp

    /** Medium mode - Tablets/foldables (600-840dp) */
    val Medium = 600.dp

    /** Expanded mode - Desktop (> 840dp) */
    val Expanded = 840.dp
}

// Utility functions for responsive design

/**
 * Remember the window width size class based on current configuration
 * @return WindowSizeClass (Compact, Medium, or Expanded)
 */
@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    return remember(configuration.screenWidthDp) {
        when {
            configuration.screenWidthDp < BreakpointsTokens.Medium.value -> WindowSizeClass.Compact
            configuration.screenWidthDp < BreakpointsTokens.Expanded.value -> WindowSizeClass.Medium
            else -> WindowSizeClass.Expanded
        }
    }
}

/**
 * Check if current window is in compact mode (mobile)
 */
@Composable
fun isCompactWindow(): Boolean {
    return rememberWindowSizeClass() == WindowSizeClass.Compact
}

/**
 * Check if current window is in medium mode (tablet/foldable)
 */
@Composable
fun isMediumWindow(): Boolean {
    return rememberWindowSizeClass() == WindowSizeClass.Medium
}

/**
 * Check if current window is in expanded mode (desktop)
 */
@Composable
fun isExpandedWindow(): Boolean {
    return rememberWindowSizeClass() == WindowSizeClass.Expanded
}