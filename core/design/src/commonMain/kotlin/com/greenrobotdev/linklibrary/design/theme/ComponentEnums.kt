package com.greenrobotdev.linklibrary.design.theme

/**
 * Component style variants following Material 3 guidelines
 *
 * These enums define the standard visual variants available across components
 * in the design system, ensuring consistency and Material 3 compliance.
 */
enum class ComponentStyle {
    /** Standard appearance for the component */
    Default,

    /** Filled container with solid background color (Primary emphasis) */
    Filled,

    /** Outlined border with transparent/surface background */
    Outlined,

    /** Elevated surface with shadow and tonal elevation */
    Elevated,

    /** Tonal surface variant with lower emphasis than filled */
    Tonal
}

/**
 * Component size variants following Material 3 guidelines
 *
 * These enums define the standard size variants available across components,
 * ensuring consistent spacing and proportions across the design system.
 *
 * Size specifications typically follow Material 3 touch target guidelines:
 * - Small: 32dp height for compact layouts
 * - Medium: 40dp height for standard usage (default)
 * - Large: 48dp height for prominent, touch-friendly targets
 */
enum class ComponentSize {
    /** Compact size for dense layouts (typically 32dp height) */
    Small,

    /** Default size for standard usage (typically 40dp height) */
    Medium,

    /** Prominent size with larger touch targets (typically 48dp height) */
    Large
}

/**
 * Component state for interactive elements
 *
 * Defines the possible interaction states for components, used for
 * consistent state management and visual feedback across the design system.
 */
enum class ComponentState {
    /** Default resting state */
    Idle,

    /** Component is currently being interacted with (hovered, pressed, etc.) */
    Active,

    /** Component is focused via keyboard or other input method */
    Focused,

    /** Component is disabled and not interactive */
    Disabled,

    /** Component is in an error state */
    Error
}