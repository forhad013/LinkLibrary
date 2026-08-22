# Architecture Validator

Automated validation rules and detection logic for verifying architecture compliance.

## Validation Rules

### 1. File Existence Detection

Check for presence of all required architecture files.

#### Rule: Complete Architecture File Set
```kotlin
// Required files for each screen
val requiredFiles = listOf(
    "{Feature}StateModels.kt",
    "{Feature}UseCase.kt",
    "{Feature}ViewModel.kt",
    "{Feature}Screen.kt"
)

// Check pattern
fun hasCompleteArchitecture(screenName: String): Boolean {
    return requiredFiles.all { file ->
        File("screens/$screenName/$file").exists()
    }
}
```

#### Detection Logic
```bash
# Find screens with incomplete architecture
for screen in screens/*/; do
    name=$(basename "$screen")
    required=(
        "${name}StateModels.kt"
        "${name}UseCase.kt"
        "${name}ViewModel.kt"
        "${name}Screen.kt"
    )
    missing=()
    for file in "${required[@]}"; do
        [ ! -f "$screen$file" ] && missing+=("$file")
    done
    [ ${#missing[@]} -gt 0 ] && echo "$name: ${missing[*]}"
done
```

---

### 2. StateModels Validation

#### Rule: @Serializable Annotation
```kotlin
// Check: State class must have @Serializable
@Serializable
data class HomeState(...) // ✓
data class HomeState(...) // ✗ Missing annotation
```

**Detection Pattern:**
```regex
@Serializable\s*data\s+class\s+\w+State\s*\(
```

#### Rule: Sealed Interface Events
```kotlin
// Check: Events must be sealed interface
sealed interface HomeEvent // ✓
sealed class HomeEvent     // ✗ Should be interface
class HomeEvent            // ✗ Should be sealed
```

**Detection Pattern:**
```regex
sealed\s+interface\s+\w+Event\s*\{
```

#### Rule: Standard State Properties
```kotlin
// Required properties
val isLoading: Boolean = false  // Required
val error: String? = null      // Required
```

**Detection Pattern:**
```regex
val\s+isLoading:\s*Boolean\s*=\s*false
val\s+error:\s*String\?\s*=\s*null
```

#### Rule: Immutability
```kotlin
// Check: No mutable state properties
val items: List<Item> = emptyList()  // ✓ Immutable
var items: List<Item> = emptyList()  // ✗ Mutable
```

**Detection Pattern:**
```regex
var\s+\w+:\s*\w+  // Flag as error in State class
```

---

### 3. UseCase Validation

#### Rule: @Composable Function
```kotlin
// Check: UseCase must be @Composable
@Composable
fun HomeUseCase(...) // ✓
fun HomeUseCase(...) // ✗ Missing @Composable
```

**Detection Pattern:**
```regex
@Composable\s+fun\s+\w+UseCase\s*\(
```

#### Rule: Standard Parameters
```kotlin
// Required parameters
fun HomeUseCase(
    initialState: HomeState,      // Required
    events: Flow<HomeEvent>,      // Required
    linkRepository: LinkRepository // Required
)
```

**Detection Pattern:**
```regex
initialState:\s*\w+State
events:\s*Flow<\w+Event>
```

#### Rule: MutableStateOf Usage
```kotlin
// Check: State variables use remember { mutableStateOf() }
var items by remember { mutableStateOf(initialState.items) } // ✓
var items = mutableStateOf(initialState.items)               // ✗ Missing remember
val items = initialState.items                                // ✗ Not reactive
```

**Detection Pattern:**
```regex
var\s+\w+\s+by\s+remember\s*\{\s*mutableStateOf\s*\(
```

#### Rule: LaunchedEffect Usage
```kotlin
// Check: Use LaunchedEffect for side effects
LaunchedEffect(Unit) { ... }  // ✓
LaunchedEffect(key) { ... }   // ✓ for reactive effects
```

**Detection Pattern:**
```regex
LaunchedEffect\s*\([^)]*\)\s*\{
```

#### Rule: Event Collection
```kotlin
// Check: Events collected in LaunchedEffect
LaunchedEffect(Unit) {
    events.collect { event ->  // ✓
        when (event) { ... }
    }
}
```

**Detection Pattern:**
```regex
events\.collect\s*\{\s*\w+\s*->\s*when\s*\(
```

