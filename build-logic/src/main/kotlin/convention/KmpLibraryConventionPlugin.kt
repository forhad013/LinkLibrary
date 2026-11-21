package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

/**
 * Convention plugin for Kotlin Multiplatform library modules.
 * Applies common plugins required for KMP + Compose + AGP 9 modules.
 */
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply required plugins for KMP + Compose + AGP 9
            apply(plugin = "org.jetbrains.kotlin.multiplatform")
            apply(plugin = "com.android.kotlin.multiplatform.library")
            apply(plugin = "org.jetbrains.compose")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")
            // Android Lint isn't built into the KMP library plugin; this adds each module's lintAndroidMain task.
            // No baseline here: lint errors in library modules fail the build, warnings don't
            apply(plugin = "com.android.lint")

            // Note: Android configuration and targets are configured in individual module build.gradle.kts files
            // This convention plugin only applies the required plugins
        }
    }
}