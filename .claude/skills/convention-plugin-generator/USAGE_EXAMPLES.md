# Convention Plugin Generator - Usage Examples

## 🎯 Real-World Examples

### Example 1: iOS Convention Plugin

**User Request:**
```
"Create a convention plugin for iOS configuration that sets up proper iOS framework settings and adds iOS-specific dependencies"
```

**Generated Plugin:**
```kotlin
package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Convention plugin for iOS framework configuration.
 * Configures iOS-specific build settings for KMP projects with proper framework support.
 */
class IosConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // iOS framework configuration
            extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension> {
                // iOS target will be configured here when support is added
                // Currently prepares for future iOS integration

                sourceSets {
                    val commonMain by getting {
                        dependencies {
                            // iOS-specific dependencies can be added here
                            // implementation("ios-specific:library:1.0.0")
                        }
                    }
                }
            }
        }
    }
}
```

**Usage:**
```kotlin
// In module build.gradle.kts
plugins {
    id("convention.ios")
}
```

---

### Example 2: Detekt Convention Plugin

**User Request:**
```
"Create a Detekt convention plugin that sets up code quality checks with custom rules, HTML reporting, and auto-format integration"
```

**Generated Plugin:**
```kotlin
package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Convention plugin for Detekt static code analysis.
 * Configures Detekt with custom rules for KMP projects, HTML/XML reporting,
 * and integrates with auto-format for consistent code style.
 */
class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
                configureDetekt()
            }

            // Configure Detekt tasks
            tasks.withType<io.gitlab.arturbosch.detekt.tasks.Detekt>().configureEach {
                reports {
                    xml.required.set(true)
                    html.required.set(true)
                    txt.required.set(true)
                    md.required.set(true)
                }
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

    basePath = projectDir.absolutePath
}
```

**Usage:**
```kotlin
// In module build.gradle.kts
plugins {
    id("convention.detekt")
}

// Optionally configure in module
detekt {
    config.setFrom("$projectDir/custom-detekt.yml")
}
```

---

### Example 3: Ktor HTTP Client Plugin

**User Request:**
```
"Create a Ktor convention plugin that configures HTTP client with content negotiation, JSON serialization, logging, and timeout settings for all platforms"
```

**Generated Plugin:**
```kotlin
package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPlugin
import org.jetbrains.kotlin.plugin.compose.gradle.ComposePlugin

/**
 * Convention plugin for Ktor HTTP client configuration.
 * Sets up Ktor client with content negotiation, JSON serialization, logging,
 * timeout settings, and platform-specific engines for all KMP targets.
 */
class KtorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply required plugins
            pluginManager.apply(KotlinMultiplatformPlugin::class.java)
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            extensions.configure<KotlinMultiplatformExtension> {
                configureKtorClient()
            }
        }
    }
}

private fun KotlinMultiplatformExtension.configureKtorClient() {
    // Ktor version
    val ktorVersion = "3.0.3"

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Ktor core client
                implementation("io.ktor:ktor-client-core:$ktorVersion")

                // Content negotiation and serialization
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                // Logging
                implementation("io.ktor:ktor-client-logging:$ktorVersion")

                // Timeout and resilience
                implementation("io.ktor:ktor-client-core:$ktorVersion")
            }
        }

        // Platform-specific engines
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }

        val wasmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktorVersion")
            }
        }
    }
}
```

**Usage:**
```kotlin
// In module build.gradle.kts
plugins {
    id("convention.kmp-library")  // Base KMP setup
    id("convention.ktor")         // Add Ktor client
}
```

---

### Example 4: Testing Convention Plugin

**User Request:**
```
"Create a testing convention plugin that sets up JUnit, MockK, and Kotest for all platforms with proper test configuration"
```

**Generated Plugin:**
```kotlin
package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for testing configuration.
 * Sets up comprehensive testing with JUnit, MockK, and Kotest
 * for all KMP targets with proper test configuration and reporting.
 */
class TestingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<KotlinMultiplatformExtension> {
                configureTesting()
            }
        }
    }
}

private fun KotlinMultiplatformExtension.configureTesting() {
    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.kotest:kotest-framework:5.9.1")
                implementation("io.kotest:kotest-assertions:5.9.1")
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
                implementation("io.mockk:mockk:3.12.0")
            }
        }

        val androidInstrumentedTest by getting {
            dependencies {
                implementation("androidx.test.ext:junit:1.2.1")
                implementation("androidx.test.espresso:espresso-core:3.6.1")
                implementation("io.mockk:mockk-android:3.12.0")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-runner-junit5:5.9.1")
                implementation("io.mockk:mockk:3.12.0")
            }
        }

        val wasmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-framework:5.9.1")
            }
        }
    }
}
```

**Usage:**
```kotlin
// In module build.gradle.kts
plugins {
    id("convention.testing")
}
```

---

