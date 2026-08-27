#!/bin/bash

echo "🔧 Claude Build Test Script for AGP 9 Migration"
echo "=============================================="
echo "This script tests the convention plugin setup and reports results"
echo "Run this in Android Studio terminal and share the output with Claude"
echo ""

PROJECT_DIR="/Volumes/Projects/KMP project/LinkLibrary"
cd "$PROJECT_DIR" || exit 1

echo "📍 Project Directory: $(pwd)"
echo "🕐 Timestamp: $(date)"
echo ""

# Test 1: Check Gradle wrapper
echo "🧪 Test 1: Gradle Wrapper Status"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
if [ -f "gradlew" ]; then
    echo "✅ gradlew found"
    ls -la gradlew
else
    echo "❌ gradlew not found"
fi
echo ""

# Test 2: Check convention plugin files
echo "🧪 Test 2: Convention Plugin Files"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Plugin Classes:"
find build-logic/src/main/kotlin/convention -name "*.kt" -type f 2>/dev/null || echo "❌ No plugin classes found"
echo ""
echo "Plugin Registration Files:"
find build-logic/src/main/resources/META-INF/gradle-plugins -name "*.properties" -type f 2>/dev/null || echo "❌ No plugin registration files found"
echo ""

# Test 3: Check module build files
echo "🧪 Test 3: Module Build File Plugin References"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
for module in core/design core/utils database bookmarks app; do
    if [ -f "$module/build.gradle.kts" ]; then
        echo "📁 $module/build.gradle.kts:"
        grep "id(\"convention\." "$module/build.gradle.kts" || echo "  ❌ No convention plugin found"
    else
        echo "❌ $module/build.gradle.kts not found"
    fi
done
echo ""

# Test 4: Try Gradle version check
echo "🧪 Test 4: Gradle Version Check"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
./gradlew --version 2>&1 | head -10
echo ""

# Test 5: Check if build-logic compiles
echo "🧪 Test 5: Build-Logic Module Compilation"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
./gradlew :build-logic:build --stacktrace 2>&1 | tail -30
echo ""

# Test 6: Try to configure a simple module
echo "🧪 Test 6: Configure Core Design Module"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
./gradlew :core:design:tasks --stacktrace 2>&1 | tail -50
echo ""

# Test 7: Full project configuration
echo "🧪 Test 7: Full Project Configuration Test"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
./gradlew tasks --stacktrace 2>&1 | tail -50
echo ""

# Test 8: Try building a simple module
echo "🧪 Test 8: Build Core Design Module"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
./gradlew :core:design:assembleDebug --stacktrace 2>&1 | tail -50
echo ""

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📋 Test Summary"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Please share this entire output with Claude for analysis"
echo "Key sections to check:"
echo "1. Test 2 - Convention Plugin Files (should show .kt and .properties files)"
echo "2. Test 3 - Plugin References (should show convention.kmp-library etc)"
echo "3. Test 5-8 - Build attempts (check for any errors)"
echo ""
echo "🎯 For Claude: Focus on any ERROR or EXCEPTION messages in the output"