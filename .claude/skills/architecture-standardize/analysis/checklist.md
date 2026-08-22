# Architecture Compliance Checklist

Use this checklist to verify that a screen follows the MVVM+UseCase+Molecule architecture pattern consistently.

## Screen Information

**Screen Name**: _______________
**Package**: _______________
**Date**: _______________
**Reviewer**: _______________

## Compliance Score

Calculate score by counting "Yes" responses divided by total items.

- **Score**: ___ / 45
- **Percentage**: ___%
- **Status**: [ ] Compliant (90%+) [ ] Needs Improvement (70-89%) [ ] Non-Compliant (<70%)

---

## 1. File Structure (5 points)

- [ ] All files located in `screens/{feature}/` directory
- [ ] File naming follows `{Feature}StateModels.kt` pattern
- [ ] File naming follows `{Feature}UseCase.kt` pattern
- [ ] File naming follows `{Feature}ViewModel.kt` pattern
- [ ] File naming follows `{Feature}Screen.kt` pattern

**Notes**: _______________

---

## 2. StateModels Compliance (10 points)

### Class Definition
- [ ] State class marked with `@Serializable` annotation
- [ ] State class is a `data class`
- [ ] Event type is a `sealed interface`
- [ ] State properties have default values
- [ ] State is immutable (no `var` properties)

### State Properties
- [ ] Includes `isLoading: Boolean = false`
- [ ] Includes `error: String? = null`
- [ ] Additional properties are domain-specific
- [ ] Lists use `emptyList()` instead of `null`
- [ ] Navigation-related state included (e.g., `success` flag)

**Notes**: _______________

---

## 3. UseCase Compliance (12 points)

### Function Signature
- [ ] Function is `@Composable`
- [ ] Function name follows `{Feature}UseCase` pattern
- [ ] Parameters: `initialState: {Feature}State`
- [ ] Parameters: `events: Flow<{Feature}Event>`
- [ ] Parameters: Repository dependencies injected

### State Management
- [ ] Uses `remember { mutableStateOf() }` for state
- [ ] State variables initialized from `initialState`
- [ ] Uses `LaunchedEffect(Unit)` for one-time setup
- [ ] Uses `LaunchedEffect(Unit)` for event collection
- [ ] Event handling in `when (event)` block
- [ ] State updates use `copy()` method
- [ ] Repository calls are suspending functions

### Return Value
- [ ] Returns state instance with all properties
- [ ] State instance matches `{Feature}State` type

**Notes**: _______________

---

## 4. ViewModel Compliance (10 points)

### Class Definition
- [ ] Class extends `ViewModel()`
- [ ] Class implements `KoinComponent`
- [ ] Constructor allows optional navigation parameters
- [ ] Private `initialState` property defined
- [ ] Private `eventsFlow: MutableSharedFlow` defined

### Dependency Injection
- [ ] Repository injected with `by inject()`
- [ ] All dependencies are Koin-provided
- [ ] No hardcoded repository instances

### Molecule Integration
- [ ] Uses `moleculeFlow(RecompositionMode.Immediate)`
- [ ] Molecule wraps UseCase function
- [ ] Converted to `StateFlow` with `stateIn()`
- [ ] Uses `viewModelScope` for lifecycle
- [ ] Uses `SharingStarted.Lazily`
- [ ] Initial value set to `initialState`

### Event Emitters
- [ ] Provides convenience methods for common events
- [ ] Events emitted via `viewModelScope.launch { eventsFlow.emit() }`
- [ ] Generic `onEvent()` method available

**Notes**: _______________

---

## 5. Screen Compliance (8 points)

### Function Signature
- [ ] Function is `@Composable`
- [ ] Function name follows `{Feature}Screen` pattern
- [ ] First parameter: `routeKey: NavKey`
- [ ] Additional parameters are navigation callbacks
- [ ] Returns `Unit` (not a component)

### ViewModel Creation
- [ ] ViewModel created with `viewModel()` factory
- [ ] Uses `routeKey.toString()` as factory key
- [ ] ViewModel initialization in lambda
- [ ] State collected via `collectAsState()`

### UI Delegation
- [ ] UI delegated to separate View composable
- [ ] State passed to View composable
- [ ] Event handlers passed to View composable
- [ ] Navigation callbacks passed to View composable

