package design.system.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.components.*
import com.greenrobotdev.linklibrary.design.theme.DesignSystemTheme

/**
 * Design System Demo App
 *
 * This is a standalone example demonstrating how to use the reusable design system
 * in a completely separate project without any Link Library dependencies.
 *
 * ## Key Features Demonstrated:
 * ✅ No Link Library dependencies - Pure design system usage
 * ✅ Material 3 Expressive components
 * ✅ Standard API patterns
 * ✅ Theme customization
 * ✅ Form validation
 * ✅ Responsive layouts
 * ✅ State management
 */

@Composable
fun DesignSystemDemoApp() {
    var selectedTab by remember { mutableStateOf(DemoTab.Forms) }

    DesignSystemTheme(
        darkTheme = false, // Could use isSystemInDarkTheme()
        brandTheme = BrandTheme.Default
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top Navigation
                DemoTopBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                // Content Area
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    when (selectedTab) {
                        DemoTab.Forms -> FormsDemo()
                        DemoTab.Selection -> SelectionDemo()
                        DemoTab.Cards -> CardsDemo()
                        DemoTab.Layout -> LayoutDemo()
                    }
                }
            }
        }
    }
}

/**
 * Demo top navigation bar
 */
@Composable
private fun DemoTopBar(
    selectedTab: DemoTab,
    onTabSelected: (DemoTab) -> Unit
) {
    Surface(
        tonalElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DemoTab.values().forEach { tab ->
                val isSelected = selectedTab == tab
                StandardButton(
                    onClick = { onTabSelected(tab) },
                    text = tab.title,
                    style = if (isSelected) {
                        ComponentStyle.Filled
                    } else {
                        ComponentStyle.Tonal
                    },
                    size = ComponentSize.Small,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Forms Demo - Shows input components and validation
 */
@Composable
private fun FormsDemo() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    val isFormValid = name.isNotEmpty() &&
            email.contains("@") &&
            password.length >= 8

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Form Components",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Demonstrates input components with validation",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(8.dp))

        // Name input with validation
        ValidatedInput(
            value = name,
            onValueChange = { name = it },
            validation = { input ->
                when {
                    input.isBlank() -> ValidationResult.Idle
                    input.length < 2 -> ValidationResult.Invalid("Name too short")
                    else -> ValidationResult.Valid
                }
            },
            label = "Full Name",
            placeholder = "Enter your name"
        )

        // Email input
        ValidatedEmailInput(
            value = email,
            onValueChange = { email = it },
            label = "Email Address",
            required = true
        )

        // Password input with strength
        ValidatedPasswordInput(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            minLength = 8,
            showStrengthIndicator = true
        )

        // Bio text area
        TextArea(
            value = bio,
            onValueChange = { bio = it },
            label = "Bio",
            placeholder = "Tell us about yourself...",
            minLines = 3,
            maxLines = 5
        )

        // Form actions
        FormActionButtons(
            primaryText = "Create Account",
            onPrimaryClick = {
                // Handle form submission
                println("Form submitted: name=$name, email=$email")
            },
            secondaryText = "Cancel",
            onSecondaryClick = {
                // Reset form
                name = ""
                email = ""
                password = ""
                bio = ""
            },
            isFormValid = isFormValid,
            isLoading = false
        )
    }
}

/**
 * Selection Demo - Shows selection components
 */
@Composable
private fun SelectionDemo() {
    val categories = remember {
        listOf(
            TagChip(id = "1", name = "Design", color = androidx.compose.ui.graphics.Color(0xFF6750A4)),
            TagChip(id = "2", name = "Development", color = androidx.compose.ui.graphics.Color.Color(0xFF625B71)),
            TagChip(id = "3", name = "Business", color = androidx.compose.ui.graphics.Color.Color(0xFF381E72)),
            TagChip(id = "4", name = "Marketing", color = androidx.compose.ui.graphics.Color.Color(0xFF49454F)),
        )
    }

    var selectedCategories by remember { mutableStateOf(setOf<String>()) }

    var switch1 by remember { mutableStateOf(false) }
    var switch2 by remember { mutableStateOf(true) }

    var sliderValue by remember { mutableStateOf(0.5f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Selection Components",
            style = MaterialTheme.typography.headlineMedium
        )

        // Tag chips selector
        TagChipsSelector(
            availableTags = categories,
            selectedTagIds = selectedCategories,
            onTagToggle = { tagId ->
                selectedCategories = selectedCategories.toMutableSet().apply {
                    if (contains(tagId)) remove(tagId) else add(tagId)
                }
            },
            onTagCreate = { name ->
                // Create new tag
                println("Creating tag: $name")
            },
            placeholder = "Select or create categories...",
            showUsageCounts = false
        )

        // Switches
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleMedium
        )

        AppSwitch(
            checked = switch1,
            onCheckedChange = { switch1 = it },
            label = "Enable notifications",
            description = "Receive push notifications"
        )

        AppSwitch(
            checked = switch2,
            onCheckedChange = { switch2 = it },
            label = "Dark mode",
            description = "Use dark theme"
        )

        // Slider
        AppSlider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            valueRange = 0f..1f,
            label = "Volume: ${(sliderValue * 100).toInt()}%",
            showValue = true
        )

        // Selection summary
        if (selectedCategories.isNotEmpty()) {
            Text(
                text = "Selected: ${selectedCategories.size} categories",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Cards Demo - Shows card components
 */
@Composable
private fun CardsDemo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Card Components",
            style = MaterialTheme.typography.headlineMedium
        )

        // Card grid
        val cardItems = remember {
            listOf(
                CardData(
                    title = "Getting Started",
                    description = "Learn the basics of the design system",
                    icon = "school"
                ),
                CardData(
                    title = "Components",
                    description = "Explore available components",
                    icon = "widgets"
                ),
                CardData(
                    title = "Theming",
                    description = "Customize colors and styles",
                    icon = "palette"
                ),
                CardData(
                    title = "Best Practices",
                    description = "Learn design system patterns",
                    icon = "tips_and_updates"
                )
            )
        }

        cardItems.forEach { cardData ->
            StandardCard(
                title = cardData.title,
                description = cardData.description,
                onClick = {
                    println("Clicked: ${cardData.title}")
                },
                leadingContent = {
                    // Icon would go here
                },
                trailingContent = {
                    IconButton(
                        onClick = { /* more options */ },
                        contentDescription = "More options"
                    ) {
                        // Icon would go here
                    }
                },
                style = ComponentStyle.Outlined
            )
        }

        // Tag card example
        TagCard(
            data = TagCardData(
                id = "example",
                name = "Design System",
                color = MaterialTheme.colorScheme.primary,
                usageCount = 42,
                description = "UI component library",
                selected = false,
                isHovered = false
            ),
            onClick = { /* navigate */ },
            onEdit = { /* edit */ },
            onDelete = { /* delete */ }
        )
    }
}

