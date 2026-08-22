# Architecture Standardization Quick Start

Get started with the architecture standardization skill in 5 minutes.

## What is this skill?

A comprehensive toolkit for ensuring all screens in LinkLibrary follow the MVVM+UseCase+Molecule architecture pattern consistently.

## Quick Navigation

- **New to architecture?** Start with [README.md](README.md)
- **Need to create a screen?** Use [templates/](templates/)
- **Checking a screen?** Use [analysis/checklist.md](analysis/checklist.md)
- **Refactoring a screen?** Follow [refactoring/migration-guide.md](refactoring/migration-guide.md)
- **Best practices?** See [refactoring/best-practices.md](refactoring/best-practices.md)

---

## 5-Minute Overview

### The Architecture Pattern

```
Screen.kt (UI)
    ↓ viewModel() factory
ViewModel.kt (Presentation)
    ↓ moleculeFlow()
UseCase.kt (Business Logic)
    ↓ Repository
    ↓ State Updates
    ↓ collectAsState()
Screen.kt (Reactive UI)
```

### The 4 Components

Every screen needs exactly 4 files:

1. **`{Feature}StateModels.kt`** - State shape and events
2. **`{Feature}UseCase.kt`** - Business logic
3. **`{Feature}ViewModel.kt`** - Presentation layer
4. **`{Feature}Screen.kt`** - UI layer

### Key Principles

- **Single Responsibility**: Each component has one job
- **Unidirectional Flow**: User → Event → State → UI
- **Immutability**: State never changes, only replaced
- **Reactive**: UI updates automatically when state changes

---

## Quick Tasks

### Task 1: Check if a Screen is Compliant

Use the checklist:

```bash
# Open the checklist
cat .claude/skills/architecture-standardize/analysis/checklist.md

# Go through the 45-point checklist
# Calculate your score
# Determine if screen is Compliant (90%+), Needs Work (70-89%), or Non-Compliant (<70%)
```

**Time**: 5-10 minutes per screen

---

### Task 2: Generate a New Screen Architecture

Use the templates:

1. Copy `templates/StateModels.kt.template` → `screens/{feature}/{Feature}StateModels.kt`
2. Copy `templates/UseCase.kt.template` → `screens/{feature}/{Feature}UseCase.kt`
3. Copy `templates/ViewModel.kt.template` → `screens/{feature}/{Feature}ViewModel.kt`
4. Copy `templates/Screen.kt.template` → `screens/{feature}/{Feature}Screen.kt`
5. Replace placeholders with your feature-specific code
6. Register in `NavigationState.kt`

**Time**: 15-30 minutes

---

### Task 3: Refactor an Existing Screen

Follow the migration guide:

1. **Assess** current architecture (5 min)
2. **Create StateModels** (10 min)
3. **Create UseCase** (20 min)
4. **Create ViewModel** (10 min)
5. **Update Screen** (15 min)
6. **Integrate** navigation (5 min)
7. **Test** (10 min)

**Time**: 1-2 hours per screen

---

## Common Scenarios

### "I need to create a new screen"

→ Use the templates. Start with `templates/StateModels.kt.template` and follow the placeholder instructions.

### "I have a screen without ViewModel/UseCase"

→ Follow the migration guide in `refactoring/migration-guide.md`. It's a step-by-step process.

### "I want to check if my screen follows the architecture"

→ Use the checklist in `analysis/checklist.md`. It has 45 validation points.

### "I'm not sure how to implement [specific feature]"

→ Check `refactoring/best-practices.md` for proven patterns and solutions.

---

## Project Status

### Compliant Screens ✓
- HomeScreen
- AddLinkScreen
- LibraryScreen

### Screens Needing Architecture
- LinkDetailScreen
- CollectionsScreen
- SettingsScreen
- AddCollectionScreen
- AIAssistantDemoScreen

---

## File Structure

```
.claude/skills/architecture-standardize/
├── README.md                    # Architecture overview and documentation
├── QUICKSTART.md               # This file
├── templates/                  # Code generation templates
│   ├── StateModels.kt.template
│   ├── UseCase.kt.template
│   ├── ViewModel.kt.template
│   └── Screen.kt.template
├── analysis/                   # Analysis and validation tools
│   ├── checklist.md           # 45-point compliance checklist
│   └── validator.md           # Automated validation rules
└── refactoring/               # Refactoring guidance
    ├── migration-guide.md     # Step-by-step migration instructions
    └── best-practices.md      # Proven patterns and solutions
```

---

## Quick Examples

### StateModels Example

