# Design System Component Catalog

Complete reference of all available design system components with usage examples, parameters, and Material 3 compliance information.

## Table of Contents
- [Input Components](#input-components)
- [Selection Components](#selection-components)
- [Action Components](#action-components)
- [Display Components](#display-components)
- [Layout Components](#layout-components)
- [Advanced Components](#advanced-components)
- [Progress Components](#progress-components)
- [Empty State Components](#empty-state-components)

---

## Input Components

### StandardTextField

Basic text input field with Material 3 styling and theming.

```kotlin
StandardTextField(
    value = text,
    onValueChange = { text = it },
    label = "Username",
    placeholder = "Enter your username",
    modifier = Modifier.fillMaxWidth()
)
```

**Parameters:**
- `value: String` - Current text value
- `onValueChange: (String) -> Unit` - Callback when text changes
- `label: String?` - Optional label text
- `placeholder: String?` - Optional placeholder text
- `modifier: Modifier` - Layout modifier
- `enabled: Boolean` - Whether field is editable

**Material 3:** ✅ Fully compliant with OutlinedTextField patterns

---

### ValidatedInput

Text input with real-time validation and animated feedback.

```kotlin
ValidatedInput(
    value = email,
    onValueChange = { email = it },
    validation = { input ->
        when {
            input.isBlank() -> ValidationResult.Idle
            !input.contains("@") -> ValidationResult.Invalid("Invalid email")
            else -> ValidationResult.Valid
        }
    },
    label = "Email",
    keyboardType = KeyboardType.Email
)
```

**Validation States:**
- `ValidationResult.Idle` - No user input yet
- `ValidationResult.Valid` - Input passes validation
- `ValidationResult.Invalid(error: String)` - Input fails with error message

**Material 3:** ✅ Expressive validation with color state transitions

---

### ValidatedUrlInput

Pre-configured URL validator with URL-specific validation logic.

```kotlin
ValidatedUrlInput(
    value = url,
    onValueChange = { url = it },
    label = "Website URL",
    placeholder = "https://example.com",
    required = true
)
```

**Validation Rules:**
- Must not be blank (if required)
- Must start with `https://`, `http://`, or `ftp://`
- Must be valid URL format

---

### ValidatedEmailInput

Pre-configured email validator.

```kotlin
ValidatedEmailInput(
    value = email,
    onValueChange = { email = it },
    label = "Email Address",
    placeholder = "user@example.com",
    required = true
)
```

---

### ValidatedPasswordInput

Password input with strength indicator and validation.

```kotlin
ValidatedPasswordInput(
    value = password,
    onValueChange = { password = it },
    label = "Password",
    minLength = 8,
    showStrengthIndicator = true
)
```

**Password Requirements:**
- Minimum length (default: 8 characters)
- Must contain letters and numbers
- Visual strength indicator (3 levels)

---

### SearchField

Search-specific text field with search icon and clear button.

```kotlin
SearchField(
    value = searchQuery,
    onValueChange = { searchQuery = it },
    placeholder = "Search...",
    onClearClick = { searchQuery = "" }
)
```

**Material 3:** ✅ Optimized for search interactions

---

### TextArea

Multi-line text input for longer content.

```kotlin
TextArea(
    value = description,
    onValueChange = { description = it },
    label = "Description",
    placeholder = "Enter description...",
    minLines = 3,
    maxLines = 6
)
```

---

## Selection Components

### TagChipsSelector

Advanced multi-select tag interface with inline creation and search.

```kotlin
val availableTags = listOf(
    TagChip(id = "1", name = "Design", color = Color.Blue),
    TagChip(id = "2", name = "Development", color = Color.Green)
)

TagChipsSelector(
    availableTags = availableTags,
    selectedTagIds = setOf("1"),
    onTagToggle = { tagId -> viewModel.toggleTag(tagId) },
    onTagCreate = { name -> viewModel.createTag(name) },
    placeholder = "Search or create tags...",
    showUsageCounts = true
)
```

**Features:**
- 🔍 Search and filter tags
- ➕ Inline tag creation
- ✨ Animated state transitions
- 🎨 Color customization
- 📊 Usage count display

**Material 3:** ✅ Full compliance with Material 3 chips

---

### TagChipsSelectorInline

Compact horizontal version for smaller spaces.

```kotlin
TagChipsSelectorInline(
    availableTags = availableTags,
    selectedTagIds = selectedTags,
    onTagToggle = { tagId -> viewModel.toggleTag(tagId) },
    onTagCreate = { name -> viewModel.createTag(name) },
    maxVisible = 3
)
```

---

### CollectionDropdown

Searchable collection selector with inline creation dialog.

```kotlin
val collections = listOf(
    CollectionItem(
        id = "1",
        name = "Design Resources",
        description = "UI/UX articles and tools",
        linkCount = 25
    )
)

CollectionDropdown(
    collections = collections,
    selectedCollectionId = selectedId,
    onCollectionSelected = { id -> viewModel.selectCollection(id) },
    onCollectionCreate = { name, description ->
        viewModel.createCollection(name, description)
    },
    placeholder = "Select collection",
    showLinkCounts = true
)
```

**Features:**
- 🔍 Real-time search and filtering
- ➕ Inline collection creation
- 📊 Usage statistics
- 🎨 Icon and color support
- ✨ Animated dropdown

**Material 3:** ✅ Expressive dropdown with Material 3 patterns

---

### CollectionSelectorCompact

Compact version for limited spaces.

```kotlin
CollectionSelectorCompact(
    collections = collections,
    selectedCollectionId = selectedId,
    onCollectionSelected = { id -> viewModel.selectCollection(id) }
)
```

---

### AppCheckbox

Material 3 checkbox with proper semantics and animations.

```kotlin
var isChecked by remember { mutableStateOf(false) }

AppCheckbox(
    checked = isChecked,
    onCheckedChange = { isChecked = it },
    label = "Accept terms and conditions",
    enabled = true
)
```

---

### AppRadio

Material 3 radio button for single selection.

```kotlin
AppRadio(
    selected = option == selectedOption,
    onClick = { selectedOption = option },
    label = option.name,
    enabled = true
)
```

---

### AppSwitch

Material 3 toggle switch.

```kotlin
AppSwitch(
    checked = isEnabled,
    onCheckedChange = { isEnabled = it },
    label = "Enable notifications",
    description = "Receive push notifications"
)
```

---

### AppSlider

Range selection slider with Material 3 styling.

```kotlin
AppSlider(
    value = sliderValue,
    onValueChange = { sliderValue = it },
    valueRange = 0f..100f,
    label = "Volume",
    showValue = true
)
```

---

## Action Components

### StandardButton

Fully-featured button with multiple variants.

```kotlin
// Filled variant (default)
StandardButton(
    onClick = { /* action */ },
    text = "Submit",
    style = ComponentStyle.Filled,
    size = ComponentSize.Medium
)

// Outlined variant
StandardButton(
    onClick = { /* action */ },
    text = "Cancel",
    style = ComponentStyle.Outlined
)

// Text variant
StandardButton(
    onClick = { /* action */ },
    text = "Learn More",
    style = ComponentStyle.Tonal
)
```

**Available Styles:**
- `ComponentStyle.Filled` - Primary action with solid color
- `ComponentStyle.Outlined` - Secondary action with border
- `ComponentStyle.Tonal` - Tonal surface background
- `ComponentStyle.Elevated` - Elevated with shadow

**Available Sizes:**
- `ComponentSize.Small` - Compact (32dp height)
- `ComponentSize.Medium` - Default (40dp height)
- `ComponentSize.Large` - Prominent (48dp height)

**Material 3:** ✅ Full compliance with Material 3 button specifications

---

### IconButton

Icon-only button for compact actions.

```kotlin
IconButton(
    onClick = { /* action */ },
    icon = Icons.Default.Search,
    contentDescription = "Search",
    style = ComponentStyle.Default
)
```

---

### ActionButton

Button with both icon and text.

```kotlin
ActionButton(
    onClick = { /* action */ },
    icon = Icons.Default.Add,
    text = "Create New",
    style = ComponentStyle.Filled
)
```

---

### AutoFetchButton

Button with integrated loading and fetch states.

```kotlin
AutoFetchButton(
    state = fetchState,
    onClick = { viewModel.fetchData() },
    text = "Load Data",
    loadingText = "Loading...",
    successText = "Loaded!"
)
```

**Fetch States:**
- `FetchState.Idle` - Ready to fetch
- `FetchState.Loading` - Currently fetching
- `FetchState.Success` - Successfully fetched
- `FetchState.Error(message)` - Fetch failed

---

## Display Components

### StandardCard

Versatile card component with multiple styles.

```kotlin
StandardCard(
    title = "Card Title",
    description = "Card description text",
    onClick = { /* navigate */ },
    leadingContent = {
        Icon(Icons.Default.Star, contentDescription = null)
    },
    trailingContent = {
        IconButton(onClick = { /* more options */ }) {
            Icon(Icons.Default.MoreVert)
        }
    },
    style = ComponentStyle.Outlined
)
```

**Card Styles:**
- `ComponentStyle.Filled` - Solid background
- `ComponentStyle.Outlined` - Bordered card
- `ComponentStyle.Elevated` - Elevated with shadow

---

### TagCard

Advanced tag display with hover effects and interactions.

```kotlin
TagCard(
    data = TagCardData(
        id = "1",
        name = "Design Systems",
        color = Color(0xFF6750A4),
        usageCount = 42,
        description = "UI/UX design patterns",
        selected = false,
        isHovered = false
    ),
    onClick = { /* navigate to tag */ },
    onEdit = { /* edit tag */ },
    onDelete = { /* delete tag */ }
)
```

**Features:**
- ✨ Hover animations
- 🎨 Customizable colors
- 📊 Usage statistics
- 🏷️ Edit and delete actions

---

### LinkCard

Link preview card with metadata display.

```kotlin
LinkCard(
    title = "Article Title",
    description = "Article description",
    url = "https://example.com/article",
    imageUrl = "https://example.com/image.jpg",
    tags = listOf("Design", "UX"),
    onClick = { /* open link */ },
    onBookmark = { /* bookmark */ }
)
```

---

### ArticleCard

Article display card with reading time and metadata.

```kotlin
ArticleCard(
    title = "Understanding Material 3",
    author = "Jane Doe",
    readingTime = "5 min read",
    publishDate = "2024-01-15",
    imageUrl = "https://example.com/cover.jpg",
    onClick = { /* open article */ }
)
```

---

## Layout Components

### AppScaffold

Standard app layout with top bar and FAB.

```kotlin
AppScaffold(
    title = "My App",
    navigationIcon = Icons.Default.Menu,
    onNavigationClick = { /* open drawer */ },
    fabIcon = Icons.Default.Add,
    onFabClick = { /* create new */ },
    fabPosition = FabPosition.End
) { padding ->
    // Main content with padding applied
    Column(Modifier.padding(padding)) {
        // Your content here
    }
}
```

**FAB Positions:**
- `FabPosition.Start` - Left side
- `FabPosition.End` - Right side (default)
- `FabPosition.Center` - Center bottom

**Material 3:** ✅ Standard Material 3 scaffold pattern

---

### BentoGrid

Responsive grid layout system with animations.

```kotlin
val items = listOf(
    BentoItem(data = "item1", size = BentoItemSize.Medium),
    BentoItem(data = "item2", size = BentoItemSize.Large),
    BentoItem(data = "item3", size = BentoItemSize.Small)
)

BentoGrid(
    items = items,
    columns = 2,
    horizontalSpacing = 12.dp,
    verticalSpacing = 12.dp
) { item, index ->
    // Item content
    StandardCard(
        title = item.data,
        description = "Item $index"
    )
}
```

**Item Sizes:**
- `BentoItemSize.Small` - 1x1 grid cell (120dp height)
- `BentoItemSize.Medium` - 1x1 grid cell (160dp height)
- `BentoItemSize.Large` - 2x2 grid cells (320dp height)
- `BentoItemSize.Full` - Full width (200dp height)

**Variants:**
- `BentoGridResponsive` - Auto-adapts columns to screen width
- `BentoRow` - Horizontal scrolling layout
- `BentoMasonry` - Masonry-style layout
- `BentoGridStaggered` - Staggered animations

**Material 3:** ✅ Expressive grid with spring animations

---

### BulkOperationsBar

Bottom action bar for bulk operations.

```kotlin
BulkOperationsBar(
    selectedCount = selectedItems.size,
    onClearAll = { viewModel.clearSelection() },
    actions = listOf(
        BulkAction(
            icon = Icons.Default.Delete,
            text = "Delete",
            onClick = { viewModel.deleteSelected() }
        ),
        BulkAction(
            icon = Icons.Default.Move,
            text = "Move",
            onClick = { /* move */ }
        )
    )
)
```

---

## Advanced Components

### ValidatedInput (Advanced Features)

Advanced validation with custom animations and states.

```kotlin
ValidatedInput(
    value = complexInput,
    onValueChange = { complexInput = it },
    validation = { input ->
        // Complex validation logic
        when {
            input.matches(Regex("^\\d{3}-\\d{2}-\\d{4}$")) -> ValidationResult.Valid
            input.isNotEmpty() -> ValidationResult.Invalid("Format: XXX-XX-XXXX")
            else -> ValidationResult.Idle
        }
    },
    maxCharacters = 11,
    leadingIcon = {
        Icon(Icons.Default.CreditCard, contentDescription = null)
    },
    shakeOnError = true
)
```

**Advanced Features:**
- 🎯 Real-time validation feedback
- ✨ Shake animation on errors
- 🎨 Color state transitions
- 📊 Character counting
- 🔧 Custom validation logic

---

### FormActionButtons

Standard form action buttons with validation states.

```kotlin
FormActionButtons(
    primaryText = "Save",
    onPrimaryClick = { /* save */ },
    secondaryText = "Cancel",
    onSecondaryClick = { /* cancel */ },
    isFormValid = isValid,
    isLoading = isSaving
)
```

---

## Progress Components

### CircularProgress

Circular progress indicator with Material 3 styling.

```kotlin
CircularProgress(
    progress = 0.75f, // 0.0 to 1.0
    modifier = Modifier.size(48.dp),
    color = MaterialTheme.colorScheme.primary
)
```

**Variants:**
- Determinate (0.0-1.0 progress)
- Indeterminate (continuous animation)

---

### LinearProgress

Linear progress bar.

```kotlin
LinearProgress(
    progress = 0.6f,
    modifier = Modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.primary
)
```

---

### ExpressiveProgress

Animated progress indicator with expressive motion.

```kotlin
ExpressiveProgress(
    progress = currentProgress,
    modifier = Modifier.fillMaxWidth(),
    showPercentage = true,
    animated = true
)
```

---

## Empty State Components

### EmptyState

Empty state component with icon, message, and optional action.

```kotlin
EmptyState(
    icon = Icons.Default.Inbox,
    message = "No items found",
    description = "Get started by creating your first item",
    actionLabel = "Create Item",
    onActionClick = { /* create */ }
)
```

---

### BentoEmptyState

Empty state for bento grids.

```kotlin
BentoEmptyState(
    message = "No content available",
    icon = Icons.Default.Collection,
    actionLabel = "Add Content",
    onActionClick = { /* add */ }
)
```

---

## Component Features Summary

| Feature | Input | Selection | Action | Display | Layout |
|---------|-------|-----------|--------|---------|--------|
| Validation | ✅ | ❌ | ❌ | ❌ | ❌ |
| Animations | ✅ | ✅ | ✅ | ✅ | ✅ |
| Accessibility | ✅ | ✅ | ✅ | ✅ | ✅ |
| Dark Mode | ✅ | ✅ | ✅ | ✅ | ✅ |
| Responsive | ✅ | ✅ | ✅ | ✅ | ✅ |
| Custom Colors | ✅ | ✅ | ✅ | ✅ | ✅ |
| Custom Sizes | ✅ | ❌ | ✅ | ❌ | ❌ |
| Slot APIs | ✅ | ❌ | ✅ | ✅ | ❌ |

## Material 3 Compliance Matrix

| Component | Colors | Typography | Shapes | Motion | Accessibility |
|-----------|--------|------------|--------|--------|---------------|
| All Input Components | ✅ | ✅ | ✅ | ✅ | ✅ |
| All Selection Components | ✅ | ✅ | ✅ | ✅ | ✅ |
| All Action Components | ✅ | ✅ | ✅ | ✅ | ✅ |
| All Display Components | ✅ | ✅ | ✅ | ✅ | ✅ |
| All Layout Components | ✅ | ✅ | ✅ | ✅ | ✅ |

---

## Usage Patterns

### Form Pattern

```kotlin
@Composable
fun MyForm() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var agreed by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ValidatedInput(
            value = name,
            onValueChange = { name = it },
            validation = {
                if (it.length >= 3) ValidationResult.Valid
                else ValidationResult.Invalid("Name too short")
            },
            label = "Full Name"
        )

        ValidatedEmailInput(
            value = email,
            onValueChange = { email = it },
            label = "Email"
        )

        AppCheckbox(
            checked = agreed,
            onCheckedChange = { agreed = it },
            label = "I agree to terms"
        )

        FormActionButtons(
            primaryText = "Submit",
            onPrimaryClick = { /* submit */ },
            isFormValid = name.isNotEmpty() && email.isValidEmail() && agreed
        )
    }
}
```

### Selection Pattern

```kotlin
@Composable
fun MySelector() {
    val availableTags = remember {
        listOf(
            TagChip(id = "1", name = "Design"),
            TagChip(id = "2", name = "Development")
        )
    }
    val selectedTags = remember { mutableStateOf(setOf<String>()) }

    TagChipsSelector(
        availableTags = availableTags,
        selectedTagIds = selectedTags.value,
        onTagToggle = { tagId ->
            selectedTags.value = selectedTags.value.toMutableSet().apply {
                if (contains(tagId)) remove(tagId) else add(tagId)
            }
        },
        onTagCreate = { name -> /* create new tag */ }
    )
}
```

### Content Display Pattern

```kotlin
@Composable
fun MyContentDisplay(items: List<Item>) {
    val bentoItems = items.map {
        BentoItem(data = it, size = BentoItemSize.Medium)
    }

    BentoGridResponsive(
        items = bentoItems,
        minColumnWidth = 200.dp
    ) { item, _ ->
        StandardCard(
            title = item.data.title,
            description = item.data.description,
            onClick = { /* navigate */ }
        )
    }
}
```

---

This catalog provides a comprehensive reference for all design system components. For specific implementation details and advanced usage, refer to individual component documentation files.