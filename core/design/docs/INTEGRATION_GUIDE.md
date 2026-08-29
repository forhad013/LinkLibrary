# Design System Integration Guide

## Overview

This design system provides a comprehensive, reusable set of Material 3 Expressive components that can be integrated into any Kotlin Multiplatform project. The components are fully decoupled from business logic and use generic APIs for maximum reusability.

## Quick Start

### 1. Add Dependency

Add the design system module to your project:

```kotlin
// settings.gradle.kts
include(":core:design")

// or in your module's build.gradle.kts
dependencies {
    implementation(project(":core:design"))
}
```

### 2. Apply Theme

Wrap your app with the design system theme:

```kotlin
import com.greenrobotdev.linklibrary.design.theme.DesignSystemTheme

@Composable
fun MyApp() {
    DesignSystemTheme(
        darkTheme = isSystemInDarkTheme(),
        // Optional: Custom brand theme
        brandTheme = BrandTheme.Default
    ) {
        // Your app content
    }
}
```

### 3. Use Components

Start using design system components:

```kotlin
import com.greenrobotdev.linklibrary.design.components.*

@Composable
fun MyScreen() {
    StandardButton(
        onClick = { /* handle click */ },
        text = "Click Me",
        style = ComponentStyle.Filled
    )

    StandardTextField(
        value = text,
        onValueChange = { text = it },
        label = "Enter text"
    )
}
```

## Component Categories

### Input Components

- **StandardTextField**: Basic text input with validation
- **ValidatedInput**: Real-time validation with animated feedback
- **SearchField**: Search-specific text field
- **TextArea**: Multi-line text input

### Selection Components

- **TagChipsSelector**: Multi-select tag interface with creation
- **CollectionDropdown**: Searchable collection selector
- **AppCheckbox**: Standard checkbox component
- **AppSwitch**: Toggle switch component
- **AppSlider**: Range selection slider

### Action Components

- **StandardButton**: Filled, outlined, text, elevated variants
- **ActionButton**: Icon buttons with various styles
- **FabButton**: Floating action buttons

### Display Components

- **StandardCard**: Card component with multiple styles
- **TagCard**: Tag display with hover effects
- **LinkCard**: Link preview card
- **ArticleCard**: Article display card

### Layout Components

- **AppScaffold**: Standard app layout with top bar and FAB
- **BentoGrid**: Responsive grid layout system
- **BulkOperationsBar**: Bulk action interface

## Customization Guide

### Theme Customization

#### Custom Colors

```kotlin
DesignSystemTheme(
    customColorScheme = lightColorScheme(
        primary = Color(0xFF6750A4),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFEADDFF),
        // ... override other colors
    )
) {
    // Content
}
```

#### Custom Typography

```kotlin
DesignSystemTheme(
    customTypography = Typography(
        displayLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 57.sp
        ),
        // ... override other text styles
    )
) {
    // Content
}
```

#### Custom Shapes

```kotlin
DesignSystemTheme(
    customShapes = Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp)
    )
) {
    // Content
}
```

### Component-Level Customization

#### Color Overrides

```kotlin
StandardButton(
    onClick = { },
    text = "Custom Button",
    colors = ComponentColors(
        container = Color.Blue,
        onContainer = Color.White,
        content = Color.White
    )
)
```

#### Size Variants

```kotlin
StandardButton(
    onClick = { },
    text = "Large Button",
    size = ComponentSize.Large
)
```

#### Content Slots

```kotlin
StandardTextField(
    value = text,
    onValueChange = { text = it },
    label = "Search",
    leadingContent = {
        Icon(Icons.Default.Search, contentDescription = "Search")
    },
    trailingContent = {
        IconButton(onClick = { /* clear */ }) {
            Icon(Icons.Default.Close, contentDescription = "Clear")
        }
    }
)
```

## Data Integration

### Using Generic Components

The design system uses generic data models that work with any domain:

```kotlin
// Your domain model
data class User(
    val id: String,
    val name: String,
    val email: String
)

// Convert to design system model
val userItems = users.map { user ->
    SelectableItem(
        id = user.id,
        data = user,
        label = user.name,
        description = user.email,
        enabled = true,
        selected = selectedUsers.contains(user.id)
    )
}

// Use with selector component
TagChipsSelector(
    availableTags = userItems.map {
        TagChip(
            id = it.id,
            name = it.label,
            selected = it.selected
        )
    },
    selectedTagIds = selectedUsers,
    onTagToggle = { userId -> /* toggle logic */ }
)
```

### Custom Data Models

For complex scenarios, create adapter functions:

```kotlin
// Adapter function
fun User.toTagChip(): TagChip {
    return TagChip(
        id = id,
        name = name,
        color = if (isActive) Color.Green else Color.Gray,
        selected = isSelected,
        usageCount = postCount
    )
}

// Usage
val userChips = users.map { it.toTagChip() }

TagChipsSelector(
    availableTags = userChips,
    selectedTagIds = selectedUserIds,
    onTagToggle = { userId -> viewModel.toggleUser(userId) },
    onTagCreate = { name -> viewModel.createUser(name) }
)
```

## Advanced Usage

### Custom Validation

