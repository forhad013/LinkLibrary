# Architecture Standardization Skill - Complete Summary

**Skill Version**: 1.0
**Created**: August 22, 2026
**Project**: LinkLibrary KMP
**Architecture**: MVVM + UseCase + Molecule

---

## Skill Overview

This skill provides a comprehensive toolkit for standardizing all screens in the LinkLibrary KMP project to follow the MVVM+UseCase+Molecule architecture pattern consistently.

### What This Skill Provides

1. **Code Generation Templates** - Ready-to-use templates for all architecture components
2. **Analysis Tools** - Checklist and validator for architecture compliance
3. **Refactoring Guides** - Step-by-step migration instructions and best practices
4. **Documentation** - Comprehensive guides and examples

---

## Skill Structure

```
architecture-standardize/
├── README.md                           # Main documentation
├── QUICKSTART.md                       # Quick start guide
├── SKILL_SUMMARY.md                   # This file
│
├── templates/                         # Code Generation
│   ├── StateModels.kt.template       # State + Events definition
│   ├── UseCase.kt.template           # Business logic layer
│   ├── ViewModel.kt.template          # Presentation layer
│   └── Screen.kt.template             # UI layer
│
├── analysis/                          # Analysis Tools
│   ├── checklist.md                  # 45-point compliance checklist
│   └── validator.md                  # Automated validation rules
│
└── refactoring/                       # Refactoring Guides
    ├── migration-guide.md            # Step-by-step migration
    └── best-practices.md             # Proven patterns
```

---

## Architecture Pattern

### The 4 Components

Every compliant screen requires exactly 4 files:

#### 1. StateModels (`{Feature}StateModels.kt`)
- Defines state shape with `@Serializable` data class
- Defines events with sealed interface
- Ensures navigation-safe state

#### 2. UseCase (`{Feature}UseCase.kt`)
- `@Composable` function managing business logic
- Uses `remember { mutableStateOf() }` for state
- Uses `LaunchedEffect` for event collection
- Returns current state

#### 3. ViewModel (`{Feature}ViewModel.kt`)
- Extends `ViewModel()` and `KoinComponent`
- Uses `moleculeFlow(RecompositionMode.Immediate)` with UseCase
- Uses `stateIn(viewModelScope, SharingStarted.Lazily, initialState)`
- Provides event emission methods

#### 4. Screen (`{Feature}Screen.kt`)
- `@Composable` function with `NavKey` parameter
- Creates ViewModel using `viewModel()` factory
- Collects state via `collectAsState()`
- Emits events via ViewModel methods

### Architecture Flow

```
User Action
    ↓
Screen.kt (UI Layer)
    ↓ viewModel::onEvent()
ViewModel.kt (Presentation Layer)
    ↓ eventsFlow.emit()
UseCase.kt (Business Logic Layer)
    ↓ Repository Call
    ↓ State Update (copy())
    ↓ collectAsState()
Screen.kt (Reactive UI Update)
```

---

## Current Project Status

### Compliant Screens (90%+ Architecture Score)

**HomeScreen** ✓
- File: `/app/src/commonMain/kotlin/com/greenrobotdev/linklibrary/screens/home/`
- Components: HomeStateModels, HomeUseCase, HomeViewModel, HomeScreen
- Status: Fully compliant
- Features: List display, pull-to-refresh, favorite toggling

**AddLinkScreen** ✓
- File: `/app/src/commonMain/kotlin/com/greenrobotdev/linklibrary/screens/add/`
- Components: AddLinkStateModels, AddLinkUseCase, AddLinkViewModel, AddLinkScreen
- Status: Fully compliant
- Features: Form validation, URL parsing, AI integration

**LibraryScreen** ✓
- File: `/app/src/commonMain/kotlin/com/greenrobotdev/linklibrary/screens/library/`
- Components: LibraryStateModels, LibraryUseCase, LibraryViewModel, LibraryScreen
- Status: Fully compliant
- Features: Search, filter, collections

### Screens Requiring Architecture Implementation

**LinkDetailScreen** (0% - Missing all components)
- Current: Basic implementation without architecture
- Required: Create StateModels, UseCase, ViewModel
- Priority: High (detail screens are common)

**CollectionsScreen** (0% - Missing all components)
- Current: Not implemented
- Required: Full architecture implementation
- Priority: Medium

**SettingsScreen** (0% - Missing all components)
- Current: Basic settings UI
- Required: Create StateModels, UseCase, ViewModel
- Priority: Low

**AddCollectionScreen** (0% - Missing all components)
- Current: Not implemented
- Required: Full architecture implementation
- Priority: Medium

**AIAssistantDemoScreen** (33% - Has UseCase only)
- Current: Has UseCase, missing ViewModel/StateModels
- Required: Create ViewModel, StateModels
- Priority: Low (demo screen)

