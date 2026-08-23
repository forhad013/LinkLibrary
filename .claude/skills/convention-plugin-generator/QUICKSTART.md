# Convention Plugin Generator - Quick Start Guide

## 🚀 Quick Start

### Basic Usage

**Simple Plugin Creation:**
```
"Create a convention plugin for Kermit logging"
```

**With More Details:**
```
"Create a convention plugin called KermitConventionPlugin that sets up Kermit logging with platform-specific handlers"
```

**Complex Plugin:**
```
"Generate a convention plugin for Ktor HTTP client with content negotiation, logging, and timeout configuration"
```

---

## 🎯 Plugin Types

### Platform Plugins
- **iOS**: iOS-specific build configuration
- **Windows**: Windows desktop configuration
- **Linux**: Linux desktop configuration
- **macOS**: macOS desktop configuration

### Feature Plugins
- **Logging**: Kermit, Timber setup
- **HTTP Client**: Ktor, Retrofit configuration
- **Database**: Room, SQLDelight enhancements
- **Testing**: JUnit, MockK, test frameworks
- **Code Quality**: Detekt, ktlint setup
- **DI Setup**: Koin, Hilt configuration
- **Navigation**: Compose Navigation setup
- **Performance**: Performance monitoring

### Build Plugins
- **Compiler Options**: Advanced compiler configuration
- **Build Performance**: Build time optimizations
- **Resource Management**: Asset and resource handling
- **Code Shrinking**: ProGuard/R8 configuration

---

## 📝 Examples

### Example 1: Simple Logging Plugin
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

### Example 2: Code Quality Plugin
```
"Create a convention plugin for Detekt with custom rules and HTML reporting"
```

**Generated Plugin:**
```kotlin
package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Convention plugin for Detekt static code analysis.
 * Configures Detekt with custom rules for KMP projects and integrates with build lifecycle.
 */
class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
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
        }
    }
}
```

### Example 3: HTTP Client Plugin
```
"Create a convention plugin for Ktor HTTP client with logging, serialization, and timeout configuration"
```

**Generated Plugin:**
```kotlin
package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for Ktor HTTP client configuration.
 * Sets up Ktor client with content negotiation, logging, serialization, and timeout settings.
 */
class KtorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            extensions.configure<KotlinMultiplatformExtension> {
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

                            // Timeout configuration
                            implementation("io.ktor:ktor-client-core:3.0.3")
                        }
                    }

                    // Platform-specific engines
                    val androidMain by getting {
                        dependencies {
                            implementation("io.ktor:ktor-client-okhttp:3.0.3")
                        }
                    }

                    val jvmMain by getting {
                        dependencies {
                            implementation("io.ktor:ktor-client-cio:3.0.3")
                        }
                    }

                    val wasmMain by getting {
                        dependencies {
                            implementation("io.ktor:ktor-client-js:3.0.3")
                        }
                    }
                }
            }
        }
    }
}
```

---

## 🎨 Customization Options

### Plugin Properties
- **Name**: Plugin class name (e.g., `DetektConventionPlugin`)
- **Purpose**: What the plugin configures
- **Complexity**: Simple, Medium, or Complex
- **Dependencies**: Required Gradle plugins and libraries
- **Extensions**: Which Gradle extensions to configure
- **Compatibility**: Works with existing plugins

### Advanced Options
```
"Create a convention plugin that:
- Configures Detekt with custom rules
- Generates HTML and XML reports
- Integrates with build lifecycle
- Is compatible with existing KMP plugins"
```

---

## 🔧 Integration Steps

### 1. Plugin Generation
The skill automatically:
- ✅ Creates plugin file in `build-logic/src/main/kotlin/convention/`
- ✅ Updates `build-logic/build.gradle.kts` if needed
- ✅ Generates documentation
- ✅ Creates usage examples

### 2. Usage in Modules
Apply in module `build.gradle.kts`:
```kotlin
plugins {
    id("convention.your-plugin-name")
}
```

### 3. Verification
```bash
./gradlew tasks
```
Should show your plugin is available and working.

---

## 📊 Plugin Complexity Levels

### Simple Plugin (1-2 plugins, minimal config)
**Example:** Compose setup
```kotlin
class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.compose")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        }
    }
}
```

### Medium Plugin (3-4 plugins, some extensions)
**Example:** KSP + Room
```kotlin
class KspRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(KspPlugin::class.java)

            extensions.configure<KotlinMultiplatformExtension> {
                // Room-specific configuration
            }
        }
    }
}
```

### Complex Plugin (5+ plugins, multiple extensions)
**Example:** Full KMP Library
```kotlin
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Multiple plugins
            pluginManager.apply(KotlinMultiplatformPlugin::class.java)

            // Multiple extensions
            extensions.configure<KotlinMultiplatformExtension> { /* KMP config */ }
            extensions.configure<LibraryExtension> { /* Android config */ }

            // Source sets configuration
            sourceSets { /* dependencies */ }
        }
    }
}
```

---

## 🎯 Best Practices

### Naming Conventions
- **Plugin Class**: `{Purpose}ConventionPlugin` (e.g., `KermitConventionPlugin`)
- **Plugin ID**: `convention.{purpose}` (e.g., `convention.kermit`)
- **File Name**: `{Purpose}ConventionPlugin.kt`

### Code Structure
- **Package**: `package convention`
- **Imports**: Grouped and organized
- **Documentation**: Comprehensive KDoc comments
- **Extension Functions**: Logical separation of concerns

### Configuration
- **Sensible Defaults**: Most common configuration
- **Override Options**: Allow customization
- **Type Safety**: Proper Kotlin typing
- **Error Handling**: Graceful degradation

---

## 🚀 Advanced Usage

### Combined Plugin Generation
```
"Create a convention plugin suite: iOS, tvOS, and watchOS configuration plugins with shared base configuration"
```

### Plugin with Dependencies
```
"Create a convention plugin for Apollo GraphQL that depends on Ktor convention plugin"
```

### Custom Configuration
```
"Create a convention plugin with custom build task generation for code generation"
```

---

## 📝 Generated Documentation

Each plugin comes with:

**KDoc Documentation:**
```kotlin
/**
 * Convention plugin for [purpose].
 * [detailed description]
 */
```

**Usage Guide:**
```kotlin
plugins {
    id("convention.plugin-name")
}
```

**Integration Notes:**
- Compatible plugins
- Dependency requirements
- Configuration options
- Troubleshooting tips

---

## 🎉 Benefits

### Time Savings
- **Instant Generation**: No manual plugin creation
- **Pattern Consistency**: Follows existing conventions
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

## 🔧 Troubleshooting

### Common Issues

**Issue:** Plugin not found after generation
**Solution:** Run `./gradlew --refresh-dependencies`

**Issue:** Compilation errors
**Solution:** Check dependencies in `build-logic/build.gradle.kts`

**Issue:** Configuration not applying
**Solution:** Verify plugin compatibility with existing plugins

---

## 🚀 Ready to Generate?

**Simple start:**
```
"Create a convention plugin for [purpose]"
```

**Detailed start:**
```
"Generate a convention plugin called [Name] that does [purpose] with [requirements]"
```

**Complex start:**
```
"Create a comprehensive convention plugin suite for [area] with [specific features]"
```

---

**Start generating your convention plugins now! Just describe what you need and the skill will handle the rest.**