```kotlin
@Serializable
data class HomeState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val links: List<Link>? = null
)

sealed interface HomeEvent {
    object Refresh : HomeEvent
    data class ToggleFavorite(val id: String) : HomeEvent
}
```

### UseCase Example

```kotlin
@Composable
fun HomeUseCase(
    initialState: HomeState,
    events: Flow<HomeEvent>,
    linkRepository: LinkRepository
): HomeState {
    var state by remember { mutableStateOf(initialState) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is HomeEvent.Refresh -> { /* handle refresh */ }
            }
        }
    }

    return state
}
```

### ViewModel Example

```kotlin
class HomeViewModel : ViewModel(), KoinComponent {
    private val eventsFlow: MutableSharedFlow<HomeEvent> = MutableSharedFlow(5)
    private val linkRepository: LinkRepository by inject()

    val states by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            HomeUseCase(initialState, eventsFlow, linkRepository)
        }.stateIn(viewModelScope, SharingStarted.Lazily, initialState)
    }

    fun onRefresh() { viewModelScope.launch { eventsFlow.emit(HomeEvent.Refresh) } }
}
```

### Screen Example

```kotlin
@Composable
fun HomeScreen(
    routeKey: NavKey,
    onNavigateToDetail: (String) -> Unit
) {
    val viewModel: HomeViewModel = viewModel(key = routeKey.toString()) { HomeViewModel() }
    val state by viewModel.states.collectAsState()

    HomeView(
        state = state,
        onRefresh = viewModel::onRefresh,
        onLinkClick = onNavigateToDetail
    )
}
```

---

## Key Commands

### Validate Architecture
```bash
# Check a specific screen
# Use analysis/checklist.md

# Validate all screens
# Use analysis/validator.md rules
```

### Generate New Screen
```bash
# Copy templates
cp .claude/skills/architecture-standardize/templates/*.template screens/myfeature/

# Replace placeholders
# Follow template instructions
```

---

## Learning Path

### Beginner (New to Architecture)
1. Read `README.md` - Understand the architecture
2. Study `templates/` - See the patterns
3. Check existing compliant screens (Home, AddLink, Library)
4. Try creating a simple screen using templates

### Intermediate (Familiar with Architecture)
1. Use `analysis/checklist.md` to verify screens
2. Study `refactoring/best-practices.md` for patterns
3. Practice refactoring a simple screen
4. Learn to handle complex scenarios (forms, lists, details)

### Advanced (Architecture Expert)
1. Contribute to improving templates
2. Add new patterns to `best-practices.md`
3. Help refactor non-compliant screens
4. Create validation scripts

---

## Troubleshooting

### "My screen doesn't update"

**Solution**: Check that you're using `collectAsState()` in Screen and `mutableStateOf` in UseCase.

### "Navigation doesn't work"

**Solution**: Ensure state class is `@Serializable` and registered in `NavigationState.kt`.

### "State is shared between screens"

**Solution**: Make sure you're using `routeKey.toString()` as the factory key in `viewModel()`.

### "I don't know how to implement [feature]"

**Solution**: Check `refactoring/best-practices.md` for proven patterns and examples.

---

## Resources

### Internal Documentation
- [Architecture Overview](README.md)
- [Code Templates](templates/)
- [Compliance Checklist](analysis/checklist.md)
- [Validation Rules](analysis/validator.md)
- [Migration Guide](refactoring/migration-guide.md)
- [Best Practices](refactoring/best-practices.md)

### External Resources
- [Molecule Documentation](https://cashapp.github.io/molecule/)
- [Navigation 3 for Compose](https://developer.android.com/guide/navigation/compose)
- [Koin DI Framework](https://insert-koin.io/)
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)

---

## Getting Help

1. **Check documentation first** - Most answers are in these files
2. **Look at compliant screens** - HomeScreen, AddLinkScreen, LibraryScreen
3. **Use the checklist** - Systematic verification
4. **Follow best practices** - Proven solutions

---

## Next Steps

1. **Explore**: Read `README.md` for comprehensive understanding
2. **Practice**: Use templates to create a test screen
3. **Validate**: Check an existing screen with the checklist
4. **Refactor**: Migrate a non-compliant screen using the guide
5. **Master**: Study best practices for advanced patterns

---

## Summary

This skill provides everything needed to:

- **Generate** new screen architectures quickly
- **Validate** existing screens for compliance
- **Refactor** non-compliant screens systematically
- **Learn** architecture patterns and best practices

The goal is consistency across all screens in the LinkLibrary project, making the codebase more maintainable and testable.

**Start with the templates, validate with the checklist, improve with best practices.**
