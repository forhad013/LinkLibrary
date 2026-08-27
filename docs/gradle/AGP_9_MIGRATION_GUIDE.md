# AGP 9 Migration Guide

## Overview
This guide documents the complete migration of the Link Library KMP project from AGP 8.x to AGP 9.0, including all issues encountered and solutions implemented.

## Final Version Configuration (2025-08-27)

```toml
[versions]
agp = "9.1.0"           # Android Gradle Plugin
kotlin = "2.4.10"        # Kotlin compiler
ksp = "2.3.11"           # Kotlin Symbol Processing
room = "2.8.4"           # Room KMP Database
compose-plugin = "1.12.0" # Compose Multiplatform
gradle = "9.3.1"         # Gradle wrapper
```

## Critical Changes & Issues Resolved

### 1. Convention Plugin Architecture

#### Problem
AGP 9 introduced breaking changes where the traditional `kotlin("multiplatform")` + `com.android.library` plugin combination became incompatible.

#### Solution
Created two specialized convention plugins:

**KmpLibraryConventionPlugin** - For KMP modules using Compose:
```kotlin
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "com.android.kotlin.multiplatform.library")
        apply(plugin = "org.jetbrains.compose")
        apply(plugin = "org.jetbrains.kotlin.plugin.compose")
    }
}
```

**AndroidRoomConventionPlugin** - For KMP modules using Room without Compose:
```kotlin
class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "com.android.kotlin.multiplatform.library")
        apply(plugin = "com.google.devtools.ksp")
        apply(plugin = "androidx.room")
    }
}
```

### 2. DSL Syntax Changes

#### Problem
AGP 9 changed how Android configuration is specified in KMP modules.

#### Solution
Use `android {}` inside `kotlin {}` block:
```kotlin
kotlin {
    android {
        namespace = "com.example.module"
        compileSdk = 37
    }
    androidTarget { }
}
```

### 3. Compose Compiler Integration

#### Problem
Compose compiler configuration moved from AGP to Kotlin plugin.

#### Solution
Configure via Kotlin Compose plugin:
```kotlin
plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

composeCompiler {
    metricsDestination = layout.buildDirectory.dir("compose-compiler-reports")
}
```

### 4. Compiler Options Configuration

#### Problem
`kotlinOptions` removed in AGP 9 KMP modules.

#### Solution
Use `compilerOptions` in `androidTarget` block:
```kotlin
androidTarget {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}
```

### 5. KSP Version Compatibility

#### Problem
KSP version must match Kotlin major version. Initial attempt with KSP 2.3.11 + Kotlin 2.4.10 appeared incompatible.

#### Solution
Research revealed KSP 2.3.10+ is actually compatible with Kotlin 2.4.x. The final working combination:
- Kotlin 2.4.10 + KSP 2.3.11 ✅

### 6. Convention Plugin Registration

#### Problem
Convention plugins weren't discoverable by Gradle.

#### Solution
Register plugins in `build-logic/build.gradle.kts`:
```kotlin
gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "convention.kmp-library"
            implementationClass = "convention.KmpLibraryConventionPlugin"
        }
        register("androidRoom") {
            id = "convention.android-room"
            implementationClass = "convention.AndroidRoomConventionPlugin"
        }
    }
}
```

### 7. Compose Runtime Missing Error

#### Problem
Database module applied Compose compiler but had no Compose dependencies:
```
The Compose Compiler requires the Compose Runtime to be on the class path, but none could be found.
```

#### Solution
Database module uses only `AndroidRoomConventionPlugin` (no Compose):
```kotlin
plugins {
    alias(libs.plugins.convention.android.room) // Only this
}
```

### 8. Room KSP Configuration

#### Problem
Hardcoded Room versions caused maintenance issues.

#### Solution
Use version catalog references:
```kotlin
dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
}
```

## Module Convention Usage

### Compose KMP Modules
Use `KmpLibraryConventionPlugin`:
- ✅ app
- ✅ bookmarks  
- ✅ core:design
- ✅ core:utils

### Non-Compose KMP Modules
Use `AndroidRoomConventionPlugin`:
- ✅ database

