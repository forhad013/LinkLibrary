#!/bin/bash

echo "🔧 Gradle Permission Fix Script"
echo "================================"

PROJECT_DIR="/Volumes/Projects/KMP project/LinkLibrary"
cd "$PROJECT_DIR" || exit 1

echo "📍 Current directory: $(pwd)"
echo ""

echo "1️⃣ Attempting to fix Gradle wrapper lock file..."

# Try to fix the specific lock file
LOCK_FILE="$HOME/.gradle/wrapper/dists/gradle-9.3.1-bin/23ovyewtku6u96viwx3xl3oks/gradle-9.3.1-bin.zip.lck"

if [ -f "$LOCK_FILE" ]; then
    echo "🔍 Found lock file: $LOCK_FILE"

    # Try multiple approaches to remove it
    echo "🛠️ Attempting to remove lock file..."

    # Approach 1: Direct remove
    rm -f "$LOCK_FILE" 2>/dev/null && echo "✅ Removed lock file with rm" && SUCCESS=true

    # Approach 2: Change permissions first
    if [ "$SUCCESS" != "true" ]; then
        chmod 777 "$LOCK_FILE" 2>/dev/null && rm -f "$LOCK_FILE" 2>/dev/null && echo "✅ Removed lock file with chmod + rm" && SUCCESS=true
    fi

    # Approach 3: Remove extended attributes
    if [ "$SUCCESS" != "true" ]; then
        xattr -cr "$LOCK_FILE" 2>/dev/null && rm -f "$LOCK_FILE" 2>/dev/null && echo "✅ Removed lock file with xattr + rm" && SUCCESS=true
    fi

    if [ "$SUCCESS" != "true" ]; then
        echo "❌ Could not remove lock file automatically"
        echo "💡 Try running: sudo rm -f '$LOCK_FILE'"
    fi
else
    echo "✅ Lock file not found or already removed"
fi

echo ""
echo "2️⃣ Testing Gradle availability..."
echo "🛠️ Running: ./gradlew --version"
echo ""

./gradlew --version

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ SUCCESS! Gradle is working."
    echo ""
    echo "🚀 You can now run your build:"
    echo "   ./gradlew clean build"
    echo ""
    echo "🧪 Or test individual modules:"
    echo "   ./gradlew :core:design:assembleDebug"
    echo "   ./gradlew :app:assembleDebug"
else
    echo ""
    echo "❌ Gradle still having issues."
    echo ""
    echo "💡 RECOMMENDED SOLUTION:"
    echo "   1. Restart your computer (this clears all file locks)"
    echo "   2. Then run: ./gradlew clean build"
    echo ""
    echo "📋 See GRADLE_PERMISSION_FIX.md for more solutions"
fi