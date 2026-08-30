# Link Library KMP Project - Claude Context

## Project Overview
A Kotlin Multiplatform (KMP) link library application for managing bookmarks with tags and collections.

## Architecture
- **Multiplatform**: Android, JVM, WASM support
- **Database**: Room KMP with SQLite
- **DI**: Koin for dependency injection
- **UI**: Jetpack Compose with Navigation 3.0
- **Pattern**: MVVM + UseCase architecture

## Module Structure
- `app/` - Main application with UI screens and view models
- `database/` - Room database, repositories, and entities
- `.claude/` - Claude Code configuration and skills

## Recent Fixes & Improvements (Current Session)

### ✅ AGP 9 Migration Complete (2025-08-26)
**Major Milestone**: Successfully migrated entire project to Android Gradle Plugin 9.0

**Project Versions (Post-Migration)**:
- **AGP**: 9.0.0 (from 8.9.1)
- **Kotlin**: 2.2.10 (from 2.1.0)
- **Gradle**: 9.1.0 (from 8.11.1)
- **KSP**: 2.2.10-2.0.2 (from 2.1.0-1.0.29)

**Key Changes**:
- All KMP modules migrated to use `com.android.kotlin.multiplatform.library` plugin (AGP 9 requirement)
- Replaced incompatible `kotlin("multiplatform")` + `com.android.library` plugin combination
- Maintained `android {}` configuration blocks for Android-specific settings
- Preserved `androidTarget {}` blocks for compiler options
- Compose integration managed through `org.jetbrains.kotlin.plugin.compose`
- Pure Android modules (androidApp) use AGP 9 built-in Kotlin support

**Modules Migrated**:
1. ✅ database (Room KMP)
2. ✅ core:design (Material 3 theme)
3. ✅ core:utils (MoleculeViewModel)
4. ✅ bookmarks (All bookmark screens)
5. ✅ app (Main KMP application)
6. ✅ androidApp (Android wrapper)

**Breaking Changes Resolved**:
- AGP 9 incompatibility with `kotlin("multiplatform")` + `com.android.library` plugins
- Solution: Use `com.android.kotlin.multiplatform.library` plugin instead
- `kotlinOptions` removed - configured via `compilerOptions` in `androidTarget` blocks
- `buildFeatures.compose` and `composeOptions` moved to Kotlin Compose plugin
- Both `android {}` and `androidTarget {}` blocks required for complete configuration

**Important Notes**:
- WASM target maintained but deferred comprehensive testing per project requirements
- All platform-specific dependencies preserved (Android/JVM desktop/iOS)
- Compose compiler metrics and monitoring configuration maintained
- Database integration maintained across all targets

**Migration Branch**: `agp9-migration`
**Documentation**: See AGP 9 migration plan for detailed implementation steps

### ✅ Fixed Tag/Collection Auto-Updating (2024-08-24)
**Problem**: When adding new tags/collections from Add Link screen, lists didn't update automatically - required app restart.

**Solution**:
- Modified `AddLinkUseCase.kt` to use reactive `Flow.collect()` instead of one-time `.first()` calls
- Tags and collections now update automatically when changes occur in database

### ✅ Removed Mandatory Description Fields (2024-08-24)
**Problem**: Tag and collection creation required both name AND description.

**Solution**:
- Updated `AddTagUseCase.kt` validation: `name.isNotBlank()` only
- Updated `AddCollectionUseCase.kt` validation: `name.isNotBlank()` only
- Description fields now optional

### ✅ Fixed Save Link Dialog Reusability (2024-08-24)
**Problem**: Plus button only showed Add Link screen once, subsequent clicks didn't work.

**Solution**:
- Added `navigateToNew()` method to `Navigator` class in `NavigationState.kt`
- Method removes current route if same type before adding new instance
- Updated FAB and navigation calls to use new method

### ✅ Fixed Tag Display Under Links (2024-08-24)
**Problem**: Tags and collections weren't showing under each link in library and detail views.

