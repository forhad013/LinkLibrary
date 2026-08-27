# Android Studio Terminal Test Instructions for Claude

## Quick Test Command

Copy and paste this entire command into your Android Studio terminal:

```bash
cd "/Volumes/Projects/KMP project/LinkLibrary" && ./claude-build-test.sh
```

## Alternative Manual Test

If the script doesn't work, run these commands one by one in Android Studio terminal and share ALL the output:

```bash
cd "/Volumes/Projects/KMP project/LinkLibrary"
./gradlew --version
./gradlew :build-logic:build --stacktrace
./gradlew :core:design:tasks --stacktrace
./gradlew tasks --stacktrace
```

## What to Share with Claude

**Share the complete terminal output** from running the test script or manual commands. Claude needs:

1. **Plugin file structure** (Test 2 output)
2. **Build errors** (any EXCEPTION or ERROR messages)
3. **Stack traces** (the full error context)
4. **Gradle version info** (Test 4 output)

## Expected Results

**✅ SUCCESS indicators:**
- Plugin files found in `build-logic/src/main/kotlin/convention/`
- Properties files found in `build-logic/src/main/resources/META-INF/gradle-plugins/`
- Gradle commands complete without "Plugin not found" errors
- Module tasks are listed successfully

**❌ FAILURE indicators:**
- "Plugin [id: 'convention.kmp-library'] was not found"
- Build failures or compilation errors
- Missing plugin files

## Quick Diagnostic

If you want a quick check, just run:

```bash
cd "/Volumes/Projects/KMP project/LinkLibrary"
./gradlew :core:design:tasks
```

If this shows tasks instead of "Plugin not found" error, the convention plugins are working!

---

## For Claude Session Analysis

When sharing results with Claude, include:
- Full terminal output (success or failure)
- Any error messages (even partial)
- The exact commands you ran
- Your Android Studio/Gradle version info