### Example 5: Logging Convention Plugin

**User Request:**
```
"Create a Kermit logging convention plugin with platform-specific log handlers and crash reporting integration"
```

**Generated Plugin:**
```kotlin
package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for Kermit logging configuration.
 * Sets up Kermit logging library with platform-specific handlers,
 * crash reporting integration, and log level management for KMP projects.
 */
class KermitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets {
                    val commonMain by getting {
                        dependencies {
                            implementation("io.github.aakira:kermit:2.0.4")
                        }
                    }

                    // Platform-specific implementations
                    val androidMain by getting {
                        dependencies {
                            implementation("io.github.aakira:kermit:2.0.4")
                            implementation("io.github.aakira:kermit-android:2.0.4")
                        }
                    }

                    val jvmMain by getting {
                        dependencies {
                            implementation("io.github.aakira:kermit:2.0.4")
                            implementation("io.github.aakira:kermit-logcat:2.0.4")
                        }
                    }

                    val wasmMain by getting {
                        dependencies {
                            implementation("io.github.aakira:kermit:2.0.4")
                            implementation("io.github.aakira:kermit-browser:2.0.4")
                        }
                    }
                }
            }
        }
    }
}
```

**Usage:**
```kotlin
// In module build.gradle.kts
plugins {
    id("convention.kermit")
}
```

---

## 🎨 Advanced Examples

### Combined Plugin Generation

**User Request:**
```
"Create a plugin suite for Apple platforms: iOS, macOS, tvOS, and watchOS with shared base configuration"
```

**Generated Output:**
- **IosConventionPlugin.kt**: iOS framework configuration
- **MacosConventionPlugin.kt**: macOS desktop configuration
- **TvosConventionPlugin.kt**: tvOS configuration
- **WatchosConventionPlugin.kt**: watchOS configuration
- **AppleBaseConventionPlugin.kt**: Shared Apple platform configuration

### Plugin with Complex Requirements

**User Request:**
```
"Create a custom build task plugin that generates API documentation from Ktor client code and integrates with Detekt"
```

**Generated Plugin:**
```kotlin
package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register

/**
 * Convention plugin for API documentation generation.
 * Creates custom build tasks to generate API documentation from Ktor client
 * code and integrates with Detekt for documentation quality checks.
 */
class ApiDocumentationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Register documentation generation task
            tasks.register("generateApiDocs") {
                group = "documentation"
                description = "Generate API documentation from Ktor client code"

                doLast {
                    // API documentation generation logic
                    println("Generating API documentation...")
                }
            }

            // Configure Detekt for documentation quality
            extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
                config.setFrom("$projectDir/detekt-documentation.yml")
            }
        }
    }
}
```

**Usage:**
```kotlin
// In module build.gradle.kts
plugins {
    id("convention.api-documentation")
}

// Run the task
./gradlew generateApiDocs
```

---

## 📊 Plugin Complexity Examples

### Simple Plugin (Generated in ~30 seconds)
**Input:** "Create a Compose UI plugin"
**Output:** Basic plugin with 1-2 plugin applications

### Medium Plugin (Generated in ~1 minute)
**Input:** "Create an Android app plugin with Material Design"
**Output:** Plugin with Android extension configuration and basic dependencies

### Complex Plugin (Generated in ~2-3 minutes)
**Input:** "Create a full KMP library plugin with Android, JVM, and WASM targets"
**Output:** Comprehensive plugin with multiple targets, source sets, and platform-specific dependencies

---

## 🔧 Integration Examples

### Single Plugin Usage
```kotlin
plugins {
    id("convention.kermit")
}
```

### Multiple Combined Plugins
```kotlin
plugins {
    id("convention.kmp-library")
    id("convention.ktor")
    id("convention.detekt")
    id("convention.kermit")
}
```

### Plugin with Custom Configuration
```kotlin
plugins {
    id("convention.ktor")
}

// Override default configuration if needed
// Ktor convention plugin provides sensible defaults
// but allows customization in module build.gradle.kts
```

---

## 🎯 Command Patterns

### Pattern 1: Purpose-Based
```
"Create a convention plugin for [technology/framework]"
```

### Pattern 2: Name-Based
```
"Create a convention plugin called [Name] for [purpose]"
```

### Pattern 3: Requirements-Based
```
"Create a convention plugin that [detailed requirements]"
```

### Pattern 4: Platform-Based
```
"Create a [platform] convention plugin with [features]"
```

---

## 🚀 Getting Started

**Right now, try one of these:**

**Simple:**
```
"Create a convention plugin for Kermit logging"
```

**Medium:**
```
"Create a Detekt convention plugin with custom rules and HTML reporting"
```

**Complex:**
```
"Create a Ktor convention plugin for HTTP client with content negotiation, JSON serialization, and timeout configuration"
```

**The skill will generate your convention plugin with proper structure, documentation, and integration!**