```kotlin
fun validateEmail(email: String): ValidationResult {
    return when {
        email.isBlank() -> ValidationResult.Idle
        !email.contains("@") -> ValidationResult.Invalid("Invalid email format")
        else -> ValidationResult.Valid
    }
}

ValidatedInput(
    value = email,
    onValueChange = { email = it },
    validation = ::validateEmail,
    label = "Email"
)
```

### Animated State Management

```kotlin
@Composable
fun AnimatedComponent() {
    var isVisible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "alpha"
    )

    StandardButton(
        onClick = { isVisible = !isVisible },
        text = "Toggle Visibility",
        modifier = Modifier.graphicsLayer { this.alpha = alpha }
    )
}
```

### Responsive Layouts

```kotlin
@Composable
fun ResponsiveContent() {
    val windowSizeClass = calculateWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            // Mobile layout
            Column { /* content */ }
        }
        WindowWidthSizeClass.Medium -> {
            // Tablet layout
            Row { /* content */ }
        }
        else -> {
            // Desktop layout
            BentoGrid { /* content */ }
        }
    }
}
```

## Migration Guide

### From Material 2 to Design System

```kotlin
// BEFORE (Material 2)
Button(
    onClick = { },
    colors = ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.primary
    )
) {
    Text("Click Me")
}

// AFTER (Design System - Material 3)
StandardButton(
    onClick = { },
    text = "Click Me",
    style = ComponentStyle.Filled
)
```

### From Custom Components to Design System

```kotlin
// BEFORE (Custom implementation)
@Composable
fun CustomTextField(
    value: String,
    onChange: (String) -> Unit
) {
    // 50+ lines of custom implementation
}

// AFTER (Using design system)
StandardTextField(
    value = value,
    onValueChange = onChange,
    // Built-in validation, animations, accessibility
)
```

## Best Practices

### 1. Consistent Component Usage

Always use design system components instead of creating custom ones:

```kotlin
// ❌ BAD - Custom button
@Composable
fun MyButton(text: String, onClick: () -> Unit) {
    // Custom implementation
}

// ✅ GOOD - Design system button
StandardButton(
    text = text,
    onClick = onClick,
    style = ComponentStyle.Filled
)
```

### 2. Theme Integration

Leverage the theme system instead of hardcoding values:

```kotlin
// ❌ BAD - Hardcoded colors
Text(
    text = "Hello",
    color = Color(0xFF6750A4)
)

// ✅ GOOD - Theme colors
Text(
    text = "Hello",
    color = MaterialTheme.colorScheme.primary
)
```

### 3. Accessibility First

Always provide proper content descriptions and semantics:

```kotlin
// ❌ BAD - Missing accessibility
IconButton(onClick = {}) {
    Icon(Icons.Default.Search)
}

// ✅ GOOD - Proper accessibility
IconButton(
    onClick = {},
    modifier = Modifier.semantics {
        contentDescription = "Search"
        role = Role.Button
    }
) {
    Icon(Icons.Default.Search)
}
```

### 4. Responsive Design

Use adaptive components for different screen sizes:

```kotlin
// ❌ BAD - Fixed layout
Column {
    repeat(10) { Item() }
}

// ✅ GOOD - Responsive layout
BentoGridResponsive(
    items = items,
    minColumnWidth = 180.dp
) { item, index ->
    Item(item)
}
```

## Troubleshooting

### Common Issues

#### Theme Not Applied

**Problem**: Components don't use custom theme colors.

**Solution**: Ensure your app is wrapped in `DesignSystemTheme`:

```kotlin
DesignSystemTheme {
    // Your content must be here
}
```

#### Components Not Found

**Problem**: Import errors for design system components.

**Solution**: Add the correct import:

```kotlin
import com.greenrobotdev.linklibrary.design.components.*
```

#### Type Mismatches

**Problem**: Can't pass your domain models to components.

**Solution**: Use adapter functions to convert to design system models:

```kotlin
fun YourModel.toComponentModel() = ComponentModel(
    id = id,
    name = name
    // ... map other fields
)
```

## Performance Tips

### 1. Avoid Unnecessary Recomposition

```kotlin
// ❌ BAD - Causes recomposition on every change
@Composable
fun ExpensiveComponent() {
    val items = viewModel.items // Causes recomposition

    // ❌ BETTER - Use derived state
    val filteredItems by remember {
        derivedStateOf {
            items.filter { it.isActive }
        }
    }
}
```

### 2. Stable Callbacks

```kotlin
// ✅ GOOD - Stable callbacks
@Composable
fun MyComponent(
    onItemClick: (Item) -> Unit
) {
    val stableCallback = remember(itemClick) {
        { item: Item -> onItemClick(item) }
    }
}
```

## Support & Contributing

### Getting Help

- Check component documentation for usage examples
- Review demo projects for implementation patterns
- Report issues via project issue tracker

### Contributing

When contributing new components:

1. Follow the [Standard API Patterns](../STANDARD_API_PATTERNS.md)
2. Use the [Component Template](../COMPONENT_TEMPLATE.kt)
3. Include comprehensive KDoc documentation
4. Add Material 3 compliance tags
5. Test with different themes and screen sizes

## Next Steps

- Explore [Component Documentation](./components/) for detailed component guides
- Check [Example Projects](../../examples/) for complete implementations
- Review [Design Tokens](./design-tokens.md) for theme customization
- Read [Accessibility Guidelines](./accessibility.md) for inclusive design