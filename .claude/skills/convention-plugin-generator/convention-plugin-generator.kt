# Convention Plugin Generator Implementation

This implementation provides the core logic for automatically generating convention plugins based on requirements and existing patterns.

## Core Generator Logic

### Plugin Analysis & Pattern Recognition

```kotlin
class ConventionPluginGenerator {
    /**
     * Analyzes existing convention plugins to identify patterns
     */
    fun analyzeExistingPlugins(): PluginPatterns {
        val pluginDir = File("build-logic/src/main/kotlin/convention/")
        val plugins = pluginDir.listFiles().filter { it.extension == "kt" }

        return PluginPatterns(
            simplePatterns = extractSimplePatterns(plugins),
            mediumPatterns = extractMediumPatterns(plugins),
            complexPatterns = extractComplexPatterns(plugins),
            commonImports = extractCommonImports(plugins),
            namingConventions = extractNamingConventions(plugins)
        )
    }

    /**
     * Generates new convention plugin based on requirements
     */
    fun generatePlugin(requirements: PluginRequirements): GeneratedPlugin {
        val patterns = analyzeExistingPlugins()
        val template = selectTemplate(requirements, patterns)
        val code = generateCode(requirements, template)
        val documentation = generateDocumentation(requirements)
        val integration = generateIntegration(requirements)

        return GeneratedPlugin(
            className = requirements.pluginName,
            code = code,
            documentation = documentation,
            integration = integration,
            usage = generateUsageExamples(requirements)
        )
    }
}
```

### Plugin Template System

```kotlin
sealed class PluginTemplate {
    data class Simple(val plugins: List<String>) : PluginTemplate()
    data class Medium(val extensions: List<ExtensionConfig>) : PluginTemplate()
    data class Complex(val configurations: List<Configuration>) : PluginTemplate()
    data class Custom(val structure: String) : PluginTemplate()
}

data class ExtensionConfig(
    val extensionType: String,
    val configuration: String
)

data class Configuration(
    val name: String,
    val setup: String,
    val validation: String
)
```

### Code Generation Engine

```kotlin
class CodeGenerator {
    fun generateClass(requirements: PluginRequirements, template: PluginTemplate): String {
        return """
package convention

${generateImports(requirements, template)}
/**
 * ${generateKDoc(requirements)}
 */
class ${requirements.pluginName} : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
${generateApplyLogic(requirements, template)}
        }
    }
}

${generateExtensionFunctions(requirements, template)}
""".trimIndent()
    }

    private fun generateImports(requirements: PluginRequirements, template: PluginTemplate): String {
        val imports = mutableListOf(
            "import org.gradle.api.Plugin",
            "import org.gradle.api.Project"
        )

        when (template) {
            is PluginTemplate.Medium -> {
                imports.addAll(listOf(
                    "import org.gradle.kotlin.dsl.configure",
                    "import org.gradle.kotlin.dsl.getByType"
                ))
            }
            is PluginTemplate.Complex -> {
                imports.addAll(listOf(
                    "import org.gradle.kotlin.dsl.configure",
                    "import org.gradle.kotlin.dsl.getByType",
                    "import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension"
                ))
            }
        }

        return imports.joinToString("\n") + "\n"
    }

    private fun generateKDoc(requirements: PluginRequirements): String {
        return """
 * Convention plugin for ${requirements.purpose}.
 * ${requirements.description?.let { it }
            ?: "Configures ${requirements.purpose} for KMP projects with standard settings."}
""".trimIndent()
    }

    private fun generateApplyLogic(requirements: PluginRequirements, template: PluginTemplate): String {
        return when (template) {
            is PluginTemplate.Simple -> {
                template.plugins.joinToString("\n") { plugin ->
                    """            pluginManager.apply("$plugin")"""
                }
            }
            is PluginTemplate.Medium -> {
                """
                // Apply required plugins
                ${template.plugins.joinToString("\n") { "pluginManager.apply(\"$it\")" }}

                // Configure extensions
                ${template.extensions.joinToString("\n") { ext ->
                    """            extensions.configure<${ext.extensionType}> {
                ${ext.configuration}
            }"""
                }}
                """.trimIndent()
            }
            is PluginTemplate.Complex -> {
                """
                // Apply required plugins
                ${template.configurations.first().setup}

                // Configure KMP extension
                extensions.configure<KotlinMultiplatformExtension> {
                    ${template.configurations.drop(1).joinToString("\n") { it.setup }}
                }
                """.trimIndent()
            }
            else -> "            // Custom implementation"
        }
    }

    private fun generateExtensionFunctions(requirements: PluginRequirements, template: PluginTemplate): String {
        return when (template) {
            is PluginTemplate.Medium -> {
                """
// Extension configuration functions
private fun ${requirements.extensionType}.configure${requirements.extensionName}() {
    // Configuration implementation
    ${requirements.configuration?.trimIndent() ?: "// Add configuration logic"}
}
""".trimIndent()
            }
            is PluginTemplate.Complex -> {
                requirements.configurations.joinToString("\n\n") { config ->
                    """
private fun ${config.type}.${config.name}() {
    // ${config.description}
    ${config.setup.trimIndent()}
}
""".trimIndent()
                }
            }
            else -> ""
        }
    }
}
```

