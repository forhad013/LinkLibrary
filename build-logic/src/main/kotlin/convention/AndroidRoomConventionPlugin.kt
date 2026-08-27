package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

/**
 * Convention plugin for Room KMP database modules.
 * Applies the required plugins for Room database development.
 */
class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply required plugins for KMP + Room + KSP
            apply(plugin = "org.jetbrains.kotlin.multiplatform")
            apply(plugin = "com.android.kotlin.multiplatform.library")
            apply(plugin = "com.google.devtools.ksp")
            apply(plugin = "androidx.room")

            // Note: Room and KSP configuration should be done in individual module build.gradle.kts files
            // This convention plugin only applies the required plugins
        }
    }
}