#### Rule: No Android Dependencies
```kotlin
// Check: No Android imports in UseCase
import androidx.compose.ui.*     // ✗ Android UI
import android.content.Context  // ✗ Android
import androidx.compose.runtime.*  // ✓ Compose runtime OK
```

**Forbidden Imports:**
```regex
android\.content\.
android\.os\.
android\.view\.
androidx\.compose\.ui\.
androidx\.compose\.foundation\.
androidx\.compose\.material[23]?\. [except basic types]
```

---

### 4. ViewModel Validation

#### Rule: ViewModel Inheritance
```kotlin
// Check: Must extend ViewModel
class HomeViewModel : ViewModel() // ✓
class HomeViewModel               // ✗ Missing ViewModel
```

**Detection Pattern:**
```regex
class\s+\w+ViewModel\s*:\s*ViewModel\s*\(
```

#### Rule: KoinComponent Implementation
```kotlin
// Check: Must implement KoinComponent
class HomeViewModel : ViewModel(), KoinComponent // ✓
class HomeViewModel : ViewModel()                  // ✗ Missing KoinComponent
```

**Detection Pattern:**
```regex
:\s*ViewModel\s*\(\s*\)\s*,\s*KoinComponent
```

#### Rule: Molecule Usage
```kotlin
// Check: Must use moleculeFlow
moleculeFlow(RecompositionMode.Immediate) { ... }  // ✓
moleculeFlow(RecompositionMode.Context) { ... }    // ✗ Wrong mode
```

**Detection Pattern:**
```regex
moleculeFlow\s*\(\s*RecompositionMode\.Immediate\s*\)
```

#### Rule: StateIn Configuration
```kotlin
// Check: Proper stateIn setup
.stateIn(
    scope = viewModelScope,
    started = SharingStarted.Lazily,
    initialValue = initialState
)
```

**Detection Pattern:**
```regex
\.stateIn\s*\(
\s*scope\s*=\s*viewModelScope
\s*started\s*=\s*SharingStarted\.Lazily
\s*initialValue\s*=\s*initialState
```

#### Rule: MutableSharedFlow Events
```kotlin
// Check: Events flow must be MutableSharedFlow
private val eventsFlow: MutableSharedFlow<Event> = MutableSharedFlow(...)
```

**Detection Pattern:**
```regex
MutableSharedFlow<\w+Event>
```

#### Rule: Koin Injection
```kotlin
// Check: Repositories injected with Koin
private val repository: Repository by inject()  // ✓
private val repository = Repository()           // ✗ Manual instantiation
```

**Detection Pattern:**
```regex
by\s+inject\s*\(\s*\)
```

---

### 5. Screen Validation

#### Rule: @Composable Function
```kotlin
// Check: Screen must be @Composable
@Composable
fun HomeScreen(...) // ✓
fun HomeScreen(...) // ✗ Missing @Composable
```

**Detection Pattern:**
```regex
@Composable\s+fun\s+\w+Screen\s*\(
```

#### Rule: NavKey Parameter
```kotlin
// Check: First parameter must be routeKey
fun HomeScreen(
    routeKey: NavKey,  // ✓ Required first param
    onNavigate: () -> Unit
)
```

**Detection Pattern:**
```regex
fun\s+\w+Screen\s*\(
\s*routeKey:\s*NavKey
```

#### Rule: ViewModel Factory
```kotlin
// Check: Must use viewModel() factory
val viewModel: HomeViewModel = viewModel<HomeViewModel>(
    key = routeKey.toString()
) { HomeViewModel() }
```

**Detection Pattern:**
```regex
viewModel<\w+ViewModel>\s*\(
\s*key\s*=\s*routeKey\.toString\s*\(\s*\)
\s*\)\s*\{\s*\w+ViewModel\s*\(.*
```

#### Rule: CollectAsState
```kotlin
// Check: State collected with collectAsState
val state by viewModel.states.collectAsState() // ✓
val state = viewModel.states.value            // ✗ Not reactive
```

**Detection Pattern:**
```regex
collectAsState\s*\(\s*\)
```

---

## Architecture Compliance Score Calculator

### Scoring Algorithm