### Build Logic Dependencies
Keep aligned with project versions:
```kotlin
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.10")
    implementation("com.android.tools.build:gradle:9.1.0")
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.3.11")
    implementation("androidx.room:androidx.room.gradle.plugin:2.8.4")
}
```

## Testing & Validation

### Key Commands
```bash
# Test specific modules
./gradlew :database:build
./gradlew :core:design:build
./gradlew :app:build

# Full project build
./gradlew clean build

# KSP processing test
./gradlew :database:kspAndroidMain
```

### Expected Results
- ✅ No plugin resolution errors
- ✅ No Compose compiler errors for non-Compose modules
- ✅ Room KSP annotation processing completes successfully
- ✅ All modules compile and build successfully

## Important Notes

### Version Compatibility Matrix
| Component | Version | Compatible With |
|-----------|---------|----------------|
| AGP | 9.1.0 | Kotlin 2.4.x, Gradle 9.1+ |
| Kotlin | 2.4.10 | AGP 9.x, KSP 2.3.11 |
| KSP | 2.3.11 | Kotlin 2.3.x - 2.4.x |
| Compose Plugin | 1.12.0 | Kotlin 2.4.x |

### Breaking Changes from AGP 8.x
1. `kotlin("multiplatform")` + `com.android.library` no longer compatible
2. Must use `com.android.kotlin.multiplatform.library` instead
3. `kotlinOptions` replaced by `compilerOptions`
4. Compose configuration moved to Kotlin plugin
5. Both `android {}` and `androidTarget {}` blocks required

## Migration Timeline

### Initial State (Pre-Migration)
- AGP: 8.9.1
- Kotlin: 2.1.0
- KSP: 2.1.0-1.0.29
- Convention plugins: Basic multiplatform setup

### Migration Process
1. Updated AGP to 9.0.0
2. Updated Kotlin to 2.2.10
3. Updated KSP to 2.2.10-2.0.2
4. Created specialized convention plugins
5. Updated all modules to use new DSL syntax
6. Fixed compatibility issues incrementally

### Final State (Post-Migration)
- AGP: 9.1.0
- Kotlin: 2.4.10
- KSP: 2.3.11
- Convention plugins: Purpose-built for Compose vs Room modules

## Lessons Learned

### 1. Convention Plugin Separation
**Lesson**: Not all KMP modules need the same plugins. Create specialized convention plugins for different use cases.

**Application**: Separate plugins for Compose modules vs Room-only modules prevents dependency conflicts.

### 2. Version Compatibility Research
**Lesson**: Don't assume version incompatibility without research. KSP 2.3.11 works with Kotlin 2.4.x despite version number mismatch.

**Application**: Always check official compatibility matrices and GitHub releases before downgrading versions.

### 3. DSL Syntax Evolution
**Lesson**: AGP 9 introduced significant DSL changes that aren't always documented clearly.

**Application**: Reference official AGP 9 migration guides and sample projects for correct syntax.

### 4. Compiler Options Migration
**Lesson**: Compiler configuration moved from AGP to Kotlin in AGP 9.

**Application**: Use `compilerOptions` in target-specific blocks, not global `kotlinOptions`.

### 5. Plugin Registration Requirement
**Lesson**: Convention plugins must be explicitly registered in build-logic module.

**Application**: Always include `gradlePlugin {}` registration block in convention plugin projects.

## Future Maintenance

### Version Update Strategy
1. Check AGP compatibility with new Kotlin versions
2. Verify KSP compatibility matrix before updates  
3. Test convention plugins with version changes
4. Update build-logic dependencies alongside main project

### Monitoring Required
- Room KMP alpha/beta releases
- Compose Multiplatform updates
- KSP compatibility announcements
- AGP preview releases

## Related Documentation
- [Compose Convention Plugin Setup](./CONVENTION_PLUGIN_SETUP.md)
- [Gradle Build Logic](../build-logic/README.md)
- [AGP 9 Release Notes](https://developer.android.com/build/releases/past-releases/agp-9-0)
- [KSP Compatibility Matrix](https://github.com/google/ksp/releases)

## Status
✅ **Migration Complete** - All modules successfully migrated to AGP 9.1.0 with Kotlin 2.4.10

---
**Last Updated**: 2025-08-27  
**Migration Branch**: agp9-migration  
**Primary Author**: Claude Code + User Collaboration