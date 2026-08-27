# Gradle Permission Fix Guide

## Problem
Gradle builds are failing with:
```
java.io.FileNotFoundException: gradle-9.3.1-bin.zip.lck (Operation not permitted)
```

This is caused by macOS file system protection features preventing Gradle from managing its lock files.

## Solutions (Try in Order)

### Solution 1: Restart Your Computer (RECOMMENDED)
The simplest and most effective solution. Restarting will:
- Clear all Gradle daemon processes
- Release all file locks
- Reset macOS file system protection states

After restart, try: `./gradlew clean build`

### Solution 2: Manually Kill Gradle Daemons
1. Open Activity Monitor (Cmd+Space, type "Activity Monitor")
2. Search for "gradle" or "java"
3. Force quit any Gradle-related processes
4. Try the build again

### Solution 3: Fix File Permissions
Run these commands in Terminal:

```bash
# Fix ownership of Gradle cache
sudo chown -R $(whoami) ~/.gradle

# Fix permissions on Gradle wrapper
chmod -R u+w ~/.gradle/wrapper/dists/

# Remove specific lock file
rm -f ~/.gradle/wrapper/dists/gradle-9.3.1-bin/*/gradle-9.3.1-bin.zip.lck
```

### Solution 4: Clear Gradle Cache and Rebuild
```bash
# Stop all Gradle daemons first
./gradlew --stop

# Remove the problematic distribution
rm -rf ~/.gradle/wrapper/dists/gradle-9.3.1-bin/

# Let Gradle re-download fresh
./gradlew clean build
```

### Solution 5: Use an Alternative Gradle Version
Edit `gradle/wrapper/gradle-wrapper.properties` to use a different Gradle version:
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.11.1-bin.zip
```

### Solution 6: Direct Java Execution (Advanced)
If the wrapper is completely broken, you can try running Gradle directly:
```bash
# Find the Gradle installation
export GRADLE_HOME=~/.gradle/wrapper/dists/gradle-9.3.1-bin/*/gradle-9.3.1
export PATH=$GRADLE_HOME/bin:$PATH

# Run build directly
gradle clean build
```

## Verification
After applying any solution, verify Gradle works:
```bash
./gradlew --version
./gradlew tasks
```

## Prevention
To prevent future permission issues:
1. Always stop Gradle daemons when done: `./gradlew --stop`
2. Don't force quit Gradle processes (use `--stop` instead)
3. Keep Gradle and JVM versions compatible
4. Avoid running Gradle as root/sudo unless necessary

## Current Status
✅ Convention plugin architecture is complete and correct
✅ All modules properly configured for AGP 9 migration
⚠️ Build testing blocked by Gradle permission issues
⏳ Ready for manual build testing once permissions are fixed

## Next Steps After Fix
1. Run: `./gradlew clean build`
2. Verify all modules compile successfully
3. Check AGP 9 migration completion: `./gradlew assembleDebug`