**Root Cause**: Architectural mismatch - tags stored in `LinkTagEntity` relationship table but code read from empty JSON field in links table.

**Solution**:
- Added `getLinksWithTags()` method to `LinkRepository` interface
- Updated `RoomLinkRepository` to accept `TagRepository` and `CollectionRepository` dependencies
- Implemented proper loading: fetch link entities → populate tags from relationship table → convert to domain models
- Updated `LibraryUseCase.kt` and `LinkDetailUseCase.kt` to use new method
- Fixed DI in `DataModule.kt` to provide required repositories

### ✅ Design System Module Implementation (2025-08-30)
**Major Milestone**: Established comprehensive design system with reusable Material 3 Expressive components

**Design Module Structure**: `core:design/`
- `components/cards/` - Card templates (FormCard, ContentCard, ToggleCard, etc.)
- `components/input/` - Input fields (EnhancedTextField, TextAreaWithCounter)
- `components/form/` - Form actions (FixedFormActionsBar)
- `components/selection/` - Selection components (PrioritySelector, OptionSelector)
- `components/typography/` - Typography components (SectionHeader, CompactSectionHeader)
- `components/feedback/` - Feedback components (ErrorMessageCard)
- `components/advanced/` - Advanced components (TagChipsSelector, CollectionDropdown)
- `theme/` - Material 3 theming (colors, typography, shapes)

**🚨 CRITICAL RULE - Card Usage**:
- **ALWAYS** use card templates from `:core:design` module
- **NEVER** create direct `Card(...)` implementations in feature modules
- **MAINTAIN** consistent Material 3 Expressive styling across all cards

**Available Card Templates**:
```kotlin
// FormCard - Card with icon + title header
FormCard(
    headerIcon = Icons.Default.Folder,
    headerTitle = "Organization",
    headerColor = MaterialTheme.colorScheme.primary
) {
    // Card content
}

// ContentCard - Flexible content card with optional header
ContentCard(
    headerIcon = Icons.Default.Link,
    headerTitle = "Link Details"
) {
    // Card content
}

// SectionContentCard - Card with title only (no icon)
SectionContentCard(
    title = "Personal Notes"
) {
    // Card content
}

// ToggleCard - Card with enable/disable functionality
ToggleCard(
    isEnabled = taskEnabled,
    onEnabledChange = { taskEnabled = it },
    title = "Add to Tasks",
    description = "Set priority and due date"
) {
    // Content shown when enabled
}

// InputCard - Card specifically designed for input fields
InputCard(
    title = "Link Information"
) {
    // Input fields
}
```

**Material 3 Expressive Design Standards**:
- **Border**: `BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))`
- **Shape**: `RoundedCornerShape(24.dp)` for cards
- **Elevation**: `CardDefaults.cardElevation(defaultElevation = 4.dp)`
- **Container Color**: `MaterialTheme.colorScheme.surfaceContainerHighest`
- **Padding**: `24.dp` ("spirited padding")
- **Spacing**: `Arrangement.spacedBy(20.dp)` for card content

**Import Guidelines**:
```kotlin
// ✅ CORRECT - Use design module components
import com.greenrobotdev.linklibrary.design.components.cards.FormCard
import com.greenrobotdev.linklibrary.design.components.form.FixedFormActionsBar
import com.greenrobotdev.linklibrary.design.components.selection.PrioritySelector

// ❌ INCORRECT - Never use raw Compose Card for UI components
import androidx.compose.material3.Card // Don't use this for new UI
import androidx.compose.material3.CardDefaults // Don't use this for new UI
```

**Component Migration Status**:
- ✅ `UrlTitleCard` - Migrated to use `ContentCard` template
- ✅ `OrganizationSection` - Uses `FormCard` template
- ✅ `PersonalNotesSection` - Uses `FormCard` template
- ✅ `TaskSection` - Uses `ToggleCard` template
- ✅ All form sections in `AddLinkComponents.kt` - Using design module templates

