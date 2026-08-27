#!/bin/bash
# Automated Build Testing Script for AGP 9 Migration
# This script tests the build process and reports results

set -e  # Exit on error

echo "🚀 Starting AGP 9 Migration Build Test"
echo "========================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✅ $2${NC}"
    else
        echo -e "${RED}❌ $2${NC}"
    fi
}

# Check Java installation
echo "📋 Checking Java installation..."
if java -version 2>&1 | grep -q "version"; then
    JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
    echo "Java version: $JAVA_VERSION"
    if [ "$JAVA_VERSION" -lt 21 ]; then
        echo -e "${YELLOW}⚠️  Java 21+ recommended, current version may not work${NC}"
    fi
else
    echo -e "${RED}❌ Java not found. Please install Java 21+ first${NC}"
    echo "Run: brew install --cask microsoft-openjdk@21"
    exit 1
fi

print_status 0 "Java check completed"
echo ""

# Clean previous builds
echo "🧹 Cleaning previous builds..."
./gradlew clean
print_status 0 "Build clean completed"
echo ""

# Test individual modules
echo "🔍 Testing individual modules..."
MODULES=("database" "core:design" "core:utils" "bookmarks" "app")

for module in "${MODULES[@]}"; do
    echo "Testing module: $module"
    if ./gradlew :$module:compileKotlinMetadata; then
        print_status 0 "Module $module compilation successful"
    else
        print_status 1 "Module $module compilation failed"
    fi
done

echo ""

# Test database KSP generation (critical for AGP 9)
echo "🔍 Testing KSP Room database generation..."
if ./gradlew :database:build --info 2>&1 | grep -q "BUILD SUCCESSFUL"; then
    print_status 0 "KSP Room database generation successful"
else
    print_status 1 "KSP Room database generation failed"
    echo "📋 Checking for KSP-related errors..."
    ./gradlew :database:build --info 2>&1 | grep -i "error\|failed" | head -5
fi

echo ""

# Test WASM compilation (was failing before)
echo "🔍 Testing WASM compilation..."
if ./gradlew :app:compileWasmJsKotlinMetadata; then
    print_status 0 "WASM compilation successful"
else
    print_status 1 "WASM compilation failed"
fi

echo ""

# Final full build test
echo "🎯 Running full build test..."
if ./gradlew build --warning-mode all; then
    print_status 0 "Full build successful! 🎉"
    echo ""
    echo "📊 Build Summary:"
    echo "  • AGP 9 Migration: ✅ Complete"
    echo "  • KSP Configuration: ✅ Working"
    echo "  • All Modules: ✅ Building successfully"
    echo "  • WASM Target: ✅ Compiling"
    exit 0
else
    print_status 1 "Full build failed"
    echo ""
    echo "🔧 Troubleshooting Steps:"
    echo "1. Check individual module errors above"
    echo "2. Verify Java version (21+ required)"
    echo "3. Check KSP compatibility issues"
    echo "4. Review AGP 9 migration steps"
    exit 1
fi