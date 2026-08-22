# Architecture Standardization Skill

## Overview

This skill provides comprehensive tools and templates for standardizing all screens in the LinkLibrary KMP project to follow the MVVM+UseCase+Molecule architecture pattern consistently.

## Architecture Pattern

The LinkLibrary project uses a modern, reactive architecture pattern that combines:

- **MVVM (Model-View-ViewModel)**: Clear separation of concerns
- **UseCase Pattern**: Business logic encapsulation
- **Molecule**: Compose-first state management
- **Koin**: Dependency injection
- **Navigation 3**: Type-safe navigation with serialization support

### Component Architecture Flow

```
Screen.kt (UI)
    ↓ viewModel() factory
ViewModel.kt
    ↓ moleculeFlow()
UseCase.kt (Business Logic)
    ↓ Repository
    ↓ State Updates
    ↓ collectAsState()
Screen.kt (Reactive UI)
```

## Skill Contents

### 1. Templates (`templates/`)
Ready-to-use code generation templates for all architecture components:

- **StateModels.kt.template**: State and event definitions
- **UseCase.kt.template**: Business logic layer
- **ViewModel.kt.template**: Presentation layer with Molecule
- **Screen.kt.template**: UI layer with navigation integration

### 2. Analysis Tools (`analysis/`)
Tools to verify architecture compliance:

- **checklist.md**: Comprehensive architecture compliance checklist
- **validator.md**: Pattern validation rules and detection logic

### 3. Refactoring Guides (`refactoring/`)
Guides for migrating existing screens:

- **migration-guide.md**: Step-by-step migration instructions
- **best-practices.md**: Common patterns and solutions

## Usage

### Generate New Screen Architecture

To create a new screen with proper architecture:

1. **Choose your screen type**:
   - List Screen (e.g., Library, Home)
   - Detail Screen (e.g., LinkDetail)
   - Form Screen (e.g., AddLink, AddCollection)
   - Settings Screen

2. **Use the templates**:
   - Copy `templates/StateModels.kt.template` and customize
   - Copy `templates/UseCase.kt.template` and implement business logic
   - Copy `templates/ViewModel.kt.template` and configure dependencies
   - Copy `templates/Screen.kt.template` and build UI

3. **Register in Navigation**:
   - Add screen key to `NavigationState.kt`
   - Add route in `RootScreen.kt`
   - Register in Koin module if needed

### Verify Existing Screen

Use the checklist to ensure a screen follows the architecture:

```bash
# Review the compliance checklist
cat .claude/skills/architecture-standardize/analysis/checklist.md
```

### Refactor Non-Compliant Screen

Follow the migration guide to update screens:

1. Read `refactoring/migration-guide.md`
2. Follow the step-by-step process
3. Use templates as reference
4. Verify with checklist

## Current Project Status

### Compliant Screens ✓

- **HomeScreen**: Full MVVM+UseCase+Molecule implementation
- **AddLinkScreen**: Full implementation with form validation
- **LibraryScreen**: Full implementation with search/filter

### Screens Needing Architecture

- **LinkDetailScreen**: Missing ViewModel/UseCase/StateModels
- **CollectionsScreen**: Missing ViewModel/UseCase/StateModels
- **SettingsScreen**: Missing ViewModel/UseCase/StateModels
- **AddCollectionScreen**: Missing ViewModel/UseCase/StateModels
- **AIAssistantDemoScreen**: Has UseCase, missing ViewModel/StateModels

## Architecture Principles

### 1. Single Responsibility
Each component has one clear purpose:
- **StateModels**: Define state shape and events
- **UseCase**: Business logic and state management
- **ViewModel**: Presentation layer and lifecycle management
- **Screen**: UI rendering and user interactions

### 2. Unidirectional Data Flow
```
User Action → Event → ViewModel → UseCase → State Update → UI Recomposition
```

### 3. Immutability
- State is immutable (using `@Serializable` data classes)
- State updates create new instances via `copy()`
- Events are sealed interfaces for type safety

### 4. Dependency Injection
- ViewModels inject repositories via Koin
- UseCases receive dependencies as parameters
- No hardcoded dependencies

### 5. Reactive State Management
- Molecule provides Compose-based state machine
- `collectAsState()` for reactive UI updates
- `stateIn()` for lifecycle-aware state sharing

## File Naming Conventions

All screen components follow consistent naming:

```
screens/
├── {feature}/
│   ├── {Feature}StateModels.kt    # State + Events
│   ├── {Feature}UseCase.kt         # Business logic
│   ├── {Feature}ViewModel.kt       # Presentation layer
│   └── {Feature}Screen.kt          # UI layer
```

Example:
```
screens/
├── home/
│   ├── HomeStateModels.kt
│   ├── HomeUseCase.kt
│   ├── HomeViewModel.kt
│   └── HomeScreen.kt
```

## Integration Points

### Navigation 3
- State classes must be `@Serializable`
- Register in `NavigationState.kt` serializers module
- Use `NavKey` as parameter in Screen composables

### Koin DI
- ViewModels are `KoinComponent`
- Inject repositories with `by inject()`
- Define ViewModels in Koin modules if custom factory needed

### Molecule
- Use `RecompositionMode.Immediate`
- Wrap UseCase in `moleculeFlow()`
- Convert to StateFlow with `stateIn()`

## Common Patterns

### Loading States
```kotlin
@Serializable
data class MyState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<Item> = emptyList()
)
```

### Form Validation
```kotlin
@Serializable
data class FormState(
    val isFormValid: Boolean = false,
    // validation logic in UseCase
)
```

### Single Event Handling
```kotlin
sealed interface MyEvent {
    object ShowSuccess : Event  // Show toast/snackbar
    object NavigateBack : Event  // Trigger navigation
}
```

## Best Practices

1. **Keep UseCase Pure**: No Android dependencies in UseCase
2. **ViewModels are Thin**: Just Molecule wrapping and event forwarding
3. **Screen is Declarative**: All UI as function of state
4. **Events are Actions**: User intents only, no state updates
5. **State is Source of Truth**: UI derived entirely from state

## Troubleshooting

### State Not Updating
- Check `LaunchedEffect` is collecting events
- Verify `mutableStateOf` is used in UseCase
- Ensure `collectAsState()` in Screen

### Navigation Issues
- Verify `@Serializable` on State class
- Check registration in `NavigationState.kt`
- Use unique `routeKey` for each screen instance

### Dependency Injection Failures
- Confirm repository is defined in Koin module
- Check `KoinComponent` is implemented
- Verify `by inject()` syntax

## Contributing

When updating this skill:

1. Keep templates synchronized with actual implementations
2. Update checklist when patterns evolve
3. Document new patterns in best-practices.md
4. Test templates against compliant screens

## Resources

- [Molecule Documentation](https://cashapp.github.io/molecule/)
- [Navigation 3 for Compose](https://developer.android.com/guide/navigation/compose)
- [Koin DI Framework](https://insert-koin.io/)
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)