**Code Quality Improvements**:
- ✅ Fixed all fully qualified `androidx.compose.foundation.layout.Arrangement` references
- ✅ Added proper imports for `Arrangement` across all design components
- ✅ Eliminated redundant qualifier names throughout design module
- ✅ Consistent import organization and code cleanliness

## Key Code Patterns

### Repository Pattern
```kotlin
// Interface in database module
interface LinkRepository {
    suspend fun getLinks(): Flow<Result<List<LinkEntity>>>
    suspend fun getLinksWithTags(): Flow<Result<List<LinkEntity>>>
}

// Implementation with dependencies
class RoomLinkRepository(
    private val databaseBuilder: DatabaseBuilder,
    private val tagRepository: TagRepository,
    private val collectionRepository: CollectionRepository
) : LinkRepository
```

### Reactive Data Loading
```kotlin
// UseCase with reactive data
LaunchedEffect(Unit) {
    tagRepository.getTags().collect { result ->
        val tags = result.getOrElse { emptyList() }.map { it.toTag() }
        state = state.copy(availableTags = tags)
    }
}
```

### Navigation 3.0 Pattern
```kotlin
// Route definitions
@Serializable
sealed class RootScreens : NavKey {
    data class AddLink(val initialUrl: String? = null) : RootScreens()
    data object AddTag : RootScreens()
}

// Navigation with state preservation
class Navigator(val state: NavigationState) {
    fun navigateToNew(route: NavKey) {
        // Remove last route if same type, then add new instance
    }
}
```

## Important File Locations

### Screens & UI
- `app/src/commonMain/kotlin/com/greenrobotdev/linklibrary/screens/`
  - `add/AddLinkScreen.kt` - Main link creation screen
  - `library/LibraryScreen.kt` - Link list/grid view
  - `details/LinkDetailScreen.kt` - Individual link details

### Database Layer
- `database/src/commonMain/kotlin/com/greenrobotdev/linklibrary/database/`
  - `room/` - Room entities and DAOs
  - `repository/` - Repository implementations
  - `di/DataModule.kt` - Dependency injection

### Model Mappings
- `app/src/commonMain/kotlin/com/greenrobotdev/linklibrary/model/`
  - `Link.kt` - Domain model
  - `EntityExtensions.kt` - Entity ↔ Domain conversions

## AGP 9 Specific Considerations

### KMP Module Configuration Pattern
AGP 9 requires specific configuration for KMP modules that target Android:

**BEFORE (AGP 8 - incompatible with AGP 9):**
```kotlin
plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    androidTarget { }
}

android {
    compileSdk = 36
    // Android configuration
}
```

**AFTER (AGP 9 compatible):**
```kotlin
plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library") version "9.0.0"
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}

// Android configuration still required for AGP 9
android {
    namespace = "com.example.module"
    compileSdk = 36
    defaultConfig {
        minSdk = 28
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
```

### Compiler Options Configuration
AGP 9 handles Kotlin compiler options differently:

```kotlin
// AGP 9 style
androidTarget {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

// Note: kotlinOptions removed - use compilerOptions in androidTarget instead
```

### Compose Integration with AGP 9
Compose compiler integration is now managed through the Kotlin Compose plugin:

```kotlin
plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

// Compose compiler configuration
composeCompiler {
    metricsDestination = layout.buildDirectory.dir("compose-compiler-reports")
    reportsDestination = layout.buildDirectory.dir("compose-compiler-reports")
}
```

### Platform-Specific Dependencies
AGP 9 migration maintains all platform-specific dependency patterns:

