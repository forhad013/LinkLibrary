# Convention Plugin Generator Skill

**Purpose:** Automatically generate new convention plugins for the LinkLibrary KMP project following established patterns and best practices.

**Usage:** `/generate-convention-plugin [plugin-name]` or invoke this skill when you need a new convention plugin.

---

## 🎯 Overview

This skill automates the creation of new convention plugins by:
- Analyzing existing plugin patterns and structure
- Generating new plugins based on requirements
- Following established naming conventions and code style
- Updating necessary configuration files
- Providing documentation and usage examples

---

## 🚀 How to Use

### Simple Invocation
```
"Create a convention plugin for [purpose]"
```

**Examples:**
- "Create a convention plugin for iOS configuration"
- "Generate a convention plugin for Detekt code quality"
- "Create a convention plugin for Ktor HTTP client setup"

### Advanced Invocation
```
"Generate a convention plugin called [PluginName] that [purpose] with [specific requirements]"
```

**Examples:**
- "Generate a convention plugin called DetektConventionPlugin that sets up Detekt with custom rules for KMP projects"
- "Create a convention plugin called KtorConventionPlugin that configures HTTP client with logging and serialization"

---

## 🎨 Supported Plugin Types

### Platform-Specific Plugins
- **iOS Configuration** - iOS-specific build settings
- **Windows Desktop** - Windows-specific configurations
- **Linux Desktop** - Linux-specific configurations
- **macOS Desktop** - macOS-specific configurations

### Feature-Specific Plugins
- **Code Quality** - Detekt, ktlint, lint checks
- **HTTP Client** - Ktor, Retrofit configuration
- **Database** - Room, SQLDelight enhancements
- **Testing** - JUnit, MockK, test frameworks
- **DI Setup** - Koin, Hilt configuration
- **Logging** - Kermit, Timber setup
- **Navigation** - Compose Navigation setup
- **Performance** - Performance monitoring and optimization

### Build Optimization Plugins
- **Compiler Options** - Advanced compiler configuration
- **Build Performance** - Build time optimizations
- **Resource Management** - Asset and resource handling
- **ProGuard/R8** - Code shrinking and obfuscation

---

## 📋 Plugin Generation Process

### 1. Requirement Analysis
The skill analyzes your requirements and determines:
- Plugin type and category
- Required dependencies and plugins
- Configuration complexity
- Integration points with existing plugins

### 2. Pattern Recognition
Analyzes existing convention plugins to identify:
- Package structure and naming conventions
- Common configuration patterns
- Extension configuration approaches
- Dependencies and imports usage

### 3. Plugin Generation
Creates new convention plugin with:
- Proper class structure and inheritance
- Required imports and dependencies
- Configuration logic
- Documentation and comments
- Usage examples

### 4. Integration Updates
Updates necessary files:
- `build-logic/build.gradle.kts` - Add new dependencies
- Plugin registration and availability
- Documentation updates

---

## 🔧 Plugin Structure

### Standard Plugin Template
```kotlin
package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

/**
 * Convention plugin for [PURPOSE].
 * [Detailed description of what the plugin does]
 */
class [PluginName]ConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply required plugins
            pluginManager.apply("[plugin-id]")

            // Configure extensions
            extensions.configure<[ExtensionType]> {
                // Configuration logic
            }
        }
    }
}

// Extension functions for configuration
private fun [ExtensionType].configure[ExtensionName]() {
    // Configuration implementation
}
```

---

## 📝 Configuration Options

### Plugin Properties
- **Name**: Plugin class name (e.g., `DetektConventionPlugin`)
- **Purpose**: What the plugin configures
- **Dependencies**: Required Gradle plugins and libraries
- **Extensions**: Which Gradle extensions to configure
- **Complexity**: Simple/Medium/Complex

### Generation Options
- **Include Documentation**: Generate comprehensive documentation
- **Add Examples**: Create usage examples
- **Update Config**: Update build configuration files
- **Create Tests**: Generate test templates