/**
 * Layout Demo - Shows layout components
 */
@Composable
private fun LayoutDemo() {
    val bentoItems = remember {
        listOf(
            BentoItem(data = "Analytics", size = BentoItemSize.Large),
            BentoItem(data = "Recent", size = BentoItemSize.Medium),
            BentoItem(data = "Popular", size = BentoItemSize.Medium),
            BentoItem(data = "Settings", size = BentoItemSize.Small),
            BentoItem(data = "Help", size = BentoItemSize.Small),
            BentoItem(data = "About", size = BentoItemSize.Small),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Layout Components",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Responsive grid layout with animated items",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        BentoGridResponsive(
            items = bentoItems,
            minColumnWidth = 180.dp
        ) { item, _ ->
            StandardCard(
                title = item.data,
                description = when (item.size) {
                    BentoItemSize.Large -> "Detailed content and analytics"
                    BentoItemSize.Medium -> "Quick overview and actions"
                    BentoItemSize.Small -> "Compact item"
                    else -> "Content"
                },
                onClick = {
                    println("Clicked: ${item.data}")
                },
                style = ComponentStyle.Filled
            )
        }
    }
}

/**
 * Demo data class
 */
data class CardData(
    val title: String,
    val description: String,
    val icon: String
)

/**
 * Demo tabs enum
 */
enum class DemoTab(val title: String) {
    Forms("Forms"),
    Selection("Selection"),
    Cards("Cards"),
    Layout("Layout")
}