```kotlin
// commonMain - shared across all platforms
commonMain.dependencies {
    implementation(compose.runtime)
    implementation(project(":core:design"))
}

// androidMain - Android only
androidMain.dependencies {
    implementation(project(":database"))
    implementation(libs.ktor.client.okhttp)
}

// wasmJsMain - WASM specific (limited dependencies)
wasmJsMain.dependencies {
    // Note: Some commonMain dependencies not available for WASM
    // Molecule runtime, ViewModel not available
}
```

## Common Issues & Solutions

### Type Inference with Flow<Result<T>>
Use explicit types and `flow { }` builder when dealing with complex transformations:
```kotlin
override suspend fun getLinksWithTags(): Flow<Result<List<LinkEntity>>> {
    return flow {
        linkDao.getAllLinks().collect { entities ->
            val entitiesWithTags = entities.map { /* transform */ }
            emit(Result.success(entitiesWithTags))
        }
    }
}
```

### Module Dependencies
- `database` module cannot depend on `app` module models
- Use extension functions in `app` module for conversions
- Repository interfaces in `database`, implementations use entities only

### Reactive vs Static Data
- **Static**: `.first()` - gets current value once
- **Reactive**: `.collect { }` - continuously observes changes
- Use reactive for data that updates during user interaction

## Development Notes

### Build Commands
```bash
# Android debug build
./gradlew app:assembleDebug

# Android release build
./gradlew app:assembleRelease

# Desktop application
./gradlew app:desktopApp:installDist

# Run tests
./gradlew test

# Check dependencies
./gradlew app:dependencies

# Clean build
./gradlew clean build

# Compose compiler reports (AGP 9)
./gradlew assembleDebug --composecompiler-metrics
```

### AGP 9 Build Requirements
- **Java**: JDK 17+ required
- **Gradle**: 9.1.0 minimum
- **Android Build Tools**: Use AGP 9 built-in Kotlin support
- **Compose**: Kotlin Compose plugin required for all Compose modules

### Key Dependencies
- Room KMP: `androidx.room:room-runtime`
- Navigation: `androidx.navigation:navigation-compose`
- Koin: `io.insert-koin:koin-core`
- Molecule: `app.cash.molecule:molecule-runtime`
- Compose Multiplatform: `org.jetbrains.compose`
- Kotlin: `2.2.10`
- AGP: `9.0.0`
- KSP: `2.2.10-2.0.2`

## TheLordsOfLinks Design System (Official)

### 🎨 Design System Overview
**TheLordsOfLinks** follows the **Material 3 Expressive Design** system with a distinctive Royal Purple and Gold color scheme. This design system provides the foundational visual language for the entire application, ensuring consistency, accessibility, and brand identity across all platforms.

### 🏆 Brand Identity

#### Brand Philosophy
TheLordsOfLinks embodies the grandeur of medieval nobility combined with modern digital sophistication:
- **Royal Purple**: Represents premium quality, wisdom, and digital excellence
- **Gold**: Signifies value, discovery, and curated content
- **Expressive Design**: Bold aesthetics with spirited interaction patterns

#### Brand Colors (Authoritative Specification)
```kotlin
// Primary Brand Colors
RoyalPurple = Color(0xFF6D28D9)     // Primary brand color
RoyalPurpleDark = Color(0xFF5B21B6)  // Darker variant
RoyalPurpleLight = Color(0xFF8B5CF6) // Lighter variant

// Secondary Brand Colors
Gold = Color(0xFFF59E0B)             // Secondary brand color
GoldDark = Color(0xFFD97706)         // Darker variant
GoldLight = Color(0xFFFBBF24)        // Lighter variant
```

### 🌈 Material 3 Color Scheme Implementation

