# Migration Guide: Converting Screens to MVVM+UseCase+Molecule

This guide provides step-by-step instructions for migrating existing screens to the standard architecture pattern.

## Migration Overview

The migration process transforms ad-hoc or partially implemented screens into fully compliant MVVM+UseCase+Molecule architecture.

### Migration Steps Overview

1. **Assessment**: Identify current architecture and missing components
2. **StateModels Creation**: Define state and events
3. **UseCase Implementation**: Extract business logic
4. **ViewModel Refactoring**: Wrap UseCase with Molecule
5. **Screen Update**: Connect UI to ViewModel
6. **Integration**: Register in Navigation and Koin
7. **Testing**: Verify migration success

---

## Phase 1: Assessment

### 1.1 Identify Current Architecture

Analyze the existing screen to understand its current state:

```kotlin
// Current Screen Analysis
fun MyScreen() {
    // Questions to answer:
    // ✓ How is state managed? (remember, mutableStateOf, ViewModel?)
    // ✓ Where is business logic? (in Screen, separate file?)
    // ✓ How are user actions handled? (callbacks, events?)
    // ✓ What dependencies are used? (repositories, managers?)
    // ✓ Is navigation integrated? (parameters, callbacks?)
}
```

### 1.2 Identify Missing Components

Check what needs to be created:

- [ ] StateModels file exists
- [ ] UseCase file exists
- [ ] ViewModel file exists
- [ ] Screen file exists
- [ ] Navigation registration
- [ ] Koin module (if needed)

### 1.3 Create Migration Plan

Based on assessment, determine migration complexity:

- **Simple Migration**: Screen with minimal state, no business logic
- **Medium Migration**: Screen with some state, simple operations
- **Complex Migration**: Screen with complex state, multiple operations

---

## Phase 2: Create StateModels

### 2.1 Extract State

Identify all state properties in the current screen:

```kotlin
// BEFORE: State scattered in Screen
@Composable
fun MyScreen() {
    var isLoading by remember { mutableStateOf(false) }
    var items by remember { mutableStateOf<List<Item>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
}

// AFTER: Consolidated in StateModels
@Serializable
data class MyState(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val error: String? = null,
    val searchQuery: String = ""
)
```

### 2.2 Define Events

Convert user action callbacks to events:

```kotlin
// BEFORE: Callbacks
@Composable
fun MyScreen(
    onRefresh: () -> Unit,
    onDelete: (String) -> Unit,
    onSearch: (String) -> Unit
)

// AFTER: Events
sealed interface MyEvent {
    object Refresh : MyEvent
    data class Delete(val id: String) : MyEvent
    data class SearchChanged(val query: String) : MyEvent
}
```

### 2.3 Create StateModels File

```kotlin
// File: screens/myfeature/MyStateModels.kt
package com.greenrobotdev.linklibrary.screens.myfeature

import kotlinx.serialization.Serializable

@Serializable
data class MyState(
    val isLoading: Boolean = false,
    val error: String? = null,
    // Add your state properties here
)

sealed interface MyEvent {
    // Add your events here
}
```

**Migration Checklist for StateModels:**
- [ ] All state properties moved to data class
- [ ] State marked with `@Serializable`
- [ ] All callbacks converted to events
- [ ] Events defined in sealed interface
- [ ] Default values provided for all properties

---

## Phase 3: Create UseCase

### 3.1 Extract Business Logic

Move business logic from Screen to UseCase:

```kotlin
// BEFORE: Business logic in Screen
@Composable
fun MyScreen() {
    var items by remember { mutableStateOf(emptyList<Item>())
    LaunchedEffect(Unit) {
        repository.getItems().collect { result ->
            result.onSuccess { items = it }
            result.onFailure { error = it.message }
        }
    }
}

// AFTER: Business logic in UseCase
@Composable
fun MyUseCase(
    initialState: MyState,
    events: Flow<MyEvent>,
    repository: MyRepository
): MyState {
    var items by remember { mutableStateOf(initialState.items) }
    var error by remember { mutableStateOf(initialState.error) }
    var isLoading by remember { mutableStateOf(initialState.isLoading) }

    LaunchedEffect(Unit) {
        isLoading = true
        repository.getItems().collect { result ->
            isLoading = false
            result.onSuccess { items = it }
            result.onFailure { error = it.message }
        }
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                // Handle events
            }
        }
    }

    return MyState(isLoading, error, items)
}
```

### 3.2 Handle Events

Convert callback invocations to event handling:

