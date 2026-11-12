# Design System Standard API Patterns

## Component Signature Standard

All design system components MUST follow this standard signature pattern:

```kotlin
@Composable
fun ComponentName(
    // ===== REQUIRED PARAMETERS =====
    // Core functionality parameters - no defaults
    requiredValue: String,
    onRequiredAction: (String) -> Unit,

    // ===== OPTIONAL PARAMETERS (Standard Order) =====
    // 1. Modifier (always first optional param)
    modifier: Modifier = Modifier,

    // 2. State & Behavior
    enabled: Boolean = true,
    visible: Boolean = true,
    readOnly: Boolean = false,

    // 3. Styling & Appearance
    style: ComponentStyle = ComponentStyle.Default,
    size: ComponentSize = ComponentSize.Medium,
    colors: ComponentColors? = null, // Null = use theme defaults

    // 4. Content Slots (Slot API Pattern)
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    beforeContent: (@Composable () -> Unit)? = null,
    afterContent: (@Composable () -> Unit)? = null,

    // 5. Labels & Text
    label: String? = null,
    placeholder: String? = null,
    description: String? = null,
    errorMessage: String? = null,

    // 6. Advanced Options
    interactionSource: MutableInteractionSource? = null,
    semantics: SemanticsPropertyReceiver.() -> Unit = {}
)
```

## Parameter Naming Conventions

### Consistent Parameter Names Across Components:
- **Values**: `value`, `selectedValue`, `text`, `content`
- **Callbacks**: `onClick`, `onValueChange`, `onSelectedChange`, `onDismiss`
- **State**: `enabled`, `visible`, `readOnly`, `loading`, `error`
- **Styling**: `style`, `size`, `colors`, `typography`, `shape`
- **Content**: `leadingContent`, `trailingContent`, `content`
- **Labels**: `label`, `placeholder`, `description`, `title`, `subtitle`

## Component Style Enums

```kotlin
/**
 * Component style variants following Material 3 guidelines
 */
enum class ComponentStyle {
    Default,    // Standard appearance
    Filled,     // Filled container (Primary)
    Outlined,   // Outlined border
    Elevated,   // Elevated surface with shadow
    Tonal       // Tonal surface variant
}

/**
 * Component size variants following Material 3 guidelines
 */
enum class ComponentSize {
    Small,      // Compact for dense layouts
    Medium,     // Default size
    Large       // Prominent, touch-friendly
}
```

## Generic Data Models

### Reusable Data Structures:

```kotlin
/**
 * Generic item model for selectable components
 */
data class SelectableItem<T>(
    val id: String,
    val data: T,
    val label: String,
    val description: String? = null,
    val enabled: Boolean = true,
    val selected: Boolean = false
)

/**
 * Generic action item for menus and lists
 */
data class ActionItem(
    val id: String,
    val text: String,
    val icon: ImageVector? = null,
    val onClick: () -> Unit,
    val enabled: Boolean = true
)

/**
 * Generic validation result
 */
sealed class ValidationResult {
    data object Idle : ValidationResult()
    data object Valid : ValidationResult()
    data class Invalid(val error: String) : ValidationResult()
}
```

## Color Parameter Standards

### Component-Specific Color Overrides:

```kotlin
/**
 * Component color overrides following Material 3 tonal palettes
 */
data class ComponentColors(
    val container: Color? = null,           // Background/surface
    val onContainer: Color? = null,         // Text/icon on container
    val content: Color? = null,             // Primary content
    val onContent: Color? = null,           // Text/icon on content
    val border: Color? = null,              // Outlines/borders
    val error: Color? = null,               // Error states
    val onError: Color? = null              // Content on error
)
```

## Animation Standards

### Expressive Motion Patterns:

```kotlin
/**
 * Standard spring animations for component states
 */
object ComponentAnimations {
    // Enter/Exit transitions
    val EnterSpec = tween(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )

    val ExitSpec = tween(
        durationMillis = 200,
        easing = FastOutSlowInEasing
    )

    // State changes (hover, press, focus)
    val StateChangeSpring = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    // Ripple effects
    val RippleSpec = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
}
```

## Component Categories & Patterns

