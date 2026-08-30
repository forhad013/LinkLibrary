---
name: Vibrant Citadel Dark
colors:
  surface: '#14121b'
  surface-dim: '#14121b'
  surface-bright: '#3b3842'
  surface-container-lowest: '#0f0d16'
  surface-container-low: '#1c1a24'
  surface-container: '#16141F'
  surface-container-high: '#1E1B29'
  surface-container-highest: '#282536'
  on-surface: '#e6e0ee'
  on-surface-variant: '#cac4d7'
  inverse-surface: '#e6e0ee'
  inverse-on-surface: '#322f39'
  outline: '#938ea0'
  outline-variant: '#484554'
  surface-tint: '#cbbeff'
  primary: '#cbbeff'
  on-primary: '#340098'
  primary-container: '#6442d6'
  on-primary-container: '#ded4ff'
  inverse-primary: '#6341d5'
  secondary: '#ccbdff'
  on-secondary: '#35108b'
  secondary-container: '#4e33a4'
  on-secondary-container: '#bfadff'
  tertiary: '#cbc3d8'
  on-tertiary: '#322e3e'
  tertiary-container: '#625c6e'
  on-tertiary-container: '#ded6eb'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#e7deff'
  primary-fixed-dim: '#cbbeff'
  on-primary-fixed: '#1e0061'
  on-primary-fixed-variant: '#4b21bd'
  secondary-fixed: '#e7deff'
  secondary-fixed-dim: '#ccbdff'
  on-secondary-fixed: '#1f0060'
  on-secondary-fixed-variant: '#4c30a2'
  tertiary-fixed: '#e7dff4'
  tertiary-fixed-dim: '#cbc3d8'
  on-tertiary-fixed: '#1d1928'
  on-tertiary-fixed-variant: '#494455'
  background: '#14121b'
  on-background: '#e6e0ee'
  surface-variant: '#36333e'
  on-surface-muted: '#A19EAC'
  error-red: '#FFB4AB'
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

The design system is an energetic, high-fidelity evolution of professional utility, optimized for low-light environments. It targets creators and power users who demand a sophisticated workspace that doesn't sacrifice personality.

The visual style is **Material 3 Expressive (Dark Mode)**. It leverages the depth of a deep charcoal base to make high-saturation accents feel luminous and immersive. The brand identity is defined by:
- **Atmospheric Vitality:** Utilizing a vibrant violet primary color that appears to "glow" against the dark canvas.
- **Expressive Geometry:** Maintaining highly rounded corners and pill-shaped interactive elements to keep the interface approachable and friendly despite the serious dark palette.
- **Tactile Depth:** Using tonal stacking and tinted shadows to create a clear sense of layering and hierarchy without relying on harsh borders.

## Colors

The palette is anchored by a deep charcoal-navy base (#0D0B14), providing a high-contrast foundation for the vibrant violet brand color.

- **Primary & Secondary:** The primary violet (#6442D6) is calibrated for dark mode accessibility. The secondary color is a lighter, desaturated lavender to provide accessible contrast for secondary actions and subtle accents.
- **Surface Strategy:** This system avoids pure black to prevent "smearing" on OLED screens and to allow for depth. It uses a **Tonal Stacking** model: the further an element "rises" from the background, the lighter its tonal value becomes, using subtle infusions of the primary hue.
- **Text & Contrast:** Primary text is off-white to reduce eye strain, while secondary/variant text uses a muted violet-gray (#A19EAC) to maintain hierarchy.

## Typography

The typography maintains a dual-font strategy that balances personality with high-readability in low-light conditions.

- **Headlines (Plus Jakarta Sans):** Used for all display and title roles. The bold weights ensure that titles "pop" against the dark background. The rounded nature of the font complements the UI's geometry.
- **Body & Labels (Be Vietnam Pro):** This font was chosen for its excellent legibility. In dark mode, slightly increased letter spacing (0.5px) is applied to body text to prevent "haloing" and ensure characters remain distinct.
- **Responsive Scaling:** Headline sizes are reduced by approximately 15% on mobile devices to prevent excessive line-wrapping in tight containers.

## Layout & Spacing

The layout utilizes a fluid-grid system that prioritizes generous internal "breathing room" to offset the visual weight of the dark theme.

- **Grid Model:** A 12-column fluid grid for desktop and a 4-column grid for mobile. 
- **Spacing Rhythm:** An 8px base unit governs all dimensions. The system uses "Spirited Padding" (24px) inside cards to make the content feel less dense and more premium.
- **Adaptive Reflow:** On mobile, margins tighten to 16px. Interactive elements like buttons transition to full-width to provide larger touch targets.

## Elevation & Depth

In this dark-themed design system, elevation is conveyed through **Atmospheric Layering** rather than traditional physical shadows.

- **Surface Tiers:** Depth is created by increasing the brightness of the surface. The background is the darkest (#0D0B14), while floating cards use a slightly lighter "Surface Container" (#16141F). Top-level overlays like modals use the "Surface High" tier (#1E1B29).
- **Subtle Tinted Shadows:** Shadows are used sparingly. They are extra-diffused (24px+ blur) and tinted with the primary violet at 20% opacity. This creates a "glow" effect rather than a dark shadow, making elements feel like they are floating over a light source.
- **Glow Accents:** High-elevation components (like FABs) may feature a subtle 1px inner-stroke of the primary color to define their edges against the dark background.

## Shapes

The shape language is defined by "Expressive Geometry," using high-radius corners to soften the technical feel of the dark theme.

- **Core Containers:** All cards and main content areas use a 16px (`rounded-lg`) radius.
- **Interactive Elements:** Buttons, chips, and tags utilize **Pill** shapes (fully rounded) to maximize visual distinction and invite interaction.
- **Nested Logic:** Maintain a 4px difference between outer and inner container radii to ensure geometric harmony.

## Components

- **Buttons:** Primary buttons are pill-shaped, using the primary violet (#6442D6) with white text. They include a "squishy" active state (0.98 scale down).
- **Cards:** Rely on tonal contrast rather than borders. Use the `surface-container` color against the background. Corner radius is 16px.
- **Input Fields:** Outlined style using the `secondary-color` for the 2px focus stroke. Backgrounds should be slightly darker than their parent card to create an "inset" feel.
- **Chips:** Pill-shaped. Unselected chips use a subtle `surface-container-highest` fill; selected chips use the primary violet with a subtle outer glow.
- **Floating Action Button (FAB):** A signature element. Use a 24px rounded square (rounded-xl) with a vibrant primary fill and a soft, violet-tinted ambient shadow.
- **Lists:** Items feature 16px vertical padding. Selection states use a "ghost" fill (primary color at 12% opacity) with a 4px vertical pill indicator on the left edge.