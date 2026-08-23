package convention

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin for Compose Multiplatform configuration.
 * Applies Compose plugin and configures common Compose settings
 * for Kotlin Multiplatform projects.
 */
class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.compose")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            // Compose configuration is handled by the plugins
            // Additional Compose-specific configuration can be added here
        }
    }
}