#### Light Theme Color Mapping
```kotlin
val LightColorScheme = lightColorScheme(
    // Primary - Royal Purple based
    primary = RoyalPurple,                      // #6D28D9
    onPrimary = Color.White,                    // #FFFFFF
    primaryContainer = Color(0xFFEDE9FE),       // Light purple container
    onPrimaryContainer = Color(0xFF2E1065),      // Dark purple text

    // Secondary - Gold based
    secondary = Gold,                           // #F59E0B
    onSecondary = Color(0xFF3B2500),            // Dark brown text
    secondaryContainer = Color(0xFFFFE8B0),     // Light gold container
    onSecondaryContainer = Color(0xFF4A3000),   // Dark brown text

    // Tertiary - Purple variant
    tertiary = Color(0xFF7C3AED),              // Purple accent
    onTertiary = Color.White,                   // #FFFFFF
    tertiaryContainer = Color(0xFFEDE9FE),     // Light purple container
    onTertiaryContainer = Color(0xFF2E1065),    // Dark purple text

    // Surface colors - Light theme optimized
    background = Color(0xFFFAF9FC),             // Very light purple-tinted white
    onBackground = Color(0xFF18181B),           // Near black text
    surface = Color.White,                       // #FFFFFF
    onSurface = Color(0xFF18181B),              // Near black text
    surfaceVariant = Color(0xFFEDEAF2),          // Light gray-purple
    onSurfaceVariant = Color(0xFF625F69),        // Medium gray text

    // Utility colors
    outline = Color(0xFF79747E),                // Medium gray outline
    outlineVariant = Color(0xFFD0CBD5),         // Light outline
    inverseSurface = Color(0xFF302E33),         // Dark gray
    inverseOnSurface = Color(0xFFF5F0F7),      // Light purple-tinted white
    inversePrimary = Color(0xFFCBB5FF),        // Light purple
    scrim = Color.Black                         // #000000
)
```

#### Dark Theme Color Mapping
```kotlin
val DarkColorScheme = darkColorScheme(
    // Primary - Light purple for dark theme
    primary = Color(0xFF8B5CF6),                // Light purple for visibility
    onPrimary = Color(0xFF2E1065),             // Dark purple text
    primaryContainer = Color(0xFF2E1065),       // Dark purple container
    onPrimaryContainer = Color(0xFFE9DDFF),     // Light purple text

    // Secondary - Light gold for dark theme
    secondary = Color(0xFFFBBF24),             // Light gold for visibility
    onSecondary = Color(0xFF3B2500),            // Dark brown text
    secondaryContainer = Color(0xFF5C4300),    // Dark gold container
    onSecondaryContainer = Color(0xFFFFE8B0),   // Light gold text

    // Tertiary - Light purple accent
    tertiary = Color(0xFFA78BFA),              // Light purple accent
    onTertiary = Color(0xFF32105F),             // Dark purple text
    tertiaryContainer = Color(0xFF4C1D95),     // Dark purple container
    onTertiaryContainer = Color(0xFFE9DDFF),    // Light purple text

    // Surface colors - Dark theme optimized
    background = Color(0xFF100B1A),             // Very dark purple-tinted
    onBackground = Color(0xFFF5F3FF),          // Light purple-tinted white
    surface = Color(0xFF1C1428),              // Dark purple-tinted surface
    onSurface = Color(0xFFF5F3FF),            // Light purple-tinted white
    surfaceVariant = Color(0xFF2A2135),        // Dark gray-purple
    onSurfaceVariant = Color(0xFFA1A1AA),      // Medium gray text

    // Utility colors
    outline = Color(0xFF938D9B),               // Medium gray outline
    outlineVariant = Color(0xFF403747),        // Dark outline
    inverseSurface = Color(0xFFF5F3FF),       // Light purple-tinted white
    inverseOnSurface = Color(0xFF302E33),      // Dark gray
    inversePrimary = Color(0xFF6D28D9),        // Royal Purple
    scrim = Color.Black                        // #000000
)
```

### 📐 Design Standards & Specifications

#### Material 3 Expressive Design Principles
TheLordsOfLinks follows Material 3 Expressive Design with these core principles:

