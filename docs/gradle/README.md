# Gradle Documentation

This directory contains comprehensive documentation for Gradle setup, AGP 9 migration, and build configuration for the Link Library KMP project.

## 📚 Documentation Index

### Migration & Setup
- **[AGP 9 Migration Guide](AGP_9_MIGRATION_GUIDE.md)** - Complete migration documentation from AGP 8.x to AGP 9.0, including all issues encountered and solutions implemented.

### Troubleshooting & Reference
- **[AGP 9 Troubleshooting Guide](AGP_9_TROUBLESHOOTING.md)** - Common issues, solutions, and diagnostic commands for AGP 9 problems.

## 🎯 Quick Reference

### Current Version Stack
```toml
[versions]
agp = "9.1.0"           # Android Gradle Plugin
kotlin = "2.4.10"        # Kotlin compiler
ksp = "2.3.11"           # Kotlin Symbol Processing
room = "2.8.4"           # Room KMP Database
compose-plugin = "1.12.0" # Compose Multiplatform
gradle = "9.3.1"         # Gradle wrapper
```

### Convention Plugins
- **KmpLibraryConventionPlugin** - For KMP modules using Compose
- **AndroidRoomConventionPlugin** - For KMP modules using Room (no Compose)

### Key Commands
```bash
# Test specific modules
./gradlew :database:build
./gradlew :core:design:build

# Full project build
./gradlew clean build

# Diagnostic commands
./gradlew :database:kspAndroidMain --info
./gradlew dependencies
```

## 🔧 Build Architecture

### Convention Plugin System
The project uses a build-logic module with convention plugins to standardize build configuration across modules:

```
build-logic/
├── build.gradle.kts              # Build logic dependencies
├── settings.gradle.kts           # Build logic settings
└── src/main/kotlin/convention/
    ├── KmpLibraryConventionPlugin.kt    # Compose KMP modules
    └── AndroidRoomConventionPlugin.kt    # Room KMP modules
```

### Module Convention Usage
- **Compose KMP Modules**: `app`, `bookmarks`, `core:design`, `core:utils`
- **Non-Compose KMP Modules**: `database`

### Version Catalog
All versions managed centrally in `gradle/libs.versions.toml`

## 📖 Learning Resources

### AGP 9 Specific
- [AGP 9 Release Notes](https://developer.android.com/build/releases/past-releases/agp-9-0)
- [Compose Compiler for Kotlin Multiplatform](https://developer.android.com/jetpack/androidx/releases/compose-compiler)
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)

### Tool-Specific
- [KSP (Kotlin Symbol Processing)](https://github.com/google/ksp)
- [Room KMP Database](https://developer.android.com/kotlin/multiplatform/room)
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)

## 🚀 Migration Status

✅ **AGP 9 Migration Complete** (2025-08-27)

**Migrated Components**:
- ✅ All KMP modules migrated to AGP 9.1.0
- ✅ Convention plugin architecture established
- ✅ Version alignment completed (Kotlin 2.4.10 + KSP 2.3.11)
- ✅ DSL syntax updated for AGP 9
- ✅ Compose compiler integration completed
- ✅ Room KSP configuration optimized

**Known Working Configuration**:
- AGP 9.1.0 + Kotlin 2.4.10 + KSP 2.3.11 ✅
- Compose Plugin 1.12.0 ✅
- Gradle 9.3.1 ✅

## 🛠️ Maintenance Guidelines

### Version Updates
1. **Check Compatibility**: Verify AGP/Kotlin/KSP compatibility matrix
2. **Update Version Catalog**: Update `gradle/libs.versions.toml`
3. **Update Build Logic**: Keep `build-logic/build.gradle.kts` aligned
4. **Test Incrementally**: Test one module before full migration
5. **Update Docs**: Keep documentation synchronized with versions

### Convention Plugin Changes
1. **Test in Isolation**: Create test project before applying to main
2. **Register Properly**: Add `gradlePlugin {}` registration
3. **Document Changes**: Update migration guide with changes
4. **Version Alignment**: Keep build-logic dependencies updated

## 📞 Support & Troubleshooting

### Common Issues
- Plugin resolution → Check convention plugin registration
- KSP errors → Verify version compatibility
- Compose errors → Check module convention plugin usage
- DSL errors → Verify AGP 9 syntax

### Diagnostic Commands
```bash
# Version check
./gradlew --version

# Plugin verification
./gradlew plugins

# Dependency analysis
./gradlew dependencies

# Detailed error output
./gradlew build --stacktrace --debug
```

### When to Seek Help
- Issue persists after trying troubleshooting steps
- Version combination not documented in guides
- Convention plugin behavior unexpected
- Performance degradation after changes

## 📝 Documentation Standards

### Creating New Documentation
1. Use clear, descriptive filenames
2. Include creation date and author
3. Cross-reference related documents
4. Provide code examples for complex topics
5. Include troubleshooting sections

### Updating Documentation
1. Update version numbers immediately after changes
2. Add new issues/solutions to troubleshooting guide
3. Update compatibility matrix with tested combinations
4. Maintain chronological order in migration guides
5. Review and update monthly

## 🎓 Best Practices

### Build Configuration
- Use convention plugins for shared configuration
- Keep individual build files simple
- Version all dependencies via catalog
- Test after each version change

### Troubleshooting
- Start with version compatibility checks
- Use diagnostic commands before making changes
- Test one module at a time
- Document new issues and solutions

### Documentation
- Keep guides current with project state
- Use examples for complex concepts
- Cross-reference related documentation
- Maintain change history

---
**Documentation Maintained**: 2025-08-27  
**Project**: Link Library KMP  
**Current AGP Version**: 9.1.0  
**Migration Branch**: agp9-migration