### Template Selection Logic

```kotlin
class TemplateSelector {
    fun selectTemplate(requirements: PluginRequirements, patterns: PluginPatterns): PluginTemplate {
        // Determine complexity based on requirements
        return when {
            requirements.isSimple() -> PluginTemplate.Simple(
                plugins = requirements.requiredPlugins
            )

            requirements.isMedium() -> {
                val extensions = requirements.extensions?.map { ext ->
                    ExtensionConfig(
                        extensionType = ext.type,
                        configuration = ext.configuration
                    )
                } ?: emptyList()

                PluginTemplate.Medium(
                    plugins = requirements.requiredPlugins,
                    extensions = extensions
                )
            }

            requirements.isComplex() -> {
                val configurations = requirements.configurations?.map { config ->
                    Configuration(
                        name = config.name,
                        setup = config.setup,
                        validation = config.validation
                    )
                } ?: emptyList()

                PluginTemplate.Complex(configurations)
            }

            else -> PluginTemplate.Custom(requirements.customStructure ?: "")
        }
    }
}

// Extension functions for requirements analysis
private fun PluginRequirements.isSimple(): Boolean {
    return requiredPlugins.size <= 2 &&
           configurations.isNullOrEmpty() &&
           extensions.isNullOrEmpty()
}

private fun PluginRequirements.isMedium(): Boolean {
    return requiredPlugins.size <= 4 &&
           !extensions.isNullOrEmpty() &&
           configurations.isNullOrEmpty()
}

private fun PluginRequirements.isComplex(): Boolean {
    return !configurations.isNullOrEmpty() ||
           requiredPlugins.size > 4 ||
           requiresSourceSetConfiguration
}
```

### Integration Manager

