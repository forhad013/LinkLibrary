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
# Android
./gradlew app:assembleDebug

# Run tests
./gradlew test

# Check dependencies
./gradlew app:dependencies
```

### Key Dependencies
- Room KMP: `androidx.room:room-runtime`
- Navigation: `androidx.navigation:navigation-compose`
- Koin: `io.insert-koin:koin-core`
- Molecule: `app.cash.molecule:molecule-runtime`

## Session Context Preservation
This file helps maintain context across Claude Code sessions. Update it when:
- Major architectural decisions are made
- Significant bugs are fixed
- New patterns are established
- Important code locations change

Last Updated: 2024-08-24
Session Focus: Fixed tag/collection display issues and made data loading reactive