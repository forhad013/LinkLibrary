# AGP 9 Troubleshooting Guide

## Common Issues and Solutions

### 1. Plugin Resolution Errors

#### "Plugin [id: 'convention.kmp-library'] was not found"
**Cause**: Convention plugin not registered in build-logic module.

**Solution**:
```kotlin
// build-logic/build.gradle.kts
gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "convention.kmp-library"
            implementationClass = "convention.KmpLibraryConventionPlugin"
        }
    }
}
```

#### "Plugin [id: 'com.google.devtools.ksp', version: 'X.X.X'] was not found"
**Cause**: KSP version doesn't exist or incompatible with repositories.

**Solution**: Check KSP GitHub releases for available versions and use compatible version:
```toml
[versions]
ksp = "2.3.11"  # Verify this version exists
```

### 2. KSP Processing Errors

#### "unexpected jvm signature V"
**Cause**: KSP version incompatible with Kotlin version.

**Solution**: Ensure KSP version matches Kotlin major version:
```toml
# Compatible combination
kotlin = "2.4.10"
ksp = "2.3.11"  # KSP 2.3.10+ works with Kotlin 2.4.x
```

#### Room KSP annotation processing failures
**Cause**: Hardcoded Room versions or missing dependencies.

**Solution**: Use version catalog references:
```kotlin
// Instead of hardcoded versions
add("kspAndroid", "androidx.room:room-compiler:2.7.0-alpha10")

// Use catalog
add("kspAndroid", libs.androidx.room.compiler)
```

### 3. Compose Compiler Errors

#### "The Compose Compiler requires the Compose Runtime to be on the class path"
**Cause**: Module applies Compose compiler plugin but has no Compose dependencies.

**Solution**: Use appropriate convention plugin:
```kotlin
// For non-Compose modules (like database)
plugins {
    alias(libs.plugins.convention.android.room)  // No Compose
}

// For Compose modules
plugins {
    alias(libs.plugins.convention.kmp.library)  // Includes Compose
}
```

#### Compose compiler metrics/reports not generating
**Cause**: Missing composeCompiler configuration block.

**Solution**: Add to root build.gradle.kts:
```kotlin
composeCompiler {
    metricsDestination = layout.buildDirectory.dir("compose-compiler-reports")
    reportsDestination = layout.buildDirectory.dir("compose-compiler-reports")
}
```

### 4. DSL Syntax Errors

#### "Unresolved reference: defaultConfig"
**Cause**: Using old AGP 8.x DSL syntax in AGP 9.

**Solution**: Simplify android {} block:
```kotlin
// AGP 9 Style
kotlin {
    android {
        namespace = "com.example.module"
        compileSdk = 37
    }
}
```

#### "Unresolved reference: kotlinOptions"
**Cause**: kotlinOptions removed in AGP 9 KMP modules.

**Solution**: Use compilerOptions in androidTarget:
```kotlin
androidTarget {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}
```

#### "Extension of type 'LibraryExtension' does not exist"
**Cause**: Trying to configure AGP extensions that don't exist in KMP context.

**Solution**: Remove configure<LibraryExtension> blocks, configure in android {} block directly.

### 5. Convention Plugin Issues

#### Convention plugin applies unwanted plugins
**Cause**: Single convention plugin used for all module types.

**Solution**: Create specialized convention plugins:
- `KmpLibraryConventionPlugin` - For Compose KMP modules
- `AndroidRoomConventionPlugin` - For Room KMP modules (no Compose)

#### Convention plugin changes not reflected
**Cause**: Gradle daemon caching old convention plugin versions.

**Solution**:
```bash
./gradlew --stop
./gradlew clean build
```

### 6. Dependency Resolution Issues

#### Platform-specific dependencies not resolving
**Cause**: Dependencies declared in wrong source set.

**Solution**: Ensure platform-specific dependencies in correct source sets:
```kotlin
sourceSets {
    val commonMain by getting { /* shared dependencies */ }
    val androidMain by getting { /* Android-specific */ }
    val jvmMain by getting { /* JVM-specific */ }
}
```

#### Transitive dependency conflicts
**Cause**: Multiple libraries bringing different versions of same dependency.