1. **Spirited Padding**: 24dp padding for major components (cards, containers)
2. **Expressive Corners**: 24dp corner radius for cards, 8dp for compact elements
3. **Atmospheric Elevation**: Subtle borders and elevation for depth perception
4. **Semantic Color Usage**: Proper use of primary, secondary, tertiary color roles
5. **Dynamic Theming**: Seamless light/dark theme transitions
6. **Accessibility First**: WCAG AA compliant color contrast ratios

#### Component Standards (Mandatory)

##### Icon System (Standardized)
```kotlin
// ✅ CORRECT - Always use AppIcon for consistent icon backgrounds
AppIcon(
    icon = Icons.Default.Folder,
    backgroundColor = MaterialTheme.colorScheme.primary,
    variant = IconBackgroundVariant.Regular
)

// Available variants:
// - IconBackgroundVariant.Regular (32dp container, 18dp icon, 8dp corners)
// - IconBackgroundVariant.Compact (24dp container, 14dp icon, 6dp corners)
```

##### Card System (Mandatory Templates)
```kotlin
// ✅ CORRECT - Always use design module card templates
import com.greenrobotdev.linklibrary.design.components.cards.*

// FormCard - Standard form with icon + header
FormCard(
    headerIcon = Icons.Default.Link,
    headerTitle = "Link Details",
    headerColor = MaterialTheme.colorScheme.primary
) { /* content */ }

// ContentCard - Flexible content with optional header
ContentCard(
    headerIcon = Icons.Default.Folder,
    headerTitle = "Organization"
) { /* content */ }

// ToggleCard - Enable/disable functionality
ToggleCard(
    isEnabled = taskEnabled,
    onEnabledChange = { taskEnabled = it },
    title = "Add to Tasks",
    activeColor = MaterialTheme.colorScheme.primary
) { /* content when enabled */ }

// ❌ FORBIDDEN - Never use raw Card() implementations
import androidx.compose.material3.Card // Do not use
```

##### Typography Standards
```kotlin
// Header Typography
titleLarge - Form card headers, section titles (fontWeight: SemiBold)
titleMedium - Subsection headers, card titles (fontWeight: SemiBold)

// Body Typography
bodyMedium - Primary content text
bodySmall - Secondary content, descriptions, helper text

// Label Typography
labelMedium - Input field labels, button text (fontWeight: Medium)
```

##### Input Field Standards
```kotlin
// ✅ CORRECT - Use standardized input components
import com.greenrobotdev.linklibrary.design.components.textfield.*

AppTextFieldEnhanced(
    value = url,
    onValueChange = { url = it },
    label = "URL",
    placeholder = "https://example.com",
    leadingIcon = Icons.Default.Link
)

AppTextFieldWithCharacterLimit(
    value = notes,
    onValueChange = { notes = it },
    maxLength = 200,
    placeholder = "Add personal notes..."
)
```

### 🎯 Design Implementation Guidelines

#### Color Usage Rules
1. **Primary Colors**: Use for main actions, active states, and key interactive elements
2. **Secondary Colors**: Use for complementary actions, emphasis, and highlight states
3. **Tertiary Colors**: Use for accents, alternative actions, and visual variety
4. **Surface Colors**: Use for containers, cards, and background layers
5. **Semantic Colors**: Always use Material 3 semantic colors (error, success, warning)

#### Component Styling Standards
```kotlin
// Border Standard (Mandatory)
BorderStroke(
    width = 1.dp,
    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
)

// Shape Standards
cards: RoundedCornerShape(24.dp)     // Expressive corners
compact: RoundedCornerShape(16.dp)    // Medium corners
buttons: RoundedCornerShape(12.dp)    // Standard Material 3

// Elevation Standards
cards: CardDefaults.cardElevation(defaultElevation = 4.dp)
elevated: CardDefaults.cardElevation(defaultElevation = 6.dp)
subtle: CardDefaults.cardElevation(defaultElevation = 2.dp)

// Spacing Standards
cards: Modifier.padding(24.dp)        // Spirited padding
compact: Modifier.padding(16.dp)       // Standard padding
content: Arrangement.spacedBy(20.dp)  // Content spacing
```