---

## Usage Guide

### For New Screens

**Time Estimate**: 15-30 minutes

1. **Copy Templates**
   ```bash
   cp .claude/skills/architecture-standardize/templates/*.template screens/myfeature/
   ```

2. **Customize StateModels**
   - Define state properties
   - Define events
   - Add `@Serializable` annotation

3. **Implement UseCase**
   - Set up state management
   - Implement initial data loading
   - Handle events
   - Return state

4. **Configure ViewModel**
   - Inject repositories
   - Set up moleculeFlow
   - Create event emitters

5. **Build Screen**
   - Create ViewModel with factory
   - Collect state
   - Build UI
   - Handle navigation

6. **Register Navigation**
   - Add to NavigationState.kt
   - Define route

### For Existing Screens

**Time Estimate**: 1-2 hours

1. **Assessment** (5 min)
   - Review current implementation
   - Identify missing components
   - Determine complexity

2. **Create StateModels** (10 min)
   - Extract state properties
   - Define events
   - Create file

3. **Create UseCase** (20 min)
   - Extract business logic
   - Implement event handling
   - Test in isolation

4. **Create ViewModel** (10 min)
   - Wrap UseCase with Molecule
   - Inject dependencies
   - Create event emitters

5. **Update Screen** (15 min)
   - Remove state management
   - Connect to ViewModel
   - Delegate to View composable

6. **Integration** (5 min)
   - Register navigation
   - Configure Koin
   - Test navigation

7. **Testing** (10 min)
   - Verify functionality
   - Test edge cases
   - Validate compliance

### For Checking Compliance

**Time Estimate**: 5-10 minutes per screen

1. **Use Checklist**
   - Open `analysis/checklist.md`
   - Go through 45 validation points
   - Calculate compliance score

2. **Identify Issues**
   - List missing components
   - Note pattern violations
   - Document improvements needed

3. **Plan Fixes**
   - Prioritize by severity
   - Estimate effort
   - Schedule refactoring

---

## Key Features of the Skill

### 1. Template-Based Code Generation

All templates include:
- Placeholder instructions (e.g., `{{PACKAGE}}`, `{{FEATURE_NAME}}`)
- Inline documentation and comments
- Multiple complete examples
- Common patterns for different screen types
- Best practices guidance

### 2. Comprehensive Compliance Checking

The checklist covers:
- File structure (5 points)
- StateModels compliance (10 points)
- UseCase compliance (12 points)
- ViewModel compliance (10 points)
- Screen compliance (8 points)
- Integration & best practices (10 points)
- Common patterns (5 points)

**Total: 45 points** (90%+ = Compliant)

### 3. Automated Validation Rules

The validator provides:
- Pattern detection using regex
- File existence checks
- Import validation
- Architecture flow verification
- Scoring algorithm
- CI/CD integration examples

### 4. Step-by-Step Migration

The migration guide includes:
- Phase-by-phase instructions
- Before/after code examples
- Common scenarios with time estimates
- Anti-patterns to avoid
- Troubleshooting guide

### 5. Proven Best Practices

Best practices document:
- State management principles
- Event handling patterns
- Loading states strategies
- Error handling approaches
- Form validation techniques
- List screen patterns
- Detail screen patterns
- Performance optimization
- Testing strategies
- Common pitfalls

---

## Architecture Principles

### 1. Single Responsibility
Each component has one clear purpose:
- **StateModels**: Define state and events
- **UseCase**: Business logic and state management
- **ViewModel**: Presentation layer and lifecycle
- **Screen**: UI rendering and user interactions

### 2. Unidirectional Data Flow
```
User Action → Event → ViewModel → UseCase → State Update → UI Recomposition
```

### 3. Immutability
- State is immutable (`@Serializable` data class)
- Updates create new instances via `copy()`
- Events are sealed interfaces

### 4. Dependency Injection
- ViewModels inject repositories via Koin
- UseCases receive dependencies as parameters
- No hardcoded dependencies

### 5. Reactive State Management
- Molecule provides Compose-based state machine
- `collectAsState()` for reactive UI updates
- `stateIn()` for lifecycle-aware state sharing

---

## Integration Points

### Navigation 3
- State classes must be `@Serializable`
- Registered in `NavigationState.kt` serializers module
- Use `NavKey` as parameter in Screen composables

### Koin DI
- ViewModels implement `KoinComponent`
- Inject repositories with `by inject()`
- Define ViewModels in Koin modules if custom factory needed

### Molecule
- Use `RecompositionMode.Immediate`
- Wrap UseCase in `moleculeFlow()`
- Convert to StateFlow with `stateIn()`

---

## File Organization

