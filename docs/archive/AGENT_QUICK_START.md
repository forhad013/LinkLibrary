# Quick Agent Prompt - Copy & Paste This

```
You are a Gradle build system expert. Investigate and fix the "Plugin not found" error for convention plugins in a Kotlin Multiplatform project.

PROJECT: /Volumes/Projects/KMP project/LinkLibrary

ISSUE: Plugin [id: 'convention.kmp-library'] was not found when running ./gradlew commands

CONTEXT:
- AGP 9 migration complete (JVM 21, context-parameters)
- Created convention plugins in build-logic/src/main/kotlin/convention/
- Added META-INF registration files for convention.kmp-library and convention.android-room
- Updated all modules to use id("convention.kmp-library")
- Root settings.gradle.kts includes includeBuild("build-logic")

FILES TO CHECK:
- build-logic/src/main/kotlin/convention/*.kt (plugin classes)
- build-logic/src/main/resources/META-INF/gradle-plugins/*.properties (registration)
- build-logic/build.gradle.kts (dependencies)
- settings.gradle.kts (includeBuild)

DIAGNOSTIC STEPS:
1. Verify plugin files exist and are correctly structured
2. Test: ./gradlew :build-logic:build --stacktrace
3. Test: ./gradlew :core:design:tasks --stacktrace  
4. Check for compilation errors, missing dependencies, or registration issues

EXPECTED RESULT:
- ./gradlew :core:design:tasks should show available tasks (not "Plugin not found")
- Convention plugins should be properly registered and applied
- AGP 9 migration features preserved

FIX & VERIFY:
1. Apply appropriate fix based on diagnosis
2. Test with ./gradlew :core:design:tasks
3. Verify build succeeds with ./gradlew clean build

Provide summary of root cause, fix applied, and verification results.
```