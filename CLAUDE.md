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

## Session Context Preservation
This file helps maintain context across Claude Code sessions. Update it when:
- Major architectural decisions are made
- Significant bugs are fixed
- New patterns are established
- Important code locations change

Last Updated: 2025-08-26
Session Focus: Completed AGP 9 migration - all modules successfully migrated to AGP 9.0.0 with Kotlin 2.2.10 and Gradle 9.1.0