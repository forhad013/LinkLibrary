# Java 21 Installation Guide for AGP 9 Migration

## 🎯 Objective
Install Java 21 to enable automated build testing for the Android Gradle Plugin 9.0 migration.

## 📋 Why Java 21 is Required
- AGP 9.0 requires Java 21+ for optimal compatibility
- Current project uses JVM_21 target configuration
- Build tools and KSP work best with latest Java versions
- JetBrains Compose compiler requires Java 21+

## 🚀 Installation Methods

### **Method 1: Homebrew (Recommended for macOS)**
```bash
# Install Microsoft OpenJDK 21 (most compatible with Android development)
brew install --cask microsoft-openjdk@21

# Verify installation
java -version
```

**Expected Output:**
```
openjdk version "21.0.x" 2024-xx-xx
OpenJDK Runtime Environment Microsoft-... (build 21.0.x+xx)
OpenJDK 64-Bit Server VM Microsoft-... (build 21.0.x+xx, mixed mode)
```

### **Method 2: Manual Download (Alternative)**
```bash
# Download Microsoft OpenJDK 21 for macOS
# Visit: https://learn.microsoft.com/en-us/java/openjdk/download#openjdk-21

# Or use Amazon Corretto 21
brew install --cask corretto@21

# Verify installation
java -version
```

### **Method 3: SDKMAN! (Advanced Users)**
```bash
# Install SDKMAN if not already installed
curl -s "https://get.sdkman.io" | bash

# Install Java 21
sdk install java 21.0.1-ms

# Set as default
sdk default java 21.0.1-ms

# Verify
java -version
```

## ⚙️ JAVA_HOME Configuration

After installation, set JAVA_HOME environment variable:

```bash
# Find Java installation path
/usr/libexec/java_home -v 21

# Add to your shell profile (~/.zshrc or ~/.bash_profile)
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"

# Reload shell configuration
source ~/.zshrc  # or source ~/.bash_profile
```

## 🔍 Verification Steps

### **Step 1: Verify Java Version**
```bash
java -version
javac -version
echo $JAVA_HOME
```

### **Step 2: Verify Gradle Recognition**
```bash
cd "/Volumes/Projects/KMP project/LinkLibrary"
./gradlew --version
```

Expected output should show:
```
------------------------------------------------------------
Gradle 9.1.0
------------------------------------------------------------
Kotlin: 2.3.20
Groovy: 4.0.21
Ant: Apache Ant(TM) version 1.10.14 compiled on January 2 2024
JVM: 21.0.x (vendor...)
OS: Mac OS X ... (arm64/x86_64)
```

### **Step 3: Test Build Compatibility**
```bash
cd "/Volumes/Projects/KMP project/LinkLibrary"
./gradlew clean
./gradlew --version
```

## 🐛 Troubleshooting Common Issues

### **Issue 1: Multiple Java Versions Installed**
```bash
# List all installed Java versions
/usr/libexec/java_home -V

# Force use Java 21 for this session
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"
```

### **Issue 2: Gradle Still Using Old Java**
```bash
# Clean Gradle cache
./gradlew clean --no-daemon

# Kill existing Gradle daemon
./gradlew --stop

# Try build again
./gradlew build
```

### **Issue 3: Homebrew Permission Issues**
```bash
# Fix Homebrew permissions
sudo chown -R $(whoami) /usr/local/bin /usr/local/lib
brew doctor

# Retry installation
brew install --cask microsoft-openjdk@21
```

## 🚀 Next Steps After Java Installation

Once Java 21 is installed and verified, run the automated build test:

```bash
cd "/Volumes/Projects/KMP project/LinkLibrary"
./test-build.sh
```

This script will:
- ✅ Verify Java installation
- ✅ Test all individual modules
- ✅ Validate KSP Room database generation
- ✅ Test WASM compilation
- ✅ Run full build test
- ✅ Provide detailed error reporting

## 📊 Success Criteria

The build migration is successful when:
1. Java 21 is recognized by `java -version`
2. All 5 modules compile successfully
3. KSP generates Room database implementations without errors
4. WASM target compiles without dependency issues
5. Full `./gradlew build` command completes successfully

## 🔗 Additional Resources

- [AGP 9 Migration Guide](https://developer.android.com/build/publishing/bync-resolution)
- [Kotlin Multiplatform Room Documentation](https://developer.android.com/kotlin/multiplatform/room)
- [Java 21 Release Notes](https://openjdk.org/projects/jdk/21/)

---

**Last Updated:** 2025-08-26
**AGP Migration Branch:** agp9-migration
**Priority:** Automated Build Testing Setup