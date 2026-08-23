package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPlugin
import org.jetbrains.kotlin.gradle.plugin.KspPlugin
import org.jetbrains.kotlin.gradle.targets.android.KotlinAndroidTarget

/**
 * Convention plugin for KSP and Room database configuration.
 * Applies KSP plugin and configures Room annotation processing
 * for Kotlin Multiplatform projects.
 */
class KspRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply KSP plugin
            pluginManager.apply(KspPlugin::class.java)

            // Configure KMP extension for Room
            extensions.configure<KotlinMultiplatformExtension> {
                // Configure Room for each target
                targets.withType(KotlinAndroidTarget::class.java).configureEach {
                    compilations.configureEach {
                        compileTaskProvider.configure {
                            compilerOptions {
                                // Room-specific compiler options
                                freeCompilerArgs.add("-opt-in=androidx.room.RoomDatabase")
                                freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
                            }
                        }
                    }
                }
            }

            // Configure KSP version
            extensions.getByType<org.jetbrains.kotlin.gradle.tasks.KspTask>().apply {
                // KSP configuration if needed
            }
        }
    }
}