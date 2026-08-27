---
name: Lords of Links
colors:
  surface: '#FEFBFF'
  surface-dim: '#dcd9da'
  surface-bright: '#fcf8f9'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f6f3f4'
  surface-container: '#F0EDED'
  surface-container-high: '#eae7e8'
  surface-container-highest: '#e5e2e3'
  on-surface: '#1b1b1c'
  on-surface-variant: '#484554'
  inverse-surface: '#303031'
  inverse-on-surface: '#f3f0f1'
  outline: '#797586'
  outline-variant: '#cac4d7'
  surface-tint: '#6341d5'
  primary: '#4c22bd'
  on-primary: '#ffffff'
  primary-container: '#EADDFF'
  on-primary-container: '#ded4ff'
  inverse-primary: '#cbbeff'
  secondary: '#6349bf'
  on-secondary: '#ffffff'
  secondary-container: '#9f86ff'
  on-secondary-container: '#340d90'
  tertiary: '#4a464b'
  on-tertiary: '#ffffff'
  tertiary-container: '#F8F1F6'
  on-tertiary-container: '#dfd8de'
  error: '#BA1A1A'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#e7deff'
  primary-fixed-dim: '#cbbeff'
  on-primary-fixed: '#1e0061'
  on-primary-fixed-variant: '#4b21bd'
  secondary-fixed: '#e7deff'
  secondary-fixed-dim: '#ccbeff'
  on-secondary-fixed: '#1e0060'
  on-secondary-fixed-variant: '#4b2ea6'
  tertiary-fixed: '#e8e0e6'
  tertiary-fixed-dim: '#cbc4ca'
  on-tertiary-fixed: '#1d1b1f'
  on-tertiary-fixed-variant: '#49454a'
  background: '#fcf8f9'
  on-background: '#1b1b1c'
  surface-variant: '#e5e2e3'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 57px
    fontWeight: '700'
    lineHeight: 64px
    letterSpacing: -0.25px
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
  headline-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 28px
    fontWeight: '700'
    lineHeight: 36px
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
  title-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
  body-lg:
    fontFamily: Be Vietnam Pro
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
    letterSpacing: 0.5px
  body-md:
    fontFamily: Be Vietnam Pro
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
    letterSpacing: 0.25px
  label-lg:
    fontFamily: Be Vietnam Pro
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.1px
  label-md:
    fontFamily: Be Vietnam Pro
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.5px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 8px
  gutter: 16px
  margin-mobile: 16px
  margin-desktop: 24px
  container-padding: 24px
  section-gap: 48px
---

## Brand & Style

The design system for this product is an energetic evolution of professional utility, blending the structured reliability of administrative tools with a spirited, personal touch. It targets creators and power users who value both efficiency and self-expression.

The visual style is **Material 3 Expressive**. It moves beyond standard utilitarianism by embracing:
- **Dynamic Vitality:** Using high-saturation accents to guide attention and spark joy.
- **Expressive Geometry:** Utilizing exaggerated roundedness and playful shapes to make the interface feel approachable and "bouncy."
- **Narrative Motion:** Prioritizing fluid transitions and clear hierarchy that feels alive and responsive to user input.
- **Modern Professionalism:** Maintaining a clean, organized structure that ensures the "spirited" aesthetic never compromises the "utility" of the application.

## Colors

The palette is anchored by a deep, vibrant violet that serves as the primary driver for action and identity. 

- **Primary & Secondary:** High-saturation purples drive primary actions, active states, and brand-heavy components.
- **Surface Strategy:** Surfaces use a crisp, near-white base contrasted by a strong, dark-gray text color to ensure high legibility and a modern "ink-on-paper" feel.
- **Vibrancy:** Apply colors with a "tinted" approach; even neutral surfaces should lean slightly toward the primary or tertiary hues to maintain a cohesive, spirited atmosphere. Use the `tertiary-container` (#F8F1F6) for warm, energetic backgrounds as an alternative to standard grays.

## Typography

This design system uses a dual-font strategy to balance character with readability.

- **Headlines (Plus Jakarta Sans):** Chosen for its soft, rounded terminals and optimistic personality. Used for all Display, Headline, and Title roles. Heavier weights (Bold/SemiBold) are utilized to create the "Expressive" impact.
- **Body & Labels (Be Vietnam Pro):** A contemporary typeface that maintains excellent legibility at smaller sizes. Its slightly wider stance complements the roundedness of the UI elements.
- **Scaling:** Headlines aggressively scale down for mobile (see `headline-lg-mobile`) to maintain visual balance within highly rounded containers.

## Layout & Spacing

The layout follows a fluid-to-fixed transition model using an 8px base grid.

- **Grid:** A 12-column grid for desktop (max-width 1440px) and a 4-column grid for mobile.
- **Spirited Padding:** Generous internal padding in cards and containers (24px) reinforces the feeling of "breathable" space.
- **Dynamic Hierarchy:** Varying vertical spacing (48px between sections, 16px between related items) creates a clear rhythmic flow through the content.
- **Responsive Behavior:** On mobile, margins reduce to 16px, and components like buttons and inputs stretch to fill the available width, emphasizing touch-friendly targets.

## Elevation & Depth

Elevation is primarily conveyed through **Tonal Layers** and **Ambient Shadows** to create a soft, approachable depth.

- **Surface Tiers:** The warm Tertiary container color is the background for the base layer, while floating cards and primary containers sit on the crisp white Surface.
- **Shadows:** Shadows are soft and diffused, tinted with the primary hue (violet) at approximately 12% opacity with a 16px blur. This prevents a "muddy" look and makes elements feel like they are floating over a light source.
- **Interactivity:** On hover or active states, a subtle "lift" effect is applied by increasing shadow spread and slightly decreasing the Y-offset, making elements feel tactile and responsive.

## Shapes

The shape language is a core pillar of the "Expressive" style. 

- **Containers & Cards:** Use the `rounded-lg` (16px) token for all standard containers to create a soft structure.
- **Buttons & Chips:** Utilize **Pill (fully rounded)** shapes for primary actions and interactive chips to create high-contrast visual distinction.
- **Inputs:** Use a medium 12px radius for input fields to bridge the gap between the soft cards and the pill-shaped buttons.
- **Consistency:** Nested elements must never have a larger corner radius than their parent container.

## Components

- **Buttons:** Primary buttons are pill-shaped, using the Primary color with white text. They include a subtle scale-down effect (0.98) on click to feel "squishy" and tactile.
- **Cards:** No borders; cards rely on Surface-on-Tertiary contrast and soft ambient shadows. Corner radius is 16px.
- **Input Fields:** Outlined style with a 2px stroke on focus using the Secondary color. Corner radius is fixed at 12px.
- **Chips:** Always pill-shaped. Selected states use the Primary-Container (#EADDFF) color.
- **Lists:** Items feature generous 16px vertical padding and use 8px rounded selection indicators that do not touch the container edges.
- **Floating Action Button (FAB):** A signature anchor. Use a large, 24px rounded square shape (rounded-xl) with the Primary color to prioritize the most important action.