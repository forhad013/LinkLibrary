# Best Practices: MVVM+UseCase+Molecule Architecture

Collection of proven patterns, solutions, and guidelines for common scenarios in the LinkLibrary architecture.

## Table of Contents

1. [State Management](#state-management)
2. [Event Handling](#event-handling)
3. [Loading States](#loading-states)
4. [Error Handling](#error-handling)
5. [Form Validation](#form-validation)
6. [List Screens](#list-screens)
7. [Detail Screens](#detail-screens)
8. [Settings Screens](#settings-screens)
9. [Navigation Patterns](#navigation-patterns)
10. [Performance Optimization](#performance-optimization)
11. [Testing Strategies](#testing-strategies)
12. [Common Pitfalls](#common-pitfalls)

---

## State Management

### Principle: Single Source of Truth

All state must flow through the ViewModel-UseCase chain. Never maintain local state in the Screen.

#### ✅ Correct: Centralized State

```kotlin
@Composable
fun MyScreen(routeKey: NavKey) {
    val viewModel: MyViewModel = viewModel(key = routeKey.toString()) { MyViewModel() }
    val state by viewModel.states.collectAsState()

    // All state comes from ViewModel
    MyView(state = state, onEvent = viewModel::onEvent)
}
```

#### ❌ Incorrect: Local State

```kotlin
@Composable
fun MyScreen(routeKey: NavKey) {
    val viewModel: MyViewModel = viewModel(key = routeKey.toString()) { MyViewModel() }
    var localState by remember { mutableStateOf(LocalState())}  // ❌ Don't do this

    MyView(state = viewModel.states, localState = localState)  // ❌
}
```

---

### Principle: Immutability

State should be immutable. Updates create new instances using `copy()`.

#### ✅ Correct: Immutable State

```kotlin
@Serializable
data class MyState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false
)

// Update with copy()
state = state.copy(
    items = newItems,
    isLoading = false
)
```

#### ❌ Incorrect: Mutable State

```kotlin
@Serializable
data class MyState(
    var items: List<Item> = emptyList(),  // ❌ Don't use var
    var isLoading: Boolean = false       // ❌ Don't use var
)

// Direct mutation
state.items = newItems  // ❌
```

---

### Principle: Default Values

Always provide sensible defaults for state properties.

```kotlin
@Serializable
data class MyState(
    // Boolean flags default to false
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val hasError: Boolean = false,

    // Nullable errors default to null
    val error: String? = null,

    // Collections default to empty
    val items: List<Item> = emptyList(),
    val selectedIds: Set<String> = emptySet(),

    // Strings default to empty
    val searchQuery: String = "",
    val username: String = "",

    // Enums default to first option
    val sortOrder: SortOrder = SortOrder.DATE_DESC
)
```

---

## Event Handling

### Principle: Events Represent Intent

Events should describe user intentions, not state updates.

#### ✅ Correct: Intent-Based Events

```kotlin
sealed interface MyEvent {
    // User actions
    object Refresh : MyEvent
    data class DeleteItem(val id: String) : MyEvent
    data class ToggleFavorite(val id: String) : MyEvent

    // Input changes
    data class SearchChanged(val query: String) : MyEvent
    data class FieldChanged(val value: String) : MyEvent

    // Navigation requests
    object NavigateBack : MyEvent
    data class NavigateToDetail(val id: String) : MyEvent
}
```

#### ❌ Incorrect: State-Based Events

```kotlin
sealed interface MyEvent {
    data class ItemsChanged(val items: List<Item>) : MyEvent  // ❌ Not a user intent
    data class LoadingChanged(val isLoading: Boolean) : MyEvent  // ❌ Not a user intent
    data class ErrorOccurred(val error: String) : MyEvent  // ❌ Not a user intent
}
```

---

### Principle: Event Emitters in ViewModel

Provide convenient methods for common events.

```kotlin
class MyViewModel : ViewModel(), KoinComponent {

    // Convenience methods for common events
    fun onRefresh() { viewModelScope.launch { eventsFlow.emit(MyEvent.Refresh) } }
    fun onDelete(id: String) { viewModelScope.launch { eventsFlow.emit(MyEvent.DeleteItem(id)) } }
    fun onSearch(query: String) { viewModelScope.launch { eventsFlow.emit(MyEvent.SearchChanged(query)) } }

    // Generic fallback for less common events
    fun onEvent(event: MyEvent) {
        viewModelScope.launch { eventsFlow.emit(event) }
    }
}
```

---

### Principle: Event Collection in UseCase

Collect events in LaunchedEffect and handle in when block.

```kotlin
@Composable
fun MyUseCase(
    initialState: MyState,
    events: Flow<MyEvent>,
    repository: MyRepository
): MyState {
    var state by remember { mutableStateOf(initialState) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is MyEvent.Refresh -> {
                    state = state.copy(isLoading = true)
                    repository.refresh().collect { result ->
                        result.onSuccess { state = state.copy(isLoading = false, items = it) }
                        result.onFailure { state = state.copy(isLoading = false, error = it.message) }
                    }
                }
                is MyEvent.DeleteItem -> {
                    repository.delete(event.id).collect { result ->
                        result.onSuccess { state = state.copy(items = state.items.filter { it.id != event.id }) }
                    }
                }
                // ... handle other events
            }
        }
    }

    return state
}
```

---

## Loading States

### Pattern: Loading/Error/Data Triad

The most common pattern for data loading.

```kotlin
@Serializable
data class MyState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<Item> = emptyList()
)
```

### UI Implementation

```kotlin
@Composable
fun MyView(state: MyState) {
    when {
        state.isLoading && state.data.isEmpty() -> {
            CircularProgressIndicator()
        }
        state.error != null -> {
            ErrorMessage(
                message = state.error,
                onRetry = { /* emit Refresh event */ }
            )
        }
        state.data.isEmpty() -> {
            EmptyState(
                message = "No items found",
                onAction = { /* emit appropriate event */ }
            )
        }
        else -> {
            DataList(items = state.data)
        }
    }
}
```

---

### Pattern: Background Refresh

Show content while refreshing in background.

```kotlin
@Serializable
data class MyState(
    val isRefreshing: Boolean = false,  // Separate flag for refresh
    val isLoading: Boolean = false,     // For initial load
    val error: String? = null,
    val data: List<Item> = emptyList()
)

// UI
if (state.isLoading && state.data.isEmpty()) {
    CircularProgressIndicator()
} else {
    LazyColumn {
        items(state.data) { item ->
            Item(item)
        }
    }
}

// Pull-to-refresh indicator
PullRefreshIndicator(
    refreshing = state.isRefreshing,
    onRefresh = { /* emit Refresh */ },
    state = pullRefreshState
)
```

---

## Error Handling

### Principle: Never Lose Errors

Always display errors to the user and provide recovery options.

```kotlin
@Serializable
data class MyState(
    val isLoading: Boolean = false,
    val error: String? = null,  // Always include error field
    val data: List<Item> = emptyList()
)

// Error handling in UseCase
repository.getData().collect { result ->
    result.onSuccess { data ->
        state = state.copy(
            isLoading = false,
            error = null,  // Clear error on success
            data = data
        )
    }
    result.onFailure { throwable ->
        state = state.copy(
            isLoading = false,
            error = throwable.message ?: "Unknown error"  // Always capture error
        )
    }
}
```

---

### Pattern: Clearable Errors

Allow users to dismiss error messages.

```kotlin
sealed interface MyEvent {
    // ... other events
    object ClearError : MyEvent  // Event to clear error
}

// In UseCase
is MyEvent.ClearError -> {
    state = state.copy(error = null)
}

// In UI
state.error?.let { error ->
    ErrorMessage(
        message = error,
        onDismiss = { /* emit ClearError event */ }
    )
}
```

---

### Pattern: Field-Level Errors

For forms, track errors per field.

```kotlin
@Serializable
data class FormState(
    val email: String = "",
    val password: String = "",
    val errors: Map<String, String> = emptyMap(),  // Field-specific errors
    val isFormValid: Boolean = false
)

// Validation
fun validateForm(state: FormState): Map<String, String> {
    val errors = mutableMapOf<String, String>()
    if (state.email.isBlank()) errors["email"] = "Email is required"
    if (!state.email.contains("@")) errors["email"] = "Invalid email format"
    if (state.password.length < 8) errors["password"] = "Password too short"
    return errors
}

// In UseCase
is FormEvent.EmailChanged -> {
    email = event.email
    errors = validateForm(FormState(email, password))
    isFormValid = errors.isEmpty()
}
```

---

## Form Validation

### Pattern: Real-Time Validation

Validate as user types.

```kotlin
@Serializable
data class FormState(
    val url: String = "",
    val isUrlValid: Boolean = false,
    val title: String = "",
    val isTitleValid: Boolean = false,
    val isFormValid: Boolean = false,
    val error: String? = null
)

// In UseCase
// Validate URL when it changes
is FormEvent.UrlChanged -> {
    url = event.url
    isUrlValid = isValidUrl(event.url)
    isFormValid = isUrlValid && isTitleValid
}

// Validate title when it changes
is FormEvent.TitleChanged -> {
    title = event.title
    isTitleValid = event.title.isNotBlank()
    isFormValid = isUrlValid && isTitleValid
}
```

---

### Pattern: Submit-Time Validation

Validate only on submit attempt.

```kotlin
@Serializable
data class FormState(
    val url: String = "",
    val title: String = "",
    val submitAttempted: Boolean = false,  // Track submit attempt
    val errors: Map<String, String> = emptyMap()
)

// In UseCase
is FormEvent.Submit -> {
    val errors = validateForm(state)
    if (errors.isNotEmpty()) {
        state = state.copy(submitAttempted = true, errors = errors)
    } else {
        // Proceed with submission
    }
}

// In UI
OutlinedTextField(
    value = state.url,
    onValueChange = { /* emit UrlChanged */ },
    isError = state.submitAttempted && state.errors.containsKey("url"),
    supportingText = if (state.submitAttempted) {
        { Text(state.errors["url"] ?: "") }
    } else null
)
```

---

## List Screens

### Pattern: Infinite Scroll

Load more items when user scrolls near bottom.

```kotlin
@Serializable
data class ListState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,  // Separate flag for pagination
    val hasMore: Boolean = true,
    val error: String? = null
)

sealed interface ListEvent {
    object LoadMore : ListEvent
}

// In UseCase
is ListEvent.LoadMore -> {
    if (!isLoadingMore && hasMore) {
        isLoadingMore = true
        repository.getItems(items.size, PAGE_SIZE).collect { result ->
            isLoadingMore = false
            result.onSuccess { newItems ->
                items = items + newItems
                hasMore = newItems.size == PAGE_SIZE
            }
        }
    }
}

// In UI
LazyColumn {
    items(state.items) { item -> Item(item) }

    if (state.hasMore) {
        item {
            LaunchedEffect(Unit) {
                // Load more when item comes into view
                onEvent(ListEvent.LoadMore)
            }
            if (state.isLoadingMore) {
                CircularProgressIndicator()
            }
        }
    }
}
```

---

### Pattern: Search/Filter

Real-time search with debouncing.

```kotlin
@Serializable
data class ListState(
    val items: List<Item> = emptyList(),
    val filteredItems: List<Item> = emptyList(),
    val searchQuery: String = ""
)

sealed interface ListEvent {
    data class SearchChanged(val query: String) : ListEvent
    data class ItemsLoaded(val items: List<Item>) : ListEvent
}

// In UseCase
is ListEvent.ItemsLoaded -> {
    items = event.items
    filteredItems = filterItems(event.items, searchQuery)
}

is ListEvent.SearchChanged -> {
    searchQuery = event.query
    filteredItems = filterItems(items, event.query)
}

private fun filterItems(items: List<Item>, query: String): List<Item> {
    return if (query.isBlank()) {
        items
    } else {
        items.filter { it.name.contains(query, ignoreCase = true) }
    }
}
```

---

## Detail Screens

### Pattern: Load by ID

Load item based on navigation parameter.

```kotlin
@Serializable
data class DetailState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val itemId: String? = null,  // Set from navigation
    val item: Item? = null
)

sealed interface DetailEvent {
    data class LoadItem(val id: String) : DetailEvent
}

// In UseCase
LaunchedEffect(initialState.itemId) {
    if (initialState.itemId != null) {
        isLoading = true
        repository.getItem(initialState.itemId).collect { result ->
            isLoading = false
            result.onSuccess { item = it }
            result.onFailure { error = it.message }
        }
    }
}

// In ViewModel
class DetailViewModel(
    private val itemId: String
) : ViewModel(), KoinComponent {
    private val initialState: DetailState = DetailState(itemId = itemId)

    init {
        viewModelScope.launch {
            eventsFlow.emit(DetailEvent.LoadItem(itemId))
        }
    }
}
```

---

### Pattern: Delete with Navigation

Auto-navigate after deletion.

```kotlin
@Serializable
data class DetailState(
    val isLoading: Boolean = false,
    val isDeleted: Boolean = false,  // Flag for navigation
    val item: Item? = null
)

sealed interface DetailEvent {
    object Delete : DetailEvent
}

// In UseCase
is DetailEvent.Delete -> {
    repository.delete(item!!.id).collect { result ->
        result.onSuccess { isDeleted = true }
        result.onFailure { error = it.message }
    }
}

// In Screen
LaunchedEffect(state.isDeleted) {
    if (state.isDeleted) {
        onBack()
    }
}
```

---

## Settings Screens

### Pattern: Auto-Save

Save changes automatically when user modifies settings.

```kotlin
@Serializable
data class SettingsState(
    val isLoading: Boolean = false,
    val settings: UserSettings = UserSettings(),
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)

sealed interface SettingsEvent {
    data class ThemeChanged(val theme: Theme) : SettingsEvent
    data class NotificationsChanged(val enabled: Boolean) : SettingsEvent
}

// In UseCase
is SettingsEvent.ThemeChanged -> {
    val newSettings = settings.copy(theme = event.theme)
    isSaving = true
    repository.saveSettings(newSettings).collect { result ->
        isSaving = false
        result.onSuccess {
            settings = newSettings
            saveSuccess = true
        }
    }
}
```

---

### Pattern: Explicit Save

Save only when user clicks save button.

```kotlin
@Serializable
data class SettingsState(
    val settings: UserSettings = UserSettings(),
    val editedSettings: UserSettings = UserSettings(),  // Track edits separately
    val hasChanges: Boolean = false,
    val isSaving: Boolean = false
)

sealed interface SettingsEvent {
    data class FieldChanged(val field: String, val value: Any) : SettingsEvent
    object Save : SettingsEvent
    object Discard : SettingsEvent
}

// In UseCase
is SettingsEvent.FieldChanged -> {
    editedSettings = when (event.field) {
        "theme" -> editedSettings.copy(theme = event.value as Theme)
        "notifications" -> editedSettings.copy(notificationsEnabled = event.value as Boolean)
        else -> editedSettings
    }
    hasChanges = editedSettings != settings
}

is SettingsEvent.Save -> {
    isSaving = true
    repository.saveSettings(editedSettings).collect { result ->
        isSaving = false
        result.onSuccess {
            settings = editedSettings
            hasChanges = false
        }
    }
}
```

---

## Navigation Patterns

### Pattern: Navigate on Success

Navigate back after successful operation.

```kotlin
@Serializable
data class FormState(
    val isLoading: Boolean = false,
    val success: Boolean = false  // Flag for navigation
)

// In UseCase
is FormEvent.Submit -> {
    isLoading = true
    repository.submit(data).collect { result ->
        isLoading = false
        result.onSuccess { success = true }
        result.onFailure { error = it.message }
    }
}

// In Screen
LaunchedEffect(state.success) {
    if (state.success) {
        onBack()
    }
}
```

---

### Pattern: Navigate with Result

Pass data back to previous screen.

```kotlin
// In child screen
@Serializable
data class SelectionState(
    val selectedId: String? = null,
    val navigateBack: Boolean = false
)

// In child Screen
LaunchedEffect(state.navigateBack) {
    if (state.navigateBack && state.selectedId != null) {
        onResult(state.selectedId)  // Pass result back
        onBack()
    }
}

// In parent screen
@Composable
fun ParentScreen() {
    var selectedItem by remember { mutableStateOf<String?>(null) }

    // Navigate to selection screen
    NavHost {
        composable("selection") {
            SelectionScreen(
                onResult = { result -> selectedItem = result }
            )
        }
    }
}
```

---

## Performance Optimization

### Principle: Avoid Unnecessary Recomposition

Minimize recomposition scope for better performance.

```kotlin
// ✅ Good: Stable lambdas
@Composable
fun MyView(
    state: MyState,
    onEvent: (MyEvent) -> Unit  // Stable lambda
) {
    LazyColumn {
        items(state.items) { item ->
            Item(
                item = item,
                onClick = { onEvent(MyEvent.ItemClicked(item.id)) }  // Lambda is stable
            )
        }
    }
}

// ❌ Bad: Creating new lambdas
@Composable
fun MyView(
    state: MyState,
    onItemClick: (String) -> Unit
) {
    LazyColumn {
        items(state.items) { item ->
            Item(
                item = item,
                onClick = { onItemClick(item.id) }  // New lambda each composition
            )
        }
    }
}
```

---

### Principle: Use Stable Types

Mark types as stable to improve performance.

```kotlin
@Immutable  // Mark as immutable
@Serializable
data class Item(
    val id: String,
    val name: String
)

@Stable  // Mark as stable
sealed interface MyEvent
```

---

### Principle: Derive State

Compute derived values instead of storing them.

```kotlin
// ✅ Good: Derive when needed
@Serializable
data class ListState(
    val items: List<Item> = emptyList()
)

// In UseCase or View
val isEmpty: Boolean
    get() = items.isEmpty()
val itemCount: Int
    get() = items.size

// ❌ Bad: Store redundant state
@Serializable
data class ListState(
    val items: List<Item> = emptyList(),
    val isEmpty: Boolean = false,  // Redundant
    val itemCount: Int = 0          // Redundant
)
```

---

## Testing Strategies

### Principle: Test UseCases in Isolation

UseCases should be pure and testable without Android.

```kotlin
@Test
fun testHomeUseCase_refreshLoadsData() = runTest {
    // Given
    val initialState = HomeState()
    val events = MutableSharedFlow<HomeEvent>()
    val fakeRepository = FakeLinkRepository(testData)

    // When
    val state = HomeUseCase(initialState, events, fakeRepository)
    events.emit(HomeEvent.Refresh)

    // Then
    assertEquals(testData, state.items)
    assertFalse(state.isLoading)
    assertNull(state.error)
}
```

---

### Principle: Test ViewModels

Test ViewModel event emission and state flow.

```kotlin
@Test
fun testHomeViewModel_emitsRefreshEvent() = runTest {
    // Given
    val viewModel = HomeViewModel()
    val testEvents = mutableListOf<HomeEvent>()

    // When
    viewModel.onRefresh()

    // Then
    // Verify event was emitted to UseCase
}
```

---

### Principle: Test Screens with UI Testing

Use Compose Testing framework for UI tests.

```kotlin
@Test
fun testHomeScreen_displaysLinks() {
    composeTestRule.setContent {
        val mockViewModel = mockk<HomeViewModel>()
        every { mockViewModel.states } returns flowOf(HomeState(links = testLinks))

        HomeScreen(
            routeKey = NavKey("test"),
            onNavigateToDetail = {},
            onAddLink = {}
        )
    }

    composeTestRule.onNodeWithText("Test Link").assertIsDisplayed()
}
```

---

## Common Pitfalls

### ❌ 1. Mixing State Management

Don't use both ViewModel state and local state.

```kotlin
// ❌ WRONG
@Composable
fun MyScreen(routeKey: NavKey) {
    val viewModel: MyViewModel = viewModel(key = routeKey.toString()) { MyViewModel() }
    val state by viewModel.states.collectAsState()
    var localState by remember { mutableStateOf(LocalState())}  // ❌ Don't

    // Mixing state sources
}
```

---

### ❌ 2. Forgetting collectAsState

Don't forget to collect state as Compose State.

```kotlin
// ❌ WRONG
@Composable
fun MyScreen(routeKey: NavKey) {
    val viewModel: MyViewModel = viewModel(key = routeKey.toString()) { MyViewModel() }
    val state = viewModel.states.value  // ❌ Not reactive, won't update
}

// ✅ CORRECT
@Composable
fun MyScreen(routeKey: NavKey) {
    val viewModel: MyViewModel = viewModel(key = routeKey.toString()) { MyViewModel() }
    val state by viewModel.states.collectAsState()  // ✓ Reactive
}
```

---

### ❌ 3. Not Using routeKey

Don't forget to use routeKey for ViewModel factory.

```kotlin
// ❌ WRONG
@Composable
fun MyScreen(routeKey: NavKey) {
    val viewModel: MyViewModel = viewModel { MyViewModel() }  // ❌ Shared instance
}

// ✅ CORRECT
@Composable
fun MyScreen(routeKey: NavKey) {
    val viewModel: MyViewModel = viewModel(key = routeKey.toString()) { MyViewModel() }  // ✓ Unique instance
}
```

---

### ❌ 4. Business Logic in ViewModel

Don't put business logic in ViewModel.

```kotlin
// ❌ WRONG
class MyViewModel : ViewModel() {
    fun onProcess() {
        val result = complexBusinessLogic(data)  // ❌ Business logic in ViewModel
        state = state.copy(processed = result)
    }
}

// ✅ CORRECT
// In UseCase
when (event) {
    is MyEvent.Process -> {
        val result = complexBusinessLogic(data)  // ✓ Business logic in UseCase
        state = state.copy(processed = result)
    }
}
```

---

### ❌ 5. Direct State Mutation

Don't mutate state directly in UseCase.

```kotlin
// ❌ WRONG
var items by remember { mutableStateOf(initialState.items) }
items.add(newItem)  // ❌ Direct mutation

// ✅ CORRECT
var items by remember { mutableStateOf(initialState.items) }
items = items + newItem  // ✓ Immutable update
```

---

### ❌ 6. Missing @Serializable

Don't forget @Serializable on state classes.

```kotlin
// ❌ WRONG
data class MyState(  // ❌ Missing @Serializable
    val isLoading: Boolean = false
)

// ✅ CORRECT
@Serializable
data class MyState(  // ✓ Has @Serializable
    val isLoading: Boolean = false
)
```

---

### ❌ 7. Android Dependencies in UseCase

Don't import Android UI components in UseCase.

```kotlin
// ❌ WRONG
import androidx.compose.ui.unit.dp  // ❌ Android dependency

@Composable
fun MyUseCase(...) {
    val padding = 16.dp  // ❌ Don't use Android types
}

// ✅ CORRECT
@Composable
fun MyUseCase(...) {
    // No Android imports, only business logic
}
```

---

## Quick Reference

### StateModels Template
```kotlin
@Serializable
data class FeatureState(
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface FeatureEvent
```

### UseCase Template
```kotlin
@Composable
fun FeatureUseCase(
    initialState: FeatureState,
    events: Flow<FeatureEvent>,
    repository: Repository
): FeatureState
```

### ViewModel Template
```kotlin
class FeatureViewModel : ViewModel(), KoinComponent {
    val states by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            FeatureUseCase(initialState, eventsFlow, repository)
        }.stateIn(viewModelScope, SharingStarted.Lazily, initialState)
    }
}
```

### Screen Template
```kotlin
@Composable
fun FeatureScreen(routeKey: NavKey) {
    val viewModel: FeatureViewModel = viewModel(key = routeKey.toString()) { FeatureViewModel() }
    val state by viewModel.states.collectAsState()
}
```

---

These best practices provide proven solutions for common scenarios. Follow them consistently to maintain architecture quality across the project.
