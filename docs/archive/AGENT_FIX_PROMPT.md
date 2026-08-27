# Agent Prompt: Fix Gradle Convention Plugin Issue

## Context
We are in the middle of AGP 9 (Android Gradle Plugin 9.0) migration for a Kotlin Multiplatform (KMP) project. The main AGP 9 migration is complete, but we're trying to simplify build files using convention plugins and hitting "Plugin not found" errors.

## Project Structure
- **Location**: `/Volumes/Projects/KMP project/LinkLibrary`
- **Build system**: Gradle 9.3.1 with Kotlin DSL
- **Modules**: app, database, bookmarks, core:design, core:utils, core:android
- **Build-logic**: Included build for convention plugins at `build-logic/`

## What We Have Done

### 1. Convention Plugin Architecture
Created programmed convention plugins in `build-logic/src/main/kotlin/convention/`:
- **KmpLibraryConventionPlugin.kt** - Main KMP + AGP 9 configuration
- **AndroidRoomConventionPlugin.kt** - Room KMP + KSP configuration

### 2. Plugin Registration
Created META-INF registration files:
- `build-logic/src/main/resources/META-INF/gradle-plugins/convention.kmp-library.properties`
- `build-logic/src/main/resources/META-INF/gradle-plugins/convention.android-room.properties`

### 3. Module Updates
Updated all module build.gradle.kts files to use:
- `id("convention.kmp-library")` for KMP modules
- `id("convention.android-room")` for Room database module

### 4. Build-Logic Configuration
Updated `build-logic/build.gradle.kts` with required dependencies:
```kotlin
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.10")
    implementation("org.jetbrains.compose:compose-gradle-plugin:1.7.1")
    implementation("com.android.tools.build:gradle:9.0.0")
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.2.10-2.0.2")
    implementation("androidx.room:androidx.room.gradle.plugin:2.7.0-alpha10")
}
```

### 5. Root Configuration
Root `settings.gradle.kts` includes:
```kotlin
includeBuild("build-logic")
```

## Current Issue
**Error**: `Plugin [id: 'convention.kmp-library'] was not found in any of the following sources`

**When it happens**: When running any Gradle command like:
```bash
./gradlew :core:design:tasks
./gradlew build
./gradlew :build-logic:build
```

## Expected Results

### Primary Goal
Convention plugins should be recognized and applied successfully. Running:
```bash
./gradlew :core:design:tasks
```
Should show available tasks instead of "Plugin not found" error.

### Secondary Goals
1. **Build-logic compilation**: `./gradlew :build-logic:build` should succeed
2. **Module configuration**: All modules should configure without plugin errors
3. **Build execution**: `./gradlew clean build` should work end-to-end

### Success Indicators
- ✅ No "Plugin not found" errors
- ✅ Convention plugins properly registered in Gradle
- ✅ Module build files successfully apply convention plugins
- ✅ AGP 9 migration features preserved (JVM 21, context-parameters, etc.)

## Investigation Tasks

### Phase 1: Diagnose the Issue
1. **Verify file structure**: Check if plugin classes and registration files exist
2. **Test build-logic**: Try compiling the build-logic module independently
3. **Check plugin resolution**: See if Gradle can find the plugins
4. **Examine error details**: Look for specific missing dependencies or configuration issues

### Phase 2: Identify Root Cause
Determine if the issue is:
- Plugin registration problem (META-INF files not being read)
- Classpath issue (plugin classes not being compiled)
- Dependency problem (missing required libraries)
- Configuration issue (build-logic not properly included)
- Gradle version incompatibility

### Phase 3: Implement Fix
Based on diagnosis, apply appropriate fix:
- Fix plugin registration files
- Update build-logic dependencies
- Correct plugin class implementation
- Adjust included build configuration
- Fix any syntax or configuration errors

### Phase 4: Verify Solution
Test that the fix works:
1. Run `./gradlew :core:design:tasks` - should show tasks
2. Run `./gradlew :build-logic:build` - should succeed
3. Run `./gradlew clean build` - should build successfully
4. Verify AGP 9 features still work (JVM 21, context-parameters)

## Technical Details

### Current Plugin Structure
```
build-logic/
├── src/main/kotlin/convention/
│   ├── KmpLibraryConventionPlugin.kt
│   └── AndroidRoomConventionPlugin.kt
├── src/main/resources/META-INF/gradle-plugins/
│   ├── convention.kmp-library.properties
│   └── convention.android-room.properties
└── build.gradle.kts
```

### Plugin Registration Format
Properties files contain:
```
implementation-class=convention.KmpLibraryConventionPlugin
```

### Module Usage Pattern
```kotlin
plugins {
    id("convention.kmp-library")
    // other plugins...
}
```

## Important Constraints

1. **Must preserve AGP 9 migration**: JVM 21 targets, context-parameters, proper Android configuration
2. **Maintain code reduction goal**: Keep ~80% reduction in build file complexity
3. **Support all modules**: database (with Room), bookmarks, app, core modules
4. **Work with existing dependencies**: Room KMP, Compose, KSP, etc.

## Testing Commands
Use these to verify the fix:

```bash
# Quick test
cd "/Volumes/Projects/KMP project/LinkLibrary"
./gradlew :core:design:tasks

# Build-logic test
./gradlew :build-logic:build --stacktrace

# Full configuration test
./gradlew tasks --stacktrace

# Build test
./gradlew :core:design:assembleDebug --stacktrace
```

## Output Requirements
After fixing, provide:
1. Summary of what was wrong
2. Explanation of the fix
3. Files that were changed
4. Verification that it works
5. Any remaining issues or concerns

---

**START INVESTIGATION**: Begin by checking the current file structure and running diagnostic tests to identify the root cause of the "Plugin not found" error.