```kotlin
data class ArchitectureScore(
    val totalPoints: Int = 45,
    val earnedPoints: Int = 0,
    val percentage: Double = 0.0,
    val status: ComplianceStatus
)

enum class ComplianceStatus {
    COMPLIANT,       // 90%+
    NEEDS_WORK,      // 70-89%
    NON_COMPLIANT    // <70%
}

fun calculateScore(screenName: String): ArchitectureScore {
    var points = 0

    // File Structure (5 points)
    if (hasAllRequiredFiles(screenName)) points += 5

    // StateModels (10 points)
    points += checkStateModelsCompliance(screenName)

    // UseCase (12 points)
    points += checkUseCaseCompliance(screenName)

    // ViewModel (10 points)
    points += checkViewModelCompliance(screenName)

    // Screen (8 points)
    points += checkScreenCompliance(screenName)

    // Integration (5 points)
    points += checkIntegrationCompliance(screenName)

    // Common Patterns (5 points)
    points += checkPatternCompliance(screenName)

    val percentage = (points.toDouble() / 45.0) * 100
    val status = when {
        percentage >= 90 -> ComplianceStatus.COMPLIANT
        percentage >= 70 -> ComplianceStatus.NEEDS_WORK
        else -> ComplianceStatus.NON_COMPLIANT
    }

    return ArchitectureScore(
        totalPoints = 45,
        earnedPoints = points,
        percentage = percentage,
        status = status
    )
}
```

---

## Detection Commands

### Find Non-Compliant Screens

```bash
#!/bin/bash

# Find all screen directories
for screen_dir in app/src/commonMain/kotlin/com/greenrobotdev/linklibrary/screens/*/; do
    screen_name=$(basename "$screen_dir")

    echo "Checking $screen_name..."

    # Check for required files
    missing_files=()
    [ ! -f "${screen_dir}${screen_name}StateModels.kt" ] && missing_files+=("StateModels")
    [ ! -f "${screen_dir}${screen_name}UseCase.kt" ] && missing_files+=("UseCase")
    [ ! -f "${screen_dir}${screen_name}ViewModel.kt" ] && missing_files+=("ViewModel")
    [ ! -f "${screen_dir}${screen_name}Screen.kt" ] && missing_files+=("Screen")

    if [ ${#missing_files[@]} -gt 0 ]; then
        echo "  ✗ Missing: ${missing_files[*]}"
    else
        echo "  ✓ All files present"
    fi

    # Check for @Serializable in StateModels
    if [ -f "${screen_dir}${screen_name}StateModels.kt" ]; then
        if grep -q "@Serializable" "${screen_dir}${screen_name}StateModels.kt"; then
            echo "  ✓ @Serializable present"
        else
            echo "  ✗ Missing @Serializable"
        fi
    fi

    # Check for sealed interface events
    if [ -f "${screen_dir}${screen_name}StateModels.kt" ]; then
        if grep -q "sealed interface.*Event" "${screen_dir}${screen_name}StateModels.kt"; then
            echo "  ✓ Sealed interface events"
        else
            echo "  ✗ Events not sealed interface"
        fi
    fi

    echo ""
done
```

### Check for Architecture Violations

```bash
#!/bin/bash

# Check for forbidden patterns

echo "Checking for architecture violations..."

# Check for 'var' in StateModels
echo "Checking for mutable state in StateModels..."
for file in app/src/commonMain/kotlin/com/greenrobotdev/linklibrary/screens/*/*StateModels.kt; do
    if grep -q "var " "$file"; then
        echo "  ✗ $file contains mutable state"
    fi
done

# Check for Android dependencies in UseCase
echo "Checking for Android dependencies in UseCase..."
for file in app/src/commonMain/kotlin/com/greenrobotdev/linklibrary/screens/*/*UseCase.kt; do
    if grep -q "import androidx.compose.ui" "$file"; then
        echo "  ✗ $file has UI dependencies"
    fi
    if grep -q "import android.content" "$file"; then
        echo "  ✗ $file has Android dependencies"
    fi
done

# Check for missing KoinComponent in ViewModel
echo "Checking for KoinComponent in ViewModels..."
for file in app/src/commonMain/kotlin/com/greenrobotdev/linklibrary/screens/*/*ViewModel.kt; do
    if ! grep -q "KoinComponent" "$file"; then
        echo "  ✗ $file missing KoinComponent"
    fi
done

# Check for moleculeFlow usage
echo "Checking for Molecule usage..."
for file in app/src/commonMain/kotlin/com/greenrobotdev/linklibrary/screens/*/*ViewModel.kt; do
    if ! grep -q "moleculeFlow" "$file"; then
        echo "  ✗ $file missing moleculeFlow"
    fi
done

# Check for collectAsState in Screen
echo "Checking for collectAsState in Screens..."
for file in app/src/commonMain/kotlin/com/greenrobotdev/linklibrary/screens/*/*Screen.kt; do
    if ! grep -q "collectAsState" "$file"; then
        echo "  ✗ $file missing collectAsState"
    fi
done
```

