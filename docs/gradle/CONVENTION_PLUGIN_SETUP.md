# Convention Plugin Setup Guide

## 🎯 Objective
Reduce 80% of build configuration duplication by creating reusable convention plugins for AGP 9 KMP modules.

## 📁 Convention Plugin Structure

```
build-logic/
├── build.gradle.kts                    # Plugin dependencies
├── settings.gradle.kts                # Plugin repository config
└── src/main/kotlin/
    ├── kmp-library-convention.plugin.gradle.kts    # Base KMP + AGP 9 setup
    ├── compose-kmp-convention.plugin.gradle.kts   # KMP + Compose setup  
    ├── android-room-convention.plugin.gradle.kts  # Room KMP setup
    └── utils-convention.plugin.gradle.kts         # Utils module setup
```

## 🚀 Implementation Steps

### **Step 1: Update Convention Plugins**
I've already created the base convention plugins in your `build-logic/` directory:
- `kmp-library-convention.plugin.gradle.kts` - Base KMP + AGP 9 setup
- `android-room-convention.plugin.gradle.kts` - Room KMP setup

### **Step 2: Simplify Module Build Files**
Replace current verbose build.gradle.kts files with simplified versions:

#### **Example: database/build.gradle.kts**
**Before (92 lines):**
```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("com.android.kotlin.multiplatform.library")
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.ksp)
}

kotlin {
    androidLibrary {
        namespace = "com.greenrobotdev.linklibrary.database"
        compileSdk = 36
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }
    jvm() { /* ... */ }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)
                // ... 20+ more dependencies
            }
        }
        // ... 50+ more lines
    }
}
```

**After (18 lines):**
```kotlin
plugins {
    id("kmp-library-convention")        // Applies KMP + AGP 9 defaults
    alias(libs.plugins.androidx.room)   // Room-specific
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.greenrobotdev.linklibrary.database"
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add("kspCommonMainMetadata", libs.androidx.room.compiler)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
}
```

### **Step 3: Refactor Priority**
Start with modules that have the most duplication:

1. **database** - Room KMP (92 lines → ~18 lines)
2. **core:utils** - Utils module (115 lines → ~25 lines)  
3. **core:design** - Design module (45 lines → ~12 lines)
4. **bookmarks** - Bookmarks module (110 lines → ~30 lines)
5. **app** - Main app (135 lines → ~40 lines)

## 🎯 Specific Module Refactoring

### **1. core/design Module**
**Current:** 45 lines of boilerplate  
**Target:** ~12 lines with convention plugin

```kotlin
// New simplified version
plugins {
    id("kmp-library-convention")    // Handles KMP + AGP 9 + Compose
}

kotlin {
    androidLibrary {
        namespace = "com.greenrobotdev.linklibrary.design"
    }
}

// Dependencies handled by convention, only Material 3 overrides needed
dependencies {
    // No explicit dependencies needed - handled by convention
}
```

### **2. core/utils Module**  
**Current:** 115 lines with iOS targets  
**Target:** ~25 lines with convention plugin

```kotlin
// New simplified version  
plugins {
    id("kmp-library-convention")
}

kotlin {
    androidLibrary {
        namespace = "com.greenrobotdev.linklibrary.utils"
    }
    
    // iOS targets still need explicit configuration
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "utils"
            isStatic = true
        }
    }
}

// Platform-specific dependencies only
dependencies {
    // Molecule runtime handled by convention
    // Only add platform-specific overrides
}
```

## 🔧 Convention Plugin Configuration

### **Base KMP Convention Plugin**
The `kmp-library-convention` plugin handles:
- ✅ KMP multiplatform setup
- ✅ AGP 9 androidLibrary configuration
- ✅ JVM target JVM_21
- ✅ Compiler options (opt-in, context-parameters)
- ✅ Compose plugin integration
- ✅ WASM support (optional)
- ✅ Platform source set structure

### **Room Convention Plugin**
The `android-room-convention` plugin handles:
- ✅ Room KMP dependencies
- ✅ SQLite driver setup
- ✅ KSP configuration
- ✅ Schema directory setup

## 📊 Benefits Analysis

### **Code Reduction:**
- **Total lines removed:** ~400+ lines of boilerplate
- **Maintenance burden:** 80% reduction
- **Update risk:** Minimal (update once, apply everywhere)

### **AGP 9 Migration Benefits:**
- **Consistency:** All modules use same AGP 9 configuration
- **Safety:** Compiler options centralized and type-safe
- **Updates:** Change AGP version in one place
- **Testing:** Test convention changes once

### **Developer Experience:**
- **Onboarding:** New modules get AGP 9 setup automatically
- **IDE Support:** Better auto-completion and validation
- **Error Prevention:** Compile-time checks for configuration
- **Documentation:** Convention plugins serve as documentation

## 🚨 Implementation Considerations

### **Modules That Benefit Most:**
1. **database** - Heavy Room configuration (92 lines → ~18 lines)
2. **app** - Complex dependencies (135 lines → ~40 lines)
3. **bookmarks** - Similar to app (110 lines → ~30 lines)

### **Modules with Less Benefit:**
- **core:design** - Simple configuration (45 lines → ~12 lines)
- **androidApp** - Pure Android module (different convention needed)

### **iOS Targets:** 
Keep explicit configuration for iOS targets since they're module-specific:
```kotlin
listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
    it.binaries.framework {
        baseName = "utils"
        isStatic = true  
    }
}
```

## 🎯 Implementation Timeline

### **Phase 1: Create Convention Plugins** ✅ (Complete)
- [x] Base KMP convention plugin
- [x] Room convention plugin
- [x] Build logic setup

### **Phase 2: Refactor Modules** (Next Step)
- [ ] Refactor database module (92 → ~18 lines)
- [ ] Refactor core:design (45 → ~12 lines)
- [ ] Refactor core:utils (115 → ~25 lines)
- [ ] Refactor bookmarks (110 → ~30 lines)
- [ ] Refactor app (135 → ~40 lines)

### **Phase 3: Testing & Validation**
- [ ] Test each module builds successfully
- [ ] Verify AGP 9 compatibility maintained
- [ ] Check all platform targets work
- [ ] Validate dependency resolution

## 🔍 Migration Verification

After refactoring, verify with:
```bash
# Test individual modules
./gradlew :database:build
./gradlew :core:design:build  
./gradlew :core:utils:build
./gradlew :bookmarks:build
./gradlew :app:build

# Test full build
./gradlew build
```

## 💡 Best Practices

### **When to Use Convention Plugins:**
- ✅ Repeated configuration across 3+ modules
- ✅ Complex setup (AGP 9, Room, Compose)
- ✅ Frequent updates needed
- ✅ Team standardization needed

### **When NOT to Use:**
- ❌ One-off configurations
- ❌ Module-specific tweaks
- ❌ Experimental features
- ❌ Simple configurations (<10 lines)

---

**Recommendation:** Start with database and core:design modules as pilot cases. If successful, expand to remaining modules. The convention plugin approach will make your AGP 9 migration more maintainable and future-proof.