```kotlin
// BEFORE: Callbacks in Screen
Button(onClick = onRefresh) { }
Button(onClick = { onDelete(itemId) }) { }
TextField(onValueChange = onSearch) { }

// AFTER: Event handling in UseCase
LaunchedEffect(Unit) {
    events.collect { event ->
        when (event) {
            is MyEvent.Refresh -> {
                isLoading = true
                repository.getItems().collect { /* ... */ }
            }
            is MyEvent.Delete -> {
                repository.delete(event.id).collect { /* ... */ }
            }
            is MyEvent.SearchChanged -> {
                searchQuery = event.query
            }
        }
    }
}
```

### 3.3 Create UseCase File

```kotlin
// File: screens/myfeature/MyUseCase.kt
package com.greenrobotdev.linklibrary.screens.myfeature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.Flow

@Composable
fun MyUseCase(
    initialState: MyState,
    events: Flow<MyEvent>,
    repository: MyRepository
): MyState {
    // State management
    // Initial data loading
    // Event handling
    // Return state
}
```

**Migration Checklist for UseCase:**
- [ ] Function marked with `@Composable`
- [ ] All state properties use `remember { mutableStateOf() }`
- [ ] Initial data loading in `LaunchedEffect(Unit)`
- [ ] Event collection in `LaunchedEffect(Unit)`
- [ ] All events handled in `when` block
- [ ] No Android UI dependencies
- [ ] Repository calls are suspending functions
- [ ] State updated using `copy()` method
- [ ] Returns complete state instance

---

## Phase 4: Create ViewModel

### 4.1 Extract Repository Dependencies

Identify repositories used in UseCase:

```kotlin
// Repositories needed for UseCase
private val repository: MyRepository by inject()
```

### 4.2 Create ViewModel with Molecule

```kotlin
// File: screens/myfeature/MyViewModel.kt
package com.greenrobotdev.linklibrary.screens.myfeature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyViewModel : ViewModel(), KoinComponent {

    private val initialState: MyState = MyState()

    private val eventsFlow: MutableSharedFlow<MyEvent> = MutableSharedFlow(5)
    private val repository: MyRepository by inject()

    val states by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            MyUseCase(initialState, eventsFlow, repository)
        }.stateIn(viewModelScope, SharingStarted.Lazily, initialState)
    }

    // Event emitters
    fun onRefresh() { viewModelScope.launch { eventsFlow.emit(MyEvent.Refresh) } }
    fun onDelete(id: String) { viewModelScope.launch { eventsFlow.emit(MyEvent.Delete(id)) } }
    fun onSearch(query: String) { viewModelScope.launch { eventsFlow.emit(MyEvent.SearchChanged(query)) } }

    fun onEvent(event: MyEvent) {
        viewModelScope.launch { eventsFlow.emit(event) }
    }
}
```

**Migration Checklist for ViewModel:**
- [ ] Class extends `ViewModel()`
- [ ] Implements `KoinComponent`
- [ ] Uses `moleculeFlow(RecompositionMode.Immediate)`
- [ ] Converts to `StateFlow` with `stateIn()`
- [ ] Uses `viewModelScope` for lifecycle
- [ ] Uses `SharingStarted.Lazily`
- [ ] Provides `onEvent()` method
- [ ] Provides convenience methods for common events

---

## Phase 5: Update Screen

### 5.1 Remove State Management

Remove state management from Screen:

```kotlin
// BEFORE: State in Screen
@Composable
fun MyScreen() {
    var items by remember { mutableStateOf(emptyList())}
    var isLoading by remember { mutableStateOf(false)}
    // ... UI code
}

// AFTER: State from ViewModel
@Composable
fun MyScreen(
    routeKey: NavKey
) {
    val viewModel: MyViewModel = viewModel(key = routeKey.toString()) { MyViewModel() }
    val state by viewModel.states.collectAsState()

    // ... UI code uses state
}
```

### 5.2 Connect Event Handlers

Replace callbacks with ViewModel methods:

```kotlin
// BEFORE: Callbacks
@Composable
fun MyScreen(
    onRefresh: () -> Unit,
    onDelete: (String) -> Unit
) {
    Button(onClick = onRefresh) { }
}

// AFTER: ViewModel methods
@Composable
fun MyScreen(
    routeKey: NavKey
) {
    val viewModel: MyViewModel = viewModel(key = routeKey.toString()) { MyViewModel() }

    Button(onClick = viewModel::onRefresh) { }
}
```

### 5.3 Add Navigation Integration

```kotlin
// BEFORE: No navigation support
@Composable
fun MyScreen() {
    // UI code
}

// AFTER: Navigation support
@Composable
fun MyScreen(
    routeKey: NavKey,
    onNavigateToDetail: (String) -> Unit,
    onBack: () -> Unit
) {
    val viewModel: MyViewModel = viewModel(key = routeKey.toString()) { MyViewModel() }
    val state by viewModel.states.collectAsState()

    // Handle navigation side effects
    LaunchedEffect(state.success) {
        if (state.success) onBack()
    }

    // UI code
}
```