**Notes**: _______________

---

## 6. Integration & Best Practices (5 points)

### Navigation 3 Integration
- [ ] State class registered in `NavigationState.kt`
- [ ] Added to serializers module
- [ ] Subclass registered correctly

### Koin Integration
- [ ] ViewModel accessible via Koin (if needed)
- [ ] Repositories defined in Koin modules
- [ ] No manual dependency injection

### Code Quality
- [ ] No Android dependencies in UseCase
- [ ] No business logic in Screen
- [ ] ViewModel is thin (just Molecule wrapping)
- [ ] State is immutable throughout flow

**Notes**: _______________

---

## 7. Common Patterns (5 points)

- [ ] Loading states handled properly
- [ ] Error states displayed to user
- [ ] Empty states handled when applicable
- [ ] Success feedback provided (e.g., navigation back)
- [ ] Form validation feedback (for form screens)

**Notes**: _______________

---

## 8. Additional Considerations

### For List Screens
- [ ] Pull-to-refresh implemented
- [ ] Pagination support (if applicable)
- [ ] Search/filter functionality
- [ ] Item click handling

### For Detail Screens
- [ ] ID parameter passed from navigation
- [ ] Data loaded on composition
- [ ] Delete/edit operations
- [ ] Favorite toggling

### For Form Screens
- [ ] Form validation in state
- [ ] Success flag for navigation
- [ ] Clear error capability
- [ ] Submit button disabled when invalid

### For Settings Screens
- [ ] Settings loaded from repository
- [ ] Changes detection
- [ ] Save on exit or explicit save

**Notes**: _______________

---

## 9. Testing Readiness (Optional)

- [ ] UseCase is pure (no Android dependencies)
- [ ] ViewModel can be tested with JUnit
- [ ] StateModels are serializable for testing
- [ ] Events are sealed for exhaustive testing

**Notes**: _______________

---

## Analysis Results

### Strengths
1. _______________
2. _______________
3. _______________

### Areas for Improvement
1. _______________
2. _______________
3. _______________

### Required Changes
1. _______________
2. _______________
3. _______________

### Recommended Actions

- [ ] Review and fix missing items
- [ ] Refactor to match architecture pattern
- [ ] Add missing components
- [ ] Update documentation
- [ ] Re-submit for review

---

## Quick Reference: Architecture Patterns

### Standard File Structure
```
screens/
├── {feature}/
│   ├── {Feature}StateModels.kt  ✓
│   ├── {Feature}UseCase.kt      ✓
│   ├── {Feature}ViewModel.kt    ✓
│   └── {Feature}Screen.kt       ✓
```

### Standard Imports

**StateModels:**
```kotlin
import kotlinx.serialization.Serializable
```

**UseCase:**
```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
```

**ViewModel:**
```kotlin
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
```

**Screen:**
```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
```

### Standard Signatures

**StateModels:**
```kotlin
@Serializable
data class FeatureState(...)
sealed interface FeatureEvent
```

**UseCase:**
```kotlin
@Composable
fun FeatureUseCase(
    initialState: FeatureState,
    events: Flow<FeatureEvent>,
    repository: Repository
): FeatureState
```

**ViewModel:**
```kotlin
class FeatureViewModel(
    private val param: Type? = null
) : ViewModel(), KoinComponent
```

**Screen:**
```kotlin
@Composable
fun FeatureScreen(
    routeKey: NavKey,
    onNavigate: () -> Unit
)
```

---

## Compliance Categories

### Fully Compliant ✓
- Score: 90%+ (41-45 points)
- All critical components present
- Follows architecture pattern exactly
- Ready for production

### Mostly Compliant ~
- Score: 70-89% (32-40 points)
- Minor deviations from pattern
- Some best practices missing
- Needs small adjustments

### Non-Compliant ✗
- Score: <70% (0-31 points)
- Major components missing
- Significant architectural deviations
- Requires refactoring

---

## Next Steps

1. **If Compliant**: Mark as verified, document any exceptions
2. **If Needs Improvement**: Create issue list, schedule fixes
3. **If Non-Compliant**: Create refactoring plan, assign priority

Use the migration guide in `refactoring/migration-guide.md` for step-by-step refactoring instructions.