### Standard Structure
```
screens/
├── {feature}/
│   ├── {Feature}StateModels.kt    # State + Events
│   ├── {Feature}UseCase.kt        # Business Logic
│   ├── {Feature}ViewModel.kt      # Presentation
│   └── {Feature}Screen.kt        # UI
```

### Example
```
screens/
├── home/
│   ├── HomeStateModels.kt
│   ├── HomeUseCase.kt
│   ├── HomeViewModel.kt
│   └── HomeScreen.kt
```

---

## Naming Conventions

### Files
- `{Feature}StateModels.kt` - State and events
- `{Feature}UseCase.kt` - Business logic
- `{Feature}ViewModel.kt` - Presentation layer
- `{Feature}Screen.kt` - UI layer

### Classes/Functions
- `FeatureState` - State data class
- `FeatureEvent` - Event sealed interface
- `FeatureUseCase()` - UseCase composable function
- `FeatureViewModel` - ViewModel class
- `FeatureScreen()` - Screen composable function
- `FeatureView()` - Private View composable (optional)

### Properties
- `states` - StateFlow in ViewModel
- `eventsFlow` - Event flow in ViewModel
- `initialState` - Initial state in ViewModel
- `isLoading` - Loading state
- `error` - Error message

---

## Common Patterns

### List Screen Pattern
```kotlin
@Serializable
data class ListState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val items: List<Item> = emptyList(),
    val searchQuery: String = ""
)
```

### Form Screen Pattern
```kotlin
@Serializable
data class FormState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val field1: String = "",
    val field2: String = "",
    val isFormValid: Boolean = false,
    val success: Boolean = false
)
```

### Detail Screen Pattern
```kotlin
@Serializable
data class DetailState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val itemId: String? = null,
    val item: Item? = null
)
```

---

## Quality Metrics

### Compliance Scoring

**Compliant** (90%+ / 41-45 points)
- All critical components present
- Follows architecture pattern exactly
- Ready for production

**Needs Improvement** (70-89% / 32-40 points)
- Minor deviations from pattern
- Some best practices missing
- Needs small adjustments

**Non-Compliant** (<70% / 0-31 points)
- Major components missing
- Significant architectural deviations
- Requires refactoring

### Current Project Status

- **Total Screens**: 8
- **Compliant Screens**: 3 (37.5%)
- **Screens Needing Work**: 5 (62.5%)
- **Target**: 100% compliance

---

## Maintenance & Updates

### When to Update This Skill

1. **Architecture Changes**: If the core pattern evolves
2. **New Patterns**: When proven patterns emerge
3. **Tool Updates**: If Molecule, Navigation, or Koin update
4. **Project Needs**: When LinkLibrary requirements change

### How to Update

1. Update templates to match new patterns
2. Add new examples to best practices
3. Update checklist with new validation points
4. Add migration notes for breaking changes
5. Test templates against compliant screens

---

## Related Documentation

### Project-Specific
- `GOOGLE_STITCH_INTEGRATION_GUIDE.md` - Google AI integration
- `README.md` - Project overview
- Koin modules - Dependency injection configuration

### External Resources
- [Molecule Documentation](https://cashapp.github.io/molecule/)
- [Navigation 3 for Compose](https://developer.android.com/guide/navigation/compose)
- [Koin DI Framework](https://insert-koin.io/)
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)

---

## Success Criteria

This skill is successful when:

1. **All new screens** use the templates
2. **All existing screens** pass the checklist (90%+)
3. **Code reviews** use the checklist
4. **Refactoring** follows the migration guide
5. **Best practices** are consistently applied

---

## Quick Reference

### Essential Commands
```bash
# Validate a screen
cat .claude/skills/architecture-standardize/analysis/checklist.md

# Generate new screen
cp .claude/skills/architecture-standardize/templates/*.template screens/myfeature/

# Learn patterns
cat .claude/skills/architecture-standardize/refactoring/best-practices.md

# Migrate screen
cat .claude/skills/architecture-standardize/refactoring/migration-guide.md
```

### Essential Files
- `README.md` - Start here for overview
- `QUICKSTART.md` - 5-minute introduction
- `analysis/checklist.md` - Validation tool
- `templates/` - Code generation
- `refactoring/migration-guide.md` - Refactoring instructions

---

## Conclusion

This architecture standardization skill provides:

✓ **Templates** for quick code generation
✓ **Checklist** for architecture validation
✓ **Guides** for systematic refactoring
✓ **Best practices** for proven solutions
✓ **Documentation** for comprehensive understanding

**Goal**: 100% architecture compliance across all screens in LinkLibrary.

**Result**: Consistent, maintainable, testable codebase.

---

**Last Updated**: August 22, 2026
**Version**: 1.0
**Status**: Ready for Use