**Solution**: Use dependency resolution rules:
```kotlin
configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-stdlib:2.4.10")
    }
}
```

### 7. Build Performance Issues

#### Slow KSP processing
**Cause**: KSP processing all files incrementally.

**Solution**: Enable KSP incremental processing:
```kotlin
configure<KspExtension> {
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}
```

#### Gradle daemon memory issues
**Cause**: Large project with many modules exhausting daemon memory.

**Solution**: Increase Gradle daemon memory:
```properties
# gradle.properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=1024m
```

### 8. Version Compatibility Matrix

### Known Working Combinations
| AGP | Kotlin | KSP | Compose Plugin | Status |
|-----|--------|-----|----------------|---------|
| 9.1.0 | 2.4.10 | 2.3.11 | 1.12.0 | ✅ Tested |
| 9.0.0 | 2.2.10 | 2.2.10-2.0.2 | 1.7.1 | ✅ Tested |
| 9.1.0 | 2.3.11 | 2.3.11 | 1.12.0 | ✅ Compatible |

### Incompatible Combinations
| AGP | Kotlin | KSP | Issue |
|-----|--------|-----|-------|
| 9.x | 2.4.x | 2.3.x - older | May cause KSP processing errors |
| 9.x | Any | 2.4.x-1.0.25 | Version doesn't exist |
| 8.x | 2.4.x | Any | AGP 8 incompatible with Kotlin 2.4.x |

## Diagnostic Commands

### Check Plugin Resolution
```bash
./gradlew projects
./gradlew dependencies --configuration compileClasspath
```

### Check KSP Processing
```bash
./gradlew :database:kspAndroidMain --info
./gradlew :database:kspAndroidMain --debug
```

### Check Compose Compiler
```bash
./gradlew assembleDebug --composecompiler-metrics
./gradlew assembleDebug --composecompiler-reports
```

### Check Version Compatibility
```bash
./gradlew --version
./gradlew dependencies | grep -E "kotlin|ksp|agp"
```

## Quick Fixes

### Reset Build Environment
```bash
# Stop daemon, clean, rebuild
./gradlew --stop
rm -rf .gradle build
./gradlew clean build
```

### Verify Convention Plugins
```bash
# Check if plugins are registered
./gradlew plugins
./gradlew help
```

### Test Individual Modules
```bash
# Test specific module
./gradlew :module-name:build

# Test with stack traces
./gradlew build --stacktrace
```

## Prevention Strategies

### 1. Version Testing Before Updates
Always test version combinations in isolation before updating:
```toml
[versions]
# Test one version at a time
kotlin = "2.4.10"  # Test this first
ksp = "2.3.11"     # Then this
```

### 2. Incremental Migration
Migrate modules one at a time to isolate issues:
```bash
# Test single module first
./gradlew :database:build
# Then migrate next module
```

### 3. Dependency Version Locking
Lock versions during migration to prevent conflicts:
```kotlin
dependencies {
    implementation("com.example:library:1.0.0") {
        version { strictly("1.0.0") }
    }
}
```

### 4. Build Logic Testing
Test convention plugins in separate project first:
```bash
# Create test project with same plugins
# Verify before applying to main project
```

## When to Seek Help

### Unresolved Issues
If issues persist after trying all solutions:

1. **Check GitHub Issues**: Search AGP, Kotlin, KSP repositories
2. **Verify Compatibility**: Check official compatibility matrices
3. **Minimal Reproduction**: Create minimal project reproducing issue
4. **Version Context**: Always provide exact versions when seeking help

### Information to Provide
When reporting issues, include:
```bash
./gradlew --version
./gradlew dependencies
./gradlew build --stacktrace --debug
```

## Related Resources
- [AGP 9 Migration Guide](./AGP_9_MIGRATION_GUIDE.md)
- [KSP GitHub Issues](https://github.com/google/ksp/issues)
- [Kotlin GitHub Issues](https://github.com/JetBrains/kotlin/issues)
- [AGP GitHub Issues](https://github.com/android/MainBuilds/issues)

---
**Last Updated**: 2025-08-27  
**Maintained By**: Link Library Development Team