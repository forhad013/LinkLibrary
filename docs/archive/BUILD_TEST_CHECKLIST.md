# AGP 9 Migration Build Testing Checklist

## 🎯 Testing Objectives
- Verify all modules compile successfully with AGP 9.0
- Validate KSP Room database generation
- Test WASM target compilation
- Confirm automated build testing system is working

## ✅ Pre-Build Verification

### **Java Environment**
- [ ] Java 21+ installed: `java -version`
- [ ] JAVA_HOME set: `echo $JAVA_HOME`
- [ ] Gradle recognizes Java 21: `./gradlew --version`

### **Project Files**
- [ ] All build.gradle.kts files use AGP 9 syntax
- [ ] Plugin ordering correct (AGP before Compose)
- [ ] JVM target set to JVM_21
- [ ] KSP dependencies configured for all targets

### **Clean Environment**
- [ ] Previous builds cleaned: `./gradlew clean`
- [ ] Gradle daemon stopped: `./gradlew --stop`
- [ ] Build cache cleared if needed

## 🔍 Module-by-Module Testing

### **1. Database Module** `:database`
```bash
./gradlew :database:compileKotlinMetadata
```

**Expected:** Successful compilation

**Common Issues:**
- KSP configuration errors → Check `add("kspAndroid", ...)` syntax
- Room constructor errors → Verify KSP generated files
- Dependency conflicts → Check libs.versions.toml versions

### **2. Design Module** `:core:design`
```bash
./gradlew :core:design:compileKotlinMetadata
```

**Expected:** Successful Material 3 theme compilation

### **3. Utils Module** `:core:utils`
```bash
./gradlew :core:utils:compileKotlinMetadata
```

**Expected:** Successful MoleculeViewModel compilation

### **4. Bookmarks Module** `:bookmarks`
```bash
./gradlew :bookmarks:compileKotlinMetadata
```

**Expected:** Successful bookmark models compilation

### **5. App Module** `:app`
```bash
./gradlew :app:compileKotlinMetadata
```

**Expected:** Successful main application compilation

### **6. WASM Target** `:app:wasmJs`
```bash
./gradlew :app:compileWasmJsKotlinMetadata
```

**Expected:** Successful WASM compilation (fixed test dependency issue)

## 🚨 Known Issues & Solutions

### **Issue: KSP Configuration Not Found**
**Error:** `Configuration with name 'kspAndroid' not found`

**Solution:** Add to `dependencies {}` block:
```kotlin
add("kspCommonMainMetadata", libs.androidx.room.compiler)
add("kspAndroid", libs.androidx.room.compiler)
add("kspJvm", libs.androidx.room.compiler)
```

### **Issue: WASM Test Dependency**
**Error:** `Could not resolve org.jetbrains.kotlin:kotlin-test-js`

**Solution:** Remove from `wasmJsTest` dependencies:
```kotlin
// OLD (deprecated):
implementation(kotlin("test-js"))

// NEW (correct):
// Dependencies inherited from commonTest, no explicit declaration needed
```

### **Issue: Plugin Ordering**
**Error:** `Unresolved reference 'namespace'`

**Solution:** Ensure correct plugin order:
```kotlin
plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")  // BEFORE Compose
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}
```

## 🎯 Full Build Test

```bash
# Run the automated build test script
./test-build.sh
```

**Expected Output:**
```
🚀 Starting AGP 9 Migration Build Test
=========================================

📋 Checking Java installation...
Java version: 21
✅ Java check completed

🧹 Cleaning previous builds...
✅ Build clean completed

🔍 Testing individual modules...
Testing module: database
✅ Module database compilation successful
Testing module: core:design
✅ Module core:design compilation successful
Testing module: core:utils
✅ Module core:utils compilation successful
Testing module: bookmarks
✅ Module bookmarks compilation successful
Testing module: app
✅ Module app compilation successful

🔍 Testing KSP Room database generation...
✅ KSP Room database generation successful

🔍 Testing WASM compilation...
✅ WASM compilation successful

🎯 Running full build test...
✅ Full build successful! 🎉

📊 Build Summary:
  • AGP 9 Migration: ✅ Complete
  • KSP Configuration: ✅ Working
  • All Modules: ✅ Building successfully
  • WASM Target: ✅ Compiling
```

## 📊 Success Criteria

**Migration is successful when:**
1. ✅ All 5 modules compile without errors
2. ✅ KSP generates Room database implementations
3. ✅ WASM target compiles successfully
4. ✅ Full `./gradlew build` command succeeds
5. ✅ No warnings about deprecated configurations
6. ✅ All tests pass (if applicable)

## 🔄 Post-Migration Steps

### **1. Verify Functionality**
- [ ] Run Android app: `./gradlew :androidApp:assembleDebug`
- [ ] Test desktop app: `./gradlew :app:desktopApp:installDist`
- [ ] Verify database operations work
- [ ] Check all screens render correctly

### **2. Performance Testing**
- [ ] Build time comparison (pre/post migration)
- [ ] APK size comparison
- [ ] Runtime performance verification

### **3. Documentation Update**
- [ ] Update CLAUDE.md with final migration notes
- [ ] Update README.md with AGP 9 requirements
- [ ] Document any breaking changes for contributors

## 🐛 Debug Mode Testing

If builds fail, run with detailed logging:

```bash
# Gradle build with detailed logging
./gradlew build --info --stacktrace

# KSP specific debugging
./gradlew :database:build --info | grep -i "ksp\|room"

# Compose compiler metrics
./gradlew assembleDebug --composecompiler-metrics
```

## 📝 Migration Sign-off

**Migration completed when:**
- [ ] All modules build successfully
- [ ] No deprecation warnings
- [ ] WASM target compiles
- [ ] Database KSP generation works
- [ ] Full test suite passes
- [ ] Documentation updated
- [ ] Performance baseline established

---

**Branch:** `agp9-migration`
**Target:** Merge to `main` after successful build testing
**Priority:** 🚀 Automated build testing setup