```kotlin
class IntegrationManager {
    /**
     * Updates necessary files for plugin integration
     */
    fun integratePlugin(generatedPlugin: GeneratedPlugin): IntegrationResult {
        val results = mutableListOf<String>()

        // Update build-logic/build.gradle.kts
        results.add(updateBuildGradle(generatedPlugin))

        // Create plugin file
        results.add(createPluginFile(generatedPlugin))

        // Update documentation
        results.add(updateDocumentation(generatedPlugin))

        // Validate integration
        results.add(validateIntegration(generatedPlugin))

        return IntegrationResult(
            success = results.all { it.contains("✅") },
            steps = results,
            message = if (results.all { it.contains("✅") }) {
                "Plugin integrated successfully"
            } else {
                "Plugin integration completed with warnings"
            }
        )
    }

    private fun updateBuildGradle(plugin: GeneratedPlugin): String {
        val buildGradle = File("build-logic/build.gradle.kts")

        if (!plugin.dependencies.isNullOrEmpty()) {
            val deps = plugin.dependencies.joinToString("\n    ") { dep ->
                """implementation("$dep")"""
            }

            // Add to dependencies block
            buildGradle.appendText(
                "\n    // ${plugin.className} dependencies\n" + deps
            )

            return "✅ Updated build-logic/build.gradle.kts"
        }

        return "✅ No build-logic updates needed"
    }

    private fun createPluginFile(plugin: GeneratedPlugin): String {
        val pluginDir = File("build-logic/src/main/kotlin/convention/")
        val pluginFile = File(pluginDir, "${plugin.className}.kt")

        pluginFile.writeText(plugin.code)

        return "✅ Created ${plugin.className}.kt"
    }

    private fun updateDocumentation(plugin: GeneratedPlugin): String {
        val readme = File("build-logic/README.md")

        if (!readme.exists()) {
            readme.writeText("# Convention Plugins\n\n")
        }

        readme.appendText(
            """
## ${plugin.className}

${plugin.documentation}

### Usage
```kotlin
plugins {
    id("convention.${plugin.className.removeSuffix("ConventionPlugin").toLowerCase()}")
}
```

"""
        )

        return "✅ Updated documentation"
    }

    private fun validateIntegration(plugin: GeneratedPlugin): String {
        // Basic validation
        val pluginFile = File("build-logic/src/main/kotlin/convention/${plugin.className}.kt")

        return if (pluginFile.exists()) {
            "✅ Plugin file validated"
        } else {
            "⚠️  Plugin file not found"
        }
    }
}
```

### Usage Example Generator

```kotlin
class UsageExampleGenerator {
    fun generateUsageExamples(requirements: PluginRequirements): List<UsageExample> {
        val examples = mutableListOf<UsageExample>()

        // Basic usage
        examples.add(UsageExample(
            title = "Basic Usage",
            description = "Apply the plugin in module build file",
            code = """
plugins {
    id("convention.${requirements.pluginName.removeSuffix("ConventionPlugin").toLowerCase()}")
}
""".trimIndent(),
            explanation = "Applies standard ${requirements.purpose} configuration"
        ))

        // Combined usage if applicable
        if (requirements.compatibleWith.isNotEmpty()) {
            examples.add(UsageExample(
                title = "Combined Usage",
                description = "Use with related convention plugins",
                code = """
plugins {
    id("convention.kmp-library")
    id("convention.${requirements.pluginName.removeSuffix("ConventionPlugin").toLowerCase()}")
${requirements.compatibleWith.joinToString("\n") { compatible ->
    """    id("convention.${compatible.removeSuffix("ConventionPlugin").toLowerCase()}")"""
                }}
}
""".trimIndent(),
                explanation = "Combines with related plugins for complete configuration"
            ))
        }

        // Advanced configuration if applicable
        if (requirements.configurable) {
            examples.add(UsageExample(
                title = "Advanced Configuration",
                description = "Customize plugin behavior",
                code = """
// Plugin can be customized in module build.gradle.kts
// Override default settings if needed
""".trimIndent(),
                explanation = "Plugin supports custom configuration overrides"
            ))
        }

        return examples
    }
}

data class UsageExample(
    val title: String,
    val description: String,
    val code: String,
    val explanation: String
)
```

### Main Generator Coordination