### 1. **Input Components**
```kotlin
@Composable
fun StandardTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    validation: (String) -> ValidationResult = { ValidationResult.Valid },
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxCharacters: Int? = null,
    colors: ComponentColors? = null
)
```

### 2. **Selection Components**
```kotlin
@Composable
fun StandardSelector<T>(
    items: List<SelectableItem<T>>,
    selectedItems: Set<T>,
    onSelectionChange: (Set<T>) -> Unit,
    modifier: Modifier = Modifier,
    multiSelect: Boolean = false,
    leadingContent: (@Composable (SelectableItem<T>) -> Unit)? = null,
    trailingContent: (@Composable (SelectableItem<T>) -> Unit)? = null,
    enabled: Boolean = true,
    style: ComponentStyle = ComponentStyle.Default
)
```

### 3. **Action Components**
```kotlin
@Composable
fun StandardButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    style: ComponentStyle = ComponentStyle.Filled,
    size: ComponentSize = ComponentSize.Medium,
    colors: ComponentColors? = null
)
```

### 4. **Display Components**
```kotlin
@Composable
fun StandardCard<T>(
    item: SelectableItem<T>,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    subtitleContent: (@Composable () -> Unit)? = null,
    descriptionContent: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    style: ComponentStyle = ComponentStyle.Outlined,
    colors: ComponentColors? = null
)
```

## Accessibility Standards

### Required Accessibility Properties:

```kotlin
/**
 * Standard accessibility for all interactive components
 */
@Composable
fun withAccessibility(
    component: String,
    contentDescription: String?,
    stateDescription: String? = null,
    role: Role? = null,
    semantics: SemanticsPropertyReceiver.() -> Unit = {}
) {
    // Apply standard semantics
    Modifier.semantics {
        if (contentDescription != null) {
            this.contentDescription = contentDescription
        }
        if (stateDescription != null) {
            this.stateDescription = stateDescription
        }
        role?.let { this.role = it }
        semantics()
    }
}
```

## Documentation Standards

### Required KDoc Sections:

```kotlin
/**
 * ComponentName - Brief description of purpose
 *
 * ## Usage Example
 * ```kotlin
 * ComponentName(
 *     requiredValue = "example",
 *     onRequiredAction = { value -> /* handle action */ }
 * )
 * ```
 *
 * ## Material 3 Compliance
 * - ✅ Uses Material3 components
 * - ✅ Proper color tokens
 * - ✅ Expressive animations
 * - ✅ Accessible interactions
 *
 * ## Component Parameters
 * @param requiredValue Required: Description of required parameter
 * @param onRequiredAction Required: Callback description
 * @param modifier Optional: Modifier for component
 * @param enabled Optional: Whether component is interactive
 *
 * ## Design Tokens Used
 * - Colors: `MaterialTheme.colorScheme.primary`, `.onPrimary`, `.surface`
 * - Typography: `MaterialTheme.typography.bodyLarge`
 * - Shapes: `MaterialTheme.shapes.medium` (12dp)
 * - Motion: Spring-based animations
 *
 * @see [RelatedComponent]
 */
```

## Testing Standards

### Component Test Structure:

```kotlin
/**
 * Component test template
 */
class ComponentNameTest {
    @Test
    fun `renders with default props`() {
        // Test default rendering
    }

    @Test
    fun `handles required parameter changes`() {
        // Test core functionality
    }

    @Test
    fun `applies custom modifier`() {
        // Test modifier application
    }

    @Test
    fun `respects enabled state`() {
        // Test interaction states
    }

    @Test
    fun `uses theme colors by default`() {
        // Test theme integration
    }
}
```

## Migration Checklist

When updating existing components to standard patterns:

- [ ] Reorder parameters to match standard pattern
- [ ] Rename parameters for consistency
- [ ] Add slot APIs for flexible content
- [ ] Implement standard color override system
- [ ] Add Material 3 compliance tags
- [ ] Include comprehensive KDoc
- [ ] Add accessibility properties
- [ ] Test with theme variations
- [ ] Verify responsive behavior
- [ ] Add component tests

---

This document defines the API patterns that ALL design system components MUST follow for consistency, reusability, and maintainability across multiple projects.