### 5.4 Delegate to View Composable

Separate container (Screen) from UI (View):

```kotlin
// Screen composable (container)
@Composable
fun MyScreen(
    routeKey: NavKey,
    onNavigateToDetail: (String) -> Unit
) {
    val viewModel: MyViewModel = viewModel(key = routeKey.toString()) { MyViewModel() }
    val state by viewModel.states.collectAsState()

    MyView(
        state = state,
        onRefresh = viewModel::onRefresh,
        onItemClick = onNavigateToDetail
    )
}

// View composable (UI)
@Composable
private fun MyView(
    state: MyState,
    onRefresh: () -> Unit,
    onItemClick: (String) -> Unit
) {
    // UI implementation
}
```

**Migration Checklist for Screen:**
- [ ] Function marked with `@Composable`
- [ ] First parameter is `routeKey: NavKey`
- [ ] ViewModel created with `viewModel()` factory
- [ ] Uses `routeKey.toString()` as factory key
- [ ] State collected with `collectAsState()`
- [ ] UI delegated to separate View composable
- [ ] Event handlers connected to ViewModel
- [ ] Navigation callbacks passed to View

---

## Phase 6: Integration

### 6.1 Register in NavigationState.kt

Add state class to serializers module:

```kotlin
// In NavigationState.kt
private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            // Existing registrations
            subclass(RootScreens.HomeTab::class, RootScreens.HomeTab.serializer())
            // Add your screen
            subclass(RootScreens.MyFeature::class, RootScreens.MyFeature.serializer())
        }
    }
}
```

### 6.2 Add Navigation Route

Define navigation route:

```kotlin
// In RootScreens.kt or navigation file
sealed interface RootScreens : NavKey {
    // Existing screens
    object HomeTab : RootScreens
    object LibraryTab : RootScreens

    // Add your screen
    @Serializable
    data object MyFeature : RootScreens
}

// Or with parameters
@Serializable
data class MyFeature(val id: String) : RootScreens
```

### 6.3 Register in Koin (if needed)

If ViewModel requires custom factory:

```kotlin
// In Koin module
val myScreenModule = module {
    viewModel { MyViewModel() }

    // With parameters
    viewModel { (id: String) -> MyViewModel(id) }
}
```

**Migration Checklist for Integration:**
- [ ] State class registered in NavigationState.kt
- [ ] Navigation route defined
- [ ] Screen accessible via navigation
- [ ] ViewModel created by Koin (if custom factory needed)

---

## Phase 7: Testing

### 7.1 Verify Architecture Compliance

Use the checklist to verify compliance:

```bash
# Run architecture validation
./.claude/skills/architecture-standardize/scripts/validate-screen.sh myfeature
```

### 7.2 Manual Testing

Test the migrated screen:

- [ ] Screen loads correctly
- [ ] State updates work
- [ ] Event handling works
- [ ] Navigation works
- [ ] Error handling works
- [ ] Loading states work

### 7.3 Edge Cases

Test edge cases:

- [ ] Empty states display correctly
- [ ] Error states display correctly
- [ ] Configuration changes preserve state
- [ ] Multiple instances work independently
- [ ] Back navigation works

**Migration Checklist for Testing:**
- [ ] Architecture compliance verified
- [ ] Manual testing completed
- [ ] Edge cases tested
- [ ] No regressions introduced

---

## Common Migration Scenarios

### Scenario 1: Simple Screen with Minimal State

**Starting Point:**
```kotlin
@Composable
fun SimpleScreen() {
    var text by remember { mutableStateOf("Hello") }
    Text(text)
}
```

**Migration Steps:**
1. Create StateModels with single `text` property
2. Create UseCase that manages `text` state
3. Create ViewModel wrapping UseCase
4. Update Screen to use ViewModel
5. Register in navigation

**Time Estimate**: 30 minutes

---

### Scenario 2: Screen with Repository Calls

**Starting Point:**
```kotlin
@Composable
fun ListScreen() {
    var items by remember { mutableStateOf(emptyList<Item>())}
    LaunchedEffect(Unit) {
        repository.getItems().collect { items = it }
    }
    LazyColumn { items(items) { item -> Item(item) } }
}
```

**Migration Steps:**
1. Create StateModels with `items`, `isLoading`, `error`
2. Create events: `Refresh`, `Delete`
3. Create UseCase that calls repository
4. Create ViewModel with repository injection
5. Update Screen with ViewModel
6. Register in navigation and Koin

**Time Estimate**: 1 hour

---

### Scenario 3: Form Screen with Validation