---

## Pattern Validation Matrix

| Component | Pattern | Required | Detection |
|-----------|---------|----------|-----------|
| StateModels | `@Serializable` | ✓ | Annotation check |
| StateModels | `data class` | ✓ | Class modifier check |
| StateModels | `sealed interface` | ✓ | Interface type check |
| StateModels | `isLoading: Boolean` | ✓ | Property check |
| StateModels | `error: String?` | ✓ | Property check |
| StateModels | Immutability | ✓ | `var` check |
| UseCase | `@Composable` | ✓ | Annotation check |
| UseCase | `initialState` param | ✓ | Parameter check |
| UseCase | `events: Flow` param | ✓ | Parameter check |
| UseCase | `mutableStateOf` | ✓ | Function call check |
| UseCase | `LaunchedEffect` | ✓ | Function call check |
| UseCase | `events.collect` | ✓ | Method call check |
| UseCase | No Android deps | ✓ | Import check |
| ViewModel | `: ViewModel()` | ✓ | Inheritance check |
| ViewModel | `KoinComponent` | ✓ | Interface check |
| ViewModel | `moleculeFlow` | ✓ | Function check |
| ViewModel | `stateIn` | ✓ | Function check |
| ViewModel | `MutableSharedFlow` | ✓ | Type check |
| ViewModel | `by inject()` | ✓ | Delegate check |
| Screen | `@Composable` | ✓ | Annotation check |
| Screen | `routeKey: NavKey` | ✓ | Parameter check |
| Screen | `viewModel()` factory | ✓ | Function check |
| Screen | `collectAsState()` | ✓ | Method check |

---

## Quick Validation Commands

### Validate Single Screen
```bash
# Check a specific screen
./validate-screen.sh home
```

### Validate All Screens
```bash
# Check all screens in project
./validate-all-screens.sh
```

### Generate Report
```bash
# Generate compliance report
./generate-compliance-report.sh > architecture-report.md
```

---

## Integration with CI/CD

### GitHub Actions Example

```yaml
name: Architecture Validation

on: [pull_request]

jobs:
  validate-architecture:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Validate Architecture
        run: |
          chmod +x .claude/skills/architecture-standardize/scripts/validate-all-screens.sh
          .claude/skills/architecture-standardize/scripts/validate-all-screens.sh
      - name: Upload Report
        uses: actions/upload-artifact@v2
        with:
          name: architecture-report
          path: architecture-report.md
```

---

## Automated Fix Suggestions

### Common Issues and Fixes

| Issue | Detection | Fix |
|-------|-----------|-----|
| Missing @Serializable | No annotation in State class | Add `@Serializable` |
| Mutable state | `var` in State class | Change to `val` |
| No Android deps | UI imports in UseCase | Remove UI imports |
| Missing KoinComponent | No interface in ViewModel | Add `, KoinComponent` |
| No moleculeFlow | Manual state in ViewModel | Add `moleculeFlow()` wrapper |
| No collectAsState | Direct state access in Screen | Change to `collectAsState()` |

---

## Validator Output Format

### JSON Output
```json
{
  "screen": "Home",
  "status": "COMPLIANT",
  "score": 43,
  "total": 45,
  "percentage": 95.6,
  "issues": [],
  "warnings": [
    {
      "component": "UseCase",
      "message": "Consider extracting validation logic",
      "severity": "INFO"
    }
  ],
  "validations": {
    "fileStructure": true,
    "stateModels": true,
    "useCase": true,
    "viewModel": true,
    "screen": true,
    "integration": true,
    "patterns": true
  }
}
```

### Markdown Output
```markdown
# Architecture Validation Report

## Home Screen
**Status**: ✅ COMPLIANT (95.6%)

### Component Scores
- File Structure: 5/5 ✓
- StateModels: 10/10 ✓
- UseCase: 11/12 ✓
- ViewModel: 10/10 ✓
- Screen: 8/8 ✓
- Integration: 5/5 ✓
- Patterns: 5/5 ✓

### Warnings
- INFO: Consider extracting validation logic in UseCase
```

---

This validator provides automated detection of architecture compliance issues. Use it to quickly identify screens that need refactoring.