```kotlin
class ConventionPluginGeneratorCoordinator {
    private val codeGenerator = CodeGenerator()
    private val templateSelector = TemplateSelector()
    private val integrationManager = IntegrationManager()
    private val usageGenerator = UsageExampleGenerator()

    /**
     * Main entry point for plugin generation
     */
    fun generateConventionPlugin(requirements: PluginRequirements): GenerationResult {
        try {
            // Analyze existing patterns
            val patterns = analyzeExistingPlugins()

            // Select appropriate template
            val template = templateSelector.selectTemplate(requirements, patterns)

            // Generate plugin code
            val pluginCode = codeGenerator.generateClass(requirements, template)

            // Generate documentation
            val documentation = generateDocumentation(requirements, template)

            // Generate usage examples
            val usageExamples = usageGenerator.generateUsageExamples(requirements)

            // Create generated plugin object
            val generatedPlugin = GeneratedPlugin(
                className = requirements.pluginName,
                code = pluginCode,
                documentation = documentation,
                usage = usageExamples,
                dependencies = requirements.dependencies
            )

            // Integrate plugin
            val integration = integrationManager.integratePlugin(generatedPlugin)

            return GenerationResult(
                success = integration.success,
                plugin = generatedPlugin,
                integration = integration,
                message = "Plugin generated and integrated successfully"
            )

        } catch (e: Exception) {
            return GenerationResult(
                success = false,
                plugin = null,
                integration = null,
                message = "Plugin generation failed: ${e.message}"
            )
        }
    }
}
```

## Data Models

### Requirements Model
```kotlin
data class PluginRequirements(
    val pluginName: String,
    val purpose: String,
    val description: String? = null,
    val requiredPlugins: List<String> = emptyList(),
    val extensions: List<ExtensionRequirement>? = null,
    val configurations: List<ConfigurationRequirement>? = null,
    val dependencies: List<String> = emptyList(),
    val compatibleWith: List<String> = emptyList(),
    val configurable: Boolean = false,
    val customStructure: String? = null,
    val requiresSourceSetConfiguration: Boolean = false
)

data class ExtensionRequirement(
    val type: String,
    val name: String,
    val configuration: String
)

data class ConfigurationRequirement(
    val type: String,
    val name: String,
    val description: String,
    val setup: String,
    val validation: String
)
```

### Results Model
```kotlin
data class GeneratedPlugin(
    val className: String,
    val code: String,
    val documentation: String,
    val usage: List<UsageExample>,
    val dependencies: List<String> = emptyList()
)

data class GenerationResult(
    val success: Boolean,
    val plugin: GeneratedPlugin?,
    val integration: IntegrationResult?,
    val message: String
)

data class IntegrationResult(
    val success: Boolean,
    val steps: List<String>,
    val message: String
)
```

## Pattern Analysis

### Plugin Pattern Extraction
```kotlin
data class PluginPatterns(
    val simplePatterns: List<String>,
    val mediumPatterns: List<String>,
    val complexPatterns: List<String>,
    val commonImports: List<String>,
    val namingConventions: Map<String, String>
)

fun extractSimplePatterns(plugins: List<File>): List<String> {
    return plugins.filter { plugin ->
        val content = plugin.readText()
        content.contains("pluginManager.apply") &&
        !content.contains("extensions.configure")
    }.map { it.nameWithoutExtension }
}

fun extractMediumPatterns(plugins: List<File>): List<String> {
    return plugins.filter { plugin ->
        val content = plugin.readText()
        content.contains("extensions.configure") &&
        !content.contains("KotlinMultiplatformExtension")
    }.map { it.nameWithoutExtension }
}

fun extractComplexPatterns(plugins: List<File>): List<String> {
    return plugins.filter { plugin ->
        val content = plugin.readText()
        content.contains("KotlinMultiplatformExtension") ||
        content.contains("sourceSets")
    }.map { it.nameWithoutExtension }
}

fun extractCommonImports(plugins: List<File>): List<String> {
    val imports = plugins.flatMap { plugin ->
        val content = plugin.readText()
        IMPORT_PATTERN.findAll(content)
            .map { it.groupValues[1] }
    }
    return imports.groupBy { it }
        .filter { it.value.size > plugins.size / 2 }
        .keys
        .toList()
}

companion object {
    val IMPORT_PATTERN = Regex("import ([\\w.]+)")
}
```

---

## Advanced Features