**Starting Point:**
```kotlin
@Composable
fun FormScreen() {
    var email by remember { mutableStateOf("")}
    var isValid by remember { mutableStateOf(false)}
    Column {
        TextField(value = email, onValueChange = {
            email = it
            isValid = isValidEmail(it)
        })
        Button(enabled = isValid, onClick = { /* submit */ })
    }
}
```

**Migration Steps:**
1. Create StateModels with form fields, validation, success flag
2. Create events for field changes, submission
3. Create UseCase with validation logic
4. Create ViewModel
5. Update Screen
6. Add auto-navigation on success
7. Register in navigation

**Time Estimate**: 1.5 hours

---

### Scenario 4: Screen with Complex State

**Starting Point:**
```kotlin
@Composable
fun ComplexScreen() {
    var state by remember { mutableStateOf(ComplexState())}
    // Multiple interconnected state properties
    // Complex business logic
    // Multiple repository calls
}
```

**Migration Steps:**
1. Carefully extract all state properties
2. Identify all user actions and create events
3. Extract business logic systematically
4. Test UseCase in isolation
5. Create ViewModel with all dependencies
6. Update Screen incrementally
7. Test thoroughly
8. Register in navigation

**Time Estimate**: 2-3 hours

---

## Migration Anti-Patterns

### ❌ Don't: Keep State in Screen

```kotlin
// WRONG: State still in Screen
@Composable
fun MyScreen(routeKey: NavKey) {
    val viewModel: MyViewModel = viewModel(key = routeKey.toString()) { MyViewModel() }
    var localState by remember { mutableStateOf(LocalState())}  // ❌

    MyView(state = viewModel.states, localState = localState)  // ❌
}
```

### ✅ Do: Move All State to ViewModel

```kotlin
// CORRECT: All state in ViewModel
@Composable
fun MyScreen(routeKey: NavKey) {
    val viewModel: MyViewModel = viewModel(key = routeKey.toString()) { MyViewModel() }
    val state by viewModel.states.collectAsState()  // ✓

    MyView(state = state, onEvent = viewModel::onEvent)  // ✓
}
```

---

### ❌ Don't: Mix Callbacks and Events

```kotlin
// WRONG: Mixed callback and event handling
@Composable
fun MyScreen(
    routeKey: NavKey,
    onRefresh: () -> Unit,  // ❌ Old callback
    onDelete: (String) -> Unit  // ❌ Old callback
) {
    val viewModel: MyViewModel = viewModel(key = routeKey.toString()) { MyViewModel() }
    Button(onClick = onRefresh) { }  // ❌
}
```

### ✅ Do: Use Only Events

```kotlin
// CORRECT: Event-based
@Composable
fun MyScreen(routeKey: NavKey) {
    val viewModel: MyViewModel = viewModel(key = routeKey.toString()) { MyViewModel() }
    Button(onClick = viewModel::onRefresh) { }  // ✓
}
```

---

### ❌ Don't: Put Business Logic in ViewModel

```kotlin
// WRONG: Business logic in ViewModel
class MyViewModel : ViewModel() {
    fun onCalculate() {
        // Complex calculations here  // ❌
        val result = complexAlgorithm(data)
    }
}
```

### ✅ Do: Put Business Logic in UseCase

```kotlin
// CORRECT: Business logic in UseCase
@Composable
fun MyUseCase(...) {
    LaunchedEffect(Unit) {
        events.collect { event ->
            if (event is MyEvent.Calculate) {
                val result = complexAlgorithm(data)  // ✓
            }
        }
    }
}
```

---

## Post-Migration Checklist

After migration, verify:

- [ ] All tests pass
- [ ] Screen works as before
- [ ] No performance regressions
- [ ] No memory leaks
- [ ] Architecture compliance verified
- [ ] Code review completed
- [ ] Documentation updated

---

## Troubleshooting

### Issue: State Not Updating

**Symptoms**: UI doesn't reflect state changes

**Causes**:
- Missing `collectAsState()`
- Not using `mutableStateOf` in UseCase
- Missing `remember` block

**Solution**: Ensure proper state management pattern

### Issue: Navigation Not Working

**Symptoms**: Can't navigate to screen or back

**Causes**:
- Missing `@Serializable` on state
- Not registered in NavigationState.kt
- Wrong `routeKey` usage

**Solution**: Check navigation integration

### Issue: Multiple Screens Share State

**Symptoms**: State shared across instances

**Causes**:
- Not using `routeKey` as factory key
- Using singleton ViewModel

**Solution**: Ensure unique factory key per instance

---

## Resources

- Architecture Overview: `README.md`
- Templates: `templates/`
- Checklist: `analysis/checklist.md`
- Validator: `analysis/validator.md`

---

This migration guide provides a systematic approach to converting any screen to the standard architecture. Take it step by step, verify at each phase, and test thoroughly.