---

## 🎯 Examples

### Example 1: Simple Plugin
```
"Create a convention plugin for Kermit logging setup"
```

**Generated Plugin:**
```kotlin
package convention

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin for Kermit logging configuration.
 * Sets up Kermit logging library for KMP projects with platform-specific log handlers.
 */
class KermitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            // Configure Kermit dependencies
            extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension> {
                sourceSets {
                    val commonMain by getting {
                        dependencies {
                            implementation("io.github.aakira:kermit:2.0.4")
                        }
                    }
                }
            }
        }
    }
}
```

### Example 2: Medium Plugin
```
"Create a convention plugin for Detekt with custom rules"
```

**Generated Plugin:**
```kotlin
package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

/**
 * Convention plugin for Detekt static code analysis.
 * Configures Detekt with custom rules for KMP projects and integrates with build lifecycle.
 */
class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
                configureDetekt()
            }
        }
    }
}

private fun io.gitlab.arturbosch.detekt.extensions.DetektExtension.configureDetekt() {
    toolVersion = "1.23.7"
    config.setFrom("$projectDir/detekt.yml")

    buildUponDefaultConfig = true

    all {
        reports {
            xml.required.set(true)
            html.required.set(true)
            txt.required.set(true)
        }
    }
}
```

### Example 3: Complex Plugin
```
"Create a convention plugin for Ktor HTTP client with logging, serialization, and timeout configuration"
```

**Generated Plugin:**
```kotlin
package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

/**
 * Convention plugin for Ktor HTTP client configuration.
 * Sets up Ktor client with content negotiation, logging, serialization, and timeout settings.
 */
class KtorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply required plugins
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension> {
                sourceSets {
                    val commonMain by getting {
                        dependencies {
                            // Ktor core
                            implementation("io.ktor:ktor-client-core:3.0.3")

                            // Content negotiation
                            implementation("io.ktor:ktor-client-content-negotiation:3.0.3")
                            implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.3")

                            // Logging
                            implementation("io.ktor:ktor-client-logging:3.0.3")

                            // Platform engines
                            @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
                            when (targetName) {
                                "android" -> {
                                    implementation("io.ktor:ktor-client-okhttp:3.0.3")
                                }
                                "jvm" -> {
                                    implementation("io.ktor:ktor-client-cio:3.0.3")
                                }
                                "wasm" -> {
                                    implementation("io.ktor:ktor-client-js:3.0.3")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
```

---

## 🔍 Pattern Analysis

### Existing Plugin Patterns

**Simple Pattern** (Compose, Testing):
```kotlin
class SimpleConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("plugin-id")
            // Minimal configuration
        }
    }
}
```

**Medium Pattern** (Android, Ksp):
```kotlin
class MediumConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("plugin-id")
            extensions.configure<ExtensionType> {
                // Standard configuration
            }
        }
    }
}
```

**Complex Pattern** (KMP Library):
```kotlin
class ComplexConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(MultiplePlugins)
            extensions.configure<Extension1> { /* config */ }
            extensions.configure<Extension2> { /* config */ }
            sourceSets { /* dependencies */ }
        }
    }
}
```

---

## 🎨 Configuration Templates

### Template 1: Platform Configuration
```kotlin
// For platform-specific plugins (iOS, Windows, etc.)
class PlatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<PlatformExtension> {
                configurePlatform()
            }
        }
    }
}
```

### Template 2: Feature Configuration
```kotlin
// For feature plugins (logging, testing, etc.)
class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("feature-plugin")
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets {
                    val commonMain by getting {
                        dependencies {
                            implementation("feature-library")
                        }
                    }
                }
            }
        }
    }
}
```

### Template 3: Build Optimization
```kotlin
// For build optimization plugins
class OptimizationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.withType<CompileTask>().configureEach {
                // Optimization logic
            }
        }
    }
}
```

---

## 📊 Usage Examples