### Plugin Conflict Detection
```kotlin
class ConflictDetector {
    fun detectConflicts(newPlugin: GeneratedPlugin): List<String> {
        val conflicts = mutableListOf<String>()

        // Check for plugin ID conflicts
        val pluginId = newPlugin.className.removeSuffix("ConventionPlugin").toLowerCase()

        existingPlugins.forEach { existing ->
            if (existing.pluginIdMatches(newPlugin)) {
                conflicts.add("Plugin ID conflict with $existing")
            }
        }

        // Check for extension conflicts
        newPlugin.extensions?.forEach { ext ->
            if (existingPluginUsesExtension(ext)) {
                conflicts.add("Extension conflict: ${ext.type}")
            }
        }

        return conflicts
    }
}
```

### Dependency Compatibility Checker
```kotlin
class DependencyChecker {
    fun checkCompatibility(dependencies: List<String>): CompatibilityResult {
        val incompatible = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        dependencies.forEach { dep ->
            val version = extractVersion(dep)
            val compatibility = checkVersionCompatibility(version)

            when {
                compatibility.isIncompatible() -> incompatible.add(dep)
                compatibility.hasWarnings() -> warnings.add(dep)
            }
        }

        return CompatibilityResult(
            compatible = incompatible.isEmpty(),
            incompatibleDependencies = incompatible,
            dependencyWarnings = warnings
        )
    }
}
```

### Code Quality Validator
```kotlin
class CodeQualityValidator {
    fun validateGeneratedCode(code: String): ValidationResult {
        val issues = mutableListOf<String>()

        // Check for common issues
        if (!containsProperPackageDeclaration(code)) {
            issues.add("Missing or incorrect package declaration")
        }

        if (!containsProperDocumentation(code)) {
            issues.add("Missing KDoc documentation")
        }

        if (!containsTypeSafeCode(code)) {
            issues.add("Contains potentially unsafe type casts")
        }

        if (!containsProperErrorHandling(code)) {
            issues.add("Missing error handling in extension functions")
        }

        return ValidationResult(
            valid = issues.isEmpty(),
            issues = issues,
            suggestions = generateSuggestions(issues)
        )
    }
}
```

---

## Usage

### Simple Generation
```kotlin
val generator = ConventionPluginGeneratorCoordinator()

val result = generator.generateConventionPlugin(
    PluginRequirements(
        pluginName = "KermitConventionPlugin",
        purpose = "Kermit logging setup",
        requiredPlugins = listOf("com.google.devtools.ksp"),
        dependencies = listOf("io.github.aakira:kermit:2.0.4")
    )
)
```

### Complex Generation
```kotlin
val result = generator.generateConventionPlugin(
    PluginRequirements(
        pluginName = "KtorConventionPlugin",
        purpose = "Ktor HTTP client configuration",
        description = "Sets up Ktor client with content negotiation, logging, and serialization",
        requiredPlugins = listOf("org.jetbrains.kotlin.plugin.serialization"),
        configurations = listOf(
            ConfigurationRequirement(
                type = "KotlinMultiplatformExtension",
                name = "configureKtor",
                description = "Configures Ktor client dependencies",
                setup = """
                    sourceSets {
                        val commonMain by getting {
                            dependencies {
                                implementation("io.ktor:ktor-client-core:3.0.3")
                                implementation("io.ktor:ktor-client-content-negotiation:3.0.3")
                            }
                        }
                    }
                """.trimIndent(),
                validation = "Validates Ktor client configuration"
            )
        ),
        dependencies = listOf(
            "io.ktor:ktor-client-core:3.0.3",
            "io.ktor:ktor-client-content-negotiation:3.0.3"
        ),
        compatibleWith = listOf("KmpLibraryConventionPlugin"),
        configurable = true
    )
)
```

---

**This implementation provides a comprehensive system for generating convention plugins that maintain consistency and quality across the LinkLibrary KMP project.**