### ✅ Implementation Verification

#### Design System Files (Authoritative Locations)
```kotlin
// Theme Implementation
core/design/src/commonMain/kotlin/com/greenrobotdev/linklibrary/design/theme/Color.kt
// - Contains official LightColorScheme and DarkColorScheme
// - RoyalPurple and Gold brand color definitions
// - Complete Material 3 color mappings

// Component Library
core/design/src/commonMain/kotlin/com/greenrobotdev/linklibrary/design/components/
// ├── cards/ - Standardized card templates (FormCard, ContentCard, ToggleCard)
// ├── icon/ - AppIcon component with variants
// ├── textfield/ - Enhanced text field components
// ├── button/ - Standardized button components
// ├── selection/ - Selection components (PrioritySelector, OptionSelector)
// └── feedback/ - Feedback components (ErrorMessageCard)

// Theme Application
core/design/src/commonMain/kotlin/com/greenrobotdev/linklibrary/design/theme/Theme.kt
// - MaterialTheme configuration
// - LightTheme and DarkTheme composables
// - Typography and shape definitions
```

#### Usage Validation (Current Status)
✅ **Color System**: Fully implemented in Color.kt with exact Royal Purple (#6D28D9) and Gold (#F59E0B) specifications
✅ **Icon Standardization**: AppIcon component with Regular and Compact variants used across all screens
✅ **Card Templates**: All screens migrated to use FormCard, ContentCard, ToggleCard templates
✅ **Component Library**: Comprehensive Material 3 Expressive components in core:design module
✅ **Theme Support**: Complete light and dark theme implementations with proper color mappings
✅ **Design Consistency**: All screens follow TheLordsOfLinks design standards

### 🚀 Development Guidelines

#### When Adding New Screens
1. **Always** use card templates from `:core:design` module
2. **Never** create custom Card() implementations
3. **Always** use AppIcon for icon backgrounds
4. **Always** use Material 3 semantic colors (primary, secondary, tertiary)
5. **Always** test both light and dark themes
6. **Never** hardcode brand colors - use MaterialTheme.colorScheme

#### When Modifying Existing Screens
1. **Check** if design module component exists before creating custom implementations
2. **Migrate** any raw Card() uses to appropriate card templates
3. **Standardize** any custom icon backgrounds to AppIcon
4. **Verify** color usage follows Material 3 patterns
5. **Test** visual consistency across both themes

#### Design System Compliance Checklist
- [ ] Uses card templates from design module (no raw Card())
- [ ] Uses AppIcon for all icon backgrounds
- [ ] Uses MaterialTheme.colorScheme for colors (no hardcoded values)
- [ ] Follows Material 3 Expressive spacing and sizing standards
- [ ] Tests properly in both light and dark themes
- [ ] Maintains WCAG AA accessibility standards
- [ ] Follows TheLordsOfLinks brand color usage patterns

### 📚 Design System Authority

This Design System section is the **authoritative source** for TheLordsOfLinks visual design standards. All design decisions must align with these specifications. When conflicts arise between this documentation and existing code, this documentation takes precedence.

**Implementation Authority**: The color system in `Color.kt` represents the definitive implementation of these design standards.

**Last Updated**: 2025-08-31
**Design System Version**: Material 3 Expressive - TheLordsOfLinks Edition

## Session Context Preservation
This file helps maintain context across Claude Code sessions. Update it when:
- Major architectural decisions are made
- Significant bugs are fixed
- New patterns are established
- Important code locations change
- Design system standards are updated or modified

Last Updated: 2025-08-31
Session Focus: Established TheLordsOfLinks Design System as official project documentation with Royal Purple (#6D28D9) and Gold (#F59E0B) brand colors, comprehensive Material 3 Expressive standards, and component library guidelines