### In Module Build Files
```kotlin
plugins {
    id("convention.detekt")          // Code quality
    id("convention.ktor")             // HTTP client
    id("convention.kermit")           // Logging
    id("convention.compose")          // Compose setup
}
```

### Combined Usage
```kotlin
plugins {
    id("convention.kmp-library")      // Base KMP setup
    id("convention.ktor")             // Add HTTP client
    id("convention.detekt")           // Add code quality
}
```

---

## 🔧 Integration Points

### Files Updated
1. **`build-logic/build.gradle.kts`** - Add new plugin dependencies
2. **`build-logic/settings.gradle.kts`** - Repository updates if needed
3. **Plugin file** - Created in `build-logic/src/main/kotlin/convention/`
4. **Documentation** - Usage examples and configuration guides

### Dependencies Managed
- Gradle plugin coordinates
- Library versions and compatibility
- Platform-specific implementations
- Transitive dependencies

---

## 🚨 Quality Checks

### Before Generation
- ✅ Checks for existing plugin with same name
- ✅ Validates plugin naming conventions
- ✅ Verifies dependency availability
- ✅ Ensures compatibility with existing plugins

### After Generation
- ✅ Validates generated code syntax
- ✅ Checks imports and dependencies
- ✅ Verifies extension usage
- ✅ Tests compilation if requested

---

## 📈 Best Practices Applied

### Code Quality
- **Consistent Style**: Follows existing code patterns
- **Documentation**: Comprehensive KDoc comments
- **Type Safety**: Proper Kotlin typing and nullability
- **Extension Functions**: Logical separation of concerns

### Architecture
- **Single Responsibility**: Each plugin has one clear purpose
- **Reusability**: Common patterns extracted to functions
- **Configurability**: Sensible defaults with override options
- **Compatibility**: Works with existing plugin ecosystem

### Maintenance
- **Clear Naming**: Self-documenting class and function names
- **Minimal Dependencies**: Only required dependencies
- **Version Management**: Centralized version handling
- **Testing Support**: Easy to test and maintain

---

## 🎯 Common Use Cases

### Adding New Platform Support
```
"Create a convention plugin for tvOS target"
```

### Adding New Technology
```
"Create a convention plugin for Apollo GraphQL client"
```

### Adding Build Features
```
"Create a convention plugin for code coverage with JaCoCo"
```

### Adding Quality Tools
```
"Create a convention plugin for dependency health checks"
```

---

## 🔮 Advanced Features

### Plugin Discovery
Analyzes existing plugins to suggest improvements or find conflicts.

### Dependency Resolution
Automatically resolves compatible versions for dependencies.

### Pattern Optimization
Identifies opportunities to consolidate or simplify plugin configurations.

### Integration Testing
Generates test projects to validate plugin functionality.

---

## 📚 Documentation Generated

Each plugin comes with:
- **KDoc Documentation**: Class and method documentation
- **Usage Guide**: How to use the plugin
- **Configuration Examples**: Common configuration patterns
- **Integration Notes**: How it works with other plugins
- **Troubleshooting**: Common issues and solutions

---

## 🎉 Benefits

### Time Savings
- **Instant Generation**: No manual plugin creation
- **Pattern Consistency**: Follows established conventions
- **Best Practices**: Built-in quality and compatibility

### Code Quality
- **Type Safety**: Proper Kotlin typing
- **Documentation**: Comprehensive inline docs
- **Maintainability**: Clean, readable code

### Integration
- **Seamless**: Works with existing plugins
- **Compatible**: Follows Gradle best practices
- **Extensible**: Easy to customize and extend

---

## 🚀 Getting Started

**Basic Usage:**
```
"Create a convention plugin for [purpose]"
```

**Advanced Usage:**
```
"Generate a convention plugin called [Name] that [detailed requirements]"
```

**Custom Configuration:**
```
"Create a convention plugin with [specific options]"
```

---

**This skill makes it easy to extend your build system with new convention plugins while maintaining consistency and quality across your KMP project!**