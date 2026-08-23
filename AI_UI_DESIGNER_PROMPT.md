ins# AI UI Designer Prompt for Link Library App

## Context & Requirements

Generate detailed UI designs for a Kotlin Multiplatform Mobile (KMP) link management app called **"Link Library"**. The app helps users save, organize, and manage web links with tags and collections.

## Design System & Guidelines

### Brand Colors (Google Material 3 Based)
```css
Primary Colors:
- Primary Blue: #4285F4 (main actions, active states)
- Primary Light: #82B1FF (hover states)
- Primary Dark: #0D47A1 (pressed states)

Secondary Colors:
- Teal Green: #34A853 (success states)
- Teal Light: #5CD65C (success backgrounds)

Accent Colors:
- Orange: #FBBC05 (warnings, highlights)
- Red: #EA4335 (errors, destructive actions)
- Purple: #9C27B0 (AI features, special states)

Neutral Colors:
- Background Light: #F8F9FA
- Surface Light: #FFFFFF
- Text Primary: #202124
- Text Secondary: #5F6368
- Text Tertiary: #9AA0A6
- Border: #E8EAED

Dark Mode Colors:
- Background Dark: #121212
- Surface Dark: #1E1E1E
- Text Primary Dark: #E8EAED
- Text Secondary Dark: #9AA0A6
```

### Typography Scale
```css
Display: Large (57sp), Medium (45sp), Small (36sp)
Headline: Large (32sp), Medium (28sp), Small (24sp)
Title: Large (22sp), Medium (16sp), Small (14sp)
Body: Large (16sp), Medium (14sp), Small (12sp)
Label: Large (14sp), Medium (12sp), Small (11sp)

Font Weights: Regular (400), Medium (500), SemiBold (600), Bold (700)
```

### Spacing & Layout
```css
Spacing Scale: 4dp, 8dp, 12dp, 16dp, 24dp, 32dp, 48dp, 64dp
Border Radius: Small (8dp), Medium (12dp), Large (16dp), XLarge (24dp)
Elevation: Level 1 (1dp), Level 2 (2dp), Level 3 (4dp), Level 4 (8dp)
```

## Screen-Specific Design Requirements

### 1. Link Details Screen (Priority)
**Purpose**: Display comprehensive information about a saved link with actions

**Key Elements Needed**:
- Hero section with link favicon/preview
- Title and description display
- Metadata display (date added, source, tags)
- Action buttons (share, edit, delete, open in browser)
- Notes section for user annotations
- Related links suggestions
- Tags management within detail view

**Layout Preferences**:
- Collapsible scrolling content
- Sticky action buttons at bottom
- Expandable notes section
- Swipe gestures for quick actions

### 2. Add/Edit Link Screen
**Purpose**: Form for adding new links or editing existing ones

**Key Elements**:
- URL input with auto-fetch metadata
- Title and description fields
- Tag selection interface
- Collection assignment
- Notes area
- Form validation feedback
- Save/cancel actions

### 3. Library/Collection Screen
**Purpose**: Browse and manage saved links

**Key Elements**:
- Search/filter functionality
- Grid vs list view toggle
- Sort options
- Multi-select for bulk operations
- Quick actions per item
- Empty state design

### 4. Tags Management Screen
**Purpose**: Create and manage tags

**Key Elements**:
- Tag grid with usage counts
- Color/icon assignment
- Bulk tag operations
- Tag merging/splitting
- Filter by tags

## Design Output Requirements

### For Each Screen, Provide:

1. **Wireframe Description**: Detailed layout structure with component hierarchy
2. **Component Specifications**: Size, spacing, colors, typography for each element
3. **Interaction Design**: Gesture support, animations, state changes
4. **Responsive Behavior**: Different screen sizes, orientations
5. **Accessibility**: Touch targets, contrast ratios, screen reader support
6. **States**: Loading, empty, error, success, content states
7. **Micro-interactions**: Button press feedback, list item animations, transitions

### Design Format Structure:

```yaml
screen_name: "LinkDetailsScreen"
layout_type: "scrollable_content"

components:
  - name: "AppBar"
    type: "TopAppBar"
    properties:
      title: "Link Details"
      actions: ["share", "edit", "delete"]
      elevation: 2
      colors: primary

  - name: "HeroSection"
    type: "Card"
    properties:
      elevation: 4
      corner_radius: 16
      padding: 24
      background_color: surface
      contents:
        - name: "FaviconPreview"
          type: "Image"
          size: 64x64
          corner_radius: 12
        - name: "LinkTitle"
          type: "Text"
          style: "headline_medium"
          color: "text_primary"
        - name: "LinkUrl"
          type: "Text"
          style: "body_medium"
          color: "text_secondary"
        - name: "LastVisited"
          type: "Text"
          style: "body_small"
          color: "text_tertiary"

  - name: "ActionButtons"
    type: "ButtonGroup"
    properties:
      layout: "horizontal"
      spacing: 12
      buttons:
        - name: "OpenInBrowser"
          type: "FilledButton"
          text: "Open"
          icon: "open_in_new"
          color: "primary"
        - name: "Share"
          type: "OutlinedButton"
          text: "Share"
          icon: "share"
          color: "secondary"
        - name: "Edit"
          type: "TextButton"
          text: "Edit"
          icon: "edit"

states:
  - name: "loading"
    description: "Circular progress indicator centered"
  - name: "content"
    description: "Full content displayed with animations"
  - name: "empty"
    description: "Empty state with illustration and message"
  - name: "error"
    description: "Error message with retry option"

interactions:
  - name: "swipe_to_delete"
    type: "swipe_gesture"
    direction: "right"
    action: "show_delete_confirmation"
  - name: "pull_to_refresh"
    type: "pull_gesture"
    action: "refresh_link_data"

animations:
  - name: "content_fade_in"
    duration: 300
    easing: "ease_out"
  - name: "button_press"
    duration: 100
    scale: 0.95
```

## Material 3 Component Preferences

- **Cards**: Use with elevation 2-4dp, rounded corners 12-16dp
- **Buttons**: Filled for primary, outlined for secondary, text for tertiary
- **Text Fields**: Outlined with labels, helper text, error states
- **Navigation**: Bottom navigation for main tabs, top app bars for screens
- **Lists**: LazyColumn/LazyRow with proper item spacing and dividers
- **Feedback**: Snackbars for transient messages, dialogs for confirmations
- **Loading**: Circular progress indeterminate for content loading

## Accessibility Requirements

- Minimum touch target: 48x48dp
- Contrast ratio: 4.5:1 for normal text, 3:1 for large text
- Semantic descriptions for all icons and images
- Keyboard navigation support
- Screen reader announcements for state changes

## Responsive Design Considerations

- **Phone**: 360dp-600dp width, single column layout
- **Tablet**: 600dp-840dp width, two-column layout where appropriate
- **Desktop**: 840dp+ width, multi-column with sidebar navigation
- **Orientation**: Support portrait and landscape with adaptive layouts

---

**Please generate detailed UI designs following this structure, focusing on the Link Details Screen first, then the other screens in order of priority.**